package main;

import java.io.*;
import java.text.SimpleDateFormat;

import javax.xml.stream.events.EndDocument;

import batchExp.AlgInfo;
import batchExp.CtrlInfo;
import batchExp.Fitness;
import batchExp.GenerateInstance;
import parameter.BP;

public class Main {
	

	public static String fileOutPath;
	
	public static CtrlInfo ct;
	public static String instanceGuide;
	public static String settingGuide;
	
//	public static resource.Map map4aco;
	
	public static AlgInfo biaco;
	public static AlgInfo acodsp;
	public static AlgInfo nsgaii;
	public static AlgInfo sa;
	public static AlgInfo gls;
	public static AlgInfo vnd;
	
	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		
		// Path setting
		GenerateInstance.rootPath = "batch\\";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String folderName = "biACO_" + df.format(System.currentTimeMillis());
		String dirPath = "batch\\result\\" + folderName;
		File dir = new File(dirPath);
		if (dir.mkdir()) {
			System.out.println("mkdir " + dirPath);
		}
		fileOutPath = dirPath + "\\result.txt";
		
		AlgInfo.lambda = new double[51];
		for (int i = 0; i <= 50; i++)
			AlgInfo.lambda[i] = (double) i / 50.0;
		
		// experiments
		String result = "";
		ct = new CtrlInfo();
		
