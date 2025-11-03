package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

   private Entorno entorno;
   private Banner banner;
   private cuadricula cuadricula;
   private regalos[] regalo;
   private planta[] plantas;
   private Zombie[] zombies;
   private ZombieRapido zombieRapido;
   private ZombieColosal zombieColosal;
   private BolaFuego[] disparos;
   private BolaEscarcha[] disparosHielo;
   private BolaNieve[] bolasNieve;
   private int contadorPlantas;
   private int zombiesEliminados;
   private int zombiesTotales;
   private boolean juegoGanado;
   private boolean juegoPerdido;
   private int ticksParaProximoRapido;
   private int ticksParaProximoColosal;
   private int rapidosAparecidos;
   private int colosalesAparecidos;
  
   // Plantas en el banner
   private planta wallnutBanner;
   private planta hieloBanner;
   private planta roseBanner;
   private planta cerezaBanner;
   
   // Zombies en el banner
   private Zombie zombieNormalBanner;
   private ZombieRapido zombieRapidoBanner;
   private ZombieColosal zombieColosalBanner;
   
   // Contadores para optimización
   private int cantidadPlantasActivas;
   private int cantidadDisparosActivos;
   private int cantidadDisparosHieloActivos;
   private int cantidadBolasNieveActivas;
   private int cantidadZombiesActivos;
  
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
       this.cantidadPlantasActivas = 0;
      
       this.zombies = new Zombie[15];
       this.zombieRapido = null;
       this.zombieColosal = null;
       this.disparos = new BolaFuego[50];
       this.disparosHielo = new BolaEscarcha[50];
       this.bolasNieve = new BolaNieve[30];
       this.zombiesEliminados = 0;
       this.zombiesTotales = 80;
       this.juegoGanado = false;
       this.juegoPerdido = false;
       this.ticksParaProximoRapido = 270;
       this.ticksParaProximoColosal = 1080;
       this.rapidosAparecidos = 0;
       this.colosalesAparecidos = 0;
      
       this.cantidadDisparosActivos = 0;
       this.cantidadDisparosHieloActivos = 0;
       this.cantidadBolasNieveActivas = 0;
       this.cantidadZombiesActivos = 0;
      
       crearPlantasBanner();
       crearZombiesBanner();
       this.entorno.iniciar();
   }
  
   private void crearPlantasBanner() {
       this.wallnutBanner = new WallNut(50, 40, entorno);
       this.hieloBanner = new PlantaDeHielo(150, 40, entorno);
       this.roseBanner = new RoseBlade(250, 40, entorno);
       this.cerezaBanner = new CerezaExplosiva(350, 40, entorno);
      
       agregarPlanta(wallnutBanner);
       agregarPlanta(hieloBanner);
       agregarPlanta(roseBanner);
       agregarPlanta(cerezaBanner);
   }
   
   private void crearZombiesBanner() {
       this.zombieNormalBanner = new Zombie(0, entorno);
       this.zombieRapidoBanner = new ZombieRapido(0, entorno);
       this.zombieColosalBanner = new ZombieColosal(entorno);
       
       this.zombieNormalBanner.setX(800);
       this.zombieNormalBanner.setY(20);
       this.zombieNormalBanner.setVivo(true);
       
       this.zombieRapidoBanner.setX(900);
       this.zombieRapidoBanner.setY(20);
       this.zombieRapidoBanner.setVivo(true);
       
       this.zombieColosalBanner.setX(1000);
       this.zombieColosalBanner.setY(20);
       this.zombieColosalBanner.setVivo(true);
   }
   
   private boolean agregarPlanta(planta nuevaPlanta) {
       for (int i = 0; i < plantas.length; i++) {
           if (plantas[i] == null) {
               plantas[i] = nuevaPlanta;
               cantidadPlantasActivas++;
               return true;
           }
       }
       return false;
   }
   
   private void eliminarPlanta(int indice) {
       if (plantas[indice] != null) {
           plantas[indice] = null;
           cantidadPlantasActivas--;
       }
   }
   
   private void compactarArrayPlantas() {
       int writeIndex = 0;
       for (int readIndex = 0; readIndex < plantas.length; readIndex++) {
           if (plantas[readIndex] != null) {
               if (writeIndex != readIndex) {
                   plantas[writeIndex] = plantas[readIndex];
                   plantas[readIndex] = null;
               }
               writeIndex++;
           }
       }
       cantidadPlantasActivas = writeIndex;
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
      
       actualizarPlantas();
       dibujarPlantasBanner();
       dibujarZombiesBanner();
      
       generarZombie();
       generarZombieRapido();
       generarZombieColosal();
       actualizarZombies();
       actualizarZombieRapido();
       actualizarZombieColosal();
       actualizarDisparos();
       actualizarDisparosHielo();
       actualizarBolasNieve();
       verificarColisiones();
       verificarColisionesHielo();
       verificarColisionesConRapido();
       verificarColisionesConColosal();
       verificarColisionesBolasNieve();
       verificarAtaquesZombies();
       verificarAtaquesZombieRapido();
       verificarAtaquesZombieColosal();
       generarDisparosZombies();
       manejarSeleccionYPlantado();
       manejarMovimientoTeclado();
       verificarFinJuego();
       dibujarUI();
       dibujarBarrasRecarga();
   }

   private void actualizarPlantas() {
       int tickActual = entorno.numeroDeTick();
       
       for(int i = 0; i < plantas.length; i++) {
           planta p = plantas[i];
           if(p != null && p.isPlantada()) {
               p.dibujar();
              
               if (p instanceof RoseBlade) {
                   RoseBlade rose = (RoseBlade) p;
                   rose.actualizar(tickActual);
                   BolaFuego nuevoDisparo = rose.disparar(tickActual);
                   if (nuevoDisparo != null) {
                       agregarDisparo(nuevoDisparo);
                   }
               }
               else if (p instanceof PlantaDeHielo) {
                   PlantaDeHielo plantaHielo = (PlantaDeHielo) p;
                   plantaHielo.actualizar(tickActual);
                   BolaEscarcha nuevoDisparoHielo = plantaHielo.disparar(tickActual);
                   if (nuevoDisparoHielo != null) {
                       agregarDisparoHielo(nuevoDisparoHielo);
                   }
               }
               else if (p instanceof CerezaExplosiva) {
                   CerezaExplosiva cereza = (CerezaExplosiva) p;
                   cereza.actualizar(tickActual);
                   verificarExplosionCereza(cereza, tickActual);
               }
           }
       }
   }

   private void dibujarZombiesBanner() {
	    if (zombieNormalBanner != null && zombieNormalBanner.getImagen() != null) {
	        zombieNormalBanner.setX(770);
	        zombieNormalBanner.setY(40);
	        entorno.dibujarImagen(zombieNormalBanner.getImagen(), 
	                            zombieNormalBanner.getX(), zombieNormalBanner.getY(), 0, 0.1);
	        entorno.cambiarFont("Arial", 12, Color.WHITE);
	        entorno.escribirTexto("Grinch", zombieNormalBanner.getX() - 15, zombieNormalBanner.getY() + 45);
	    }
	    
	    if (zombieRapidoBanner != null && zombieRapidoBanner.getImagen() != null) {
	        zombieRapidoBanner.setX(870);
	        zombieRapidoBanner.setY(40);
	        entorno.dibujarImagen(zombieRapidoBanner.getImagen(), 
	                            zombieRapidoBanner.getX(), zombieRapidoBanner.getY(), 0, 0.1);
	        entorno.cambiarFont("Arial", 12, Color.WHITE);
	        entorno.escribirTexto("Rápido", zombieRapidoBanner.getX() - 15, zombieRapidoBanner.getY() + 45);
	    }
	    
	    if (zombieColosalBanner != null && zombieColosalBanner.getImagen() != null) {
	        zombieColosalBanner.setX(970);
	        zombieColosalBanner.setY(40);
	        entorno.dibujarImagen(zombieColosalBanner.getImagen(), 
	                            zombieColosalBanner.getX(), zombieColosalBanner.getY(), 0, 0.1);
	        entorno.cambiarFont("Arial", 12, Color.WHITE);
	        entorno.escribirTexto("Colosal", zombieColosalBanner.getX() - 15, zombieColosalBanner.getY() + 45);
	    }
	}

   private void generarDisparosZombies() {
	    int tickActual = entorno.numeroDeTick();
	    
	    for (int i = 0; i < zombies.length; i++) {
	        if (zombies[i] != null && zombies[i].estaVivo() && 
	            zombies[i].puedeDisparar(tickActual) && !zombies[i].estaBloqueado()) {
	            
	            BolaNieve nuevaBola = zombies[i].disparar(tickActual);
	            if (nuevaBola != null) {
	                agregarBolaNieve(nuevaBola);
	            }
	        }
	    }
	}

   private void agregarBolaNieve(BolaNieve bola) {
       for (int i = 0; i < bolasNieve.length; i++) {
           if (bolasNieve[i] == null) {
               bolasNieve[i] = bola;
               cantidadBolasNieveActivas++;
               break;
           }
       }
   }

   private void actualizarBolasNieve() {
       for (int i = 0; i < bolasNieve.length; i++) {
           if (bolasNieve[i] != null) {
               bolasNieve[i].mover();
               bolasNieve[i].dibujar();
               if (!bolasNieve[i].isActiva()) {
                   bolasNieve[i] = null;
                   cantidadBolasNieveActivas--;
               }
           }
       }
   }

   private void verificarColisionesBolasNieve() {
       for (int i = 0; i < bolasNieve.length; i++) {
           if (bolasNieve[i] != null && bolasNieve[i].isActiva()) {
               for (int j = 0; j < plantas.length; j++) {
                   if (plantas[j] != null && plantas[j].isPlantada() &&
                       bolasNieve[i].colisionaCon(plantas[j])) {
                       
                       plantas[j].recibirAtaque(entorno.numeroDeTick());
                       
                       if (!plantas[j].isPlantada()) {
                           int indiceX = cuadricula.cercanoL(plantas[j].getX(), plantas[j].getY()).x;
                           int indiceY = cuadricula.cercanoL(plantas[j].getX(), plantas[j].getY()).y;
                           cuadricula.setOcupado(indiceX, indiceY, false);
                           
                           eliminarPlanta(j);
                       }
                       
                       bolasNieve[i].setActiva(false);
                       bolasNieve[i] = null;
                       cantidadBolasNieveActivas--;
                       break;
                   }
               }
           }
       }
   }

   private void generarZombieRapido() {
       if (zombieRapido == null && ticksParaProximoRapido > 0) {
           ticksParaProximoRapido--;
           
           if (ticksParaProximoRapido <= 0) {
               int fila = (int)(Math.random() * 5);
               zombieRapido = new ZombieRapido(fila, entorno);
               rapidosAparecidos++;
           }
       }
       
       if (zombieRapido == null && ticksParaProximoRapido <= 0) {
           ticksParaProximoRapido = 270;
       }
   }

   private void actualizarZombieRapido() {
       if (zombieRapido != null) {
           zombieRapido.mover();
           zombieRapido.dibujar();
           
           if (zombieRapido.llegoARegalos()) {
               juegoPerdido = true;
           }
           
           if (!zombieRapido.estaVivo()) {
               zombieRapido = null;
               zombiesEliminados += 1;
           }
       }
   }

   private void verificarColisionesConRapido() {
       if (zombieRapido == null || !zombieRapido.estaVivo()) return;
       
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null && disparos[i].isActiva()) {
               double distancia = Math.sqrt(Math.pow(disparos[i].getX() - zombieRapido.getX(), 2) +
                                         Math.pow(disparos[i].getY() - zombieRapido.getY(), 2));
               if (distancia < 40) {
                   zombieRapido.recibirDanio();
                   disparos[i].setActiva(false);
                   disparos[i] = null;
                   cantidadDisparosActivos--;
                   break;
               }
           }
       }
       
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null && disparosHielo[i].isActiva()) {
               double distancia = Math.sqrt(Math.pow(disparosHielo[i].getX() - zombieRapido.getX(), 2) +
                                         Math.pow(disparosHielo[i].getY() - zombieRapido.getY(), 2));
               if (distancia < 40) {
                   zombieRapido.ralentizar(disparosHielo[i].getDuracionRalentizacion());
                   disparosHielo[i].setActiva(false);
                   disparosHielo[i] = null;
                   cantidadDisparosHieloActivos--;
                   break;
               }
           }
       }
   }

   private void verificarAtaquesZombieRapido() {
       if (zombieRapido == null || !zombieRapido.estaVivo()) return;
       
       int tickActual = entorno.numeroDeTick();
       
       for (int j = 0; j < plantas.length; j++) {
           if (plantas[j] != null && plantas[j].isPlantada() &&
               zombieRapido.colisionaConPlanta(plantas[j])) {
               
               if (!zombieRapido.estaBloqueado()) {
                   zombieRapido.bloquear(plantas[j]);
               }
               
               if (zombieRapido.puedeAtacar(tickActual)) {
                   plantas[j].recibirAtaque(tickActual);
                   
                   if (!plantas[j].isPlantada()) {
                       int indiceX = cuadricula.cercanoL(plantas[j].getX(), plantas[j].getY()).x;
                       int indiceY = cuadricula.cercanoL(plantas[j].getX(), plantas[j].getY()).y;
                       cuadricula.setOcupado(indiceX, indiceY, false);
                       
                       eliminarPlanta(j);
                       zombieRapido.liberar();
                   }
                   zombieRapido.registrarAtaque(tickActual);
               }
               break;
           }
       }
       
       zombieRapido.verificarPlantaBloqueadora();
   }

   private void verificarExplosionCereza(CerezaExplosiva cereza, int tickActual) {
       if (cereza.debeExplotar(tickActual)) {
           for (int i = 0; i < zombies.length; i++) {
               if (zombies[i] != null && zombies[i].estaVivo()) {
                   double distancia = Math.sqrt(Math.pow(zombies[i].getX() - cereza.getX(), 2) +
                                              Math.pow(zombies[i].getY() - cereza.getY(), 2));
                   if (distancia < cereza.getRadioExplosion()) {
                       zombies[i].morir();
                       zombiesEliminados++;
                   }
               }
           }
          
           if (zombieRapido != null && zombieRapido.estaVivo()) {
               double distancia = Math.sqrt(Math.pow(zombieRapido.getX() - cereza.getX(), 2) +
                                          Math.pow(zombieRapido.getY() - cereza.getY(), 2));
               if (distancia < cereza.getRadioExplosion()) {
                   zombieRapido.morir();
                   zombiesEliminados += 2;
               }
           }
          
           if (zombieColosal != null && zombieColosal.estaVivo()) {
               double distancia = Math.sqrt(Math.pow(zombieColosal.getX() - cereza.getX(), 2) +
                                          Math.pow(zombieColosal.getY() - cereza.getY(), 2));
               if (distancia < cereza.getRadioExplosion()) {
                   zombieColosal.morir();
                   zombiesEliminados += 5;
               }
           }
          
           cereza.setPlantada(false);
           int indiceX = cuadricula.cercanoL(cereza.getX(), cereza.getY()).x;
           int indiceY = cuadricula.cercanoL(cereza.getX(), cereza.getY()).y;
           cuadricula.setOcupado(indiceX, indiceY, false);
          
           for (int i = 0; i < plantas.length; i++) {
               if (plantas[i] == cereza) {
                   eliminarPlanta(i);
                   break;
               }
           }
       }
   }
  
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
  
   private void actualizarZombieColosal() {
       if (zombieColosal != null) {
           zombieColosal.mover();
           zombieColosal.dibujar();
          
           if (zombieColosal.llegoARegalos()) {
               juegoPerdido = true;
           }
          
           if (!zombieColosal.estaVivo()) {
               zombieColosal = null;
               zombiesEliminados += 1;
           }
       }
   }
  
   private void verificarColisionesConColosal() {
       if (zombieColosal == null || !zombieColosal.estaVivo()) return;
      
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null && disparos[i].isActiva()) {
               double distancia = Math.sqrt(Math.pow(disparos[i].getX() - zombieColosal.getX(), 2) +
                                          Math.pow(disparos[i].getY() - zombieColosal.getY(), 2));
               if (distancia < 70) {
                   zombieColosal.recibirDanio();
                   disparos[i].setActiva(false);
                   disparos[i] = null;
                   cantidadDisparosActivos--;
                   break;
               }
           }
       }
      
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null && disparosHielo[i].isActiva()) {
               double distancia = Math.sqrt(Math.pow(disparosHielo[i].getX() - zombieColosal.getX(), 2) +
                                          Math.pow(disparosHielo[i].getY() - zombieColosal.getY(), 2));
               if (distancia < 70) {
                   zombieColosal.ralentizar(disparosHielo[i].getDuracionRalentizacion());
                   disparosHielo[i].setActiva(false);
                   disparosHielo[i] = null;
                   cantidadDisparosHieloActivos--;
                   break;
               }
           }
       }
   }
  
   private void verificarAtaquesZombieColosal() {
       if (zombieColosal == null || !zombieColosal.estaVivo()) return;
      
       int tickActual = entorno.numeroDeTick();
      
       for (int j = 0; j < plantas.length; j++) {
           if (plantas[j] != null && plantas[j].isPlantada() &&
               zombieColosal.colisionaConPlanta(plantas[j])) {
              
               if (zombieColosal.puedeAtacar(tickActual)) {
                   if (plantas[j] instanceof WallNut) {
                       WallNut wallnut = (WallNut) plantas[j];
                       wallnut.recibirAtaque(tickActual);
                       wallnut.recibirAtaque(tickActual);
                   } else {
                       plantas[j].recibirAtaque(tickActual);
                       if (!plantas[j].isPlantada()) {
                           int indiceX = cuadricula.cercanoL(plantas[j].getX(), plantas[j].getY()).x;
                           int indiceY = cuadricula.cercanoL(plantas[j].getX(), plantas[j].getY()).y;
                           cuadricula.setOcupado(indiceX, indiceY, false);
                           eliminarPlanta(j);
                       }
                   }
                   zombieColosal.registrarAtaque(tickActual);
               }
               break;
           }
       }
   }
  
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
   
   private void manejarMovimientoTeclado() {
	       planta plantaSeleccionada = null;
	       for (planta p : plantas) {
	           if (p != null && p.isSeleccionada()) {
	               plantaSeleccionada = p;
	               break;
	           }
	       }
	       
	       if (plantaSeleccionada == null) {
	           return;
	       }
	       
	       double velocidad = 105;
	       double nuevaX = plantaSeleccionada.getX();
	       double nuevaY = plantaSeleccionada.getY();
	       
	       if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
	           nuevaY -= velocidad;
	       }
	       if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
	           nuevaY += velocidad;
	       }
	       if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
	           nuevaX -= velocidad;
	       }
	       if (entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
	           nuevaX += velocidad;
	       }
	       
	       boolean enBanner = nuevaY < 100;
	       boolean enPrimeraColumna = nuevaX < 100;
	       boolean dentroDeLimites = nuevaX >= 30 && nuevaX <= 1000 && nuevaY >= 30 && nuevaY <= 550;
	       
	       if (!enBanner && !enPrimeraColumna && dentroDeLimites) {
	           plantaSeleccionada.setPosicion(nuevaX, nuevaY);
	       }
	   }
   
   private void verificarAtaquesZombies() {
       int tickActual = entorno.numeroDeTick();
       
       for (int i = 0; i < zombies.length; i++) {
           if (zombies[i] != null && zombies[i].estaVivo()) {
               
               zombies[i].verificarPlantaBloqueadora();
               
               if (zombies[i].estaBloqueado()) {
                   planta plantaBloqueadora = zombies[i].getPlantaBloqueadora();
                   if (plantaBloqueadora != null && plantaBloqueadora.isPlantada() && 
                       zombies[i].puedeAtacar(tickActual)) {
                       
                       plantaBloqueadora.recibirAtaque(tickActual);
                       
                       if (!plantaBloqueadora.isPlantada()) {
                           int indiceX = cuadricula.cercanoL(plantaBloqueadora.getX(), plantaBloqueadora.getY()).x;
                           int indiceY = cuadricula.cercanoL(plantaBloqueadora.getX(), plantaBloqueadora.getY()).y;
                           cuadricula.setOcupado(indiceX, indiceY, false);
                           
                           for (int j = 0; j < plantas.length; j++) {
                               if (plantas[j] == plantaBloqueadora) {
                                   eliminarPlanta(j);
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
                       if (plantas[j] != null && plantas[j].isPlantada() && 
                           zombies[i].colisionaConPlanta(plantas[j])) {
                           
                           zombies[i].bloquear(plantas[j]);
                           
                           if (plantas[j] instanceof CerezaExplosiva) {
                               CerezaExplosiva cereza = (CerezaExplosiva) plantas[j];
                               if (!cereza.isExplotando()) {
                                   if (cereza.hayZombieCerca(zombies)) {
                                       verificarExplosionCereza(cereza, tickActual);
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
   
   private void dibujarBarrasRecarga() {
       int tickActual = entorno.numeroDeTick();
      
       if (wallnutBanner instanceof WallNut) {
           WallNut wallnut = (WallNut) wallnutBanner;
           double porcentaje = wallnut.porcentajeRecarga(tickActual);
           Color marronFondo = new Color(139, 69, 19);
           Color marronBorde = new Color(160, 82, 45);
           dibujarBarraRecargaCompleta(50, 75, porcentaje, marronFondo, marronBorde, "WallNut", wallnut, tickActual);
       }
      
       if (hieloBanner instanceof PlantaDeHielo) {
           PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
           double porcentaje = hielo.porcentajeRecarga(tickActual);
           Color celesteFondo = new Color(135, 206, 250);
           Color celesteBorde = new Color(173, 216, 230);
           dibujarBarraRecargaCompleta(150, 75, porcentaje, celesteFondo, celesteBorde, "Escarchi", hielo, tickActual);
       }
      
       if (roseBanner instanceof RoseBlade) {
           RoseBlade rose = (RoseBlade) roseBanner;
           double porcentaje = rose.porcentajeRecarga(tickActual);
           Color rojoFondo = new Color(255, 0, 0);
           Color rojoBorde = new Color(255, 69, 0);
           dibujarBarraRecargaCompleta(250, 75, porcentaje, rojoFondo, rojoBorde, "RoseBlade", rose, tickActual);
       }
      
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
	    
	    Color fondoOscuro = oscurecerColor(colorFondo, 0.5f);
	    entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, fondoOscuro);
	    
	    if (porcentaje > 0) {
	        double anchoLleno = anchoBarra * porcentaje;
	        
	        if (porcentaje < 1.0) {
	            for (int i = 0; i < anchoLleno; i += 4) {
	                double onda = Math.sin((tickActual * 0.3) + (i * 0.2)) * 2;
	                double alturaOnda = altoBarra + onda;
	                double posY = y - onda/2;
	                
	                if (i + 2 <= anchoLleno) {
	                    entorno.dibujarRectangulo(
	                        x - anchoBarra/2 + i + 2, 
	                        posY, 
	                        2, 
	                        alturaOnda, 
	                        0, 
	                        aclararColor(colorFondo, 0.1f)
	                    );
	                }
	            }
	        }
	        
	        entorno.dibujarRectangulo(x - anchoBarra/2 + anchoLleno/2, y, anchoLleno, altoBarra, 0, colorFondo);
	    }
	    
	    entorno.dibujarRectangulo(x, y, anchoBarra, altoBarra, 0, colorBorde);
	    
	    entorno.cambiarFont("Arial", 12, Color.WHITE);
	    entorno.escribirTexto(texto, x - 25, y - 5);
	    
	    if (porcentaje < 1.0) {
	        int segundosRestantes = calcularSegundosRestantes(planta, tickActual);
	        
	        Color colorTiempo = esColorOscuro(colorFondo) ? Color.WHITE : Color.BLACK;
	        entorno.cambiarFont("Arial", 10, colorTiempo);
	        
	        if (segundosRestantes <= 5) {
	            entorno.cambiarFont("Arial", 10, Color.YELLOW);
	        } else if (segundosRestantes <= 10) {
	            entorno.cambiarFont("Arial", 10, Color.ORANGE);
	        }
	        
	        entorno.escribirTexto(segundosRestantes + "s", x - 8, y + 20);
	    } else {
	        Color colorListo = esColorOscuro(colorFondo) ? Color.GREEN : new Color(0, 100, 0);
	        entorno.cambiarFont("Arial", 10, colorListo);
	        entorno.escribirTexto("Listo", x - 12, y + 20);
	    }
	}
   
   private Color aclararColor(Color color, float factor) {
	    int rojo = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
	    int verde = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
	    int azul = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
	    return new Color(rojo, verde, azul);
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
	        // Usar el método público para obtener el tiempo de recarga
	        int tiempoTranscurrido = tickActual - hielo.getTiempoUltimoPlantado();
	        int tiempoRestante = Math.max(0, hielo.getTiempoRecargaPlantado() - tiempoTranscurrido);
	        return (int) Math.ceil(tiempoRestante / 6.0);
	    } else if (planta instanceof RoseBlade) {
	        RoseBlade rose = (RoseBlade) planta;
	        int tiempoTranscurrido = tickActual - rose.getTiempoUltimoPlantado();
	        int tiempoRestante = Math.max(0, rose.getTiempoRecargaPlantado() - tiempoTranscurrido);
	        return (int) Math.ceil(tiempoRestante / 6.0);
	    } else if (planta instanceof WallNut) {
	        WallNut wallnut = (WallNut) planta;
	        int tiempoTranscurrido = tickActual - wallnut.getTiempoUltimoUso();
	        int tiempoRestante = Math.max(0, wallnut.getTiempoRecargaPlantado() - tiempoTranscurrido);
	        return (int) Math.ceil(tiempoRestante / 6.0);
	    } else if (planta instanceof CerezaExplosiva) {
	        CerezaExplosiva cereza = (CerezaExplosiva) planta;
	        int tiempoTranscurrido = tickActual - cereza.getTiempoUltimoPlantado();
	        int tiempoRestante = Math.max(0, cereza.getTiempoRecargaPlantado() - tiempoTranscurrido);
	        return (int) Math.ceil(tiempoRestante / 6.0);
	    }
	    return 0;
	}
   private void agregarDisparo(BolaFuego disparo) {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] == null) {
               disparos[i] = disparo;
               cantidadDisparosActivos++;
               break;
           }
       }
   }
  
   private void agregarDisparoHielo(BolaEscarcha disparo) {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] == null) {
               disparosHielo[i] = disparo;
               cantidadDisparosHieloActivos++;
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
                   cantidadZombiesActivos++;
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
              
               if (!zombies[i].estaVivo()) {
                   zombies[i] = null;
                   zombiesEliminados++;
                   cantidadZombiesActivos--;
               }
           }
       }
   }
  
   private void actualizarDisparos() {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null) {
               disparos[i].mover();
               disparos[i].dibujar();
               if (!disparos[i].isActiva()) {
                   disparos[i] = null;
                   cantidadDisparosActivos--;
               }
           }
       }
   }
  
   private void actualizarDisparosHielo() {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null) {
               disparosHielo[i].mover();
               disparosHielo[i].dibujar();
               if (!disparosHielo[i].isActiva()) {
                   disparosHielo[i] = null;
                   cantidadDisparosHieloActivos--;
               }
           }
       }
   }
  
   private void verificarColisiones() {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null && disparos[i].isActiva()) {
               for (int j = 0; j < zombies.length; j++) {
                   if (zombies[j] != null && zombies[j].estaVivo() &&
                       disparos[i].colisionaCon(zombies[j])) {
                       zombies[j].recibirDanio();
                       disparos[i].setActiva(false);
                       disparos[i] = null;
                       cantidadDisparosActivos--;
                       break;
                   }
               }
           }
       }
   }
  
   private void verificarColisionesHielo() {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null && disparosHielo[i].isActiva()) {
               for (int j = 0; j < zombies.length; j++) {
                   if (zombies[j] != null && zombies[j].estaVivo() &&
                       disparosHielo[i].colisionaCon(zombies[j])) {
                       zombies[j].ralentizar(disparosHielo[i].getDuracionRalentizacion());
                       disparosHielo[i].setActiva(false);
                       disparosHielo[i] = null;
                       cantidadDisparosHieloActivos--;
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
                   plantas[i].setSeleccionada(false);
               }
           }
          
           int tickActual = entorno.numeroDeTick();
          
           if (wallnutBanner != null && wallnutBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               WallNut wallnut = (WallNut) wallnutBanner;
               if (!wallnut.estaEnRecarga(tickActual)) {
                   wallnutBanner.setSeleccionada(true);
               }
           }
           else if (hieloBanner != null && hieloBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
               if (!hielo.estaEnRecarga(tickActual)) {
                   hieloBanner.setSeleccionada(true);
               }
           }
           else if (roseBanner != null && roseBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               RoseBlade rose = (RoseBlade) roseBanner;
               if (!rose.estaEnRecarga(tickActual)) {
                   roseBanner.setSeleccionada(true);
               }
           }
           else if (cerezaBanner != null && cerezaBanner.encima(entorno.mouseX(), entorno.mouseY())) {
               CerezaExplosiva cereza = (CerezaExplosiva) cerezaBanner;
               if (!cereza.estaEnRecarga(tickActual)) {
                   cerezaBanner.setSeleccionada(true);
               }
           }
           else {
               for (int i = 0; i < plantas.length; i++) {
                   if (plantas[i] != null && plantas[i].isPlantada() && plantas[i].encima(entorno.mouseX(), entorno.mouseY())) {
                       plantas[i].setSeleccionada(true);
                       break;
                   }
               }
           }
       }
      
       if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
           for (int i = 0; i < plantas.length; i++) {
               if (plantas[i] != null && plantas[i].isSeleccionada()) {
                   plantas[i].arrastrar(entorno.mouseX(), entorno.mouseY());
               }
           }
       }
      
       if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
           manejarSueltaBoton();
       }
   }
   
   private void manejarSueltaBoton() {
       int tickActual = entorno.numeroDeTick();
       
       if (wallnutBanner != null && wallnutBanner.isSeleccionada()) {
           manejarSueltaWallNut(tickActual);
       }
       else if (hieloBanner != null && hieloBanner.isSeleccionada()) {
           manejarSueltaHielo(tickActual);
       }
       else if (roseBanner != null && roseBanner.isSeleccionada()) {
           manejarSueltaRose(tickActual);
       }
       else if (cerezaBanner != null && cerezaBanner.isSeleccionada()) {
           manejarSueltaCereza(tickActual);
       }
       else {
           manejarSueltaPlantaExistente();
       }
   }
   
   private void manejarSueltaWallNut(int tickActual) {
       WallNut wallnut = (WallNut) wallnutBanner;
      
       if (entorno.mouseY() < 70) {
           wallnutBanner.setPosicion(wallnutBanner.getXInicial(), wallnutBanner.getYInicial());
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.isOcupado(indiceX, indiceY)) {
               WallNut nuevaWallnut = new WallNut(cuadricula.getCoorX()[indiceX], cuadricula.getCoorY()[indiceY], entorno);
               nuevaWallnut.setPlantada(true);
              
               if (agregarPlanta(nuevaWallnut)) {
                   cuadricula.setOcupado(indiceX, indiceY, true);
                   wallnut.usar(tickActual);
               }
           }
           wallnutBanner.setPosicion(wallnutBanner.getXInicial(), wallnutBanner.getYInicial());
       }
       wallnutBanner.setSeleccionada(false);
   }
   
   private void manejarSueltaHielo(int tickActual) {
       PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
      
       if (entorno.mouseY() < 70) {
           hieloBanner.setPosicion(hieloBanner.getXInicial(), hieloBanner.getYInicial());
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.isOcupado(indiceX, indiceY)) {
               PlantaDeHielo nuevaHielo = new PlantaDeHielo(cuadricula.getCoorX()[indiceX], cuadricula.getCoorY()[indiceY], entorno);
               nuevaHielo.setPlantada(true);
              
               if (agregarPlanta(nuevaHielo)) {
                   cuadricula.setOcupado(indiceX, indiceY, true);
                   hielo.usar(tickActual);
               }
           }
           hieloBanner.setPosicion(hieloBanner.getXInicial(), hieloBanner.getYInicial());
       }
       hieloBanner.setSeleccionada(false);
   }
   
   private void manejarSueltaRose(int tickActual) {
       RoseBlade rose = (RoseBlade) roseBanner;
      
       if (entorno.mouseY() < 70) {
           roseBanner.setPosicion(roseBanner.getXInicial(), roseBanner.getYInicial());
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.isOcupado(indiceX, indiceY)) {
               RoseBlade nuevaRose = new RoseBlade(cuadricula.getCoorX()[indiceX], cuadricula.getCoorY()[indiceY], entorno);
               nuevaRose.setPlantada(true);
              
               if (agregarPlanta(nuevaRose)) {
                   cuadricula.setOcupado(indiceX, indiceY, true);
                   rose.usar(tickActual);
               }
           }
           roseBanner.setPosicion(roseBanner.getXInicial(), roseBanner.getYInicial());
       }
       roseBanner.setSeleccionada(false);
   }
   
   private void manejarSueltaCereza(int tickActual) {
       CerezaExplosiva cereza = (CerezaExplosiva) cerezaBanner;
      
       if (entorno.mouseY() < 70) {
           cerezaBanner.setPosicion(cerezaBanner.getXInicial(), cerezaBanner.getYInicial());
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.isOcupado(indiceX, indiceY)) {
               CerezaExplosiva nuevaCereza = new CerezaExplosiva(cuadricula.getCoorX()[indiceX], cuadricula.getCoorY()[indiceY], entorno);
               nuevaCereza.setPlantada(true);
              
               if (agregarPlanta(nuevaCereza)) {
                   cuadricula.setOcupado(indiceX, indiceY, true);
                   cereza.usar(tickActual);
               }
           }
           cerezaBanner.setPosicion(cerezaBanner.getXInicial(), cerezaBanner.getYInicial());
       }
       cerezaBanner.setSeleccionada(false);
   }
   
   private void manejarSueltaPlantaExistente() {
       for (int i = 0; i < plantas.length; i++) {
           if (plantas[i] != null && plantas[i].isPlantada() && plantas[i].isSeleccionada()) {
               int indiceX = cuadricula.cercanoL(plantas[i].getX(), plantas[i].getY()).x;
               int indiceY = cuadricula.cercanoL(plantas[i].getX(), plantas[i].getY()).y;
              
               int indiceXAnterior = cuadricula.cercanoL(plantas[i].getXInicial(), plantas[i].getYInicial()).x;
               int indiceYAnterior = cuadricula.cercanoL(plantas[i].getXInicial(), plantas[i].getYInicial()).y;
               cuadricula.setOcupado(indiceXAnterior, indiceYAnterior, false);
              
               if (indiceX > 0 && !cuadricula.isOcupado(indiceX, indiceY)) {
                   cuadricula.centrarPlanta(plantas[i], indiceX, indiceY);
                   cuadricula.setOcupado(indiceX, indiceY, true);
                   plantas[i].setPosicionInicial(plantas[i].getX(), plantas[i].getY());
               } else {
                   plantas[i].setPosicion(plantas[i].getXInicial(), plantas[i].getYInicial());
                   cuadricula.setOcupado(indiceXAnterior, indiceYAnterior, true);
               }
           }
       }
   }
  
   private void dibujarUI() {
	    entorno.cambiarFont("Arial", 20, Color.WHITE);
	    entorno.escribirTexto("Zombies: " + zombiesEliminados + "/" + zombiesTotales, 400, 40);
	    entorno.escribirTexto("Tiempo: " + entorno.tiempo()/1000 + "s", 400, 80);
	   
	    int zombiesEnPantalla = 0;
	    for (Zombie z : zombies) {
	        if (z != null && z.estaVivo()) {
	            zombiesEnPantalla++;
	        }
	    }
	    entorno.escribirTexto("Zombies en pantalla: " + zombiesEnPantalla, 400, 60);
	   
	  //Aviso de que los zombies van a aparecer en pantalla
	    if (zombieRapido != null && zombieRapido.estaVivo()) {
	        entorno.cambiarFont("Impact", 50, Color.ORANGE);
	        entorno.escribirTexto("¡ZOMBIE RÁPIDO!", 330, 350);
	    } 
	    
	    if (zombieColosal != null && zombieColosal.estaVivo()) {
	        entorno.cambiarFont("Impact", 50, Color.RED);
	        entorno.escribirTexto("¡ZOMBIE COLOSAL!", 330, 350);
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
	        entorno.escribirTexto("Rápidos derrotados: " + rapidosAparecidos, 400, 410);
	        entorno.escribirTexto("Colosales derrotados: " + colosalesAparecidos, 400, 440);
	    } else if (juegoPerdido) {
	        entorno.cambiarFont("Arial", 40, Color.RED);
	        entorno.escribirTexto("¡DERROTA!", 400, 300);
	        entorno.cambiarFont("Arial", 24, Color.WHITE);
	        entorno.escribirTexto("Zombies eliminados: " + zombiesEliminados, 400, 350);
	      
	    }
	}
	  
	public static void main(String[] args) {
	       new Juego();
	   }
}