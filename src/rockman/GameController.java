package rockman;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageProducer;
import java.net.URL;






public class GameController implements KeyListener, Runnable {
	static boolean paint_flag = true;	//描画処理が必要か実験
	static int move_speed = 5;	//ロックマンの移動スピード
	static int gravity = 5;
	static int Game_over_point_y = (Stage.Stage_1.length-1) * Game.TILESIZE -1;
	static int Game_clear_point_x = (Stage.Stage_1[0].length-1) * Game.TILESIZE -1;
	static int start_base_x = -10;
	static int start_base_y = -470;
	static int start_player_x = 350;
	static int start_player_y = 620;
	private int jump_power = 13;
	private int left_or_right = 0;	//0押されていない、-１左、1右
	private int top_or_bottom = 0; //0押されていない -1上(zボタン）
	private boolean atk_btn = false;	//攻撃ボタン（ｘキー）

	private player rockman;
	private Enemy[] enemys = new Enemy[20];
	private Image[] rockman_imgs = new Image[12];
	private Image[] enemy_imgs = new Image[2];
	Stage st;

	public GameController() {
		// TODO Auto-generated constructor stub
		st = new Stage();
		Stage.stage.frame.addKeyListener(this); //キーリスナーツィてフレームに追加
		Stage.stage.frame.setVisible(true);

		try {
			//playerの画像の読み込み
			//攻撃１
			URL url_0=this.getClass().getResource("img/zero/atk_1.png");
			rockman_imgs[0] = st.createImage((ImageProducer) url_0.getContent());
			//攻撃２
			URL url_1=this.getClass().getResource("img/zero/atk_2.png");
			rockman_imgs[1] = st.createImage((ImageProducer) url_1.getContent());
			//攻撃３
			URL url_2 = this.getClass().getResource("img/zero/atk_3.png");
			rockman_imgs[2] = st.createImage((ImageProducer) url_2.getContent());
			//攻撃４
			URL url_3 = this.getClass().getResource("img/zero/atk_4.png");
			rockman_imgs[3] = st.createImage((ImageProducer) url_3.getContent());
			//ダッシュ
			URL url_4 = this.getClass().getResource("img/zero/dash.png");
			rockman_imgs[4] = st.createImage((ImageProducer) url_4.getContent());
			//ダメージ
			URL url_5=this.getClass().getResource("img/zero/dmg.png");
			rockman_imgs[5] = st.createImage((ImageProducer) url_5.getContent());
			//ジャンプ攻撃
			URL url_6 = this.getClass().getResource("img/zero/jmp_atk.png");
			rockman_imgs[6] = st.createImage((ImageProducer) url_6.getContent());
			//ジャンプ
			URL url_7 = this.getClass().getResource("img/zero/jmp.png");
			rockman_imgs[7] = st.createImage((ImageProducer) url_7.getContent());
			//歩き１
			URL url_8 = this.getClass().getResource("img/zero/move_1.png");
			rockman_imgs[8] = st.createImage((ImageProducer) url_8.getContent());
			//歩き２
			URL url_9 = this.getClass().getResource("img/zero/move_2.png");
			rockman_imgs[9] = st.createImage((ImageProducer) url_9.getContent());
			//歩き３
			URL url_10 = this.getClass().getResource("img/zero/move_3.png");
			rockman_imgs[10] = st.createImage((ImageProducer) url_10.getContent());
			//待機
			URL url_11 = this.getClass().getResource("img/zero/zero.png");
			rockman_imgs[11] = st.createImage((ImageProducer) url_11.getContent());

			//敵
			URL url_12 = this.getClass().getResource("img/enemy_1/enemy_1.png");
			enemy_imgs[0] = st.createImage((ImageProducer) url_12.getContent());
			URL url_13 = this.getClass().getResource("img/enemy_1/enemy_2.png");
			enemy_imgs[1] = st.createImage((ImageProducer) url_13.getContent());

		}	catch(Exception ex) {
			System.out.println("Resource Error!");
			System.out.println(ex.toString());
		}

		rockman = new player(rockman_imgs,start_player_x, start_player_y);
		st.set_player(rockman);

		//敵配置
		enemys[0] = new Enemy(enemy_imgs, 1100, 650, rockman);
		enemys[1] = new Enemy(enemy_imgs, 2000, 600, rockman);
		enemys[2] = new Enemy(enemy_imgs, 3000, 670, rockman);
		enemys[3] = new Enemy(enemy_imgs, 4420, 750, rockman);
		enemys[4] = new Enemy(enemy_imgs, 1400, 200, rockman);
		enemys[5] = new Enemy(enemy_imgs, 7400, 370, rockman);
		enemys[6] = new Enemy(enemy_imgs, 8220, 500, rockman);
		enemys[7] = new Enemy(enemy_imgs, 9600, 550, rockman);
		enemys[8] = new Enemy(enemy_imgs, 10000, 550, rockman);
		enemys[9] = new Enemy(enemy_imgs, 11500, 700, rockman);
		enemys[10] = new Enemy(enemy_imgs, 13000, 700, rockman);
		enemys[11] = new Enemy(enemy_imgs, 15210, 770, rockman);
		enemys[12] = new Enemy(enemy_imgs, 14800, 770, rockman);
		enemys[13] = new Enemy(enemy_imgs, 17000, 500, rockman);
		enemys[14] = new Enemy(enemy_imgs, 18600, 860, rockman);
		enemys[15] = new Enemy(enemy_imgs, 21220, 300, rockman);
		enemys[16] = new Enemy(enemy_imgs, 21600, 300, rockman);
		enemys[17] = new Enemy(enemy_imgs, 22000, 750, rockman);
		enemys[18] = new Enemy(enemy_imgs, 22300, 500, rockman);
		enemys[19] = new Enemy(enemy_imgs, 23300, 720, rockman);
		st.set_enemy(enemys);

		(new Thread(this)).start(); //ゲーム開始
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Stage.stage.repaint(); //ステージの再描画
		synchronized (this) {
			while (true) { //ゲーム終了までループ
				while(rockman.check_game_over()) {
					try {
						this.move_act(left_or_right, top_or_bottom);
						rockman.damage();	//ダメージチェック；
						Thread.sleep(10);
						//this.wait(); //ゲーム状況に変化があるまで待機
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("reset");
				//敵配置
				enemys[0] = new Enemy(enemy_imgs, 1100, 650, rockman);
				enemys[1] = new Enemy(enemy_imgs, 2000, 600, rockman);
				enemys[2] = new Enemy(enemy_imgs, 3000, 670, rockman);
				enemys[3] = new Enemy(enemy_imgs, 4420, 750, rockman);
				enemys[4] = new Enemy(enemy_imgs, 1400, 200, rockman);
				enemys[5] = new Enemy(enemy_imgs, 7400, 370, rockman);
				enemys[6] = new Enemy(enemy_imgs, 8220, 500, rockman);
				enemys[7] = new Enemy(enemy_imgs, 9600, 550, rockman);
				enemys[8] = new Enemy(enemy_imgs, 10000, 550, rockman);
				enemys[9] = new Enemy(enemy_imgs, 11500, 700, rockman);
				enemys[10] = new Enemy(enemy_imgs, 13000, 700, rockman);
				enemys[11] = new Enemy(enemy_imgs, 15210, 770, rockman);
				enemys[12] = new Enemy(enemy_imgs, 14800, 770, rockman);
				enemys[13] = new Enemy(enemy_imgs, 17000, 500, rockman);
				enemys[14] = new Enemy(enemy_imgs, 18600, 860, rockman);
				enemys[15] = new Enemy(enemy_imgs, 21220, 300, rockman);
				enemys[16] = new Enemy(enemy_imgs, 21600, 300, rockman);
				enemys[17] = new Enemy(enemy_imgs, 22000, 750, rockman);
				enemys[18] = new Enemy(enemy_imgs, 22300, 500, rockman);
				enemys[19] = new Enemy(enemy_imgs, 23300, 720, rockman);
				st.set_enemy(enemys);

				//落ちたとこに合わせてチェックポイント
				if (rockman.player_x >= 19005) {
					GameController.start_base_x =  -18665;
					GameController.start_base_y = -431;
					GameController.start_player_x = 19005;
					GameController.start_player_y = 817;
				} else if (rockman.player_x >= 15600) {
					GameController.start_base_x =  -15260;
					GameController.start_base_y = -332;
					GameController.start_player_x = 15600;
					GameController.start_player_y = 676;
				} else if (rockman.player_x >= 10620) {
					GameController.start_base_x =  -10280;
					GameController.start_base_y = -479;
					GameController.start_player_x = 10620;
					GameController.start_player_y = 673;
				} else if (rockman.player_x >= 5520) {
					GameController.start_base_x =  -4876;
					GameController.start_base_y = -281;
					GameController.start_player_x = 5220;
					GameController.start_player_y = 576;
				}
				Stage.baseX = GameController.start_base_x;
				Stage.baseY = GameController.start_base_y;
				rockman.player_x = GameController.start_player_x;
				rockman.player_y = GameController.start_player_y;
				try {
				Thread.sleep(1000);
				} catch (Exception e) {};
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int move_x = 0, move_y = 0;

		if (rockman.can_jump == true) {	//キーを離すと待機画像
			rockman.now_image = rockman.images[11];
		}

		switch (e.getKeyCode()) {
		//ここにキー処理を書く
		case KeyEvent.VK_Z:
			top_or_bottom = -1;
			break;
		case KeyEvent.VK_X :
			atk_btn = true;
			break;
		case KeyEvent.VK_RIGHT :
			left_or_right = 1;
			break;
		case KeyEvent.VK_LEFT :
			left_or_right = -1;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		//ここにキー処理を書く
		case KeyEvent.VK_Z :
			top_or_bottom = 0;
			break;
		case KeyEvent.VK_X:
			atk_btn = false;
			break;
		case KeyEvent.VK_RIGHT :
			left_or_right = 0;
			break;
		case KeyEvent.VK_LEFT :
			left_or_right = 0;
			break;
		}
	}


	//移動部分のメソッド
	public void move_act(int left_or_right, int top_or_bottom){ //左-1右1、上-1下１
		int move_x = 0, move_y = 0;

		//移動処理
		if (left_or_right == -1) {	//左
			move_x = -move_speed;
			move_x = rockman.player_run(move_x);
		} else if (left_or_right == 1) {  //右
			move_x = move_speed;
			move_x = rockman.player_run(move_x);
		}

		if (top_or_bottom == -1) {	//上
			if (rockman.can_jump == true) {
				rockman.is_jumped = true;
			}
		} else if (top_or_bottom == 1) {	//下
			//move_y = move_speed;
		}

		//ジャンプ処理
		if (rockman.is_jumped == true) {
			move_y = rockman.player_jump(jump_power);
		}

		//重力チェック
		if (rockman.grav_force(move_x)) {
			move_y = gravity;
		}

		//攻撃処理
		if (atk_btn) {
			rockman.atk();
		}
		rockman.change_atk_img();//攻撃画像処理

		//ロックマンをずらす
		rockman.player_y += move_y;

		//マップ全体をずらす
		Stage.baseX -= move_x;
		Stage.baseY -= move_y;
		//描画範囲をずらす
		//Stage.write_point_x += move_x;
		//Stage.write_point_y += move_y;
		//左移動限界チェック
		if (Stage.baseX>=0) {
			Stage.baseX += move_x;
			//Stage.write_point_x -= move_x;
		}
		//右移動限界
		if (Stage.baseX<= -1*Game.TILESIZE*(Stage.Stage_1[0].length-Stage.PANEL_W_NUM -1)){
			Stage.baseX += move_x;
		}
		//下移動限界チェック
		if (Stage.baseY<= -1*Game.TILESIZE*(Stage.Stage_1.length - Stage.PANEL_H_NUM -1)){
			Stage.baseY += move_y;
			//Stage.write_point_y -= move_y;
		}
		//上移動限界チェック
		if (Stage.baseY>=0) {
			Stage.baseY += move_y;
			//Stage.write_point_y -= move_y;
		}

		//write_pointの調整
		Stage.write_point_x = -1* (Stage.baseX/Game.TILESIZE)+1;
		Stage.write_point_y = -1* (Stage.baseY/Game.TILESIZE)+1;

		//マップ再描画処理
		//System.out.println("baseX:"+Stage.baseX+"; baseY:"+Stage.baseY+"; w_p_x:"+Stage.write_point_x+"; w_p_y:"+Stage.write_point_y);
		Stage.stage.repaint();
	}
}
