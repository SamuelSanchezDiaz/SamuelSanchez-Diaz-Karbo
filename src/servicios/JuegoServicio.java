package servicios;

import modelo.Cuadro;
import modelo.Laberinto;
import modelo.Partida;
import modelo.Usuario;
import dao.PartidaDAO;
import gui.LaberintoGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.logging.Logger;
import gui.LaberintoPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import modelo.EstructurasLaberintos;
import gui.LaberintoGame;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class JuegoServicio {
    private Laberinto laberinto;
    private JTextArea outputArea;
    private LaberintoPanel laberintoPanel;
    private int jugadorPosX;
    private int jugadorPosY;
    private Clip clip;
    private static final Logger logger = Logger.getLogger(JuegoServicio.class.getName());
    private LaberintoGame laberintoGame;
    private JPanel mainPanel;
    private Cuadro[][] estructura;
    
    
    public JuegoServicio(LaberintoGame laberintoGame, Laberinto laberinto, JTextArea outputArea, LaberintoPanel laberintoPanel) {
        this.laberintoGame = laberintoGame;
        this.laberinto = laberinto;
        this.outputArea = outputArea;
        this.laberintoPanel = laberintoPanel;
        this.jugadorPosX = 0;
        this.jugadorPosY = 0;
    }
public void iniciarJuego() {
    // Suponiendo que el inicio del jugador es la posición del 'S' en el laberinto
    for (int y = 0; y < laberinto.getEstructura().length; y++) {
        for (int x = 0; x < laberinto.getEstructura()[y].length; x++) {
            if (laberinto.getEstructura()[y][x].getTipo() == Cuadro.Tipo.INICIO) {
                jugadorPosX = x;
                jugadorPosY = y;
                break;
            }
        }
    }
   
    
    playTrabajo("/resources/trabajo.wav");
    playBackgroundMusic("/resources/background_music.wav");
}





public void playBackgroundMusic(String filePath) {
    try {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
        }

        // Obtén el recurso como un InputStream
        InputStream audioSrc = getClass().getResourceAsStream(filePath);
        if (audioSrc == null) {
            throw new IOException("Resource not found: " + filePath);
        }
        
        // Añade un buffer alrededor del InputStream
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);

        logger.info("Background music started: " + filePath);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        logger.log(java.util.logging.Level.SEVERE, "Error al reproducir la música de fondo: " + e.getMessage(), e);
    }
}



public void stopBackgroundMusic() {
    if (clip != null) {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.close();
        clip = null; // Añadir esta línea para asegurar que el clip se libere correctamente
    }
}



public void playDerrumbe(String filePath) {
    playSound(filePath);
}

public void playOportunidad(String filePath) {
    playSound(filePath);
}

public void playBloqueada(String filePath) {
    playSound(filePath);
}

public void playTrabajo(String filePath) {
    playSound(filePath);
}

public void playCongratulations(String filePath) {
    stopBackgroundMusic(); // Detener la música de fondo antes de reproducir la felicitación
    playSound(filePath);
}
public void playQueso(String filePath) {
    playSound(filePath);
}
public void playPasos(String filePath) {
    playSound(filePath);
}
public void playVino(String filePath) {
    playSound(filePath);
}
private void playSound(String filePath) {
    try {
        // Obtén el recurso como un InputStream
        InputStream audioSrc = getClass().getResourceAsStream(filePath);
        if (audioSrc == null) {
            throw new IOException("Resource not found: " + filePath);
        }
        
        // Añade un buffer alrededor del InputStream
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        Clip soundClip = AudioSystem.getClip();
        soundClip.open(audioStream);
        soundClip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        logger.log(java.util.logging.Level.SEVERE, "Error al reproducir el sonido: " + e.getMessage(), e);
    }
}


