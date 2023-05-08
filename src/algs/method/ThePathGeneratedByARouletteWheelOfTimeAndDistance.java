package algs.method;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.ArrayList;

//根据距离和时间进行轮盘赌
public class ThePathGeneratedByARouletteWheelOfTimeAndDistance {
	public static boolean thePathGeneratedByARouletteWheelOfTimeAndDistance(UAV u, Map map) throws CloneNotSupportedException {
		ArrayList<Integer> arr = new ArrayList(u.nodeSet);
		u.nodeSequence.clear();
		int thisIndex = map.whichChargingStationIsCloser[arr.get((int) (Math.random() * arr.size()))];
		while (arr.size() != 0) {
			ArrayList<Float> result = determineProbability(arr, map, thisIndex);
			result.set(result.size() - 1, (float) 1);
			double dice = Math.random();
			for (int i = 0; i < result.size(); i++) {
				if (dice <= result.get(i)) {
					u.nodeSequence.add(arr.get(i));
					u.nodeSequence.add(arr.get(i));
					arr.remove(i);
					break;
				}
			}

			thisIndex = u.nodeSequence.get(u.nodeSequence.size() - 1);

		}
		if (Calfitness2.calfitness(map, u) != null) return true;
		return false;

	}

	//arr中存放的是所有还未完成的任务，thisIndex是无人机当前的位置
	public static ArrayList<Float> determineProbability(ArrayList<Integer> arr, Map map, int thisIndex) {
		ArrayList<Float> distance = new ArrayList();
		ArrayList<Float> time = new ArrayList();
		for (int i = 0; i < arr.size(); i++) {


			//节点距离无人机的距离的倒数
			float x = 1 / map.distance[thisIndex][arr.get(i)];
			distance.add(x);

			float y = 1 / map.node[i].deadline;
			time.add(y);
		}
		float distanceSum = 0;
		float timeSum = 0;
		for (float x : distance) {
			distanceSum += x;
		}
		for (float x : time) {
			timeSum += x;
		}
		for (int i = 0; i < distance.size(); i++) {
			distance.set(i, distance.get(i) / distanceSum);
		}
		for (int j = 0; j < time.size(); j++) {
			time.set(j, time.get(j) / timeSum);
		}
		ArrayList<Float> result = new ArrayList();
		for (int i = 0; i < time.size(); i++) {
			float x = time.get(i) + distance.get(i);
			x = x / 2;
			result.add(x);
		}
		for (int i = 1; i < result.size(); i++) {
			result.set(i, result.get(i - 1) + result.get(i));
		}
		return result;

	}
}
