package proyecto.todolist.Capa_Datos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true) // 🔹 Evita errores si hay más propiedades en el JSON recibido
public class TareaModel {
    private String idTarea;
    private String titulo;
    private String descripcion;
    private String categoria; // Aquí se guarda el id de la categoría (si lo usas)
    private String nombreCategoria; // 🔹 Nuevo campo para el nombre real de la categoría
    private String prioridad;
    private String estado;
    private LocalDateTime fecha;

    // ✅ Constructor vacío (necesario para Jackson)
    public TareaModel() {}

    // ✅ Constructor con parámetros
    public TareaModel(String idTarea, String titulo, String descripcion, String categoria, String nombreCategoria, String prioridad, String estado, LocalDateTime fecha) {
        this.idTarea = idTarea;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nombreCategoria = nombreCategoria;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fecha = fecha;
    }

    // ✅ Getters y Setters
    public String getIdTarea() { return idTarea; }
    public void setIdTarea(String idTarea) { this.idTarea = idTarea; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNombreCategoria() { return nombreCategoria; } // 🔹 Getter para `nombreCategoria`
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return titulo + " - " + estado;
    }
}
