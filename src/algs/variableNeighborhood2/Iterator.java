package algs.variableNeighborhood2;

import algs.Resource.Map;
import algs.nsga2.Individual;

import java.util.ArrayList;
import java.util.HashSet;

public class Iterator {

	public static void iterator(Map map, ArrayList<Individual> arr) throws CloneNotSupportedException {
		HashSet<String> hs = new HashSet<>();
		for (int i = 0; i < arr.size(); i++) {
			Individual a = arr.get(i);
			hs.add(a.fitness1 + " " + a.fitness2);
		}

		//对当前的所有individual进行邻域搜索
		for (int i = 0; i < arr.size(); i++) {
			Individual in = arr.get(i);
			//每个individual搜索一百次
			for (int j = 0; j < 60; j++) {
				Individual clone = in.clone();

				Variation2.variation2(clone, map);
				//如果搜到非支配解放进去
				if ((clone.fitness1 < in.fitness1 && clone.fitness2 >= clone.fitness2) || (clone.fitness1 >= in.fitness1 && clone.fitness2 < clone.fitness2)) {
					if (!hs.contains(clone.fitness1 + " " + clone.fitness2))
						arr.add(clone);

					hs.add(clone.fitness1 + " " + clone.fitness2);
				}

				//如果搜到比当前更好的解，更新
				if (clone.fitness1 < in.fitness1 && clone.fitness2 < in.fitness2) {
					arr.add(clone);
					SelectTheFirstRank.selectTheFirstRank(arr);
					return;
				}

			}
			SelectTheFirstRank.selectTheFirstRank(arr);
		}

	}


	public static void iterator2(Map map, ArrayList<Individual> arr) throws CloneNotSupportedException {
		HashSet<String> hs = new HashSet<>();
		for (int i = 0; i < arr.size(); i++) {
			Individual a = arr.get(i);
			hs.add(a.fitness1 + " " + a.fitness2);
		}

		//对当前的所有individual进行邻域搜索
		for (int i = 0; i < arr.size(); i++) {
			Individual in = arr.get(i);
			//每个individual搜索一百次
			for (int j = 0; j < 60; j++) {
				Individual clone = in.clone();


				Variation3.variation3(clone, map);
				//如果搜到非支配解放进去
				if ((clone.fitness1 < in.fitness1 && clone.fitness2 >= clone.fitness2) || (clone.fitness1 >= in.fitness1 && clone.fitness2 < clone.fitness2)) {
					if (!hs.contains(clone.fitness1 + " " + clone.fitness2))
						arr.add(clone);

					hs.add(clone.fitness1 + " " + clone.fitness2);
				}

				//如果搜到比当前更好的解，更新
				if (clone.fitness1 < in.fitness1 && clone.fitness2 < in.fitness2) {
					arr.add(clone);
					SelectTheFirstRank.selectTheFirstRank(arr);
					return;
				}

			}
			SelectTheFirstRank.selectTheFirstRank(arr);

		}
	}
}
