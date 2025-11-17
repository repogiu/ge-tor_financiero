package appfinanzas.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Clase abstracta que representa una transacción financiera.
 * Es la SUPERCLASE de la cual heredarán Ingreso y Gasto.
 * Al ser abstracta, no se pueden crear objetos Transaccion directamente.
 */
public abstract class Transaccion {
    // Atributos privados: se acceden vía getters
    private double monto;
    private String nombre;
    private LocalDate fecha;
    private String categoria;

    /**
     * Constructor de la superclase - NO usa super() porque no hereda de nadie más
     * que Object (que tiene constructor sin argumentos)
     */
    public Transaccion(String nombre, double monto, LocalDate fecha, String categoria) {
        this.nombre = nombre;      // Se asume validado previamente en la capa de vista/controlador
        this.monto = monto;        // Se asume monto >= 0 validado en la vista
        this.fecha = fecha;        // Si fuese null se trataría antes; aquí no forzamos excepción
        this.categoria = categoria; // Simplificación: modelo liviano sin fail-fast
    }

    // Getters comunes para todas las transacciones
    public double getMonto() {
        return monto;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    /**
     * Método abstracto - cada subclase DEBE implementarlo
     * Define cómo impacta la transacción en la caja
     * - Ingresos: suma el monto (si no es futuro)
     * - Gastos: resta el monto
     */
    public abstract double calcularImpactoEnCaja();

    /**
     * Método para formatear la transacción como texto
     * Las subclases pueden usar super.toString() y agregar su información específica
     */
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String montoStr = String.format(Locale.forLanguageTag("es-AR"), "%.2f", monto);
        return nombre + " | $" + montoStr + " | " + fecha.format(fmt);
    }
}