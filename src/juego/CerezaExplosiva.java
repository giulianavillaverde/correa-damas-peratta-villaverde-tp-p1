package juego;
import entorno.Entorno;

public class CerezaExplosiva extends planta {
    public int tiempoRecargaPlantado;
    public int tiempoUltimoPlantado;
    private boolean disponibleParaPlantar;
    private double radioExplosion;
    private boolean explotando;
    
    public CerezaExplosiva(double x, double y, Entorno e) {
        super(x, y, e, "CerezaExplosiva.png", "CerezaExplosiva.png", 0.11);
        
        // NUEVO: Resistencia específica para CerezaExplosiva
        this.resistencia = 1; // Muy frágil, explota rápido
        this.resistenciaMaxima = 1;
        
        this.tiempoRecargaPlantado = 720;
        this.tiempoUltimoPlantado = -100;
        this.disponibleParaPlantar = true;
        this.radioExplosion = 200;
        this.explotando = false;
    }
    
    public void actualizar(int tickActual) {
        if (!disponibleParaPlantar && !plantada && tickActual - tiempoUltimoPlantado > tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
        }
    }
    
    // NUEVO: Sobrescribir recibirAtaque para que explote inmediatamente
    @Override
    public void recibirAtaque() {
        this.resistencia--;
        this.bajoAtaque = true;
        System.out.println("CerezaExplosiva recibió ataque!");
        
        new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                    bajoAtaque = false;
                }
            }, 
            500
        );
        
        if (resistencia <= 0 && !explotando) {
            explotando = true;
        }
    }
    
    public boolean hayZombieCerca(Zombie[] zombies) {
        if (!plantada || explotando) return false;
        
        for (Zombie zombie : zombies) {
            if (zombie != null && zombie.vivo) {
                double distancia = Math.sqrt(Math.pow(zombie.x - x, 2) + 
                                           Math.pow(zombie.y - y, 2));
                if (distancia < 50) {
                    explotando = true;
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean debeExplotar() {
        return explotando;
    }
    
    public double getRadioExplosion() {
        return radioExplosion;
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
}