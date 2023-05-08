package algs.subiterations;

import algs.Resource.UAV;

public class Variation {
	public static void variation(UAV u)
	{
		int x=(int) (u.nodeSequence.size()*Math.random());
		int y=(int)(u.nodeSequence.size()*Math.random());
		int x1=u.nodeSequence.get(x);
		int y1=u.nodeSequence.get(y);
		u.nodeSequence.set(x, y1);
		u.nodeSequence.set(y, x1);
	}
}
