package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class BolaFuego {
    private double x, y;
    private double velocidad;
    private Image imagen;
    private Entorno e;
    private boolean activa;
    
    public BolaFuego(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.velocidad = 4;
        
        try {
            this.imagen = Herramientas.cargarImagen("bolaFuego.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar bolaFuego.png");
            this.imagen = null;
        }
        
        this.activa = true;
    }
    
    public void dibujar() {
        if (activa && imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, 0.05);
        }
    }
    
    public void mover() {
        if (activa) {
            x += velocidad; // Se mueve hacia la derecha
            
            // Si sale de la pantalla, se desactiva
            if (x > 1100) {
                activa = false;
            }
        }
    }
    
    public boolean colisionaCon(Zombie zombie) {
        if (!activa || !zombie.estaVivo()) return false;
        
        double distancia = Math.sqrt(Math.pow(x - zombie.getX(), 2) + Math.pow(y - zombie.getY(), 2));
        return distancia < 25; // Radio de colisión
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isActiva() { return activa; }
    public Image getImagen() { return imagen; }
    
    // Setters
    public void setActiva(boolean activa) { this.activa = activa; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}