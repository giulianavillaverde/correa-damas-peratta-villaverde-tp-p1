package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;
import juego.PlantaDeHielo;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	Entorno entorno;
	Banner banner;
	cuadricula cuadricula;
	regalos[] regalo;
	planta [] plantas;
	
	// Variables y métodos propios de cada grupo//
	Juego(){
		
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 1034, 585);
		this.cuadricula = new cuadricula (50, 150, entorno);
		this.banner = new Banner (entorno);
		
		this.regalo = new regalos [5];
		for (int i = 0; i< regalo.length; i++) {
			double x= 50;
			double y = 140 + (i * 100);
			this.regalo[i] = new regalos(x, y, entorno);
		}
		this.plantas = new planta[30];
		double x_girasol = 50;
		double y_banner = 40;
		this.plantas[0]= new planta(x_girasol, y_banner,entorno);
		double x_hielo = x_girasol + 100;
		this.plantas[1] = new PlantaDeHielo (x_hielo, y_banner, entorno);
		
		
		
		
		// Inicializar lo que haga falta para el juego
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick(){
		entorno.colorFondo(Color.GREEN);
		
		this.banner.dibujar();
		this.cuadricula.dibujar();
		for (regalos r: this.regalo) {
			r.dibujar();
			
				}
		for(planta p: this.plantas) {
			if(p != null) {
				p.dibujar();
			}
			
		}
		
		if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
			for (int x=0; x < this.plantas.length; x++) {
				if(this.plantas[x] != null ) {
				
					if(plantas[x].encima(entorno.mouseX(), entorno.mouseY())) {
						plantas[x].seleccionada = true;
						if (entorno.mouseY() > 70) {
							cuadricula.ocupado[cuadricula.cercano(entorno.mouseX(), entorno.mouseY()).x][cuadricula.cercano(entorno.mouseX(), entorno.mouseY()).y] = false;
						}	
					}else{
						plantas[x].seleccionada = false;
						
					}
				}
			}
		}
		
		if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
			for (int x=0; x < this.plantas.length; x++) {
				if(this.plantas[x] != null ) {
			
					if (plantas[x].seleccionada){
					
					plantas[x].arrastrar(entorno.mouseX(), entorno.mouseY());
					}
				}
			}
		}
		if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
			for (int ite=0; ite < this.plantas.length; ite++) {
				if (this.plantas[ite] != null) {
					if (plantas[ite].seleccionada) {
						if (entorno.mouseY() < 70 && !plantas[ite].plantada) {
							plantas[ite].arrastrar(50,40);
						
					}else {
						int indiceX = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
						int indiceY = cuadricula.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
						if (cuadricula.ocupado[indiceX][indiceY]) {
							return;
						}
						plantas[ite].arrastrar(cuadricula.coorX[indiceX], cuadricula.coorY[indiceY]);
						cuadricula.ocupado[indiceX][indiceY] = true;
						plantas[ite].plantada = true;
						}
						
					}
					
			}
		}
	}
		System.out.println("plantada" + plantas[0].plantada);
		System.out.println("seleccionada" + plantas[0].seleccionada);
		
	for (int ite = 0; ite < this.plantas.length; ite++) {	
		if (this.plantas[ite]!= null && this.plantas[ite].seleccionada && this.plantas[ite].plantada && entorno.sePresiono(entorno.TECLA_ARRIBA)) {
			int indiceX = cuadricula.cercanoL(plantas[0].x, plantas[0].y).x;
			int indiceY = cuadricula.cercanoL(plantas[0].x, plantas[0].y ).y;
			if (indiceY > 1 && !cuadricula.ocupado[indiceX][indiceY-1]) {
				this.cuadricula.ocupado [indiceX][indiceY] = false;
				plantas[ite].x = cuadricula.coorX[indiceX];
				plantas[ite].y = cuadricula.coorY[indiceY - 1];
				this.cuadricula.ocupado [indiceX][indiceY-1] = true;
				break;
			}	
		}
	}
	for (int ite = 0; ite < this.plantas.length; ite++) {
		if (this.plantas[ite] != null && this.plantas[ite].seleccionada && this.plantas[ite].plantada && entorno.sePresiono(entorno.TECLA_ABAJO)) {
			System.out.println("hola");
			int indiceX = cuadricula.cercanoL(plantas[ite].x, plantas[ite].y).x;
			int indiceY = cuadricula.cercanoL(plantas[ite].x, plantas[ite].y ).y;
			if (indiceY < 4 && !cuadricula.ocupado[indiceX][indiceY+1]) {
				this.cuadricula.ocupado [indiceX][indiceY] = false;
				plantas[ite].x = cuadricula.coorX[indiceX]; 
	            plantas[ite].y = cuadricula.coorY[indiceY + 1];
				this.cuadricula.ocupado [indiceX][indiceY+1] = true;
				break;
			}
		}
	}
	for (int ite = 0; ite < this.plantas.length; ite++) {
		if (this.plantas[ite] != null && this.plantas[ite].seleccionada && this.plantas[ite].plantada && entorno.sePresiono(entorno.TECLA_DERECHA)) {
			int indiceX = cuadricula.cercanoL(plantas[ite].x, plantas[ite].y).x;
			int indiceY = cuadricula.cercanoL(plantas[ite].x, plantas[ite].y ).y;
			if (indiceX < 9 && cuadricula.ocupado[indiceX+1][indiceY]) {
				this.cuadricula.ocupado [indiceX][indiceY] = false;
				plantas[ite].x = cuadricula.coorX[indiceX + 1]; 
	            plantas[ite].y = cuadricula.coorY[indiceY];
				this.cuadricula.ocupado [indiceX+1][indiceY] = true;
				break;
			}
		}
	}
	for (int ite = 0; ite < this.plantas.length; ite++) {
		if (this.plantas[ite] != null && this.plantas[ite].seleccionada && this.plantas[ite].plantada && entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
			int indiceX = cuadricula.cercanoL(plantas[ite].x, plantas[ite].y).x;
			int indiceY = cuadricula.cercanoL(plantas[ite].x, plantas[ite].y ).y;
			if (indiceY > 1 && !cuadricula.ocupado[indiceX-1][indiceY]) {
				this.cuadricula.ocupado [indiceX][indiceY] = false;
				plantas[ite].x = cuadricula.coorX[indiceX - 1]; 
	            plantas[ite].y = cuadricula.coorY[indiceY];
				this.cuadricula.ocupado [indiceX-1][indiceY] = true;
				break;
			}
		}
	}
	
	if (!plantasNoPlantadas(this.plantas)) {
		crearPlanta(this.plantas);
			}
		}
	
private void crearPlanta(planta[] pl) {
	for(int x=0 ; x < pl.length; x++) {
		if (pl[x] == null) {
			pl[x] = new planta(50,40,entorno);
			return;
		}
	}
}



		// Procesamiento de un instante de tiempo
		// ...
		
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
	public boolean plantasNoPlantadas(planta[]  pl) {
		for (planta p:pl) {
			if (p != null && !p.plantada) {
				return true;
				
			}
			
		}
		return false;
	}
}
