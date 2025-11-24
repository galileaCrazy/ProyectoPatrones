package adapter;

/**
 * PATRÓN ADAPTER - Adapter Concreto para repositorio
 * Adapta Google Drive a la interfaz IRepositorioArchivos del LMS
 */
public class GoogleDriveAdapter implements IRepositorioArchivos {
    private GoogleDriveAPI driveAPI;

    public GoogleDriveAdapter() {
        this.driveAPI = new GoogleDriveAPI();
    }

    @Override
    public String subirArchivo(String nombreArchivo, byte[] contenido) {
        // Adaptar: determinar MIME type automáticamente
        String mimeType = determinarMimeType(nombreArchivo);
        return driveAPI.uploadFile(nombreArchivo, contenido, mimeType, "root");
    }

    @Override
    public byte[] descargarArchivo(String idArchivo) {
        return driveAPI.downloadFile(idArchivo);
    }

    @Override
    public boolean eliminarArchivo(String idArchivo) {
        try {
            driveAPI.deleteFile(idArchivo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String obtenerUrlPublica(String idArchivo) {
        return driveAPI.createPublicLink(idArchivo);
    }

    @Override
    public String getProveedor() {
        return "Google Drive";
    }

    private String determinarMimeType(String nombreArchivo) {
        if (nombreArchivo.endsWith(".pdf")) return "application/pdf";
        if (nombreArchivo.endsWith(".mp4")) return "video/mp4";
        if (nombreArchivo.endsWith(".jpg")) return "image/jpeg";
        return "application/octet-stream";
    }
}
