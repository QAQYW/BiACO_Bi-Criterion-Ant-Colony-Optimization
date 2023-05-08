package method;

import java.util.ArrayList;

import resource.Map;

public class Roulette {
	
	// preferences for pheromone matrix and heuristic matrix
	public static double alpha;	// for pheromone matrix
	public static double beta;	// for heuristic matrix
	
	// assist calculating
	public static double[] eta_beta;
	
	// for Roulette choose
	public double[] poss;	// probability for every candidate task
	public double sum;		// sum of this array
	
	
	public Roulette() {
		poss = new double[Map.totNum];
		sum = 0;
	}
	
	public void init() {
		poss = new double[Map.totNum];
		sum = 0;
	}
	
//	public static double calcProbForFirstTask(double eta) {
//		return Math.pow(eta, beta);
//	}
	
	public static double calcProbForFirstTask(double tau1, double tau2, double eta_beta, double lambda) {
		double tau = Math.pow(tau1, lambda) * Math.pow(tau2, 1.0 - lambda);
		return (Math.pow(tau, alpha) * eta_beta);
	}
	
	public static double calcProbForNextTask(double tau1, double tau2, double eta, double lambda) {
		double tau = Math.pow(tau1, lambda) * Math.pow(tau2, 1.0 - lambda);
		return (Math.pow(tau, alpha) * Math.pow(eta, beta));
	}
	
	/**
	 * Returns the index of selected task in waiting (ArrayList)
	 * @param waiting
	 * @param tabu
	 * @return
	 */
	public int choose(ArrayList<Integer> waiting, ArrayList<Integer> tabu) {
		int size = waiting.size();
		if (tabu.isEmpty()) {
			sum = 0;
			for (int i = 0; i < size; i++)
				sum += poss[waiting.get(i)];
			double rand = Math.random() * sum, temp = 0;
			for (int i = 0; i < size; i++) {
				temp += poss[waiting.get(i)];
				if (rand <= temp)
					return i;
			}
			return size - 1;
		}
		
		int j = 0, tabuSize = tabu.size(), task, lastPos = -1;
		double rand = Math.random() * sum, temp = 0;
		for (int i = 0; i < size; i++) {
			task = waiting.get(i);
			if (j < tabuSize && tabu.get(j) == task) {
				++j;
				continue;
			}
			temp += poss[task];
			if (rand <= temp)
				return i;
			lastPos = i;
		}
		return lastPos;
	}
}
