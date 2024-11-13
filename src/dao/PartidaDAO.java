package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Partida;
import modelo.Usuario;
import modelo.Laberinto;
import modelo.Cuadro;
import modelo.EstructurasLaberintos;
import java.sql.Timestamp;

public class PartidaDAO {
    private Connection connection;

    public PartidaDAO() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

public void guardarPartida(Partida partida) throws SQLException {
    String query = "INSERT INTO partidas_guardadas (usuario_id, estado_partida, puntuacion, jugador_pos_x, jugador_pos_y, nivel, estructura_laberinto) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE estado_partida=?, puntuacion=?, jugador_pos_x=?, jugador_pos_y=?, nivel=?, estructura_laberinto=?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, partida.getUsuario().getId());
        stmt.setString(2, partida.getEstado());
        stmt.setInt(3, partida.getPuntuacion());
        stmt.setInt(4, partida.getJugadorPosX());
        stmt.setInt(5, partida.getJugadorPosY());
        stmt.setInt(6, partida.getNivel());
        stmt.setString(7, partida.getEstructuraLaberinto());
        stmt.setString(8, partida.getEstado());
        stmt.setInt(9, partida.getPuntuacion());
        stmt.setInt(10, partida.getJugadorPosX());
        stmt.setInt(11, partida.getJugadorPosY());
        stmt.setInt(12, partida.getNivel());
        stmt.setString(13, partida.getEstructuraLaberinto());
        stmt.executeUpdate();
    }
}



public Partida recuperarPartida(Usuario usuario) throws SQLException {
    String query = "SELECT * FROM partidas_guardadas WHERE usuario_id = ? ORDER BY fecha_guardado DESC LIMIT 1";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, usuario.getId());
        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            Partida partida = new Partida(usuario);
            partida.setEstado(resultSet.getString("estado_partida"));
            partida.setPuntuacion(resultSet.getInt("puntuacion"));
            partida.setJugadorPosX(resultSet.getInt("jugador_pos_x"));
            partida.setJugadorPosY(resultSet.getInt("jugador_pos_y"));
            partida.setNivel(resultSet.getInt("nivel"));
            partida.setEstructuraLaberinto(resultSet.getString("estructura_laberinto"));
            return partida;
        }
    }
    return null;
}





    public boolean usuarioExiste(int usuarioId) throws SQLException {
        String query = "SELECT COUNT(*) FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void insertarUsuario(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuarios (id, nombre, email, password, fecha_registro) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getContrasena()); // Cambiado de getPassword() a getContrasena()
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        }
    }
}
