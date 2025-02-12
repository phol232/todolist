package proyecto.todolist.Capa_Presentacion.Vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class CrearTareaApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/todolist/fxml/crearTarea-view.fxml"));
            Parent root = loader.load();

            // Definir el tamaño de la ventana 350x500 px
            Scene scene = new Scene(root, 300, 460);
            primaryStage.setTitle("Crear Tarea");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Bloquear el cambio de tamaño
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al cargar la vista CrearTarea.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
