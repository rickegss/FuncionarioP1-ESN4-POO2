module ricardo.funcionariop1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ricardo.funcionariop1 to javafx.fxml;
    exports ricardo.funcionariop1;
    exports ricardo.funcionariop1.controller;
    opens ricardo.funcionariop1.controller to javafx.fxml;
}