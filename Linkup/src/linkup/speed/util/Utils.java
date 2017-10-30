package linkup.speed.util;

//汉字以UTF-8格式编码

public interface Utils {
	//节点行数(须为偶数)
	public int ROWCOUNT = 12;
	//节点列数(须为偶数)
	public int COLCOUNT = 8;
	//游戏宽度
	public final int GAMEWIDTH = ROWCOUNT*70 + (ROWCOUNT-1)*5 + 155;
	//游戏高度
	public final int GAMEHEIGHT = COLCOUNT*70 + (COLCOUNT-1)*5 + 40;
	//状态面板宽度
	public final int CONDITIONWIDTH = GAMEWIDTH;
	//状态面板高度
	public final int CONDITIONHEIGHT = 40;
	//控制面板宽度
	public final int CONTROLWIDTH = 155;
	//控制面板高度
	public final int CONTROLHEIGHT = GAMEHEIGHT;
	//初始倒计时时间
	public final int TIMELEFT = 100;
	//剩余提示次数
	public final int PROMPTLEFT = 5;
	//节点图片
	public String[] ImageName = {"/res/image/image1.png", "/res/image/image2.png", "/res/image/image3.png", "/res/image/image4.png",
			"/res/image/image5.png", "/res/image/image6.png", "/res/image/image7.png", "/res/image/image8.png",
			"/res/image/image9.png", "/res/image/image10.png", "/res/image/image11.png", "/res/image/image12.png",
			"/res/image/image13.png", "/res/image/image14.png", "/res/image/image15.png",	"/res/image/image16.png",
			"/res/image/image17.png", "/res/image/image18.png", "/res/image/image19.png", "/res/image/image20.png"};
	
	
	
}
