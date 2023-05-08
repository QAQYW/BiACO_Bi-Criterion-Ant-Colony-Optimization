package batchExp;

import java.io.IOException;

import javax.print.attribute.standard.PageRanges;

import algs.BatchTest.ChangeParameter;

/**
 * Control information
 * @author wyqaq
 *
 */
public class CtrlInfo implements Cloneable {
	
	public String mapName;
	public int nodeNum;
	public double speed;
	public double flyConsume;
	public double dataPara;
	public double ddlPara;
	public int priceMag;
	public int reservedPara;
	public int reservedNum;
	public String mainMethod;
	public String brkMehtod;
	public String divMethod;
	
	public String control;
	
	@Override
	public CtrlInfo clone() throws CloneNotSupportedException {
		CtrlInfo info = null;
		try {
			info = (CtrlInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return info;
	}
	
	public void parseControl(AlgInfo info) throws IOException, CloneNotSupportedException {
		/*
		 * All the parameters (string)
		 * 0: map name
		 * 1: compared algorithm (main method)
		 * 2: 
		 * 3:
		 * 4: speed (e.g. "30", not "30_1000")
		 * 5: data transmission parameter
		 * 6: deadline parameter
		 * 7: price magnification
		 * 8: reserved parameter
		 */
		
		String[] paras = control.split("\t");
		String mapName = paras[0];
		String speed = paras[4];
		String dataTransPara = paras[5];
		String ddlPara = paras[6];
		
		// change the name of main method (algorithm)
		String algName = mainMethod;
		switch (mainMethod) {
		case "NSGA-II":
			algName = "NSGA2";
			break;
		case "SA":
			algName = "SA";
			break;
		case "GLS":
			algName = "GREEDY";
			break;
		case "VND":
			algName = "VN";
			break;
		}
		
		info.PF = ChangeParameter.changeParameter(algName,
				speed,
				"sa","ReduceTime",
				mapName,
				ddlPara,
				this.reservedPara,
				this.priceMag,
				dataTransPara);
	}
	
	
}
