package proyecto.todolist.Capa_Presentacion.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Hyperlink forgotPassword;

    @FXML
    private Hyperlink createAccount;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        // Manejar clic en "Create an account"
        createAccount.setOnAction(event -> handleCreateAccountRedirect());

        // Manejar clic en "Login"
        loginButton.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (validateLogin(email, password)) {
            System.out.println("✅ Login successful!");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Welcome to ToDo List!");

            // Redirigir al MenuView.fxml después de iniciar sesión exitosamente
            loadMainMenu();
        }
    }

    private boolean validateLogin(String email, String password) {
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ Please enter your email.");
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ Enter a valid email.");
            return false;
        }
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ Please enter your password.");
            return false;
        }
        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ Password must be at least 6 characters.");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCreateAccountRedirect() {
        try {
            // Cargar la vista de Registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/todolist/fxml/register-view.fxml"));
            Parent root = loader.load();

            // Crear la nueva escena y aplicar CSS
            Scene scene = new Scene(root, 350, 550);
            scene.getStylesheets().add(getClass().getResource("/proyecto/todolist/css/register.css").toExternalForm());

            // Obtener la ventana actual y reemplazar la escena
            Stage stage = (Stage) createAccount.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registro - ToDo List");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla de registro.");
        }
    }

    private void loadMainMenu() {
        try {
            // Cargar la vista del menú principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/todolist/fxml/MenuView.fxml"));
            Parent root = loader.load();

            // Crear la nueva escena y aplicar CSS
            Scene scene = new Scene(root, 1250, 700);
            scene.getStylesheets().add(getClass().getResource("/proyecto/todolist/css/menu.css").toExternalForm());

            // Obtener la ventana actual y reemplazar la escena
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("ToDo List - Menú Principal");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar el menú principal.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
