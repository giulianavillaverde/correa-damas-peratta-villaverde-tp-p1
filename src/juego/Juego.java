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
   public int contadorPlantas;
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
   public int cantidadPlantasActivas;
   public int cantidadDisparosActivos;
   public int cantidadDisparosHieloActivos;
   public int cantidadBolasNieveActivas;
   public int cantidadZombiesActivos;
  
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
       
       this.zombieNormalBanner.x = 800;
       this.zombieNormalBanner.y = 20;
       this.zombieNormalBanner.vivo = true;
       
       this.zombieRapidoBanner.x = 900;
       this.zombieRapidoBanner.y = 20;
       this.zombieRapidoBanner.vivo = true;
       
       this.zombieColosalBanner.x = 1000;
       this.zombieColosalBanner.y = 20;
       this.zombieColosalBanner.vivo = true;
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
   
   public void compactarArrayPlantas() {
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
           if(p != null && p.plantada) {
               p.dibujar();
               p.actualizar(tickActual);
               p.ejecutarComportamientoEspecifico(tickActual, this);
           }
       }
   }

   private void dibujarZombiesBanner() {
	    if (zombieNormalBanner != null && zombieNormalBanner.imagen != null) {
	        zombieNormalBanner.x = 770;
	        zombieNormalBanner.y = 40;
	        entorno.dibujarImagen(zombieNormalBanner.imagen, 
	                            zombieNormalBanner.x, zombieNormalBanner.y, 0, 0.1);
	        entorno.cambiarFont("Arial", 12, Color.WHITE);
	        entorno.escribirTexto("Grinch", zombieNormalBanner.x - 15, zombieNormalBanner.y + 45);
	    }
	    
	    if (zombieRapidoBanner != null && zombieRapidoBanner.imagen != null) {
	        zombieRapidoBanner.x = 870;
	        zombieRapidoBanner.y = 40;
	        entorno.dibujarImagen(zombieRapidoBanner.imagen, 
	                            zombieRapidoBanner.x, zombieRapidoBanner.y, 0, 0.1);
	        entorno.cambiarFont("Arial", 12, Color.WHITE);
	        entorno.escribirTexto("Rápido", zombieRapidoBanner.x - 15, zombieRapidoBanner.y + 45);
	    }
	    
	    if (zombieColosalBanner != null && zombieColosalBanner.imagen != null) {
	        zombieColosalBanner.x = 970;
	        zombieColosalBanner.y = 40;
	        entorno.dibujarImagen(zombieColosalBanner.imagen, 
	                            zombieColosalBanner.x, zombieColosalBanner.y, 0, 0.1);
	        entorno.cambiarFont("Arial", 12, Color.WHITE);
	        entorno.escribirTexto("Colosal", zombieColosalBanner.x - 15, zombieColosalBanner.y + 45);
	    }
	}

   private void generarDisparosZombies() {
	    int tickActual = entorno.numeroDeTick();
	    
	    for (int i = 0; i < zombies.length; i++) {
	        if (zombies[i] != null && zombies[i].vivo && 
	            zombies[i].puedeDisparar(tickActual) && !zombies[i].bloqueadoPorPlanta) {
	            
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
               if (!bolasNieve[i].activa) {
                   bolasNieve[i] = null;
                   cantidadBolasNieveActivas--;
               }
           }
       }
   }

   private void verificarColisionesBolasNieve() {
       for (int i = 0; i < bolasNieve.length; i++) {
           if (bolasNieve[i] != null && bolasNieve[i].activa) {
               for (int j = 0; j < plantas.length; j++) {
                   if (plantas[j] != null && plantas[j].plantada &&
                       bolasNieve[i].colisionaCon(plantas[j])) {
                       
                       plantas[j].recibirAtaque(entorno.numeroDeTick());
                       
                       if (!plantas[j].plantada) {
                           int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                           int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                           cuadricula.Ocupado(indiceX, indiceY, false);
                           
                           eliminarPlanta(j);
                       }
                       
                       bolasNieve[i].activa = false;
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
           
           if (!zombieRapido.vivo) {
               zombieRapido = null;
               zombiesEliminados += 1;
           }
       }
   }

   private void verificarColisionesConRapido() {
       if (zombieRapido == null || !zombieRapido.vivo) return;
       
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null && disparos[i].activa) {
               double distancia = Math.sqrt(Math.pow(disparos[i].x - zombieRapido.x, 2) +
                                         Math.pow(disparos[i].y - zombieRapido.y, 2));
               if (distancia < 40) {
                   zombieRapido.recibirDanio();
                   disparos[i].activa = false;
                   disparos[i] = null;
                   cantidadDisparosActivos--;
                   break;
               }
           }
       }
       
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null && disparosHielo[i].activa) {
               double distancia = Math.sqrt(Math.pow(disparosHielo[i].x - zombieRapido.x, 2) +
                                         Math.pow(disparosHielo[i].y - zombieRapido.y, 2));
               if (distancia < 40) {
                   zombieRapido.ralentizar(disparosHielo[i].duracionRalentizacion);
                   disparosHielo[i].activa = false;
                   disparosHielo[i] = null;
                   cantidadDisparosHieloActivos--;
                   break;
               }
           }
       }
   }

   private void verificarAtaquesZombieRapido() {
       if (zombieRapido == null || !zombieRapido.vivo) return;
       
       int tickActual = entorno.numeroDeTick();
       
       for (int j = 0; j < plantas.length; j++) {
           if (plantas[j] != null && plantas[j].plantada &&
               zombieRapido.colisionaConPlanta(plantas[j])) {
               
               if (!zombieRapido.bloqueadoPorPlanta) {
                   zombieRapido.bloquear(plantas[j]);
               }
               
               if (zombieRapido.puedeAtacar(tickActual)) {
                   plantas[j].recibirAtaque(tickActual);
                   
                   if (!plantas[j].plantada) {
                       int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                       int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                       cuadricula.Ocupado(indiceX, indiceY, false);
                       
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
               if (zombies[i] != null && zombies[i].vivo) {
                   double distancia = Math.sqrt(Math.pow(zombies[i].x - cereza.x, 2) +
                                              Math.pow(zombies[i].y - cereza.y, 2));
                   if (distancia < cereza.radioExplosion) {
                       zombies[i].morir();
                       zombiesEliminados++;
                   }
               }
           }
          
           if (zombieRapido != null && zombieRapido.vivo) {
               double distancia = Math.sqrt(Math.pow(zombieRapido.x - cereza.x, 2) +
                                          Math.pow(zombieRapido.y - cereza.y, 2));
               if (distancia < cereza.radioExplosion) {
                   zombieRapido.morir();
                   zombiesEliminados += 2;
               }
           }
          
           if (zombieColosal != null && zombieColosal.vivo) {
               double distancia = Math.sqrt(Math.pow(zombieColosal.x - cereza.x, 2) +
                                          Math.pow(zombieColosal.y - cereza.y, 2));
               if (distancia < cereza.radioExplosion) {
                   zombieColosal.morir();
                   zombiesEliminados += 5;
               }
           }
          
           cereza.plantada = false;
           int indiceX = cuadricula.cercanoL(cereza.x, cereza.y).x;
           int indiceY = cuadricula.cercanoL(cereza.x, cereza.y).y;
           cuadricula.Ocupado(indiceX, indiceY, false);
          
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
          
           if (!zombieColosal.vivo) {
               zombieColosal = null;
               zombiesEliminados += 1;
           }
       }
   }
  
   private void verificarColisionesConColosal() {
       if (zombieColosal == null || !zombieColosal.vivo) return;
      
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] != null && disparos[i].activa) {
               double distancia = Math.sqrt(Math.pow(disparos[i].x - zombieColosal.x, 2) +
                                          Math.pow(disparos[i].y - zombieColosal.y, 2));
               if (distancia < 70) {
                   zombieColosal.recibirDanio();
                   disparos[i].activa = false;
                   disparos[i] = null;
                   cantidadDisparosActivos--;
                   break;
               }
           }
       }
      
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] != null && disparosHielo[i].activa) {
               double distancia = Math.sqrt(Math.pow(disparosHielo[i].x - zombieColosal.x, 2) +
                                          Math.pow(disparosHielo[i].y - zombieColosal.y, 2));
               if (distancia < 70) {
                   zombieColosal.ralentizar(disparosHielo[i].duracionRalentizacion);
                   disparosHielo[i].activa = false;
                   disparosHielo[i] = null;
                   cantidadDisparosHieloActivos--;
                   break;
               }
           }
       }
   }
  
   private void verificarAtaquesZombieColosal() {
       if (zombieColosal == null || !zombieColosal.vivo) return;
      
       int tickActual = entorno.numeroDeTick();
      
       for (int j = 0; j < plantas.length; j++) {
           if (plantas[j] != null && plantas[j].plantada &&
               zombieColosal.colisionaConPlanta(plantas[j])) {
              
               if (zombieColosal.puedeAtacar(tickActual)) {
                   if (plantas[j] instanceof WallNut) {
                       WallNut wallnut = (WallNut) plantas[j];
                       wallnut.recibirAtaque(tickActual);
                       wallnut.recibirAtaque(tickActual);
                   } else {
                       plantas[j].recibirAtaque(tickActual);
                       if (!plantas[j].plantada) {
                           int indiceX = cuadricula.cercanoL(plantas[j].x, plantas[j].y).x;
                           int indiceY = cuadricula.cercanoL(plantas[j].x, plantas[j].y).y;
                           cuadricula.Ocupado(indiceX, indiceY, false);
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
	           if (p != null && p.seleccionada) {
	               plantaSeleccionada = p;
	               break;
	           }
	       }
	       
	       if (plantaSeleccionada == null) {
	           return;
	       }
	       
	       double velocidad = 105;
	       double nuevaX = plantaSeleccionada.x;
	       double nuevaY = plantaSeleccionada.y;
	       
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
	           plantaSeleccionada.x = nuevaX;
	           plantaSeleccionada.y = nuevaY;
	       }
	   }
   
   private void verificarAtaquesZombies() {
       int tickActual = entorno.numeroDeTick();
       
       for (int i = 0; i < zombies.length; i++) {
           if (zombies[i] != null && zombies[i].vivo) {
               
               zombies[i].verificarPlantaBloqueadora();
               
               if (zombies[i].bloqueadoPorPlanta) {
                   planta plantaBloqueadora = zombies[i].plantaBloqueadora;
                   if (plantaBloqueadora != null && plantaBloqueadora.plantada && 
                       zombies[i].puedeAtacar(tickActual)) {
                       
                       plantaBloqueadora.recibirAtaque(tickActual);
                       
                       if (!plantaBloqueadora.plantada) {
                           int indiceX = cuadricula.cercanoL(plantaBloqueadora.x, plantaBloqueadora.y).x;
                           int indiceY = cuadricula.cercanoL(plantaBloqueadora.x, plantaBloqueadora.y).y;
                           cuadricula.Ocupado(indiceX, indiceY, false);
                           
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
                       if (plantas[j] != null && plantas[j].plantada && 
                           zombies[i].colisionaConPlanta(plantas[j])) {
                           
                           zombies[i].bloquear(plantas[j]);
                           
                           if (plantas[j] instanceof CerezaExplosiva) {
                               CerezaExplosiva cereza = (CerezaExplosiva) plantas[j];
                               if (!cereza.explotando) {
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
	    int tiempoTranscurrido = tickActual - planta.tiempoUltimoPlantado;
	    int tiempoRestante = Math.max(0, planta.tiempoRecargaPlantado - tiempoTranscurrido);
	    return (int) Math.ceil(tiempoRestante / 50.0);
	}

   // MÉTODOS PÚBLICOS PARA QUE LAS PLANTAS LOS USEN
   public void agregarDisparo(BolaFuego disparo) {
       for (int i = 0; i < disparos.length; i++) {
           if (disparos[i] == null) {
               disparos[i] = disparo;
               cantidadDisparosActivos++;
               break;
           }
       }
   }
  
   public void agregarDisparoHielo(BolaEscarcha disparo) {
       for (int i = 0; i < disparosHielo.length; i++) {
           if (disparosHielo[i] == null) {
               disparosHielo[i] = disparo;
               cantidadDisparosHieloActivos++;
               break;
           }
       }
   }
  
   public void ejecutarExplosionCereza(CerezaExplosiva cereza, int tickActual) {
       verificarExplosionCereza(cereza, tickActual);
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
              
               if (!zombies[i].vivo) {
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
               if (!disparos[i].activa) {
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
               if (!disparosHielo[i].activa) {
                   disparosHielo[i] = null;
                   cantidadDisparosHieloActivos--;
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
                       cantidadDisparosActivos--;
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
                   plantas[i].x = entorno.mouseX();
                   plantas[i].y = entorno.mouseY();
               }
           }
       }
      
       if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
           manejarSueltaBoton();
       }
   }
   
   private void manejarSueltaBoton() {
       int tickActual = entorno.numeroDeTick();
       
       if (wallnutBanner != null && wallnutBanner.seleccionada) {
           manejarSueltaWallNut(tickActual);
       }
       else if (hieloBanner != null && hieloBanner.seleccionada) {
           manejarSueltaHielo(tickActual);
       }
       else if (roseBanner != null && roseBanner.seleccionada) {
           manejarSueltaRose(tickActual);
       }
       else if (cerezaBanner != null && cerezaBanner.seleccionada) {
           manejarSueltaCereza(tickActual);
       }
       else {
           manejarSueltaPlantaExistente();
       }
   }
   
   private void manejarSueltaWallNut(int tickActual) {
       WallNut wallnut = (WallNut) wallnutBanner;
      
       if (entorno.mouseY() < 70) {
           wallnutBanner.x = wallnutBanner.xInicial;
           wallnutBanner.y = wallnutBanner.yInicial;
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
               WallNut nuevaWallnut = new WallNut(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
               nuevaWallnut.plantada = true;
              
               if (agregarPlanta(nuevaWallnut)) {
                   cuadricula.Ocupado(indiceX, indiceY, true);
                   wallnut.usar(tickActual);
               }
           }
           wallnutBanner.x = wallnutBanner.xInicial;
           wallnutBanner.y = wallnutBanner.yInicial;
       }
       wallnutBanner.seleccionada = false;
   }
   
   private void manejarSueltaHielo(int tickActual) {
       PlantaDeHielo hielo = (PlantaDeHielo) hieloBanner;
      
       if (entorno.mouseY() < 70) {
           hieloBanner.x = hieloBanner.xInicial;
           hieloBanner.y = hieloBanner.yInicial;
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
               PlantaDeHielo nuevaHielo = new PlantaDeHielo(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
               nuevaHielo.plantada = true;
              
               if (agregarPlanta(nuevaHielo)) {
                   cuadricula.Ocupado(indiceX, indiceY, true);
                   hielo.usar(tickActual);
               }
           }
           hieloBanner.x = hieloBanner.xInicial;
           hieloBanner.y = hieloBanner.yInicial;
       }
       hieloBanner.seleccionada = false;
   }
   
   private void manejarSueltaRose(int tickActual) {
       RoseBlade rose = (RoseBlade) roseBanner;
      
       if (entorno.mouseY() < 70) {
           roseBanner.x = roseBanner.xInicial;
           roseBanner.y = roseBanner.yInicial;
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
               RoseBlade nuevaRose = new RoseBlade(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
               nuevaRose.plantada = true;
              
               if (agregarPlanta(nuevaRose)) {
                   cuadricula.Ocupado(indiceX, indiceY, true);
                   rose.usar(tickActual);
               }
           }
           roseBanner.x = roseBanner.xInicial;
           roseBanner.y = roseBanner.yInicial;
       }
       roseBanner.seleccionada = false;
   }
   
   private void manejarSueltaCereza(int tickActual) {
       CerezaExplosiva cereza = (CerezaExplosiva) cerezaBanner;
      
       if (entorno.mouseY() < 70) {
           cerezaBanner.x = cerezaBanner.xInicial;
           cerezaBanner.y = cerezaBanner.yInicial;
       } else {
           int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
           int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
          
           if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
               CerezaExplosiva nuevaCereza = new CerezaExplosiva(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY], entorno);
               nuevaCereza.plantada = true;
              
               if (agregarPlanta(nuevaCereza)) {
                   cuadricula.Ocupado(indiceX, indiceY, true);
                   cereza.usar(tickActual);
               }
           }
           cerezaBanner.x = cerezaBanner.xInicial;
           cerezaBanner.y = cerezaBanner.yInicial;
       }
       cerezaBanner.seleccionada = false;
   }
   
   private void manejarSueltaPlantaExistente() {
       for (int i = 0; i < plantas.length; i++) {
           if (plantas[i] != null && plantas[i].plantada && plantas[i].seleccionada) {
               int indiceX = cuadricula.cercanoL(plantas[i].x, plantas[i].y).x;
               int indiceY = cuadricula.cercanoL(plantas[i].x, plantas[i].y).y;
              
               int indiceXAnterior = cuadricula.cercanoL(plantas[i].xInicial, plantas[i].yInicial).x;
               int indiceYAnterior = cuadricula.cercanoL(plantas[i].xInicial, plantas[i].yInicial).y;
               cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, false);
              
               if (indiceX > 0 && !cuadricula.estaOcupado(indiceX, indiceY)) {
                   cuadricula.centrarPlanta(plantas[i], indiceX, indiceY);
                   cuadricula.Ocupado(indiceX, indiceY, true);
                   plantas[i].xInicial = plantas[i].x;
                   plantas[i].yInicial = plantas[i].y;
               } else {
                   plantas[i].x = plantas[i].xInicial;
                   plantas[i].y = plantas[i].yInicial;
                   cuadricula.Ocupado(indiceXAnterior, indiceYAnterior, true);
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
	        if (z != null && z.vivo) {
	            zombiesEnPantalla++;
	        }
	    }
	    entorno.escribirTexto("Zombies en pantalla: " + zombiesEnPantalla, 400, 60);
	   
	    if (zombieRapido != null && zombieRapido.vivo) {
	        entorno.cambiarFont("Impact", 50, Color.ORANGE);
	        entorno.escribirTexto("¡ZOMBIE RÁPIDO!", 330, 350);
	    } 
	    
	    if (zombieColosal != null && zombieColosal.vivo) {
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

