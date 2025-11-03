package juego;

import entorno.Entorno;

public class CerezaExplosiva extends planta {
    private double radioExplosion;
    private boolean explotando;
    private int tickExplosion;
    
    public CerezaExplosiva(double x, double y, Entorno e) {
        super(x, y, e, "CerezaExplosiva.png", "CerezaExplosiva.png", 0.11);
        
        this.setResistencia(1);
        this.setResistenciaMaxima(1);
        
        this.tiempoRecargaPlantado = 720;
        this.radioExplosion = 200;
        this.explotando = false;
        this.tickExplosion = -1;
    }
    
    @Override
    public void actualizar(int tickActual) {
        if (isBajoAtaque() && tickActual >= getTiempoFinAtaque()) {
            setBajoAtaque(false);
        }
    }
    
    @Override
    public void recibirAtaque(int tickActual) {
        this.setResistencia(this.getResistencia() - 1);
        this.setBajoAtaque(true);
        this.setTiempoFinAtaque(tickActual + 30);
        
        if (this.getResistencia() <= 0 && !explotando) {
            explotando = true;
            tickExplosion = tickActual;
        }
    }
    
    @Override
    public void usar(int tickActual) {
        this.disponibleParaPlantar = false;
        this.tiempoUltimoPlantado = tickActual;
    }
    
    @Override
    public void ejecutarComportamientoEspecifico(int tickActual, Juego juego) {
        // La cereza maneja su explosión internamente
        if (debeExplotar(tickActual)) {
            juego.ejecutarExplosionCereza(this, tickActual);
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
                    tickExplosion = getEntorno().numeroDeTick();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean debeExplotar(int tickActual) {
        return explotando && tickExplosion != -1 && tickActual >= tickExplosion;
    }
    
    // Getters específicos
    public double getRadioExplosion() { return radioExplosion; }
    public boolean isExplotando() { return explotando; }
    public void setExplotando(boolean explotando) { this.explotando = explotando; }
}