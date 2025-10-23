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
        this.imagen = Herramientas.cargarImagen("bolaFuego.png");
        this.activa = true;
    }
    
    public void dibujar() {
        if (activa) {
            e.dibujarImagen(imagen, x, y, 0, 0.08);
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
        if (!activa || !zombie.vivo) return false;
        
        double distancia = Math.sqrt(Math.pow(x - zombie.x, 2) + Math.pow(y - zombie.y, 2));
        return distancia < 25; // Radio de colisión
    }
}
