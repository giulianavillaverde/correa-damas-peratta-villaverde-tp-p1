package juego;
import entorno.Entorno;

public class WallNut extends planta {
    public int tiempoRecargaPlantado;
    public int tiempoUltimoUso;
    private boolean disponibleParaPlantar;
    public int resistencia;
    
    public WallNut(double x, double y, Entorno e) {
        super(x, y, e, "wallnut.png", "wallnut.png", 0.11);
        this.tiempoRecargaPlantado = 360; // 1 minuto para volver a plantar
        this.tiempoUltimoUso = -100;
        this.disponibleParaPlantar = true;
        this.resistencia = 180; // 3 minutos de resistencia (180 ataques a 1 por segundo)
    }
    
    public boolean estaEnRecarga(int tickActual) {
        if (disponibleParaPlantar) return false;
        
        if (tickActual - tiempoUltimoUso >= tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
            return false;
        }
        return true;
    }
    
    public void usar(int tickActual) {
        disponibleParaPlantar = false;
        tiempoUltimoUso = tickActual;
    }
    
    public double porcentajeRecarga(int tickActual) {
        if (disponibleParaPlantar) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoUso;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaPlantado);
    }
    
    public void recibirAtaque() {
        this.resistencia--;
        System.out.println("WallNut recibió ataque, resistencia restante: " + resistencia);
    }
}