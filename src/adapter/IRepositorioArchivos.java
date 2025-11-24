package adapter;

/**
 * PATRÓN ADAPTER - Interface Target para repositorios
 * Define la interfaz que el sistema LMS espera para almacenamiento de archivos
 */
public interface IRepositorioArchivos {
    // Subir un archivo al repositorio
    String subirArchivo(String nombreArchivo, byte[] contenido);

    // Descargar un archivo del repositorio
    byte[] descargarArchivo(String idArchivo);

    // Eliminar un archivo
    boolean eliminarArchivo(String idArchivo);

    // Obtener URL pública del archivo
    String obtenerUrlPublica(String idArchivo);

    // Obtener información del proveedor
    String getProveedor();
}
