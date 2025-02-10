module org.compras {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires retrofit2.converter.gson;
    requires retrofit2;

    opens org.compras to javafx.fxml;
    exports org.compras;

    exports org.compras.controller to javafx.fxml;

    opens org.compras.controller to javafx.fxml;
    opens org.compras.model to javafx.fxml, com.google.gson;
    requires okhttp3;
    requires com.google.gson;
    requires okio;
    requires org.apache.commons.net;
}