package appfinanzas;

import java.util.Scanner;

/**
 * comentarios
 * @author Giuliana
 */
public class AppFinanzas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Saludo y creación de usuario
        System.out.println("Hola! Bienvenid@ a tu calculadora financiera 💰");
        System.out.print("Antes de comenzar, ingresa tu nombre: ");
        String nombreUsuario = scanner.nextLine();
        
        // creacion del objeto usuario
        Usuario usuario = new Usuario(nombreUsuario);
        System.out.println("¡Listo! Usuario registrado con ID: " + usuario.getId());
        
        // Registro de ingresos
        System.out.println("Ahora vamos a registrar tus ingresos.");
        usuario.registrarIngreso(scanner); // Método que luego agregamos

        // Registro de gastos
        System.out.println("¡Genial! Ahora continuamos registrando tus gastos.");
        usuario.registrarGasto(scanner); // Método que luego agregamos

        // Mostrar menú
        mostrarMenu(usuario, scanner);
    }
    
    public static void mostrarMenu(Usuario usuario, Scanner scanner) {
        int opcion = 0;

        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1) Ver Caja Libre");
            System.out.println("2) Ver Presupuesto Financiero");
            System.out.println("3) Ver Perfil Financiero");
            System.out.println("4) Modificar Ingresos");
            System.out.println("5) Modificar Gastos");
            System.out.println("6) Salir");
            System.out.print("Seleccioná una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    usuario.verCajaLibre();
                    break;
                case 2:
                    usuario.verPresupuesto();
                    break;
                case 3:
                   usuario.verPerfilFinanciero();
                    break;
                case 4:
                    usuario.registrarIngreso(scanner);
                    break;
                case 5:
                    usuario.registrarGasto(scanner);
                    break;
                case 6:
                    System.out.println("¡Gracias por usar tu app financiera, " + usuario.getNombre() + "!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 6);
    }
    
}

/**




public class AppFinanzas {

        

        

        
    }

    
}

 */