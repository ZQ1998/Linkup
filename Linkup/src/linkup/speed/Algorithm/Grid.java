package linkup.speed.Algorithm;

import linkup.speed.MainClass.Main;

//汉字以UTF-8格式编码

import linkup.speed.service.*;
import linkup.speed.util.*;
import java.util.Random;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Grid extends GridPane {
	//存储节点
	public  Map<Point, Node> map = new HashMap<Point, Node>();
	//刷新临时节点
	public List<Node> tempNode = new LinkedList<>();
	//存储图片名称
	public List<String> iname = new LinkedList<String>();
	//开始节点
	public Node startNode = null;
	//结束节点
	public Node endNode = null;
	//提示点
	public Point startPoint;
	public Point endPoint;
	//随机种子
	private Random random = new Random();
	//存储路径坐标
	public DoubleProperty X1 = new SimpleDoubleProperty(0);
	public DoubleProperty Y1 = new SimpleDoubleProperty(0);
	public DoubleProperty X2 = new SimpleDoubleProperty(0);
	public DoubleProperty Y2 = new SimpleDoubleProperty(0);
	public DoubleProperty X3 = new SimpleDoubleProperty(0);
	public DoubleProperty Y3 = new SimpleDoubleProperty(0);
	public DoubleProperty X4 = new SimpleDoubleProperty(0);
	public DoubleProperty Y4 = new SimpleDoubleProperty(0);
	//关卡数
	public int num = 1;
	public StringProperty level = new SimpleStringProperty("Level:" + num);
	//计时线程
	public Thread t = null;
	public IntegerProperty time = new SimpleIntegerProperty(100);
	//剩余提示次数
	public IntegerProperty promptleft = new SimpleIntegerProperty();
	//游戏是否结束
	public boolean isend = false;
	//暂停状态
	public boolean ispause = false;

	public int count = 0;
	
	public int flag;
	
	public Main main;	

	public ProgressBar p = new ProgressBar();
	
	//节点面板的构造方法
	public Grid(Main main1) {
		p.setPrefSize(390, 10);
		main = main1;
		this.setHgap(5);
		this.setVgap(5);
	}
	
	//初始化方法
	public void init() {
		int i;
		String s;
		
		this.getChildren().clear();
		iname.clear();
		for(i = 0; i < (Utils.ROWCOUNT-2)*(Utils.COLCOUNT-2)/2; i++) {
			s = Utils.ImageName[random.nextInt(20)];
			iname.add(s);
			iname.add(s);
		}
		Collections.shuffle(iname);
		
		map.clear();
		startNode = null;
		endNode = null;
		create();
		isend = false;
		time.setValue(Utils.TIMELEFT - (num-1)*5);
		main.t = time.getValue();
		p.setProgress(1);
		t = new Thread(new Runner());
		t.start();
		System.gc();
	}
	
	//刷新方法
	public void refresh() {
		this.getChildren().clear();
		startNode = null;
		endNode = null;
		tempNode.clear();
		
		for(int i = 1; i < Utils.ROWCOUNT-1; i++) {
			for(int j = 1; j < Utils.COLCOUNT-1; j++) {
				tempNode.add(map.get(new Point(i, j)));
			}
		}
		map.clear();
		Collections.shuffle(tempNode);
		
		for(int i = 0; i < Utils.ROWCOUNT; i++) {
			for(int j = 0; j < Utils.COLCOUNT; j++) {
				if(i == 0 || j == 0 || i == Utils.ROWCOUNT-1 || j == Utils.COLCOUNT-1) {
					Point point = new Point(i, j);
					Node node = new Node(this, point, Utils.ImageName[0], main);
					node.remove();
					node.clickable.setValue(false);
					this.add(node, i, j);
					map.put(node.getPoint(), node);
				}
			}
		}
		
		for(int i = 1; i < Utils.ROWCOUNT-1; i++) {// 1 - 10
			for(int j = 1; j < Utils.COLCOUNT-1; j++) {// 1 - 6
				Point point = new Point(i, j);
				Node node = tempNode.get((Utils.COLCOUNT-2)*(i-1) + j-1);
				node.point.setValue(point);
				this.add(node, i, j);
				map.put(node.getPoint(), node);
			}
		}
		
	}
	
	//生成节点并添加到面板之中
	public void create() {
		for(int i = 0; i < Utils.ROWCOUNT; i++) {
			for(int j = 0; j < Utils.COLCOUNT; j++) {
				if(i == 0 || j == 0 || i == Utils.ROWCOUNT-1 || j == Utils.COLCOUNT-1) {
					Point point = new Point(i, j);
					Node node = new Node(this, point, Utils.ImageName[0], main);
					node.remove();
					node.clickable.setValue(false);
					this.add(node, i, j);
					map.put(node.getPoint(), node);
				}
			}
		}
		
		for(int i = 1; i < Utils.ROWCOUNT-1; i++) {
			for(int j = 1; j < Utils.COLCOUNT-1; j++) {
				Point point = new Point(i, j);
				Node node = new Node(this, point, iname.get((i-1)*(Utils.COLCOUNT-2) + j -1), main);
				this.add(node, i, j);
				map.put(node.getPoint(), node);
			}
		}
	}
	
	//查找路径并保存路径信息
	public boolean findPath(Point start, Point end) {
		boolean flag = false;
		//直接相连
		if(start.row == end.row || start.col == end.col) {
			if(Scan(start, end)) {
				flag = true;
				X1.set(map.get(start).getLayoutX()+35);
				Y1.set(map.get(start).getLayoutY()+75);
				X2.set(map.get(start).getLayoutX()+35);
				Y2.set(map.get(start).getLayoutY()+75);
				X3.set(map.get(start).getLayoutX()+35);
				Y3.set(map.get(start).getLayoutY()+75);
				X4.set(map.get(end).getLayoutX()+35);
				Y4.set(map.get(end).getLayoutY()+75);
				return true;
			}
		}
		if(!flag){
			int i;
			Point currentp;
			
			//向上遍历
			i = start.col;
			currentp = new Point(start.row, i);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p1).getLayoutX()+35);
						Y3.set(map.get(p1).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p2).getLayoutX()+35);
						Y3.set(map.get(p2).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {
					i--;
					if(i < 0) {
						break;
					}
					currentp = new Point(start.row, i);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i--;
						if(i < 0) {
							break;
						}
						currentp = new Point(start.row, i);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
			//向下遍历
			i = start.col;
			currentp = new Point(start.row, i);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p1).getLayoutX()+35);
						Y3.set(map.get(p1).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p2).getLayoutX()+35);
						Y3.set(map.get(p2).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {		
					i++;
					if(i > Utils.COLCOUNT-1) {
						break;
					}
					currentp = new Point(start.row, i);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i++;
						if(i > Utils.COLCOUNT-1) {
							break;
						}
						currentp = new Point(start.row, i);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
			//向左遍历
			i = start.row;
			currentp = new Point(i, start.col);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p1).getLayoutX()+35);
						Y3.set(map.get(p1).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p2).getLayoutX()+35);
						Y3.set(map.get(p2).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {		
					i--;
					if(i < 0) {
						break;
					}
					currentp = new Point(i, start.col);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i--;
						if(i < 0) {
							break;
						}
						currentp = new Point(i, start.col);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
			//向右遍历
			i = start.row;
			currentp = new Point(i, start.col);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p1).getLayoutX()+35);
						Y3.set(map.get(p1).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						X1.set(map.get(start).getLayoutX()+35);
						Y1.set(map.get(start).getLayoutY()+75);
						X2.set(map.get(currentp).getLayoutX()+35);
						Y2.set(map.get(currentp).getLayoutY()+75);
						X3.set(map.get(p2).getLayoutX()+35);
						Y3.set(map.get(p2).getLayoutY()+75);
						X4.set(map.get(end).getLayoutX()+35);
						Y4.set(map.get(end).getLayoutY()+75);
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {		
					i++;
					if(i > Utils.ROWCOUNT-1) {
						break;
					}
					currentp = new Point(i, start.col);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i++;
						if(i > Utils.ROWCOUNT-1) {
							break;
						}
						currentp = new Point(i, start.col);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
		}
		return false;
	}
	
	//行列扫描
	public boolean Scan(Point start, Point end) {
		Point temp;
		int min, max, i;
		
		//横向扫描
		if(start.row == end.row) {
			min = Math.min(start.col, end.col);
			max = Math.max(start.col, end.col);
			if(max - min == 1) {
				return true;
			}
			else {
				for(i = min+1; i < max; i++) {
					temp = new Point(start.row, i);
					if(map.get(temp).clickable.getValue()) {
						break;
					}
				}
				if(i == max)
					return true;
			}
		}
		
		//纵向扫描
		else if(start.col == end.col) {
			min = start.row < end.row? start.row: end.row;
			max = start.row > end.row? start.row: end.row;
			if(max - min == 1) {
				return true;
			}
			else {
				for(i = min+1; i < max; i++) {
					temp = new Point(i, start.col);
					if(map.get(temp).clickable.getValue()) {
						break;
					}
				}
				if(i == max)
					return true;
			}
		}
		
		return false;
	}
	
	//判断是否消完
	public boolean isEmpty() {
		for(int i = 0; i < Utils.ROWCOUNT; i++) {
			for(int j = 0; j < Utils.COLCOUNT; j++) {
				if(map.get(new Point(i, j)).clickable.getValue()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void demo() {
		t = new Thread(new Runner());
		t.start();
	}
	
	//倒计时类
	class Runner implements Runnable {
		@Override
		public void run() {
			while(true) {	
				if(!main.mainThread.isAlive()) {
					break;
				}
				if(time.getValue() == 0) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
				
				int i = time.getValue() - 1;
				time.setValue(i);
			}
		}
	}
	
	//提示功能
	public boolean prompt() {
		Point end;
		for(Point start: map.keySet()) {
			if(map.get(start).clickable.getValue()) {
				for(int i = 1; i < Utils.ROWCOUNT-1; i++) {
					for(int j = 1; j < Utils.COLCOUNT-1; j++) {
						end = new Point(i, j);
						if(map.get(end).clickable.getValue()) {
							if(!start.equals(end) && map.get(start).type.getValue().equals(map.get(end).type.getValue())) {
								if(findPath1(start, end)) {
									this.startPoint = start;
									this.endPoint = end;
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	//查找路径(提示功能)
	public boolean findPath1(Point start, Point end) {
		boolean flag = false;
		//直接相连
		if(start.row == end.row || start.col == end.col) {
			if(Scan(start, end)) {
				flag = true;
				return true;
			}
		}
		if(!flag){
			int i;
			Point currentp;
			
			//向上遍历
			i = start.col;
			currentp = new Point(start.row, i);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {
					i--;
					if(i < 0) {
						break;
					}
					currentp = new Point(start.row, i);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i--;
						if(i < 0) {
							break;
						}
						currentp = new Point(start.row, i);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
			//向下遍历
			i = start.col;
			currentp = new Point(start.row, i);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {		
					i++;
					if(i > Utils.COLCOUNT-1) {
						break;
					}
					currentp = new Point(start.row, i);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i++;
						if(i > Utils.COLCOUNT-1) {
							break;
						}
						currentp = new Point(start.row, i);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
			//向左遍历
			i = start.row;
			currentp = new Point(i, start.col);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {		
					i--;
					if(i < 0) {
						break;
					}
					currentp = new Point(i, start.col);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i--;
						if(i < 0) {
							break;
						}
						currentp = new Point(i, start.col);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
			//向右遍历
			i = start.row;
			currentp = new Point(i, start.col);
			do {
				boolean flag1 = false;
				
				Point p1 = new Point(end.row, currentp.col);
				Point p2 = new Point(currentp.row , end.col);
				if(!map.get(p1).clickable.getValue()) {
					if(Scan(currentp, p1) && Scan(p1, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!map.get(p2).clickable.getValue()) {
					if(Scan(currentp, p2) && Scan(p2, end)) {
						flag1 = true;
						return true;
					}
				}
				if(!flag1) {		
					i++;
					if(i > Utils.ROWCOUNT-1) {
						break;
					}
					currentp = new Point(i, start.col);
					if(map.get(currentp).clickable.getValue()) {
						break;
					}
				}
				
				if(currentp.col == end.col) {
					if(Scan(currentp, end)) {
						return true;
					}
					else {
						i++;
						if(i > Utils.ROWCOUNT-1) {
							break;
						}
						currentp = new Point(i, start.col);
					}
				}
			}while(!map.get(currentp).clickable.getValue());
			
		}
		return false;
	}
}