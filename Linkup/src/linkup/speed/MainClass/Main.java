package linkup.speed.MainClass;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import linkup.speed.Algorithm.Grid;
import linkup.speed.Forms.*;

public class Main extends Application {
	public Scene scene;
	public MainForm pane;
	public GameForm gameForm;
	public PauseForm pauseForm;
	public EndForm endForm;
	public SettingForm settingForm;
	public AboutForm aboutForm;
	public Grid grid;
	public Thread mainThread = Thread.currentThread();
	public Exit exit = new Exit();
	public int t;
	
	@Override
	public void start(Stage stage) {
		grid = new Grid(this);
		pane = new MainForm(stage, this, grid);
		scene = pane.scene;
		gameForm = new GameForm(stage, this, grid);
		pauseForm = new PauseForm(stage, this, grid);
		endForm = new EndForm(stage, this, grid);
		settingForm = new SettingForm(stage, this, grid);
		aboutForm = new AboutForm(stage, this);

		stage.initStyle(StageStyle.UNIFIED);
		stage.setTitle("连连看");
		stage.setScene(scene);
		stage.show();
		stage.setResizable(false);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
