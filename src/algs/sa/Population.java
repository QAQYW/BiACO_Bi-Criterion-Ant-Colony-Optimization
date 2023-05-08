package algs.sa;

import algs.Resource.Map;
import algs.nsga2.Individual;
import algs.nsga2.InitIndividual;

import java.util.HashMap;


public class Population {
	public static final int MAXSIZE = 100;//种群数量
	//这里的MATH.pow(2,9)是提前设定好hashmap的大小，避免重构
	public HashMap<Integer, Individual> map = new HashMap<Integer, Individual>((int) Math.pow(2, 10));//用于存放该种群的所有个体


	public void init(Map m) throws CloneNotSupportedException {

		for (int i = 0; i < Population.MAXSIZE; i++) {

			Individual in = new Individual();
			InitIndividual.initIndividual(in, m);
			if (in.fitness1 > Iterator.maxFitness1) Iterator.maxFitness1 = in.fitness1;
			if (in.fitness2 > Iterator.maxFitness2) Iterator.maxFitness2 = in.fitness2;
			if (in.fitness1 < Iterator.minFitness1) Iterator.minFitness1 = in.fitness1;
			if (in.fitness2 < Iterator.minFitness2) Iterator.minFitness2 = in.fitness2;
			map.put(i, in);
		}
	}
}
