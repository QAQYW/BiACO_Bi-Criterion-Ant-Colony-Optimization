package algs.variableNeighborhood2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.heapSort.HeapSort;
import algs.method.Calfitness;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;
import algs.nsga2.Individual;

import java.util.ArrayList;

public class Variation3 {
	//heapsort//随机找一个无人机，从中取出一个任务，然后找出最空闲的前百分之多少的无人机，看看他们能否完成这个任务
	public static void variation3(Individual individual, Map map) throws CloneNotSupportedException {
		if (individual.uav.size() == 0 || individual.uav.size() == 1) return;
		//x是无人机的编号。随机生成
		int x = (int) (Math.random() * individual.uav.size());
//	        UAV u=individual.uav.get(0);
//	    	individual.uav.forEach((o1)->{
//	    		if(u.timeToCompleteAllTasks<o1.timeToCompleteAllTasks)u=o;
//	    	});
		//这是对应的无人机
		UAV u = individual.uav.get(x);
		//用于存放无人机要遍历的节点
		ArrayList<Integer> arr = new ArrayList<Integer>();
		//将无人机要遍历的节点放入arr中
		arr.addAll(u.nodeSet);
		//x是节点在arr中的位置，随机生成
		x = (int) Math.random() * arr.size();
		//x是节点的编号
		x = (int) arr.get(x);
		u.nodeSet.remove(x);
		if (u.nodeSet.isEmpty()) individual.uav.remove(u);
		else {
			// System.out.println(u.nodeSequence+"start");
			for (int t = u.nodeSequence.size() - 1; t >= 0; t--) {
				if (u.nodeSequence.get(t) == x)
					u.nodeSequence.remove(t);
			}
			// System.out.println(x);
			Calfitness.calfitness(map, u);
		}
		ArrayList<UAV> copy = new ArrayList();
		for (int i = 0; i < individual.uav.size(); i++) {
			UAV u2 = individual.uav.get(i);
			if (u2 == u) continue;
			copy.add(u2);
		}
		int num = (int) (copy.size() * 0.2);
		HeapSort.heapSort(copy, num);
		boolean flag = false;
		for (int i = copy.size() - 1; i >= copy.size() - num - 1; i--) {
			UAV ux = copy.get(i);
			UAV u1 = new UAV();
			u1.nodeSet.addAll(ux.nodeSet);
			u1.nodeSet.add(x);
			u1.nodeSequence.addAll(ux.nodeSequence);

			if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u1, map)) {
				if (u1.nodeSet.size() > 3)

					ux.nodeSequence.clear();
				ux.nodeSequence.addAll(u1.nodeSequence);
				ux.length = u1.length;
				ux.nodeSet.clear();
				ux.nodeSet.addAll(u1.nodeSet);
				ux.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;
				flag = true;
				break;
			}
		}
		if (flag == false) {
			UAV u1 = new UAV();
			u1.nodeSet.add(x);
			u1.nodeSequence.add(x);
			u1.nodeSequence.add(x);
//            	 System.out.println(u1.nodeSet);
			Calfitness.calfitness(map, u1);
			//if(u1.nodeSequence.size()!=u1.nodeSet.size()*2)System.out.println("新生成的无人机错误");
			individual.uav.add(u1);

		}
		individual.calfitness1();
		individual.calfitness2();

	}

	public static void Iterator1(Map map, ArrayList<Individual> list) throws CloneNotSupportedException {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			variation33(list.get(i), map, list);
			if (list.size() == 1) {
				count++;
				i = 1;
				if (count > 300) break;
			}
		}
	}

	public static void variation33(Individual individual, Map map, ArrayList<Individual> list) throws CloneNotSupportedException {
		float fitness1 = individual.fitness1;
		float fitness2 = individual.fitness2;
		if (individual.uav.size() == 0 || individual.uav.size() == 1) return;
		//x是无人机的编号。随机生成
//	    	int x=(int) (Math.random()*individual.uav.size());
		UAV u = individual.uav.get(0);
		for (int i = 1; i < individual.uav.size(); i++) {
			if (u.timeToCompleteAllTasks < individual.uav.get(i).timeToCompleteAllTasks) {
				u = individual.uav.get(i);
			}
		}
		//这是对应的无人机
//	         UAV u=individual.uav.get(x);
		//用于存放无人机要遍历的节点

		for (int x : u.nodeSet) {
			ArrayList<Integer> arr = new ArrayList<Integer>();
			//将无人机要遍历的节点放入arr中
			arr.addAll(u.nodeSet);
			//x是节点在arr中的位置，随机生成

			u.nodeSet.remove(x);
			if (u.nodeSet.isEmpty()) individual.uav.remove(u);
			else {
				// System.out.println(u.nodeSequence+"start");
				for (int t = u.nodeSequence.size() - 1; t >= 0; t--) {
					if (u.nodeSequence.get(t) == x)
						u.nodeSequence.remove(t);
				}
				// System.out.println(x);
				Calfitness.calfitness(map, u);
			}
			ArrayList<UAV> copy = new ArrayList();
			for (int i = 0; i < individual.uav.size(); i++) {
				UAV u2 = individual.uav.get(i);
				if (u2 == u) continue;
				copy.add(u2);
			}
			int num = (int) (copy.size() * 0.2);
			HeapSort.heapSort(copy, num);
			boolean flag = false;
			for (int i = copy.size() - 1; i >= copy.size() - num - 1; i--) {
				UAV ux = copy.get(i);
				UAV u1 = new UAV();
				u1.nodeSet.addAll(ux.nodeSet);
				u1.nodeSet.add(x);
				u1.nodeSequence.addAll(ux.nodeSequence);

				if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u1, map)) {
					if (u1.nodeSet.size() > 3)
						ux.nodeSequence.clear();
					ux.nodeSequence.addAll(u1.nodeSequence);
					ux.length = u1.length;
					ux.nodeSet.clear();
					ux.nodeSet.addAll(u1.nodeSet);
					ux.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;
					flag = true;
					break;
				}
			}
			if (flag == false) {
				UAV u1 = new UAV();
				u1.nodeSet.add(x);
				u1.nodeSequence.add(x);
				u1.nodeSequence.add(x);
//           	 System.out.println(u1.nodeSet);
				Calfitness.calfitness(map, u1);
				//if(u1.nodeSequence.size()!=u1.nodeSet.size()*2)System.out.println("新生成的无人机错误");
				individual.uav.add(u1);
			}
			individual.calfitness1();
			individual.calfitness2();
			if (individual.fitness1 < fitness1 && individual.fitness2 < fitness2) {
				list.clear();
				list.add(individual);
				return;
			}
			if ((individual.fitness1 < fitness1 && individual.fitness2 > fitness2) || (individual.fitness1 > fitness1 && individual.fitness2 < fitness2)) {
				list.add(individual);
			}

		}
	}


	public static void Iterator2(Map map, ArrayList<Individual> list) throws CloneNotSupportedException {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			variation44(list.get(i), map, list);
			if (list.size() == 1) {
				count++;
				i = 1;
				if (count > 300) break;
			}
		}
	}

	public static void variation44(Individual individual, Map map, ArrayList<Individual> list) throws CloneNotSupportedException {
		float fitness1 = individual.fitness1;
		float fitness2 = individual.fitness2;
		if (individual.uav.size() == 0 || individual.uav.size() == 1) return;
		//x是无人机的编号。随机生成
//	    	int x=(int) (Math.random()*individual.uav.size());
		UAV u = individual.uav.get(0);
		for (int i = 1; i < individual.uav.size(); i++) {
			if (u.timeToCompleteAllTasks < individual.uav.get(i).timeToCompleteAllTasks) {
				u = individual.uav.get(i);
			}
		}
		//这是对应的无人机
//	         UAV u=individual.uav.get(x);
		//用于存放无人机要遍历的节点

		for (int x : u.nodeSet) {
			ArrayList<Integer> arr = new ArrayList<Integer>();
			//将无人机要遍历的节点放入arr中
			arr.addAll(u.nodeSet);
			//x是节点在arr中的位置，随机生成

			u.nodeSet.remove(x);
			if (u.nodeSet.isEmpty()) individual.uav.remove(u);
			else {
				// System.out.println(u.nodeSequence+"start");
				for (int t = u.nodeSequence.size() - 1; t >= 0; t--) {
					if (u.nodeSequence.get(t) == x)
						u.nodeSequence.remove(t);
				}
				// System.out.println(x);
				Calfitness.calfitness(map, u);
			}
			ArrayList<UAV> copy = new ArrayList();
			for (int i = 0; i < individual.uav.size(); i++) {
				UAV u2 = individual.uav.get(i);
				if (u2 == u) continue;
				copy.add(u2);
			}
			int num = (int) (copy.size() * 0.2);
			HeapSort.heapSort1(copy, num);
			boolean flag = false;
			for (int i = copy.size() - 1; i >= copy.size() - num - 1; i--) {
				UAV ux = copy.get(i);
				UAV u1 = new UAV();
				u1.nodeSet.addAll(ux.nodeSet);
				u1.nodeSet.add(x);
				u1.nodeSequence.addAll(ux.nodeSequence);

				if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u1, map)) {
					if (u1.nodeSet.size() > 3)
						ux.nodeSequence.clear();
					ux.nodeSequence.addAll(u1.nodeSequence);
					ux.length = u1.length;
					ux.nodeSet.clear();
					ux.nodeSet.addAll(u1.nodeSet);
					ux.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;
					flag = true;
					break;
				}
			}
			if (flag == false) {
				UAV u1 = new UAV();
				u1.nodeSet.add(x);
				u1.nodeSequence.add(x);
				u1.nodeSequence.add(x);
//           	 System.out.println(u1.nodeSet);
				Calfitness.calfitness(map, u1);
				//if(u1.nodeSequence.size()!=u1.nodeSet.size()*2)System.out.println("新生成的无人机错误");
				individual.uav.add(u1);
			}
			individual.calfitness1();
			individual.calfitness2();
			if (individual.fitness1 < fitness1 && individual.fitness2 < fitness2) {
				list.clear();
				list.add(individual);
				return;
			}
			if ((individual.fitness1 < fitness1 && individual.fitness2 > fitness2) || (individual.fitness1 > fitness1 && individual.fitness2 < fitness2)) {
				list.add(individual);
			}

		}
	}
}
