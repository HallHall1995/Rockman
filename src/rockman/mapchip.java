package rockman;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
//import java.util.ArrayList;

//import minigame.Actor;

public class mapchip {

	public Image img;	//Actorの見た目を保持
	protected int posX, posY;	//map_chipの位置(パネル数で）

	public mapchip(Image img, int posY, int posX) {
		this.img = img;
		this.posX = posX;
		this.posY = posY;
	}

	public void paintActors(Graphics g, ImageObserver io) {
		g.drawImage(img, this.posX * Game.TILESIZE, this.posY * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE, io);
		//System.out.println("img="+img+";  posX="+posX*Game.TILESIZE+";  posY="+posY*Game.TILESIZE);
	}

}
