package algs.tool;

import algs.Resource.Map;
import algs.Resource.Node;
import algs.Resource.UAV;
import algs.kmeans.Kmean;

import java.io.*;
import java.nio.charset.StandardCharsets;


//��������ʵ��ʱ��//���б�����Ҫ���Kmeans���µ�Keman��ʹ��//����ʹ��kmean������������ӻ�վ��Ȼ��ʹ�ñ����������
//����deadline
public class Read {
	//�����λ��
	public static String FILEOUTPATH = "";
	public static String distribution = "uniform";

	//filename��
	public static void read(Map map) throws IOException {
		FILEOUTPATH = "D:\\NSGA2������������ʵ��\\node532uniform.txt";
		double[] x;
		double[] y;
		String strbuff;
		BufferedReader data = new BufferedReader(new InputStreamReader(
				new FileInputStream(Kmean.FILEPATH)));
		//kmeans.FILEPATH��
		data.readLine();//��һ��
		strbuff = data.readLine();//�ڶ���
		map.totalNum = Integer.parseInt(strbuff);//�ļ��ڶ����ǳ�������
		data.readLine();//������
		strbuff = data.readLine();//������
		map.csNum = Integer.parseInt(strbuff);//�ļ��������ǳ��ڵ�����
		data.readLine();
		//�ýڵ�����ĸ����
		map.whichChargingStationIsCloser = new int[map.totalNum];
		map.distance = new float[map.totalNum][map.totalNum];
		x = new double[map.totalNum];
		y = new double[map.totalNum];
		for (int j = 0; j < map.csNum; j++) {
			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");
			map.whichChargingStationIsCloser[j] = Integer.parseInt(strcol[0]) - 1;

			x[j] = Double.parseDouble(strcol[2]);// x����
			y[j] = Double.parseDouble(strcol[4]);// y����
		}

		data.readLine();

		map.node = (algs.Resource.Node[]) new Node[map.totalNum];
		for (int i = map.csNum; i < map.totalNum; i++) {

			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");// �ַ��ָ�
			x[i] = Double.parseDouble(strcol[2]);// x����
			y[i] = Double.parseDouble(strcol[4]);// y����
			map.whichChargingStationIsCloser[i] = Integer.parseInt(strcol[0]) - 1;
			//System.out.println(map.whichChargingStationIsCloser[i]);
		}

		for (int i = 0; i < map.totalNum; i++)//��ʼ��ÿ������֮��ľ���
			for (int j = 0; j < map.totalNum; j++) {
				if (i <= j) {
					map.distance[i][j] = (float) Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
				} else
					map.distance[i][j] = map.distance[j][i];
			}


		//�Խڵ�����긳ֵ
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
			//�豣֤����һ̨���˻��ܹ����
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

	//������kmeans����������������л�վ�ĵ�ͼ,Ȼ����Read������������read2���õĵ�ͼ
	public static void main(String[] args) throws IOException {
		Map map = new Map();
		float[] NodeAlpha = {5, 10, 15};
		for (float v : NodeAlpha) {
			Node.alpha = v;
			read(map);
		}
	}

}
