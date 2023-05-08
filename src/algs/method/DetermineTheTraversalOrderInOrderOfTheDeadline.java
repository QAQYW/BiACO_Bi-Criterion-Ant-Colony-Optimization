package algs.method;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.ArrayList;
import java.util.Collections;

public class DetermineTheTraversalOrderInOrderOfTheDeadline {
	//根据节点的截止时间安排任务序列
	public static boolean determineTheTraversalOrderInOrderOfTheDeadline(UAV u, Map map) throws CloneNotSupportedException {
		u.nodeSequence.clear();
		u.nodeSequence.addAll(u.nodeSet);
		u.nodeSequence.addAll(u.nodeSet);
		Collections.sort(u.nodeSequence, (o1, o2) -> {
			if (map.node[(int) o1].deadline < map.node[(int) o2].deadline) return -1;
			else if (map.node[(int) o1].deadline > map.node[(int) o2].deadline) return 1;
			return 0;
		});

		ArrayList<?> arr;
		arr = Calfitness2.calfitness(map, u);
		if (arr != null) return true;
		u.nodeSequence.clear();
		return false;
	}
}
