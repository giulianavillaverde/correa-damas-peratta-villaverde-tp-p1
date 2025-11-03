package juego;

import entorno.Entorno;

public class CerezaExplosiva extends planta {
    // Variables específicas públicas
    public double radioExplosion;
    public boolean explotando;
    public int tickExplosion;
    
    public CerezaExplosiva(double x, double y, Entorno e) {
        super(x, y, e, "CerezaExplosiva.png", "CerezaExplosiva.png", 0.11);
        
        this.resistencia = 1;
        this.resistenciaMaxima = 1;
        this.tiempoRecargaPlantado = 720;
        this.radioExplosion = 200;
        this.explotando = false;
        this.tickExplosion = -1;
    }
    
    public void actualizar(int tickActual) {
        if (bajoAtaque && tickActual >= tiempoFinAtaque) {
            bajoAtaque = false;
        }
    }
    
    public void recibirAtaque(int tickActual) {
        this.resistencia--;
        this.bajoAtaque = true;
        this.tiempoFinAtaque = tickActual + 30;
        
        if (this.resistencia <= 0 && !explotando) {
            explotando = true;
            tickExplosion = tickActual;
        }
    }
    
    public void usar(int tickActual) {
        this.disponibleParaPlantar = false;
        this.tiempoUltimoPlantado = tickActual;
    }
    
    public void ejecutarComportamientoEspecifico(int tickActual, Juego juego) {
        if (debeExplotar(tickActual)) {
            juego.ejecutarExplosionCereza(this, tickActual);
        }
    }
    
    public boolean hayZombieCerca(Zombie[] zombies) {
        if (!plantada || explotando) return false;
        
        for (Zombie zombie : zombies) {
            if (zombie != null && zombie.vivo) {
                double distancia = Math.sqrt(Math.pow(zombie.x - x, 2) + 
                                           Math.pow(zombie.y - y, 2));
                if (distancia < 50) {
                    explotando = true;
                    tickExplosion = e.numeroDeTick();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean debeExplotar(int tickActual) {
        return explotando && tickExplosion != -1 && tickActual >= tickExplosion;
    }
}