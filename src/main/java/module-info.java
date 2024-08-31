module com.mycompany.drsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.drsystem.Controller to javafx.fxml;
    opens com.mycompany.drsystem.Model to javafx.base;
    exports com.mycompany.drsystem;
}
