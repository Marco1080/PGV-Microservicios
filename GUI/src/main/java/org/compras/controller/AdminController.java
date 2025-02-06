package org.compras.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.compras.model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static org.compras.HelloApplication.showLoginView;
import static org.compras.HelloApplication.showProductView;

public class AdminController {

    @FXML
    private TableView<Producto> productTableView;
    @FXML
    private TableColumn<Producto, Long> idColumn;
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

    private List<Producto> productos = new ArrayList<>(); // Simulación de productos

    @FXML
    private void initialize() {
        // Configurar las columnas
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        precioColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        stockColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

        // Simular carga de productos
        loadProducts();

        // Botón para añadir productos
        addButton.setOnAction(e -> openProductForm(null));
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

        // Botón para modificar productos
        updateButton.setOnAction(e -> {
            Producto selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                openProductForm(selectedProduct);
            }
        });

        // Botón para eliminar productos seleccionados
        deleteButton.setOnAction(e -> {
            List<Producto> selectedProducts = new ArrayList<>(productTableView.getSelectionModel().getSelectedItems());
            selectedProducts.forEach(product -> {
                productos.removeIf(p -> p.getId().equals(product.getId()));
                System.out.println("Producto eliminado: " + product.getNombre());
            });
            refreshTable();
        });

        productTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadProducts() {
        productos.add(new Producto(1L, "Laptop", 1000.0, 5));
        productos.add(new Producto(2L, "Smartphone", 500.0, 10));
        productos.add(new Producto(3L, "Tablet", 300.0, 7));
        refreshTable();
    }

    private void refreshTable() {
        productTableView.getItems().setAll(productos);
    }

    private void openProductForm(Producto producto) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(producto == null ? "Añadir Producto" : "Modificar Producto");

        TextField nameField = new TextField(producto != null ? producto.getNombre() : "");
        TextField priceField = new TextField(producto != null ? String.valueOf(producto.getPrecio()) : "");
        TextField stockField = new TextField(producto != null ? String.valueOf(producto.getStock()) : "");

        dialog.getDialogPane().setContent(new VBox(10,
                new Label("Nombre:"), nameField,
                new Label("Precio (€):"), priceField,
                new Label("Stock:"), stockField
        ));

        dialog.showAndWait().ifPresent(result -> {
            String nombre = nameField.getText();
            double precio = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            if (producto == null) {
                Producto nuevoProducto = new Producto((long) (productos.size() + 1), nombre, precio, stock);
                productos.add(nuevoProducto);
            } else {
                producto.setNombre(nombre);
                producto.setPrecio(precio);
                producto.setStock(stock);
            }
            refreshTable();
        });
    }
}
