package gui;

import dao.PartidaDAO;
import dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import modelo.EstructurasLaberintos;
import modelo.Laberinto;
import modelo.Partida;
import modelo.Usuario;
import servicios.JuegoServicio;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import modelo.Laberinto;

public class LaberintoGame implements KeyListener {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel gamePanel;
    private JScrollPane scrollPane;
    private JButton startButton;
    private JButton loadButton;
    private JButton exitButton;
    private JButton saveButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton moveLeftButton;
    private JButton moveRightButton;
    private LaberintoPanel laberintoPanel;
    private JuegoServicio juegoServicio;
    private Usuario usuario;
    private JTextArea outputArea;
    private static final Logger logger = Logger.getLogger(LaberintoGame.class.getName());

public LaberintoGame() throws SQLException {
    frame = new JFrame("Juego del Laberinto");
    mainPanel = new JPanel(new CardLayout());

    // Inicializa outputArea
    outputArea = new JTextArea(20, 200);
    outputArea.setEditable(false);
    outputArea.setLineWrap(true);
    outputArea.setWrapStyleWord(true);
    scrollPane = new JScrollPane(outputArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // Crear los paneles del juego y del menú
    crearMenuPanel();
    crearGamePanel();

    // Añadir paneles al mainPanel
    mainPanel.add(menuPanel, "Menu");
    mainPanel.add(gamePanel, "Game");

    // Añadir mainPanel al frame
    frame.add(mainPanel);
    frame.setSize(600, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    // Verificar si el usuario no está autenticado, mostrar el cuadro de diálogo de autenticación
    if (usuario == null) {
        mostrarDialogoAutenticacion();
    }

    // Registrar KeyListener en el JFrame
    frame.addKeyListener(this);
    frame.setFocusable(true);
    frame.setFocusTraversalKeysEnabled(false);

    // Registrar KeyListener en el panel del laberinto
    laberintoPanel.addKeyListener(this);
    laberintoPanel.setFocusable(true);
    laberintoPanel.setFocusTraversalKeysEnabled(false);

    requestFocusInGame();
}

private void requestFocusInGame() {
    frame.requestFocusInWindow();
    laberintoPanel.requestFocusInWindow();
    System.out.println("Foco solicitado.");
}





    private void crearMenuPanel() {
        menuPanel = new JPanel();
        startButton = new JButton("Nueva Partida");
        loadButton = new JButton("Cargar Partida");
        exitButton = new JButton("Salir");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarNuevaPartida();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarPartida();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuPanel.add(startButton);
        menuPanel.add(loadButton);
        menuPanel.add(exitButton);
    }



private void crearGamePanel() {
    gamePanel = new JPanel(new BorderLayout());
    JPanel topPanel = new JPanel();
    JPanel bottomPanel = new JPanel(new BorderLayout());  // Asegúrate de usar BorderLayout
    JPanel movementPanel = new JPanel();

    saveButton = new JButton("Guardar Partida");
    moveUpButton = new JButton("Mover Arriba");
    loadButton = new JButton("Cargar Partida");
    exitButton = new JButton("Salir");
    moveDownButton = new JButton("Mover Abajo");
    moveLeftButton = new JButton("Mover Izquierda");
    moveRightButton = new JButton("Mover Derecha");

    // Inicializar outputArea y agregarla a un JScrollPane
    outputArea = new JTextArea(10, 200);
    outputArea.setEditable(false);
    outputArea.setLineWrap(true);
    outputArea.setWrapStyleWord(true);
    scrollPane = new JScrollPane(outputArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // Asegúrate de crear laberintoPanel antes de pasar al constructor de JuegoServicio
    Laberinto laberinto = new Laberinto(1, "Nivel 1", EstructurasLaberintos.NIVEL_1);
    laberintoPanel = new LaberintoPanel(laberinto);

    saveButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (juegoServicio != null) {
            int puntuacionActual = juegoServicio.getPuntuacion(); // Obtener la puntuación actual
            juegoServicio.guardarPartida(usuario, "en progreso", puntuacionActual); // Usar la puntuación actual
            appendOutput("Partida Guardada!");
        }
    }
});


    loadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (juegoServicio != null) {
                cargarPartida();
            }
        }
    });

    exitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });

    moveUpButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (juegoServicio != null) {
                moverJugador("arriba");
            }
        }
    });

    moveDownButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (juegoServicio != null) {
                moverJugador("abajo");
            }
        }
    });

    moveLeftButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (juegoServicio != null) {
                moverJugador("izquierda");
            }
        }
    });

    moveRightButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (juegoServicio != null) {
                moverJugador("derecha");
            }
        }
    });

    topPanel.add(saveButton);
    topPanel.add(loadButton);
    topPanel.add(exitButton);
    movementPanel.add(moveUpButton);
    movementPanel.add(moveDownButton);
    movementPanel.add(moveLeftButton);
    movementPanel.add(moveRightButton);

    bottomPanel.add(movementPanel, BorderLayout.NORTH);
    bottomPanel.add(scrollPane, BorderLayout.CENTER);  // Añadir scrollPane al centro de bottomPanel

    gamePanel.add(topPanel, BorderLayout.NORTH);
    gamePanel.add(laberintoPanel, BorderLayout.CENTER);
    gamePanel.add(bottomPanel, BorderLayout.SOUTH);  // Añadir bottomPanel al sur de gamePanel

    frame.addKeyListener(this);
    frame.setFocusable(true);
    frame.setFocusTraversalKeysEnabled(false);
}


