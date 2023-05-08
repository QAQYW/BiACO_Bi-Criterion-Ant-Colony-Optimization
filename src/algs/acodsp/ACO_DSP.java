package algs.acodsp;

import java.io.IOException;

import pareto.ParetoFront;
import pareto.Solution;
import resource.Map;
import resource.UAV;

public class ACO_DSP {
	
	public static int MAX_ITERATION = 250;
	public static int ANT_NUMBER = 50;
	
	public static Ant_DSP[] ants;
	
	public static int pheromoneMatrixNum = 0;
	public static Pheromone_DSP[] pheromone;
	
	public static ParetoFront PF;
	public static ParetoFront globalPF;
	
	public ACO_DSP() throws CloneNotSupportedException, IOException {
		// Initialize all ants
		ants = new Ant_DSP[ACO_DSP.ANT_NUMBER];
		for (int i = 0; i < ACO_DSP.ANT_NUMBER; i++)
			ants[i] = new Ant_DSP();
		pheromoneMatrixNum = 0;
		pheromone = new Pheromone_DSP[Map.taskNum];
		
		PF = new ParetoFront();
		globalPF = new ParetoFront();
//		System.out.println("Initialized!");
		
		int arrayLen = Ant_DSP.windowLengthSet.length;
		for (int wl = 0; wl < arrayLen; wl++) {
			
			long startMili = System.currentTimeMillis();
			
			System.out.println("now wl = " + Integer.toString(wl));
			Ant_DSP.timeWindowLength = Ant_DSP.windowLengthSet[wl];
			pheromoneMatrixNum = 0;
			PF.pf.clear();
			
			int iter = 0;
			while (iter < ACO_DSP.MAX_ITERATION) {
				for (int i = 0; i < ACO_DSP.ANT_NUMBER; i++) {
					ants[i].generatePath();
					ants[i].calcFitness();
					Solution sol = new Solution(ants[i]);
					PF.addSol(sol);
				}
				
				updatePheromone();
				iter++;
				
				long currMili = System.currentTimeMillis();
				double duration = (double) (currMili - startMili) / 1000.0;
				if (duration > 10000.0 / (double) arrayLen) break;
			}
			for (Solution sol : PF.pf) {
				globalPF.addSol(sol);
			}
		}
		
		for (Pheromone_DSP ph : pheromone) {
			ph = null;
		}
	}
	
	public void updatePheromone() {
		int bestSolIndex = selectBestSolution();
		if (bestSolIndex == -1) return;
		Solution sol = PF.pf.get(bestSolIndex);
		
		int index, next, phIndex;
		boolean[][] isUp = new boolean[Map.totNum][Map.totNum];
		for (UAV uav : sol.uavList) {
			phIndex = uav.getPhIdex();
			for (int i = 0; i < Map.totNum; i++) {
				for (int j = 0; j < Map.totNum; j++) {
					isUp[i][j] = false;
				}
			}
			
			index = uav.nodeSeq2.get(0);
			next = uav.nodeSeq.get(0);
			ACO_DSP.pheromone[phIndex].tau[index][next] += Pheromone_DSP.TAU_UP;
			if (ACO_DSP.pheromone[phIndex].tau[index][next] > Pheromone_DSP.MAX_TAU)
				ACO_DSP.pheromone[phIndex].tau[index][next] = Pheromone_DSP.MAX_TAU;
			isUp[index][next] = true;
			int size = uav.nodeSeq.size();
			for (int i = 1; i < size; i++) {
				index = next;
				next = uav.nodeSeq.get(i);
				ACO_DSP.pheromone[phIndex].tau[index][next] += Pheromone_DSP.TAU_UP;
				if (ACO_DSP.pheromone[phIndex].tau[index][next] > Pheromone_DSP.MAX_TAU)
					ACO_DSP.pheromone[phIndex].tau[index][next] = Pheromone_DSP.MAX_TAU;
				isUp[index][next] = true;
			}
			
			for (int i = 0; i < Map.totNum; i++) {
				for (int j = 0; j < Map.totNum; j++) {
					if (isUp[i][j]) continue;
					ACO_DSP.pheromone[phIndex].tau[i][j] -= Pheromone_DSP.TAU_DOWN;
					if (ACO_DSP.pheromone[phIndex].tau[i][j] < Pheromone_DSP.MIN_TAU)
						ACO_DSP.pheromone[phIndex].tau[i][j] = Pheromone_DSP.MIN_TAU;
				}
			}
		}
	}
	
	public int selectBestSolution() {
		int size = PF.pf.size();
		if (size == 0) return -1;
		if (size == 0) return 0;
		PF.unique();
		return 0;
	}
}
