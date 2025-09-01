module org.example.johnsupermarket {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.johnsupermarket to javafx.fxml;
    opens org.example.johnsupermarket.Models to javafx.base;

    exports org.example.johnsupermarket;
    exports org.example.johnsupermarket.Models;
}