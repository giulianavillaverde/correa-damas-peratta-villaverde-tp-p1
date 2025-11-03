package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class regalos {
    private double x, y, escala, alto, ancho;
    private double angulo;
    private Entorno e;
    private Image imagen;
    
    public regalos(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.escala = 0.15;
        this.angulo = 0;
        
        try {
            this.imagen = Herramientas.cargarImagen("regalo.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar regalo.png");
            this.imagen = null;
        }
        
        this.ancho = 30;
        this.alto = 30;
    }
    
    public void dibujar() {
        if (imagen != null) {
            e.dibujarImagen(imagen, x, y, this.angulo, this.escala);
        } else {
            // Fallback si no hay imagen
            e.dibujarRectangulo(x, y, 25, 25, 0, Color.RED);
            e.dibujarRectangulo(x, y, 15, 25, 0, Color.GREEN);
        }
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getEscala() { return escala; }
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
    public double getAngulo() { return angulo; }
    public Image getImagen() { return imagen; }
    
    // Setters
    public void setPosicion(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setEscala(double escala) {
        this.escala = escala;
    }
    
    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }
}