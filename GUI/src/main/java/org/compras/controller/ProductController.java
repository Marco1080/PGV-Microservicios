package org.compras.controller;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.compras.model.Compra;
import org.compras.model.Producto;

import java.util.ArrayList;
import java.util.List;

import static org.compras.HelloApplication.*;

public class ProductController {

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
    private Button buyButton;
    @FXML
    private Button goToAdminButton, logoutButton;

    private List<Producto> productos = new ArrayList<>(); // Simulación de productos

    @FXML
    private void initialize() {
        // Configurar las columnas para mostrar atributos de Producto
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        precioColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        stockColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

        // Simular carga de productos
        loadProducts();

        // Botón para realizar la compra de productos seleccionados
        buyButton.setOnAction(e -> realizarCompra());

        goToAdminButton.setOnAction(e -> {
            try {
                showAdminView();
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
        productos.add(new Producto(1L, "Laptop", 1000.0, 5));
        productos.add(new Producto(2L, "Smartphone", 500.0, 10));
        productos.add(new Producto(3L, "Tablet", 300.0, 7));
        refreshTable();
    }

    private void refreshTable() {
        productTableView.getItems().setAll(productos);
    }

    private void realizarCompra() {
        List<Producto> selectedProducts = new ArrayList<>(productTableView.getSelectionModel().getSelectedItems());

        if (selectedProducts.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Debes seleccionar al menos un producto para comprar.");
            return;
        }

        double totalPrecio = selectedProducts.stream().mapToDouble(Producto::getPrecio).sum();
        String productosComprados = selectedProducts.stream()
                .map(Producto::getNombre)
                .reduce((p1, p2) -> p1 + ", " + p2)
                .orElse("");

        // Simulación de la compra
        Compra compra = new Compra("Usuario1", selectedProducts);
        System.out.println("Compra realizada para: " + compra.getUsuario() + " - Productos: " + productosComprados);

        showAlert(Alert.AlertType.INFORMATION,
                "Compra realizada con éxito:\nProductos: " + productosComprados + "\nTotal: " + totalPrecio + "€");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}