package biACO;

import java.io.IOException;

import main.RunBatch;
import method.Division;
import method.Roulette;
import pareto.ParetoFront;
import pareto.Solution;
import resource.Map;

public class BiACO {
	
	public static int MAX_ITERATION;
	
	public static int COLONY_NUMBER;
	
	public static Colony[] colonies;
	public static ParetoFront PF;// Pareto front
	
	public BiACO() throws CloneNotSupportedException, IOException {
		// Initialize all colonies and ants
		colonies = new Colony[BiACO.COLONY_NUMBER];
		for (int i = 0; i < BiACO.COLONY_NUMBER; i++) {
			colonies[i] = new Colony((double) i);
		}
		
		Roulette.eta_beta = new double[Map.totNum];
		for (int i = Map.staNum; i < Map.totNum; i++) {
			Roulette.eta_beta[i] = Math.pow(Map.nodes[i].getSlotTime(), -Roulette.beta);
		}
		
		PF = new ParetoFront();
		
		// Generate initial solutions
//		for (int i = 0; i < BiACO.COLONY_NUMBER; i++) {
//			for (int j = 0; j < Colony.ANT_NUMBER; j++) {
//				colonies[i].ants[j].generate(colonies[i]);
//				colonies[i].ants[j].calcFitness();
//				
//				Solution sol = new Solution(colonies[i].ants[j]);
//				if (RunBatch.ct.divMethod.equals("division")) {
//					if (PF.addSol(sol)) {
//						Ant _ant = Division.division(colonies[i].ants[j]);
//						if (_ant != null) {
//							Solution _sol = new Solution(_ant);
//							PF.addSol(_sol);
//						}
//					}
//				} else PF.addSol(sol);
//			}
//		}
//		this.update(PF);
		
		// inter
		int iteration = 0;
		while (iteration < BiACO.MAX_ITERATION) {
			for (int i = 0; i < BiACO.COLONY_NUMBER; i++) {
				for (int j = 0; j < Colony.ANT_NUMBER; j++) {
					colonies[i].ants[j].generate(colonies[i]);
					colonies[i].ants[j].calcFitness();
					
					Solution sol = new Solution(colonies[i].ants[j]);
					if (RunBatch.ct.divMethod.equals("division")) {
						if (PF.addSol(sol)) {
							Ant _ant = Division.division(colonies[i].ants[j]);
							if (_ant != null) {
								Solution _sol = new Solution(_ant);
								PF.addSol(_sol);
							}
						}
					} else PF.addSol(sol);
				}
			}
			this.update(PF);
			
			iteration++;
			
		}
	}
	
	public void update(ParetoFront PF) {
		// evaporation
		for (int i = 0; i < BiACO.COLONY_NUMBER; i++) {
			colonies[i].evaporation();
		}
		
		// sort
		PF.sortPF();
		
		int pfNum = PF.pf.size(), l, r;
		double num = (double) pfNum / BiACO.COLONY_NUMBER;
		for (int i = 0; i < BiACO.COLONY_NUMBER; i++) {
			l = (int) Math.floor(i * num);
			r = (int) Math.ceil(l + num - 1);
			for (int j = l; j <= r; j++) {
				colonies[i].updateTau(PF.pf.get(j), j + 1, pfNum);
			}
		}
	}
	
	public static void print() {
		PF.print();
	}
}
