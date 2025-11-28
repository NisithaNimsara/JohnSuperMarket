  # JohnSuperMarket

This project involves creating a JavaFX-based GUI application for John’s Super Market to manage inventory and dealer operations efficiently. Alongside development, a detailed report must be submitted, covering design choices, implementation steps, testing processes, and compliance with software quality standards.

## Features

- 📦 **Inventory management**
  - Store item code, name, brand, price, quantity, category, purchase date, threshold, and image path.
  - Inventory data is persisted in a text file: `src/main/resources/Inventory/details.txt`.

- 🚨 **Low-stock overview**
  - Main page shows items that are at or below their threshold.
  - Helps quickly identify products that need restocking.

- ➕ **Add items**
  - Dedicated “Add Item” page with form fields and validation.
  - Optional image path for each item (used to display product images).

- ✏️ **Update items**
  - Update item details (e.g. price, quantity, threshold) from an “Update Item” page.

- 🗑️ **Delete items**
  - Remove items from inventory via a “Delete Item” page.

- 📋 **Inventory view**
  - Full inventory table with columns for code, name, brand, price, quantity, category, purchase date, and threshold.
  - Uses product images and cell formatting to improve readability.

- 🧾 **Dealers & their items**
  - Dealer list page showing name, contact and location.
  - Items-per-dealer page displaying products supplied by a selected dealer.
  - Dealer data stored in `src/main/resources/Dealers/DealerDetails.txt`.

- ✅ **Unit testing**
  - Basic JUnit 5 test for `DealerItem` model in `src/Tests/TestsFunctions/DealerItemTest.java`.

---

## Tech Stack

- **Language:** Java (configured for `source`/`target` 24 in `pom.xml`)
- **UI:** JavaFX (FXML-based views)
- **Build tool:** Maven
- **Testing:** JUnit 5 (Jupiter)

Module definition (simplified):

```java
module org.example.johnsupermarket {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.johnsupermarket to javafx.fxml;
    opens org.example.johnsupermarket.Models to javafx.base;

    exports org.example.johnsupermarket;
    exports org.example.johnsupermarket.Models;
}
```
## Project Structure
```
JohnSuperMarket/
├─ pom.xml                        # Maven configuration (JavaFX, JUnit, plugins)
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  ├─ module-info.java
│  │  │  └─ org/example/johnsupermarket
│  │  │     ├─ HelloApplication.java       # JavaFX application entry point
│  │  │     ├─ MainController.java         # Main dashboard + navigation
│  │  │     ├─ AddPageController.java      # Add item page
│  │  │     ├─ UpdatePageController.java   # Update item page
│  │  │     ├─ DeletePageController.java   # Delete item page
│  │  │     ├─ ViewinventryPageController.java
│  │  │     ├─ DealerItemsPageController.java
│  │  │     ├─ SelectdealersPageController.java
│  │  │     └─ Models
│  │  │        ├─ Item.java
│  │  │        ├─ Dealer.java
│  │  │        └─ DealerItem.java
│  │  └─ resources
│  │     ├─ Inventory/details.txt          # Inventory data
│  │     ├─ Dealers/DealerDetails.txt      # Dealer + dealer items data
│  │     ├─ org/example/johnsupermarket    # FXML views
│  │     │  ├─ main-view.fxml
│  │     │  ├─ addPage-view.fxml
│  │     │  ├─ updatePage-view.fxml
│  │     │  ├─ deletePage-view.fxml
│  │     │  ├─ viewINVENPage-view.fxml
│  │     │  ├─ dealerItemsPage-view.fxml
│  │     │  └─ randomDealersPage-view.fxml
│  │     └─ images                         # Product images used in UI
│  └─ Tests
│     └─ TestsFunctions/DealerItemTest.java
```
## Prerequisites
- JDK 24 (or adjust <source> / <target> in pom.xml to match your installed JDK)
-Maven 3.x
-JavaFX SDK is handled by Maven dependencies and the javafx-maven-plugin.
