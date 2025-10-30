package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;


public class Juego extends InterfaceJuego
{
   Entorno entorno;
   Banner banner;
   cuadricula cuadricula;
   regalos[] regalo;
   planta[] plantas;
   Zombie[] zombies;
   ZombieColosal zombieColosal;
   BolaFuego[] disparos;
   BolaEscarcha[] disparosHielo;
   int contadorPlantas;
   int zombiesEliminados;
   int zombiesTotales;
   boolean juegoGanado;
   boolean juegoPerdido;
   int ticksParaProximoColosal;
   int colosalesAparecidos;
  
   // Plantas en el banner para controlar recarga
   planta wallnutBanner;
   planta hieloBanner;
   planta roseBanner;
   planta cerezaBanner;
  
   public Juego(){
       this.entorno = new Entorno(this, "Zombies Grinch", 1034, 585);
       this.cuadricula = new cuadricula(50, 150, entorno);
       this.banner = new Banner(entorno);
      
       this.regalo = new regalos[5];
       for (int i = 0; i < regalo.length; i++) {
           double x = 50;
           double y = 140 + (i * 100);
           this.regalo[i] = new regalos(x, y, entorno);
       }
      
       this.plantas = new planta[30];
       this.contadorPlantas = 0;
      
       this.zombies = new Zombie[15];
       this.zombieColosal = null;
       this.disparos = new BolaFuego[50];
       this.disparosHielo = new BolaEscarcha[50];
       this.zombiesEliminados = 0;
       this.zombiesTotales = 50;
       this.juegoGanado = false;
       this.juegoPerdido = false;
       this.ticksParaProximoColosal = 1080;
       this.colosalesAparecidos = 0;
      
       // Crear plantas del banner
       crearPlantasBanner();
      
       this.entorno.iniciar();
   }
  
