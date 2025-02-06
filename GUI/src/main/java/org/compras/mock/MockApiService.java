package org.compras.mock;

import okio.Timeout;
import org.compras.ApiService;
import org.compras.model.Compra;
import org.compras.model.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MockApiService implements ApiService {

    private List<Producto> productos = new ArrayList<>();

    public MockApiService() {
        productos.add(new Producto(1L, "Laptop", 1000.0, 5));
        productos.add(new Producto(2L, "Smartphone", 500.0, 10));
    }

    @Override
    public Call<List<Producto>> getProductos() {
        return createMockCall(productos);
    }

    private long nextId = 3;

    @Override
    public Call<Producto> agregarProducto(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(nextId++);
        }
        productos.add(producto);
        return createMockCall(producto);
    }


    @Override
    public Call<Producto> actualizarProducto(Long id, Producto productoActualizado) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                p.setNombre(productoActualizado.getNombre());
                p.setPrecio(productoActualizado.getPrecio());
                p.setStock(productoActualizado.getStock());
                break;
            }
        }
        return createMockCall(productoActualizado);
    }

    @Override
    public Call<Void> eliminarProducto(Long id) {
        productos.removeIf(p -> p.getId() != null && p.getId().equals(id));
        return createMockCall(null);
    }

    @Override
    public Call<Compra> realizarCompra(Compra compra) {
        System.out.println("Simulación de compra realizada para: " + compra.getUsuario());
        return createMockCall(null);
    }

    // Método genérico para simular respuestas
    private <T> Call<T> createMockCall(T response) {
        return new Call<T>() {
            @Override
            public void enqueue(Callback<T> callback) {
                callback.onResponse(this, Response.success(response));
            }

            @Override public Response<T> execute() { return Response.success(response); }
            @Override public boolean isExecuted() { return false; }
            @Override public void cancel() {}
            @Override public boolean isCanceled() { return false; }
            @Override public Call<T> clone() { return this; }
            @Override public okhttp3.Request request() { return null; }
            @Override public Timeout timeout() { return null; }
        };
    }
}
