package algs.method;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.ArrayList;
import java.util.HashSet;

public class InsertIntoArrange {
	public static boolean insertIntoArrange(UAV u, Map map, int p) throws CloneNotSupportedException {
		
		for (int i = 0; i < u.nodeSequence.size(); i++) {
			UAV ux = new UAV();
			ux.nodeSequence.addAll(u.nodeSequence);
			ux.nodeSequence.add(i, p);
			for (int j = 0; j < u.nodeSequence.size(); j++) {
				ux.nodeSequence.add(i, p);
				ArrayList arr = Calfitness2.calfitness(map, u);
				if (arr != null) {
					u.nodeSequence.clear();
					u.nodeSequence.addAll(ux.nodeSequence);

					return true;
				}
			}
		}
		return false;
	}

	public static boolean insertIntoArrange(UAV u, Map map, HashSet<Integer> hs) throws CloneNotSupportedException {

		int size = hs.size();
		for (int j = 0; j < 50; j++) {
			UAV ux = new UAV();
			ux.nodeSet.addAll(u.nodeSet);
			ux.nodeSequence.addAll(u.nodeSequence);
			for (int x : hs) {
				ux.nodeSequence.add((int) Math.random() * ux.nodeSequence.size(), x);
				ux.nodeSequence.add((int) Math.random() * ux.nodeSequence.size(), x);
			}
			if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(ux, map)) {
				u.nodeSequence.clear();
				u.nodeSequence.addAll(ux.nodeSequence);
				u.nodeSet.clear();
				u.nodeSet.addAll(ux.nodeSet);
				u.timeToCompleteAllTasks = ux.timeToCompleteAllTasks;
				return true;
			}
		}
		return false;
	}
}
