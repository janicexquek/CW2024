module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.prefs;
    requires com.google.gson; // Add this line

    opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.overlay to javafx.fxml;
    opens com.example.demo.mainmenu to javafx.fxml;
    opens com.example.demo.display to javafx.fxml;
    opens com.example.demo.plane to javafx.fxml;
    opens com.example.demo.projectile to javafx.fxml;
}