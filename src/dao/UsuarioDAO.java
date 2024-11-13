package dao;

import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

public void guardarUsuario(Usuario usuario) throws SQLException {
    String query = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE email=?, password=?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, usuario.getNombre());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getContrasena());
        stmt.setString(4, usuario.getEmail());
        stmt.setString(5, usuario.getContrasena());
        stmt.executeUpdate();
    }
}

    public Usuario obtenerUsuarioPorNombreYContrasena(String nombre, String contrasena) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE nombre = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), rs.getString("password"));
            }
        }
        return null;
    }
}
