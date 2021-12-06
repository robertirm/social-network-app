module com.codebase.socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.codebase.socialnetwork to javafx.fxml;
    exports com.codebase.socialnetwork;

    opens com.codebase.socialnetwork.controller to javafx.fxml;
    exports com.codebase.socialnetwork.controller;
    exports com.codebase.socialnetwork.service;
    exports com.codebase.socialnetwork.domain;

}