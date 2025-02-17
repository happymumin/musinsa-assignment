# Coordinator(무신사 백앤드 과제)

## 목차

1. [실행](#실행)
2. [빌드](#빌드)
3. [Swagger](#Swagger)
4. [DB 설계](#DB-설계)
5. [API Spec](#API-Spec)

## 기술 스택

언어

- Kotlin 1.9.25 (JDK 17)

프레임워크 및 라이브러리

- Spring Boot 3
- Spring WebFlux
- Spring Data JPA
- QueryDSL
- Kotlin Coroutine

Database

- H2

## 실행

```
./gradlew bootRun 
```

- 실행시 default로 dev 환경에서 실행됩니다.
- h2 db에 예제와 동일한 데이터가 자동으로 셋팅되어 API 호출시 데이터를 바로 확인해보실 수 있습니다.

## 빌드 & 테스트

```
 ./gradlew clean build
```

## Swagger

[http://localhost:5002/webjars/swagger-ui/index.html](http://localhost:5002/webjars/swagger-ui/index.html)

## DB 설계

### category 테이블

| Column | Type         | NOT NULL | Primary Key | Unique | Index |
|--------|--------------|----------|-------------|--------|-------|
| id     | VARCHAR(255) | ✅        | ✅           |        |       |
| name   | VARCHAR(255) | ✅        |             | ✅      |       |

### brand 테이블

| Column      | Type         | NOT NULL | Primary Key | Unique | Index |
|-------------|--------------|----------|-------------|--------|-------|
| id          | INT          | ✅        | ✅           |        |       |
| name        | VARCHAR(255) | ✅        |             |        |       |
| enabled     | BOOLEAN      | ✅        |             |        |       |
| created_at  | TIMESTAMP    | ✅        |             |        |       |
| modified_at | TIMESTAMP    | ✅        |             |        |       |

### product 테이블

| Column      | Type         | NOT NULL | Primary Key | Unique | Index               |
|-------------|--------------|----------|-------------|--------|---------------------|
| id          | INT          | ✅        | ✅           |        |                     |
| name        | VARCHAR(255) | ✅        |             |        |                     |
| price       | INT          | ✅        |             |        | ✅ (idx_price)       |
| status      | INT          | ✅        |             |        |                     |
| brand_id    | INT          | ✅        |             |        | ✅ (idx_brand_id)    |
| category_id | VARCHAR(255) | ✅        |             |        | ✅ (idx_category_id) |
| created_at  | TIMESTAMP    | ✅        |             |        |                     |
| modified_at | TIMESTAMP    | ✅        |             |        |                     |

- product 테이블을 기반으로 카테고리/브랜드별 최저가를 구하기 때문에 필요한 필드에 index를 추가해주었습니다.
- 상품은 판매중, 삭제 외 다른 상태도 추가될 수 있는 점을 고려해 `status`로 명명했습니다.

## API Spec

### [요구사항 1] 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API

Endpoint `GET /api/v1/stats/categories/min-price-brand`

Response Body

```json
{
  "categories": [
    {
      "categoryName": "상의",
      "brandName": "C",
      "minPrice": 10000
    },
    // 생략 
  ],
  "totalPrice": 34200
}
```

핵심 쿼리

```sql
SELECT
    p.id,
    p.brand_id,
    p.category_id,
    p.created_at,
    p.modified_at,
    p.name,
    p.price,
    p.status,
    b.name 
FROM
    product p 
JOIN
    brand b 
    ON p.brand_id = b.id 
    AND b.enabled = 1
WHERE
    p.category_id = {categoryId}
    AND p.status = 0
ORDER BY
    p.price 
LIMIT 1
```

설명

- 처음에는 브랜드/카테고리별 최저가 스냅샷을 RDBMS나 인메모리 DB에 저장하고 업데이트하여 stat 조회시에는 스냅샷을 조회하는 것을 고민했습니다.
- 하지만 최저가 상품이 수정, 삭제되거나 동시성 이슈 같은 엣지 케이스들을 모두 커버하는데 시간이 다소 소요될 것 같아 쿼리를 효율적이고 빠르게 조회하는 방향으로 다시 고민했습니다.
- 최종적으로 Coroutine을 사용하여 최대 3개 카테고리의 최저가를 동시에 조회하도록 구현했습니다.
- 상품이 삭제되는 경우나 브랜드가 삭제되는 경우를 고려했습니다.
- 참고로 카테고리는 추가/수정이 드물게 발생하고 데이터 수가 기하급수적으로 늘어나지 않을 것이라고 예상하고, 로컬 캐싱하여 데이터를 조회하도록 구현했습니다.
- 만약에 카테고리나 상품 수가 많아지게 된다면, 그때는 처음 고민했던 방법을 고도화하거나 실시간성이 중요한지 등 정책도 재검토해보는 등 다각도로 개선 방안을 모색하면 좋을 것 같습니다.

---
### [요구사항 2] 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API

Endpoint `GET /api/v1/stats/brands/min-total-price`

Response Body

```json
{
  "minPrice": {
    "brandName": "D",
    "minPricesWithCategory": [
      {
        "categoryName": "상의",
        "minPrice": 10100
      },
      // 생략
    ],
    "totalPrice": 36100
  }
}
```

핵심 쿼리

```sql
SELECT
    MIN(p.price),
    p.brand_id,
    p.category_id 
FROM
    product p 
JOIN
    brand b 
    ON p.brand_id = b.id 
    AND b.enabled = 0
WHERE p.status = 0
GROUP BY
    p.brand_id,
    p.category_id;
```

설명

- 브랜드와 카테고리 데이터 수는 상품 데이터 수에 비해서 현저히 적을 것이라고 예상했습니다.
  - 예상 데이터 수: 카테고리 < 브랜드 < 상품
- 때문에 브랜드X카테고리의 최저가를 구했을 때 데이터 수는 애플리케이션 로직으로 충분히 처리할 수 있을만한 데이터 수를 조회할 수 있을 것이라고 예상했습니다.

---
### [요구사항 3] 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API

Endpoint `GET /api/v1/stats/categories/{categoryName}/min-max-price-brand`

Response Body

```json
{
  "categoryName": "상의",
  "minPrice": {
    "brandName": "C",
    "price": 10000
  },
  "maxPrice": {
    "brandName": "I",
    "price": 11400
  }
}
```

핵심 쿼리

```sql
SELECT
    p.id,
    p.brand_id,
    p.category_id,
    p.created_at,
    p.modified_at,
    p.name,
    p.price,
    p.status,
    b.name 
FROM
    product p 
JOIN
    brand b 
    ON p.brand_id = b.id 
    AND b.enabled = 1
WHERE
    p.category_id = {categoryId}
    AND p.status = 0
ORDER BY
  p.price -- 최저가 asc, 최고가 desc
LIMIT 1
```

---
### [요구사항 4] 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API

#### 브랜드 추가

Endpoint `POST /api/v1/brands`

Request Body

```json
{
  "name": "브랜드 1"
}
```

Response Body

```json
{
  "id": 0
}
```

---

#### 브랜드 수정

Endpoint `PUT /api/v1/brands/{brandId}`

Request Body

```json
{
  "name": "브랜드 1"
}
```

---

#### 브랜드 삭제

Endpoint `DELETE /api/v1/brands/{brandId}`

---

#### 상품 추가

Endpoint `POST /api/v1/products`

Request Body

```json
{
  "name": "상품1",
  "price": 1000,
  "categoryId": "100",
  "brandId": 1
}
```

Response Body

```json
{
  "id": 0
}
```

---

#### 상품 수정

Endpoint `PUT /api/v1/products/{productId}`

Request Body

```json
{
  "name": "상품1",
  "price": 1000,
  "categoryId": "100",
  "brandId": 1
}
```

---

#### 상품 삭제

Endpoint `DELETE /api/v1/products/{productId}`

---

### 공통 Error Response 예시

```json
{
  "errorCode": "NOT_FOUND_BRAND",
  "reason": "존재하지 않는 브랜드입니다."
}
```


