package modelo;

public class Partida {
    private Usuario usuario;
    private Laberinto laberinto;
    private String estado;
    private int puntuacion;
    private int jugadorPosX;
    private int jugadorPosY;
    private int nivel;  // Nueva propiedad
     private String estructuraLaberinto;
    // Constructor
    public Partida(Usuario usuario) {
        this.usuario = usuario;
    }

    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

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

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
        public String getEstructuraLaberinto() {
        return estructuraLaberinto;
    }

    public void setEstructuraLaberinto(String estructuraLaberinto) {
        this.estructuraLaberinto = estructuraLaberinto;
    }

}
