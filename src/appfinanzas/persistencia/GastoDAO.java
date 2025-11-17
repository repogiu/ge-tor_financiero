package appfinanzas.persistencia;

import appfinanzas.modelo.Gasto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GastoDAO {

    public List<Gasto> listarPorDni(String dni) throws SQLException {
        String sql = "SELECT id, nombre, monto, fecha, es_fijo, categoria FROM Gasto WHERE dni_usuario = ?";
        List<Gasto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double monto = rs.getDouble("monto");
                    boolean esFijo = rs.getBoolean("es_fijo");
                    Gasto g = new Gasto(monto, nombre, esFijo);
                    g.setId(id); // guardamos id
                    lista.add(g);
                }
            }
        }
        return lista;
    }

    public void guardar(String dniUsuario, Gasto gasto) throws SQLException {
        String sql = "INSERT INTO Gasto(dni_usuario, nombre, monto, fecha, es_fijo, categoria) VALUES(?,?,?,?,?,?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dniUsuario);
            ps.setString(2, gasto.getNombre());
            ps.setDouble(3, gasto.getMonto());
            ps.setDate(4, Date.valueOf(gasto.getFecha()));
            ps.setBoolean(5, gasto.isEsFijo());
            ps.setString(6, gasto.getCategoria());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    gasto.setId(rs.getInt(1));
                }
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Gasto WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void actualizar(Gasto gasto) throws SQLException {
        if (gasto.getId() == null) {
            throw new SQLException("Gasto sin id: no se puede actualizar");
        }
        String sql = "UPDATE Gasto SET nombre = ?, monto = ?, fecha = ?, es_fijo = ?, categoria = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, gasto.getNombre());
            ps.setDouble(2, gasto.getMonto());
            ps.setDate(3, Date.valueOf(gasto.getFecha()));
            ps.setBoolean(4, gasto.isEsFijo());
            ps.setString(5, gasto.getCategoria());
            ps.setInt(6, gasto.getId());
            ps.executeUpdate();
        }
    }
}
