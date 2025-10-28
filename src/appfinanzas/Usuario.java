package appfinanzas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Representa al usuario del sistema financiero.
 * Puede registrar ingresos y gastos, y consultar su caja libre.
 * @author Giuliana
 */
public class Usuario {
    private String nombre;
    private String id;
    private ArrayList<Ingreso> ingresos;
    private ArrayList<Gasto> gastos;

    public Usuario(String nombre) {
        this.nombre = nombre;
        this.id = java.util.UUID.randomUUID().toString();
        this.ingresos = new ArrayList<>();
        this.gastos = new ArrayList<>();
    }

    // ------------------------------
    // REGISTRO DE INGRESOS
    // ------------------------------
    public void registrarIngreso(Scanner scanner) {
        String continuar;

        do {
            try {
                System.out.print("Nombre del ingreso: ");
                String nombreIngreso = scanner.next();

                System.out.print("Monto: ");
                float monto = scanner.nextFloat();
                scanner.nextLine(); // Limpiar buffer

                System.out.print("Fecha ingreso (dd/MM/yyyy): ");
                String fechaTexto = scanner.nextLine();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = sdf.parse(fechaTexto);

                boolean esFuturo = false;
                boolean respuestaValida = false;

                while (!respuestaValida) {
                    System.out.print("¿Es un ingreso futuro? (sí/no): ");
                    String respuesta = scanner.nextLine().toLowerCase();

                    if (respuesta.equals("sí") || respuesta.equals("si")) {
                        esFuturo = true;
                        respuestaValida = true;
                    } else if (respuesta.equals("no")) {
                        esFuturo = false;
                        respuestaValida = true;
                    } else {
                        System.out.println("❌ Respuesta no válida. Escribí exactamente 'sí' o 'no'.");
                    }
                }

                Ingreso nuevoIngreso = new Ingreso(nombreIngreso, monto, fecha, esFuturo);
                ingresos.add(nuevoIngreso);

                System.out.println("✅ Ingreso registrado correctamente: " + nuevoIngreso);

            } catch (Exception e) {
                System.out.println("⚠️ Error al registrar el ingreso. Intenta nuevamente.");
                scanner.nextLine(); // limpiar buffer si hay error
            }

            // Validación estricta del "sí" o "no"
            do {
                System.out.print("¿Querés registrar otro ingreso? (sí/no): ");
                continuar = scanner.nextLine().toLowerCase();

                if (!(continuar.equals("sí") || continuar.equals("si") || continuar.equals("no"))) {
                    System.out.println("❌ Respuesta no válida. Escribí exactamente 'sí' o 'no'.");
                }
            } while (!(continuar.equals("sí") || continuar.equals("si") || continuar.equals("no")));

        } while (continuar.equals("sí") || continuar.equals("si"));
    }
    
    // ------------------------------
    // REGISTRO DE GASTOS
    // ------------------------------
    public void registrarGasto(Scanner scanner) {
        String continuar = "";

        do {
            try {
                System.out.print("Nombre del gasto: ");
                String nombre = scanner.nextLine();

                System.out.print("Monto: ");
                float monto = scanner.nextFloat();
                scanner.nextLine(); // 🧹 Limpiar buffer antes de leer texto

                boolean respuestaValida = false;
                String categoria = "";
                boolean esFijo = false;

                // 🔹 Preguntar si es gasto fijo o variable
                while (!respuestaValida) {
                    System.out.print("¿Es un gasto fijo o variable? (fijo/variable): ");
                    String respuesta = scanner.nextLine().toLowerCase().trim();

                    if (respuesta.equals("fijo")) {
                        categoria = "fijo";
                        esFijo = true;
                        respuestaValida = true;
                    } else if (respuesta.equals("variable")) {
                        categoria = "variable";
                        esFijo = false;
                        respuestaValida = true;
                    } else {
                        System.out.println("❌ Respuesta no válida. Escribí exactamente 'fijo' o 'variable'.");
                    }
                }

                // 🔹 Crear el gasto y agregarlo a la lista
                Gasto nuevoGasto = new Gasto(monto, nombre, esFijo, categoria);
                gastos.add(nuevoGasto);

                System.out.println("✅ Gasto registrado correctamente: " + nuevoGasto);

            } catch (Exception e) {
                System.out.println("⚠️ Error al registrar el gasto. Intenta nuevamente.");
                scanner.nextLine(); // 🧹 Limpiar buffer si hay error
            }

            // 🔹 Preguntar si desea registrar otro gasto
            do {
                System.out.print("¿Querés registrar otro gasto? (sí/no): ");
                continuar = scanner.nextLine().toLowerCase().trim();

                if (!(continuar.equals("sí") || continuar.equals("si") || continuar.equals("no"))) {
                    System.out.println("❌ Respuesta no válida. Escribí exactamente 'sí' o 'no'.");
                }

            } while (!(continuar.equals("sí") || continuar.equals("si") || continuar.equals("no")));

        } while (continuar.equals("sí") || continuar.equals("si"));
    }


    // ------------------------------
    // CONSULTAS
    // ------------------------------
    public void verCajaLibre() {
        float totalIngresos = 0;
        float totalGastos = 0;

        for (Ingreso ingreso : ingresos) {
            if (ingreso.esDisponible()) {
                totalIngresos += ingreso.getMonto();
            }
        }

        for (Gasto gasto : gastos) {
            totalGastos += gasto.getMonto();
        }

        float cajaLibre = totalIngresos - totalGastos;
        System.out.println("💵 Tu caja libre actual es: $" + cajaLibre);
    }

    public void verPresupuesto() {
        System.out.println("📊 Esta función aún no está implementada.");
    }

    public void verPerfilFinanciero() {
        System.out.println("📊 Esta función aún no está implementada.");
    }

    // ------------------------------
    // GETTERS
    // ------------------------------
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
