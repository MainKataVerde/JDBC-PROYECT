package gestionar;

import java.sql.Connection;//se usar para establecer la conexcion
import java.sql.DriverManager;// gestiona los driver de la base de datos , crea conexiones
import java.sql.ResultSet;
import java.sql.SQLException;//se usa para manejar excepciones de sql
import java.sql.Statement;//se usa para ejecutar sentencias sql

/**
 * Clase que se encarga de gestionar los productos de la base de datos
 */
public class GestionProductos {
    // elementos de conexion a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/koktelcitos?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Kigali2020";

    private static Connection conexion;
    private static Statement sentencia;

    public GestionProductos() {

    }

    /**
     * Metodo que se encarga de insertar un producto en la base de datos
     * 
     * @param nombre          nombre del producto
     * @param precio          precio del producto
     * @param stock           stock del producto
     * @param id_categoria    id de la categoria del producto
     * @param fecha_caducidad fecha de caducidad del producto
     * @return true si se ha insertado correctamente, false si ha habido algun error
     * @throws ClassNotFoundException
     */
    public static Boolean insertarProducto(String nombre, double precio, int stock, int id_categoria,
            String fecha_caducidad) throws ClassNotFoundException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();
            // ejecutamos la sentencia
            sentencia.executeUpdate(
                    "INSERT INTO productos (nombre, id_categoria , precio , stock , fecha_caducidad) VALUES ('" + nombre
                            + "', " + id_categoria + ", " + precio
                            + ", " + stock + ", '" + fecha_caducidad + "')");
            // cerramos la sentencia
            sentencia.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                // cerramos la conexion
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static String mostrarProductos() throws ClassNotFoundException {
        String productos = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();

            // ejecutamos la sentencia
            ResultSet rs = sentencia.executeQuery("SELECT * FROM productos");

            while (rs.next()) {
                productos += "ID: " + rs.getInt("id_producto") + "|Nombre: " + rs.getString("nombre") + "|Precio: "
                        + rs.getDouble("precio") + "|Stock: " + rs.getInt("stock") + "|Categoria: "
                        + rs.getString("id_categoria")
                        + "\n";
            }
            return productos;
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

    public static String mostrarUltimoProducto() throws ClassNotFoundException {
        String[] ultimo = mostrarProductos()
                .split("\n");

        return ultimo[ultimo.length - 1];
    }

    public static Boolean borrarProducto(String id_producto) throws ClassNotFoundException {
        try {

            String[] ids = id_producto.split(",");

            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();
            // ejecutamos la sentencia

            for (String id : ids) {
                sentencia.executeUpdate("DELETE FROM productos WHERE id_producto = " + id);
            }
            // cerramos la sentencia
            sentencia.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                // cerramos la conexion
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}