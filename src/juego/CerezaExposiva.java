package juego;
import entorno.Entorno;


public class CerezaExplosiva extends planta {
   public int tiempoRecargaPlantado;
   public int tiempoUltimoPlantado;
   private boolean disponibleParaPlantar;
   private double radioExplosion;
   private boolean explotando;
  
   public CerezaExplosiva(double x, double y, Entorno e) {
       super(x, y, e, "CerezaExplosiva.png", "CerezaExplosiva.png", 0.11);
       this.tiempoRecargaPlantado = 720; // 2 minutos de recarga
       this.tiempoUltimoPlantado = -100;
       this.disponibleParaPlantar = true;
       this.radioExplosion = 200; // Radio de explosión
       this.explotando = false;
   }
  
   public void actualizar(int tickActual) {
       // Actualizar disponibilidad para plantar
       if (!disponibleParaPlantar && !plantada && tickActual - tiempoUltimoPlantado > tiempoRecargaPlantado) {
           disponibleParaPlantar = true;
       }
   }
  
   // Método para verificar si hay zombies cerca (será llamado desde Juego)
   public boolean hayZombieCerca(Zombie[] zombies) {
       if (!plantada || explotando) return false;
      
       for (Zombie zombie : zombies) {
           if (zombie != null && zombie.vivo) {
               // Usar las variables públicas x e y del zombie en lugar de getX() y getY()
               double distancia = Math.sqrt(Math.pow(zombie.x - x, 2) +
                                          Math.pow(zombie.y - y, 2));
               if (distancia < 50) { // AUMENTÉ la distancia de detección de 30 a 50
                   explotando = true;
                   return true;
               }
           }
       }
       return false;
   }
  
   public boolean debeExplotar() {
       return explotando;
   }
  
   public double getRadioExplosion() {
       return radioExplosion;
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
}
