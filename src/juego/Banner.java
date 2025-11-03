package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class Banner {
    private double x, y, escala;
    private Image imagen;
    private Entorno e;
    
    public Banner(Entorno e) {
        this.x = -5;
        this.y = 28;
        this.e = e;
        this.escala = 3;
        
        try {
            this.imagen = Herramientas.cargarImagen("Bannerrr.jpg");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar Bannerrr.jpg");
            this.imagen = null;
        }
    }
    
    public void dibujar() {
        if (imagen != null) {
            e.dibujarImagen(imagen, x, y, 0.0, escala);
        } else {
            // Fallback
            e.dibujarRectangulo(500, 30, 1000, 60, 0, Color.DARK_GRAY);
        }
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getEscala() { return escala; }
    public Image getImagen() { return imagen; }
}