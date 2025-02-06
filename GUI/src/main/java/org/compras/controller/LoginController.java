package org.compras.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.compras.ApiService;
import org.compras.HelloApplication;
import org.compras.model.Cliente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private ApiService apiService;

    public void initialize() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:9090/api/") // Asegúrate de que este puerto sea el correcto
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            return;
        }

        Cliente cliente = new Cliente(username, password);
        Call<String> call = apiService.registrarUsuario(cliente);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Platform.runLater(() -> { // Asegura que la UI se actualice en el hilo correcto
                    if (response.isSuccessful() && response.body() != null && response.body().equals("200")) {
                        showAlert("Éxito", "Usuario registrado correctamente.");
                        try {
                            HelloApplication.showProductView(username); // Ir a la vista de productos
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert("Error", "No se pudo abrir la vista de productos.");
                        }
                    } else {
                        showAlert("Error", "El usuario ya existe o hubo un problema.");
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Platform.runLater(() -> showAlert("Error", "No se pudo conectar con el servidor."));
            }
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            return;
        }

        Cliente cliente = new Cliente(username, password);
        Call<String> call = apiService.verificaUsuario(Cliente);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        String respuesta = response.body();
                        if (respuesta.equals("Usuario autenticado correctamente")) {
                            showAlert("Éxito", "Inicio de sesión exitoso.");
                            try {
                                HelloApplication.showProductView(username);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showAlert("Error", "No se pudo abrir la vista de productos.");
                            }
                        } else {
                            showAlert("Error", "Credenciales incorrectas o usuario no encontrado.");
                        }
                    } else {
                        showAlert("Error", "Error en la autenticación.");
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Platform.runLater(() -> showAlert("Error", "No se pudo conectar con el servidor."));
            }
        });
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> { // Asegura que la alerta se ejecute en el hilo de JavaFX
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
