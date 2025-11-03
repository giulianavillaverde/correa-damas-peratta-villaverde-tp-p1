package juego;

import entorno.Entorno;

public class RoseBlade extends planta {
    // TIEMPOS DE RECARGA SEPARADOS
    private int tiempoRecargaPlantado;
    private int tiempoRecargaDisparo;
    
    private int tiempoUltimoPlantado;
    private int tiempoUltimoDisparo;
    
    private boolean puedeDisparar;
    private boolean disponibleParaPlantar;
    
    public RoseBlade(double x, double y, Entorno e) {
        super(x, y, e, "roseblade.png", "roseblade.png", 0.10);
        
        // Resistencia específica para RoseBlade
        this.setResistencia(5);
        this.setResistenciaMaxima(5);
        
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
        
        if (!disponibleParaPlantar && !isPlantada() && tickActual - tiempoUltimoPlantado > tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
        }
        
        // Manejar efecto de ataque basado en ticks
        if (isBajoAtaque() && tickActual >= getTiempoFinAtaque()) {
            setBajoAtaque(false);
        }
    }
    
    public BolaFuego disparar(int tickActual) {
        if (puedeDisparar && isPlantada()) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaFuego(getX() + 40, getY(), getEntorno());
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
    
    // Getters adicionales
    public boolean isPuedeDisparar() { return puedeDisparar; }
    public boolean isDisponibleParaPlantar() { return disponibleParaPlantar; }
    public int getTiempoRecargaPlantado() { return tiempoRecargaPlantado; }
    public int getTiempoRecargaDisparo() { return tiempoRecargaDisparo; }
    public int getTiempoUltimoPlantado() { return tiempoUltimoPlantado; } // AGREGADO
}