package gestionar;

import excepciones.NoStockException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase encarda de realizar las bentas
 */
public class GestionVentas {

    private static final String URL = "jdbc:mysql://localhost:3306/koktelcitos?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_spanish_ci";
    private static final String USER = "root";
    private static final String PASS = "Kigali2020";

    private static Connection conexion;
    private static Statement sentencia;

    /**
     * 
     * @param nombreProducto
     * @param cantidad
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws NoStockException
     */
    // hacer que genere un ticket para el cliente con un archivo txt
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

                    while (rs.next()) {
                        id_producto = rs.getInt("id_producto");
                        precio = rs.getDouble("precio");

                    }

                    double subtotal = precio * cantidad;

                    // insertar en la tabla ventas
                    sentencia.executeUpdate(
                            "INSERT INTO ventas (id_producto, cantidad, subtotal) " +
                                    "VALUES (" + id_producto + "," + cantidad + ", "
                                    + subtotal
                                    + ")");

                    GestionProductos.actualizarCantidad(nombreProducto, cantidad, GestionProductos.PRODUCTO.VENTA);
                    System.out.println("Generando recivo de venta");
                    recivoVenta();
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

    /**
     * Metodo que muestra todas las ventas de la base de datos
     * 
     * @return String con todas las ventas
     * @throws ClassNotFoundException
     */
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
            System.err.println(e);
            return null;
        } finally {
            try {
                // cerramos la conexion
                conexion.close();
            } catch (SQLException e) {
                System.err.println(e);
                return null;
            }
        }
    }

    /**
     * Metodo que devuelve la ultima venta realizada
     * 
     * @return String con los datos de la ultima venta
     * @throws ClassNotFoundException
     */
    public static String ultimaVenta() throws ClassNotFoundException {
        String[] ultimo = mostrarVentas()
                .split("\n");

        return ultimo[ultimo.length - 1];
    }

    /**
     * Metodo encargado de generar recivos de Venta en un txt , cada pedido es un
     * txt nuevo
     */
    public static void recivoVenta() {
        try {
            String VentaNum = mostrarVentas().split("\\|")[1].trim()
                    .split(":")[1].trim();

            String ruta = "C:/Users/PORTATIL DE MAKINON/Desktop/CLASES OMG/TRABAJOS/JONY/2ºDAM/Acceso_a_datos/ProyectoBBDD/bbdd/src/tickets/ventas/venta_"
                    + VentaNum + ".txt";
            File archivo = new File(ruta);
            if (archivo.createNewFile()) {
                System.out.println("Ticket de Compra creado " + archivo.getName());

            }

            try (FileWriter fw = new FileWriter(archivo)) {
                fw.write(ultimaVenta());
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }
}
