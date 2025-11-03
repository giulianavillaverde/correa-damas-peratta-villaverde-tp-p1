package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class BolaEscarcha {
    private double x, y;
    private double velocidad;
    private Image imagen;
    private Entorno e;
    private boolean activa;
    private int duracionRalentizacion;
    
    public BolaEscarcha(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.velocidad = 3;
        this.activa = true;
        this.duracionRalentizacion = 120;
        
        try {
            this.imagen = Herramientas.cargarImagen("bolaEscarcha.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar bolaEscarcha.png");
            this.imagen = null;
        }
    }
    
    public void dibujar() {
        if (activa) {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, 0.05);
            } else {
                // Fallback visual
                e.dibujarCirculo(x, y, 12, Color.CYAN);
                e.dibujarCirculo(x, y, 6, Color.BLUE);
            }
        }
    }
    
    public void mover() {
        if (activa) {
            x += velocidad;
            if (x > 1100) {
                activa = false;
            }
        }
    }
    
    public boolean colisionaCon(Zombie zombie) {
        if (!activa || !zombie.estaVivo()) return false;
        double distancia = Math.sqrt(Math.pow(x - zombie.getX(), 2) + Math.pow(y - zombie.getY(), 2));
        return distancia < 25;
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isActiva() { return activa; }
    public int getDuracionRalentizacion() { return duracionRalentizacion; }
    
    // Setters
    public void setActiva(boolean activa) { this.activa = activa; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}