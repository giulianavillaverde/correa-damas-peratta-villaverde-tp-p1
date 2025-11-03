package juego;

import entorno.Entorno;

public class CerezaExplosiva extends planta {
    private int tiempoRecargaPlantado;
    private int tiempoUltimoPlantado;
    private boolean disponibleParaPlantar;
    private double radioExplosion;
    private boolean explotando;
    private int tickExplosion;
    
    public CerezaExplosiva(double x, double y, Entorno e) {
        super(x, y, e, "CerezaExplosiva.png", "CerezaExplosiva.png", 0.11);
        
        // Resistencia específica para CerezaExplosiva
        this.setResistencia(1); // Muy frágil, explota rápido
        this.setResistenciaMaxima(1);
        
        this.tiempoRecargaPlantado = 720;
        this.tiempoUltimoPlantado = -100;
        this.disponibleParaPlantar = true;
        this.radioExplosion = 200;
        this.explotando = false;
        this.tickExplosion = -1;
    }
    
    public void actualizar(int tickActual) {
        if (!disponibleParaPlantar && !isPlantada() && tickActual - tiempoUltimoPlantado > tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
        }
        
        // Manejar efecto de ataque basado en ticks
        if (isBajoAtaque() && tickActual >= getTiempoFinAtaque()) {
            setBajoAtaque(false);
        }
    }
    
    // Sobrescribir recibirAtaque para que explote inmediatamente
    @Override
    public void recibirAtaque(int tickActual) {
        this.setResistencia(this.getResistencia() - 1);
        this.setBajoAtaque(true);
        this.setTiempoFinAtaque(tickActual + 30); // 30 ticks ≈ 500ms
        System.out.println("CerezaExplosiva recibió ataque!");
        
        if (this.getResistencia() <= 0 && !explotando) {
            explotando = true;
            tickExplosion = tickActual;
        }
    }
    
    public boolean hayZombieCerca(Zombie[] zombies) {
        if (!isPlantada() || explotando) return false;
        
        for (Zombie zombie : zombies) {
            if (zombie != null && zombie.estaVivo()) {
                double distancia = Math.sqrt(Math.pow(zombie.getX() - getX(), 2) + 
                                           Math.pow(zombie.getY() - getY(), 2));
                if (distancia < 50) {
                    explotando = true;
                    tickExplosion = getEntorno().numeroDeTick(); // CORREGIDO: usar getEntorno()
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean debeExplotar(int tickActual) {
        return explotando && tickExplosion != -1 && tickActual >= tickExplosion;
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
    
    // Getters adicionales
    public boolean isExplotando() { return explotando; }
    public int getTiempoRecargaPlantado() { return tiempoRecargaPlantado; }
    public int getTiempoUltimoPlantado() { return tiempoUltimoPlantado; }
    
    // Setters adicionales
    public void setExplotando(boolean explotando) { this.explotando = explotando; }
}