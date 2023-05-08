package main;

import java.io.IOException;

import biACO.BiACO;
import visualization.Visualize;

public class VisualizationMain {
	public static void main(String[] args) {
		try {
			BiACOMain.runVisualization();
//			Visualize visualize = new Visualize(BiACO.PF);
			new Visualize(BiACO.PF);
		} catch (CloneNotSupportedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
