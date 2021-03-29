module com.Assignment1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.Assignment1 to javafx.fxml;
    exports com.Assignment1;
}