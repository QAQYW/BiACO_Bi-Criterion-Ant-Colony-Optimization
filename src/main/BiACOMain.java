package main;


import java.text.SimpleDateFormat;
import java.io.*;

import batchExp.AlgInfo;
import batchExp.CtrlInfo;
import batchExp.Fitness;
import batchExp.GenerateInstance;
import biACO.Ant;
import biACO.BiACO;
import biACO.Colony;
import method.Roulette;
import parameter.BP;
import resource.Node;
import resource.UAV;

public class BiACOMain {
	public static long startMili;
	public static String fileOutPath;
	public static CtrlInfo ct;
	public static String instanceGuide;
	public static String settingGuide;
	
	public static void setBatchParameter(CtrlInfo ct) {
		// ---------- Roulette ----------
		Roulette.alpha = 1;
		Roulette.beta = 5;
		
		// ---------- Colony ----------
		Colony.RHO = 0.1;
		Colony.STAY = 1.0 - Colony.RHO;
		Colony.Q = 5;
		Colony.DELTA_TAU = 0.5;
		Colony.TAU_INITIAL_VALUE = 1.0;
		Colony.ANT_NUMBER = 30;
		Colony.MAX_BREAK_PROBABILITY = 0.7;
		Colony.MIN_BREAK_PROBABILITY = 0;
		
		// ---------- Ant ----------
		Ant.reservedNum = ct.reservedNum;
		Ant.priceMag = ct.priceMag;
		Ant.MAX_TRIAL_TIMES = 15;
		
		// ---------- BiACO ----------
		BiACO.MAX_ITERATION = 150;
		BiACO.COLONY_NUMBER = 5;
		
		// ---------- Node ----------
		Node.dataPara = ct.dataPara;
		Node.ddlPara = ct.ddlPara;
		
		// ---------- UAV ----------
		UAV.SPEED = ct.speed;
		UAV.MAX_ENERGY = 548 * 60 * 60;
		UAV.FLY_ENERGY_CONSUME = ct.flyConsume;
	}
	
	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		
		/*
		GenerateInstance.rootPath = "batch\\";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String folderName = "biACO_" + df.format(System.currentTimeMillis());
		String dirPath = "batch\\result\\" + folderName;
		File dir = new File(dirPath);
		if (dir.mkdir()) {
			System.out.println("mkdir " + dirPath);
		}
		fileOutPath = dirPath + "\\result.txt";
		*/
		
		BP.repeatTimes = 1;
		
		ct = new CtrlInfo();
		
		ct.mapName = "node48uniform";
		ct.nodeNum = 48;
		ct.speed = 20;
		ct.flyConsume = 850;
		ct.dataPara = 0.25;
		ct.ddlPara = 5;
		instanceGuide = "node48uniform 20_850 0.25 5";
		GenerateInstance.generate(ct);
		ct.priceMag = 2;
		ct.reservedPara = 20;
		ct.reservedNum = ct.nodeNum / ct.reservedPara + 1;
		if (ct.reservedNum % 2 == 1) ct.reservedNum++;
		settingGuide = "2\t20";
		ct.control = "20\t0.25\t5\t2\t20";

		ct.mainMethod = "biACO";
		ct.brkMehtod = "none";
		ct.divMethod = "none";
//		ct.control = "node48uniform" + "\t" + ct.mainMethod + "\t" + ct.brkMehtod + "\t" + ct.divMethod + "\t" + ct.control;
		ct.control = "48" + "\t"
				+ ct.mainMethod + "\t"
				+ ct.brkMehtod + "\t"
				+ ct.divMethod + "\t"
				+ ct.control;
		RunBatch.ct = ct;
		
//		run(ct);
		run();
		BiACO.print();
	}
	
	public static void runVisualization() throws CloneNotSupportedException, IOException {
		BP.repeatTimes = 1;
		
		ct = new CtrlInfo();
		ct.mapName = "node48uniform";
		ct.nodeNum = 48;
		ct.speed = 20;
		ct.flyConsume = 850;
		ct.dataPara = 0.25;
		ct.ddlPara = 5;
		instanceGuide = "node48uniform 20_850 0.25 5";
		GenerateInstance.generate(ct);
		ct.priceMag = 2;
		ct.reservedPara = 20;
		ct.reservedNum = ct.nodeNum / ct.reservedPara + 1;
		if (ct.reservedNum % 2 == 1) ct.reservedNum++;
		settingGuide = "2\t20";
		ct.control = "20\t0.25\t5\t2\t20";

		ct.mainMethod = "biACO";
		ct.brkMehtod = "none";
		ct.divMethod = "none";
//		ct.control = "node48uniform" + "\t" + ct.mainMethod + "\t" + ct.brkMehtod + "\t" + ct.divMethod + "\t" + ct.control;
		ct.control = "48" + "\t"
				+ ct.mainMethod + "\t"
				+ ct.brkMehtod + "\t"
				+ ct.divMethod + "\t"
				+ ct.control;
		RunBatch.ct = ct;
		
		run();
	}
	
	public static void run() throws CloneNotSupportedException, IOException {
		setBatchParameter(ct);
		long startMili = System.currentTimeMillis();
		BiACO biaco = new BiACO();
		long endMili = System.currentTimeMillis();
		double seconds = (double) (endMili - startMili) / 1000.0;
		System.out.println("runtime = " + seconds);
//		return biaco;
	}
	
	public static AlgInfo run(CtrlInfo ct) throws CloneNotSupportedException, IOException {
		setBatchParameter(ct);
		
		long startMili = System.currentTimeMillis();
		
		BiACO biaco = new BiACO();
		
		long endMili = System.currentTimeMillis();
		double seconds = (double) (endMili - startMili) / 1000.0;
		
		AlgInfo info = new AlgInfo(seconds);
		
		info.pfSize = BiACO.PF.pf.size();
		double f1, f2;
		for (int i = 0; i < info.pfSize; i++) {
			f1 = BiACO.PF.pf.get(i).fitness1;
			f2 = BiACO.PF.pf.get(i).fitness2;
			Fitness sol = new Fitness(f1, f2);
			info.PF.add(sol);
		}
		info.ct = ct.clone();
		info.runtime = seconds;
		
		return info;
	}
}