public void moverJugador(String direccion) {
    if (puedeMover(direccion)) {
        switch (direccion) {
            case "arriba":
                playPasos("/resources/pasos.wav");
                jugadorPosY--;
                break;
            case "abajo":
                playPasos("/resources/pasos.wav");
                jugadorPosY++;
                break;
            case "izquierda":
                playPasos("/resources/pasos.wav");
                jugadorPosX--;
                break;
            case "derecha":
                playPasos("/resources/pasos.wav");
                jugadorPosX++;
                break;
        }
        Cuadro.Tipo tipoActual = laberinto.getEstructura()[jugadorPosY][jugadorPosX].getTipo();
        System.out.println("Jugador movido a posición: (" + jugadorPosX + ", " + jugadorPosY + "), Tipo de cuadro: " + tipoActual);

        switch (tipoActual) {
            case FIN:
                appendOutput('\n' + "Un nivel menos, cada vez estás más cerca de la fortaleza...");
                if (laberinto.getId() == 1) {
                    iniciarNivel2();
                    
                } else if (laberinto.getId() == 2) {
                    iniciarNivel3();
                    
                } else {
                    appendOutput("¡Has completado todos los niveles!");
                    mostrarPantallaFelicitacion();
                    
                }
                break;
            case DERRUMBE:
                playDerrumbe("/resources/derrumbe.wav");
                appendOutput("¡Derrumbe! El camino estaba en mal estado, algún enano vago lo habrá cavado.");
                parpadearPersonaje();
                  laberinto.getEstructura()[jugadorPosY][jugadorPosX] = new Cuadro('#'); // Cambiar D a #
                laberintoPanel.repaint();
                jugadorPosX = obtenerPosicionInicialX();
                jugadorPosY = obtenerPosicionInicialY();
                playOportunidad("/resources/oportunidad.wav");
                break;
            case BLOQUEADA:
                playBloqueada("/resources/bloqueada.wav");
                appendOutput("Parece que esta salida está bloqueada, habrá que reclamar mantenimiento para esta zona...");
                break;
            case QUESO:
                playQueso("/resources/queso.wav");
                appendOutput("¡Queso! Cuanto rico más viejo");
                 incrementarPuntuacion(10);
                laberinto.getEstructura()[jugadorPosY][jugadorPosX] = new Cuadro(' ');
                break;
             case VINO:
                playQueso("/resources/vino.wav");
                appendOutput("¿Vino? ¿Por qué andarán urgando en el almacén?");
                 incrementarPuntuacion(25);
                laberinto.getEstructura()[jugadorPosY][jugadorPosX] = new Cuadro(' ');
                break;
                
        }
        laberintoPanel.repaint();
    } else {
        appendOutput("¡Hay una pared! Y no es momento de ponerse a cavar...");
    }
}







private void mostrarPantallaFelicitacion() {
    JPanel felicitacionPanel = new JPanel(new BorderLayout());

    // Imagen de felicitación
    JLabel imageLabel = new JLabel();
    ImageIcon icon = new ImageIcon(getClass().getResource("/resources/felicitaciones.png"));
    imageLabel.setIcon(icon);
    imageLabel.setHorizontalAlignment(JLabel.CENTER);  // Centrar la imagen
    felicitacionPanel.add(imageLabel, BorderLayout.CENTER);

    // Texto de felicitación completo
    String felicitacionTexto = "<html>Y así el enano llegó de vuelta al salón de la fortaleza, donde los enanos bailaban y saltaban y el suelo agitaban; " +
                               "la causa más probable de los derrumbamientos anteriores. ¡Por las barbas de Mohg, vaya panda de ineptos! - pensó el enano, " +
                               "mientras tiraba su saco al suelo y se disponía a bailar y saltar junto a ellos.<br><br>" +
                               "¡Has completado todos los niveles!</html>";
    JLabel textLabel = new JLabel(felicitacionTexto, JLabel.CENTER);
    textLabel.setFont(new Font("Serif", Font.BOLD, 18));  // Cambiar la fuente y el tamaño para mejor legibilidad
    textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Añadir espacio alrededor del texto para mayor claridad

    // Crear un panel para la imagen y el texto juntos
    JPanel imageTextPanel = new JPanel(new BorderLayout());
    imageTextPanel.add(imageLabel, BorderLayout.CENTER);
    imageTextPanel.add(textLabel, BorderLayout.SOUTH);  // Colocar el texto debajo de la imagen
    felicitacionPanel.add(imageTextPanel, BorderLayout.CENTER);

    // Panel para los botones
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    // Botón para volver a empezar
    JButton nuevaPartidaButton = new JButton("Volver a empezar");
    nuevaPartidaButton.addActionListener(e -> stopBackgroundMusic());
    
    buttonPanel.add(nuevaPartidaButton);

    // Botón para salir del juego
    JButton salirButton = new JButton("Salir");
    salirButton.addActionListener(e -> System.exit(0));
    buttonPanel.add(salirButton);

    // Añadir el panel de botones al panel de felicitación
    felicitacionPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Detener la música de fondo
    stopBackgroundMusic();

    // Reproducir el sonido de felicitación
    playCongratulations("/resources/congratulations.wav");

    // Reemplazar el contenido del frame con el panel de felicitación
    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(laberintoPanel);
    frame.getContentPane().removeAll();
    frame.getContentPane().add(felicitacionPanel);
    frame.revalidate();
    frame.repaint();
}






