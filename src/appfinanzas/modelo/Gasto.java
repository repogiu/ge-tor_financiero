package appfinanzas.modelo;

import java.time.LocalDate;

/**
 * SUBCLASE que representa un gasto en el sistema financiero.
 * Hereda de Transaccion → por eso usa extends
 *
 * @author Giuliana
 */
public class Gasto extends Transaccion {
    // Indica si el gasto es fijo (true) o variable (false)
    private boolean esFijo;
    // Identificador en BD (null si aún no se guardó)
    private Integer id;

    /**
     * Constructor de la subclase:
     * 1. PRIMERO llama a super() para inicializar la parte de Transaccion
     * 2. Luego inicializa sus propios atributos
     */
    // Constructor para nuevos gastos (sin id)
    public Gasto(double monto, String nombre, boolean esFijo) {
        // super() DEBE ser la primera línea del constructor
        super(nombre, monto, LocalDate.now(), esFijo ? "Fijo" : "Variable");  // La fecha se asigna automáticamente
        this.esFijo = esFijo;
    }

    /**
     * Implementación del método abstracto de Transaccion
     * Define cómo un Gasto impacta en la caja:
     * Los gastos siempre restan del balance
     */
    @Override
    public double calcularImpactoEnCaja() {
        return -getMonto();  // Los gastos restan (por eso el signo -)
    }

    /**
     * Devuelve si el gasto es fijo o no
     */
    public boolean isEsFijo() {
        return esFijo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Sobreescribimos toString() para agregar info específica de Gasto
     * Usamos super.toString() para la parte común y agregamos si es fijo/variable
     */
    @Override
    public String toString() {
        return super.toString() + " | " + (esFijo ? "Fijo" : "Variable");
    }
}
