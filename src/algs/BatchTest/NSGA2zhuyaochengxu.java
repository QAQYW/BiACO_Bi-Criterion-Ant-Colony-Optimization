package algs.BatchTest;


import algs.Resource.Map;
import algs.Resource.UAV;
import algs.nsga2.*;
import algs.tool.Read2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import batchExp.Fitness;

public class NSGA2zhuyaochengxu {


	public static ArrayList<Fitness> running(String s) throws CloneNotSupportedException, IOException {
//		FileWriter out = new FileWriter(ChangeParameter.fileOutPath, true);
//		long time = System.currentTimeMillis();
		
		
		// ------------------------------------
		
		Map map = new Map();
		Read2.read2(map);
//		Map map = run.RunBatch.map4other;
		
		// ------------------------------------
		

		Population father = new Population();
		father.init(map);

		ArrayList<Individual> firstRank;
		for (int i = 0; i < 400; i++) {
//			System.out.println(s + " 第" + i + "次循环");
			Population child;
			child = GenerateNextPopulation.generateNextPopulation(father, map, i);
//			System.out.println("tset");
			father = NSGA2Select.nsga2(father, child);
		}

		firstRank = SelectTheFirstRank.selectTheFirstRank(father);
		
		float f1, f2;
		ArrayList<Fitness> PF_comparedWithAnt = new ArrayList<Fitness>();
		for (Individual ind : firstRank) {
			f1 = ind.fitness1;
			f2 = ind.fitness2;
			Fitness fn = new Fitness(f1, f2);
			PF_comparedWithAnt.add(fn);
		}
		return PF_comparedWithAnt;
		
		//下面是输出用的  能用的....      --by wyq
		
//		time = System.currentTimeMillis() - time;
//		out.write(s);
//		out.write("\r\n");
//		out.write(String.valueOf(time));
//		out.write("\r\n");
//		for (int i = 0; i < firstRank.size(); i++) {
//			Individual in = firstRank.get(i);
//			out.write(in.fitness1 + "\t" + in.fitness2);
//			out.write("\r\n");
//		}
//		out.write("\r\n");
//		out.write("\r\n");
//		out.close();
//		
//		wyq_output(time, firstRank);

	}
	
	public static String wyqOutPath;
	public static void wyq_output(long time, ArrayList<Individual> firstRank) throws IOException {
		FileWriter out = new FileWriter(wyqOutPath, true);
		out.write("time = " + String.valueOf((double) time / 1000.0) + "\n");
		int size = firstRank.size();
		out.write("size of firstRank = " + String.valueOf(size) + "\n");
		for (int i = 0; i < size; i++) {
			System.out.println("-------------------");
			out.write("-------------------\n");
			out.write("number " + String.valueOf(i) + " in firstRank:\n");
			Individual _individual = firstRank.get(i);
			int _UAVNum = _individual.uav.size();
			out.write("UAV number = " + String.valueOf(_UAVNum) + "\n");
			float _time = (float) 0;
			for (UAV _uav : _individual.uav) {
				_time = Math.max(_time, _uav.timeToCompleteAllTasks);
				for (int t : _uav.nodeSequence) {
					System.out.print(t + ", ");
				}
				System.out.println(" ");
			}
			out.write("max {all UAVs} completeTime = " + String.valueOf(_time) + "\n");
		}
		out.close();
		
		System.out.println("successful!");
	}

}
