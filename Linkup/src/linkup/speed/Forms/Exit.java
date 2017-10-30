package linkup.speed.Forms;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Exit extends BorderPane {
	public Stage stage = new Stage();
	private Scene scene;
	public Exit() {
		Label l = new Label("确定退出游戏吗");
		l.setStyle("-fx-font-size: 50px");
		l.setId("l");
		Button bt1 = new Button("确定");
		bt1.setPrefSize(150, 30);
		Button bt2 = new Button("取消");
		bt2.setPrefSize(150, 30);
		
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setPrefSize(500, 60);
		hBox.setSpacing(125);
		hBox.getChildren().addAll(bt1, bt2);
		
		this.setCenter(l);
		this.setBottom(hBox);
		scene = new Scene(this, 500, 250);
		this.setId("this");
		scene.getStylesheets().add(this.getClass().getResource("/res/css/Exit.css").toString());
		stage.setScene(scene);
		
		bt1.setOnMouseClicked(e -> {
			System.exit(0);
			stage.hide();
		});
		
		bt2.setOnMouseClicked(e -> {
			stage.hide();
		});
	}
}
