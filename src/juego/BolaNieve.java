package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class BolaNieve {
    private double x, y;
    private double velocidad;
    private boolean activa;
    private int danio;
    private Entorno e;
    private Image imagen;
    
    public BolaNieve(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.velocidad = 2;
        this.activa = true;
        this.danio = 1;
        this.e = e;
        
        // CARGAR IMAGEN DE BOLA DE NIEVE
        try {
            this.imagen = Herramientas.cargarImagen("Snowball.png");
        } catch (Exception ex) {
            this.imagen = null;
        }
    }
    
    public void mover() {
        if (activa) {
            x -= velocidad;
            if (x < -50) {
                activa = false;
            }
        }
    }
    
    public void dibujar() {
        if (activa && imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, 0.05);
        }
    }
    
    public boolean colisionaCon(planta p) {
        if (!activa || p == null || !p.isPlantada()) return false;
        
        double distancia = Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2));
        return distancia < 30;
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isActiva() { return activa; }
    public int getDanio() { return danio; }
    public Image getImagen() { return imagen; }
    
    // Setters
    public void setActiva(boolean activa) { this.activa = activa; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}