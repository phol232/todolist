package proyecto.todolist.Capa_Presentacion.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import proyecto.todolist.Capa_Conexion.TareaService;
import proyecto.todolist.Capa_Datos.TareaModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> menuList;
    @FXML private MenuButton priorityButton;
    @FXML private TextField searchInput;
    @FXML private Button searchButton;
    @FXML private Button createTaskButton;

    // 🔹 Columnas de la tabla de tareas
    @FXML private TableView<TareaModel> taskTable;
    @FXML private TableColumn<TareaModel, String> colNombre;
    @FXML private TableColumn<TareaModel, String> colDescripcion;
    @FXML private TableColumn<TareaModel, String> colCategoria;
    @FXML private TableColumn<TareaModel, String> colPrioridad;
    @FXML private TableColumn<TareaModel, String> colStatus;
    @FXML private TableColumn<TareaModel, String> colFecha;
    @FXML private TableColumn<TareaModel, Void> colAcciones;

    private final TareaService tareaService = new TareaService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome back, User!");

        ObservableList<String> items = FXCollections.observableArrayList(
                "☰ ALL TASKS",
                "📅 CALENDAR",
                "⚙️ SETTINGS"
        );
        menuList.setItems(items);

        setupEventHandlers();
        configurarColumnas();
        cargarTareasEnTabla();
    }

    private void setupEventHandlers() {
        // 🔹 Configurar menú de prioridad dinámicamente
        MenuItem highPriority = new MenuItem("Alta 🔴");
        MenuItem mediumPriority = new MenuItem("Media 🟡");
        MenuItem lowPriority = new MenuItem("Baja 🟢");

        highPriority.setOnAction(event -> setPriorityHigh());
        mediumPriority.setOnAction(event -> setPriorityMedium());
        lowPriority.setOnAction(event -> setPriorityLow());

        priorityButton.getItems().addAll(highPriority, mediumPriority, lowPriority);

        // 🔹 Eventos para botones
        searchButton.setOnAction(event -> performSearch());
        createTaskButton.setOnAction(event -> openCreateTaskWindow());
    }

    @FXML
    private void setPriorityHigh() { priorityButton.setText("Alta 🔴"); }
    @FXML
    private void setPriorityMedium() { priorityButton.setText("Media 🟡"); }
    @FXML
    private void setPriorityLow() { priorityButton.setText("Baja 🟢"); }

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/proyecto/todolist/FXML/crearTarea-view.fxml"));
            Parent root = fxmlLoader.load();

            CrearTareaController crearTareaController = fxmlLoader.getController();
            crearTareaController.setOnTaskSaved(() -> Platform.runLater(this::cargarTareasEnTabla)); // ✅ Ejecutar en UI thread

            Stage stage = new Stage();
            stage.setTitle("Crear Nueva Tarea");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de creación de tareas.");
        }
    }


    private void configurarColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria")); // ✅ Usa el nombre real de la categoría
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        // ✅ Configurar columna de acciones con botones
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnCompletar = new Button("Completar");
            private final Button btnEliminar = new Button("Eliminar");

            {
                // 🔹 Estilos para los botones
                btnEditar.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                btnCompletar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                // 🔹 Eventos para los botones
                btnEditar.setOnAction(event -> {
                    TareaModel tarea = getTableView().getItems().get(getIndex());

                });

                btnCompletar.setOnAction(event -> {
                    TareaModel tarea = getTableView().getItems().get(getIndex());
                    if (tarea != null) {
                        tareaService.editarTarea(tarea.getIdTarea(), tarea.getTitulo(), tarea.getDescripcion(),
                                tarea.getCategoria(), tarea.getPrioridad(), "✅ Completado"); // ✅ Actualiza estado
                        cargarTareasEnTabla(); // 🔄 Refresca la tabla
                    }
                });

                btnEliminar.setOnAction(event -> {
                    TareaModel tarea = getTableView().getItems().get(getIndex());
                    if (tarea != null) {
                        tareaService.eliminarTarea(tarea.getIdTarea());
                        cargarTareasEnTabla();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(5, btnEditar, btnCompletar, btnEliminar);
                    setGraphic(buttonsBox);
                }
            }
        });
    }



    // 🔹 Cargar tareas en la tabla en segundo plano
    private void cargarTareasEnTabla() {
        Task<List<TareaModel>> task = new Task<>() {
            @Override
            protected List<TareaModel> call() {
                return tareaService.listarTareas(); // Obtener tareas del backend
            }
        };

        task.setOnSucceeded(event -> {
            List<TareaModel> tareas = task.getValue();
            if (tareas != null && taskTable != null) {
                ObservableList<TareaModel> tareaObservableList = FXCollections.observableArrayList(tareas);
                taskTable.setItems(tareaObservableList);
            } else {
                showAlert(Alert.AlertType.WARNING, "Sin datos", "No se pudieron cargar las tareas.");
            }
        });

        task.setOnFailed(event -> {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo conectar con el servidor.");
            System.out.println("❌ Error al cargar tareas: " + task.getException());
        });

        new Thread(task).start(); // ✅ Ejecutar la carga en un hilo secundario
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
