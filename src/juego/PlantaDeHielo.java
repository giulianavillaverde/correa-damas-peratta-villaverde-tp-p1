package juego;

import entorno.Entorno;

public class PlantaDeHielo extends planta {
    // Variables específicas 
    public int tiempoRecargaDisparo;
    public int tiempoUltimoDisparo;
    public boolean puedeDisparar;
    
    public PlantaDeHielo(double x, double y, Entorno e) {
        super(x, y, e, "plantaHielo.png", "plantaHielo.png", 0.12);
        
        this.resistencia = 5;
        this.resistenciaMaxima = 5;
        this.tiempoRecargaPlantado = 1080;
        this.tiempoRecargaDisparo = 45;
        this.tiempoUltimoDisparo = -100;
        this.puedeDisparar = true;
    }
    
    public void actualizar(int tickActual) {
        if (!puedeDisparar && tickActual - tiempoUltimoDisparo > tiempoRecargaDisparo) {
            puedeDisparar = true;
        }
        
        if (bajoAtaque && tickActual >= tiempoFinAtaque) {
            bajoAtaque = false;
        }
    }
    
    public void recibirAtaque(int tickActual) {
        this.resistencia--;
        this.bajoAtaque = true;
        this.tiempoFinAtaque = tickActual + 30;
        
        if (this.resistencia <= 0) {
            this.plantada = false;
        }
    }
    
    public void usar(int tickActual) {
        this.disponibleParaPlantar = false;
        this.tiempoUltimoPlantado = tickActual;
    }
    
    public void ejecutarComportamientoEspecifico(int tickActual, Juego juego) {
        BolaEscarcha nuevoDisparoHielo = disparar(tickActual);
        if (nuevoDisparoHielo != null) {
            juego.agregarDisparoHielo(nuevoDisparoHielo);
        }
    }
    
    public BolaEscarcha disparar(int tickActual) {
        if (puedeDisparar && plantada) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaEscarcha(x + 40, y, e);
        }
        return null;
    }
}