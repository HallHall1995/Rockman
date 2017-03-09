package rockman;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Random;

public class Enemy{
	private Image[] images = new Image[2];	//Actorの見た目を保持
	public Image now_img;
	protected int posX, posY;	//Acctorの現在の位置(配列上）
	public int enemy_x, enemy_y;
	private int gosa = 5;
	private int enemy_speed = 10;

	private player player;

	private int draw_width = 300;

	public Enemy(Image[] image, int enemy_x, int enemy_y, player player) {
		this.images = image;
		this.now_img = images[0];
		this.enemy_x = enemy_x;
		this.enemy_y = enemy_y;
		this.player = player;
	}

	public void paintEnemys(Graphics g, ImageObserver io) {
		enemy_move();
		g.drawImage(now_img, enemy_x, enemy_y,  Game.TILESIZE, Game.TILESIZE, io);
	}

	//位置情報からActorを取得するメソッド
	/*static public Actor existsAt(int x, int y) {
		synchronized (Actor.actors) {
			for (Actor actor : Actor.actors) {
				if ((actor.posX == x) && (actor.posY == y)) {
					return actor;
				}
			}
			return null;
		}
	}*/

	//横移動
	private void enemy_move() {
		int vx = new Random().nextInt(3) -1;
		if (vx < 0) {
			set_pos(3);
			if (Stage.Stage_1[posY][posX] == 0) {
				this.enemy_x += vx;
			}
		}
		if (vx > 0) {
			set_pos(1);
			if (Stage.Stage_1[posY][posX] == 0) {
				this.enemy_x += vx;
			}
		}
	}

	//posXとposYの計算
	private void set_pos(int check_num) {	//０なら下（中心の）1ならば右上、２右下、３左上、４左下
		if (check_num == 0) {
			posX = (enemy_x+(Game.TILESIZE/2))/Game.TILESIZE;
			posY = (enemy_y+Game.TILESIZE)/Game.TILESIZE;
		} else if (check_num == 1) {	//右上
			posX = (enemy_x+Game.TILESIZE -gosa)/Game.TILESIZE;
			posY = enemy_y/Game.TILESIZE;
		} else if (check_num == 2) {	//右下
			posX = (enemy_x+Game.TILESIZE)/Game.TILESIZE;
			posY = (enemy_y+Game.TILESIZE - gosa)/Game.TILESIZE;
		} else if (check_num == 3) {	//左上
			posX = (enemy_x +gosa)/Game.TILESIZE;
			posY = enemy_y/Game.TILESIZE;
		} else if (check_num == 4) {	//左下
			posX = enemy_x/Game.TILESIZE;
			posY = (enemy_y+Game.TILESIZE - gosa)/Game.TILESIZE;
		}
	}

	public void test() {
		System.out.println("rre");
	}
}
