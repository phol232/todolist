package proyecto.todolist.Capa_Presentacion.Controllers;

import proyecto.todolist.Capa_Conexion.ApiClient;

public class ProbarController {
    public void probarConexion() {
        try {
            String response = ApiClient.get("/users"); // Conexión con el backend
            System.out.println("✅ Conexión exitosa! Respuesta del servidor: " + response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al conectar con el backend: " + e.getMessage());
        }
    }
}
