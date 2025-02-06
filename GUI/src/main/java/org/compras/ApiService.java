package org.compras;

import org.compras.model.Compra;
import org.compras.model.Producto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {

    @GET("/productos")
    Call<List<Producto>> getProductos();

    @POST("/producto")
    Call<Producto> agregarProducto(@Body Producto producto);

    @PUT("/producto/{id}")
    Call<Producto> actualizarProducto(@Path("id") Long id, @Body Producto producto);

    @DELETE("/producto/{id}")
    Call<Void> eliminarProducto(@Path("id") Long id);

    @POST("/compra")
    Call<Compra> realizarCompra(@Body Compra compra);
}

