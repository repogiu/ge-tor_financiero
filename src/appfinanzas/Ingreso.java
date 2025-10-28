/*

 */
package appfinanzas;

import java.util.Date;

/**
 *
 * @author Giuliana
 */

public class Ingreso {
    private float monto;
    private Date fechaIngreso;
    private String nombre;
    private boolean esFuturo;
    private String categoria;

    public Ingreso(String nombre, float monto, Date fechaIngreso, boolean esFuturo) {
        this.nombre = nombre;
        this.monto = monto;
        this.fechaIngreso = fechaIngreso;
        this.esFuturo = esFuturo;
        this.categoria = "Ingreso";
    }

    public boolean esDisponible() {
        // Si es un ingreso a futuro, no se puede sumar a la caja
        return !esFuturo;
    }

    public float getMonto() {
        return monto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isEsFuturo() {
        return esFuturo;
    }

    @Override
    public String toString() {
        return nombre + " - $" + monto + " - " + (esFuturo ? "Futuro" : "Disponible") + " - Fecha: " + fechaIngreso;
    }
}