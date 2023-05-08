package algs.Resource;

import algs.method.NormalRandom;

public class Node {
	public double x;
	public double y;

	public boolean isVisited = false;//若无人机没遍历过该节点为false，遍历过一次为true
	public boolean iscal = false;//若这个节点的任务已经被计算过，则其为true

	//接下来的参数是为了无线传感器设置的，充电节点只需要坐标参数
	public float missionTime;//完成本节点任务所需时间
	public float missionTimeLeft;//这个值可以变，需要完成本任务剩余的时间
	static float trasTime;//传输本任务的源数据所需时间
	static float transTime2;//传输本任务计算结果所需时间
	public float deadline;//超过这个时间没返回就算这个任务失败

	//这两个值代表该节点生成任务需要无人机计算的时长的上下限
	static float calMinTime = 10;
	static float calMaxTime = 100;
	//alpha用来指代deadline的系数
	public static float alpha = (float) 5.0;

	public Node(double x2, double y2) {
		this.x = x2;
		this.y = y2;
		this.missionTime = (float) (Math.random() * (this.calMaxTime - this.calMinTime) + this.calMinTime);//随机生成该任务所需时间
		this.deadline = this.missionTime * this.alpha;
		this.missionTimeLeft = this.missionTime;
		this.trasTime = (float) (0.1 * this.missionTime);
		this.transTime2 = (float) (0.01 * this.missionTime);
	}

	public Node(double x2, double y2, double a, double b)
//a为3*60，b为1.02
	{
		this.x = x2;
		this.y = y2;
		this.missionTime = (float) NormalRandom.normalRandom(a, b);
		this.deadline = this.missionTime * this.alpha;
		this.missionTimeLeft = this.missionTime;
		this.trasTime = (float) (0.1 * this.missionTime);
		this.transTime2 = (float) (0.01 * this.missionTime);
	}
	
	// function index = 2
	public Node(double x2, double y2, float missionTime) {
		this.x = x2;
		this.y = y2;
		this.missionTime = missionTime;
		this.deadline = this.missionTime * this.alpha;
		this.missionTimeLeft = this.missionTime;
		this.trasTime = (float) (0.1 * this.missionTime);
		this.transTime2 = (float) (0.01 * this.missionTime);
	}

	//minTime是距离这个节点最近的无人机飞过去完成的最短时间
	public Node(double x2, double y2, float missionTime, float minTime) {
		this.x = x2;
		this.y = y2;
		this.missionTime = missionTime;
		this.deadline = minTime * this.alpha;
		this.missionTimeLeft = this.missionTime;
		this.trasTime = (float) (0.1 * this.missionTime);
		this.transTime2 = (float) (0.01 * this.missionTime);
	}


}
