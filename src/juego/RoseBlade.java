package juego;
import entorno.Entorno;

public class RoseBlade extends planta {
    // TIEMPOS DE RECARGA SEPARADOS
    public int tiempoRecargaPlantado;
    public int tiempoRecargaDisparo;
    
    public int tiempoUltimoPlantado;
    public int tiempoUltimoDisparo;
    
    private boolean puedeDisparar;
    private boolean disponibleParaPlantar;
    
    public RoseBlade(double x, double y, Entorno e) {
        super(x, y, e, "roseblade.png", "roseblade.png", 0.10);
        
        // NUEVO: Resistencia específica para RoseBlade
        this.resistencia = 5;
        this.resistenciaMaxima = 5;
        
        // TIEMPOS SEPARADOS
        this.tiempoRecargaPlantado = 360;
        this.tiempoRecargaDisparo = 80;
        
        this.tiempoUltimoPlantado = -100;
        this.tiempoUltimoDisparo = -100;
        
        this.puedeDisparar = true;
        this.disponibleParaPlantar = true;
    }
    
    public void actualizar(int tickActual) {
        if (!puedeDisparar && tickActual - tiempoUltimoDisparo > tiempoRecargaDisparo) {
            puedeDisparar = true;
        }
        
        if (!disponibleParaPlantar && !plantada && tickActual - tiempoUltimoPlantado > tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
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
    
    public void usar(int tickActual) {
        disponibleParaPlantar = false;
        tiempoUltimoPlantado = tickActual;
    }
    
    public boolean estaEnRecarga(int tickActual) {
        if (disponibleParaPlantar) return false;
        
        if (tickActual - tiempoUltimoPlantado >= tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
            return false;
        }
        return true;
    }
    
    public double porcentajeRecarga(int tickActual) {
        if (disponibleParaPlantar) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoPlantado;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaPlantado);
    }
    
    public double porcentajeRecargaDisparo(int tickActual) {
        if (puedeDisparar) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoDisparo;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaDisparo);
    }
}