   // Método para crear plantas del banner
   private void crearPlantasBanner() {
       this.wallnutBanner = new WallNut(50, 40, entorno);
       this.hieloBanner = new PlantaDeHielo(150, 40, entorno);
       this.roseBanner = new RoseBlade(250, 40, entorno);
       this.cerezaBanner = new CerezaExplosiva(350, 40, entorno);
      
       // Agregar plantas al array
       plantas[contadorPlantas++] = wallnutBanner;
       plantas[contadorPlantas++] = hieloBanner;
       plantas[contadorPlantas++] = roseBanner;
       plantas[contadorPlantas++] = cerezaBanner;
   }
   public void tick(){
       if (juegoGanado || juegoPerdido) {
           dibujarPantallaFin();
           return;
       }
      
       entorno.colorFondo(Color.GREEN);
       this.banner.dibujar();
       this.cuadricula.dibujar();
      
       for (regalos r: this.regalo) {
           r.dibujar();
       }
      
       // ACTUALIZAR Y DIBUJAR PLANTAS (solo las plantadas en la cuadrícula)
       for(planta p: this.plantas) {
           if(p != null && p.plantada) {
               p.dibujar();
              
               if (p instanceof RoseBlade) {
                   RoseBlade rose = (RoseBlade) p;
                   rose.actualizar(entorno.numeroDeTick());
                   BolaFuego nuevoDisparo = rose.disparar(entorno.numeroDeTick());
                   if (nuevoDisparo != null) {
                       agregarDisparo(nuevoDisparo);
                   }
               }
               else if (p instanceof PlantaDeHielo) {
                   PlantaDeHielo plantaHielo = (PlantaDeHielo) p;
                   plantaHielo.actualizar(entorno.numeroDeTick());
                   BolaEscarcha nuevoDisparoHielo = plantaHielo.disparar(entorno.numeroDeTick());
                   if (nuevoDisparoHielo != null) {
                       agregarDisparoHielo(nuevoDisparoHielo);
                   }
               }
               else if (p instanceof CerezaExplosiva) {
                   CerezaExplosiva cereza = (CerezaExplosiva) p;
                   cereza.actualizar(entorno.numeroDeTick());
                   verificarExplosionCereza(cereza);
               }
           }
       }
      
       // DIBUJAR PLANTAS DEL BANNER SOLO SI NO ESTÁN EN RECARGA
       dibujarPlantasBanner();
      
       generarZombie();
       generarZombieColosal();
       actualizarZombies();
       actualizarZombieColosal();
       actualizarDisparos();
       actualizarDisparosHielo();
       verificarColisiones();
       verificarColisionesHielo();
       verificarColisionesConColosal();
       verificarAtaquesZombies();
       verificarAtaquesZombieColosal();
       manejarSeleccionYPlantado();
       manejarMovimientoTeclado();
       verificarFinJuego();
       dibujarUI();
       dibujarBarrasRecarga();
   }
  
// MÉTODO: Verificar explosión de cereza
   private void verificarExplosionCereza(CerezaExplosiva cereza) {
       // Primero verificar si hay zombies cerca
       if (cereza.hayZombieCerca(zombies)) {
           System.out.println("¡Cereza explotando! Eliminando zombies en radio...");
          
           // Eliminar zombies en el radio de explosión
           int zombiesEliminadosPorCereza = 0;
           for (int i = 0; i < zombies.length; i++) {
               if (zombies[i] != null && zombies[i].vivo) {
                   double distancia = Math.sqrt(Math.pow(zombies[i].x - cereza.x, 2) +
                                              Math.pow(zombies[i].y - cereza.y, 2));
                   if (distancia < cereza.getRadioExplosion()) {
                       zombies[i].vivo = false;
                       zombiesEliminados++;
                       zombiesEliminadosPorCereza++;
                       System.out.println("Zombie eliminado por explosión de cereza");
                   }
               }
           }
          
           System.out.println("Cereza eliminó " + zombiesEliminadosPorCereza + " zombies");
          
           // Eliminar la cereza
           cereza.plantada = false;
           int indiceX = cuadricula.cercanoL(cereza.x, cereza.y).x;
           int indiceY = cuadricula.cercanoL(cereza.x, cereza.y).y;
           cuadricula.ocupado[indiceX][indiceY] = false;
          
           // Eliminar del array de plantas
           for (int i = 0; i < plantas.length; i++) {
               if (plantas[i] == cereza) {
                   plantas[i] = null;
                   break;
               }
           }
          
           // Dibujar efecto visual de explosión (opcional)
           dibujarExplosion(cereza.x, cereza.y, cereza.getRadioExplosion());
       }
   }
   // Método opcional para dibujar efecto visual de explosión
   private void dibujarExplosion(double x, double y, double radio) {
       // Dibujar círculo de explosión (rojo semitransparente)
       entorno.dibujarCirculo(x, y, radio, new java.awt.Color(255, 0, 0, 100));
      
       // También puedes agregar más efectos visuales aquí
       entorno.dibujarCirculo(x, y, radio * 0.7, new java.awt.Color(255, 165, 0, 80));
       entorno.dibujarCirculo(x, y, radio * 0.4, new java.awt.Color(255, 255, 0, 60));
   }
  
   // MÉTODO: Generar zombie colosal cada 3 minutos
   private void generarZombieColosal() {
       if (zombieColosal == null && ticksParaProximoColosal > 0) {
           ticksParaProximoColosal--;
          
           if (ticksParaProximoColosal <= 0) {
               zombieColosal = new ZombieColosal(entorno);
               colosalesAparecidos++;
           }
       }
      
       if (zombieColosal == null && ticksParaProximoColosal <= 0) {
           ticksParaProximoColosal = 1080;
       }
   }
  
   // MÉTODO: Actualizar zombie colosal
   private void actualizarZombieColosal() {
       if (zombieColosal != null) {
           zombieColosal.mover();
           zombieColosal.dibujar();
          
           if (zombieColosal.llegoARegalos()) {
               juegoPerdido = true;
           }
          
           if (!zombieColosal.vivo) {
               zombieColosal = null;
               zombiesEliminados += 5;
           }
       }
   }
  