		for (String map : BP.mapName) {
			ct.mapName = new String(map);
			switch (ct.mapName) {
			case "node48uniform": ct.nodeNum = 48;break;
			case "node60uniform": ct.nodeNum = 60;break;
			case "node150uniform": ct.nodeNum = 150;break;
			case "node532uniform": ct.nodeNum = 532;break;
			}
			
			for (String speed_fly : BP.speedAndEnergy) {
				String[] temp = speed_fly.split("_");
				ct.speed = Double.parseDouble(temp[0]);
				ct.flyConsume = Double.parseDouble(temp[1]);
				
				for (String data : BP.dataTransmission) {
					ct.dataPara = Double.parseDouble(data);
					
					for (String ddl : BP.ddl) {
						ct.ddlPara = Double.parseDouble(ddl);
						
						for (String price : BP.priceMag) {
							ct.priceMag = Integer.parseInt(price);
							
							for (String reserve : BP.reserved) {
								ct.reservedPara = Integer.parseInt(reserve);
								ct.reservedNum = ct.nodeNum / ct.reservedPara + 1;
								if (ct.reservedNum % 2 == 1) ct.reservedNum++;
								
								instanceGuide = map + " " + speed_fly + " " + data + " " + ddl;
								GenerateInstance.generate(ct);
								
								settingGuide = price + "\t" + reserve;
								
								int algNum = BP.mainMethods.length;
								for (int algId = 0; algId < algNum; algId++) {
									String alg = BP.mainMethods[algId];
									switch (alg) {
									case "biACO":
										entrance_biACO(algId);
										break;
									case "ACO-DSP":
										entrance_ACODSP(algId);
										break;
									case "NSGA-II":
									case "SA":
									case "GLS":
									case "VND":
										entrance_other(alg, algId);
										break;
									}
								}
								
								// calculate 4 indicators
								evaluate();
								BufferedWriter bw = new BufferedWriter(new FileWriter(fileOutPath, true));
								
								
							}
						}
					}
				}
			}
		}
	}
	
	public static void evaluate() {
		AlgInfo optimal = new AlgInfo();
		
		optimal.PF.addAll(biaco.PF);
		optimal.PF.addAll(acodsp.PF);
		optimal.PF.addAll(nsgaii.PF);
		optimal.PF.addAll(sa.PF);
		optimal.PF.addAll(gls.PF);
		optimal.PF.addAll(vnd.PF);
		
		optimal.sortPF();
		optimal.pfSize = optimal.PF.size();
		
		/*
		 * find global max and min value of both fitness1 and fitness2
		 */
		double max1g = optimal.PF.get(0).fitness1;
		double min1g = max1g;
		double max2g = optimal.PF.get(0).fitness2;
		double min2g = max2g;
		
		for (Fitness fn : optimal.PF) {
			max1g = Math.max(max1g, fn.fitness1);
			min1g = Math.min(min1g, fn.fitness1);
			max2g = Math.max(max2g, fn.fitness2);
			min2g = Math.min(min2g, fn.fitness2);
		}
		
		/*
		 * remove dominated solutions or repetitive solutions
		 */
		int p = 1;
		while (p < optimal.PF.size()) {
			if (optimal.PF.get(p).fitness1 < optimal.PF.get(p - 1).fitness1 
					&& optimal.PF.get(p).fitness2 >= optimal.PF.get(p - 1).fitness2) {
				optimal.PF.remove(p);
			} else if (optimal.PF.get(p).fitness1 <= optimal.PF.get(p - 1).fitness1 
					&& optimal.PF.get(p).fitness2 > optimal.PF.get(p - 1).fitness2) {
				optimal.PF.remove(p);
			} else if (optimal.PF.get(p).fitness1 == optimal.PF.get(p - 1).fitness1 
					&& optimal.PF.get(p).fitness2 == optimal.PF.get(p - 1).fitness2 
					&& optimal.PF.get(p).from == optimal.PF.get(p - 1).from) {
				optimal.PF.remove(p);
			} else p++;
		}
		optimal.pfSize = optimal.PF.size();
		
		/*
		 * Normalization
		 */
		optimal.normalization(max1g, min1g, max2g, min2g);
		biaco.normalization(max1g, min1g, max2g, min2g);
		acodsp.normalization(max1g, min1g, max2g, min2g);
		nsgaii.normalization(max1g, min1g, max2g, min2g);
		sa.normalization(max1g, min1g, max2g, min2g);
		gls.normalization(max1g, min1g, max2g, min2g);
		vnd.normalization(max1g, min1g, max2g, min2g);
		
		/*
		 * Calculate 4 indicators
		 */
		biaco.calcIndi(max1g, min1g, max2g, min2g, optimal);
		acodsp.calcIndi(max1g, min1g, max2g, min2g, optimal);
		nsgaii.calcIndi(max1g, min1g, max2g, min2g, optimal);
		sa.calcIndi(max1g, min1g, max2g, min2g, optimal);
		gls.calcIndi(max1g, min1g, max2g, min2g, optimal);
		vnd.calcIndi(max1g, min1g, max2g, min2g, optimal);
		
//		double op_max1 = optimal.PF.get(optimal.pfSize - 1).fitness1;
//		double op_min1 = optimal.PF.get(0).fitness1;
//		double op_max2 = optimal.PF.get(0).fitness2;
//		double op_min2 = optimal.PF.get(optimal.pfSize - 1).fitness2;
//		double op_len1 = op_max1 - op_min1;
//		double op_len2 = op_max2 - op_min2;
//		
//		optimal.normalization(op_max1, op_min1, op_max2, op_min2);
//		biaco.normalization(op_max1, op_min1, op_max2, op_min2);
//		acodsp.normalization(op_max1, op_min1, op_max2, op_min2);
//		nsgaii.normalization(op_max1, op_min1, op_max2, op_min2);
//		sa.normalization(op_max1, op_min1, op_max2, op_min2);
//		gls.normalization(op_max1, op_min1, op_max2, op_min2);
//		vnd.normalization(op_max1, op_min1, op_max2, op_min2);
//		
//		biaco.calcIndi(op_max1, op_min1, op_max2, op_min2, optimal);
//		acodsp.calcIndi(op_max1, op_min1, op_max2, op_min2, optimal);
//		nsgaii.calcIndi(op_max1, op_min1, op_max2, op_min2, optimal);
//		sa.calcIndi(op_max1, op_min1, op_max2, op_min2, optimal);
//		gls.calcIndi(op_max1, op_min1, op_max2, op_min2, optimal);
//		vnd.calcIndi(op_max1, op_min1, op_max2, op_min2, optimal);
		
	}
	
	public static void entrance_biACO(int algId) throws CloneNotSupportedException, IOException {
		ct.mainMethod = "biACO";
		ct.brkMehtod = "break";
		ct.divMethod = "division";
		ct.control = ct.mapName
				+ "\t" + ct.mainMethod
				+ "\t" + ct.brkMehtod
				+ "\t" + ct.divMethod
				+ "\t" + ct.control;
		RunBatch.ct = ct;
		biaco = BiACOMain.run(ct);
		biaco.algIndex = algId;
		biaco.ct = ct.clone();
		for (Fitness fn : biaco.PF) {
			fn.from = algId;
		}
		biaco.sortPF();
		biaco.unique();
	}
	
	public static void entrance_ACODSP(int algId) throws CloneNotSupportedException, IOException {
		ct.mainMethod = "ACO-DSP";
		ct.brkMehtod = "none";
		ct.divMethod = "none";
		ct.control = ct.mapName
				+ "\t" + ct.mainMethod
				+ "\t" + ct.brkMehtod
				+ "\t" + ct.divMethod
				+ "\t" + ct.control;
		RunBatch.ct = ct;
		acodsp = ACO_DSPMain.run(ct);
		acodsp.algIndex = algId;
		acodsp.ct = ct.clone();
		for (Fitness fn : acodsp.PF) {
			fn.from = algId;
		}
		acodsp.sortPF();
		acodsp.unique();
	}
	
	public static void entrance_other(String alg, int algId) throws CloneNotSupportedException, IOException {
		long startMili = System.currentTimeMillis();
		
		ct.mainMethod = alg;
		ct.brkMehtod = "none";
		ct.divMethod = "none";
		ct.control = ct.mapName
				+ "\t" + ct.mainMethod
				+ "\t" + ct.brkMehtod
				+ "\t" + ct.divMethod
				+ "\t" + ct.control;

		RunBatch.ct = ct;
		AlgInfo info = new AlgInfo();
		ct.parseControl(info);
		
		long endMili = System.currentTimeMillis();
		double seconds = (double) (endMili - startMili) / 1000.0;
		info.runtime = seconds;
		info.algIndex = algId;
		info.ct = ct.clone();
		for (Fitness fn : info.PF) {
			fn.from = algId;
		}
		info.sortPF();
		info.unique();
		
		switch (alg) {
		case "NSGA-II":	nsgaii = info;break;
		case "SA":		sa = info;break;
		case "GLS":		gls = info;break;
		case "VND":		vnd = info;break;
		}
	}
}
