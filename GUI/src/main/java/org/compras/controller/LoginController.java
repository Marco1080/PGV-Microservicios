// LoginController.java - Restaurado y adaptado
package org.compras.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import okhttp3.ResponseBody;
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
                .baseUrl("http://localhost:8082")
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
                Platform.runLater(() -> {
                    if (response.isSuccessful() && "200".equals(response.body())) {
                        showAlert("Éxito", "Usuario registrado correctamente.");
                        try {
                            HelloApplication.showProductView(username);
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

        Call<ResponseBody> call = apiService.verificarUsuario(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String respuesta = response.body().string(); // Convierte ResponseBody a String
                            if ("Usuario autenticado correctamente".equals(respuesta.trim())) {
                                showAlert("Éxito", "Inicio de sesión exitoso.");
                                try {
                                    HelloApplication.showProductView(username);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showAlert("Error", "No se pudo abrir la vista de productos.");
                                }
                            } else {
                                showAlert("Error", respuesta); // Muestra el mensaje exacto de la API
                            }
                        } else {
                            showAlert("Error", "Error en la autenticación.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Error", "No se pudo leer la respuesta.");
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Platform.runLater(() -> showAlert("Error", "No se pudo conectar con el servidor."));
            }
        });
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
