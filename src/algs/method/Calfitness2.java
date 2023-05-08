package algs.method;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Calfitness2 {
	//修复了calfitness没算传输时间的问题

	public static String chargeMethod = "charge";


	//传输时间的参数
	public static float transmissionTimeParameter = (float) (Math.random() * 1);
	//返回任务时长的参数
	static float returnTransmissionTimeParameter = (float) 0.01;


	public static ArrayList<?> calfitness(Map map, UAV u1) throws CloneNotSupportedException {
		u1.index = map.whichChargingStationIsCloser[u1.nodeSequence.get(0)];
		UAV u = new UAV();
		u = (UAV) u1.clone();
		u.nodeSequence = new ArrayList<Integer>();
		u.nodeSequence.addAll(u1.nodeSequence);
		u.nodeSet = new HashSet<Integer>();
		u.nodeSet.addAll(u1.nodeSet);

		switch (chargeMethod) {
			case "charge":
				Charge.charge(u, map);
				break;
			case "charge2":
				Charge2.charge2(u, map);
				break;
		}

		int theInitialPosition = u1.index;

		/**
		 *计算无人机遍历的总长度
		 */
		float length = 0;
		int index = u.index;//无人机当前所在位置
		Iterator<Integer> it = u.nodeSequence.iterator();//对节点进行遍历
		int next;
		while (it.hasNext()) {
			next = it.next();
			map.node[next].isVisited = false;
			map.node[next].iscal = false;
			length += map.distance[index][next];
			map.node[next].missionTimeLeft = map.node[next].missionTime;
			index = next;

		}

		/**
		 * 无人机返回数据的节点顺序
		 */
		@SuppressWarnings("unchecked")
		HashSet<Integer> hs = new HashSet();//辅助确定无人机返回数据所需要遍历节点的顺序
		LinkedList<Integer> ReturnOrder = new LinkedList<Integer>();//里面存放无人机返回数据节点的顺序
		for (int i : u.nodeSequence) {//如果
			if (hs.contains(i) && i >= map.csNum) {
				ReturnOrder.add(i);
			} else if (i >= map.csNum) hs.add(i);
		}


		/**
		 * 无人机完成任务的总时间
		 */
		index = u.index;//无人机当前坐标
		float TimeThatHasNowPassed = 0;//当前已经过去的时间
		ArrayList<Integer> TheNodesThatNeedToBeEvaluated = new ArrayList<Integer>();//用来存放已经被无人机收取但
		//还未进行计算的任务//这是有序的
		HashSet<Integer> hSet = new HashSet<Integer>();//用来存放已经计算完成的任务
		it = u.nodeSequence.iterator();
		//无人机飞行时间
		float time;

		while (it.hasNext()) {
			next = it.next();
			time = map.distance[next][index] / UAV.speed;//无人机飞行时间


			if (map.node[next].isVisited == false || next < map.csNum)//该节点没有被遍历过，说明是收取该节点的任务
			{
				//将这个节点标记为已经遍历过
				if (next >= map.csNum) map.node[next].isVisited = true;
				TimeThatHasNowPassed += time + map.node[next].missionTime * transmissionTimeParameter;//因为该节点没有被遍历过，所以无人机收了数据就走，离开的时间等于其到达时间
				if (TheNodesThatNeedToBeEvaluated.size() == 0)//如果离开上一个节点的时候没有任务可以计算，说明待计算序列为空
				{
					if (next >= map.csNum)//需要保证这个节点不是充电节点
						TheNodesThatNeedToBeEvaluated.add(next);//因为只有一个任务，所以不需要排序
				} else//如果离开上个节点的时候有任务可以计算
				{
					while (TheNodesThatNeedToBeEvaluated.size() != 0) {
						if (map.node[TheNodesThatNeedToBeEvaluated.get(0)].missionTimeLeft < time)//如果这个任务算完之后还有时间
						{//说明本任务已经算完了
							time -= map.node[TheNodesThatNeedToBeEvaluated.get(0)].missionTimeLeft;
							hSet.add(TheNodesThatNeedToBeEvaluated.get(0));
							TheNodesThatNeedToBeEvaluated.remove(0);
						} else//如果到达本节点的时候还没有算完
						{
							//这个任务依旧需要计算，但是需要把该任务已经计算的时间减去
							map.node[TheNodesThatNeedToBeEvaluated.get(0)].missionTimeLeft -= time;
							//指示到达这个节点的时候正在计算这个节点
							map.node[TheNodesThatNeedToBeEvaluated.get(0)].iscal = true;
							break;
						}
					}

					//while循环之后分为两种情况，要么是到达该节点时任务已经全部算完了，要么是还有需要计算的任务
					if (TheNodesThatNeedToBeEvaluated.size() == 0)//如果已经全部计算完成，那么只需要插入即可
					{
						if (next >= map.csNum)
							TheNodesThatNeedToBeEvaluated.add(next);
					} else {
						if (map.node[TheNodesThatNeedToBeEvaluated.get(0)].iscal == true)//这里做的是将刚才已经计算的节点
						//继续放在第一位计算
						{
							if (next >= map.csNum)//要确保该节点是无线传感器节点
							{
								int x = TheNodesThatNeedToBeEvaluated.get(0);
								TheNodesThatNeedToBeEvaluated.remove(0);
								TheNodesThatNeedToBeEvaluated.add(next);
								TheNodesThatNeedToBeEvaluated.sort((o1, o2) -> {
									int i = ReturnOrder.indexOf(o1) > ReturnOrder.indexOf(o2) ? 1 : -1;
									return i;
								});
								TheNodesThatNeedToBeEvaluated.add(0, x);
							}
						} else {
							if (next >= map.csNum)//确保该节点是无线传感器节点
							{
								TheNodesThatNeedToBeEvaluated.add(next);
								TheNodesThatNeedToBeEvaluated.sort((o1, o2) -> {
									int i = ReturnOrder.indexOf(o1) > ReturnOrder.indexOf(o2) ? 1 : -1;
									return i;
								});
							}
						}

					}

				}
			} else//如果本节点已经被遍历过//说明要返回任务
			{
				TimeThatHasNowPassed += time + returnTransmissionTimeParameter * map.node[next].missionTime;//
				if (hSet.contains(next))//如果到达这个节点的时候这个任务已经计算完成了
				{
					if (TimeThatHasNowPassed > map.node[next].deadline)//如果返回的时间已经超出了截止日期
					{//u.timeToCompleteAllTasks=Float.MAX_VALUE;
						return null;//直接返回null
					}
				} else//如果到达本节点的时候还没有计算完成
				{
					while (!hSet.contains(next))//只要next还没有计算完成
					{
						TimeThatHasNowPassed += map.node[TheNodesThatNeedToBeEvaluated.get(0)].missionTime;
						hSet.add(TheNodesThatNeedToBeEvaluated.get(0));
						TheNodesThatNeedToBeEvaluated.remove(0);
					}
					//计算完成后返回任务
					if (TimeThatHasNowPassed > map.node[next].deadline)//如果返回的时间已经超出了截止日期
					{//u.timeToCompleteAllTasks=Float.MAX_VALUE;
						return null;//直接返回null
					}
				}
			}
			index = next;
		}
		length += map.distance[theInitialPosition][index];
		TimeThatHasNowPassed += map.distance[theInitialPosition][index] / u.speed;
		ArrayList ret = new ArrayList();
		ret.add(length);//第一个返回飞行长度，第二个返回时长
		//返回时长
		ret.add(TimeThatHasNowPassed);
		u1.length = length;
		u1.timeToCompleteAllTasks = TimeThatHasNowPassed;
		
		return ret;

	}

}
