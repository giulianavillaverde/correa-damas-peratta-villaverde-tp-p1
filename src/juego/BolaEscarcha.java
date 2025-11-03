package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class BolaEscarcha {
    // Variables públicas
    public double x, y;
    public double velocidad;
    public Image imagen;
    public Entorno e;
    public boolean activa;
    public int duracionRalentizacion;
    
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
        if (!activa || !zombie.vivo) return false;
        double distancia = Math.sqrt(Math.pow(x - zombie.x, 2) + Math.pow(y - zombie.y, 2));
        return distancia < 25;
    }
}