# Critical Issues Report

This document captures critical issues discovered in the inventory-gen codebase, along with impact analysis and recommended fixes.

> Note: Verified on 2026-03-25. The codebase has been updated and this issue list is now used for verification and regression tracking.

> Status: All previously reported issues have been resolved in code. Kept for post-fix audit.

## 1. Incorrect `SalesHistory.quantitySold` semantics in `InventoryService`

**Status: Resolved**

Location:
- `src/main/java/com/prodnretail/inventory_gen/service/InventoryService.java`

Location:
- `src/main/java/com/prodnretail/inventory_gen/service/InventoryService.java`

Problem:
- `restockProduct(...)` sets `SalesHistory.quantitySold = quantity` when restocking.
- `sellProduct(...)` sets `SalesHistory.quantitySold = -quantity` when selling.

Why it’s critical:
- This is opposite of expected sales tracking behavior. It corrupts historical data and can break reporting/analytics.

Suggested fix:
- For `sellProduct`: set `quantitySold = quantity` (positive sale count).
- For `restockProduct`: do not create `SalesHistory` records at all (or set `quantitySold = 0` and/or introduce separate restock audit entity).

## 2. Missing input validation in `restockProduct` and endpoints

**Status: Resolved**

Location:
- `InventoryService.restockProduct(...)`
- `InventoryController.restock(...)` and `sell(...)`

Problem:
- `restockProduct(...)` accepts zero/negative quantity.
- `sellProduct(...)` validates `quantity <= 0` but not `restockProduct`.

Why it’s critical:
- Negative restock can reduce inventory unexpectedly, creating inflation/deflation issues and allowing invalid states.

Suggested fix:
- Add check `if(quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");` in `restockProduct`.
- Consider `@RequestParam` validation (e.g., `@Min(1)` with `@Validated`).

## 3. Debug console logging left in production code

**Status: Resolved**

Location:
- `InventoryService.restockProduct(...)` and `sellProduct(...)`
- `System.out.println("Before saving sales history")`, etc.

Problem:
- Uses `System.out.println` for debug logs.

Why it’s critical:
- Pollutes logs; not thread-safe; not environment-configurable.

Suggested fix:
- Replace with SLF4J logger (`private static final Logger log = LoggerFactory.getLogger(InventoryService.class);`) and use `log.debug(...)` or remove.

## 4. Potential NPE or invalid value handling for inventory price fields

**Status: Resolved**

Location:
- `InventoryService.toDto()` total inventory value uses `p.getCostPrice() * p.getQuantity()` without null checks.

Problem:
- If `costPrice` is `null`, this throws NPE.

Why it’s critical:
- Breaks summary API under malformed data.

Suggested fix:
- Null-safe handling: `(p.getCostPrice() == null ? 0 : p.getCostPrice())`.

## 5. Missing `@ExceptionHandler` for `IllegalArgumentException`

**Status: Resolved**

Location:
- `GlobalExceptionHandler.java`

Problem:
- Controllers and services can throw `IllegalArgumentException`, but it is uncaught by the global handler.

Why it’s critical:
- Could expose stack traces or return 500 instead of 400.

Suggested fix:
- Add handler mapping `IllegalArgumentException` to `HttpStatus.BAD_REQUEST`.

## 6. `SalesHistoryService.getAllSales()` uses `s.getId()` as `SalesDataDTO.productId`

**Status: Resolved**

Location:
- `src/main/java/com/prodnretail/inventory_gen/service/SalesHistoryService.java`

Problem:
- `getAllSales()` sets `new SalesDataDTO(s.getId(), ...)` instead of using referenced product ID.

Why it’s critical:
- Produces incorrect product IDs in reporting UI.

Suggested fix:
- Use `s.getProduct().getId()`.

---

## Additional recommendations
- Add unit/integration tests for stock transitions and history tracking.
- Add API request validation on DTO fields using Bean Validation (`@NotNull`, `@Min`, etc.).
- Improve `StockStatus` synchronization by calculating status once and updating on product changes.
- Add data migration/cleanup for existing incorrect sales history records prior to deploy.
