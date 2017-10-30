package linkup.speed.Forms;

import linkup.speed.util.*;
import linkup.speed.Algorithm.*;
import java.util.Random;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import linkup.speed.MainClass.*;

public class MainForm extends BorderPane {
	public Scene scene;
	public Grid grid;
	public MediaPlayer mediaPlayer;
	public MediaPlayer mediaPlayer20s;
	public MediaPlayer m_button;
	public MediaPlayer m_choosefirst;
	public MediaPlayer m_clear;
	public MediaPlayer m_gameover;
	public MediaPlayer m_rechoose;
	
	public MainForm(Stage stage, Main main, Grid grid1) {
		grid = grid1;
		
		//背景音乐
		Random random = new Random();
		int music_num = random.nextInt(5) + 1;
		Media media = new Media(this.getClass().getResource("music/music-bg" + music_num + ".mp3").toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		
		//倒计时提示音效
		Media media20s = new Media(this.getClass().getResource("music/20s.mp3").toString());
		mediaPlayer20s = new MediaPlayer(media20s);
		mediaPlayer20s.setCycleCount(Timeline.INDEFINITE);
		
		//按钮音效
		m_button = new MediaPlayer(new Media(this.getClass().getResource("music/button1.mp3").toString()));
		m_button.setCycleCount(1);
		
		//选择第一个节点音效
		m_choosefirst = new MediaPlayer(new Media(this.getClass().getResource("music/choosefirst.mp3").toString()));
		m_choosefirst.setCycleCount(1);
		
		//清除音效
		m_clear = new MediaPlayer(new Media(this.getClass().getResource("music/clear.mp3").toString()));
		m_clear.setCycleCount(1);
		
		//游戏结束音效
		m_gameover = new MediaPlayer(new Media(this.getClass().getResource("music/gameover.mp3").toString()));
		m_gameover.setCycleCount(1);
		
		//取消选择音效
		m_rechoose = new MediaPlayer(new Media(this.getClass().getResource("music/rechoose.mp3").toString()));
		m_rechoose.setCycleCount(1);
		
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.BOTTOM_CENTER);
		VBox vBox = new VBox();
		vBox.setSpacing(50);
		vBox.setAlignment(Pos.CENTER);
		
		Label l = new Label("动  漫  连  连  看");
		l.setId("l");
		hBox.getChildren().add(l);
		
		Button bt1 = new Button("开始游戏");
		bt1.setPrefSize(300, 40);
		Button bt2 = new Button("设置");
		bt2.setPrefSize(300, 40);
		Button bt3 = new Button("关于游戏");
		bt3.setPrefSize(300, 40);
		Button bt4 = new Button("退出游戏");
		bt4.setPrefSize(300, 40);
		vBox.getChildren().addAll(bt1, bt2, bt3, bt4);
		
		this.setId("this");
		this.setTop(hBox);
		this.setCenter(vBox);
		
		scene = new Scene(this, 910, 625);
		scene.getStylesheets().add(this.getClass().getResource("/res/css/MainForm.css").toString());

		bt1.setOnMouseClicked(e -> {
			m_button.stop();
			m_button.play();
			mediaPlayer.play();
			grid.promptleft.setValue(Utils.PROMPTLEFT);
			grid.level.set("Level: " + grid.num);
			if(grid.t != null) {
				grid.t.interrupt();
			}
			grid.time.setValue(Utils.TIMELEFT);
			grid.num = 1;
			grid.level.setValue("Level:" + 1);
			grid.count = 0;
			grid.init();
			if(!grid.prompt()) {
				grid.refresh();
			}
			stage.setScene(main.gameForm.scene);
		});
		bt2.setOnMouseClicked(e -> {
			m_button.stop();
			m_button.play();
			grid.flag = 2;
			stage.setScene(main.settingForm.scene);
		});
		bt3.setOnMouseClicked(e -> {
			m_button.stop();
			m_button.play();
			stage.setScene(main.aboutForm.scene);
		});
		bt4.setOnMouseClicked(e -> {
			m_button.stop();
			m_button.play();
			main.exit.stage.show();
		});
	}
}
