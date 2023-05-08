package batchExp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Algorithm information
 * @author wyqaq
 *
 */
public class AlgInfo {
	
	public static double[] lambda;
	// 0/50, 1/50, ..., 50/50
	
	public int algIndex;
	public CtrlInfo ct;
	public int pfSize;
	public ArrayList<Fitness> PF;
	public ArrayList<Fitness> intersaction;
	public double runtime;	// unit: second (s)
	public Indicators indi;	// 4 indicators
	
//	public static double ACCURACY = 0.001;
	
	
	public AlgInfo() {
//		algIndex = alg;
		pfSize = 0;
		PF = new ArrayList<Fitness>();
		intersaction = new ArrayList<Fitness>();
		indi = new Indicators();
	}
	
	public AlgInfo(double time) {
		pfSize = 0;
		PF = new ArrayList<Fitness>();
		intersaction = new ArrayList<Fitness>();
		indi = new Indicators();
		runtime = time;
	}
	
	public AlgInfo(int alg, double time) {
		algIndex = alg;
		pfSize = 0;
		PF = new ArrayList<Fitness>();
		intersaction = new ArrayList<Fitness>();
		indi = new Indicators();
		runtime = time;
	}
	
	/**
	 * at ascending order of f1 (fitness1), 
	 * that is descending order of f2 (fitness2)<br>
	 * for 'Fitness', not for 'Solution'
	 */
	public void sortPF() {
		Collections.sort(PF, new Comparator<Fitness>() {
			@Override
			public int compare(Fitness fn1, Fitness fn2) {
				if (fn1.fitness1 == fn2.fitness1) {
					if (fn1.fitness2 == fn2.fitness2) {//return 0;
						if (fn1.from == fn2.from) return 0;
						return fn1.from < fn2.from ? (-1) : 1;
					}
					return fn1.fitness2 < fn2.fitness2 ? (-1) : 1;
				}
				return fn1.fitness1 < fn2.fitness1 ? (-1) : 1;
			}
		});
	}
	
	/**
	 * Call sortPF() before unique()
	 */
	public void unique() {
		int p = PF.size() - 1;
		while (p > 0) {
			if (PF.get(p).fitness1 == PF.get(p - 1).fitness1 && PF.get(p).fitness2 == PF.get(p -1).fitness2 && PF.get(p).from == PF.get(p - 1).from)
				PF.remove(p);
			p--;
		}
		pfSize = PF.size();
	}
	
	public void normalization(double max1, double min1, double max2, double min2) {
		double len1 = max1 - min1;
		double len2 = max2 - min2;
		for (Fitness fn : PF) {
			if (fn.fitness1 > max1 || fn.fitness1 < min1) {
				System.out.println("Normalization Error - Range Error - Fitness1 Error");
				System.out.println("fitness1 = " + fn.fitness1);
				System.out.println("range is [" + min1 + "," + max1 + "]");
			} else {
				fn.norf1 = (fn.fitness1 - min1) / len1;
			}
			if (fn.fitness2 > max2 || fn.fitness2 < min2) {
				System.out.println("Normalization Error - Range Error - Fitness2 Error");
				System.out.println("fitness2 = " + fn.fitness2);
				System.out.println("range is [" + min2 + "," + max2 + "]");
			} else {
				fn.norf2 = (fn.fitness2 - min2) / len2;
			}
		}
	}
	
	public void getIntersaction(AlgInfo optimal) {
		int size = optimal.PF.size();
		intersaction.clear();
		for (int i = 0; i < size; i++) {
			if (optimal.PF.get(i).from == algIndex) {
				intersaction.add(optimal.PF.get(i));
			}
		}
	}
	
	private void calcAQ() {
		indi.aq = 0;
		int length = lambda.length;
		for (int i = 0; i < length; i++)
			indi.aq += score(lambda[i]);
		indi.aq /= (double) length;
	}
	
