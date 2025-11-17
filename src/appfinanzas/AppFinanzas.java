package appfinanzas;

import appfinanzas.controlador.UsuarioController;
import appfinanzas.vista.ConsolaVistaUsuario; 
import appfinanzas.vista.VistaUsuario;
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

        VistaUsuario vista = new ConsolaVistaUsuario(scanner);
        UsuarioController controller = new UsuarioController(vista);
        controller.iniciar();
    }
}
