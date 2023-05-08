package algs.variableNeighborhood2;

import algs.BatchTest.ChangeParameter;
import algs.Resource.Map;
import algs.nsga2.Individual;
import algs.nsga2.InitIndividual3;
import algs.tool.Read2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import batchExp.Fitness;

public class VnMain1 {

	//批量实验主要运行程序
	public static ArrayList<Fitness> vnMain(String s) throws IOException, CloneNotSupportedException {
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
		for (int i = 0; i < 900; i++) {
			Iterator.iterator(map, arr);
//			System.out.println(s + "vn1第" + i + "次迭代");
		}
		for (int i = 0; i < 900; i++) {
			Iterator.iterator2(map, arr);
//			System.out.println(s + "vn2第" + i + "次迭代");
		}
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
		
		//下面是输出用的  能用的....      --by wyq
		
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

	public static void VnMain2(String s) throws IOException, CloneNotSupportedException {
		FileWriter out = new FileWriter(ChangeParameter.fileOutPath, true);

		Map map = new Map();
		Read2.read2(map);
		Individual in = new Individual();
		InitIndividual3.initIndividual3(in, map);
		ArrayList<Individual> arr = new ArrayList<>();
		Variation3.Iterator1(map, arr);
		Variation3.Iterator2(map, arr);
		SelectTheFirstRank.selectTheFirstRank(arr);
		out.write(s);
		out.write("\r\n");
		for (int i = 0; i < arr.size(); i++) {
			Individual inn = arr.get(i);
			out.write(inn.fitness1 + "  " + inn.fitness2);
			out.write("\r\n");
		}
		out.write("\r\n");
		out.write("\r\n");
		out.close();


	}
}
