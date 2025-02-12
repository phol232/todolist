package proyecto.todolist.Capa_Datos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryModel {
    private String idCat;
    private String nombre;

    // ✅ Constructor vacío requerido por Jackson
    public CategoryModel() {}

    // ✅ Constructor con todos los campos
    public CategoryModel(String idCat, String nombre) {
        this.idCat = idCat;
        this.nombre = nombre;
    }

    // ✅ Constructor solo con nombre (usado en la edición de tareas)
    public CategoryModel(String nombre) {
        this.nombre = nombre;
    }

    public String getIdCat() { return idCat; }
    public void setIdCat(String idCat) { this.idCat = idCat; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
