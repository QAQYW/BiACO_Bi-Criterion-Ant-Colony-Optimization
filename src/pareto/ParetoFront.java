package pareto;

//import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ParetoFront {
	
//	public static String ROOT = "D\\Desktop\\DataSpecific_Folder\\result";
	
	public ArrayList<Solution> pf;
	
	public ParetoFront() {
		pf = new ArrayList<Solution>();
	}
	
	/**
	 * Sort pf at ascending order of fitness1, i.e., descending order of fitness2<br>
	 * for 'Solution'
	 */
	public void sortPF() {
		Collections.sort(pf, new Comparator<Solution>() {
			@Override
			public int compare(Solution s1, Solution s2) {
//				if (s1.fitness1 == s2.fitness1) {
//				}
				if (s1.fitness1 == s2.fitness1 && s1.fitness2 == s2.fitness2)
					return 0;
				return s1.fitness1 < s2.fitness1 ? (-1) : 1;
			}
		});
	}
	
	/**
	 * try to add sol to pf
	 * @param sol
	 * @return
	 * 	true: add sol to pf successfully, and remove some dominated solutions<br>
	 * 	false: fail to add sol, because it is dominated by certain solution in pf
	 */
	public boolean addSol(Solution sol) {
		int size = pf.size(), cmp, pos;
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			cmp = sol.isDominated(pf.get(i));
			if (cmp == -1)
				return false;
			if (cmp == 1)
				removeList.add(i);
		}
		for (int i = removeList.size() - 1; i >= 0; i--) {
			pos = removeList.get(i);
			pf.remove(pos);
		}
		pf.add(sol);
		return true;
	}
	
	/**
	 * combine two ParetoFronts, non-dominated solutions are kept
	 * @param newPF
	 */
	public void combine(ParetoFront newPF) {
		for (Solution sol : newPF.pf) {
			this.addSol(sol);
		}
	}
	
	/**
	 * remove duplicate elements<br>
	 * includes the method sortPF(), no need to call sortPF() before
	 */
	public void unique() {
		sortPF();
		int size = pf.size();
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		for (int i = 1; i < size; i++) {
			if (pf.get(i).isSame(pf.get(i - 1)))
				removeList.add(i);
		}
		size = removeList.size();
		for (int i = size - 1; i >= 0; i--) {
			pf.remove((int) removeList.get(i));
		}
	}
	
	public void print() {
		for (Solution sol : pf) {
			sol.print();
		}
	}
}
