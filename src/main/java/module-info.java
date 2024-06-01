module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.prefs;
    requires javafx.swing;


    exports main;
    exports analysis;
    exports setting;
    exports pet;

    opens main to javafx.fxml;
    opens analysis to javafx.fxml;
    opens setting to javafx.fxml;
    opens pet to javafx.fxml;

}
