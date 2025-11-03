package juego;

import java.awt.Color;
import entorno.Entorno;

public class WallNut extends planta {
    private int tiempoRecargaPlantado;
    private int tiempoUltimoUso;
    private boolean disponibleParaPlantar;
    
    public WallNut(double x, double y, Entorno e) {
        super(x, y, e, "wallnut.png", "wallnut.png", 0.11);
        
        // Resistencia específica para WallNut (más alta)
        this.setResistencia(10);
        this.setResistenciaMaxima(10);
        
        this.tiempoRecargaPlantado = 360;
        this.tiempoUltimoUso = -100;
        this.disponibleParaPlantar = true;
    }
    
    @Override
    public void dibujar() {
        super.dibujar(); // Usa el dibujar de la clase base
        
        // Manejar efecto de ataque basado en ticks
        if (isBajoAtaque() && getEntorno().numeroDeTick() >= getTiempoFinAtaque()) {
            setBajoAtaque(false);
        }
        
        // Punto rojo más grande para WallNut
        if (isBajoAtaque() && isPlantada()) {
            getEntorno().dibujarCirculo(getX(), getY(), 45, new Color(255, 0, 0, 100));
        }
    }
    
    @Override
    public void recibirAtaque(int tickActual) {
        this.setResistencia(this.getResistencia() - 1);
        this.setBajoAtaque(true);
        this.setTiempoFinAtaque(tickActual + 30); // 30 ticks ≈ 500ms
        System.out.println("WallNut recibió ataque, resistencia: " + getResistencia() + "/" + getResistenciaMaxima());
        
        if (this.getResistencia() <= 0) {
            System.out.println("WallNut destruida!");
            this.setPlantada(false);
        }
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
    
    // Getters adicionales
    public boolean isDisponibleParaPlantar() { return disponibleParaPlantar; }
    public int getTiempoRecargaPlantado() { return tiempoRecargaPlantado; }
    public int getTiempoUltimoUso() { return tiempoUltimoUso; }
}