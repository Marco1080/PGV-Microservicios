package org.compras.controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.compras.ApiService;
import org.compras.FTPCompraService;
import org.compras.model.Compra;
import org.compras.model.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

import static org.compras.HelloApplication.showAdminView;
import static org.compras.HelloApplication.showLoginView;

public class ProductController {

    @FXML
    private TableView<Producto> productTableView;
    @FXML
    private TableColumn<Producto, Integer> idColumn;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, Double> precioColumn;
    @FXML
    private TableColumn<Producto, Integer> stockColumn;

    @FXML
    private Button buyButton, goToAdminButton, logoutButton;

    private ApiService apiService;

    private String username = "";

    @FXML
    private void initialize() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        precioColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        stockColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

        loadProducts();

        buyButton.setOnAction(e -> realizarCompra());

        goToAdminButton.setOnAction(e -> {
            try {
                showAdminView(username);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        logoutButton.setOnAction(e -> {
            try {
                showLoginView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        productTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadProducts() {
        apiService.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productTableView.getItems().setAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable throwable) {
                System.out.println("Error cargando productos: " + throwable.getMessage());
            }
        });
    }

    private void realizarCompra() {
        List<Producto> productosSeleccionados = new ArrayList<>(productTableView.getSelectionModel().getSelectedItems());

        if (productosSeleccionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Debes seleccionar al menos un producto para comprar.");
            return;
        }

        // Asegurarse de que el username no sea null ni vacío
        if (username == null || username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error: No se ha detectado el usuario.");
            return;
        }
        System.out.println(username);
        // Crear el objeto Compra asegurando que el cliente está definido
        Compra compra = new Compra(username, productosSeleccionados);

        // Enviar la compra a la API
        apiService.realizarCompra(compra).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Compra> call, Response<Compra> response) {
                if (response.isSuccessful()) {
                    showAlert(Alert.AlertType.INFORMATION, "Compra realizada con éxito.");
                    loadProducts();
                } else {
                    System.out.println(response.message() + response.body());
                    showAlert(Alert.AlertType.ERROR, "Error al realizar la compra.");
                }
            }

            @Override
            public void onFailure(Call<Compra> call, Throwable throwable) {
                System.out.println();
                throwable.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error de conexión: " + throwable.getMessage());
            }
        });

        double totalCompra = productosSeleccionados.stream().mapToDouble(Producto::getPrecio).sum();
        int cantidadProductos = productosSeleccionados.size();
        FTPCompraService ftpCompraService = new FTPCompraService();
        ftpCompraService.registrarCompraEnFTP(username, cantidadProductos, totalCompra);
    }


    private void showAlert(Alert.AlertType type, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

