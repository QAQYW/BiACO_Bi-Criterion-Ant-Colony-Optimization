package algs.subiterations;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.Collections;

public class RandomGeneratedSequence {
	public static void randomGeneratedSequence(UAV u, Map map) {
		u.nodeSequence.clear();
		u.nodeSequence.addAll(u.nodeSet);
		u.nodeSequence.addAll(u.nodeSet);

		//������������
		Collections.shuffle(u.nodeSequence);

	}
}
