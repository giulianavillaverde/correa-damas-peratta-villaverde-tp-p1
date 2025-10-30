package juego;

import java.awt.Color;
import entorno.Entorno;

public class WallNut extends planta {
    public int tiempoRecargaPlantado;
    public int tiempoUltimoUso;
    private boolean disponibleParaPlantar;
    
    public WallNut(double x, double y, Entorno e) {
        super(x, y, e, "wallnut.png", "wallnut.png", 0.11);
        
        // NUEVO: Resistencia específica para WallNut (más alta)
        this.resistencia = 10;
        this.resistenciaMaxima = 10;
        
        this.tiempoRecargaPlantado = 360;
        this.tiempoUltimoUso = -100;
        this.disponibleParaPlantar = true;
    }
    
    @Override
    public void dibujar() {
        super.dibujar(); // Usa el dibujar de la clase base
        
        // Punto rojo más grande para WallNut
        if (bajoAtaque && plantada) {
            e.dibujarCirculo(x, y, 45, new Color(255, 0, 0, 100));
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
}