package juego;

import entorno.Entorno;

public class RoseBlade extends planta {
    private int tiempoRecargaDisparo;
    private int tiempoUltimoDisparo;
    private boolean puedeDisparar;
    
    public RoseBlade(double x, double y, Entorno e) {
        super(x, y, e, "roseblade.png", "roseblade.png", 0.10);
        
        this.setResistencia(5);
        this.setResistenciaMaxima(5);
        
        this.tiempoRecargaPlantado = 360;
        this.tiempoRecargaDisparo = 80;
        this.tiempoUltimoDisparo = -100;
        this.puedeDisparar = true;
    }
    
    @Override
    public void actualizar(int tickActual) {
        if (!puedeDisparar && tickActual - tiempoUltimoDisparo > tiempoRecargaDisparo) {
            puedeDisparar = true;
        }
        
        if (isBajoAtaque() && tickActual >= getTiempoFinAtaque()) {
            setBajoAtaque(false);
        }
    }
    
    @Override
    public void usar(int tickActual) {
        this.disponibleParaPlantar = false;
        this.tiempoUltimoPlantado = tickActual;
    }
    
    @Override
    public void ejecutarComportamientoEspecifico(int tickActual, Juego juego) {
        BolaFuego nuevoDisparo = disparar(tickActual);
        if (nuevoDisparo != null) {
            juego.agregarDisparo(nuevoDisparo);
        }
    }
    
    public BolaFuego disparar(int tickActual) {
        if (puedeDisparar && isPlantada()) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaFuego(getX() + 40, getY(), getEntorno());
        }
        return null;
    }
    
    // Getters específicos
    public boolean isPuedeDisparar() { return puedeDisparar; }
    public int getTiempoRecargaDisparo() { return tiempoRecargaDisparo; }
}