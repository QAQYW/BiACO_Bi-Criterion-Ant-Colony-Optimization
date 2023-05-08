package algs.subiterations;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness;

import java.util.Collections;

public class SubSa {
	public static void subSa(UAV u, Map map) throws CloneNotSupportedException {
		if (u.nodeSet.size() < 2) return;
		UAV ux = new UAV();
		ux.nodeSequence.addAll(u.nodeSequence);
		ux.nodeSet.addAll(u.nodeSet);
		float T = 1;
		float multiplier = (float) 0.98;

		while (T > 0.0001) {
			int x = (int) (u.nodeSequence.size() * Math.random());
			int y = (int) (u.nodeSequence.size() * Math.random());
			while (y == x) {
				y = (int) (u.nodeSequence.size() * Math.random());
			}
			Collections.swap(ux.nodeSequence, x, y);
			Calfitness.calfitness(map, ux);
			double w = Math.pow(Math.E, -(ux.timeToCompleteAllTasks - u.timeToCompleteAllTasks) / T);

			double rand = Math.random();
			if (ux.timeToCompleteAllTasks < u.timeToCompleteAllTasks) {
				u.nodeSequence.clear();
				u.nodeSequence.addAll(ux.nodeSequence);
				u.timeToCompleteAllTasks = ux.timeToCompleteAllTasks;
			} else if (rand < w && ux.timeToCompleteAllTasks != u.timeToCompleteAllTasks) {
				u.nodeSequence.clear();
				u.nodeSequence.addAll(ux.nodeSequence);
				u.timeToCompleteAllTasks = ux.timeToCompleteAllTasks;
			} else {
				ux.nodeSequence.clear();
				ux.nodeSequence.addAll(u.nodeSequence);
			}
			T = T * multiplier;
		}

	}
}
