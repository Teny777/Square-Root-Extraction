module com.example.squarerootextraction {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.squarerootextraction to javafx.fxml;
    exports com.example.squarerootextraction;
}