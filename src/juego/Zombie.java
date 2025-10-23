package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class Zombie {
    public double x, y;
    public double velocidad;
    public double velocidadNormal;
    public int fila;
    public int resistencia;
    public int resistenciaMaxima;
    public Image imagen;
    public Entorno e;
    public boolean vivo;
    public boolean ralentizado;
    public int ticksRalentizacion;
    public int golpesEscarcha; //Contador de golpes de escarcha
    
    public Zombie(int fila, Entorno e) {
        this.fila = fila;
        this.e = e;
        this.x = 1100;
        this.y = 140 + (fila * 100);
        this.velocidadNormal = 0.3;
        this.velocidad = velocidadNormal;
        this.resistenciaMaxima = 2;
        this.resistencia = resistenciaMaxima;
        this.vivo = true;
        this.ralentizado = false;
        this.ticksRalentizacion = 0;
        this.golpesEscarcha = 0; //Inicializar contador
        
        try {
            this.imagen = Herramientas.cargarImagen("zombieGrinch.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar zombieGrinch.png");
            this.imagen = null;
        }
    }
    
    public void dibujar() {
        if (vivo) {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, 0.1);
            }
            
            if (ralentizado) {
                // Dibujar aura azul cuando está ralentizado
                e.dibujarCirculo(x, y, 35, new Color(0, 150, 255, 100));
            }
        }
    }
    
    public void mover() {
        if (vivo) {
            x -= velocidad;
            
            if (ralentizado) {
                ticksRalentizacion--;
                if (ticksRalentizacion <= 0) {
                    ralentizado = false;
                    velocidad = velocidadNormal;
                }
            }
        }
    }
    
    public void recibirDanio() {
        resistencia--;
        if (resistencia <= 0) {
            morir();
        }
    }
    
    public void ralentizar(int duracion) {
        this.ralentizado = true;
        this.ticksRalentizacion = duracion;
        this.velocidad = velocidadNormal * 0.4; // 60% más lento
        
        // Contar golpes de escarcha y matar después de 4
        golpesEscarcha++;
        if (golpesEscarcha >= 4) {
            morir();
        }
    }
    
    public void morir() {
        vivo = false;
    }
    
    public boolean llegoARegalos() {
        return x <= 70;
    }
}