package proyecto.todolist.Capa_Presentacion.Vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/proyecto/todolist/fxml/login-view.fxml"));
        Parent root = fxmlLoader.load();

        // Configurar la escena
        Scene scene = new Scene(root, 400, 550);

        // Agregar la hoja de estilos (CSS)
        scene.getStylesheets().add(getClass().getResource("/proyecto/todolist/css/login.css").toExternalForm());

        // Configurar y mostrar la ventana
        stage.setTitle("Login - ToDo List");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
