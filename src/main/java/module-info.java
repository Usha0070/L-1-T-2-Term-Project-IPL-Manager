module com.example.demonew {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.example.demonew.updatedClient to javafx.fxml;
    opens com.example.demonew.updatedViewer to javafx.fxml;
    opens com.example.demonew.server to javafx.fxml;
    opens com.example.demonew.login to javafx.fxml;
    opens com.example.demonew to javafx.fxml;

    exports com.example.demonew;
    exports com.example.demonew.server;
    exports com.example.demonew.login;
    exports com.example.demonew.updatedClient;
    exports com.example.demonew.updatedViewer;
    exports com.example.demonew.util;
}