private void mostrarDialogoAutenticacion() {
    JPanel panel = new JPanel(new GridLayout(3, 2));
    JTextField nombreField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    panel.add(new JLabel("Nombre:"));
    panel.add(nombreField);
    panel.add(new JLabel("Email:"));
    panel.add(emailField);
    panel.add(new JLabel("Contraseña:"));
    panel.add(passwordField);

    int option = JOptionPane.showConfirmDialog(frame, panel, "Registro/Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (option == JOptionPane.OK_OPTION) {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String contrasena = new String(passwordField.getPassword());

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.obtenerUsuarioPorNombreYContrasena(nombre, contrasena);
            if (usuario == null) {
                usuario = new Usuario(0, nombre, email, contrasena);
                usuarioDAO.guardarUsuario(usuario);
                appendOutput("Usuario registrado con éxito.");
            } else {
                appendOutput("Bienhallado, " + nombre);
            }
            this.usuario = usuario; // Asigna el usuario autenticado a la variable de instancia
            juegoServicio = new JuegoServicio(this, new Laberinto(1, "Nivel 1", EstructurasLaberintos.NIVEL_1), outputArea, laberintoPanel); // Inicializa juegoServicio aquí
            iniciarNuevaPartida();  // Iniciar nueva partida aquí
        } catch (SQLException e) {
            appendOutput("Error al autenticar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        System.exit(0); // Cierra el programa si el usuario cancela el diálogo
    }
    requestFocusInGame();
}


public void iniciarNuevaPartida() {
    if (laberintoPanel != null && juegoServicio != null) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Game");
        juegoServicio.iniciarJuego();
        laberintoPanel.setJugadorPos(juegoServicio.getJugadorPosX(), juegoServicio.getJugadorPosY());
        laberintoPanel.repaint();

        if (!isMessageDisplayed) {
            appendOutput("Después de un largo y duro día de trabajo en las minas, te dispones a guardar el último pedazo de tetrahedrita. Tu mochila está llena, es hora de llevar los minerales a la reserva.\n");
            appendOutput("¡Encuentra el camino de vuelta a la fortaleza enana!\n\n");
            isMessageDisplayed = true;  // Asegúrate de que el mensaje se muestra solo una vez
        }
    } else {
        appendOutput("Error: El panel del laberinto o el servicio del juego no están inicializados.");
    }
}
private boolean isMessageDisplayed = false;  // Variable para controlar la visualización del mensaje

private void cargarPartida() {
    if (juegoServicio != null) {
        juegoServicio.cargarPartida(usuario);
    } else {
        appendOutput("Error: El servicio del juego no está inicializado.");
    }
}


public void guardarPartida(Usuario usuario, String estado, int puntuacion) {
    try {
        PartidaDAO partidaDAO = new PartidaDAO();
        Partida partida = new Partida(usuario);
        partida.setLaberinto(juegoServicio.getLaberinto());
        partida.setEstado(estado);
        partida.setPuntuacion(puntuacion);
        partidaDAO.guardarPartida(partida);
        appendOutput("Partida guardada.");
        requestFocusInGame(); // Solicitar el foco después de guardar la partida
    } catch (SQLException e) {
        logger.severe("Error al guardar la partida: " + e.getMessage());
        e.printStackTrace();
    }
}





private void moverJugador(String direccion) {
    System.out.println("Mover jugador: " + direccion); // Mensaje de depuración
    juegoServicio.moverJugador(direccion);
    laberintoPanel.setJugadorPos(juegoServicio.getJugadorPosX(), juegoServicio.getJugadorPosY());
    laberintoPanel.repaint();
}


    private void appendOutput(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se usa, pero debe estar presente debido a la interfaz KeyListener
    }

@Override
public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    System.out.println("Tecla presionada: " + key); // Mensaje de depuración

    switch (key) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
            System.out.println("Mover arriba"); // Mensaje de depuración
            moverJugador("arriba");
            break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
            System.out.println("Mover abajo"); // Mensaje de depuración
            moverJugador("abajo");
            break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
            System.out.println("Mover izquierda"); // Mensaje de depuración
            moverJugador("izquierda");
            break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
            System.out.println("Mover derecha"); // Mensaje de depuración
            moverJugador("derecha");
            break;
    }
}



    @Override
    public void keyReleased(KeyEvent e) {
        // No se usa, pero debe estar presente debido a la interfaz KeyListener
    }

    public static void main(String[] args) throws SQLException {
        new LaberintoGame();
    }
}
