package rockman;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class player {
	public Image[] images = new Image[12];	//Actorの見た目を保持
	public Image now_image;
	public int posX, posY;	//Acctorの現在の位置(配列上での位置）
	public int player_x, player_y; //playerの位置（ゲーム上)
	public boolean can_jump=false;
	public boolean is_jumped = false;
	public boolean is_damage = false;
	public boolean is_atk = false;
	public int atk_count = 0;
	private int damage_count = 0;
	private int damage_count_time = 2000; //硬直時間(ただ今反応なし）
	private int MaxHP = 50;
	private int HP = MaxHP;
	public int left_or_right = 1; //向いている方向、右なら1左は-1
	private int jump_count=0;
	private int jump_time = 22; //ジャンプする時間
	private int run_img_count = 80; //80~100;

	//攻撃範囲
	private int atk_x_area = Game.TILESIZE;

	private int gosa = 5; //当たり判定の誤差

	static player player; //playerインスタンスを保持


	public player(Image[] img, int player_x, int player_y) {
		// TODO Auto-generated constructor stub
		this.images = img;
		this.now_image = img[11];
		this.player_x = player_x;
		this.player_y = player_y;
	}

	public void paintActors(Graphics g, ImageObserver io) {
		System.out.println("x="+this.player_x+"; y="+this.player_y+ "; Base_X="+Stage.baseX+"; Base_Y="+Stage.baseY);
		if (left_or_right == 1) {
			g.drawImage(this.now_image, player_x, player_y, Game.TILESIZE, Game.TILESIZE, io);
		} else {
			g.drawImage(this.now_image, player_x + Game.TILESIZE, player_y, -Game.TILESIZE, Game.TILESIZE, io);
		}
	}

	//posXとposYの計算
	private void set_pos(int check_num) {	//０なら下（中心の）1ならば右上、２右下、３左上、４左下
		if (check_num == 0) {
			posX = (player_x+(Game.TILESIZE/2))/Game.TILESIZE;
			posY = (player_y+Game.TILESIZE)/Game.TILESIZE;
		} else if (check_num == 1) {	//右上
			posX = (player_x+Game.TILESIZE -gosa)/Game.TILESIZE;
			posY = player_y/Game.TILESIZE;
		} else if (check_num == 2) {	//右下
			posX = (player_x+Game.TILESIZE)/Game.TILESIZE;
			posY = (player_y+Game.TILESIZE - gosa)/Game.TILESIZE;
		} else if (check_num == 3) {	//左上
			posX = (player_x +gosa)/Game.TILESIZE;
			posY = player_y/Game.TILESIZE;
		} else if (check_num == 4) {	//左下
			posX = player_x/Game.TILESIZE;
			posY = (player_y+Game.TILESIZE - gosa)/Game.TILESIZE;
		}
	}

	//重力処理
	public boolean grav_force(int move_x) {
		if (!is_damage){	//硬直時間は下に落ちない
			set_pos(0);
			if (!(Stage.Stage_1[this.posY][this.posX] == 0)) {	//地面ならば
				can_jump = true;
				is_jumped = false;
				jump_count = 0;
				if (move_x==0) now_image = images[11];
				return false;
			} else {	//空中ならば
				can_jump = false;
				now_image = images[7];
				return true;
			}
		}
		return false;
	}


	public int player_jump(int move_y) {
		if (!is_damage) {	//硬直時間は動けない
			if (jump_count <= jump_time) {
				set_pos(1);
				if(Stage.Stage_1[this.posY][this.posX] == 0) {
					set_pos(3);
					if(Stage.Stage_1[this.posY][this.posX] == 0) {
						this.player_y -= move_y;
						Stage.baseY += move_y;
						if (Stage.baseY>=0) { //画面外にスクロールしないように
							Stage.baseY -= move_y;
						}
						jump_count ++;
						return move_y;
					}
				}
			}
			jump_count ++;
		}
		return 0;
	}


	public int player_run(int move_x) {
		if (!is_damage) {	//硬直時間は動けない
			if (move_x<0) {	//左移動チェック
				set_pos(1);
				if(Stage.Stage_1[this.posY][this.posX-1] == 0) {
					set_pos(2);
					if(Stage.Stage_1[this.posY][this.posX-1] == 0) {
						this.player_x += move_x;
						change_run_img();
						left_or_right = -1;
						return move_x;
					}
				}
			}
			if (move_x>0) {	//右移動チェック
				set_pos(3);
				if(Stage.Stage_1[this.posY][this.posX+1] == 0) {
					set_pos(4);
					if(Stage.Stage_1[this.posY][this.posX+1] == 0) {
						this.player_x += move_x;
						change_run_img();
						left_or_right = 1;
						return move_x;
					}
				}
			}
		}
		return 0;
	}

	private void change_run_img(){
		this.now_image = this.images[(int)run_img_count/10];
		run_img_count++;
		if (run_img_count >= 110) run_img_count = 80;
	}


	//ダメージ動作
	public void damage() {
		check_damage();
		if (is_damage) {
			this.now_image = this.images[5];
			this.damage_count ++;
			if (damage_count == this.damage_count) {
				this.is_damage = false;
				this.damage_count = 0;
				HP --;
				if (HP <= 0) {
					try {
					Thread.sleep(1000);
					HP = MaxHP;
					System.out.println("reset");
					//敵配置
					Stage.baseX = GameController.start_base_x;
					Stage.baseY = GameController.start_base_y;
					this.player_x = GameController.start_player_x;
					this.player_y = GameController.start_player_y;
					} catch (Exception e) {};
				}
			}
		}
	}

	//ダメージ判定	ダメージを食らうときはis_damage=true
	public void check_damage() {
		if (!is_damage) {
			for (int i=0; i<Stage.stage.enemys.length; i++){
				Enemy e = Stage.stage.enemys[i];
					if (!(e == null)){
					//判定
					if (Math.abs(this.player_x - e.enemy_x) < Game.TILESIZE){ //自分と敵のx位置の差が一パネル以内
						if (Math.abs(this.player_y - e.enemy_y) < Game.TILESIZE){ //自分と敵のy位置の差が一パネル以内
							is_damage = true;
						}
					}
				}
			}
		}
	}

	//攻撃判定
	public void atk() {
		if (!is_atk) {
			is_atk = true;
			for (int i=0; i<Stage.stage.enemys.length; i++){
				Enemy e = Stage.stage.enemys[i];
				if (!(e == null)) {
					if (left_or_right == 1) { //判定
						//右判定
						if (Math.abs(this.player_x+this.atk_x_area+Game.TILESIZE - e.enemy_x) < Game.TILESIZE*1.5){ //自分と敵のx位置の差が一パネル以内
							if (Math.abs(this.player_y - e.enemy_y) < Game.TILESIZE*2){ //自分と敵のy位置の差が一パネル以内
								Stage.stage.enemys[i] = null;
							}
						}
					} else if (left_or_right == -1) {
						//左判定
						if (Math.abs(this.player_x-this.atk_x_area - e.enemy_x+Game.TILESIZE) < Game.TILESIZE*1.5){ //自分と敵のx位置の差が一パネル以内
							if (Math.abs(this.player_y - e.enemy_y) < Game.TILESIZE*2){ //自分と敵のy位置の差が一パネル以内
								Stage.stage.enemys[i] = null;
							}
						}
					}
				}
			}
		}
	}

	//攻撃画像に変化
	public void change_atk_img(){
		if (is_atk) {
			this.now_image = this.images[(int)atk_count/10];
			atk_count ++;
			if (atk_count >= 40) {
				atk_count = 0;
				is_atk = false;
			}
		}
	}



	//ゲームオーバーチェック
	public boolean check_game_over() {
		//System.out.println(player_y);
		//System.out.println(GameController.Game_over_point_y);
		if (this.player_y >= GameController.Game_over_point_y) {
			return false;
		}
		if (this.player_x >= GameController.Game_clear_point_x) {
			return false;
		}
		return true;
	}

}
