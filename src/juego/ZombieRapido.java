package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class ZombieRapido {
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
    public Object plantaBloqueadora; // Cambiado a Object
    
    public ZombieRapido(int fila, Entorno e) {
        this.fila = fila;
        this.e = e;
        this.x = 1100;
        this.y = 140 + (fila * 100);
        this.velocidadNormal = 0.8;
        this.velocidad = velocidadNormal;
        this.resistenciaMaxima = 65;
        this.resistencia = resistenciaMaxima;
        this.vivo = true;
        this.ralentizado = false;
        this.ticksRalentizacion = 0;
        this.golpesEscarcha = 0;
        this.tiempoUltimoAtaque = 0;
        this.cooldownAtaque = 120;
        this.bloqueadoPorPlanta = false;
        this.plantaBloqueadora = null;
        
        try {
            this.imagen = Herramientas.cargarImagen("zombieRapido.png");
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
                e.dibujarImagen(imagen, x, y, 0, 0.1);
            } else {
                e.dibujarRectangulo(x, y, 40, 80, 0, new Color(0, 200, 0));
                e.dibujarRectangulo(x, y, 30, 70, 0, new Color(100, 255, 100));
                e.dibujarRectangulo(x + 25, y, 10, 5, 0, Color.YELLOW);
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
    
    public void verificarPlantaBloqueadora(WallNut[] wallnuts, PlantaDeHielo[] hielos, RoseBlade[] roses, CerezaExplosiva[] cerezas) {
        if (bloqueadoPorPlanta && plantaBloqueadora != null) {
            boolean siguePlantada = false;

            for (int i = 0; i < wallnuts.length; i++) {
                if (wallnuts[i] == plantaBloqueadora && wallnuts[i].plantada) {
                    siguePlantada = true;
                    break;
                }
            }
            for (int i = 0; i < hielos.length; i++) {
                if (hielos[i] == plantaBloqueadora && hielos[i].plantada) {
                    siguePlantada = true;
                    break;
                }
            }
            for (int i = 0; i < roses.length; i++) {
                if (roses[i] == plantaBloqueadora && roses[i].plantada) {
                    siguePlantada = true;
                    break;
                }
            }
            for (int i = 0; i < cerezas.length; i++) {
                if (cerezas[i] == plantaBloqueadora && cerezas[i].plantada) {
                    siguePlantada = true;
                    break;
                }
            }

            if (!siguePlantada) {
                bloqueadoPorPlanta = false;
                plantaBloqueadora = null;
            }
        }
    }
    
    public void bloquear(Object planta) {
        this.bloqueadoPorPlanta = true;
        this.plantaBloqueadora = planta;
        this.velocidad = 0;
    }
    
    public void liberar() {
        this.bloqueadoPorPlanta = false;
        this.plantaBloqueadora = null;
        this.velocidad = ralentizado ? velocidadNormal * 0.2 : velocidadNormal;
    }
    
    // Métodos de colisión específicos
    public boolean colisionaConWallnut(WallNut p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distancia = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return distancia < 50;
    }
    
    public boolean colisionaConPlantaHielo(PlantaDeHielo p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distancia = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return distancia < 50;
    }
    
    public boolean colisionaConRoseBlade(RoseBlade p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distancia = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return distancia < 50;
    }
    
    public boolean colisionaConCereza(CerezaExplosiva p) {
        if (!vivo || p == null || !p.plantada) return false;
        double distancia = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return distancia < 50;
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
        this.velocidad = velocidadNormal * 0.3;
        
        golpesEscarcha++;
        if (golpesEscarcha >= 3) {
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
    
    public boolean estaVivo() {
        return vivo;
    }
}