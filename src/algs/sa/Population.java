package algs.sa;

import algs.Resource.Map;
import algs.nsga2.Individual;
import algs.nsga2.InitIndividual;

import java.util.HashMap;


public class Population {
	public static final int MAXSIZE = 100;//��Ⱥ����
	//�����MATH.pow(2,9)����ǰ�趨��hashmap�Ĵ�С�������ع�
	public HashMap<Integer, Individual> map = new HashMap<Integer, Individual>((int) Math.pow(2, 10));//���ڴ�Ÿ���Ⱥ�����и���


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
