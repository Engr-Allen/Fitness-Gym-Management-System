module com.allen.fgms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;
    requires fontawesomefx;

    opens com.allen.fgms to javafx.fxml;
    exports com.allen.fgms;

    exports com.allen.fgms.Controller to javafx.fxml;
    opens com.allen.fgms.Controller to javafx.fxml;

    exports com.allen.fgms.Controller.Admin to javafx.fxml;
    opens com.allen.fgms.Controller.Admin to javafx.fxml, javafx.base;

    exports com.allen.fgms.Model to javafx.fxml;
    opens com.allen.fgms.Model to javafx.base, javafx.fxml;
}
