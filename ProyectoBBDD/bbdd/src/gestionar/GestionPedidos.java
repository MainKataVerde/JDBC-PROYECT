package gestionar;

import excepciones.NoStockException;
import java.sql.Connection;//se usar para establecer la conexcion
import java.sql.DriverManager;// gestiona los driver de la base de datos , crea conexiones
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;//se usa para manejar excepciones de sql

/**
 * Clase en la que gestrionamos los pedidos hacia los proveedores
 */
public class GestionPedidos {
    private static final String URL = "jdbc:mysql://localhost:3306/koktelcitos?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_spanish_ci";
    private static final String USER = "root";
    private static final String PASS = "Kigali2020";

    private static Connection conexion;
    private static Statement sentencia;
    private static Statement sentencia2;

    /**
     * Contructora de la clase pedido , cada instacia de esta clase es un pedido
     * nuevo
     * 
     * @param precio_total precio total del pedido
     * @param cantidad     cantidad pedida
     * @param id_proveedor id del proveedor al que se le solicita el pedido
     * @param id_producto  id del producto que se solicita
     */
    // hacer que genere un ticket para nosotros con un archivo txt
    public GestionPedidos(float precio_total, int cantidad, int id_proveedor, int id_producto) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            sentencia = conexion.createStatement();
            sentencia.executeUpdate("INSERT INTO pedidos (id_proveedor , fecha_pedido , cantidad_total) VALUE ('"
                    + id_proveedor + "',CURDATE(),'" + precio_total + "')");
            ResultSet rs = sentencia.executeQuery("SELECT id_pedido FROM pedidos WHERE id_proveedor = '" + id_proveedor
                    + "' AND fecha_pedido = CURDATE() AND cantidad_total = '" + precio_total + "'");

            int id_pedido = 0;

            while (rs.next()) {
                id_pedido = rs.getInt("id_pedido");
            }

            sentencia2 = conexion.createStatement();
            sentencia2.executeUpdate(
                    "INSERT INTO detalles_pedidos (id_pedido , id_producto , cantidad , subtotal) VALUE ('" + id_pedido
                            + "','"
                            + id_producto + "','" + cantidad + "','" + precio_total + "')");

            String nombre = GestionProductos.buscarPorID(id_producto).split("\\|")[1].trim()
                    .split(":")[1].trim();
            GestionProductos.actualizarCantidad(nombre, cantidad, GestionProductos.PRODUCTO.COMPRA);
        } catch (SQLException | ClassNotFoundException | NoStockException e) {
            System.err.println(e);
        }
    }

    public static String mostrarPedidos() {
        String pedido = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            sentencia = conexion.createStatement();

            ResultSet rs = sentencia
                    .executeQuery("SELECT * FROM pedidos NATURAL JOIN detalles_pedidos NATURAL JOIN productos");

            while (rs.next()) {
                pedido += "ID Pedido : " + rs.getString("id_pedido") + " | ID Proveedor : "
                        + rs.getString("id_proveedor") + " | Fecha Del Pedido : " + rs.getString("fecha_pedido")
                        + " | ID Cantidad Total : " + rs.getInt("cantidad_total") + " | Nombre Producto : "
                        + rs.getString("nombre") + " | Cantidad : " + rs.getInt("cantidad") + " | Subtotal : "
                        + rs.getInt("subtotal") + "\n";
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }

        return pedido;
    }

    public static String ultimoPedido() {
        String[] ultimo = mostrarPedidos()
                .split("\n");

        return ultimo[ultimo.length - 1];
    }
}
