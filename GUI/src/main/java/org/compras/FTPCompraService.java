package org.compras;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPCompraService {

    private static final String SERVER = "localhost";
    private static final int PORT = 21;
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String FTP_DIRECTORY = "/compras"; 

    public void registrarCompraEnFTP(String username, int cantidadProductos, double totalCompra) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USER, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String fileName = username + "_compras.txt";
            String filePath = FTP_DIRECTORY + "/" + fileName;

            boolean archivoExiste = false;
            for (String file : ftpClient.listNames(FTP_DIRECTORY)) {
                if (file.equals(filePath)) {
                    archivoExiste = true;
                    break;
                }
            }

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

            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            contenido.append(fechaHora).append(" - Productos: ").append(cantidadProductos)
                    .append(" - Total: $").append(totalCompra).append("\n");

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
