module com.example.social_networks {
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
    requires org.jgrapht.core;
    requires org.jgrapht.ext;
    requires java.desktop;

    opens com.example.social_networks to javafx.fxml;
    exports com.example.social_networks;
}
