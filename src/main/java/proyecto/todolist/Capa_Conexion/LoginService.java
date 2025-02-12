package proyecto.todolist.Capa_Conexion;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LoginService {

    private static final String SERVER_URL = "http://192.168.224.5:8080/auth/login";
    private static String userId = null;  // ID del usuario autenticado
    private static String sessionCookie = null; // ✅ Cookie de sesión para autenticación

    public static boolean login(String email, String password) {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 🚀 Cuerpo de la solicitud en JSON
            String jsonInputString = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // OK
                // ✅ Leer respuesta del servidor
                Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
                String responseBody = scanner.useDelimiter("\\A").next();
                scanner.close();

                // ✅ Convertir respuesta a JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                if (jsonResponse.has("id")) {
                    userId = jsonResponse.getString("id"); // Guardar el ID del usuario
                }

                // ✅ Capturar y guardar la cookie de sesión
                Map<String, List<String>> headers = connection.getHeaderFields();
                List<String> cookies = headers.get("Set-Cookie");
                if (cookies != null) {
                    for (String cookie : cookies) {
                        if (cookie.contains("JSESSIONID")) {
                            sessionCookie = cookie.split(";")[0]; // Extraer solo "JSESSIONID=..."
                            break;
                        }
                    }
                }

                System.out.println("✅ Login exitoso. ID: " + userId);
                System.out.println("🍪 Cookie de sesión: " + sessionCookie);
                return true;
            } else {
                System.out.println("❌ Error en login. Código de respuesta: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ Error en login: " + e.getMessage());
            return false;
        }
    }

    // ✅ Método para obtener el ID del usuario autenticado
    public static String getUserId() {
        return userId;
    }

    // ✅ Método para obtener la cookie de sesión
    public static String getSessionCookie() {
        return sessionCookie;
    }
}
