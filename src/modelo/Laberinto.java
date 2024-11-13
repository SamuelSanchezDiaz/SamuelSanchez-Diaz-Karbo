package modelo;

public class Laberinto {
    private int id;
    private String nombre;
    private Cuadro[][] estructura;

    public Laberinto(int id, String nombre, Cuadro[][] estructura) {
        this.id = id;
        this.nombre = nombre;
        this.estructura = estructura;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Cuadro[][] getEstructura() {
        return estructura;
    }

    public void setEstructura(Cuadro[][] estructura) {
        this.estructura = estructura;
    }

    public int getAncho() {
        return estructura[0].length;
    }

    public int getAlto() {
        return estructura.length;
    }

    public Cuadro getCuadro(int x, int y) {
        return estructura[y][x];
    }

    // Método para obtener la posición X inicial del jugador
    public int getPosicionInicialX() {
        for (int y = 0; y < estructura.length; y++) {
            for (int x = 0; x < estructura[y].length; x++) {
                if (estructura[y][x].getTipo() == Cuadro.Tipo.INICIO) {
                    return x;
                }
            }
        }
        return -1; // No se encontró la posición inicial
    }

    // Método para obtener la posición Y inicial del jugador
    public int getPosicionInicialY() {
        for (int y = 0; y < estructura.length; y++) {
            for (int x = 0; x < estructura[y].length; x++) {
                if (estructura[y][x].getTipo() == Cuadro.Tipo.INICIO) {
                    return y;
                }
            }
        }
        return -1; // No se encontró la posición inicial
    }
    
    private int jugadorPosX;
    private int jugadorPosY;

    public int getJugadorPosX() {
        return jugadorPosX;
    }

    public void setJugadorPosX(int jugadorPosX) {
        this.jugadorPosX = jugadorPosX;
    }

    public int getJugadorPosY() {
        return jugadorPosY;
    }

    public void setJugadorPosY(int jugadorPosY) {
        this.jugadorPosY = jugadorPosY;
    }
}

