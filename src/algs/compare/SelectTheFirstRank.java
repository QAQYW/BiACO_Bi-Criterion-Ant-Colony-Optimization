package algs.compare;

import java.util.ArrayList;

public class SelectTheFirstRank {
	public static ArrayList<PF> selectTheFirstRank(PFSet p1, PFSet p2) {
		ArrayList<PF> result = new ArrayList<PF>();
		result.addAll(p1.set);
		result.addAll(p2.set);
		for (int i = result.size() - 1; i >= 0; i--) {
			PF pfi = result.get(i);
			for (int j = result.size() - 1; j >= 0; j--) {
				PF pfj = result.get(j);
				if (i == j) continue;
				if ((pfi.cost >= pfj.cost && pfi.dd > pfj.dd) || (pfi.cost > pfj.cost && pfi.dd >= pfj.dd) || (pfi.cost == pfj.cost && pfi.dd == pfj.dd)) {
					result.remove(i);
					break;
				}

			}
		}
		return result;
	}
}