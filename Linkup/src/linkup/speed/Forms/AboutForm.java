package linkup.speed.Forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import linkup.speed.MainClass.Main;

public class AboutForm extends BorderPane {
	public Scene scene;
	
	public AboutForm(Stage stage, Main main) {
		HBox h1 = new HBox();
		h1.setPadding(new Insets(30, 30, 30, 30));
		h1.setAlignment(Pos.CENTER_RIGHT);
		
		Button bt = new Button("返回");
		bt.setPrefSize(150, 40);
		h1.getChildren().add(bt);

		this.setId("this");
		this.setBottom(h1);
		
		scene = new Scene(this, 910, 625);
		scene.getStylesheets().add(this.getClass().getResource("/res/css/AboutForm.css").toString());
		
		bt.setOnMouseClicked(e -> {
			main.pane.m_button.stop();
			main.pane.m_button.play();
			stage.setScene(main.scene);
		});
	}
}
