import excepciones.NoStockException;
import gestionar.*;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase en la que se ejecuta la logia
 */
public class Engine {
    public void start() {
        run();
    }

    public void run() {
        Boolean acction = false;
        Scanner sc2;
        try (Scanner sc = new Scanner(System.in)) {
            sc2 = new Scanner(System.in);
            do {
                System.out.print("\033[H\033[2J");
                System.out.println("BIENVENIDO A LA GESTION DE SU TIENDA");
                System.out.println("--------SELECCIONE UNA OPCION--------");
                System.out.println("1. Gestion de productos" + "\n" + "2. Gestion de Proveedores" + "\n"
                        + "3. Gestion de Ventas" + "\n" + "4. Salir");
                int modo = sc.nextInt();
                switch (modo) {
                    case 1:
                        System.out.print("\033[H\033[2J");
                        System.out.println("--------GESTION DE PRODUCTOS--------");
                        System.out.println("\n1. Insertar producto" + "\n" + "2. Mostrar productos" + "\n"
                                + "3. Borrar producto" + "\n" + "4. Volver");
                        int modoG = sc.nextInt();
                        switch (modoG) {
                            case 1:
                                System.out.println("Inserte los datos del nuevos producto");
                                System.out.print("Nombre: ");
                                String nombre = sc2.nextLine();
                                try {
                                    if (GestionProductos.existeProducto(nombre)) {
                                        System.out.println(
                                                "El procudto " + nombre + " ya existe asique lo añadimeros al stock");
                                        System.out.print("Inserte la cantidad a introducir: ");
                                        int stock = sc.nextInt();
                                        try {
                                            GestionProductos.actualizarCantidad(nombre, stock,
                                                    GestionProductos.PRODUCTO.COMPRA);
                                        } catch (NoStockException e) {
                                            System.out.println(e);
                                        }
                                        System.out.println(GestionProductos.elementoBuscado(nombre));
                                        System.out
                                                .println(
                                                        "¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                        int continuar = sc.nextInt();
                                        if (continuar == 2) {
                                            acction = true;
                                        }
                                        System.out.print("\033[H\033[2J");
                                    } else {
                                        System.out.print("Precio: ");
                                        double precio = sc.nextDouble();
                                        System.out.print("Stock: ");
                                        int stock = sc.nextInt();
                                        System.out.print(
                                                "Seleccione la categoria: 1.Licores | 2.Mixers | 3.Frutas | 4.Decoraciones |5. Otros \nElige: ");
                                        int id_categoria = sc.nextInt();
                                        System.out.print("Fecha de caducidad: ");
                                        String fecha_caducidad = sc.next();
                                        System.out.print(
                                                "Seleccione la categoria: 1.Distribuidora Licores MX | 2.Mixers y Más S.A. | 3.Frutas Frescas Express | 4.Hielos Premium |5. Decoraciones Cocteleras \nElige: ");
                                        int id_proveedor = sc.nextInt();
                                        try {
                                            if (GestionProductos.insertarProducto(nombre, precio, stock, id_categoria,
                                                    fecha_caducidad, id_proveedor)) {
                                                System.out.println("Producto insertado correctamente");
                                                System.out.println(GestionProductos.elementoBuscado(nombre));
                                                System.out
                                                        .println(
                                                                "¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                                int continuar = sc.nextInt();
                                                if (continuar == 2) {
                                                    acction = true;
                                                }
                                                System.out.print("\033[H\033[2J");
                                            } else {
                                                System.out.println("Error al insertar el producto");

                                            }
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    System.out.print("\033[H\033[2J");
                                    System.out.println(GestionProductos.mostrarProductos());
                                    System.out
                                            .println("¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                    int continuar = sc.nextInt();
                                    if (continuar == 2) {
                                        acction = true;
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                    acction = true;
                                }
                                break;
                            case 3:
                                try {
                                    System.out.print("\033[H\033[2J");
                                    System.out.println(GestionProductos.mostrarProductos());
                                    System.out.println(
                                            "Introduce el id de los productos a borrar separados con ','\nEscribe aqui: ");
                                    String id_producto = sc.next();
                                    if (GestionProductos.borrarProducto(id_producto)) {
                                        System.out.print("\033[H\033[2J");
                                        System.out.println(GestionProductos.mostrarProductos());
                                        System.out.println("Producto borrado correctamente");
                                        System.out
                                                .println(
                                                        "¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                        int continuar = sc.nextInt();
                                        if (continuar == 2) {
                                            acction = true;
                                        }
                                        System.out.print("\033[H\033[2J");
                                    } else {
                                        System.out.println("Error al borrar el producto");
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        System.out.print("\033[H\033[2J");
                        System.out.println("--------GESTION DE PROVEEDORES--------");
                        System.out.println("\n1. Insertar proveedor" + "\n" + "2. Mostrar proveedores" + "\n"
                                + "3. Realizar pedido" + "\n" + "4. Volver");
                        modoG = sc.nextInt();

                        switch (modoG) {
                            case 1:
                                System.out.println("Inserte los datos del nuevo proveedor");
                                System.out.print("Nombre: ");
                                String nombre = sc2.nextLine();
                                System.out.print("Contacto: ");
                                String contacto = sc2.nextLine();
                                System.out.print("Direccion: ");
                                String direccion = sc2.nextLine();
                                try {
                                    if (GestionProveedores.insertarProveedor(nombre, contacto, direccion)) {
                                        System.out.println("Proveedor insertado correctamente");
                                        System.out.println(GestionProveedores.mostrarProveedores());
                                        System.out
                                                .println(
                                                        "¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                        int continuar = sc.nextInt();
                                        if (continuar == 2) {
                                            acction = true;
                                        }
                                        System.out.print("\033[H\033[2J");
                                    } else {
                                        System.out.println("Error al insertar el proveedor");
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (SQLException ex) {
                                }
                                break;
                            case 2:
                                System.out.print("\033[H\033[2J");
                                System.out.println(GestionProveedores.mostrarProveedores());
                                System.out
                                        .println("¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                int continuar = sc.nextInt();
                                if (continuar == 2) {
                                    acction = true;
                                }
                                break;
                            case 3:
                                System.out.println(GestionProveedores.mostrarProveedores());
                                System.out.print("Introduce el id del proveedor: ");
                                int id_proveedor = sc.nextInt();
                                System.out.println("Aqui estan todos los productos del ese proveedor");
                                System.out.println(GestionProductos.mostrarProductosPorProveedor(id_proveedor));
                                System.out.print("Elige el producto(ID) que proporciona ese proveedor:");
                                int id_producto = sc.nextInt();
                                System.out.print("Introduce la cantidad a comprar: ");
                                int cantidad = sc.nextInt();
                                float precioTotal = cantidad * GestionProductos.obtenerPrecioProducto(id_producto);
                                GestionPedidos pedido = new GestionPedidos(precioTotal, cantidad,
                                        id_proveedor, id_producto);
                                System.out.println("Pedido realizado correctamente");
                                // hacer que elijan a los preoveedores y cuando los elige salgan los productos
                                // de ese proveedor y luego eliges el producto
                                System.out.println(GestionProductos.buscarPorID(id_producto));
                                System.out
                                        .println(
                                                "¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                continuar = sc.nextInt();
                                if (continuar == 2) {
                                    acction = true;
                                }
                                System.out.print("\033[H\033[2J");
                                break;

                            default:
                                break;
                        }
                    case 3:
                        System.out.print("\033[H\033[2J");
                        System.out.println("--------GESTION DE VENTAS--------");
                        System.out.println("\n1. Realizar venta" + "\n" + "2. Mostrar ventas" + "\n" + "3. Volver");
                        modoG = sc.nextInt();
                        switch (modoG) {
                            case 1:
                                System.out.println(GestionProductos.mostrarProductos());
                                System.out.print("Introduce el nombre del producto: ");
                                String nombreProducto = sc2.nextLine();
                                System.out.print("Introduce la cantidad: ");
                                int cantidad = sc.nextInt();
                                try {
                                    if (GestionVentas.Venta(nombreProducto, cantidad)) {
                                        System.out.println("Venta realizada correctamente");
                                        System.out.println(GestionVentas.ultimaVenta());
                                        System.out.println(nombreProducto + " restantes:");
                                        System.out.println(GestionProductos.elementoBuscado(nombreProducto));
                                        System.out
                                                .println(
                                                        "¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                        int continuar = sc.nextInt();
                                        if (continuar == 2) {
                                            acction = true;
                                        }
                                        System.out.print("\033[H\033[2J");
                                    } else {
                                        System.out.println("Error al realizar la venta");
                                        System.out
                                                .println(
                                                        "¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                        int continuar = sc.nextInt();
                                        if (continuar == 2) {
                                            acction = true;
                                        }
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (NoStockException ex) {
                                }
                                break;
                            case 2:
                                System.out.print("\033[H\033[2J");
                                System.out.println("--------VENTAS--------");
                                System.out.println(GestionVentas.mostrarVentas());
                                System.out
                                        .println("¿Quieres continuar o cerrar el programa?\n1.Continuar\n2.Cerrar");
                                int continuar = sc.nextInt();
                                if (continuar == 2) {
                                    acction = true;
                                }
                                break;
                            default:
                                break;
                        }
                        break;

                    case 4:
                        acction = true;
                        break;
                    default:
                        break;
                }

            } while (!acction);
            System.out.println("SALIENDO.... Good bye world");
        } catch (ClassNotFoundException ex) {
        }

    }
}