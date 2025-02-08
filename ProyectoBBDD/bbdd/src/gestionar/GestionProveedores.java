package gestionar;

import java.sql.Connection;//se usar para establecer la conexcion
import java.sql.DriverManager;// gestiona los driver de la base de datos , crea conexiones
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;//se usa para manejar excepciones de sql

/**
 * Clase encargada de gestionar los productos
 */
public class GestionProveedores {
    private static final String URL = "jdbc:mysql://localhost:3306/koktelcitos?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_spanish_ci";
    private static final String USER = "root";
    private static final String PASS = "Kigali2020";

    private static Connection conexion;
    private static Statement sentencia;

    /**
     * Metodo que muestra todos los proveedores
     * 
     * @return String con todos los proveedores
     */
    public static String mostrarProveedores() {
        String proveedores = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            sentencia = conexion.createStatement();

            ResultSet rs = sentencia.executeQuery("SELECT * FROM proveedores");

            while (rs.next()) {
                proveedores += "ID: " + rs.getInt("id_proveedor") + "| Nombre: " + rs.getString("nombre_proveedor")
                        + "| Telefono: "
                        + rs.getString("contacto") + "| Direccion: " + rs.getString("direccion") + "\n";
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
        }
        return proveedores;
    }

    /**
     * Metodo que inserta un nuevo proveedor
     * 
     * @param nombre
     * @param contacto
     * @param direccion
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Boolean insertarProveedor(String nombre, String contacto, String direccion)
            throws ClassNotFoundException, SQLException {

        try {
            if (!existeProveedor(nombre)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASS);
                sentencia = conexion.createStatement();

                String sql = "INSERT INTO proveedores (nombre, contacto, direccion) VALUES ('"
                        + nombre + "', '" + contacto + "', '" + direccion + "')";

                sentencia.executeUpdate(sql);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar proveedor: " + e.getMessage());
            throw e;
        } finally {
            if (sentencia != null)
                sentencia.close();
            if (conexion != null)
                conexion.close();
        }
    }

    /**
     * Metodo que dice si un proveedor existe ya en la base de datos
     * 
     * @param nombre nombre del proveedor
     * @return true si existe, false si no
     */
    public static Boolean existeProveedor(String nombre) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            sentencia = conexion.createStatement();

            ResultSet rs = sentencia.executeQuery("SELECT * FROM proveedores WHERE nombre = '" + nombre + "'");

            return rs.next();

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
            return false;
        }

    }
}
