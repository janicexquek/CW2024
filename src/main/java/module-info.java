module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.prefs;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.overlay to javafx.fxml;
    opens com.example.demo.mainmenu to javafx.fxml;
    opens com.example.demo.display to javafx.fxml;
    opens com.example.demo.plane to javafx.fxml;
    opens com.example.demo.projectile to javafx.fxml;
    opens com.example.demo.shield to javafx.fxml;
    opens com.example.demo.level to javafx.fxml;
    opens com.example.demo.gamemanager to javafx.fxml;
    opens com.example.demo.levelview to javafx.fxml;
    opens com.example.demo.styles to javafx.fxml;
    opens com.example.demo.mainmenumanager to javafx.fxml;
}