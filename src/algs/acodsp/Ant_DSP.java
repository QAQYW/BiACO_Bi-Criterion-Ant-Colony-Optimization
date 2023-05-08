package algs.acodsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import resource.Map;

public class Ant_DSP {
	
	public int uavNum;
	public ArrayList<UAV_DSP> uavList;
	public ArrayList<Integer> unscheduledTask;
	public ArrayList<Integer> candidateList;
	
	public double fitness1;
	public double fitness2;
//	public double fitnessW;
//	public static double weightedParameter;
//	public static double[] weightedSet = {5, 10, 20, 30, 50, 100};
	public static double timeWindowLength;
	public static double[] windowLengthSet = {500, 1000, 5000, 10000, 20000, 30000};
	
	public static int reservedUAVNum;
	public static int priceMag;
	
	public Ant_DSP() {
		uavNum = 0;
		uavList = new ArrayList<UAV_DSP>();
		unscheduledTask = new ArrayList<Integer>();
		candidateList = new ArrayList<Integer>();
	}
	
	public void init() {
		uavNum = 0;
		uavList.clear();
		unscheduledTask.clear();
		for (int i = Map.staNum; i < Map.totNum; i++)
			unscheduledTask.add(i);
	}
	
	public void generatePath() throws CloneNotSupportedException {
		init();
		int countUAV = 0, phIndex;
		
		while (!unscheduledTask.isEmpty()) {
			phIndex = countUAV++;
			UAV_DSP uav = new UAV_DSP(phIndex);
			
			if (countUAV > ACO_DSP.pheromoneMatrixNum) {
				ACO_DSP.pheromone[ACO_DSP.pheromoneMatrixNum] = new Pheromone_DSP();
				ACO_DSP.pheromoneMatrixNum = countUAV;
			}
			
			candidateList.clear();
			candidateList.addAll(unscheduledTask);
//			int pos = chooseFirstTask(uav, phIndex);
			int pos = chooseTask(uav, phIndex);
			int task = candidateList.get(pos);
			
			uav.nodeSeq.add(task);
			uav.nodeSeq.add(task);
			unscheduledTask.remove((Integer) task);
			uav.boundaryParameter = 0;
			
			while (!unscheduledTask.isEmpty()) {
				int trials = Math.min(unscheduledTask.size(), 10);
				candidateList.clear();
				candidateList.addAll(unscheduledTask);
				
				while (trials > 0) {
//					pos = chooseNextTask(uav, phIndex);
					pos = chooseTask(uav, phIndex);
					task = candidateList.get(pos);
					
					int insertPos = insertCheck(task, uav);
					if (insertPos != -1) {
						uav.nodeSeq.add(uav.boundaryParameter + 1, task);
						uav.nodeSeq.add(insertPos, task);
						unscheduledTask.remove((Integer) task);
						break;
					} else {
						trials--;
						candidateList.remove(pos);
					}
				}
				
				uav.boundaryParameter++;
				if (uav.boundaryParameter == uav.nodeSeq.size())
					break;
			}
			uavList.add(uav);
		}
		uavNum = countUAV;
	}
	
	/*
	public int chooseFirstTask(UAV_DSP uav, int phIndex) {
		Roulette_DSP roulette = new Roulette_DSP();
		int size = candidateList.size(), task;
		for (int i = 0; i < size; i++) {
			task = candidateList.get(i);
			double tau = ACO_DSP.pheromone[phIndex].tau[uav.nodeSeq.get(uav.boundaryParameter)][task];
			double eta = 1.0 / Map.nodes[task].getSlotTime();
			roulette.poss[task] = Roulette_DSP.calcPoss(tau, eta);
		}
		int pos = roulette.choose(candidateList);
		return pos;
	}
	
	public int chooseNextTask(UAV_DSP uav, int phIndex) {
		Roulette_DSP roulette = new Roulette_DSP();
		int size = candidateList.size(), task;
		for (int i = 0; i < size; i++) {
			task = candidateList.get(i);
			double tau = ACO_DSP.pheromone[phIndex].tau[uav.nodeSeq.get(uav.boundaryParameter)][task];
			double eta = 1.0 / Map.nodes[task].getSlotTime();
			roulette.poss[task] = Roulette_DSP.calcPoss(tau, eta);
		}
		int pos = roulette.choose(candidateList);
		return pos;
	}*/
	
	public int chooseTask(UAV_DSP uav, int phIndex) {
		Roulette_DSP roulette = new Roulette_DSP();
		int size = candidateList.size(), task;
		for (int i = 0; i < size; i++) {
			task = candidateList.get(i);
			double tau = ACO_DSP.pheromone[phIndex].tau[uav.nodeSeq.get(uav.boundaryParameter)][task];
			double eta = 1.0 / Map.nodes[task].getSlotTime();
			roulette.poss[task] = Roulette_DSP.calcPoss(tau, eta);
		}
		int pos = roulette.choose(candidateList);
		return pos;
	}
	
	public int insertCheck(int task, UAV_DSP uav) throws CloneNotSupportedException {
		UAV_DSP _uav = uav.clone();
		_uav.nodeSeq.add(_uav.boundaryParameter + 1, task);
		
		int size = _uav.nodeSeq.size();
		ArrayList<Integer> candidatePosition = new ArrayList<Integer>();
		for (int i = _uav.boundaryParameter + 2; i <= size; i++)
			candidatePosition.add(i);
		Collections.shuffle(candidatePosition);
		int bestPos = -1;
		double minTime = Double.MAX_VALUE;
		
		int ii = Math.min(candidatePosition.size(), 5);
		for (int i = ii - 1; i >= 0; i--) {
			int pos = candidatePosition.get(i);
			_uav.nodeSeq.add(pos, task);
			if (_uav.calcTimeConsume()) {
				if (_uav.timeConsume <= timeWindowLength && _uav.timeConsume < minTime) {
					bestPos = pos;
					minTime = _uav.timeConsume;
				}
			}
			_uav.nodeSeq.remove(pos);
		}
		
		return bestPos;
	}
	
	public void calcFitness() {
		fitness1 = 0;
		fitness2 = 0;
		for (UAV_DSP _uav : uavList) {
			_uav.calcTimeConsume();
			if (_uav.timeConsume == Double.MAX_VALUE) {
				System.out.println("ERROR - Illegal trajectory");
			}
		}
		
		Collections.sort(uavList, new Comparator<UAV_DSP>() {
			@Override
			public int compare(UAV_DSP u1, UAV_DSP u2) {
				// TODO Auto-generated method stub
				if (u1.timeConsume == u2.timeConsume) return 0;
				return u1.timeConsume > u2.timeConsume ? (-1) : 1;
			}
		});
		
		double _time;
		for (int i = 0; i < uavList.size(); i++) {
			_time = uavList.get(i).timeConsume;
			fitness1 += _time * (i >= reservedUAVNum ? priceMag : 1);
			fitness2 = Math.max(fitness2, _time);
		}
	}
}
