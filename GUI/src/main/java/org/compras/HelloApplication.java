package org.compras;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.compras.controller.AdminController;
import org.compras.controller.ProductController;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginView();
    }

    public static void showLoginView() throws Exception {
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("/org/compras/login_view.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void showProductView(String username) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/compras/product_view.fxml"));
        Parent root = fxmlLoader.load();

        // Obtener el controlador y pasarle el username
        ProductController productController = fxmlLoader.getController();
        productController.setUsername(username);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Productos");
    }

    public static void showAdminView(String username) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/compras/admin_view.fxml"));
        Parent root = fxmlLoader.load();

        // Obtener el controlador y pasarle el username
        AdminController adminController = fxmlLoader.getController();
        adminController.setUsername(username);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Panel de Administrador");
    }


    public static void main(String[] args) {
        launch(args);
    }
}