package juego;
import entorno.Entorno;

public class PlantaDeHielo extends planta {
    private int tiempoRecarga;
    private int tiempoUltimoDisparo;
    private boolean puedeDisparar;
    
    public PlantaDeHielo(double x, double y, Entorno e) {
        super(x, y, e, "plantaHielo.png", "plantaHielo.png", 0.12);
        this.tiempoRecarga = 80;
        this.tiempoUltimoDisparo = 0;
        this.puedeDisparar = true;
    }
    
    public void actualizar(int tickActual) {
        if (tickActual - tiempoUltimoDisparo > tiempoRecarga) {
            puedeDisparar = true;
        }
    }
    
    public BolaEscarcha disparar(int tickActual) {
        if (puedeDisparar && plantada) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaEscarcha(x + 40, y, e);
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