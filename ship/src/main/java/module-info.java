module ca.ship {
    requires javafx.controls;
    requires javafx.fxml;
    requires xstream;


    opens ca.ship to javafx.fxml;
    exports ca.ship;
    exports ca.ship.model;
    opens ca.ship.model to javafx.fxml,xstream;
    exports ca.ship.controllers;
    opens ca.ship.controllers to javafx.fxml;
}