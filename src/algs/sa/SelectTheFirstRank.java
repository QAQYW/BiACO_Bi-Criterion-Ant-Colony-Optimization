package algs.sa;

import algs.nsga2.Individual;

import java.util.ArrayList;
import java.util.HashMap;


public class SelectTheFirstRank {
	public static ArrayList<Individual> selectTheFirstRank(Population father) {
		HashMap<Integer, Individual> fatherAndChild = new HashMap<Integer, Individual>();
		fatherAndChild.putAll(father.map);

		fatherAndChild.forEach((key, in) -> {
			in.n = 0;
			in.dominate.clear();
		});
		fatherAndChild.forEach((key1, in) -> {
			fatherAndChild.forEach((key, o) -> {
				if (((in.fitness1 < o.fitness1) && (in.fitness2 < o.fitness2)) || ((in.fitness1 <= o.fitness1) && (in.fitness2 < o.fitness2)) || ((in.fitness1 < o.fitness1) && (in.fitness2 <= o.fitness2)))// 找出in支配的个体放入dominate中
				{
					in.dominate.add(o);
				} else if (((in.fitness1 > o.fitness1) && (in.fitness2 > o.fitness2)) || ((in.fitness1 >= o.fitness1) && (in.fitness2 > o.fitness2)) || ((in.fitness1 > o.fitness1) && (in.fitness2 >= o.fitness2)))// 碰上一个支配in的个体就++n
				{
					in.n++;
				}
			});
		});


		ArrayList<ArrayList<Individual>> rank = new ArrayList<ArrayList<Individual>>();// 对种群进行分级别
		// 外层的ArrayList是存放每一个rank，内层的ArrayList是存放该rank对应的个体

		// 本函数将所有个体进行分级，分级后放入rank数组中

		ArrayList<Individual> po = new ArrayList<Individual>();// 用来存放第x rank的个体，这里的x是while的第x个循环
		for (int i = 0; i < Population.MAXSIZE; i++)// 对哈希表中的每个个体进行遍历
		{
			Individual indi = fatherAndChild.get(i);
			if (indi.n == 0)// 如果发现该个体的n为0，即没有支配该个体的其他个体
			{
				po.add(indi);// 将该个体放入第n rank的集合中
			}
		}
		return po;// 该rank的所有个体都放入ArrayList后将该ArrayList放入对应rank的位置
	}
}
