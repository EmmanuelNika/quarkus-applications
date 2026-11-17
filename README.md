# Quarkus Inventory

An inventory service in [Quarkus](https://quarkus.io): items, stock receipts,
sales, and the part most inventory demos skip — **what the stock actually
cost**. It values each item five ways (weighted average, FIFO, LIFO,
highest-in-first-out and first-expiry-first-out), derives a selling price
from the item's markup, and sells batch- and serial-tracked stock correctly.

The same service is built twice to compare Quarkus's two reactive HTTP
stacks, alongside two small authentication starting points (Keycloak and
Okta). Part of [emmanuelnika.com](https://emmanuelnika.com/#projects).

## Modules

| Module | What it is |
| --- | --- |
| [`inventory-resteasy-reactive`](./inventory-resteasy-reactive) | **The full service.** RESTEasy Reactive + Hibernate ORM Panache on MySQL, with the cost queries on the reactive MySQL client: items, receiving, sales, cost and selling-price calculators, OpenAPI/Swagger UI. |
| [`inventory-reactive`](./inventory-reactive) | The same domain on **Reactive Routes** (Vert.x `@Route`): items and stock receipts, with integration tests. Useful side by side with the module above. |
| [`keycloak-authorization`](./keycloak-authorization) | Starting point for **multi-tenant Keycloak** in a SaaS app: a `Business` tenant entity and Keycloak Dev Services with a bundled realm (`config/quarkus-realm.json`). |
| [`okta-authorization`](./okta-authorization) | Starting point for **Okta** sign-in: Quarkus OIDC configured as a web app against an Okta developer org. |

The two authorization modules are independent templates; they are not wired
into the inventory services.

## What the inventory service does

**Items** — create, read, update and delete inventory items, each with a
name, barcode, type, a returnable flag and the markup used for pricing.

**Transactions** — every receipt and sale is an `InventoryActivity`:

- `POST /transactions/receive` records stock in at its unit cost. Stock can
  be untracked, tracked by batch (with an expiry date) or by serial numbers.
- `POST /transactions/sale` takes stock out: untracked stock at average cost,
  batch stock from the named batch, serial stock by serial number.

**Cost calculator** — the current unit cost of an item under each method,
computed in SQL from its activity history:

| Method | Endpoint |
| --- | --- |
| Weighted average | `GET /cost-calculator/average/items/{id}` |
| First in, first out (FIFO) | `GET /cost-calculator/first-in-first-out/items/{id}` |
| Last in, first out (LIFO) | `GET /cost-calculator/last-in-first-out/items/{id}` |
| Highest in, first out (HIFO) | `GET /cost-calculator/highest-in-first-out/items/{id}` |
| First expiry, first out (FEFO) | `GET /cost-calculator/first-expiry-first-out/items/{id}` |

**Selling-price calculator** — the same five methods under
`/selling-calculator/…`, applying the item's markup to the cost.

All paths sit under the `/inventory-api` root. The full, browsable API is in
Swagger UI (below).

## Stack

- Quarkus 2.16 (RESTEasy Reactive, Reactive Routes, Quarkus OIDC)
- Hibernate ORM with Panache for the full service's writes; cost queries on the reactive MySQL client with Mutiny `Uni`
- Hibernate Reactive with Panache in the Reactive Routes module — non-blocking end to end
- Hibernate Validator for request payloads
- SmallRye OpenAPI + Swagger UI
- Keycloak Dev Services, Okta OIDC

## Run it

Requirements: JDK 11+ and a MySQL server on `localhost:3306` (or Docker to
run one). The Maven wrapper is included.

1. Create the databases each module expects — for the inventory services
   `inventory_dev` (dev mode) and `inventory_test` (tests).
2. Set the database user and password in the module's
   `src/main/resources/application.properties`.
3. Start a module in dev mode:

   ```shell
   ./mvnw -pl inventory-resteasy-reactive quarkus:dev
   ```

4. Open Swagger UI at <http://localhost:8081/inventory-api/q/swagger-ui/>.

Both inventory modules use port 8081 in dev mode, so run one at a time. Tests:
`./mvnw -pl inventory-reactive test`.

Hibernate creates and updates the schema on start (`database.generation=update`
in dev, `drop-and-create` in tests).

## Contributing

See [CONTRIBUTING](CONTRIBUTING.md).
