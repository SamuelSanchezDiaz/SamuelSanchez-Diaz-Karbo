package modelo;

public class Cuadro {
    public enum Tipo {
        CAMINO(' '),
        MURO('#'),
        INICIO('S'),
        FIN('E'),
        DERRUMBE('D'), 
        QUESO('Q'),
        BLOQUEADA('B'),
        VINO('V');
        
        private final char representacion;

        Tipo(char representacion) {
            this.representacion = representacion;
        }

        public char getRepresentacion() {
            return representacion;
        }

        public static Tipo desdeChar(char c) {
            for (Tipo tipo : values()) {
                if (tipo.getRepresentacion() == c) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo de cuadro no válido: " + c);
        }
    }

    private Tipo tipo;

    public Cuadro(char tipo) {
        this.tipo = Tipo.desdeChar(tipo);
    }

    public Cuadro(Tipo tipo) {
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public char getRepresentacion() {
        return tipo.getRepresentacion();
    }

    public void setRepresentacion(char representacion) {
        this.tipo = Tipo.desdeChar(representacion);
    }
}