public void setLaberintoGame(LaberintoGame laberintoGame) {
    this.laberintoGame = laberintoGame;
}


private void parpadearPersonaje() {
    System.out.println("Iniciando parpadeo del personaje");
    new Thread(() -> {
        try {
            for (int i = 0; i < 6; i++) {
                SwingUtilities.invokeAndWait(() -> {
                    laberintoPanel.setMostrarPersonaje(!laberintoPanel.isMostrarPersonaje());
                    laberintoPanel.repaint();
                });
                Thread.sleep(100);
            }
            SwingUtilities.invokeAndWait(() -> {
                laberintoPanel.setMostrarPersonaje(true);
                laberintoPanel.repaint();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }).start();
}




    public int obtenerPosicionInicialX() {
        for (int y = 0; y < laberinto.getEstructura().length; y++) {
            for (int x = 0; x < laberinto.getEstructura()[y].length; x++) {
                if (laberinto.getEstructura()[y][x].getTipo() == Cuadro.Tipo.INICIO) {
                    return x;
                }
            }
        }
        return -1; // No se encontró la posición inicial
    }

    public int obtenerPosicionInicialY() {
        for (int y = 0; y < laberinto.getEstructura().length; y++) {
            for (int x = 0; x < laberinto.getEstructura()[y].length; x++) {
                if (laberinto.getEstructura()[y][x].getTipo() == Cuadro.Tipo.INICIO) {
                    return y;
                }
            }
        }
        return -1; // No se encontró la posición inicial
    }

    private boolean puedeMover(String direccion) {
        int nuevaPosX = jugadorPosX;
        int nuevaPosY = jugadorPosY;

        switch (direccion) {
            case "arriba":
                nuevaPosY--;
                break;
            case "abajo":
                nuevaPosY++;
                break;
            case "izquierda":
                nuevaPosX--;
                break;
            case "derecha":
                nuevaPosX++;
                break;
        }

        if (nuevaPosX < 0 || nuevaPosX >= laberinto.getEstructura()[0].length || nuevaPosY < 0 || nuevaPosY >= laberinto.getEstructura().length) {
            return false;
        }

        return laberinto.getEstructura()[nuevaPosY][nuevaPosX].getTipo() != Cuadro.Tipo.MURO;
    }

    public int getJugadorPosX() {
        return jugadorPosX;
    }

    public int getJugadorPosY() {
        return jugadorPosY;
    }
    


public void cambiarNivel(Cuadro[][] nuevoNivel) {
     
    laberinto.setEstructura(nuevoNivel);
    iniciarJuego(); // Reubica al jugador al inicio del nuevo nivel
    laberintoPanel.setLaberinto(laberinto); // Actualiza el panel con el nuevo laberinto
    laberintoPanel.repaint(); // Redibuja el panel
}


public void guardarPartida(Usuario usuario, String estado, int puntuacion) {
    try {
        PartidaDAO partidaDAO = new PartidaDAO();
        Partida partida = new Partida(usuario);
        partida.setLaberinto(laberinto);
        partida.setEstado(estado != null ? estado : "en progreso");
        partida.setPuntuacion(puntuacion);
        partida.setJugadorPosX(jugadorPosX);
        partida.setJugadorPosY(jugadorPosY);
        partida.setNivel(laberinto.getId());
        
        // Serializar la estructura del laberinto
        String estructuraSerializada = serializarEstructura(laberinto.getEstructura());
        partida.setEstructuraLaberinto(estructuraSerializada);

        partidaDAO.guardarPartida(partida);
        appendOutput("Guardando partida...");

        // Asegurar el foco
        SwingUtilities.invokeLater(() -> laberintoPanel.requestFocusInWindow());
    } catch (SQLException e) {
        logger.severe("Error al guardar la partida: " + e.getMessage());
        e.printStackTrace();
    }
}



public void cargarPartida(Usuario usuario) {
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
            PartidaDAO partidaDAO = new PartidaDAO();
            Partida partida = partidaDAO.recuperarPartida(usuario);

            if (partida != null) {
                appendOutput("Partida recuperada.");
                SwingUtilities.invokeLater(() -> {
                    int nivel = partida.getNivel(); // Recuperar el nivel
                    String estructuraSerializada = partida.getEstructuraLaberinto();
                    
                    if (estructuraSerializada != null) {
                        Cuadro[][] estructura = deserializarEstructura(estructuraSerializada); // Obtener la estructura del nivel
                        Laberinto laberinto = new Laberinto(nivel, "Nivel " + nivel, estructura);
                        
                        setLaberinto(laberinto); // Establecer el laberinto recuperado
                        setPuntuacion(partida.getPuntuacion());
                        setJugadorPos(partida.getJugadorPosX(), partida.getJugadorPosY());
                        laberintoPanel.setLaberinto(laberinto); // Actualizar el panel con el nuevo laberinto
                        laberintoPanel.setJugadorPos(jugadorPosX, jugadorPosY);
                        laberintoPanel.repaint();

                        // Asegurar el foco
                        laberintoPanel.requestFocusInWindow();
                    } else {
                        logger.severe("Error: estructuraSerializada es null");
                    }
                });
            } else {
                appendOutput("No se pudo cargar la partida.");
            }
            return null;
        }
    };
    worker.execute();
}



private String serializarEstructura(Cuadro[][] estructura) {
    if (estructura == null) return null;
    StringBuilder sb = new StringBuilder();
    for (Cuadro[] fila : estructura) {
        for (Cuadro cuadro : fila) {
            sb.append(cuadro.getTipo().name()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1); // Eliminar la última coma
        sb.append(";");
    }
    return sb.toString();
}

private Cuadro[][] deserializarEstructura(String estructuraSerializada) {
    if (estructuraSerializada == null || estructuraSerializada.isEmpty()) return null;
    String[] filas = estructuraSerializada.split(";");
    Cuadro[][] estructura = new Cuadro[filas.length][];
    for (int y = 0; y < filas.length; y++) {
        String[] cuadros = filas[y].split(",");
        estructura[y] = new Cuadro[cuadros.length];
        for (int x = 0; x < cuadros.length; x++) {
            estructura[y][x] = new Cuadro(Cuadro.Tipo.valueOf(cuadros[x]));
        }
    }
    return estructura;
}


private Cuadro[][] obtenerEstructuraPorNivel(int nivel) {
    switch (nivel) {
        case 1:
            return EstructurasLaberintos.NIVEL_1;
        case 2:
            return EstructurasLaberintos.NIVEL_2;
        case 3:
            return EstructurasLaberintos.NIVEL_3;
        // Añade más niveles según sea necesario
        default:
            throw new IllegalArgumentException("Nivel no válido: " + nivel);
    }
}


public void setLaberinto(Laberinto laberinto) {
    this.laberinto = laberinto;
    if (laberinto != null) {
        this.estructura = laberinto.getEstructura();
    } else {
        System.err.println("Error: Intentando establecer un laberinto null.");
    }
}







private void iniciarJuegoDesdePartida(Laberinto laberinto, int posX, int posY) {
    setLaberinto(laberinto);
    this.jugadorPosX = posX;
    this.jugadorPosY = posY;
    laberintoPanel.setLaberinto(laberinto);
    laberintoPanel.setJugadorPos(posX, posY);
    laberintoPanel.repaint();
}







    private void appendOutput(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }



    public Laberinto getLaberinto() {
        return this.laberinto;
    }

    public void setJugadorPos(int x, int y) {
        this.jugadorPosX = x;
        this.jugadorPosY = y;
    }




private int puntuacion;


public void setPuntuacion(int puntuacion) {
    this.puntuacion = puntuacion;
    appendOutput("Puntuación: " + this.puntuacion);
}

// Método para obtener la puntuación actual
public int getPuntuacion() {
    return puntuacion;
}

// Incrementar la puntuación al completar un nivel
private void incrementarPuntuacion(int puntos) {
    setPuntuacion(this.puntuacion + puntos);
}

// En los métodos iniciarNivel2 e iniciarNivel3
public void iniciarNivel2() {
    incrementarPuntuacion(100);
    Laberinto laberintoNivel2 = new Laberinto(2, "Nivel 2", EstructurasLaberintos.NIVEL_2);
    setLaberinto(laberintoNivel2);
    laberintoPanel.setLaberinto(laberintoNivel2);
    iniciarJuego();
    appendOutput("¡Has subido al Nivel 2!\n\n");
}

public void iniciarNivel3() {
    incrementarPuntuacion(100);
    Laberinto laberintoNivel3 = new Laberinto(3, "Nivel 3", EstructurasLaberintos.NIVEL_3);
    setLaberinto(laberintoNivel3);
    laberintoPanel.setLaberinto(laberintoNivel3);
    iniciarJuego();
    appendOutput("¡Has subido al Nivel 3!\n\n");
}

}