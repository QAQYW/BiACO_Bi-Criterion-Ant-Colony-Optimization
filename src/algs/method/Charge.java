package algs.method;

import algs.Resource.Map;
import algs.Resource.UAV;

public  class Charge {
	public static void  charge(UAV u,Map map)
	{     
		u.index=map.whichChargingStationIsCloser[u.nodeSequence.get(0)];
		u.energyLeft=u.totalEnergy;
		int index=u.index;//无人机现在的位置
		for(int i=0;i<u.nodeSequence.size();i++)
		{
			float time=(map.distance[index][u.nodeSequence.get(i)]/u.speed);// /60; 原来有除以60，现在去掉，因为总能量扩大了60倍 -- by wyq
			float energyConsume=time*u.flyEnergyConsume;
			u.energyLeft-=energyConsume;
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
			
			if(u.energyLeft<(map.distance[u.nodeSequence.get(i)][chargeIndex]/u.speed)*u.flyEnergyConsume)//如果电量不足以飞到充电节点了
				{
					u.nodeSequence.add(i, chargeIndex);//要先去充电才能到这个节点
					u.energyLeft=u.totalEnergy;
					i++;
				}
			index=u.nodeSequence.get(i);
		}

	}
}
