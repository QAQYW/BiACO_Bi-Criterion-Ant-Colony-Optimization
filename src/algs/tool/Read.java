package algs.tool;

import algs.Resource.Map;
import algs.Resource.Node;
import algs.Resource.UAV;
import algs.kmeans.Kmean;

import java.io.*;
import java.nio.charset.StandardCharsets;


//此类生成实例时用//运行本程序要结合Kmeans包下的Keman类使用//首先使用kmean类的主函数增加基站，然后使用本类的主函数
//增加deadline
public class Read {
	//输出的位置
	public static String FILEOUTPATH = "";
	public static String distribution = "uniform";

	//filename是
	public static void read(Map map) throws IOException {
		FILEOUTPATH = "D:\\NSGA2批量试验最终实例\\node532uniform.txt";
		double[] x;
		double[] y;
		String strbuff;
		BufferedReader data = new BufferedReader(new InputStreamReader(
				new FileInputStream(Kmean.FILEPATH)));
		//kmeans.FILEPATH是
		data.readLine();//第一行
		strbuff = data.readLine();//第二行
		map.totalNum = Integer.parseInt(strbuff);//文件第二行是城市数量
		data.readLine();//第三行
		strbuff = data.readLine();//第四行
		map.csNum = Integer.parseInt(strbuff);//文件第四行是充电节点数量
		data.readLine();
		//该节点距离哪个最近
		map.whichChargingStationIsCloser = new int[map.totalNum];
		map.distance = new float[map.totalNum][map.totalNum];
		x = new double[map.totalNum];
		y = new double[map.totalNum];
		for (int j = 0; j < map.csNum; j++) {
			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");
			map.whichChargingStationIsCloser[j] = Integer.parseInt(strcol[0]) - 1;

			x[j] = Double.parseDouble(strcol[2]);// x坐标
			y[j] = Double.parseDouble(strcol[4]);// y坐标
		}

		data.readLine();

		map.node = (algs.Resource.Node[]) new Node[map.totalNum];
		for (int i = map.csNum; i < map.totalNum; i++) {

			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");// 字符分割
			x[i] = Double.parseDouble(strcol[2]);// x坐标
			y[i] = Double.parseDouble(strcol[4]);// y坐标
			map.whichChargingStationIsCloser[i] = Integer.parseInt(strcol[0]) - 1;
			//System.out.println(map.whichChargingStationIsCloser[i]);
		}

		for (int i = 0; i < map.totalNum; i++)//初始化每个城市之间的距离
			for (int j = 0; j < map.totalNum; j++) {
				if (i <= j) {
					map.distance[i][j] = (float) Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
				} else
					map.distance[i][j] = map.distance[j][i];
			}


		//对节点的坐标赋值
		FileOutputStream fos = new FileOutputStream(FILEOUTPATH);
		OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
//		OutputStreamWriter osw=new OutputStreamWriter(fos,"");
		osw.write(map.whichChargingStationIsCloser.length + " " + "\n");
		for (int i = 0; i < map.totalNum; i++) {
			switch (Read.distribution) {
				case "normal":
					map.node[i] = new Node(x[i], y[i], 180, 3060);
					break;
				case "uniform":
					map.node[i] = new Node(x[i], y[i]);
					break;
			}

			// map.node[i]=new Node(x[i],y[i],180,3060);
			// map.node[i]=new Node(x[i],y[i]);
			float minTime = (float) (map.distance[map.whichChargingStationIsCloser[i]][i] / UAV.speed * 2.75 + map.node[i].missionTime);
			//需保证至少一台无人机能够完成
			if (map.node[i].deadline < minTime) map.node[i].deadline = minTime + 1;
			map.node[i].deadline = minTime;
			osw.write(map.whichChargingStationIsCloser[i] + " ");
			osw.write(map.node[i].x + " " + map.node[i].y + " ");
			osw.write(map.node[i].missionTime + "\n");
			osw.flush();
		}

		osw.close();
		fos.close();
		map.wsNum = map.totalNum - map.csNum;
		data.close();

	}

	//首先用kmeans聚类的主函数生成有基站的地图,然后用Read的主函数生成read2可用的地图
	public static void main(String[] args) throws IOException {
		Map map = new Map();
		float[] NodeAlpha = {5, 10, 15};
		for (float v : NodeAlpha) {
			Node.alpha = v;
			read(map);
		}
	}

}
