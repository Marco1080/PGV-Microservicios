package org.compras;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FTPCompraService {

    private static final String SERVER = "localhost"; // Cambia según tu configuración
    private static final int PORT = 21;
    private static final String USER = "root"; // Usuario genérico
    private static final String PASSWORD = "root"; // Contraseña genérica
    private static final String FTP_DIRECTORY = "/compras"; // Directorio en el servidor FTP

    public void registrarCompraEnFTP(String username, int cantidadProductos, double totalCompra) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USER, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Nombre del archivo basado en el usuario
            String fileName = username + "_compras.txt";
            String filePath = FTP_DIRECTORY + "/" + fileName;

            // Verificar si el archivo ya existe en el servidor
            boolean archivoExiste = false;
            for (String file : ftpClient.listNames(FTP_DIRECTORY)) {
                if (file.equals(filePath)) {
                    archivoExiste = true;
                    break;
                }
            }

            // Descargar el archivo si existe
            StringBuilder contenido = new StringBuilder();
            if (archivoExiste) {
                InputStream inputStream = ftpClient.retrieveFileStream(filePath);
                if (inputStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            contenido.append(line).append("\n");
                        }
                    }
                    ftpClient.completePendingCommand();
                }
            }

            // Agregar nueva compra
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            contenido.append(fechaHora).append(" - Productos: ").append(cantidadProductos)
                    .append(" - Total: $").append(totalCompra).append("\n");

            // Subir el archivo actualizado al FTP
            try (InputStream inputStream = new ByteArrayInputStream(contenido.toString().getBytes())) {
                ftpClient.storeFile(filePath, inputStream);
            }

            System.out.println("Compra registrada en el servidor FTP.");
        } catch (IOException e) {
            System.err.println("Error en FTP: " + e.getMessage());
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
