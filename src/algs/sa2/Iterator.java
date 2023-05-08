package algs.sa2;

import algs.Resource.Map;
import algs.nsga2.Individual;

import java.util.ArrayList;


public class Iterator {
	public static double T = 1000;

	public static double coefficient = 0.9999;//0.99999;
	public static double maxFitness1 = Double.MIN_VALUE;
	public static double minFitness1 = Double.MAX_VALUE;
	public static double maxFitness2 = Double.MIN_VALUE;
	public static double minFitness2 = Double.MAX_VALUE;

	public static void iterator(Individual in, Map map, ArrayList<Individual> list) throws CloneNotSupportedException {

		/*
		 * 接下来执行复制操作
		 */
		Individual individual;
		individual = in.clone();

		if (in.fitness1 > maxFitness1) maxFitness1 = in.fitness1;
		if (in.fitness2 > maxFitness2) maxFitness2 = in.fitness2;
		if (in.fitness1 < minFitness1) minFitness1 = in.fitness1;
		if (in.fitness2 < minFitness2) minFitness2 = in.fitness2;

		Variation2.variation2(individual, map);

		if (individual.fitness1 > maxFitness1) maxFitness1 = individual.fitness1;
		if (individual.fitness2 > maxFitness2) maxFitness2 = individual.fitness2;
		if (individual.fitness1 < minFitness1) minFitness1 = individual.fitness1;
		if (individual.fitness2 < minFitness2) minFitness2 = individual.fitness2;

		if ((individual.fitness1 < in.fitness1 && individual.fitness2 < in.fitness2) ||
				(individual.fitness1 <= in.fitness1 && individual.fitness2 < in.fitness2) ||
				(individual.fitness1 < in.fitness1 && individual.fitness2 <= in.fitness2)) {
			in.uav = individual.uav;
			in.fitness1 = individual.fitness1;
			in.fitness2 = individual.fitness2;
			SaMain1.cnt = 0;
		} else {
			float rand = (float) Math.random();
			float deltFitness1 = (float) Math.pow((individual.fitness1 - minFitness1) / (maxFitness1 - minFitness1), 2);
			float deltFitness2 = (float) Math.pow((individual.fitness2 - minFitness2) / (maxFitness2 - minFitness2), 2);

			float de = (float) Math.pow(deltFitness1 + deltFitness2, 0.5);
			double w = Math.pow(Math.E, -de / T);
//			System.out.println("概率为:" + w);

			if (rand < w) {
				in.uav.clear();
				in.uav = individual.uav;
				in.fitness1 = individual.fitness1;
				in.fitness2 = individual.fitness2;
				SaMain1.cnt = 0;
			}
			else SaMain1.cnt++;
		}

		list.add(individual.clone());
		SelectTheFirstRank.selectTheFirstRank(list);
		if (!list.contains(in)) {
			list.add(in);
		}


		T *= coefficient;

	}
}
