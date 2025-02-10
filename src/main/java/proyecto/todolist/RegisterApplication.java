package proyecto.todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class RegisterApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/todolist/fxml/register-view.fxml"));
            Parent root = loader.load();

            // Configurar la escena
            Scene scene = new Scene(root);

            // Agregar la hoja de estilos (CSS)
            scene.getStylesheets().add(RegisterApplication.class.getResource("/proyecto/todolist/css/register.css").toExternalForm());

            // Configurar y mostrar la ventana
            primaryStage.setScene(scene);
            primaryStage.setTitle("Registro - ToDO-LIST");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
