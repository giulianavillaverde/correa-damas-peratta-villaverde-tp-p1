package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class BolaEscarcha {
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
    
    // Dibuja la bola de escarcha o un círculo de respaldo si no hay imagen
    public void dibujar() {
        if (activa) {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, 0.05);
            } else {
                e.dibujarCirculo(x, y, 12, Color.CYAN);
                e.dibujarCirculo(x, y, 6, Color.BLUE);
            }
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
    
 
    // Estos métodos verifican si la bola colisiona con diferentes tipos de zombies
    // Retornan true si la distancia es menor a 25 píxeles (radio de colisión)
    
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