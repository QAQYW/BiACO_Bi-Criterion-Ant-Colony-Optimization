package algs.acodsp;

import java.util.ArrayList;

import resource.Map;

public class UAV_DSP implements Cloneable {
	
	public static double SPEED;
	public static double MAX_ENERGY;
	public static double FLY_ENERGY_CONSUME;
	
	public double energyLeft;
	public double timeConsume;
	public double curTime;
	public int index;
	public int boundaryParameter;
	public ArrayList<Integer> nodeSeq;
	public ArrayList<Integer> nodeSeq2;
	
	public int initLoc;
	public int pheromoneIndex;
	
	public UAV_DSP(int phIndex) {
		pheromoneIndex = phIndex;
		nodeSeq = new ArrayList<Integer>();
		nodeSeq2 = new ArrayList<Integer>();
		init();
		boundaryParameter = 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UAV_DSP clone() throws CloneNotSupportedException {
		UAV_DSP uav = null;
		try {
			uav = (UAV_DSP) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		uav.pheromoneIndex = this.pheromoneIndex;
		uav.nodeSeq = (ArrayList<Integer>) this.nodeSeq.clone();
		uav.nodeSeq2 = (ArrayList<Integer>) this.nodeSeq2.clone();
		return uav;
	}
	
	public void init() {
		energyLeft = MAX_ENERGY;
		timeConsume = Double.MAX_VALUE;
		index = 0;
		curTime = 0;
		nodeSeq2.clear();
	}
	
	public boolean calcTimeConsume() {
		init();
		for (int i = Map.staNum; i < Map.totNum; i++)
			Map.nodes[i].resetCalcTimeLeft();
		
		int size = nodeSeq.size(), taskIndex;
		int[] vis = new int[Map.totNum];
		
		double time, energy, timeSta, energySta, timeNext, timeCom;
		boolean[] complete = new boolean[Map.totNum];
		
		index = Map.closeStation[nodeSeq.get(0)];
		nodeSeq2.add(index);
		
		int next, computingIndex = -1;
		for (int i = 0; i < size; i++) {
			next = nodeSeq.get(i);
			
			if (vis[next] == 0) {
				// data transmission
				timeNext = Map.distance[index][next] / SPEED;
				if (computingIndex == -1) timeCom = 0;
				else timeCom = Map.nodes[computingIndex].calcTimeLeft;
				timeSta = Math.max(timeNext, timeCom) + Map.nodes[next].getDataTime() + Map.distance[next][Map.closeStation[next]] / SPEED;
				energySta = timeSta * FLY_ENERGY_CONSUME;
				
				// check residual energy
				if (energySta > energyLeft) {
					int sta = chooseSta(next);
					time = Map.distance[index][sta] / SPEED;
					curTime += time;
					index = sta;
					energyLeft = MAX_ENERGY;
					if (computingIndex != -1) {
						if (Map.nodes[computingIndex].calcTimeLeft <= time) {
							Map.nodes[computingIndex].calcTimeLeft = 0;
							complete[computingIndex] = true;
							computingIndex = -1;
						} else {
							Map.nodes[computingIndex].calcTimeLeft -= time;
						}
					}
					nodeSeq2.add(sta);
				}
				
				// fly to "next"
				time = Map.distance[index][next] / SPEED;
				if (computingIndex != -1) {
					if (Map.nodes[computingIndex].calcTimeLeft <= time) {
						Map.nodes[computingIndex].calcTimeLeft = 0;
						complete[computingIndex] = true;
						computingIndex = -1;
					} else {
						time = Map.nodes[computingIndex].calcTimeLeft;
						complete[computingIndex] = true;
						Map.nodes[computingIndex].calcTimeLeft = 0;
						computingIndex = -1;
					}
				}
				time += Map.nodes[next].getDataTime();
				energy = time * FLY_ENERGY_CONSUME;
				curTime += time;
				energyLeft -= energy;
				index = next;
				computingIndex = index;
				vis[next]++;
			} else {
				// result transmission
				timeNext = Map.distance[index][next] / SPEED;
				if (computingIndex == -1) timeCom = 0;
				else timeCom = Map.nodes[computingIndex].calcTimeLeft;
				timeSta = Math.max(timeNext, timeCom) + Map.nodes[next].getResTime() + Map.distance[next][Map.closeStation[next]] / SPEED;
				energySta = timeSta * FLY_ENERGY_CONSUME;
				
				// check residual energy
				if (energySta > energyLeft) {
					int sta = chooseSta(next);
					time = Map.distance[index][sta] / SPEED;
					curTime += time;
					index = sta;
					energyLeft = MAX_ENERGY;
					if (computingIndex != -1) {
						if (Map.nodes[computingIndex].calcTimeLeft <= time) {
							Map.nodes[computingIndex].calcTimeLeft = 0;
							complete[computingIndex] = true;
							computingIndex = -1;
						} else {
							Map.nodes[computingIndex].calcTimeLeft -= time;
						}
					}
					nodeSeq2.add(sta);
				}
				
				// fly to "next"
				time = Map.distance[index][next] / SPEED;
				if (computingIndex != -1) {
					if (Map.nodes[computingIndex].calcTimeLeft <= time) {
						Map.nodes[computingIndex].calcTimeLeft = 0;
						complete[computingIndex] = true;
						computingIndex = -1;
					} else {
						time = Map.nodes[computingIndex].calcTimeLeft;
						complete[computingIndex] = true;
						Map.nodes[computingIndex].calcTimeLeft = 0;
						computingIndex = -1;
					}
				}
				time += Map.nodes[next].getResTime();
				energy = time * FLY_ENERGY_CONSUME;
				curTime += time;
				energyLeft -= energy;
				index = next;
				vis[next]++;
				
				if (curTime > Map.nodes[index].getDdl()) {
					timeConsume = Double.MAX_VALUE;
					nodeSeq2.clear();
					return false;
				}
			}
			
			nodeSeq2.add(next);
		}
		
		int sta = Map.closeStation[index];
		curTime += Map.distance[index][sta] / SPEED;
		nodeSeq2.add(sta);
		timeConsume = curTime;
		
		return true;
	}
	
	public int chooseSta(int next) {
		int sta = Map.closeStation[index];
		double minDis = Map.distance[index][sta] + Map.distance[sta][next];
		double disLeft = energyLeft * SPEED / FLY_ENERGY_CONSUME;
		double dis;
		for (int _sta = 0; _sta < Map.staNum; _sta++) {
			if (Map.distance[index][_sta] > disLeft)
				continue;
			dis = Map.distance[index][_sta] + Map.distance[_sta][next];
			if (dis > minDis)
				continue;
			if (dis < minDis || Map.distance[_sta][next] < Map.distance[sta][next]) {
				sta = _sta;
				minDis = dis;
			}
		}
		return sta;
	}
}
