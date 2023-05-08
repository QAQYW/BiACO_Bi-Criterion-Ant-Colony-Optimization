package algs.compare;

import algs.nsga2.Individual;

import java.util.ArrayList;

public class IndividualToPF {
	//come是这个来自于哪个方法
	public static PFSet individualtoPF(ArrayList<Individual> arr, String come) {
		PFSet pfSet = new PFSet();
		for (Individual in : arr) {
			PF pf = new PF(in.fitness1, in.fitness2, come);
			pfSet.addPF(pf);
		}
		return pfSet;
	}
}
