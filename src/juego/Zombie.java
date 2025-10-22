package juego;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Zombie {
    public double x, y;
    public double velocidad;
    public int fila;
    public int resistencia;
    public int resistenciaMaxima;
    public Image imagen;
    public Entorno e;
    public boolean vivo;
    
    public Zombie(int fila, Entorno e) {
        this.fila = fila;
        this.e = e;
        this.x = 1100; // Aparece fuera de pantalla por la derecha
        this.y = 140 + (fila * 100); // Posición Y según la fila
        this.velocidad = 0.3;
        this.resistenciaMaxima = 2; // Requiere 2 disparos para morir
        this.resistencia = resistenciaMaxima;
        this.imagen = Herramientas.cargarImagen("zombieGrinch.png");
        this.vivo = true;
    }
    
    public void dibujar() {
        if (vivo) {
            e.dibujarImagen(imagen, x, y, 0, 0.1);
        }
    }
    
    public void mover() {
        if (vivo) {
            x -= velocidad; // Se mueve hacia la izquierda (hacia los regalos)
        }
    }
    
    public void recibirDanio() {
        resistencia--;
        if (resistencia <= 0) {
            morir();
        }
    }
    
    public void morir() {
        vivo = false;
    }
    
    public boolean llegoARegalos() {
        return x <= 70; // Si llega a la primera columna (regalos)
    }
}