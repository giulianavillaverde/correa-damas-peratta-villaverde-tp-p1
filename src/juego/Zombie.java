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
    public int golpesEscarcha;
    public int tiempoUltimoAtaque;
    public int cooldownAtaque;
    public boolean bloqueadoPorPlanta; 
    public planta plantaBloqueadora;
    
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
        this.golpesEscarcha = 0;
        this.tiempoUltimoAtaque = 0;
        this.cooldownAtaque = 180;
        this.bloqueadoPorPlanta = false; 
        this.plantaBloqueadora = null;
        
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
                e.dibujarCirculo(x, y, 35, new Color(0, 150, 255, 100));
            }
        }
    }
    
    public void mover() {
        if (vivo) {
            if (!bloqueadoPorPlanta) { 
                x -= velocidad;
            }
            
            if (ralentizado) {
                ticksRalentizacion--;
                if (ticksRalentizacion <= 0) {
                    ralentizado = false;
                    velocidad = velocidadNormal;
                }
            }
        }
    }
    
    public void verificarPlantaBloqueadora() {
        if (bloqueadoPorPlanta && plantaBloqueadora != null) { // CAMBIADO
            if (!plantaBloqueadora.plantada) {
                liberar();
            }
        }
    }
    
    //Ahora bloquea con cualquier planta
    public void bloquear(planta planta) {
        this.bloqueadoPorPlanta = true;
        this.plantaBloqueadora = planta;
        this.velocidad = 0;
        System.out.println("Zombie bloqueado por " + planta.getClass().getSimpleName() + " en fila " + fila);
    }
    
    public void liberar() {
        this.bloqueadoPorPlanta = false; // CAMBIADO
        this.plantaBloqueadora = null;
        this.velocidad = ralentizado ? velocidadNormal * 0.2 : velocidadNormal;
        System.out.println("Zombie liberado en fila " + fila);
    }
    
    
    public boolean colisionaConPlanta(planta p) {
        if (!vivo || p == null || !p.plantada) return false;
        
        double distancia = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return distancia < 50; // AUMENTADO de 40 a 50
    }
    
    
    public boolean puedeAtacar(int tickActual) {
        return (tickActual - tiempoUltimoAtaque) >= cooldownAtaque;
    }
    
    public void registrarAtaque(int tickActual) {
        this.tiempoUltimoAtaque = tickActual;
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
        this.velocidad = velocidadNormal * 0.2;
        
        golpesEscarcha++;
        if (golpesEscarcha >= 6) {
            morir();
        }
    }
    
    public void morir() {
        vivo = false;
        if (bloqueadoPorPlanta) { 
            liberar();
        }
    }
    
    public boolean llegoARegalos() {
        return x <= 70;
    }
    
    public boolean estaBloqueado() {
        return bloqueadoPorPlanta; 
    }
}