   // MÉTODO: Verificar colisiones con el zombie colosal
   private void verificarColisionesConColosal() {
       if (zombieColosal == null || !zombieColosal.vivo) return;
      
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null && disparos[i].activa) {
               double distancia = Math.sqrt(Math.pow(disparos[i].x - zombieColosal.getX(), 2) +
                                          Math.pow(disparos[i].y - zombieColosal.getY(), 2));
               if (distancia < 70) {
                   zombieColosal.recibirDanio();
                   disparos[i].activa = false;
                   disparos[i] = null;
                   break;
               }
           }
       }
      
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null && disparosHielo[i].activa) {
               double distancia = Math.sqrt(Math.pow(disparosHielo[i].x - zombieColosal.getX(), 2) +
                                          Math.pow(disparosHielo[i].y - zombieColosal.getY(), 2));
               if (distancia < 70) {
                   zombieColosal.ralentizar(disparosHielo[i].duracionRalentizacion);
                   disparosHielo[i].activa = false;
                   disparosHielo[i] = null;
                   break;
               }
           }
       }
   }
  
   // MÉTODO: Verificar ataques del zombie colosal
   private void verificarAtaquesZombieColosal() {
       if (zombieColosal == null || !zombieColosal.vivo) return;
      
       int tickActual = entorno.numeroDeTick();
      
       for (int j = 0; j < plantas.length; j++) {
           if (plantas[j] != null && plantas[j].plantada &&
               zombieColosal.colisionaConPlanta(plantas[j])) {
              
               if (zombieColosal.puedeAtacar(tickActual)) {
                   if (plantas[j] instanceof WallNut) {
                       WallNut wallnut = (WallNut) plantas[j];
                       wallnut.recibirAtaque();
                       wallnut.recibirAtaque();
                   } else {
                       int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                       int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                       cuadricula.ocupado[indiceX][indiceY] = false;
                       plantas[j] = null;
                   }
                   zombieColosal.registrarAtaque(tickActual);
               }
               break;
           }
       }
   }
  
   // MÉTODO: Dibujar plantas del banner solo si no están en recarga
   private void dibujarPlantasBanner() {
       int tickActual = entorno.numeroDeTick();
      
       if (wallnutBanner instanceof WallNut) {
           WallNut wallnut = (WallNut) wallnutBanner;
           if (!wallnut.estaEnRecarga(tickActual)) {
               wallnutBanner.dibujar();
           }
       }
      
       if (hieloBanner instanceof PlantaDeHielo) {
           PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
           if (!hielo.estaEnRecarga(tickActual)) {
               hieloBanner.dibujar();
           }
       }
      
       if (roseBanner instanceof RoseBlade) {
           RoseBlade rose = (RoseBlade) roseBanner;
           if (!rose.estaEnRecarga(tickActual)) {
               roseBanner.dibujar();
           }
       }
      
       if (cerezaBanner instanceof CerezaExplosiva) {
           CerezaExplosiva cereza = (CerezaExplosiva) cerezaBanner;
           if (!cereza.estaEnRecarga(tickActual)) {
               cerezaBanner.dibujar();
           }
       }
   }
   
   // Método para manejar movimiento con teclado
  private void manejarMovimientoTeclado() {
       planta plantaSeleccionada = null;
       for (planta p : plantas) {
           if (p != null && p.seleccionada) {
               plantaSeleccionada = p;
               break;
           }
       }
      
       if (plantaSeleccionada == null) {
           return;
       }
      
       double velocidad = 105;
     if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
           plantaSeleccionada.y -= velocidad;
       }
       if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
           plantaSeleccionada.y += velocidad;
       }
      if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
           plantaSeleccionada.x -= velocidad;
       }
      if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
           plantaSeleccionada.x += velocidad;
       }
      
      plantaSeleccionada.x = Math.max(30, Math.min(plantaSeleccionada.x, 1000));
      plantaSeleccionada.y = Math.max(30, Math.min(plantaSeleccionada.y, 550));
  }
 
   