	private void calcMS(AlgInfo optimal) {
		// Get the intersaction between this PF and the optimal PF
		getIntersaction(optimal);
		
		// Max and min value of optimal PF
		double max1op = optimal.PF.get(optimal.pfSize - 1).norf1;
		double min1op = optimal.PF.get(0).norf1;
		double max2op = optimal.PF.get(0).norf2;
		double min2op = optimal.PF.get(optimal.pfSize - 1).norf2;
		
		// Max and min value of the intersaction
		int inSize = intersaction.size();
		double max1in = intersaction.get(inSize - 1).norf1;
		double min1in = intersaction.get(0).norf1;
		double max2in = intersaction.get(0).norf2;
		double min2in = intersaction.get(inSize - 1).norf2;
		
		// MS
		double temp1 = (max1in - min1in) / (max1op - min1op);
		double temp2 = (max2in - min2in) / (max2op - min2op);
		indi.ms = Math.sqrt(0.5 * temp1 * temp1 + 0.5 * temp2 * temp2);
	}
	
	private void calcDavgDmax(AlgInfo optimal) {
		// Max and min value of optimal PF
		double max1op = optimal.PF.get(optimal.pfSize - 1).norf1;
		double min1op = optimal.PF.get(0).norf1;
		double max2op = optimal.PF.get(0).norf2;
		double min2op = optimal.PF.get(optimal.pfSize - 1).norf2;
		
		// Max and min value of this PF
		double max1th = PF.get(pfSize - 1).norf1;
		double min1th = PF.get(0).norf1;
		double max2th = PF.get(0).norf2;
		double min2th = PF.get(pfSize - 1).norf2;
		
		// Max and min value among solutions in this two PF
		double max1 = Math.max(max1op, max1th);
		double min1 = Math.min(min1op, min1th);
		double max2 = Math.max(max2op, max2th);
		double min2 = Math.min(min2op, min2th);
		
		// Range
		double range1 = max1 - min1;
		double range2 = max2 - min2;
		
		// Davg and Dmax
		double dis, dis1, dis2;
		double[] minDis = new double[optimal.pfSize];
		for (int i = 0; i < optimal.pfSize; i++) {
			// j = 0
			dis1 = PF.get(0).norf1 - optimal.PF.get(i).norf1;
			dis2 = PF.get(0).norf2 - optimal.PF.get(i).norf2;
			minDis[i] = Math.max(dis1 / range1, dis2 / range2);
			// j > 0
			for (int j = 1; j < pfSize; j++) {
				dis1 = PF.get(j).norf1 - optimal.PF.get(i).norf1;
				dis2 = PF.get(j).norf2 - optimal.PF.get(i).norf2;
				dis = Math.max(dis1 / range1, dis2 / range2);
				minDis[i] = Math.min(minDis[i], dis);
			}
		}
		indi.d_avg = minDis[0];
		indi.d_max = minDis[0];
		for (int i = 1; i < optimal.pfSize; i++) {
			indi.d_avg += minDis[i];
			indi.d_max = Math.max(indi.d_max, minDis[i]);
		}
		indi.d_avg /= (double) optimal.pfSize;
	}
	
