package appfinanzas.modelo;

import java.time.LocalDate;

/**
 * SUBCLASE que representa un ingreso en el sistema financiero.
 * Hereda de Transaccion → por eso usa extends
 */
public class Ingreso extends Transaccion {
    // Atributo específico de Ingreso
    private boolean esFuturo;
    // Identificador en BD (puede ser null si aún no se insertó)
    private Integer id;

    /**
     * Constructor de la subclase:
     * 1. Primero llama a super() para inicializar la parte de Transaccion
     * 2. Luego inicializa sus propios atributos
     */
    // Constructor para nuevos ingresos (sin id todavía)
    public Ingreso(String nombre, double monto, LocalDate fechaIngreso, boolean esFuturo) {
        // super() DEBE ser la primera línea del constructor
        super(nombre, monto, fechaIngreso, "Ingreso");
        this.esFuturo = esFuturo;
    }

    // Constructor para reconstruir desde BD con id
    public Ingreso(Integer id, String nombre, double monto, LocalDate fechaIngreso, boolean esFuturo) {
        super(nombre, monto, fechaIngreso, "Ingreso");
        this.id = id;
        this.esFuturo = esFuturo;
    }

    /**
     * Implementación del método abstracto de Transaccion
     * Define cómo un Ingreso impacta en la caja:
     * - Si es futuro: no afecta (retorna 0)
     * - Si es presente: suma el monto
     */
    @Override
    public double calcularImpactoEnCaja() {
        return esFuturo ? 0 : getMonto();
    }

    public boolean esDisponible() {
        // Si es un ingreso a futuro, no se puede sumar a la caja
        return !esFuturo;
    }

    public boolean isEsFuturo() {
        return esFuturo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Sobreescribimos toString() para agregar info específica de Ingreso
     * Usamos super.toString() para la parte común y agregamos si es futuro
     */
    @Override
    public String toString() {
        // Primero obtenemos el formato base de Transaccion
        return super.toString() + " | " + (esFuturo ? "Futuro" : "Disponible");
    }
}
