module proyecto.todolist {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.json;
    requires com.jfoenix;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens proyecto.todolist to javafx.fxml;
    exports proyecto.todolist;

    exports proyecto.todolist.Capa_Presentacion.Controllers;
    opens proyecto.todolist.Capa_Presentacion.Controllers to javafx.fxml;

    exports proyecto.todolist.Capa_Presentacion.Vista;
    opens proyecto.todolist.Capa_Presentacion.Vista to javafx.fxml;

    // ✅ Solución: Permitir que Jackson acceda a las clases de datos
    exports proyecto.todolist.Capa_Datos;
    opens proyecto.todolist.Capa_Datos to com.fasterxml.jackson.databind;
}
