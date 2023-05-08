package method;

import biACO.Ant;
import resource.Map;
import resource.UAV;

public class Division {
	
	/**
	 * Select the uav (u0) with the longest completion time.
	 * Divide u0 into two new uavs, u1 and u2.
	 * @param map
	 * @param father
	 * @return
	 * null: Fail to generate a new solution.<br>
	 * Otherwise: A newly generated feasible solution.
	 * @throws CloneNotSupportedException
	 */
	public static Ant division(Ant father) throws CloneNotSupportedException {
		UAV u0 = father.uavList.get(0).clone();
		if (u0.nodeSeq.size() == 2)
			return null;
		
		Ant child = new Ant();
		
		UAV u1 = new UAV();
		UAV u2 = new UAV();
		int[] tag = new int[Map.totNum];
		for (Integer node : u0.nodeSeq) {
			if (tag[node] == 0) {// if visit this node for the 1st time
				double rand = Math.random();
				if (rand < 0.5) {
					u1.nodeSeq.add(node);
					tag[node] = 1;
				} else {
					u2.nodeSeq.add(node);
					tag[node] = 2;
				}
			} else if (tag[node] == 1) {
				u1.nodeSeq.add(node);
			} else {
				u2.nodeSeq.add(node);
			}
		}
		
		if (u1.nodeSeq.size() == 0 || u2.nodeSeq.size() == 0)
			return null;

		child = father.clone();
		child.uavList.remove(0);
		if (u1.nodeSeq.size() > 0) {
			u1.calcTimeConsume();
			child.uavList.add(u1);
		}
		if (u2.nodeSeq.size() > 0) {
			u2.calcTimeConsume();
			child.uavList.add(u2);
		}
		child.countUAV = child.uavList.size();
		child.calcFitness();
		
		return child;
	}
}
