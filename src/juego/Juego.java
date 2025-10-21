package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	Entorno entorno;
	Banner banner;
	cuadricula cuadricula;
	regalos[] regalo;
	planta [] plantas;
	
	// Variables y métodos propios de cada grupo
	// ...
	
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
		this.plantas = new planta[15];
		this.plantas[0]= new planta(50,40,entorno);
		
		
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
			
				if(plantas[0].encima(entorno.mouseX(), entorno.mouseY())) {
					plantas[0].seleccionada = true;
				
				
			}else{
				plantas[0].seleccionada = false;
			
			
		}
		if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
			
		
			if (plantas[0].seleccionada){
				
				plantas[0].arrastrar(entorno.mouseX(), entorno.mouseY());
				}
			}
		}
		if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
			for (int ite=0; ite < this.plantas.length; ite++) {
				if (this.plantas[ite] != null) {
					if (entorno.mouseY() < 70 && !plantas[ite].plantada) {
						plantas[0].arrastrar(50,40);
					
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
	if (this.plantas[0].seleccionada && this.plantas[0].plantada && entorno.sePresiono(entorno.TECLA_ARRIBA)) {
		int indiceX = cuadricula.cercanoL(plantas[0].x, plantas[0].y).x;
		int indiceY = cuadricula.cercanoL(plantas[0].x, plantas[0].y ).y;
		if (indiceY >= 1 && cuadricula.ocupado[indiceX][indiceY-1]) {
			this.cuadricula.ocupado [indiceX][indiceY] = false;
			this.cuadricula.ocupado [indiceX][indiceY-1] = true;
			this.plantas[0].y -= 100;
			
		}
	}
	
	if (this.plantas[0].seleccionada && this.plantas[0].plantada && entorno.sePresiono(entorno.TECLA_ABAJO)) {
		int indiceX = cuadricula.cercanoL(plantas[0].x, plantas[0].y).x;
		int indiceY = cuadricula.cercanoL(plantas[0].x, plantas[0].y ).y;
		if (indiceY <= 3 && cuadricula.ocupado[indiceX][indiceY+1]) {
			this.cuadricula.ocupado [indiceX][indiceY] = false;
			this.cuadricula.ocupado [indiceX][indiceY+1] = true;
			this.plantas[0].y += 100;
		}
	}
	if (this.plantas[0].seleccionada && this.plantas[0].plantada && entorno.sePresiono(entorno.TECLA_DERECHA)) {
		int indiceX = cuadricula.cercanoL(plantas[0].x, plantas[0].y).x;
		int indiceY = cuadricula.cercanoL(plantas[0].x, plantas[0].y ).y;
		if (indiceX <= 6 && cuadricula.ocupado[indiceX+1][indiceY]) {
			this.cuadricula.ocupado [indiceX][indiceY] = false;
			this.cuadricula.ocupado [indiceX+1][indiceY] = true;
			this.plantas[0].x += 100;
		}
	}
	
	if (this.plantas[0].seleccionada && this.plantas[0].plantada && entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
		int indiceX = cuadricula.cercanoL(plantas[0].x, plantas[0].y).x;
		int indiceY = cuadricula.cercanoL(plantas[0].x, plantas[0].y ).y;
		if (indiceY > 1 && cuadricula.ocupado[indiceX-1][indiceY]) {
			this.cuadricula.ocupado [indiceX][indiceY] = false;
			this.cuadricula.ocupado [indiceX-1][indiceY] = true;
			this.plantas[0].y -= 100;
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
