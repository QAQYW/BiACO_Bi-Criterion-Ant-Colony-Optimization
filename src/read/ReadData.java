package read;

import java.io.*;

import resource.Map;
import resource.Node;
import resource.UAV;

public class ReadData {
	
	public static String FILE_PATH;
	
	public static void read() throws IOException {
		String strbuff;
		BufferedReader data = new BufferedReader(new FileReader(FILE_PATH));
		
		strbuff = data.readLine();
		Map.totNum = Integer.parseInt(strbuff);
		strbuff = data.readLine();
		Map.staNum = Integer.parseInt(strbuff);
		Map.taskNum = Map.totNum - Map.staNum;
		
		Map.closeStation = new int[Map.totNum];
		Map.distance = new double[Map.totNum][Map.totNum];
		Map.nodes = new Node[Map.totNum];
		
		double time;
		double[] x = new double[Map.totNum];
		double[] y = new double[Map.totNum];
		
		for (int i = 0; i < Map.staNum; i++) {
			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");
			x[i] = Double.parseDouble(strcol[1]);
			y[i] = Double.parseDouble(strcol[2]);
			time = Double.parseDouble(strcol[3]);
			Map.nodes[i] = new Node(x[i], y[i], time);
		}
		for (int i = Map.staNum; i < Map.totNum; i++) {
			strbuff = data.readLine();
			String[] strcol = strbuff.split(" ");
			x[i] = Double.parseDouble(strcol[1]);
			y[i] = Double.parseDouble(strcol[2]);
			time = Double.parseDouble(strcol[3]);
			Map.closeStation[i] = Integer.parseInt(strcol[0]);
			Map.nodes[i] = new Node(x[i], y[i], time);
		}
		
		// Initilize distance between every pair of nodes
		for (int i = 0; i < Map.totNum; i++) {
			Map.distance[i][i] = 0;
			for (int j = 0; j < Map.totNum; j++) {
				if (i > j) {
					Map.distance[i][j] = Map.distance[j][i];
				} else {
					Map.distance[i][j] = Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
				}
			}
		}
		
		// Set deadline and slotTime
		for (int i = Map.staNum; i < Map.totNum; i++) {
			time = Map.distance[Map.closeStation[i]][i] / UAV.SPEED * 2 + Map.nodes[i].getCalcTime() * 1.75;
			Map.nodes[i].setDdl(Node.ddlPara * time);
//			Map.nodes[i].setDdl(5.0 * time);
			Map.nodes[i].setSlotTime(Map.nodes[i].getDdl() - Map.nodes[i].getTransTime() - Map.nodes[i].getDataTime());
		}
		
		data.close();
	}
}
