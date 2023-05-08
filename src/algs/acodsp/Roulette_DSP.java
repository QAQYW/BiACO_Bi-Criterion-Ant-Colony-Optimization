package algs.acodsp;

import java.util.ArrayList;

import resource.Map;

public class Roulette_DSP {
	
	public static double alpha;
	public static double beta;
	
	public double[] poss;
	public double sum;
	
	public Roulette_DSP() {
		poss = new double[Map.totNum];
		sum = 0;
	}
	
	public static double calcPoss(double tau, double eta) {
		double g = (tau - 1) / Pheromone_DSP.TAU_MA;
		return (Math.pow(g, alpha) * Math.pow(eta, beta));
	}
	
	public int choose(ArrayList<Integer> candidateList) {
		sum = 0;
		int size = candidateList.size(), task;
		for (int i = 0; i < size; i++) {
			task = candidateList.get(i);
			sum += poss[task];
		}
		double rand = Math.random() * sum, temp = 0;
		for (int i = 0; i < size; i++) {
			task = candidateList.get(i);
			temp += poss[task];
			if (rand <= temp) return i;
		}
		return (size - 1);
	}
}
