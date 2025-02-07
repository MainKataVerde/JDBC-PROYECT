package gestionar;

import excepciones.NoStockException;//se usar para establecer la conexcion
import java.sql.Connection;// gestiona los driver de la base de datos , crea conexiones
import java.sql.DriverManager;
import java.sql.ResultSet;//se usa para manejar excepciones de sql
import java.sql.SQLException;//se usa para ejecutar sentencias sql
import java.sql.Statement;

/**
 * Clase que se encarga de gestionar los productos de la base de datos
 */
public class GestionProductos {
    // elementos de conexion a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/koktelcitos?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_spanish_ci";
    private static final String USER = "root";
    private static final String PASS = "Kigali2020";

    private static Connection conexion;
    private static Statement sentencia;
    private static Statement sentencia2;

    public enum PRODUCTO {
        VENTA, COMPRA
    }

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
     * @param id_proveedor    id del proveedor del producto
     * @return true si se ha insertado correctamente, false si ha habido algun error
     * @throws ClassNotFoundException
     */
    public static Boolean insertarProducto(String nombre, double precio, int stock, int id_categoria,
            String fecha_caducidad, int id_proveedor) throws ClassNotFoundException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();
            sentencia2 = conexion.createStatement();

            sentencia2.executeUpdate(
                    "INSERT INTO productos (nombre, id_categoria , precio , stock , fecha_caducidad , id_proveedor) VALUES ('"
                            + nombre
                            + "', " + id_categoria + ", " + precio
                            + ", " + stock + ", '" + fecha_caducidad + "', '" + id_proveedor + "')");
            // ejecutamos la sentencia

            // cerramos la sentencia
            sentencia.close();
            return true;
        } catch (Exception e) {
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

    /**
     * Metodo que imprime los productos de la base de datos
     * 
     * @return String con todos los productos de la base de datos
     * @throws ClassNotFoundException
     */
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

    /**
     * Metodo que decuelve el ulimo producto del base de datos
     * 
     * @return String con el ultimo producto
     * @throws ClassNotFoundException
     */
    public static String mostrarUltimoProducto() throws ClassNotFoundException {
        String[] ultimo = mostrarProductos()
                .split("\n");

        return ultimo[ultimo.length - 1];
    }

    /**
     * Metodo que nos permite borrar productos de la base de datos
     * 
     * @param id_producto
     * @return
     * @throws ClassNotFoundException
     */
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

    /**
     * Este motodo te dice si el producto exite en la base de datos
     * 
     * @param nombre_Producto Nombre del producro que queremos buscar
     * @return true si existe false si no existe
     * @throws ClassNotFoundException
     */
    public static Boolean existeProducto(String nombre_Producto) throws ClassNotFoundException {
        Boolean find = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();

            // ejecutamos la sentencia
            ResultSet rs = sentencia
                    .executeQuery("SELECT nombre FROM productos WHERE nombre = '" + nombre_Producto + "'");

            if (rs.next()) {
                find = true;
            }
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

        return find;
    }

    /**
     * Metodo que actualiza la cantidad de producto , tanto restar como sumar
     * 
     * @param nombre_producto Nombre del producto a actualizar
     * @param stock           Numero de a restar o a sumar al stock
     * @param producto        Enum que determina la accion que vamos a hacer puede
     *                        ser tanto VENDER como COMPRAR
     * @throws ClassNotFoundException
     * @throws NoStockException
     */
    public static void actualizarCantidad(String nombre_producto, int stock, PRODUCTO producto)
            throws ClassNotFoundException, NoStockException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        // establecemos la conexion
        try {
            conexion = DriverManager.getConnection(URL, USER, PASS);
            sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("SELECT * FROM productos WHERE nombre = '" + nombre_producto + "'");
            if (producto.equals(PRODUCTO.VENTA)) {
                if (rs.next()) {
                    if ((rs.getInt("stock") - stock) < 0) {
                        throw new NoStockException("No hay stocksuficiente");
                    } else {
                        int newStock = rs.getInt("stock") - stock;
                        Statement updateStatement = conexion.createStatement();
                        updateStatement.executeUpdate(
                                "UPDATE productos SET stock = " + newStock + " WHERE nombre = '" + nombre_producto
                                        + "'");
                        updateStatement.close();
                    }
                }
            } else if (producto.equals(PRODUCTO.COMPRA)) {
                if (rs.next()) {
                    int newStock = rs.getInt("stock") + stock;
                    Statement updateStatement = conexion.createStatement();
                    updateStatement.executeUpdate(
                            "UPDATE productos SET stock = " + newStock + " WHERE nombre = '" + nombre_producto + "'");
                    updateStatement.close();
                }
            }
            rs.close();
            sentencia.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexion != null)
                    conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param nombre_Producto
     * @return
     * @throws ClassNotFoundException
     */
    public static String elementoBuscado(String nombre_Producto) throws ClassNotFoundException {
        String productos = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();

            // ejecutamos la sentencia
            ResultSet rs = sentencia.executeQuery("SELECT * FROM productos WHERE nombre = '" + nombre_Producto + "'");

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

    /**
     * 
     * @param id_producto
     * @return
     */
    public static String buscarPorID(int id_producto) {
        String productos = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();

            // ejecutamos la sentencia
            ResultSet rs = sentencia.executeQuery("SELECT * FROM productos WHERE id_producto = " + id_producto);

            while (rs.next()) {
                productos += "ID: " + rs.getInt("id_producto") + "|Nombre: " + rs.getString("nombre") + " |Precio: "
                        + rs.getDouble("precio") + "|Stock: " + rs.getInt("stock") + "|Categoria: "
                        + rs.getString("id_categoria")
                        + "\n";
            }
            return productos;
            // cerramos la sentencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException ex) {
        } finally {
            try {
                // cerramos la conexion
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return productos;
    }

    public static float obtenerPrecioProducto(int id_producto) {
        int precio = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();

            // ejecutamos la sentencia
            ResultSet rs = sentencia.executeQuery("SELECT precio FROM productos WHERE id_producto = " + id_producto);

            while (rs.next()) {
                precio = rs.getInt("precio");
            }
            return precio;
            // cerramos la sentencia
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } catch (ClassNotFoundException ex) {
        } finally {
            try {
                // cerramos la conexion
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return precio;
    }

    /**
     * 
     * @param id_proveedor
     * @return
     * @throws ClassNotFoundException
     */

    public static String mostrarProductosPorProveedor(int id_proveedor) throws ClassNotFoundException {
        String productos = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establecemos la conexion
            conexion = DriverManager.getConnection(URL, USER, PASS);
            // creamos la sentencia
            sentencia = conexion.createStatement();

            // ejecutamos la sentencia
            ResultSet rs = sentencia.executeQuery("SELECT * FROM productos WHERE id_proveedor = " + id_proveedor);

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
}