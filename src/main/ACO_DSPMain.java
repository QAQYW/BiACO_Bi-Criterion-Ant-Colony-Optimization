package main;

import java.io.IOException;

import algs.acodsp.*;
import batchExp.AlgInfo;
import batchExp.CtrlInfo;
import batchExp.Fitness;
import resource.Node;

public class ACO_DSPMain {
	public static String fileOutPath;
	public static CtrlInfo ct;
	
	public static void setBatchParameter(CtrlInfo ct) {
		// ---------- Roulette_DSP ----------
		Roulette_DSP.alpha = 1;
		Roulette_DSP.beta = 4;
		
		// ---------- Pheromone_DSP ----------
		Pheromone_DSP.INITIAL_TAU = 10;
		Pheromone_DSP.MAX_TAU = 100;
		Pheromone_DSP.MIN_TAU = 2;
		Pheromone_DSP.TAU_UP = 3;
		Pheromone_DSP.TAU_DOWN = 1;
		
		// ---------- Ant_DSP ----------
		Ant_DSP.reservedUAVNum = ct.reservedNum;
		Ant_DSP.priceMag = ct.priceMag;
		
		// ---------- ACO_DSP ----------
		ACO_DSP.MAX_ITERATION = 100;
		ACO_DSP.ANT_NUMBER = 30;
		
		// ---------- UAV_DSP ----------
		UAV_DSP.SPEED = ct.speed;
		UAV_DSP.MAX_ENERGY = 548 * 60 * 60;
		UAV_DSP.FLY_ENERGY_CONSUME = ct.flyConsume;
		
		// ---------- Node ----------
		Node.dataPara = ct.dataPara;
		Node.ddlPara = ct.ddlPara;
	}
	
	public static AlgInfo run(CtrlInfo ct) throws CloneNotSupportedException, IOException {
		setBatchParameter(ct);
		
		long startMili = System.currentTimeMillis();
		ACO_DSP acodsp = new ACO_DSP();
		long endMili = System.currentTimeMillis();
		double seconds = (double) (endMili - startMili) / 1000.0;
		
		AlgInfo info = new AlgInfo(seconds);
		
		info.pfSize = ACO_DSP.globalPF.pf.size();
		double f1, f2;
		for (int i = 0; i < info.pfSize; i++) {
			f1 = ACO_DSP.globalPF.pf.get(i).fitness1;
			f2 = ACO_DSP.globalPF.pf.get(i).fitness2;
			Fitness sol = new Fitness(f1, f2);
			info.PF.add(sol);
		}
		info.ct = ct.clone();
		info.runtime = seconds;
		
		return info;
	}
}
