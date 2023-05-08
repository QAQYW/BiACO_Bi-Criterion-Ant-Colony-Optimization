package algs.subiterations;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness;

import java.util.Collections;

public class SubGreedy {
	public static void subGreedy(UAV u, Map map) throws CloneNotSupportedException {
		if (u.nodeSet.size() < 2) return;
		UAV ux = new UAV();
		ux.nodeSequence.addAll(u.nodeSequence);
		ux.nodeSet.addAll(u.nodeSet);
		for (int i = 0; i < 100; i++) {
			int x = (int) (u.nodeSequence.size() * Math.random());
			int y = (int) (u.nodeSequence.size() * Math.random());
			Collections.swap(ux.nodeSequence, x, y);
			Calfitness.calfitness(map, ux);
			if (ux.timeToCompleteAllTasks < u.timeToCompleteAllTasks) {
				u.nodeSequence.clear();
				u.nodeSequence.addAll(ux.nodeSequence);
				u.timeToCompleteAllTasks = ux.timeToCompleteAllTasks;
			} else {
				ux.nodeSequence.clear();
				ux.nodeSequence.addAll(u.nodeSequence);
			}
		}
	}
}