	public void calcIndi(double max1, double min1, double max2, double min2, AlgInfo optimal) {
		pfSize = PF.size();
		calcAQ();
		calcMS(optimal);
		calcDavgDmax(optimal);
//		pfSize = PF.size();//---------------------------------
//		
//		double maxn1 = optimal.PF.get(optimal.pfSize - 1).norf1;//---------------------------------
//		double minn1 = optimal.PF.get(0).norf1;//---------------------------------
//		double maxn2 = optimal.PF.get(0).norf2;//---------------------------------
//		double minn2 = optimal.PF.get(optimal.pfSize - 1).norf2;//---------------------------------
//		double dn1 = maxn1 - minn1;//---------------------------------
//		double dn2 = maxn2 - minn2;//---------------------------------
//		
//		normalization(max1, min1, max2, min2);//---------------------------------
//		
//		// calculate AQ
//		indi.aq = 0;
//		int len = lambda.length;
//		for (int i = 0; i < len; i++) {
//			indi.aq += score(lambda[i]);
//		}
//		indi.aq /= (double) len;
//		
//		// calculate MS
//		getIntersaction(optimal);
//		int inSize = intersaction.size();
//		if (inSize == 0) {
//			indi.ms = 0;
//		} else  if (max1 - min1 == 0 || max2 - min2 == 0) {
//			indi.ms = 1;
//		} else {
//			double inMax1 = intersaction.get(inSize - 1).norf1;
//			double inMin1 = intersaction.get(0).norf1;
//			double inMax2 = intersaction.get(0).norf2;
//			double inMin2 = intersaction.get(inSize - 1).norf2;
//			indi.ms = Math.sqrt(0.5 * Math.pow((inMax1 - inMin1) / (max1 - min1), 2) + 0.5 * Math.pow((inMax2 - inMin2) / (max2 - min2), 2));
//		}
//		
//		// calculate davg and dmax
//		// g means global (normalized)
//		double max1g = Math.max(maxn1, PF.get(pfSize - 1).norf1);//---------------------------------
//		double min1g = Math.min(minn1, PF.get(0).norf1);//---------------------------------
//		double max2g = Math.max(maxn2, PF.get(0).norf2);//---------------------------------
//		double min2g = Math.min(minn2, PF.get(pfSize - 1).norf2);//---------------------------------
//		double dn1g = max1g - min1g;//---------------------------------
//		double dn2g = max2g - min2g;//---------------------------------
//		double dis1, dis2;
//		double[] minDis = new double[optimal.pfSize];
//		HashSet<String> explored = new HashSet<String>();
//		for (int i = 0; i < optimal.pfSize; i++) {
//			String str = optimal.PF.get(i).stringHash();
//			if (explored.contains(str)) continue;
//			explored.add(str);
//			
//			// j = 0
////			dis1 = PF.get(0).norf1 - optimal.PF.get(i).norf1;
////			dis2 = PF.get(0).norf2 - optimal.PF.get(i).norf2;
//			if (dn1g == 0) dis1 = 0;
//			else dis1 = (PF.get(0).norf1 - optimal.PF.get(i).norf1) / dn1g;
//			if (dn2g == 0) dis2 = 0;
//			else dis2 = (PF.get(0).norf2 - optimal.PF.get(i).norf2) / dn2g;
//			minDis[i] = Math.max(dis1, dis2);
//			
//			for (int j = 1; j < pfSize; j++) {
//				if (dn1g == 0) dis1 = 0;
//				else dis1 = (PF.get(j).norf1 - optimal.PF.get(i).norf1) / dn1g;
//				if (dn2g == 0) dis2 = 0;
//				else dis2 = (PF.get(j).norf2 - optimal.PF.get(i).norf2) / dn2g;
//				minDis[i] = Math.min(minDis[i], Math.max(dis1, dis2));
//			}
//		}
//		indi.d_avg = minDis[0];
//		indi.d_max = minDis[0];
//		for (int i = 1; i < optimal.pfSize; i++) {
//			indi.d_avg += minDis[i];
//			indi.d_max = Math.max(indi.d_max, minDis[i]);
//		}
//		indi.d_avg /= (double) optimal.pfSize;
	}
	
	private double score(double _lambda) {
		double s1 = _lambda * PF.get(0).norf1;
		double s2 = (1 - _lambda) * PF.get(0).norf2;
		double s = Math.max(s1, s2) + 0.01 * (s1 + s2);// i = 0
		double t;
		for (int i = 1; i < pfSize; i++) {
			s1 = _lambda * PF.get(i).norf1;
			s2 = (1 - _lambda) * PF.get(i).norf2;
			t = Math.max(s1, s2) + 0.01 * (s1 + s2);
			s = Math.min(s, t);
		}
		return s;
	}
}
