package proyecto.todolist.Capa_Presentacion.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> menuList;
    @FXML private MenuButton priorityButton;
    @FXML private TextField searchInput;
    @FXML private Button searchButton;
    @FXML private Button createTaskButton;
    @FXML private TableView<Object> taskTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome back, User!");

        ObservableList<String> items = FXCollections.observableArrayList(
                "☰ ALL TASKS",
                "📅 CALENDAR",
                "⚙️ SETTINGS"
        );
        menuList.setItems(items);  // Esto asigna la lista de opciones al ListView
    }


    private void setupEventHandlers() {
        // ✅ Configurar menú de prioridad dinámicamente
        MenuItem highPriority = new MenuItem("Alta 🔴");
        MenuItem mediumPriority = new MenuItem("Media 🟡");
        MenuItem lowPriority = new MenuItem("Baja 🟢");

        highPriority.setOnAction(event -> setPriorityHigh());
        mediumPriority.setOnAction(event -> setPriorityMedium());
        lowPriority.setOnAction(event -> setPriorityLow());

        priorityButton.getItems().addAll(highPriority, mediumPriority, lowPriority);

        // ✅ Evento para el botón de búsqueda
        searchButton.setOnAction(event -> performSearch());

        // ✅ Evento para el botón de creación de tareas
        createTaskButton.setOnAction(event -> openCreateTaskWindow());
    }

    @FXML
    private void setPriorityHigh() {
        priorityButton.setText("Alta 🔴");
    }

    @FXML
    private void setPriorityMedium() {
        priorityButton.setText("Media 🟡");
    }

    @FXML
    private void setPriorityLow() {
        priorityButton.setText("Baja 🟢");
    }

    @FXML
    private void performSearch() {
        String query = searchInput.getText().trim();
        if (query.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Búsqueda Vacía", "Por favor, ingresa un término de búsqueda.");
        } else {
            System.out.println("Buscando tareas con: " + query);
            showAlert(Alert.AlertType.INFORMATION, "Búsqueda", "Buscando tareas con: " + query);
        }
    }

    @FXML
    private void openCreateTaskWindow() {
        System.out.println("Abriendo ventana de creación de tareas...");
        showAlert(Alert.AlertType.INFORMATION, "Crear Tarea", "Función para abrir el formulario de creación de tareas.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
