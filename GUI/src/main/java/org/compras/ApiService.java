// ApiService.java - Restaurado y adaptado
package org.compras;

import okhttp3.ResponseBody;
import org.compras.model.Cliente;
import org.compras.model.Compra;
import org.compras.model.Producto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {

    @GET("/api/productos")
    Call<List<Producto>> getProductos();

    @POST("/api/producto")
    Call<Producto> agregarProducto(@Body Producto producto);

    @PUT("/api/producto/{id}")
    Call<String> actualizarProducto(@Path("id") Integer id, @Body Producto producto);

    @DELETE("/api/producto/{id}")
    Call<String> eliminarProducto(@Path("id") Integer id);

    @POST("/api/compra")
    Call<Compra> realizarCompra(@Body Compra compra);

    // Restaurado para que /api/login sea el registro
    @POST("/api/login")
    Call<String> registrarUsuario(@Body Cliente cliente);


    @GET("/api/user")
    Call<ResponseBody> verificarUsuario(@Query("nombre") String nombre, @Query("contrasena") String contrasena);

}




