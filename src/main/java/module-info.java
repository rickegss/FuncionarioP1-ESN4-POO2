module ricardo.funcionariop1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens ricardo.funcionariop1 to javafx.fxml;
    exports ricardo.funcionariop1;
    exports ricardo.funcionariop1.controller;
    opens ricardo.funcionariop1.controller to javafx.fxml;
    opens ricardo.funcionariop1.model to javafx.base;
}