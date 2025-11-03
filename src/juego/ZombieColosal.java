package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class ZombieColosal {
    public double x, y;
    public double velocidad;
    public double velocidadNormal;
    public Image imagen;
    public Entorno e;
    public boolean vivo;
    public boolean ralentizado;
    public int ticksRalentizacion;
    public int golpesEscarcha;
    public int tiempoUltimoAtaque;
    public int cooldownAtaque;
    public int resistencia;
    public int resistenciaMaxima;
    
    public ZombieColosal(Entorno e) {
        this.e = e;
        this.x = 1100;
        this.y = 335;
        this.velocidadNormal = 0.15;
        this.velocidad = velocidadNormal;
        this.resistenciaMaxima = 100;
        this.resistencia = resistenciaMaxima;
        this.vivo = true;
        this.ralentizado = false;
        this.ticksRalentizacion = 0;
        this.golpesEscarcha = 0;
        this.tiempoUltimoAtaque = 0;
        this.cooldownAtaque = 180;
        
        try {
            this.imagen = Herramientas.cargarImagen("zombieColosal.png");
        } catch (Exception ex) {
            try {
                this.imagen = Herramientas.cargarImagen("zombieGrinch.png");
            } catch (Exception ex2) {
                this.imagen = null;
            }
        }
    }
    
    public void dibujar() {
        if (vivo) {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, 0.2);
            } else {
                e.dibujarRectangulo(x, y, 100, 400, 0, Color.RED);
                e.dibujarRectangulo(x, y, 80, 380, 0, Color.DARK_GRAY);
            }
            
            if (ralentizado) {
                e.dibujarCirculo(x, y, 80, new Color(0, 150, 255, 100));
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
    
    // Métodos de colisión específicos
    public boolean colisionaConWallnut(WallNut p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distanciaX = Math.abs(x - p.x);
        double distanciaY = Math.abs(y - p.y);
        return distanciaX < 60 && distanciaY < 250;
    }
    
    public boolean colisionaConPlantaHielo(PlantaDeHielo p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distanciaX = Math.abs(x - p.x);
        double distanciaY = Math.abs(y - p.y);
        return distanciaX < 60 && distanciaY < 250;
    }
    
    public boolean colisionaConRoseBlade(RoseBlade p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distanciaX = Math.abs(x - p.x);
        double distanciaY = Math.abs(y - p.y);
        return distanciaX < 60 && distanciaY < 250;
    }
    
    public boolean colisionaConCereza(CerezaExplosiva p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distanciaX = Math.abs(x - p.x);
        double distanciaY = Math.abs(y - p.y);
        return distanciaX < 60 && distanciaY < 250;
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
    
    public void recibirDanioFuerte() {
        resistencia -= 2;
        if (resistencia <= 0) {
            morir();
        }
    }
    
    public void ralentizar(int duracion) {
        this.ralentizado = true;
        this.ticksRalentizacion = duracion;
        this.velocidad = velocidadNormal * 0.3;
        
        golpesEscarcha++;
        if (golpesEscarcha >= 7) {
            recibirDanioFuerte();
        }
    }
    
    public void morir() {
        vivo = false;
    }
    
    public boolean llegoARegalos() {
        return x <= 120;
    }
    
    public boolean estaVivo() {
        return vivo;
    }
}