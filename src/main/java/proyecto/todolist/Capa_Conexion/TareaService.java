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
                    if (titulo.isEmpty() || descripcion.isEmpty()) {
                        Platform.runLater(() -> System.out.println("⚠️ Error: Título y descripción son obligatorios."));
                        return null;
                    }

                    if (!verificarConexion()) {
                        Platform.runLater(() -> System.out.println("⚠️ Error: El backend no está accesible."));
                        return null;
                    }

                    // 📌 Construir el JSON correctamente con categoría como objeto
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("titulo", titulo);
                    requestBody.put("descripcion", descripcion);
                    requestBody.put("categoria", Map.of("nombre", categoria)); // ✅ Corrección aquí
                    requestBody.put("prioridad", prioridad);
                    requestBody.put("estado", estado);

                    String jsonBody = objectMapper.writeValueAsString(requestBody);
                    System.out.println("📡 Enviando JSON al backend: " + jsonBody);

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL))
                            .header("Content-Type", "application/json")
                            .header("Cookie", LoginService.getSessionCookie())
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                        Platform.runLater(callback);
                    } else {
                        Platform.runLater(() -> {
                            System.out.println("❌ Error al guardar tarea. Código: " + response.statusCode());
                            System.out.println("🔍 Respuesta del servidor: " + response.body());
                        });
                    }
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> System.out.println("❌ Error en la solicitud: " + e.getMessage()));
                }
                return null;
            }
        };

        new Thread(task).start();
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

    public void editarTareaAsync(String idTarea, String titulo, String descripcion, String categoria, String prioridad, String estado, Runnable callback) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    if (idTarea == null || idTarea.isEmpty()) {
                        Platform.runLater(() -> System.out.println("⚠️ Error: El ID de la tarea es inválido."));
                        return null;
                    }

                    // 📌 Construir JSON con `categoria` como objeto
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("titulo", titulo);
                    requestBody.put("descripcion", descripcion);

                    // ✅ Asegurar que `categoria` no sea nula
                    if (categoria != null && !categoria.isEmpty()) {
                        requestBody.put("categoria", Map.of("nombre", categoria)); // 🔹 Corrección aquí
                    } else {
                        requestBody.put("categoria", Map.of("nombre", "Sin Categoría")); // 🔹 Valor por defecto
                    }

                    requestBody.put("prioridad", prioridad);
                    requestBody.put("estado", estado);

                    // 🔍 Convertir el mapa en un JSON
                    String jsonBody = objectMapper.writeValueAsString(requestBody);
                    System.out.println("📡 Enviando JSON al backend: " + jsonBody);

                    // 📌 Construir la solicitud HTTP
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/" + idTarea))
                            .header("Content-Type", "application/json")
                            .header("Cookie", LoginService.getSessionCookie()) // ✅ Enviar la cookie de sesión
                            .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    // 🚀 Enviar la solicitud al backend
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    // ✅ Verificar la respuesta del backend
                    if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                        Platform.runLater(callback);
                        System.out.println("✅ Tarea actualizada correctamente.");
                    } else {
                        Platform.runLater(() -> {
                            System.out.println("❌ Error al actualizar tarea. Código: " + response.statusCode());
                            System.out.println("🔍 Respuesta del servidor: " + response.body());
                        });
                    }
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> System.out.println("❌ Error en la solicitud: " + e.getMessage()));
                }
                return null;
            }
        };

        new Thread(task).start(); // ✅ Ejecutar en segundo plano
    }


    // ✅ Eliminar tarea sin mostrar `categoria`
    public List<TareaModel> eliminarTarea(String idTarea) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + idTarea))
                    .header("Cookie", LoginService.getSessionCookie())
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                List<TareaModel> tareas = objectMapper.readValue(response.body(), new TypeReference<List<TareaModel>>() {});

                // ✅ Eliminar `categoria` de la respuesta
                for (TareaModel tarea : tareas) {
                    tarea.setCategoria(null);
                }

                return tareas;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(); // ✅ Retornar lista vacía en caso de error
    }

    public List<TareaModel> listarTareas() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Cookie", LoginService.getSessionCookie())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("📡 JSON recibido del backend: " + response.body()); // ✅ Depuración
                return objectMapper.readValue(response.body(), new TypeReference<List<TareaModel>>() {});
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return List.of();
    }

}
