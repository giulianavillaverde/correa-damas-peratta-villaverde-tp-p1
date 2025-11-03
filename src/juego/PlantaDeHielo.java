package juego;

import entorno.Entorno;

public class PlantaDeHielo extends planta {
    private int tiempoRecargaDisparo;
    private int tiempoUltimoDisparo;
    private boolean puedeDisparar;
    
    public PlantaDeHielo(double x, double y, Entorno e) {
        super(x, y, e, "plantaHielo.png", "plantaHielo.png", 0.12);
        
        this.setResistencia(5);
        this.setResistenciaMaxima(5);
        
        this.tiempoRecargaPlantado = 1080;
        this.tiempoRecargaDisparo = 45;
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
        BolaEscarcha nuevoDisparoHielo = disparar(tickActual);
        if (nuevoDisparoHielo != null) {
            juego.agregarDisparoHielo(nuevoDisparoHielo);
        }
    }
    
    public BolaEscarcha disparar(int tickActual) {
        if (puedeDisparar && isPlantada()) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaEscarcha(getX() + 40, getY(), getEntorno());
        }
        return null;
    }
    
    // Getters específicos
    public boolean isPuedeDisparar() { return puedeDisparar; }
    public int getTiempoRecargaDisparo() { return tiempoRecargaDisparo; }
    public int getTiempoUltimoPlantado() { return tiempoUltimoPlantado; }
}