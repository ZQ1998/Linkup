package linkup.speed.Forms;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import linkup.speed.Algorithm.Grid;
import linkup.speed.MainClass.Main;

public class PauseForm extends BorderPane {
	public Scene scene;
	
	public PauseForm(Stage stage, Main main, Grid grid) {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.BOTTOM_CENTER);
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(70);
		
		Label l = new Label("休息一下");
		l.setId("l");
		hBox.getChildren().add(l);
		
		Button bt1 = new Button("返回游戏");
		bt1.setPrefSize(280, 40);
		Button bt2 = new Button("设置");
		bt2.setPrefSize(280, 40);
		Button bt3 = new Button("结束本局");
		bt3.setPrefSize(280, 40);
		vBox.getChildren().addAll(bt1, bt2, bt3);
		
		this.setId("this");
		this.setTop(hBox);
		this.setCenter(vBox);
		
		scene = new Scene(this, 910, 625);
		scene.getStylesheets().add(this.getClass().getResource("/res/css/PauseForm.css").toString());
		
		bt1.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			if(grid.ispause && grid.time.getValue() <= 15)
				main.pane.mediaPlayer20s.play();
			grid.ispause = false;
			grid.demo();
			stage.setScene(main.gameForm.scene);
		});
		bt2.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			grid.flag = 1;
			stage.setScene(main.settingForm.scene);
		});
		bt3.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			grid.ispause = false;
			if(grid.t != null) {
				if(grid.t.isAlive()) {
					grid.t.interrupt();
				}
			}
			grid.map.clear();
			stage.setScene(main.pane.scene);
		});
	}
}
