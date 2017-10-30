package linkup.speed.Forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import linkup.speed.Algorithm.Grid;
import linkup.speed.MainClass.Main;
import linkup.speed.util.Utils;

public class EndForm extends BorderPane {
	public Scene scene;
	public Grid grid;
	public Stage stage;
	public Main main;
		
	public EndForm(Stage stage1, Main main1, Grid grid1) {
		stage = stage1;
		main = main1;
		grid = grid1;
		HBox hBox1 = new HBox();
		hBox1.setAlignment(Pos.BOTTOM_CENTER);
		HBox hBox2 = new HBox();
		hBox2.setPrefSize(910, 70);
		hBox2.setPadding(new Insets(30, 30, 30, 30));
		hBox2.setAlignment(Pos.CENTER);
		hBox2.setSpacing(550);
		
		Label l = new Label("Game over");
		l.setId("l");
		hBox1.getChildren().add(l);
		
		Button bt1 = new Button("再来一局");
		bt1.setPrefSize(150, 40);
		Button bt2 = new Button("返回主菜单");
		bt2.setPrefSize(150, 40);
		hBox2.getChildren().addAll(bt2, bt1);
		
		this.setId("this");
		this.setTop(hBox1);
		this.setBottom(hBox2);
		
		scene = new Scene(this, 910, 625);
		scene.getStylesheets().add(this.getClass().getResource("/res/css/EndForm.css").toString());
		
		bt1.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
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
			
			stage.setScene(main.gameForm.scene);			
		});
		bt2.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			stage.setScene(main.scene);
		});
	}
}
