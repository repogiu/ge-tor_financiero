package appfinanzas.persistencia;

import appfinanzas.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT nombre FROM Usuario WHERE dni = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    return new Usuario(nombre, dni);
                }
                return null;
            }
        }
    }

    public void guardar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario(dni, nombre) VALUES(?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombre());
            ps.executeUpdate();
        }
    }
}
