module proyectoMorse {    
    requires java.base;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.web;
    requires javafx.media;
    requires javafx.fxml;

    opens proyectoMorse.controlador to javafx.fxml, javafx.base,javafx.controls,javafx.graphics,javafx.swing,javafx.web,javafx.media,java.base;
    exports proyectoMorse.controlador;
    
    opens proyectoMorse.modelo to javafx.fxml;
    exports proyectoMorse.modelo;
    
    
}