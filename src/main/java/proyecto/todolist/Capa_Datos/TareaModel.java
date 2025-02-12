package proyecto.todolist.Capa_Datos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true) // 🔹 Evita errores si hay más propiedades en el JSON recibido
public class TareaModel {
    private String idTarea;
    private String titulo;
    private String descripcion;
    private CategoryModel categoria;
    private String prioridad;
    private String estado;
    private LocalDateTime fecha;

    @JsonProperty("nombreCategoria") // ✅ Forzar el mapeo del JSON a esta propiedad
    private String nombreCategoria;

    public TareaModel() {}

    public TareaModel(@JsonProperty("idTarea") String idTarea,
                      @JsonProperty("titulo") String titulo,
                      @JsonProperty("descripcion") String descripcion,
                      @JsonProperty("categoria") CategoryModel categoria,
                      @JsonProperty("prioridad") String prioridad,
                      @JsonProperty("estado") String estado,
                      @JsonProperty("fecha") LocalDateTime fecha,
                      @JsonProperty("nombreCategoria") String nombreCategoria) {
        this.idTarea = idTarea;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fecha = fecha;
        this.nombreCategoria = (nombreCategoria != null && !nombreCategoria.isEmpty()) ? nombreCategoria : "Sin Categoría";
    }

    public String getIdTarea() { return idTarea; }
    public void setIdTarea(String idTarea) { this.idTarea = idTarea; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public CategoryModel getCategoria() { return categoria; }
    public void setCategoria(CategoryModel categoria) {
        this.categoria = categoria;
        this.nombreCategoria = (categoria != null && categoria.getNombre() != null) ? categoria.getNombre() : "Sin Categoría";
    }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = (nombreCategoria != null && !nombreCategoria.isEmpty()) ? nombreCategoria : "Sin Categoría";
    }

    @Override
    public String toString() {
        return titulo + " - " + estado + " - " + nombreCategoria; // ✅ Para depuración
    }
}
