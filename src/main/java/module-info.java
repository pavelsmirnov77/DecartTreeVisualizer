module com.example.decarttree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.decarttree to javafx.fxml;
    exports com.example.decarttree;
}