package resource;

import java.util.ArrayList;

import org.omg.PortableServer.AdapterActivator;

import algs.acodsp.UAV_DSP;
import resource.Map;

public class UAV implements Cloneable {
	
	public static double SPEED;
	public static double MAX_ENERGY;
	public static double FLY_ENERGY_CONSUME;
	public static double HOVER_ENERGY_CONSUME = 1370;
	
	private double energyLeft;
	
	public ArrayList<Integer> nodeSeq;	// sequence of tasks
	public ArrayList<Integer> nodeSeq2;	// sequence of tasks and stations
	
	public double timeConsume;
	
	public int boundary;
	public int node;
	
	private int index;		// current location (node)
	private double currTime;// current time
	private int phIndex;	// pheromone index (UAV-DSP-specific)
	
	
	public UAV() {
		nodeSeq = new ArrayList<Integer>();
		nodeSeq2 = new ArrayList<Integer>();
	}
	
	public UAV(int phIndex) {
		nodeSeq = new ArrayList<Integer>();
		nodeSeq2 = new ArrayList<Integer>();
		
		this.phIndex = phIndex;
	}
	
	@SuppressWarnings("unchecked")
	public UAV(UAV_DSP uavdsp) {
		nodeSeq = (ArrayList<Integer>) uavdsp.nodeSeq.clone();
		nodeSeq2 = (ArrayList<Integer>) uavdsp.nodeSeq2.clone();
		timeConsume = uavdsp.timeConsume;
		
		phIndex = uavdsp.pheromoneIndex;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UAV clone() throws CloneNotSupportedException {
		UAV uav = null;
		try {
			uav = (UAV) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		uav.phIndex = this.phIndex;
		uav.nodeSeq = (ArrayList<Integer>) this.nodeSeq.clone();
		uav.nodeSeq2 = (ArrayList<Integer>) this.nodeSeq2.clone();
		return uav;
	}
	
	public int getPhIdex() {
		return phIndex;
	}
	
//	public double getEnergyLeft() {
//		return energyLeft;
//	}
//
//	public void setEnergyLeft(double energyLeft) {
//		this.energyLeft = energyLeft;
//	}
	
	private void init() {
//		boundary = 0;
		energyLeft = MAX_ENERGY;
		currTime = 0;
		nodeSeq2.clear();
	}
	
	public boolean calcTimeConsume() {
		init();
		for (int i = Map.staNum; i < Map.totNum; i++)
			Map.nodes[i].resetCalcTimeLeft();
		
		int size = nodeSeq.size();
		int[] vis = new int[Map.totNum];
		double time, energy, timeSta, energySta, timeNext, timeCom;
		boolean[] complete = new boolean[Map.totNum];
		
		index = Map.closeStation[nodeSeq.get(0)];
		nodeSeq2.add(index);

		int next, computingIndex = -1;
		for (int i = 0; i < size; i++) {
			next = nodeSeq.get(i);
			
			if (vis[next] == 0) {
				timeNext = Map.distance[index][next] / SPEED;
				if (computingIndex == -1) {
					timeCom = 0;
				} else {
					timeCom = Map.nodes[computingIndex].calcTimeLeft;
				}
				timeSta = Math.max(timeNext, timeCom)
						+ Map.nodes[next].getDataTime() + Map.distance[next][Map.closeStation[next]] / SPEED;
				energySta = timeSta * FLY_ENERGY_CONSUME;
				
				// fly to station first if energy is inadequate
				if (energySta > energyLeft) {
					int sta = chooseStation(next);
					time = Map.distance[index][sta] / SPEED;
					currTime += time;
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
				
				// fly to next node
				time = Map.distance[index][next] / SPEED;
				if (computingIndex != -1) {
					if (Map.nodes[computingIndex].calcTimeLeft > time)
						time = Map.nodes[computingIndex].calcTimeLeft;
					Map.nodes[computingIndex].calcTimeLeft = 0;
					complete[computingIndex] = true;
					computingIndex = -1;
				}
				time += Map.nodes[next].getDataTime();
				energy = time * FLY_ENERGY_CONSUME;
				currTime += time;
				energyLeft -= energy;
				index = next;
				computingIndex = index;
				vis[next]++;
			} else {
				timeNext = Map.distance[index][next] / SPEED;
				if (computingIndex == -1) {
					timeCom = 0;
				} else {
					timeCom = Map.nodes[computingIndex].calcTimeLeft;
				}
				timeSta = Math.max(timeNext, timeCom)
						+ Map.nodes[next].getResTime() + Map.distance[next][Map.closeStation[next]] / SPEED;
				energySta = timeSta * FLY_ENERGY_CONSUME;
				
				// fly to station first if energy is inadequate
				if (energySta > energyLeft) {
					int sta = chooseStation(next);
					time = Map.distance[index][sta] / SPEED;
					currTime += time;
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
				
				// fly to next node
				time = Map.distance[index][next] / SPEED;
				if (computingIndex != -1) {
					if (Map.nodes[computingIndex].calcTimeLeft > time)
						time = Map.nodes[computingIndex].calcTimeLeft;
					Map.nodes[computingIndex].calcTimeLeft = 0;
					complete[computingIndex] = true;
					computingIndex = -1;
				}
				time += Map.nodes[next].getResTime();
				energy = time * FLY_ENERGY_CONSUME;
				currTime += time;
				energyLeft -= energy;
				index = next;
				vis[next]++;
				
				if (currTime > Map.nodes[index].getDdl()) {
//					timeConsume = Double.MAX_VALUE;
//					nodeSeq2.clear();
					return false;
				}
			}
			nodeSeq2.add(next);
		}
		// back to any station (closest station)
		int sta = Map.closeStation[index];
		currTime += Map.distance[index][sta] / SPEED;
		nodeSeq2.add(sta);
		
		timeConsume = currTime;
		return true;
	}
	
	private int chooseStation(int next) {
		int sta = Map.closeStation[index];
		double minDis = Map.distance[index][sta] + Map.distance[sta][next];
		double maxDis = energyLeft * SPEED / FLY_ENERGY_CONSUME;
		double dis;
		
		for (int _sta = 0; _sta < Map.staNum; _sta++) {
			if (Map.distance[index][_sta] > maxDis) continue;
			dis = Map.distance[index][_sta] + Map.distance[_sta][next];
			if (dis > minDis) continue;
			if (dis < minDis || Map.distance[_sta][next] < Map.distance[sta][next]) {
				sta = _sta;
				minDis = dis;
			}
		}
		return sta;
	}
	
	public void printNodeSeq() {
		printSequence(nodeSeq);
	}
	
	public void printNodeSeq2() {
		printSequence(nodeSeq2);
	}
	
	public void printSequence(ArrayList<Integer> sequence) {
		int size = sequence.size();
		System.out.print("{" + sequence.get(0));
		for (int i = 1; i < size; i++)
			System.out.print(", " + sequence.get(i));
		System.out.println("}");
	}
}
