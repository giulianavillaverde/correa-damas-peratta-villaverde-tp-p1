package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Banner {
	double x,y,escala;
	Image imagen;
	Entorno e;
public Banner (Entorno e) {
	this.x = -5;
	this.y = 28;
	this.imagen = Herramientas.cargarImagen("Bannerrr.jpg");
	this.escala = 3;
	this.e = e;
	
	
}
public void dibujar () {
	//double anchoImg = this.imagen.getWidth(null) * this.escala;
	//int repeticiones = (int) Math.ceil(e.ancho()/ anchoImg) +2;
	//for (int i = 0; i< repeticiones; i++) {
		e.dibujarImagen(imagen, x , y,0.0, escala);
		
	//}
	
	//e.cambiarFont("arial", 24, Color.pink);
	//e.escribirTexto(e.mouseX() + " " + e.mouseY(), 300,40);
	}
}