//MÉTODO ACTUALIZADO: Verificar cuando los zombies atacan plantas (BLOQUEO CON TODAS LAS PLANTAS)
private void verificarAtaquesZombies() {
   int tickActual = entorno.numeroDeTick();
   
   for (int i = 0; i < zombies.length; i++) {
       if (zombies[i] != null && zombies[i].vivo) {
           
           zombies[i].verificarPlantaBloqueadora();
           
           if (zombies[i].estaBloqueado()) {
               planta plantaBloqueadora = zombies[i].plantaBloqueadora;
               if (plantaBloqueadora != null && plantaBloqueadora.plantada && 
                   zombies[i].puedeAtacar(tickActual)) {
                   
                   // TODAS las plantas reciben daño
                   plantaBloqueadora.recibirAtaque();
                   
                   // Si la planta es destruida, liberar al zombie
                   if (!plantaBloqueadora.plantada) {
                       int indiceX = cuadricula.cercanoL(plantaBloqueadora.x, plantaBloqueadora.y).x;
                       int indiceY = cuadricula.cercanoL(plantaBloqueadora.x, plantaBloqueadora.y).y;
                       cuadricula.ocupado[indiceX][indiceY] = false;
                       
                       // Buscar y eliminar la planta del array
                       for (int j = 0; j < plantas.length; j++) {
                           if (plantas[j] == plantaBloqueadora) {
                               plantas[j] = null;
                               break;
                           }
                       }
                       zombies[i].liberar();
                   }
                   zombies[i].registrarAtaque(tickActual);
               }
           } 
           else {
               for (int j = 0; j < plantas.length; j++) {
                   if (plantas[j] != null && plantas[j].plantada && 
                       zombies[i].colisionaConPlanta(plantas[j])) {
                       
                       // BLOQUEAR CON CUALQUIER PLANTA
                       zombies[i].bloquear(plantas[j]);
                       
                       // Si la planta es CerezaExplosiva, hacerla explotar inmediatamente
                       if (plantas[j] instanceof CerezaExplosiva) {
                           CerezaExplosiva cereza = (CerezaExplosiva) plantas[j];
                           if (!cereza.debeExplotar()) {
                               if (cereza.hayZombieCerca(zombies)) {
                                   verificarExplosionCereza(cereza);
                               }
                           }
                       }
                       break;
                   }
               }
           }
       }
   }
}
   // BARRAS DE RECARGA
   private void dibujarBarrasRecarga() {
       int tickActual = entorno.numeroDeTick();
      
       // WallNut - MARRÓN
       if (wallnutBanner instanceof WallNut) {
           WallNut wallnut = (WallNut) wallnutBanner;
           double porcentaje = wallnut.porcentajeRecarga(tickActual);
           Color marronFondo = new Color(139, 69, 19);
           Color marronBorde = new Color(160, 82, 45);
           dibujarBarraRecargaCompleta(50, 75, porcentaje, marronFondo, marronBorde, "WallNut", wallnut, tickActual);
       }
      
       // Escarchi - AZUL CELESTE
       if (hieloBanner instanceof PlantaDeHielo) {
           PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
           double porcentaje = hielo.porcentajeRecarga(tickActual);
           Color celesteFondo = new Color(135, 206, 250);
           Color celesteBorde = new Color(173, 216, 230);
           dibujarBarraRecargaCompleta(150, 75, porcentaje, celesteFondo, celesteBorde, "Escarchi", hielo, tickActual);
       }
      
       // RoseBlade - ROJO
       if (roseBanner instanceof RoseBlade) {
           RoseBlade rose = (RoseBlade) roseBanner;
           double porcentaje = rose.porcentajeRecarga(tickActual);
           Color rojoFondo = new Color(255, 0, 0);
           Color rojoBorde = new Color(255, 69, 0);
           dibujarBarraRecargaCompleta(250, 75, porcentaje, rojoFondo, rojoBorde, "RoseBlade", rose, tickActual);
       }
      
       // Cereza Explosiva - VIOLETA
       if (cerezaBanner instanceof CerezaExplosiva) {
           CerezaExplosiva cereza = (CerezaExplosiva) cerezaBanner;
           double porcentaje = cereza.porcentajeRecarga(tickActual);
           Color violetaFondo = new Color(148, 0, 211);
           Color violetaBorde = new Color(186, 85, 211);
           dibujarBarraRecargaCompleta(350, 75, porcentaje, violetaFondo, violetaBorde, "Cereza", cereza, tickActual);
       }
   }
  
   private void dibujarBarraRecargaCompleta(double x, double y, double porcentaje, Color colorFondo, Color colorBorde, String texto, planta planta, int tickActual) {
       double anchoBarra = 80;
       double altoBarra = 12;
      
       entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, colorFondo);
      
       double anchoVacio = anchoBarra * (1 - porcentaje);
       if (anchoVacio > 0) {
           Color colorVacio = oscurecerColor(colorFondo, 0.5f);
           entorno.dibujarRectangulo(x + (anchoBarra - anchoVacio) / 2, y, anchoVacio, altoBarra, 0, colorVacio);
       }
      
       entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, colorBorde);
      
       entorno.cambiarFont("Arial", 12, Color.WHITE);
       entorno.escribirTexto(texto, x - 25, y - 5);
      
       if (porcentaje < 1.0) {
           int segundosRestantes = calcularSegundosRestantes(planta, tickActual);
          
           Color colorTiempo = Color.BLACK;
           if (esColorOscuro(colorFondo)) {
               colorTiempo = Color.WHITE;
           }
          
           entorno.cambiarFont("Arial", 10, colorTiempo);
          
           if (segundosRestantes <= 5) {
               entorno.cambiarFont("Arial", 10, Color.YELLOW);
           } else if (segundosRestantes <= 10) {
               entorno.cambiarFont("Arial", 10, Color.ORANGE);
           }
          
           entorno.escribirTexto(segundosRestantes + "s", x - 8, y + 20);
          
           if (segundosRestantes <= 3 && tickActual % 10 < 5) {
               entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, new Color(255, 255, 255, 100));
           }
       } else {
           Color colorListo = esColorOscuro(colorFondo) ? Color.GREEN : new Color(0, 100, 0);
           entorno.cambiarFont("Arial", 10, colorListo);
           entorno.escribirTexto("Listo", x - 12, y + 20);
       }
   }
  
   private Color oscurecerColor(Color color, float factor) {
       int rojo = Math.max(0, (int)(color.getRed() * factor));
       int verde = Math.max(0, (int)(color.getGreen() * factor));
       int azul = Math.max(0, (int)(color.getBlue() * factor));
       return new Color(rojo, verde, azul);
   }
  
   private boolean esColorOscuro(Color color) {
       double luminosidad = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
       return luminosidad < 0.5;
   }
  
   private int calcularSegundosRestantes(planta planta, int tickActual) {
       if (planta instanceof PlantaDeHielo) {
           PlantaDeHielo hielo = (PlantaDeHielo) planta;
           int tiempoTranscurrido = tickActual - hielo.tiempoUltimoPlantado;
           int tiempoRestante = Math.max(0, hielo.tiempoRecargaPlantado - tiempoTranscurrido);
           return (int) Math.ceil(tiempoRestante / 6.0);
       } else if (planta instanceof RoseBlade) {
           RoseBlade rose = (RoseBlade) planta;
           int tiempoTranscurrido = tickActual - rose.tiempoUltimoPlantado;
           int tiempoRestante = Math.max(0, rose.tiempoRecargaPlantado - tiempoTranscurrido);
           return (int) Math.ceil(tiempoRestante / 6.0);
       } else if (planta instanceof WallNut) {
           WallNut wallnut = (WallNut) planta;
           int tiempoTranscurrido = tickActual - wallnut.tiempoUltimoUso;
           int tiempoRestante = Math.max(0, wallnut.tiempoRecargaPlantado - tiempoTranscurrido);
           return (int) Math.ceil(tiempoRestante / 6.0);
       } else if (planta instanceof CerezaExplosiva) {
           CerezaExplosiva cereza = (CerezaExplosiva) planta;
           int tiempoTranscurrido = tickActual - cereza.tiempoUltimoPlantado;
           int tiempoRestante = Math.max(0, cereza.tiempoRecargaPlantado - tiempoTranscurrido);
           return (int) Math.ceil(tiempoRestante / 6.0);
       }
       return 0;
   }
  
   private void agregarDisparo(BolaFuego disparo) {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] == null) {
               disparos[i] = disparo;
               break;
           }
       }
   }
  
   private void agregarDisparoHielo(BolaEscarcha disparo) {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] == null) {
               disparosHielo[i] = disparo;
               break;
           }
       }
   }
  
   private void generarZombie() {
       if (Math.random() < 0.01 && zombiesEliminados < zombiesTotales) {
           for (int i = 0; i < zombies.length; i++) {
               if (zombies[i] == null) {
                   int fila = (int)(Math.random() * 5);
                   zombies[i] = new Zombie(fila, entorno);
                   break;
               }
           }
       }
   }
  
   private void actualizarZombies() {
       for (int i = 0; i < zombies.length; i++) {
           if (zombies[i] != null) {
               zombies[i].mover();
               zombies[i].dibujar();
              
               if (zombies[i].llegoARegalos()) {
                   juegoPerdido = true;
               }
              
               if (!zombies[i].vivo) {
                   zombies[i] = null;
                   zombiesEliminados++;
               }
           }
       }
   }
  
   private void actualizarDisparos() {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null) {
               disparos[i].mover();
               disparos[i].dibujar();
               if (!disparos[i].activa) {
                   disparos[i] = null;
               }
           }
       }
   }
  
   private void actualizarDisparosHielo() {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null) {
               disparosHielo[i].mover();
               disparosHielo[i].dibujar();
               if (!disparosHielo[i].activa) {
                   disparosHielo[i] = null;
               }
           }
       }
   }
  
   private void verificarColisiones() {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null && disparos[i].activa) {
               for (int j = 0; j < zombies.length; j++) {
                   if (zombies[j] != null && zombies[j].vivo &&
                       disparos[i].colisionaCon(zombies[j])) {
                       zombies[j].recibirDanio();
                       disparos[i].activa = false;
                       disparos[i] = null;
                       break;
                   }
               }
           }
       }
   }
  
   private void verificarColisionesHielo() {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null && disparosHielo[i].activa) {
               for (int j = 0; j < zombies.length; j++) {
                   if (zombies[j] != null && zombies[j].vivo &&
                       disparosHielo[i].colisionaCon(zombies[j])) {
                       zombies[j].ralentizar(disparosHielo[i].duracionRalentizacion);
                       disparosHielo[i].activa = false;
                       disparosHielo[i] = null;
                       break;
                   }
               }
           }
       }
   }
  
   private void manejarSeleccionYPlantado() {
       if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
           for (int i = 0; i < plantas.length; i++) {
               if (plantas[i] != null) {
                   plantas[i].seleccionada = false;
               }
           }
          
           int tickActual = entorno.numeroDeTick();
          
           if (wallnutBanner != null && wallnutBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               WallNut wallnut = (WallNut) wallnutBanner;
               if (!wallnut.estaEnRecarga(tickActual)) {
                   wallnutBanner.seleccionada = true;
               }
           }
           else if (hieloBanner != null && hieloBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
               if (!hielo.estaEnRecarga(tickActual)) {
                   hieloBanner.seleccionada = true;
               }
           }
           else if (roseBanner != null && roseBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               RoseBlade rose = (RoseBlade) roseBanner;
               if (!rose.estaEnRecarga(tickActual)) {
                   roseBanner.seleccionada = true;
               }
           }
           else if (cerezaBanner != null && cerezaBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               CerezaExplosiva cereza = (CerezaExplosiva) cerezaBanner;
               if (!cereza.estaEnRecarga(tickActual)) {
                   cerezaBanner.seleccionada = true;
               }
           }
           else {
               for (int i = 0; i < plantas.length; i++) {
                   if (plantas[i] != null && plantas[i].plantada && plantas[i].encima(entorno.mouseX(), entorno.mouseY())) {
                       plantas[i].seleccionada = true;
                       break;
                   }
               }
           }
       }
      
       if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
           for (int i = 0; i < plantas.length; i++) {
               if (plantas[i] != null && plantas[i].seleccionada) {
                   plantas[i].arrastrar(entorno.mouseX(), entorno.mouseY());
               }
           }
       }
      
       if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
           // WallNut
           if (wallnutBanner != null && wallnutBanner.seleccionada) {
               WallNut wallnut = (WallNut) wallnutBanner;
              
               if (entorno.mouseY() < 70) {
                   wallnutBanner.x = wallnutBanner.xInicial;
                   wallnutBanner.y = wallnutBanner.yInicial;
               } else {
                   int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                   int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                  
                   if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                       WallNut nuevaWallnut = new WallNut(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
                       nuevaWallnut.plantada = true;
                      
                       for (int i = 0; i < plantas.length; i++) {
                           if (plantas[i] == null) {
                               plantas[i] = nuevaWallnut;
                               break;
                           }
                       }
                      
                       cuadricula.ocupado[indiceX][indiceY] = true;
                       wallnut.usar(entorno.numeroDeTick());
                   }
                   wallnutBanner.x = wallnutBanner.xInicial;
                   wallnutBanner.y = wallnutBanner.yInicial;
               }
              wallnutBanner.seleccionada = false;
           }
          
           // PlantaDeHielo
           else if (hieloBanner != null && hieloBanner.seleccionada) {
               PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
              
               if (entorno.mouseY() < 70) {
                   hieloBanner.x = hieloBanner.xInicial;
                   hieloBanner.y = hieloBanner.yInicial;
               } else {
                   int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                   int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                  
                   if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                       PlantaDeHielo nuevaHielo = new PlantaDeHielo(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
                       nuevaHielo.plantada = true;
                      
                       for (int i = 0; i < plantas.length; i++) {
                           if (plantas[i] == null) {
                               plantas[i] = nuevaHielo;
                               break;
                           }
                       }
                      
                       cuadricula.ocupado[indiceX][indiceY] = true;
                       hielo.usar(entorno.numeroDeTick());
                   }
                   hieloBanner.x = hieloBanner.xInicial;
                   hieloBanner.y = hieloBanner.yInicial;
               }
               hieloBanner.seleccionada = false;
           }
          
           // RoseBlade
           else if (roseBanner != null && roseBanner.seleccionada) {
               RoseBlade rose = (RoseBlade) roseBanner;
              
               if (entorno.mouseY() < 70) {
                   roseBanner.x = roseBanner.xInicial;
                   roseBanner.y = roseBanner.yInicial;
               } else {
                   int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                   int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                  
                   if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                       RoseBlade nuevaRose = new RoseBlade(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
                       nuevaRose.plantada = true;
                      
                       for (int i = 0; i < plantas.length; i++) {
                           if (plantas[i] == null) {
                               plantas[i] = nuevaRose;
                               break;
                           }
                       }
                      
                       cuadricula.ocupado[indiceX][indiceY] = true;
                       rose.usar(entorno.numeroDeTick());
                   }
                   roseBanner.x = roseBanner.xInicial;
                   roseBanner.y = roseBanner.yInicial;
               }
              roseBanner.seleccionada = false;
           }
          
           // Cereza Explosiva
           else if (cerezaBanner != null && cerezaBanner.seleccionada) {
               CerezaExplosiva cereza = (CerezaExplosiva) cerezaBanner;
              
               if (entorno.mouseY() < 70) {
                   cerezaBanner.x = cerezaBanner.xInicial;
                   cerezaBanner.y = cerezaBanner.yInicial;
               } else {
                   int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                   int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                  
                   if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                       CerezaExplosiva nuevaCereza = new CerezaExplosiva(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
                       nuevaCereza.plantada = true;
                      
                       for (int i = 0; i < plantas.length; i++) {
                           if (plantas[i] == null) {
                               plantas[i] = nuevaCereza;
                               break;
                           }
                       }
                      
                       cuadricula.ocupado[indiceX][indiceY] = true;
                       cereza.usar(entorno.numeroDeTick());
                   }
                   cerezaBanner.x = cerezaBanner.xInicial;
                   cerezaBanner.y = cerezaBanner.yInicial;
               }
               cerezaBanner.seleccionada = false;
           }
          
           // Plantas ya plantadas
           for (int i = 0; i < plantas.length; i++) {
               if (plantas[i] != null && plantas[i].plantada && plantas[i].seleccionada) {
                   int indiceX = cuadricula.cercanoL(plantas[i].x, plantas[i].y).x;
                   int indiceY = cuadricula.cercanoL(plantas[i].x, plantas[i].y).y;
                  
                   int indiceXAnterior = cuadricula.cercanoL(plantas[i].xInicial, plantas[i].yInicial).x;
                   int indiceYAnterior = cuadricula.cercanoL(plantas[i].xInicial, plantas[i].yInicial).y;
                   cuadricula.ocupado[indiceXAnterior][indiceYAnterior] = false;
                  
                   if (indiceX > 0 && !cuadricula.ocupado[indiceX][indiceY]) {
                       cuadricula.centrarPlanta(plantas[i], indiceX, indiceY);
                       cuadricula.ocupado[indiceX][indiceY] = true;
                       plantas[i].xInicial = plantas[i].x;
                       plantas[i].yInicial = plantas[i].y;
                   } else {
                       plantas[i].x = plantas[i].xInicial;
                       plantas[i].y = plantas[i].yInicial;
                       cuadricula.ocupado[indiceXAnterior][indiceYAnterior] = true;
                   }
                  
                  // plantas[i].seleccionada = false;
               }
           }
       }
   }
  
   private void dibujarUI() {
       entorno.cambiarFont("Arial", 20, Color.WHITE);
       entorno.escribirTexto("Zombies: " + zombiesEliminados + "/" + zombiesTotales, 400, 40);
       entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + "s", 400, 80);
      
       int plantasActivas = 0;
       for (planta p : plantas) {
           if (p != null && p.plantada) {
               plantasActivas++;
           }
       }
       entorno.escribirTexto("Plantas: " + plantasActivas, 400, 20);
      
       int zombiesEnPantalla = 0;
       for (Zombie z : zombies) {
           if (z != null && z.vivo) {
               zombiesEnPantalla++;
           }
       }
       entorno.escribirTexto("Zombies en pantalla: " + zombiesEnPantalla, 400, 60);
      
       if (zombieColosal != null && zombieColosal.vivo) {
           entorno.cambiarFont("Arial", 50, Color.RED);
           entorno.escribirTexto("¡ZOMBIE COLOSAL!", 212, 300);
       } else if (ticksParaProximoColosal > 0) {
           int segundosRestantes = ticksParaProximoColosal / 6;
           entorno.cambiarFont("Arial", 20, Color.red);
           entorno.escribirTexto("Próximo colosal en: " + segundosRestantes + "s", 600, 84);
       } else {
           entorno.cambiarFont("Arial", 20, Color.orange);
           entorno.escribirTexto("Colosales: " + colosalesAparecidos, 600, 84);
       }
   }
  
   private void verificarFinJuego() {
       if (zombiesEliminados >= zombiesTotales) {
           juegoGanado = true;
       }
   }
  
   private void dibujarPantallaFin() {
       entorno.colorFondo(Color.BLACK);
       if (juegoGanado) {
           entorno.cambiarFont("Arial", 40, Color.GREEN);
           entorno.escribirTexto("¡VICTORIA!", 400, 300);
           entorno.cambiarFont("Arial", 24, Color.WHITE);
           entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
           entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + " segundos", 400, 380);
           entorno.escribirTexto("Colosales derrotados: " + colosalesAparecidos, 400, 410);
       } else if (juegoPerdido) {
           entorno.cambiarFont("Arial", 40, Color.RED);
           entorno.escribirTexto("¡DERROTA!", 400, 300);
           entorno.cambiarFont("Arial", 24, Color.WHITE);
           entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
           entorno.escribirTexto("Colosales derrotados: " + colosalesAparecidos, 400, 380);
       }
   }
   public static void main(String[] args) {
       new Juego();
   }
}