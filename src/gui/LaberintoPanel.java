package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import modelo.Cuadro;
import modelo.Laberinto;

public class LaberintoPanel extends JPanel {
    
    private Laberinto laberinto;
    private int jugadorPosX;
    private int jugadorPosY;
    private ImageIcon iconoPersonaje;
    private boolean mostrarPersonaje = true;
    private BufferedImage muroImage;
    private BufferedImage escaleraImage;
    private BufferedImage escalerabImage;
    private BufferedImage sueloImage;
    private BufferedImage quesoImage;
    private BufferedImage vinoImage;
       

    public void setMostrarPersonaje(boolean mostrar) {
        this.mostrarPersonaje = mostrar;
    }

    public boolean isMostrarPersonaje() {
        return mostrarPersonaje;
    }
    public LaberintoPanel(Laberinto laberinto) {
        this.laberinto = laberinto;
        this.iconoPersonaje = new ImageIcon(getClass().getResource("/resources/icon.png"));

        if (this.iconoPersonaje.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.out.println("Error al cargar la imagen del personaje.");
        } else {
            System.out.println("Imagen del personaje cargada correctamente.");
        }

        // Pre-cargar las imágenes
        try {
            muroImage = ImageIO.read(getClass().getResource("/resources/muro.png"));
            escaleraImage = ImageIO.read(getClass().getResource("/resources/escalera.png"));
            escalerabImage = ImageIO.read(getClass().getResource("/resources/escalerab.png"));
            sueloImage = ImageIO.read(getClass().getResource("/resources/suelo.png"));
            quesoImage = ImageIO.read(getClass().getResource("/resources/queso.png"));
            vinoImage = ImageIO.read(getClass().getResource("/resources/vino.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set preferred size to match the maze dimensions
        int mazeWidth = laberinto.getEstructura()[0].length * 50;
        int mazeHeight = laberinto.getEstructura().length * 50;
        setPreferredSize(new Dimension(mazeWidth, mazeHeight));
    }

// Declarar la variable estructura en LaberintoPanel
private Cuadro[][] estructura;

// En el método setLaberinto
public void setLaberinto(Laberinto laberinto) {
    if (laberinto != null) {
        this.laberinto = laberinto;
        this.estructura = laberinto.getEstructura();
        int mazeWidth = estructura[0].length * 50;
        int mazeHeight = estructura.length * 50;
        setPreferredSize(new Dimension(mazeWidth, mazeHeight));
        repaint();
    } else {
        System.err.println("Error: El laberinto es null en setLaberinto");
    }
}




    public void setJugadorPos(int x, int y) {
        this.jugadorPosX = x;
        this.jugadorPosY = y;
    }

  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("paintComponent called");

        if (laberinto.getEstructura() != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int laberintoWidth = laberinto.getEstructura()[0].length * 50;
            int laberintoHeight = laberinto.getEstructura().length * 50;
            int cellSize = Math.min(panelWidth / laberinto.getEstructura()[0].length, panelHeight / laberinto.getEstructura().length);
            int startX = (panelWidth - laberinto.getEstructura()[0].length * cellSize) / 2;
            int startY = (panelHeight - laberinto.getEstructura().length * cellSize) / 2;

            System.out.println("Panel Width: " + panelWidth + ", Panel Height: " + panelHeight);
            System.out.println("Laberinto Width: " + laberintoWidth + ", Laberinto Height: " + laberintoHeight);
            System.out.println("Start X: " + startX + ", Start Y: " + startY);

            for (int i = 0; i < laberinto.getEstructura().length; i++) {
                for (int j = 0; j < laberinto.getEstructura()[i].length; j++) {
                    int x = startX + j * cellSize;
                    int y = startY + i * cellSize;

                    char tipo = laberinto.getEstructura()[i][j].getTipo().getRepresentacion();
                    System.out.println("Drawing type " + tipo + " at (" + x + ", " + y + ")");

                    switch (tipo) {
                        case '#':
                            g.drawImage(muroImage, x, y, cellSize, cellSize, this);
                            break;
                        case 'S':
                            g.setColor(Color.GREEN);
                            g.fillRect(x, y, cellSize, cellSize);
                            break;
                        case 'E':
                            g.drawImage(escaleraImage, x, y, cellSize, cellSize, this);
                            break;
                        case 'D':
                            g.drawImage(sueloImage, x, y, cellSize, cellSize, this);
                            break;
                        case 'B':
                            g.drawImage(escalerabImage, x, y, cellSize, cellSize, this);
                            break;
                        case 'Q':
                            g.drawImage(quesoImage, x, y, cellSize, cellSize, this);
                            break;
                        case 'V':
                            g.drawImage(vinoImage, x, y, cellSize, cellSize, this);
                            break;
                        default:
                            g.drawImage(sueloImage, x, y, cellSize, cellSize, this);
                            break;
                    }
                }
            }

            if (mostrarPersonaje) {
                g.drawImage(iconoPersonaje.getImage(), startX + jugadorPosX * cellSize, startY + jugadorPosY * cellSize, cellSize, cellSize, this);
                System.out.println("Character drawn at (" + (startX + jugadorPosX * cellSize) + ", " + (startY + jugadorPosY * cellSize) + ")");
            }
        } else {
            System.out.println("Laberinto structure is null");
        }
    }
    
}
