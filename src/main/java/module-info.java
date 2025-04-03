module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires java.desktop;
    requires commons.math3;
    requires jdk.javadoc;


    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
}