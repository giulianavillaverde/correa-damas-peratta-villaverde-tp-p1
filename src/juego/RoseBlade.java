package juego;
import entorno.Entorno;

public class RoseBlade extends planta {
    private int tiempoRecarga;
    private int tiempoUltimoDisparo;
    private boolean puedeDisparar;
    
    public RoseBlade(double x, double y, Entorno e) {
        super(x, y, e, "roseblade.png", "roseblade.png", 0.10);
        this.tiempoRecarga = 60;
        this.tiempoUltimoDisparo = 0;
        this.puedeDisparar = true;
    }
    
    public void actualizar(int tickActual) {
        if (tickActual - tiempoUltimoDisparo > tiempoRecarga) {
            puedeDisparar = true;
        }
    }
    
    public BolaFuego disparar(int tickActual) {
        if (puedeDisparar && plantada) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaFuego(x + 40, y, e);
        }
        return null;
    }
    
    public double porcentajeRecarga(int tickActual) {
        if (puedeDisparar) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoDisparo;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecarga);
    }

    public boolean estaEnRecarga(int tickActual) {
        return !puedeDisparar;
    }
}