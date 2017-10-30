package linkup.speed.Forms;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import linkup.speed.Algorithm.Grid;
import linkup.speed.MainClass.Main;
import linkup.speed.util.*;

public class GameForm extends BorderPane {
	public Scene scene;
	public Main main;
	public Grid grid;
	public boolean flag;
	private static Line line = new Line(-100, 170, Utils.GAMEWIDTH/2, 170);
	
	public GameForm(Stage stage, Main main1, Grid grid1) {
		grid = grid1;
		main = main1;
		grid.setId("grid");
		HBox hBox = new HBox();
		hBox.setId("hBox");
		hBox.setPrefSize(910, 40);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setSpacing(40);
		HBox h1 = new HBox();
		h1.setAlignment(Pos.CENTER_LEFT);
		h1.setPrefSize(450, 40);
		
		Button bt1 = new Button("重新开始");
		bt1.setPrefSize(80, 30);
		Button bt2 = new Button("暂停");
		bt2.setPrefSize(80, 30);
		Button bt3 = new Button("提示");
		bt3.setPrefSize(80, 30);
		Label l = new Label("生命：");
		Text t1 = new Text("提示:5");
		h1.getChildren().addAll(l, grid.p);
		hBox.getChildren().addAll(bt1, bt2, bt3, t1, h1);
		
		//关卡提示
		Text text = new Text();
		text.setId("text");
		text.textProperty().bind(grid.level);
		text.setLayoutX(-100);
		text.setLayoutY(-100);

		//消除响应线段一
		Line line1 = new Line();
		line1.startXProperty().bind(grid.X1);
		line1.startYProperty().bind(grid.Y1);
		line1.endXProperty().bind(grid.X2);
		line1.endYProperty().bind(grid.Y2);
		line1.setStroke(Color.RED);
		line1.setStrokeWidth(3);
		
		//消除响应线段二
		Line line2 = new Line();
		line2.startXProperty().bind(grid.X2);
		line2.startYProperty().bind(grid.Y2);
		line2.endXProperty().bind(grid.X3);
		line2.endYProperty().bind(grid.Y3);
		line2.setStroke(Color.RED);
		line2.setStrokeWidth(3);
		
		//消除响应线段三
		Line line3 = new Line();
		line3.startXProperty().bind(grid.X3);
		line3.startYProperty().bind(grid.Y3);
		line3.endXProperty().bind(grid.X4);
		line3.endYProperty().bind(grid.Y4);
		line3.setStroke(Color.RED);
		line3.setStrokeWidth(3);
		
		this.setTop(hBox);
		this.setCenter(main.grid);
		this.getChildren().addAll(line1, line2, line3, text);
		
		//消除线段响应动画
		FadeTransition ft1 = new FadeTransition(Duration.millis(500), line1);
		ft1.setFromValue(1);
		ft1.setToValue(0);
		ft1.setCycleCount(1);
		FadeTransition ft2 = new FadeTransition(Duration.millis(500), line2);
		ft2.setFromValue(1);
		ft2.setToValue(0);
		ft2.setCycleCount(1);
		FadeTransition ft3 = new FadeTransition(Duration.millis(500), line3);
		ft3.setFromValue(1);
		ft3.setToValue(0);
		ft3.setCycleCount(1);
		
		//关卡响应动画
		PathTransition pt = new PathTransition(Duration.millis(500), line, text);
		pt.setCycleCount(1);
		
		text.textProperty().addListener(e -> {
		        	pt.play();
		        });
		
		grid.X4.addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
        	if(oldVal != newVal) {
        		flag = true;
        	}
        	ft1.stop();
        	ft2.stop();
        	ft3.stop();
        	ft1.play();
        	ft2.play();
        	ft3.play();
        });
		flag = false;
		if(!flag)
		grid.Y4.addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
        	ft1.stop();
        	ft2.stop();
        	ft3.stop();
        	ft1.play();
        	ft2.play();
        	ft3.play();
        });
		
		grid.time.addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {			
			if(newVal.intValue() <= 0) {
				main.pane.mediaPlayer20s.pause();
				main.pane.m_gameover.stop();
				main.pane.m_gameover.play();
				grid.isend = true;
				grid.map.clear();
				Platform.runLater(new Runnable() {
					public void run() {
						stage.setScene(main.endForm.scene);
					}
				});
			}
			
			if(newVal.intValue() == 15) {
				main.pane.mediaPlayer20s.play();
			}
		});
		
		grid.time.addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
			grid.p.setProgress(newVal.doubleValue()/main.t);
		});
		
		grid.promptleft.addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
			t1.setText("提示:" + newVal);
		});
		
		scene = new Scene(this, 910, 625);
		scene.getStylesheets().add(this.getClass().getResource("/res/css/GameForm.css").toString());
		
		bt1.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			main.pane.mediaPlayer20s.pause();
			if(grid.t != null) {
				grid.t.interrupt();
			}
			grid.time.setValue(Utils.TIMELEFT);
			grid.num = 1;
			grid.count = 0;
			grid.init();
			if(!grid.prompt()) {
				grid.refresh();
			}
			grid.promptleft.setValue(Utils.PROMPTLEFT);
			grid.level.set("Level: " + grid.num);
		});
		bt2.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			grid.ispause = true;
			main.pane.mediaPlayer20s.pause();
			grid.t.interrupt();
			stage.setScene(main.pauseForm.scene);
		});
		bt3.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			if(grid.promptleft.getValue() > 0) {
				grid.promptleft.setValue(grid.promptleft.getValue()-1);
				if(grid.prompt()) {
					grid.map.get(grid.startPoint).color.setValue(Color.BLUE);
					grid.map.get(grid.endPoint).color.setValue(Color.BLUE);					
				}
			}
		});
	}
}
