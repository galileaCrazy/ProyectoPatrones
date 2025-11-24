package adapter;

/**
 * PATRÓN ADAPTER - Adaptee para repositorio de archivos
 * Simula la API real de Google Drive
 */
public class GoogleDriveAPI {
    private String folderId;

    public GoogleDriveAPI() {
        this.folderId = "root";
    }

    // Método específico de Google Drive
    public String uploadFile(String fileName, byte[] content, String mimeType, String parentId) {
        String fileId = "gdrive_" + System.currentTimeMillis();
        System.out.println("[GoogleDriveAPI] Archivo subido: " + fileName + " -> " + fileId);
        return fileId;
    }

    // Método específico de Google Drive
    public byte[] downloadFile(String fileId) {
        System.out.println("[GoogleDriveAPI] Descargando archivo: " + fileId);
        return ("Contenido de " + fileId).getBytes();
    }

    // Método específico de Google Drive
    public void deleteFile(String fileId) {
        System.out.println("[GoogleDriveAPI] Eliminando archivo: " + fileId);
    }

    // Método específico de Google Drive
    public String createPublicLink(String fileId) {
        return "https://drive.google.com/file/d/" + fileId + "/view";
    }
}
