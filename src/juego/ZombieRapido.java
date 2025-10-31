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
    public planta plantaBloqueadora;
    
    public ZombieRapido(int fila, Entorno e) {
        this.fila = fila;
        this.e = e;
        this.x = 1100;
        this.y = 140 + (fila * 100);
        this.velocidadNormal = 0.8; // Mucho más rápido que el zombie normal (0.3)
        this.velocidad = velocidadNormal;
        this.resistenciaMaxima = 65; // Menos resistencia que el zombie normal (2)
        this.resistencia = resistenciaMaxima;
        this.vivo = true;
        this.ralentizado = false;
        this.ticksRalentizacion = 0;
        this.golpesEscarcha = 0;
        this.tiempoUltimoAtaque = 0;
        this.cooldownAtaque = 120; // Ataca más rápido (2 segundos vs 3 segundos)
        this.bloqueadoPorPlanta = false; 
        this.plantaBloqueadora = null;
        
        try {
            this.imagen = Herramientas.cargarImagen("zombieRapido.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar zombieRapido.png - usando zombie normal");
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
                // Fallback visual - dibujar un zombie verde más delgado y rápido
                e.dibujarRectangulo(x, y, 40, 80, 0, new Color(0, 200, 0)); // Verde más brillante
                e.dibujarRectangulo(x, y, 30, 70, 0, new Color(100, 255, 100)); // Verde neón
                // Indicador visual de velocidad
                e.dibujarRectangulo(x + 25, y, 10, 5, 0, Color.YELLOW); // Efecto de movimiento
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
            if (!plantaBloqueadora.plantada) {
                liberar();
            }
        }
    }
    
    public void bloquear(planta planta) {
        this.bloqueadoPorPlanta = true;
        this.plantaBloqueadora = planta;
        this.velocidad = 0;
        System.out.println("Zombie Rápido bloqueado por " + planta.getClass().getSimpleName() + " en fila " + fila);
    }
    
    public void liberar() {
        this.bloqueadoPorPlanta = false;
        this.plantaBloqueadora = null;
        this.velocidad = ralentizado ? velocidadNormal * 0.2 : velocidadNormal;
        System.out.println("Zombie Rápido liberado en fila " + fila);
    }
    
    public boolean colisionaConPlanta(planta p) {
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
        this.velocidad = velocidadNormal * 0.3; // Más afectado por la ralentización
        
        golpesEscarcha++;
        if (golpesEscarcha >= 3) { // Menos resistente al hielo que los normales
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
    
    // Métodos getter para compatibilidad
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}