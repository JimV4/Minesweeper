module com.example.minedemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.minedemo to javafx.fxml;
    exports com.example.minedemo;
}