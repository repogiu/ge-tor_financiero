package appfinanzas.persistencia;

import appfinanzas.modelo.Ingreso;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IngresoDAO {

    public List<Ingreso> listarPorDni(String dni) throws SQLException {
        String sql = "SELECT id, nombre, monto, fecha, es_futuro FROM Ingreso WHERE dni_usuario = ?";
        List<Ingreso> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double monto = rs.getDouble("monto");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    boolean esFuturo = rs.getBoolean("es_futuro");
                    lista.add(new Ingreso(id, nombre, monto, fecha, esFuturo));
                }
            }
        }
        return lista;
    }

    public void guardar(String dniUsuario, Ingreso ingreso) throws SQLException {
        String sql = "INSERT INTO Ingreso(dni_usuario, nombre, monto, fecha, es_futuro) VALUES(?,?,?,?,?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dniUsuario);
            ps.setString(2, ingreso.getNombre());
            ps.setDouble(3, ingreso.getMonto());
            ps.setDate(4, Date.valueOf(ingreso.getFecha()));
            ps.setBoolean(5, ingreso.isEsFuturo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ingreso.setId(rs.getInt(1));
                }
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Ingreso WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void actualizar(Ingreso ingreso) throws SQLException {
        if (ingreso.getId() == null) {
            throw new SQLException("Ingreso sin id: no se puede actualizar");
        }
        String sql = "UPDATE Ingreso SET nombre = ?, monto = ?, fecha = ?, es_futuro = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ingreso.getNombre());
            ps.setDouble(2, ingreso.getMonto());
            ps.setDate(3, Date.valueOf(ingreso.getFecha()));
            ps.setBoolean(4, ingreso.isEsFuturo());
            ps.setInt(5, ingreso.getId());
            ps.executeUpdate();
        }
    }
}
