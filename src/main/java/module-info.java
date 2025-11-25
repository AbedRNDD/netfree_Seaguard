module com.example.netfreeseaguard {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;

    opens com.example.netfreeseaguard to javafx.fxml;
    exports com.example.netfreeseaguard;
}