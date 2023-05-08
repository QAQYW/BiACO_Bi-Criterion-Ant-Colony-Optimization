package pareto;

import java.io.*;
import java.util.ArrayList;

import algs.acodsp.Ant_DSP;
import algs.acodsp.UAV_DSP;
import biACO.Ant;
import resource.UAV;

public class Solution {
	
	public double fitness1;	// total cost
	public double fitness2;	// completion time
	
	public int uavNum;
	public ArrayList<UAV> uavList;
	
	public Solution(Ant ant) throws CloneNotSupportedException {
		this.fitness1 = ant.fitness1;
		this.fitness2 = ant.fitness2;
		uavNum = ant.countUAV;
		uavList = new ArrayList<UAV>();
		for (UAV uav : ant.uavList) {
			UAV _uav = uav.clone();
			this.uavList.add(_uav);
		}
	}
	
	public Solution(Ant_DSP antdsp) throws CloneNotSupportedException {
		this.fitness1 = antdsp.fitness1;
		this.fitness2 = antdsp.fitness2;
		uavNum = antdsp.uavNum;
		uavList = new ArrayList<UAV>();
		for (UAV_DSP uavdsp : antdsp.uavList) {
			UAV uav = new UAV(uavdsp);
			uavList.add(uav);
		}
	}
	
	/**
	 * check if this solution is dominated by sol
	 * @param sol
	 * @return
	 * 	1: this is dominated by sol<br>
	 * 	-1: sol is dominated by this<br>
	 * 	0: no dominate
	 */
	public int isDominated(Solution sol) {
		if (this.fitness1 < sol.fitness1 && this.fitness2 <= sol.fitness2)
			return 1;
		if (this.fitness1 <= sol.fitness1 && this.fitness2 < sol.fitness2)
			return 1;
		
		if (this.fitness1 > sol.fitness1 && this.fitness2 >= sol.fitness2)
			return 1;
		if (this.fitness1 >= sol.fitness1 && this.fitness2 > sol.fitness2)
			return 1;
		
		return 0;
	}
	
	public boolean isSame(Solution sol) {
		if (this.fitness1 != sol.fitness1 || this.fitness2 != sol.fitness2)
			return false;
		if (this.uavNum != sol.uavNum)
			return false;
		
		for (int i = 0, size; i < this.uavNum; i++) {
			UAV u1 = this.uavList.get(i);
			UAV u2 = this.uavList.get(i);
			if (u1.nodeSeq.size() != u2.nodeSeq.size())
				return false;
			size = u1.nodeSeq.size();
			for (int j = 0; j < size; j++) {
				if (u1.nodeSeq.get(j) != u2.nodeSeq.get(j))
					return false;
			}
		}
		
		return true;
	}
	
	public void print() {
		System.out.println("[uavNum = " + uavNum + ", fitness1 = " + fitness1 + ", fitness2 = " + fitness2 + "]");
		for (UAV uav : uavList) {
			uav.printNodeSeq();
		}
	}
}
