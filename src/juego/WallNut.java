package juego;

import java.awt.Color;
import entorno.Entorno;

public class WallNut extends planta {
    public int tiempoRecargaPlantado;
    public int tiempoUltimoUso;
    private boolean disponibleParaPlantar;
    public int resistencia;
    public boolean bajoAtaque; // SOLO para el punto rojo
    
    public WallNut(double x, double y, Entorno e) {
        super(x, y, e, "wallnut.png", "wallnut.png", 0.11);
        this.tiempoRecargaPlantado = 360;
        this.tiempoUltimoUso = -100;
        this.disponibleParaPlantar = true;
        this.resistencia = 10; 
        this.bajoAtaque = false;
    }
    
    @Override
    public void dibujar() {
        if (seleccionada) {
            if (imagenSeleccionada != null) {
                e.dibujarImagen(imagenSeleccionada, x, y, 0, escala);
            } else if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, escala);
            }
        } else {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, escala);
            }
        }
        
        // SOLO punto rojo cuando está bajo ataque (sin texto)
        if (bajoAtaque && plantada) {
            e.dibujarCirculo(x, y, 45, new Color(255, 0, 0, 100)); // Rojo semitransparente
        }
        
        // ELIMINADO: Barra de resistencia
    }
    
    public void recibirAtaque() {
        this.resistencia--;
        this.bajoAtaque = true;
        System.out.println("WallNut recibió ataque, resistencia restante: " + resistencia);
        
        // Programar para quitar el efecto visual después de un tiempo
        new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                    bajoAtaque = false;
                }
            }, 
            500 // 500 milisegundos = 0.5 segundos
        );
        
        if (resistencia <= 0) {
            System.out.println("WallNut destruida!");
            plantada = false;
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