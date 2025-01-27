import java.util.Scanner;
import gestionar.*;

/**
 * Clase en la que se ejecuta la logia
 */
public class Engine {
    public void start() {
        run();
    }

    public void run() {
        Boolean acction = false;
        Scanner sc = new Scanner(System.in);
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
                    System.out.println("\n1. Insertar producto" + "\n" + "2. Mostrar productos" + "\n" + "3. Volver");
                    int modoG = sc.nextInt();
                    switch (modoG) {
                        case 1:
                            System.out.println("Inserte los datos del nuevos producto");
                            System.out.print("Nombre: ");
                            String nombre = sc.next();
                            System.out.print("Precio: ");
                            double precio = sc.nextDouble();
                            System.out.print("Stock: ");
                            int stock = sc.nextInt();
                            System.out.print(
                                    "Seleccione la categoria: 1.Licores \n 2.Mixers \n 3.Frutas \n 4.Decoraciones \n 5. Otros");
                            int id_categoria = sc.nextInt();
                            System.out.println("Fecha de caducidad: ");
                            String fecha_caducidad = sc.next();
                            try {
                                if (GestionProductos.insertarProducto(nombre, precio, stock, id_categoria,
                                        fecha_caducidad)) {
                                    System.out.println("Producto insertado correctamente");
                                } else {
                                    System.out.println("Error al insertar el producto");

                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                System.out.print("\033[H\033[2J");
                                System.out.println(GestionProductos.mostrarProductos());
                                System.out.println("¿Quieres continuar o cerrar el programa? 1.Continuar\n2.Cerrar");
                                int continuar = sc.nextInt();
                                if (continuar == 2) {
                                    acction = true;
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
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
        System.out.println("SALIENDO....");
        sc.close();
    }
}