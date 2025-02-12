package proyecto.todolist.Capa_Conexion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import proyecto.todolist.Capa_Datos.TareaModel;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TareaService {

    private static final String BASE_URL = "http://192.168.224.5:8080/api/tareas"; // URL del backend
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TareaService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // ✅ Guardar tarea en segundo plano
    public void guardarTareaAsync(String titulo, String descripcion, String categoria, String prioridad, String estado, Runnable callback) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    // 🚀 Validar que los datos no estén vacíos antes de enviarlos
                    if (titulo.isEmpty() || descripcion.isEmpty()) {
                        Platform.runLater(() -> System.out.println("⚠️ Error: Título y descripción son obligatorios."));
                        return null;
                    }

                    // 🚀 Validar si el backend está accesible antes de enviar la petición
                    if (!verificarConexion()) {
                        Platform.runLater(() -> System.out.println("⚠️ Error: El backend no está accesible."));
                        return null;
                    }

                    Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("titulo", titulo);
                    requestBody.put("descripcion", descripcion);
                    requestBody.put("categoria", categoria);
                    requestBody.put("prioridad", prioridad);
                    requestBody.put("estado", estado);

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL))
                            .header("Content-Type", "application/json")
                            .header("Cookie", LoginService.getSessionCookie()) // ✅ Enviar la cookie de sesión
                            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                        Platform.runLater(callback); // ✅ Ejecuta el callback cuando la tarea se complete
                    } else {
                        Platform.runLater(() -> {
                            System.out.println("❌ Error al guardar tarea. Código: " + response.statusCode());
                            System.out.println("🔍 Respuesta del servidor: " + response.body()); // ✅ Mostrar respuesta del servidor
                        });
                    }
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> System.out.println("❌ Error en la solicitud: " + e.getMessage()));
                }
                return null;
            }
        };

        new Thread(task).start(); // Ejecuta en segundo plano
    }

    // ✅ Método para verificar si el backend está accesible
    private boolean verificarConexion() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Editar tarea existente
    public List<TareaModel> editarTarea(String idTarea, String titulo, String descripcion, String categoria, String prioridad, String estado) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("titulo", titulo);
            requestBody.put("descripcion", descripcion);
            requestBody.put("categoria", categoria);
            requestBody.put("prioridad", prioridad);
            requestBody.put("estado", estado);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + idTarea))
                    .header("Content-Type", "application/json")
                    .header("Cookie", LoginService.getSessionCookie()) // ✅ Enviar la cookie de sesión
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                return objectMapper.readValue(response.body(), new TypeReference<List<TareaModel>>() {});
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Eliminar tarea
    public List<TareaModel> eliminarTarea(String idTarea) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + idTarea))
                    .header("Cookie", LoginService.getSessionCookie()) // ✅ Enviar la cookie de sesión
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                return objectMapper.readValue(response.body(), new TypeReference<List<TareaModel>>() {});
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Listar tareas
    public List<TareaModel> listarTareas() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Cookie", LoginService.getSessionCookie()) // ✅ Enviar la cookie de sesión
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                // ✅ Ahora considera `nombreCategoria`
                return objectMapper.readValue(response.body(), new TypeReference<List<TareaModel>>() {});
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
