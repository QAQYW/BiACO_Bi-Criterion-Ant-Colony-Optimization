package algs.Resource;

import java.util.ArrayList;
import java.util.HashSet;


//speed=20,flyEnergyConsume=900;

public class UAV implements Cloneable{
	public int x,y;//无人机坐标
	public static float speed=22;//无人机飞行速度//单位米每秒//40//22
	//public static float totalEnergy=548*60;//单位瓦时，可以以548w的功率输出60分钟
	public static float totalEnergy = 548 * 60 * 60;//之前量纲有误，现更正 --by wyq
	public float energyLeft=totalEnergy;
	
	public static float flyEnergyConsume=850;//无人机飞行能耗//单位w//1250//850
	public HashSet<Integer> nodeSet=new HashSet<Integer>() ;//用来存放需要飞行的节点
	public ArrayList<Integer> nodeSequence=new ArrayList<Integer>();//用来存放节点序列
	public float length=0;//遍历所有节点的飞行长度
	public int index;//本无人机现在在哪
	public float timeToCompleteAllTasks=Float.MAX_VALUE;
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
	
	public UAV()
	{
		
	}
	
	
	public void init(HashSet<Integer> arr)
	{
		this.nodeSet=arr;
	}
	

}
