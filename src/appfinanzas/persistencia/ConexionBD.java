package appfinanzas.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Proveedor de conexiones JDBC a MySQL.
 * Lee la configuración desde db/db.properties y expone un método getConnection().
 *
 * Flujo básico:
 * 1) Carga propiedades (URL, usuario, password) desde db/db.properties
 * 2) Registra el driver MySQL (explícito para errores más claros)
 * 3) Devuelve una nueva Connection con DriverManager
 */
public final class ConexionBD {
    // Se define donde buscar el archivo de configuración (ruta relativa) dentro del proyecto
    private static final String PROPERTIES_PATH = "db/db.properties";

    private ConexionBD() { }
    // Obtiene una conexión nueva a la base MySQL usando las propiedades configuradas.
    public static Connection getConnection() throws SQLException {
        // Lee las claves db.url, db.user, db.password desde db.properties.
        Properties props = new Properties();
        Path path = Paths.get(PROPERTIES_PATH);
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            props.load(fis); // ahora props contiene db.url, db.user, db.password
        } catch (IOException e) {
            // si falta alguna propiedad, lanza SQLException explicando el problema.
            throw new SQLException("No se pudo leer " + PROPERTIES_PATH + ": " + e.getMessage(), e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        // Validación mínima de configuración
        if (url == null || user == null || pass == null) {
            throw new SQLException("Faltan propiedades db.url/db.user/db.password en " + PROPERTIES_PATH);
        }

        // Registra el driver si no lo cargo. En versiones modernas muchas veces es auto, pero acá lo dejamos para obtener un error claro si el JAR no está en classpath.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado. ¿Agregaste el JAR a lib/?", e);
        }

        // 3) Creación de la conexión: a partir de aquí ya se puede usar PreparedStatement
        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * Realiza un ping sencillo a la base creando una conexión y usando isValid().
     * Devuelve true si responde dentro del timeout.
     */
    public static boolean ping(int timeoutSeconds) {
        int t = Math.max(timeoutSeconds, 2); // aseguramos un mínimo razonable
        // try-with-resources garantiza cierre de la conexión aún con excepciones
        try (Connection c = getConnection()) {
            return c.isValid(t); // consulta de salud al servidor
        } catch (SQLException e) {
            return false; // si hay error, consideramos que el ping falló
        }
    }

    /**
     * Pequeño runner para validar la conexión desde VS Code sin depender de otras clases.
     */
    public static void main(String[] args) {
        boolean ok = ping(3);
        if (ok) {
            System.out.println("Conexión MySQL OK (ping válido)");
        } else {
            System.out.println("No se pudo validar la conexión. Revisá db/db.properties, el driver en lib/ y que MySQL esté activo.");
        }
    }
}
