package gestionar;

import excepciones.NoStockException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionVentas {

    private static final String URL = "jdbc:mysql://localhost:3306/koktelcitos?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_spanish_ci";
    private static final String USER = "root";
    private static final String PASS = "Kigali2020";

    private static Connection conexion;
    private static Statement sentencia;
    private static Statement sentencia2;

    /**
     * 
     * @param nombreProducto
     * @param cantidad
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws NoStockException
     */
    //hacer que genere un ticket para el cliente con un archivo txt
    public static Boolean Venta(String nombreProducto, int cantidad)
            throws ClassNotFoundException, SQLException, NoStockException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);

            try {
                sentencia = conexion.createStatement();

                // obtener la info del producto
                ResultSet rs = sentencia.executeQuery(
                        "SELECT id_producto, precio, stock FROM productos WHERE nombre = '" + nombreProducto + "'");

                if (GestionProductos.existeProducto(nombreProducto) == false) {
                    return false;
                } else {
                    int id_producto = 0;
                    double precio = 0;
                    int stockActual = 0;

                    while (rs.next()) {
                        id_producto = rs.getInt("id_producto");
                        precio = rs.getDouble("precio");
                        stockActual = rs.getInt("stock");
                    }

                    double subtotal = precio * cantidad;

                    // insertar en la tabla ventas
                    sentencia.executeUpdate(
                            "INSERT INTO ventas (id_producto, cantidad, subtotal) " +
                                    "VALUES (" + id_producto + "," + cantidad + ", "
                                    + subtotal
                                    + ")");

                    GestionProductos.actualizarCantidad(nombreProducto, cantidad, GestionProductos.PRODUCTO.VENTA);

                    return true;
                }

            } catch (SQLException e) {
                conexion.rollback(); // Si hay error, revertir cambios
                System.out.println("Error en la transacción: " + e.getMessage());
                throw e;
            }

        } finally {
            if (sentencia != null)
                sentencia.close();
            if (conexion != null)
                conexion.close();
        }
    }

    public static String mostrarVentas() throws ClassNotFoundException {
        String ventas = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();

            // ejecutamos la sentencia
            ResultSet rs = sentencia.executeQuery("SELECT * FROM ventas");

            while (rs.next()) {
                ventas += "ID: " + rs.getInt("id_ventas") + "|ID Producto: " + rs.getInt("id_producto")
                        + "|Cantidad: " + rs.getInt("cantidad") + "|Subtotal: " + rs.getFloat("subtotal") + "\n";
            }
            return ventas;
            // cerramos la sentencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                // cerramos la conexion
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String ultimaVenta() throws ClassNotFoundException {
        String[] ultimo = mostrarVentas()
                .split("\n");

        return ultimo[ultimo.length - 1];
    }
}
