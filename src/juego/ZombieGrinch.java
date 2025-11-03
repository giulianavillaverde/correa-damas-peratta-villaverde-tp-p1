package juego;

import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class ZombieGrinch {
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
    public int tiempoUltimoDisparo;
    public int cooldownAtaque;
    public int cooldownDisparo;
    public boolean bloqueadoPorPlanta;
    public Object plantaBloqueadora; // Cambiado a Object
    
    public ZombieGrinch(int fila, Entorno e) {
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
        this.tiempoUltimoDisparo = 0;
        this.cooldownAtaque = 180;
        this.cooldownDisparo = 300;
        this.bloqueadoPorPlanta = false;
        this.plantaBloqueadora = null;
        
        try {
            this.imagen = Herramientas.cargarImagen("zombieGrinch.png");
        } catch (Exception ex) {
            this.imagen = null;
        }
    }
    
    public void dibujar() {
        if (vivo) {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, 0.1);
            } else {
                e.dibujarRectangulo(x, y, 40, 80, 0, Color.GREEN);
                e.dibujarRectangulo(x, y, 30, 70, 0, Color.DARK_GRAY);
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
        if (bloqueadoPorPlanta && plantaBloqueadora != null) {
            // Verificar si la planta sigue plantada
            boolean plantaViva = false;
            if (plantaBloqueadora instanceof WallNut) {
                plantaViva = ((WallNut)plantaBloqueadora).plantada;
            } else if (plantaBloqueadora instanceof PlantaDeHielo) {
                plantaViva = ((PlantaDeHielo)plantaBloqueadora).plantada;
            } else if (plantaBloqueadora instanceof RoseBlade) {
                plantaViva = ((RoseBlade)plantaBloqueadora).plantada;
            } else if (plantaBloqueadora instanceof CerezaExplosiva) {
                plantaViva = ((CerezaExplosiva)plantaBloqueadora).plantada;
            }
            
            if (!plantaViva) {
                liberar();
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
    
    // Métodos de colisión específicos para cada tipo de planta
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
    
    public boolean puedeDisparar(int tickActual) {
        return (tickActual - tiempoUltimoDisparo) >= cooldownDisparo;
    }
    
    public void registrarAtaque(int tickActual) {
        this.tiempoUltimoAtaque = tickActual;
    }
    
    public BolaNieve disparar(int tickActual) {
        if (puedeDisparar(tickActual)) {
            this.tiempoUltimoDisparo = tickActual;
            return new BolaNieve(x - 20, y, e);
        }
        return null;
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
        if (golpesEscarcha >= 5) {
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
    
    public boolean colisionaConBolaFuego(BolaFuego bola) {
        if (!vivo || bola == null || !bola.activa) return false;
        double distancia = Math.sqrt(Math.pow(x - bola.x, 2) + Math.pow(y - bola.y, 2));
        return distancia < 25;
    }
}