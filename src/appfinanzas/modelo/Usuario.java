package appfinanzas.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Se encarga de gestionar los ingresos y gastos registrados.
 */
public class Usuario {
    private final String nombre;
    private final String dni;
    private final List<Ingreso> ingresos;
    private final List<Gasto> gastos;

    /**
     * Crea un usuario nuevo con el DNI.
     */
    public Usuario(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
        this.ingresos = new ArrayList<>();
        this.gastos = new ArrayList<>();
    }


    public void agregarIngreso(Ingreso ingreso) {
        if (ingreso == null) {
            throw new IllegalArgumentException("El ingreso no puede ser nulo");
        }
        ingresos.add(ingreso);
    }

    public void agregarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo");
        }
        gastos.add(gasto);
    }

    public double calcularCajaLibre() {
        double totalIngresos = 0.0;
        double totalGastos = 0.0;

        for (Ingreso ingreso : ingresos) {
            if (ingreso.esDisponible()) {
                totalIngresos += ingreso.getMonto();
            }
        }

        for (Gasto gasto : gastos) {
            totalGastos += gasto.getMonto();
        }

        return totalIngresos - totalGastos;
    }

    public String obtenerMensajePresupuesto() {
        return obtenerMensajePresupuesto(null);
    }

    /** Permite calcular el presupuesto usando una fecha de próximo ingreso opcional (override). */
    public String obtenerMensajePresupuesto(java.time.LocalDate proximaFechaOverride) {
        double cajaLibre = calcularCajaLibre();
        java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("es-AR"));

        if (cajaLibre <= 0) {
            String faltante = nf.format(Math.abs(cajaLibre));
            return "No hay caja libre para asignar presupuesto. Tenés un faltante de " + faltante +
                   "; evitá gastos discrecionales hasta el próximo ingreso.";
        }

        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDate proximaFecha = proximaFechaOverride;
        if (proximaFecha == null) {
            for (Ingreso i : ingresos) {
                if (!i.esDisponible() && (i.getFecha().isAfter(hoy) || i.getFecha().isEqual(hoy))) {
                    if (proximaFecha == null || i.getFecha().isBefore(proximaFecha)) {
                        proximaFecha = i.getFecha();
                    }
                }
            }
        }

        if (proximaFecha == null) {
            return "No tenés un próximo ingreso registrado. Ingresá la fecha aproximada de tu próximo ingreso para calcular tu presupuesto.";
        } else {
            // Determinar fechas relevantes: último ingreso disponible y próximo ingreso
            java.time.LocalDate ultimaFechaPago = null;
            for (Ingreso i : ingresos) {
                if (i.esDisponible() && (i.getFecha().isBefore(hoy) || i.getFecha().isEqual(hoy))) {
                    if (ultimaFechaPago == null || i.getFecha().isAfter(ultimaFechaPago)) {
                        ultimaFechaPago = i.getFecha();
                    }
                }
            }

            // Días del período para prorratear el diario: desde último ingreso hasta próximo ingreso (inclusivo)
            long diasPeriodo;
            if (ultimaFechaPago != null) {
                diasPeriodo = java.time.temporal.ChronoUnit.DAYS.between(ultimaFechaPago, proximaFecha) + 1;
            } else {
                diasPeriodo = java.time.temporal.ChronoUnit.DAYS.between(hoy, proximaFecha) + 1;
            }
            if (diasPeriodo < 1) diasPeriodo = 1;

            // Monto diario basado en todo el período (fijo durante el ciclo)
            double diario = cajaLibre / diasPeriodo;
            // Monto semanal fijo (no se reduce aunque falten menos de 7 días)
            double semanal = diario * 7;

            String fechaStr = proximaFecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String aclaracionDiario = (ultimaFechaPago != null)
                    ? " (calculado desde tu último ingreso del " + ultimaFechaPago.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")"
                    : "";

                return "Podés gastar hasta " + nf.format(diario) + " por día" + aclaracionDiario +
                   " o " + nf.format(semanal) + " por semana" +
                   " hasta tu próximo ingreso (" + fechaStr + ").";
        }
    }

    public String obtenerMensajePerfilFinanciero() {
        // Totales base
        double ingresosDisponibles = 0.0;
        for (Ingreso i : ingresos) {
            if (i.esDisponible()) ingresosDisponibles += i.getMonto();
        }
        double totalGastos = 0.0;
        for (Gasto g : gastos) totalGastos += g.getMonto();

        java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("es-AR"));

        if (ingresosDisponibles <= 0) {
            return "No puedo evaluar tu perfil financiero porque no tenés ingresos disponibles registrados. Agregá al menos un ingreso disponible.";
        }

        double cajaLibre = ingresosDisponibles - totalGastos;
        double necesidadesPct = (totalGastos / ingresosDisponibles) * 100.0;

        // Determinar banda del semáforo y regla sugerida
        String semaforoEstado; // Texto descriptivo sin etiquetas
        int pctDeseos;
        int pctAhorro;
        boolean ahorroSimbolico = false;

        if (necesidadesPct <= 50.0) {
            semaforoEstado = "Equilibrado";
            pctDeseos = 30;
            pctAhorro = 20;
        } else if (necesidadesPct <= 70.0) {
            semaforoEstado = "Ajustado";
            pctDeseos = 20;
            pctAhorro = 10;
        } else if (necesidadesPct <= 85.0) {
            semaforoEstado = "Riesgoso";
            pctDeseos = 0;
            pctAhorro = 20;
        } else {
            semaforoEstado = "Critico";
            pctDeseos = 0;
            // Ahorro simbólico dinámico 5/3/1% según severidad
            if (necesidadesPct <= 92.0) pctAhorro = 5;
            else if (necesidadesPct <= 97.0) pctAhorro = 3;
            else pctAhorro = 1;
            ahorroSimbolico = true;
        }

        // Montos sugeridos según base por estado:
        // - Equilibrado/Ajustado/Riesgoso: porcentajes sobre el ingreso total
        // - Crítico: ahorro simbólico sobre la caja libre; deseos = 0
        double montoDeseos = 0.0;
        double montoAhorro = 0.0;
        boolean esCritico = "Critico".equals(semaforoEstado);
        if (cajaLibre > 0) {
            if (esCritico) {
                montoDeseos = 0.0;
                montoAhorro = cajaLibre * pctAhorro / 100.0;
            } else {
                double deseadoDeseos = ingresosDisponibles * pctDeseos / 100.0;
                double deseadoAhorro = ingresosDisponibles * pctAhorro / 100.0;
                double suma = deseadoDeseos + deseadoAhorro;
                if (suma > cajaLibre) {
                    double factor = cajaLibre / suma;
                    montoDeseos = deseadoDeseos * factor;
                    montoAhorro = deseadoAhorro * factor;
                } else {
                    montoDeseos = deseadoDeseos;
                    montoAhorro = deseadoAhorro;
                }
            }
        }
                // Colores ANSI (fallback si NO_COLOR está definido)
                boolean sinColor = System.getenv("NO_COLOR") != null; // permite desactivar colores exportando NO_COLOR
                final String RESET = sinColor ? "" : "\u001B[0m";
                final String VERDE = sinColor ? "" : "\u001B[32m";
                final String AMARILLO = sinColor ? "" : "\u001B[33m";
                boolean sin256 = System.getenv("NO_256COLOR") != null; // permite forzar fallback sin 256 colores
                final String NARANJA = sinColor ? "" : (sin256 ? "\u001B[33m" : "\u001B[38;5;208m"); // naranja real en 256 colores, amarillo si no
                final String ROJO = sinColor ? "" : "\u001B[31m";
                String color;
                // Mapeo corregido a los estados actuales
                if (semaforoEstado.startsWith("Equi")) color = VERDE;          // Equilibrado
                else if (semaforoEstado.startsWith("Ajust")) color = AMARILLO; // Ajustado
                else if (semaforoEstado.startsWith("Riesg")) color = NARANJA;  // Riesgoso
                else color = ROJO;                                             // Critico

                // Construcción del mensaje en líneas para luego encuadrar
                java.util.List<String> lineas = new java.util.ArrayList<>();
                // Encabezado unificado para todos los estados
                String mostrarEstado = semaforoEstado.equals("Critico") ? "Crítico" : semaforoEstado;
                String encabezado = "Tu situación actual se encuentra en un estado: " + color + mostrarEstado + RESET;
                lineas.add(encabezado);
                lineas.add(String.format(java.util.Locale.ROOT, "Este mes tus necesidades representan el %.0f%% de tus ingresos.", necesidadesPct));

                // Explicación de regla aplicada
                String reglaExplicacion;
                if (semaforoEstado.startsWith("Equi")) {
                    reglaExplicacion = "Regla aplicada: 50/30/20 (porcentajes sobre el ingreso; 50% necesidades, 30% deseos, 20% ahorro)";
                } else if (semaforoEstado.startsWith("Ajust")) {
                    reglaExplicacion = "Regla aplicada: 70/20/10 (porcentajes sobre el ingreso; 70% necesidades, 20% deseos, 10% ahorro)";
                } else if (semaforoEstado.startsWith("Riesg")) {
                    reglaExplicacion = "Regla aplicada: 80/20 (porcentajes sobre el ingreso; 80% necesidades, 20% ahorro; deseos pausados)";
                } else {
                    int necesidadesR = Math.round((float)necesidadesPct);
                    int cajaLibreR = Math.max(0, 100 - necesidadesR);
                    // Regla de supervivencia: mostramos solo enteros sin equivalencia sobre el ingreso total.
                    reglaExplicacion = String.format(java.util.Locale.ROOT,
                        "Regla aplicada: Supervivencia (%d%% necesidades, %d%% caja libre; ahorro %d%% de la caja libre)",
                        necesidadesR, cajaLibreR, pctAhorro);
                }
                lineas.add(reglaExplicacion);

                // Umbral siguiente informativo
                String umbralMsg = null;
                if (semaforoEstado.startsWith("Riesg")) {
                    umbralMsg = "Si bajás tus necesidades al 70% entrarías en estado Ajustado.";
                } else if (semaforoEstado.startsWith("Ajust")) {
                    umbralMsg = "Si bajás tus necesidades al 50% entrarías en estado Equilibrado.";
                } else if (semaforoEstado.equals("Critico")) {
                    umbralMsg = "Si bajás tus necesidades al 85% salís de estado Critico.";
                }
                if (umbralMsg != null) lineas.add(umbralMsg);

                // Ahorro sugerido (si hay caja libre)
                if (cajaLibre > 0) {
                    if (ahorroSimbolico) {
                        // Modo Supervivencia: frase explícita de hábito sin marcar '(simbólico)'
                        lineas.add("Ahorrá al menos " + nf.format(montoAhorro) + " este mes para mantener el hábito.");
                    } else {
                        lineas.add("Ahorrá al menos " + nf.format(montoAhorro) + " este mes.");
                    }
                    // Línea breve de destinos del ahorro
                    lineas.add("Podés destinar el ahorro a: fondo de emergencia, pago deudas, inversiones, objetivos personales.");

                    // Línea breve de destinos de los deseos (si hay porcentaje > 0)
                    if (pctDeseos > 0) {
                        lineas.add("Podés destinar los deseos a: ocio, salidas, hobbies, compras personales, suscripciones, viajes, regalos.");
                    }
                }

                // Resumen financiero extendido
                lineas.add("Resumen financiero:");
                lineas.add("- Ingreso disponible: " + nf.format(ingresosDisponibles));
                double cajaLibrePct = ingresosDisponibles > 0 ? ((cajaLibre / ingresosDisponibles) * 100.0) : 0.0;
                // Se eliminó la equivalencia del ahorro como porcentaje del ingreso para evitar confusiones.
                double necesidadesMonto = totalGastos;
                lineas.add(String.format(java.util.Locale.ROOT, "- Necesidades: %.0f%% (%s)", necesidadesPct, nf.format(necesidadesMonto)));
                lineas.add(String.format(java.util.Locale.ROOT, "- Caja libre: %s (%.0f%% del ingreso)", nf.format(cajaLibre), cajaLibrePct));
                if (pctDeseos > 0) {
                    lineas.add(String.format(java.util.Locale.ROOT, "- Deseos: %d%% (%s)", pctDeseos, nf.format(montoDeseos)));
                } else {
                    lineas.add("- Deseos: pausados (0%)");
                }
                lineas.add(String.format(java.util.Locale.ROOT, "- Ahorro: %d%% (%s)", pctAhorro, nf.format(montoAhorro)));

                if (cajaLibre <= 0) {
                    lineas.add("No tenés caja libre: enfocá ajustes antes de nuevos gastos.");
                }

                // Encapsular en cuadro ASCII
                int maxLen = 0;
                for (String ln : lineas) {
                    // Ignorar códigos ANSI para el cálculo de ancho (simplificado)
                    String sinAnsi = ln.replaceAll("\u001B\\[[;\\d]*m", "");
                    if (sinAnsi.length() > maxLen) maxLen = sinAnsi.length();
                }
                int padding = 1; // espacio lateral
                String horizontal = repeatChar('-', maxLen + padding * 2);
                StringBuilder sb = new StringBuilder();
                sb.append('+').append(horizontal).append('+').append('\n');
                for (String ln : lineas) {
                    String sinAnsi = ln.replaceAll("\u001B\\[[;\\d]*m", "");
                    int diff = maxLen - sinAnsi.length();
                    sb.append('|').append(' ');
                    sb.append(ln);
                    // rellenar espacios sin romper colores
                    for (int k = 0; k < diff + padding - 1; k++) sb.append(' ');
                    sb.append('|').append('\n');
                }
                sb.append('+').append(horizontal).append('+');

                return sb.toString();
    }

    // ------------------------------
    // GETTERS
    // ------------------------------
    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public List<Ingreso> getIngresos() {
        return new ArrayList<>(ingresos);
    }

    public List<Gasto> getGastos() {
        return new ArrayList<>(gastos);
    }

    // Utilidad interna para repetir caracteres en el marco del cuadro
    private static String repeatChar(char c, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }
}
