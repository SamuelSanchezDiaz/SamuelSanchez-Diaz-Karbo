package servicios;

import dao.UsuarioDAO;
import java.sql.SQLException;
import modelo.Usuario;

public class UsuarioServicio {
    private UsuarioDAO usuarioDAO;

    public UsuarioServicio() {
        usuarioDAO = new UsuarioDAO();
    }

    public void registrarUsuario(Usuario usuario) throws SQLException {
        usuarioDAO.guardarUsuario(usuario);
    }

    // Otros métodos para la gestión de usuarios
}
