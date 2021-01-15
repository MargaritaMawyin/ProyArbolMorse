module com.mycompany.proyarbolmorse {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.proyarbolmorse to javafx.fxml;
    exports com.mycompany.proyarbolmorse;
}