module com.enigmamachine.enigmawithgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.enigmamachine.enigmawithgui to javafx.fxml;
    exports com.enigmamachine.enigmawithgui;
}