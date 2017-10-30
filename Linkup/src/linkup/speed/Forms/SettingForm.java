package linkup.speed.Forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import linkup.speed.Algorithm.Grid;
import linkup.speed.MainClass.Main;

public class SettingForm extends BorderPane {
	public Scene scene;
	public Grid grid;
	
	public SettingForm(Stage stage, Main main, Grid grid1) {
		grid = grid1;
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.BOTTOM_CENTER);
		HBox h1 = new HBox();
		h1.setSpacing(10);
		h1.setAlignment(Pos.CENTER);
		HBox h2 = new HBox();
		h2.setSpacing(10);
		h2.setAlignment(Pos.CENTER );
		HBox h3 = new HBox();
		h3.setPrefSize(910, 70);
		h3.setPadding(new Insets(30, 30, 30, 30));
		h3.setAlignment(Pos.BOTTOM_RIGHT);
		VBox vBox = new VBox();
		vBox.setSpacing(100);
		vBox.setAlignment(Pos.CENTER);
		
		Label l = new Label("设     置");
		l.setId("l");
		hBox.getChildren().add(l);
		
		//音乐音量控制滚动条
		Slider slider = new Slider(0, 1, 0.2);
		main.pane.mediaPlayer.volumeProperty().bind(slider.valueProperty());
		
		//音乐静音复选框
		CheckBox checkBox = new CheckBox("");
		main.pane.mediaPlayer.muteProperty().bind(checkBox.selectedProperty());
		
		//音效音量控制滚动条
		Slider slider1 = new Slider(0, 1, 0.3);
		main.pane.mediaPlayer20s.volumeProperty().bind(slider1.valueProperty());
		main.pane.m_button.volumeProperty().bind(slider1.valueProperty());
		main.pane.m_choosefirst.volumeProperty().bind(slider1.valueProperty());
		main.pane.m_clear.volumeProperty().bind(slider1.valueProperty());
		main.pane.m_gameover.volumeProperty().bind(slider1.valueProperty());
		main.pane.m_rechoose.volumeProperty().bind(slider1.valueProperty());
		
		//音效静音复选框
		CheckBox checkBox1 = new CheckBox("");
		main.pane.mediaPlayer20s.muteProperty().bind(checkBox1.selectedProperty());
		main.pane.m_button.muteProperty().bind(checkBox1.selectedProperty());
		main.pane.m_choosefirst.muteProperty().bind(checkBox1.selectedProperty());
		main.pane.m_clear.muteProperty().bind(checkBox1.selectedProperty());
		main.pane.m_gameover.muteProperty().bind(checkBox1.selectedProperty());
		main.pane.m_rechoose.muteProperty().bind(checkBox1.selectedProperty());
		
		Text t1 = new Text("音乐");
		Text t2 = new Text("静音");
		Text t3 = new Text("音效");
		Text t4 = new Text("静音");
		h1.getChildren().addAll(t1, slider, t2, checkBox);
		h2.getChildren().addAll(t3, slider1, t4, checkBox1);
		vBox.getChildren().addAll(h1, h2);
		
		Button bt = new Button("返回");
		bt.setPrefSize(200, 40);
		h3.getChildren().add(bt);
		
		this.setId("this");
		this.setTop(hBox);
		this.setCenter(vBox);
		this.setBottom(h3);
		
		scene = new Scene(this, 910, 625);
		scene.getStylesheets().add(this.getClass().getResource("/res/css/SettingForm.css").toString());
		
		bt.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			if(grid.flag == 1) {
				stage.setScene(main.pauseForm.scene);
				grid.flag = 0;
			}
			if(grid.flag == 2) {
				stage.setScene(main.scene);
				grid.flag = 0;
			}
		});
	}
}
