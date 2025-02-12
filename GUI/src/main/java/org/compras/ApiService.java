// ApiService.java - Restaurado y adaptado
package org.compras;

import java.util.List;

import org.compras.model.Cliente;
import org.compras.model.Compra;
import org.compras.model.Producto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @POST("/api/login")
    Call<String> registrarUsuario(@Body Cliente cliente);


    @GET("/api/user")
    Call<ResponseBody> verificarUsuario(@Query("nombre") String nombre, @Query("contrasena") String contrasena);

}




