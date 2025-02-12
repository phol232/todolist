package proyecto.todolist.Capa_Presentacion.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.todolist.Capa_Conexion.LoginService;

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

    private static String userId; // 📌 Variable para almacenar el ID del usuario autenticado

    @FXML
    private void initialize() {
        createAccount.setOnAction(event -> handleCreateAccountRedirect());
        loginButton.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (!validateLogin(email, password)) {
            return;
        }

        // Llamar al servicio de autenticación
        boolean isAuthenticated = LoginService.login(email, password);

        if (isAuthenticated) {
            // 📌 Guardar el `userId` para usarlo en otras partes de la app
            userId = LoginService.getUserId();

            if (userId != null) {
                System.out.println("🔥 Usuario autenticado. ID: " + userId);
            }

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Inicio de sesión exitoso.");
            loadMainMenu();  // Redirige al menú principal
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Correo o contraseña incorrectos.");
        }
    }

    // 📌 Método para obtener el `userId` en otras partes del programa
    public static String getUserId() {
        return userId;
    }

    private boolean validateLogin(String email, String password) {
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ Por favor ingrese su email.");
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ Ingrese un email válido.");
            return false;
        }
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ Ingrese su contraseña.");
            return false;
        }
        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Error", "⚠️ La contraseña debe tener al menos 6 caracteres.");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCreateAccountRedirect() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/todolist/FXML/register-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 350, 550);
            scene.getStylesheets().add(getClass().getResource("/proyecto/todolist/CSS/register.css").toExternalForm());

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/todolist/FXML/MenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 700);
            scene.getStylesheets().add(getClass().getResource("/proyecto/todolist/CSS/menu.css").toExternalForm());

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
