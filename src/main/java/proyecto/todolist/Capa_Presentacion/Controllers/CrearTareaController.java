package proyecto.todolist.Capa_Presentacion.Controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import proyecto.todolist.Capa_Conexion.TareaService;

import java.time.LocalDate;

public class CrearTareaController {

    @FXML private ComboBox<String> cbCategoria, cbPrioridad, cbEstado;
    @FXML private TextField txtTitulo, txtFecha;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnGuardarTarea;

    private final TareaService tareaService = new TareaService();
    private Runnable onTaskSavedCallback; // ✅ Callback para actualizar la tabla en MenuController

    @FXML
    public void initialize() {
        // ✅ Agregar categorías 📌
        cbCategoria.getItems().addAll("Software", "Hardware", "Red");

        // ✅ Agregar prioridades ⚡
        cbPrioridad.getItems().addAll("🔴 Alta", "🟡 Media", "🟢 Baja");

        // ✅ Agregar estados 🔄
        cbEstado.getItems().addAll("✅ Terminado", "🔄 En Proceso");

        // ✅ Establecer valores predeterminados
        cbCategoria.setValue("Software");
        cbPrioridad.setValue("🟡 Media");
        cbEstado.setValue("🔄 En Proceso");

        // ✅ Configurar TextField para abrir el DatePicker
        txtFecha.setText(LocalDate.now().toString());
        txtFecha.setEditable(false);
        txtFecha.setOnMouseClicked(event -> abrirCalendario());

        // ✅ Configurar evento del botón
        btnGuardarTarea.setOnAction(event -> guardarTarea());
    }

    // ✅ Método para establecer el callback desde MenuController
    public void setOnTaskSaved(Runnable callback) {
        this.onTaskSavedCallback = callback;
    }

    private void abrirCalendario() {
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Seleccionar Fecha");
        dialog.getDialogPane().setContent(new VBox(datePicker));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                txtFecha.setText(datePicker.getValue().toString());
            }
        });
    }

    @FXML
    private void guardarTarea() {
        System.out.println("🔵 Guardando tarea...");

        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String categoria = cbCategoria.getValue();
        String prioridad = cbPrioridad.getValue();
        String estado = cbEstado.getValue();

        // ✅ Validar campos
        if (titulo.isEmpty() || descripcion.isEmpty() || categoria == null || prioridad == null || estado == null) {
            showAlert(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        // ✅ Guardar tarea en segundo plano
        tareaService.guardarTareaAsync(titulo, descripcion, categoria, prioridad, estado, () -> {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Tarea guardada correctamente.");
            limpiarFormulario();

            // ✅ Llamar al callback para actualizar la tabla en MenuController
            if (onTaskSavedCallback != null) {
                onTaskSavedCallback.run();
            }

            // ✅ Cerrar la ventana después de guardar
            cerrarVentana();
        });
    }

    private void limpiarFormulario() {
        txtTitulo.clear();
        txtDescripcion.clear();
        cbCategoria.setValue("Software");
        cbPrioridad.setValue("🟡 Media");
        cbEstado.setValue("🔄 En Proceso");
        txtFecha.setText(LocalDate.now().toString());
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnGuardarTarea.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
