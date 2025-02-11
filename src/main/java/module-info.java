module proyecto.todolist {
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

    opens proyecto.todolist to javafx.fxml;
    exports proyecto.todolist;

    exports proyecto.todolist.Capa_Presentacion.Controllers;
    opens proyecto.todolist.Capa_Presentacion.Controllers to javafx.fxml;

    exports proyecto.todolist.Capa_Presentacion.Vista;
    opens proyecto.todolist.Capa_Presentacion.Vista to javafx.fxml;
}
