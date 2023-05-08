package algs.sa2;

import algs.BatchTest.ChangeParameter;
import algs.Resource.Map;
import algs.nsga2.Individual;
import algs.nsga2.InitIndividual3;
import algs.tool.Read2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import batchExp.Fitness;

public class SaMain1 {
	
	public static int cnt = 0;

	//批量实验sa主要运行程序
	public static ArrayList<Fitness> samain(String s) throws IOException, CloneNotSupportedException {
//		FileWriter out = new FileWriter(ChangeParameter.fileOutPath, true);
		
		
		// ------------------------------------
		
		Map map = new Map();
		Read2.read2(map);
//		Map map = run.RunBatch.map4other;
		
		// ------------------------------------
		
		Individual in = new Individual();
		InitIndividual3.initIndividual3(in, map);
		ArrayList<Individual> arr = new ArrayList<>();
		arr.add(in);
		cnt = 0;
		while (Iterator.T > 0.1) {
			Iterator.iterator(in, map, arr);
			System.out.println("温度为:"+Iterator.T);
			if (cnt > 1000) break;
		}
		if (cnt > 1000) System.out.println("break");
		SelectTheFirstRank.selectTheFirstRank(arr);
		
		float f1, f2;
		ArrayList<Fitness> PF_comparedWithAnt = new ArrayList<Fitness>();
		for (Individual ind : arr) {
			f1 = ind.fitness1;
			f2 = ind.fitness2;
			Fitness fn = new Fitness(f1, f2);
			PF_comparedWithAnt.add(fn);
		}
		return PF_comparedWithAnt;
		
//		out.write(s);
//		out.write("\r\n");
//		for (int i = 0; i < arr.size(); i++) {
//			Individual inn = arr.get(i);
//			out.write(inn.fitness1 + "  " + inn.fitness2);
//			out.write("\r\n");
//		}
//		out.write("\r\n");
//		out.write("\r\n");
//		out.close();

	}
}
