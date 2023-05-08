package algs.BatchTest;

import algs.Resource.Map;
import algs.Resource.Node;
import algs.Resource.UAV;
import algs.greedy2.GreedyMain2;
import algs.method.Calfitness2;
import algs.nsga2.Exchange4;
import algs.nsga2.GenerateNextPopulation;
import algs.nsga2.Individual;
import algs.sa2.SaMain1;
import algs.variableNeighborhood2.VnMain1;

import java.io.IOException;
import java.util.ArrayList;

import batchExp.Fitness;
import batchExp.GenerateInstance;

public class ChangeParameter {
	//这里控制着输出的路径
	public static String fileOutPath = "batchTest2022_10_17.txt";

	//alpha是传输时长，比例为0.25 0.5 0.75
	public static ArrayList<Fitness> changeParameter(String mainMethods, String speed, String subIterator, String iterator, String map, String deadline, int selfOwnUAVFactor, int priceMagnification, String timeMagni) throws IOException, CloneNotSupportedException {
		Individual.priceMagnification = priceMagnification;
		switch (timeMagni) {
			case "0.25":
				Calfitness2.transmissionTimeParameter = (float) 0.25;
				break;
			case "0.5":
				Calfitness2.transmissionTimeParameter = (float) 0.5;
				break;
			case "0.75":
				Calfitness2.transmissionTimeParameter = (float) 0.75;
				break;
			case "rand":
//				Calfitness2.transmissionTimeParameter = (float) Math.random();
				Calfitness2.transmissionTimeParameter = (float) 0.1;
				break;
		}

		switch (speed) {
			case "40":
				UAV.speed = 40;
				UAV.flyEnergyConsume = 1250;
				break;
			case "30":
				UAV.speed = 30;
				UAV.flyEnergyConsume = 1000;
				break;
			case "22":
				UAV.speed = 22;
				UAV.flyEnergyConsume = 850;
				break;
			case "50":
				UAV.speed = 50;
				UAV.flyEnergyConsume = 1750;
				break;
		}


		switch (map) {
			case "node48uniform":
//				Map.filepath = "D:\\BatchTestExamples\\node48uniform.txt";
				Map.filepath = GenerateInstance.rootPath + "node48uniform.txt";
				Individual.selfOwnedNum = 48 / selfOwnUAVFactor + 1;
				if (Individual.selfOwnedNum % 2 != 0) Individual.selfOwnedNum++;
				break;
			case "node532uniform":
//				Map.filepath = "D:\\BatchTestExamples\\node532uniform.txt";
				Map.filepath = GenerateInstance.rootPath + "node532uniform.txt";
				Individual.selfOwnedNum = 532 / selfOwnUAVFactor + 1;
				if (Individual.selfOwnedNum % 2 != 0) Individual.selfOwnedNum++;
				break;
			case "node60uniform":
//				Map.filepath = "D:\\BatchTestExamples\\node60uniform.txt";
				Map.filepath = GenerateInstance.rootPath + "node60uniform.txt";
				Individual.selfOwnedNum = 60 / selfOwnUAVFactor + 1;
				if (Individual.selfOwnedNum % 2 != 0) Individual.selfOwnedNum++;
				break;
			case "node150uniform":
//				Map.filepath = "D:\\BatchTestExamples\\node150uniform.txt";
				Map.filepath = GenerateInstance.rootPath + "node150uniform.txt";
				Individual.selfOwnedNum = 150 / selfOwnUAVFactor + 1;
				if (Individual.selfOwnedNum % 2 != 0) Individual.selfOwnedNum++;
				break;
		}

		Exchange4.subIterator = subIterator;

		switch (deadline) {
			case "5":
				Node.alpha = 5;
				break;

			case "10":
				Node.alpha = 10;
				break;

			case "15":
				Node.alpha = 15;
				break;
		}

		GenerateNextPopulation.iterator = iterator;

		ArrayList<Fitness> PF_comparedWithAnt = new ArrayList<Fitness>();
//		ArrayList<Fitness> PF_comparedWithAnt;
		String ssss = map + " " + mainMethods + " " + speed + " " + deadline + " " + subIterator + " " + iterator + " " + selfOwnUAVFactor + " " + priceMagnification + " " + timeMagni;
		switch (mainMethods) {
			case "VN":
				PF_comparedWithAnt = VnMain1.vnMain(ssss);
				break;
			case "GREEDY":
				PF_comparedWithAnt = GreedyMain2.greedymain(ssss);
				break;
			case "SA":
				algs.sa2.Iterator.T = 1000;
				algs.sa2.Iterator.maxFitness1 = Double.MIN_VALUE;
				algs.sa2.Iterator.minFitness1 = Double.MAX_VALUE;
				algs.sa2.Iterator.maxFitness2 = Double.MIN_VALUE;
				algs.sa2.Iterator.minFitness2 = Double.MAX_VALUE;
				PF_comparedWithAnt = SaMain1.samain(ssss);
				break;
			case "NSGA2":
				PF_comparedWithAnt = NSGA2zhuyaochengxu.running(ssss);
		}
		return PF_comparedWithAnt;
	}
}
