package org.compras.controller;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.compras.ApiService;
import org.compras.model.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import static org.compras.HelloApplication.showLoginView;
import static org.compras.HelloApplication.showProductView;

public class AdminController {

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
    private Button addButton, updateButton, deleteButton;
    @FXML
    private Button goToProductsButton, logoutButton;

    private ApiService apiService;

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

        addButton.setOnAction(e -> openProductForm(null));
        updateButton.setOnAction(e -> {
            Producto selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                openProductForm(selectedProduct);
            }
        });

        deleteButton.setOnAction(e -> eliminarProductos());

        goToProductsButton.setOnAction(e -> {
            try {
                showProductView();
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

    private void eliminarProductos() {
        Producto selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            apiService.eliminarProducto(selectedProduct.getId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    loadProducts();
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    System.out.println("Error eliminando producto: " + throwable.getMessage());
                }
            });
        }
    }

    private void openProductForm(Producto producto) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(producto == null ? "Añadir Producto" : "Modificar Producto");

        DialogPane dialogPane = new DialogPane();
        dialog.setDialogPane(dialogPane);

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del producto");

        TextField precioField = new TextField();
        precioField.setPromptText("Precio");

        TextField stockField = new TextField();
        stockField.setPromptText("Stock");

        if (producto != null) {
            nombreField.setText(producto.getNombre());
            precioField.setText(String.valueOf(producto.getPrecio()));
            stockField.setText(String.valueOf(producto.getStock()));
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Nombre:"), nombreField,
                new Label("Precio (€):"), precioField,
                new Label("Stock:"), stockField
        );

        dialogPane.setContent(content);

        ButtonType saveButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(saveButtonType, cancelButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int stock = Integer.parseInt(stockField.getText());

                if (producto == null) {
                    Producto nuevoProducto = new Producto(nombre, precio, stock);
                    apiService.agregarProducto(nuevoProducto).enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            loadProducts(); // Recargar productos después de añadir
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable throwable) {
                            System.out.println("Error al agregar producto: " + throwable.getMessage());
                        }
                    });
                } else {
                    producto.setNombre(nombre);
                    producto.setPrecio(precio);
                    producto.setStock(stock);
                    apiService.actualizarProducto(producto.getId(), producto).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            loadProducts(); // Recargar productos después de modificar
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable throwable) {
                            System.out.println("Error al actualizar producto: " + throwable.getMessage());
                        }
                    });
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

}

