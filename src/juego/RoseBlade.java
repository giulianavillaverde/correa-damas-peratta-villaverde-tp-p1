package juego;
import entorno.Entorno;

public class RoseBlade extends planta {
    // TIEMPOS DE RECARGA SEPARADOS
    public int tiempoRecargaPlantado;  // Para plantar desde el banner
    public int tiempoRecargaDisparo;   // Para disparar cuando está plantada
    
    public int tiempoUltimoPlantado;   // Cuándo se plantó desde el banner
    public int tiempoUltimoDisparo;    // Cuándo disparó por última vez
    
    private boolean puedeDisparar;
    private boolean disponibleParaPlantar;
    
    public RoseBlade(double x, double y, Entorno e) {
        super(x, y, e, "roseblade.png", "roseblade.png", 0.10);
        
        // TIEMPOS SEPARADOS
        this.tiempoRecargaPlantado = 360;  // 1 minuto para volver a plantar
        this.tiempoRecargaDisparo = 80;    // Tiempo entre disparos
        
        this.tiempoUltimoPlantado = -100;
        this.tiempoUltimoDisparo = -100;
        
        this.puedeDisparar = true;
        this.disponibleParaPlantar = true;
    }
    
    public void actualizar(int tickActual) {
        // Actualizar recarga de disparo
        if (!puedeDisparar && tickActual - tiempoUltimoDisparo > tiempoRecargaDisparo) {
            puedeDisparar = true;
        }
        
        // Actualizar disponibilidad para plantar (solo si está en el banner)
        if (!disponibleParaPlantar && !plantada && tickActual - tiempoUltimoPlantado > tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
        }
    }
    
    public BolaFuego disparar(int tickActual) {
        if (puedeDisparar && plantada) {
            puedeDisparar = false;
            tiempoUltimoDisparo = tickActual;
            return new BolaFuego(x + 40, y, e);
        }
        return null;
    }
    
    // Método para cuando se planta desde el banner
    public void usar(int tickActual) {
        disponibleParaPlantar = false;
        tiempoUltimoPlantado = tickActual;
    }
    
    // PARA EL BANNER: Verificar si está disponible para plantar
    public boolean estaEnRecarga(int tickActual) {
        if (disponibleParaPlantar) return false;
        
        if (tickActual - tiempoUltimoPlantado >= tiempoRecargaPlantado) {
            disponibleParaPlantar = true;
            return false;
        }
        return true;
    }
    
    // PARA EL BANNER: Porcentaje de recarga de plantado
    public double porcentajeRecarga(int tickActual) {
        if (disponibleParaPlantar) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoPlantado;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaPlantado);
    }
    
    // PARA DISPAROS: Porcentaje de recarga de disparo (esto NO se muestra en el banner)
    public double porcentajeRecargaDisparo(int tickActual) {
        if (puedeDisparar) return 1.0;
        int tiempoTranscurrido = tickActual - tiempoUltimoDisparo;
        return Math.min(1.0, (double) tiempoTranscurrido / tiempoRecargaDisparo);
    }
}