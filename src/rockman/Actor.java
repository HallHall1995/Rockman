package rockman;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Actor{
	private Image image;	//Actorの見た目を保持
	protected int posX, posY;	//Acctorの現在の位置
	protected boolean isMovale; //動かせる物かどうか（isMovableですよ m9(＾ω＾)）
	protected boolean isEdible; //食べられるかどうか
	static ArrayList<Actor> actors = new ArrayList<Actor>();	//全てのActorを保持

	public Actor(Image image, int posX, int posY, boolean isMovale, boolean isEdible) {
		this.image = image;
		this.posX = posX;
		this.posY = posY;
		this.isMovale = isMovale;
		this.isEdible = isEdible;
		Actor.actors.add(this);	//actorsリストに自分自身を登録
	}

	static public void paintActors(Graphics g, ImageObserver io) {
		synchronized (Actor.actors) {
			for (Actor actor : Actor.actors) {
				g.drawImage(actor.image, actor.posX * Game.TILESIZE, actor.posY * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE, io);
			}
		}
	}

	//位置情報からActorを取得するメソッド
	static public Actor existsAt(int x, int y) {
		synchronized (Actor.actors) {
			for (Actor actor : Actor.actors) {
				if ((actor.posX == x) && (actor.posY == y)) {
					return actor;
				}
			}
			return null;
		}
	}

	//押されるメソッド
	public boolean pushed(int x, int y) {
		if (this.isMovale) {
			synchronized(Actor.actors) {
				if (existsAt(this.posX+x, this.posY+y) == null){
					this.posX += x; this.posY += y;
				}
			}
			return true;
		}
		return false;
	}

	//食べられる動作をするメソッド
	public void eaten() {
		if (this.isEdible) {
			synchronized (Actor.actors) {
				actors.remove(this);
			}
		}
	}
}
