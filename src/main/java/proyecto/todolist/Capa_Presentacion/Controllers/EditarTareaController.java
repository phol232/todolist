package proyecto.todolist.Capa_Presentacion.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.todolist.Capa_Conexion.TareaService;
import proyecto.todolist.Capa_Datos.TareaModel;
import proyecto.todolist.Capa_Datos.CategoryModel; // ✅ Asegurar importar el modelo de categoría

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditarTareaController {

    @FXML private TextField txtTitulo;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private ComboBox<String> cbPrioridad;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TextField txtFecha;
    @FXML private Button btnGuardarTarea;
    @FXML private Button btnCancelar;

    private final TareaService tareaService = new TareaService();
    private TareaModel tareaActual;

    @FXML
    public void initialize() {
        // ✅ Cargar opciones en ComboBox
        cbCategoria.getItems().addAll("Software", "Hardware", "Red");
        cbPrioridad.getItems().addAll("🔴 Alta", "🟡 Media", "🟢 Baja");
        cbEstado.getItems().addAll("✅ Terminado", "🔄 En Proceso");

        // ✅ Configurar campo de fecha
        txtFecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        txtFecha.setEditable(false);
        txtFecha.setOnMouseClicked(event -> abrirCalendario());

        // ✅ Configurar evento para guardar
        btnGuardarTarea.setOnAction(event -> guardarTarea());
    }

    public void setTarea(TareaModel tarea) {
        this.tareaActual = tarea;
        if (tarea != null) {
            System.out.println("📝 Editando tarea con ID: " + tarea.getIdTarea());

            // ✅ Evita `NullPointerException`
            txtTitulo.setText(tarea.getTitulo());
            txtDescripcion.setText(tarea.getDescripcion());
            cbCategoria.setValue((tarea.getCategoria() != null) ? tarea.getCategoria().getNombre() : "Sin Categoría"); // ✅ Manejo seguro
            cbPrioridad.setValue(tarea.getPrioridad());
            cbEstado.setValue(tarea.getEstado());

            if (tarea.getFecha() != null) {
                txtFecha.setText(tarea.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        } else {
            System.out.println("⚠️ No se recibió tarea para editar.");
        }
    }


    @FXML
    private void guardarTarea() {
        if (tareaActual == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "❌ No hay una tarea seleccionada para editar.");
            return;
        }

        System.out.println("🔹 Guardando cambios en la tarea con ID: " + tareaActual.getIdTarea());

        // ✅ Crear objeto de categoría correctamente
        CategoryModel categoria = new CategoryModel(cbCategoria.getValue());

        // Actualizar valores desde la interfaz
        tareaActual.setTitulo(txtTitulo.getText());
        tareaActual.setDescripcion(txtDescripcion.getText());
        tareaActual.setCategoria(categoria); // ✅ Ahora asigna el objeto `CategoryModel`
        tareaActual.setPrioridad(cbPrioridad.getValue());
        tareaActual.setEstado(cbEstado.getValue());

        // ✅ Enviar al backend con la estructura correcta
        tareaService.editarTareaAsync(
                tareaActual.getIdTarea(),
                tareaActual.getTitulo(),
                tareaActual.getDescripcion(),
                categoria.getNombre(), // ✅ Pasar solo el nombre en un nuevo objeto `CategoryModel`
                tareaActual.getPrioridad(),
                tareaActual.getEstado(),
                () -> {
                    showAlert(Alert.AlertType.INFORMATION, "Tarea Actualizada", "✅ La tarea se ha actualizado correctamente.");
                    cerrarVentana();
                }
        );
    }



    // ✅ Cerrar la ventana al hacer clic en "CANCELAR"
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // ✅ Método para abrir un calendario y seleccionar la fecha
    private void abrirCalendario() {
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Seleccionar Fecha");
        dialog.getDialogPane().setContent(datePicker);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(date -> {
            txtFecha.setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        });
    }

    // ✅ Mostrar alertas en la interfaz gráfica
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
