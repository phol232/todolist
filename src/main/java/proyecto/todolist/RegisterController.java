package proyecto.todolist;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private void initialize() {
        // Manejar clic en "Inicia sesión"
        loginLink.setOnAction(event -> handleLoginRedirect());
    }

    @FXML
    private void handleRegister() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Todos los campos son obligatorios.");
            return;
        }

        System.out.println("Registro exitoso: " + fullName + " - " + email);
        showAlert(AlertType.INFORMATION, "Éxito", "Cuenta creada exitosamente.");
    }

    @FXML
    private void handleLoginRedirect() {
        try {
            // Cargar la vista de Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/todolist/fxml/login-view.fxml"));
            Parent root = loader.load();

            // Crear la nueva escena y agregar el CSS
            Scene scene = new Scene(root, 400, 550);
            scene.getStylesheets().add(getClass().getResource("/proyecto/todolist/css/login.css").toExternalForm());

            // Obtener la ventana actual y reemplazar la escena
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login - ToDo List");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "No se pudo cargar la pantalla de inicio de sesión.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
