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
        this.y = 335; // Centro vertical (entre las 5 filas)
        this.velocidadNormal = 0.15; // Más lento que los zombies normales
        this.velocidad = velocidadNormal;
        this.resistenciaMaxima = 100; 
        this.resistencia = resistenciaMaxima;
        this.vivo = true;
        this.ralentizado = false;
        this.ticksRalentizacion = 0;
        this.golpesEscarcha = 0;
        this.tiempoUltimoAtaque = 0;
        this.cooldownAtaque = 180; // 3 segundos entre ataques
        
        try {
            this.imagen = Herramientas.cargarImagen("zombieColosal.png");
        } catch (Exception ex) {
            System.err.println("ERROR: No se pudo cargar zombieColosal.png - usando zombie normal");
            try {
                this.imagen = Herramientas.cargarImagen("zombieGrinch.png");
            } catch (Exception ex2) {
                this.imagen = null;
            }
        }
    }
    
    // NUEVO MÉTODO: getImagen()
    public Image getImagen() {
        return this.imagen;
    }
    
    public void dibujar() {
        if (vivo) {
            if (imagen != null) {
                e.dibujarImagen(imagen, x, y, 0, 0.2); // Más grande
            } else {
                // Fallback visual - dibujar un rectángulo grande rojo
                e.dibujarRectangulo(x, y, 100, 400, 0, Color.RED);
                e.dibujarRectangulo(x, y, 80, 380, 0, Color.DARK_GRAY);
            }
            
            // ELIMINADO: Barra de vida
            
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
    
    public boolean colisionaConPlanta(planta p) {
        if (!vivo || p == null || !p.plantada) return false;
        
        double distanciaX = Math.abs(x - p.x);
        double distanciaY = Math.abs(y - p.y);
        
        // Colisión más amplia porque ocupa todas las filas
        return distanciaX < 60 && distanciaY < 250; // Muy amplio verticalmente
    }
    
    public boolean puedeAtacar(int tickActual) {
        return (tickActual - tiempoUltimoAtaque) >= cooldownAtaque;
    }
    
    public void registrarAtaque(int tickActual) {
        this.tiempoUltimoAtaque = tickActual;
    }
    
    public void recibirDanio() {
        resistencia--;
        System.out.println("Zombie Colosal recibió daño! Resistencia: " + resistencia + "/" + resistenciaMaxima);
        if (resistencia <= 0) {
            morir();
        }
    }
    
    public void recibirDanioFuerte() {
        resistencia -= 2; // Recibe doble daño de algunos ataques
        System.out.println("Zombie Colosal recibió daño fuerte! Resistencia: " + resistencia + "/" + resistenciaMaxima);
        if (resistencia <= 0) {
            morir();
        }
    }
    
    public void ralentizar(int duracion) {
        this.ralentizado = true;
        this.ticksRalentizacion = duracion;
        this.velocidad = velocidadNormal * 0.3; // Más lento cuando está ralentizado
        
        golpesEscarcha++;
        if (golpesEscarcha >= 7) { // Necesita más golpes de hielo
            recibirDanioFuerte();
        }
    }
    
    public void morir() {
        vivo = false;
        System.out.println("¡Zombie Colosal derrotado!");
    }
    
    public boolean llegoARegalos() {
        return x <= 120; // Más ancho, llega antes a los regalos
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}