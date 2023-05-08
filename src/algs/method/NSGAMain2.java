package algs.method;

import algs.Resource.Map;
import algs.kmeans.Kmean;
import algs.nsga2.*;
import algs.tool.Read2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NSGAMain2 {
	public static void main(String[] args) throws IOException, CloneNotSupportedException {

		/*
		 * 下面的程序用来看程序开始运行时的时间
		 */
		Calendar Cld = Calendar.getInstance();
		int YY = Cld.get(Calendar.YEAR);
		int MM = Cld.get(Calendar.MONTH) + 1;
		int DD = Cld.get(Calendar.DATE);
		int HH = Cld.get(Calendar.HOUR_OF_DAY);
		int mm = Cld.get(Calendar.MINUTE);
		int SS = Cld.get(Calendar.SECOND);
		int MI = Cld.get(Calendar.MILLISECOND);
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		Date before = new Date();
		Date now = new Date();
		System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));

		/*
		 * 主程序
		 */
		for (int n = 0; n < 10; n++) {

			Map.filepath = "D:\\NSGA2实例\\node48normal" + n + ".txt";
			System.out.println(n);
			Map map = new Map();

			Kmean kmean = new Kmean(5, 200);

			//初始化点集、KMeansCluster对象
			kmean.init();
			//使用KMeansCluster对象进行聚类
			kmean.runKmeans();
			Read2.read2(map);

			Population father = new Population();

			father.init(map);
			System.out.println("init");
			ArrayList<Individual> firstRank = SelectTheFirstRank.selectTheFirstRank(father);
			System.out.println("first");
			for (int i = 0; i < 0; i++) {
				Population child = GenerateNextPopulation.generateNextPopulation(father, map, i);
				father = NSGA2Select.nsga2(father, child);
				System.out.println(i);
				if ((i) % 50 == 0) {
					firstRank = SelectTheFirstRank.selectTheFirstRank(father);
					for (Individual in : firstRank) {
//    				for(int y=0;y<in.uav.size();y++)
//    				   System.out.println(in.uav.get(y).nodeSequence+" "+in.uav.get(y).length+"   "+in.uav.get(y).timeToCompleteAllTasks);
						System.out.println("总耗时 " + in.fitness1 + " 时间" + in.fitness2 + " 无人机数量" + in.uav.size());
					}
				}
				firstRank = SelectTheFirstRank.selectTheFirstRank(father);
//    	   now = new Date();
//    	    if((now.getTime()-before.getTime())/1000>60)break;
			}
			for (Individual in : firstRank) {
				System.out.println("总耗时 " + in.fitness1 + " 时间" + in.fitness2 + " 无人机数量" + in.uav.size());
			}

		}


		cal = Calendar.getInstance();
		date = cal.getTime();
		System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));
		System.out.println("charge");
	}
}
