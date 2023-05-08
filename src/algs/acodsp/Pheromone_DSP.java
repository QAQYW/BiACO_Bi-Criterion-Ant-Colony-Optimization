package algs.acodsp;

import resource.Map;

public class Pheromone_DSP {
	
	public static double INITIAL_TAU;
	
	public static double MAX_TAU;
	public static double MIN_TAU;
	
	public static double TAU_UP;
	public static double TAU_DOWN;
	
	public static double TAU_MA;
	
	public double[][] tau;
	
	public Pheromone_DSP() {
		tau = new double[Map.totNum][Map.totNum];
		for (int i = 0; i < Map.totNum; i++)
			for (int j = 0; j < Map.totNum; j++)
				tau[i][j] = Pheromone_DSP.INITIAL_TAU;
	}
}
