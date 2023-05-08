package algs.tool;

import algs.Resource.Map;
import algs.Resource.Node;
import algs.Resource.UAV;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//本函数是用来读取Read类已经生成的数据的，Read类生成的数据里应该包含
//总节点数（包括基站数）
//每个节点最近的基站  每个节点的x坐标 y坐标和任务时长  比如
//52 
//0 6452.0 2191.87 23.149616
//1 7519.42 4290.75 84.63723
//
public class Read2 {

	public static void read2(Map map) throws IOException {
		double[] x;
		double[] y;
		String strbuff;
		BufferedReader data = new BufferedReader(new InputStreamReader(
				new FileInputStream(Map.filepath)));

		strbuff = data.readLine();//第一行
		strbuff = strbuff.replace(" ", "");
		map.totalNum = Integer.parseInt(strbuff);//文件第一行是城市数量
		switch (map.totalNum) {
			case 52:
				map.csNum = 4;
				break;
			case 66:
				map.csNum = 6;
				break;
			case 165:
				map.csNum = 15;
				break;
			case 585:
				map.csNum = 53;
		}


		map.whichChargingStationIsCloser = new int[map.totalNum];
		map.distance = new float[map.totalNum][map.totalNum];
		x = new double[map.totalNum];
		y = new double[map.totalNum];
		map.wsNum = map.totalNum - map.csNum;

		map.node = (algs.Resource.Node[]) new Node[map.totalNum];


		for (int j = 0; j < map.csNum; j++) {
			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");
			map.whichChargingStationIsCloser[j] = Integer.parseInt(strcol[0]);
			//
			//	float  minTime=(float) (map.distance[map.whichChargingStationIsCloser[j]][j]/UAV.speed*2+map.node[j].missionTime*1.75);
			Node node = new Node(Double.parseDouble(strcol[1]), Double.parseDouble(strcol[2]), Float.parseFloat(strcol[3]));
			map.node[j] = node;
			x[j] = Double.parseDouble(strcol[1]);
			y[j] = Double.parseDouble(strcol[2]);
		}


		for (int i = map.csNum; i < map.totalNum; i++) {
			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");
			map.whichChargingStationIsCloser[i] = Integer.parseInt(strcol[0]);
			//float  minTime=(float) (map.distance[map.whichChargingStationIsCloser[i]][i]/UAV.speed*2+map.node[i].missionTime*1.75);
			Node node = new Node(Double.parseDouble(strcol[1]), Double.parseDouble(strcol[2]), Float.parseFloat(strcol[3]));
			map.node[i] = node;
			x[i] = Double.parseDouble(strcol[1]);
			y[i] = Double.parseDouble(strcol[2]);
		}

		for (int i = 0; i < map.totalNum; i++)//初始化每个城市之间的距离
			for (int j = 0; j < map.totalNum; j++) {
				if (i <= j) {
					map.distance[i][j] = (float) Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
				} else
					map.distance[i][j] = map.distance[j][i];
			}


		//修改deadline
		for (int i = map.csNum; i < map.totalNum; i++) {
			float minTime = (float) (map.distance[map.whichChargingStationIsCloser[i]][i] / UAV.speed * 2 + map.node[i].missionTime * 1.75);
			map.node[i].deadline = Node.alpha * minTime;
		}

		data.close();

	}


}
