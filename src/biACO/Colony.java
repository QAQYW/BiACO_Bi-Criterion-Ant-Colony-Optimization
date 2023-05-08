package biACO;

import pareto.Solution;
import resource.Map;
import resource.UAV;

public class Colony {
	
	public static int ANT_NUMBER;	// number of ants in each colony
	public static double DELTA_TAU;
	public static double Q;			// pheromone const
	
	public static double RHO;		// evaporation
	public static double STAY;		// 1 - RHO
	public static double TAU_INITIAL_VALUE;		// initial value of tau (pheromone matrices)
	public static double MAX_BREAK_PROBABILITY;	// upper bound of break probability
	public static double MIN_BREAK_PROBABILITY;	// lower bound of break probability
	
	public Ant[] ants;
	public double[][][] tau1;	// corresponding to fitness1
	public double[][][] tau2;	// corresponding to fitness2
	public double[] tauFT1;
	public double[] tauFT2;
	
	private double breakProb;
	
	public double getBreakProb() {
		return breakProb;
	}
	
	public Colony(double index) {
		double minLambda = index / (BiACO.COLONY_NUMBER + 1);
		double maxLambda = (index + 2) / (BiACO.COLONY_NUMBER + 1);
		
		breakProb = (MAX_BREAK_PROBABILITY - MIN_BREAK_PROBABILITY) / (BiACO.COLONY_NUMBER - 1) * index + MIN_BREAK_PROBABILITY;
		
		ants = new Ant[ANT_NUMBER];
		for (int i = 0; i < ANT_NUMBER; i++) {
			ants[i] = new Ant();
			ants[i].setH((double) i);
			ants[i].setLambda(ants[i].getH() / (BiACO.COLONY_NUMBER - 1) * (maxLambda - minLambda) + minLambda);
		}
		
		tau1 = new double[Map.totNum][Map.totNum][4];
		tau2 = new double[Map.totNum][Map.totNum][4];
		for (int i = 0; i < Map.totNum; i++)
			for (int j = 0; j < Map.totNum; j++)
				for (int k = 0; k < 4; k++) {
					tau1[i][j][k] = TAU_INITIAL_VALUE;
					tau2[i][j][k] = TAU_INITIAL_VALUE;
				}
		tauFT1 = new double[Map.totNum];
		tauFT2 = new double[Map.totNum];
		for (int i = 0; i < Map.totNum; i++) {
			tauFT1[i] = TAU_INITIAL_VALUE;
			tauFT2[i] = TAU_INITIAL_VALUE;
		}
	}
	
	/*
	 * 搞一个虚拟节点
	 * dummy node 的 index 为 totNum
	 */
	
	
	public void evaporation() {
		for (int i = 0; i < Map.totNum; i++)
			for (int j = 0; j < Map.totNum; j++)
				for (int k = 0; k < 4; k++) {
					tau1[i][j][k] *= STAY;
					tau2[i][j][k] *= STAY;
				}
		for (int i = 0; i < Map.totNum; i++) {
			tauFT1[i] *= STAY;
			tauFT2[i] *= STAY;
		}
	}
	
	public void updateTau(Solution sol, int rank, int pfNum) {
		double dTau = DELTA_TAU;
		double p1 = (double) rank / (double) pfNum;// proportion
		double p2 = 1.0 - p1;
		
		int[] vis = new int[Map.totNum];
		for (int i = 0; i < Map.totNum; i++) vis[i] = 0;
		
		for (UAV uav : sol.uavList) {
			// tau
			int len = uav.nodeSeq.size();
			int u, v = uav.nodeSeq.get(0), state;
			for (int i = 1; i < len; i++) {
				u = v;
				v = uav.nodeSeq.get(i);
				state = (vis[u] << 1) + vis[v];
				tau1[u][v][state] += dTau * p1;
				tau2[u][v][state] += dTau * p2;
				vis[u]++;
			}
			// tauFT
			int ft = uav.nodeSeq.get(0);
			tauFT1[ft] += dTau * p1;
			tauFT2[ft] += dTau * p2;
		}
	}
}
