package linkup.speed.service;

//汉字以UTF-8格式编码

import linkup.speed.Algorithm.*;
import linkup.speed.MainClass.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Node extends Pane {
	//节点的位置坐标
	public ObjectProperty<Point> point = new SimpleObjectProperty<Point>();
	//节点的类型
	public StringProperty type = new SimpleStringProperty();
	//节点是否可点击
	public BooleanProperty clickable = new SimpleBooleanProperty(true);
	//所在所在面板
	public Grid grid;
	//节点的图片
	//public ObjectProperty<Image> image = new SimpleObjectProperty<Image>();
	public ImageView imageView;
	//节点边框
	public Rectangle rectangle = new Rectangle(2.5, 2.5, 65, 65);
	//节点边框颜色
	public ObjectProperty<Color> color = new SimpleObjectProperty<Color>(Color.PINK);
	public Main main;
	
	//节点构造方法
	public Node(final Grid grid, Point point, String type, Main main1) {
		this.setPrefSize(70, 70);
		this.grid = grid;
		this.point.setValue(point);
		this.type.setValue(type);
		main = main1;
		
		//生成节点图片
		imageView = new ImageView(new Image(type));
		this.getChildren().add(imageView);
		imageView.setTranslateX(5);
		imageView.setTranslateY(5);
		
		//生成节点边框
		rectangle.setStrokeWidth(3);
		rectangle.strokeProperty().bind(color);
		rectangle.setFill(null);
		rectangle.setArcWidth(10);
		rectangle.setArcHeight(10);
		
		this.getChildren().add(rectangle);
		this.setOnMouseClicked(e -> {
			if(clickable.getValue()) {
				handleAction();				
			}
		});
	}
	
	//点击事件处理
	public void handleAction() {
		if(grid.startNode == null && grid.endNode == null) {
			main.pane.m_choosefirst.stop();
			this.color.setValue(Color.RED);
			grid.startNode = this;
			main.pane.m_choosefirst.play();
		}
		else if(grid.startNode != null && grid.endNode == null) {
			this.color.setValue(Color.RED);
			grid.endNode = this;
			if(grid.startNode.point.getValue().equals(grid.endNode.point.getValue())) {
				main.pane.m_rechoose.stop();
				grid.map.get(grid.startNode.point.getValue()).color.setValue(Color.PINK);
				grid.map.get(grid.endNode.point.getValue()).color.setValue(Color.PINK);
				grid.startNode = grid.endNode = null;
				main.pane.m_rechoose.play();
			}
			else {
				if(grid.startNode.type.getValue().equals(grid.endNode.type.getValue())) {
					
					if(grid.findPath(grid.startNode.point.getValue(), grid.endNode.point.getValue())) {
						//清除节点
						main.pane.m_clear.stop();
						grid.map.get(grid.startNode.point.getValue()).clickable.setValue(false);
						grid.map.get(grid.endNode.point.getValue()).clickable.setValue(false);
						grid.map.get(grid.startNode.point.getValue()).remove();
						grid.map.get(grid.endNode.point.getValue()).remove();
						grid.startNode = grid.endNode = null;
						main.pane.m_clear.play();
						grid.count++;
						if(!grid.prompt()) {
							grid.refresh();
						}
						
						if(grid.isEmpty()) {
							main.pane.mediaPlayer20s.pause();
							grid.t.interrupt();
							grid.map.clear();
							grid.startNode = null;
							grid.endNode = null;
							grid.iname.clear();
							grid.num++;
							grid.init();
							if(!grid.prompt()) {
								grid.refresh();
							}
							grid.level.set("Level: " + grid.num);
						}
					}
					else {
						main.pane.m_choosefirst.stop();
						grid.map.get(grid.startNode.point.getValue()).color.setValue(Color.PINK);
						grid.map.get(grid.endNode.point.getValue()).color.setValue(Color.RED);
						grid.startNode = grid.endNode;
						grid.endNode = null;
						main.pane.m_choosefirst.play();
					}
				}
				else {
					main.pane.m_choosefirst.stop();
					grid.map.get(grid.startNode.point.getValue()).color.setValue(Color.PINK);
					grid.map.get(grid.endNode.point.getValue()).color.setValue(Color.RED);
					grid.startNode = grid.endNode;
					grid.endNode = null;
					main.pane.m_choosefirst.play();
				}
			}
		}
	}
	
	//消除节点内容
	public void remove() {
		this.getChildren().clear();
	}
	
	public Point getPoint() {
		return point.getValue();
	}
}
