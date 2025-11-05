package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class BolaFuego {
    public double x, y;
    public double velocidad;
    public Image imagen;
    public Entorno e;
    public boolean activa;
    
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
    
    // Dibuja la bola de fuego si está activa y tiene imagen
    public void dibujar() {
        if (activa && imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, 0.05);
        }
    }
    
    // Mueve la bola hacia la derecha y la desactiva si sale de pantalla
    public void mover() {
        if (activa) {
            x += velocidad;
            if (x > 1100) {
                activa = false;
            }
        }
    }
    
 
    // Verifican colisión con diferentes tipos de zombies usando la distancia 
    // Radio de colisión: 25 píxeles para todos los zombies
    
    public boolean colisionaConGrinch(ZombieGrinch zombie) {
        if (!activa || !zombie.vivo) return false;
        double distancia = Math.sqrt(Math.pow(x - zombie.x, 2) + Math.pow(y - zombie.y, 2));
        return distancia < 25;
    }
    
    public boolean colisionaConRapido(ZombieRapido zombie) {
        if (!activa || !zombie.vivo) return false;
        double distancia = Math.sqrt(Math.pow(x - zombie.x, 2) + Math.pow(y - zombie.y, 2));
        return distancia < 25;
    }
    
    public boolean colisionaConColosal(ZombieColosal zombie) {
        if (!activa || !zombie.vivo) return false;
        double distancia = Math.sqrt(Math.pow(x - zombie.x, 2) + Math.pow(y - zombie.y, 2));
        return distancia < 25;
    }
}