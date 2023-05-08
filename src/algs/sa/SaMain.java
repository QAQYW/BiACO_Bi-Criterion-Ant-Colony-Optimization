package algs.sa;

import algs.BatchTest.ChangeParameter;
import algs.Resource.Map;
import algs.kmeans.Kmean;
import algs.nsga2.Individual;
import algs.tool.Read;
import algs.tool.Read2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;

public class SaMain {
	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		Map map = new Map();
		Kmean kmean = new Kmean(4, 200);
		//初始化点集、KMeansCluster对象
		kmean.init();
		//使用KMeansCluster对象进行聚类
		kmean.runKmeans();
		Read.read(map);
		Population father = new Population();
		father.init(map);
		java.util.Iterator<Entry<Integer, Individual>> it = father.map.entrySet().iterator();
		while (it.hasNext()) {
			Individual in = it.next().getValue();
			System.out.println(in.fitness1 + " " + in.fitness2);
		}
		System.out.println("======================");
		Date before = new Date();
		Date now = new Date();
		while (Iterator.T > 0.01) {
			father = Iterator.iterator(father, map);
			now = new Date();
			if ((now.getTime() - before.getTime()) / 1000 > 60)
				break;
		}
		ArrayList<Individual> firstRank = new ArrayList<>();
		firstRank = SelectTheFirstRank.selectTheFirstRank(father);
		for (int i = 0; i < firstRank.size(); i++) {
			System.out.println(firstRank.get(i).fitness1 + " " + firstRank.get(i).fitness2 + " " + firstRank.get(i).uav.size());
		}

	}


	//批量实验sa主要运行程序
	public static void samain(String s) throws IOException, CloneNotSupportedException {
		FileWriter out = new FileWriter(ChangeParameter.fileOutPath, true);
		long time = System.currentTimeMillis();
//	   System.out.println(s);
		Map map = new Map();

		Read2.read2(map);
		System.out.println("SA read2");
		Population father = new Population();
		father.init(map);
		System.out.println("SA init");
		Date before = new Date();
		Date now = new Date();
		while (Iterator.T > 0.0001) {
			father = Iterator.iterator(father, map);
			now = new Date();
//			System.out.println(s+"温度 "+Iterator.T);
		}
		ArrayList<Individual> firstRank = new ArrayList<>();
		firstRank = SelectTheFirstRank.selectTheFirstRank(father);
		time = System.currentTimeMillis() - time;
		out.write(s);
		out.write("\r\n");
		out.write(String.valueOf(time));
		out.write("\r\n");
		for (int i = 0; i < firstRank.size(); i++) {
			Individual in = firstRank.get(i);
			out.write(in.fitness1 + "  " + in.fitness2);
			out.write("\r\n");
		}
		out.write("\r\n");
		out.write("\r\n");
		out.close();

	}
}
