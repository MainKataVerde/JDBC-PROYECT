package gestionar;

import java.sql.Connection;//se usar para establecer la conexcion
import java.sql.DriverManager;// gestiona los driver de la base de datos , crea conexiones
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;//se usa para manejar excepciones de sql

public class GestionProveedores {
    private static final String URL = "jdbc:mysql://localhost:3306/koktelcitos?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_spanish_ci";
    private static final String USER = "root";
    private static final String PASS = "Kigali2020";

    private static Connection conexion;
    private static Statement sentencia;

    public enum ACCION {
        INSERTAR,
        ELIMINAR
    }

    // crear un metodo que muestre todos los proveedores

    public static String mostrarProveedores() {
        String proveedores = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            sentencia = conexion.createStatement();

            ResultSet rs = sentencia.executeQuery("SELECT * FROM proveedores");

            while (rs.next()) {
                proveedores += "ID: " + rs.getInt("id_proveedor") + "| Nombre: " + rs.getString("nombre")
                        + "| Telefono: "
                        + rs.getString("contacto") + "| Direccion: " + rs.getString("direccion") + "\n";
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return proveedores;
    }

    /**
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
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            sentencia = conexion.createStatement();

            String sql = "INSERT INTO proveedores (nombre, contacto, direccion) VALUES ('"
                    + nombre + "', '" + contacto + "', '" + direccion + "')";

            sentencia.executeUpdate(sql);
            return true;

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
}
