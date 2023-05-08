package algs.method;

import algs.Resource.Map;
import algs.Resource.UAV;

public class Charge2 {
	public static void  charge2(UAV u,Map map)
	{     
		//无人机现在的位置
		u.index=map.whichChargingStationIsCloser[u.nodeSequence.get(0)];
		//无人机剩余电量
		u.energyLeft=UAV.totalEnergy;
		//无人机现在的位置
		int index=u.index;

		for(int i=0;i<u.nodeSequence.size();i++)
		{
			//到达下一个位置需要花费的时间
			float time=(map.distance[index][u.nodeSequence.get(i)]/UAV.speed)/60;
			//到达下一个位置需要花费的电量
			float energyConsume=time*UAV.flyEnergyConsume;
			//更新电量
			u.energyLeft-=energyConsume;
			//下面的代码是看无人机到达哪个充电站更近
			float length=map.distance[u.nodeSequence.get(i)][0];
			int chargeIndex=0;
			for(int j=1;j<map.csNum;j++)//无人机到达这个节点距离最近的充电节点是哪个
				{
					if(length>map.distance[u.nodeSequence.get(i)][j])
					{
						length=map.distance[u.nodeSequence.get(i)][j];	
						chargeIndex=j;
					}
				}
			
			if(u.energyLeft<(map.distance[u.nodeSequence.get(i)][chargeIndex]/UAV.speed)*UAV.flyEnergyConsume)//如果电量不足以飞到充电节点了
				{
					u.nodeSequence.add(i, chargeIndex);//要先去充电才能到这个节点
					u.energyLeft=UAV.totalEnergy;
					i++;
				}
			else  if(i+1<u.nodeSequence.size()&&u.energyLeft<UAV.totalEnergy*0.3)
			   {
					float length1=map.distance[u.nodeSequence.get(i+1)][chargeIndex];
				
				if(length<length1)
				{
					u.nodeSequence.add(i, chargeIndex);//要先去充电才能到这个节点
					u.energyLeft=UAV.totalEnergy;
					i++;
				}
			   }
			index=u.nodeSequence.get(i);
		}
	}
}
