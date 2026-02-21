module com.example.miniproject1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.miniproject1 to javafx.fxml;
    exports com.example.miniproject1;
}