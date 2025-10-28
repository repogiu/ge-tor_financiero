package appfinanzas;

import java.util.Date;

/**
 * Representa un gasto dentro del sistema financiero personal.
 * Incluye información sobre su monto, nombre, si es fijo o variable
 * y su categoría asociada.
 * 
 * @author Giuliana
 */
public class Gasto {
    private double monto;
    private String nombre;
    private boolean esFijo;
    private String categoria; // "fijo" o "variable"
    private Date fechaRegistro;

    // Constructor completo
    public Gasto(double monto, String nombre, boolean esFijo, String categoria) {
        this.monto = monto;
        this.nombre = nombre;
        this.esFijo = esFijo;
        this.categoria = categoria;
        this.fechaRegistro = new Date(); // Se asigna automáticamente la fecha actual
    }

    // Getters
    public double getMonto() {
        return monto;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEsFijo() {
        return esFijo;
    }

    public String getCategoria() {
        return categoria;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public String toString() {
        String tipo = esFijo ? "Fijo" : "Variable";
        return "Gasto: " + nombre + " - $" + monto + " - " + tipo + " - Categoría: " + categoria;
    }
}
