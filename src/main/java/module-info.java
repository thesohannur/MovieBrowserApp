module com.lab.moviebrowserapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires org.controlsfx.controls;

    opens com.lab.moviebrowserapplication to javafx.fxml;
    opens com.lab.moviebrowserapplication.controllers to javafx.fxml;
    opens com.lab.moviebrowserapplication.models to javafx.base;

    exports com.lab.moviebrowserapplication;
}