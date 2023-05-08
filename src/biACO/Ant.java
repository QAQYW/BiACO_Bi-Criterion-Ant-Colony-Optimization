package biACO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import method.Roulette;
import resource.Map;
import resource.UAV;


public class Ant implements Cloneable {
	
	public static int MAX_TRIAL_TIMES = 15;
	
	public static int reservedNum;	// number of reserved UAVs
	public static int priceMag;		// price magnification
	
	/*
	 * for ants
	 */
	private double h;		// index of ant
	private double lambda;	// preference between two pheromone matrices (objectives)
	private double[] eta;
	
	/*
	 * for UAVs
	 */
	public ArrayList<UAV> uavList;
	public ArrayList<Integer> waiting;
	
	public int countUAV;
	public double fitness1;
	public double fitness2;
	
	/*
	 * assisted variables
	 */
	private int[] vis;
	private int[] firstTask;	// record an UAV's first task to represent itself
	private double[][] _distance;
	private ArrayList<Integer> tabu;
	
	
	public Ant() {
		eta = new double[Map.totNum];
		uavList = new ArrayList<UAV>();
		waiting = new ArrayList<Integer>();

		countUAV = 0;
		vis = new int[Map.totNum];
		firstTask = new int[Map.totNum];
		int len = Map.distance.length;
		_distance = new double[len][len];
		tabu = new ArrayList<Integer>();
		
		fitness1 = 0;
		fitness2 = 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Ant clone() throws CloneNotSupportedException {
		Ant ant = null;
		try {
			ant = (Ant) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ant.uavList = (ArrayList<UAV>) this.uavList.clone();
		return ant;
	}
	
	public void sortUAVList() {
		Collections.sort(uavList, new Comparator<UAV>() {
			@Override
			public int compare(UAV u1, UAV u2) {
				if (u1.timeConsume == u2.timeConsume) return 0;
				return u1.timeConsume > u2.timeConsume ? (-1) : 1;
			}
		});
	}
	
	public void calcFitness() {
		fitness1 = 0;
		fitness2 = 0;
		
		for (UAV uav : uavList)
			uav.calcTimeConsume();
		sortUAVList();
		double time;
		for (int i = 0; i < countUAV; i++) {
			time = uavList.get(i).timeConsume;
			fitness1 += time * (i >= reservedNum ? priceMag : 1);
			fitness2 = Math.max(fitness2, time);
		}
	}
	
	/**
	 * set:<br>
	 * 	vis[i] = 0;<br>
	 * 	add all tasks to waiting list;<br>
	 * 	initialize _distance[][] array with Map.distance[][]
	 */
	private void init() {
		for (int i = 0; i < Map.totNum; i++)
			vis[i] = 0;
		for (int i = Map.staNum; i < Map.totNum; i++)
			waiting.add(i);
		uavList.clear();
		countUAV = 0;
		int len = _distance.length;
		for (int i = 0; i < len; i++)
			for (int j = 0; j < len; j++)
				_distance[i][j] = Map.distance[i][j];
	}
	
	/**
	 * Generate a feasible solution
	 * @param colony
	 * @throws CloneNotSupportedException
	 */
	public void generate(Colony colony) throws CloneNotSupportedException {
		init();
		// set vis[i] = 0
		Roulette roulette = new Roulette();
		
		while (!waiting.isEmpty()) {
			
			tabu.clear();
			UAV uav = new UAV();
			int pos = chooseFirstTask(roulette, colony);
			int task = waiting.get(pos);
			firstTask[++countUAV] = task;
			waiting.remove(pos);
			
			uav.nodeSeq.add(task);
			uav.nodeSeq.add(task);
			uav.node = task;
			uav.boundary = 0;
			vis[task]++;
			
			// Span [0, boundary] in nodeSeq is fixed
			
			// Assign tasks to UAVs, schedule trajectory
			while (!waiting.isEmpty()) {
				
//				boolean breakFlag = true;
//				if (RunBatch.ct.brkMethod.equals("none"))
//					breakFlag = false;
				
				double prob = Math.random();
				if (prob < colony.getBreakProb()) break;
//				if (breakFlag && prob < colony.getBreakProb()) break;
				
				tabu.clear();
				int maxTrials = Math.min(waiting.size(), MAX_TRIAL_TIMES);// max = 15
				
				while (tabu.size() < maxTrials) {
					pos = chooseNextTask(roulette, uav, colony);
					task = waiting.get(pos);
					int insertIndex = insertCheck(task, uav, colony);
					
					if (insertIndex != -1) {
						uav.nodeSeq.add(uav.boundary + 1, task);
						uav.nodeSeq.add(insertIndex, task);
						waiting.remove(pos);
						
						// update _distance[][]
						for (Integer _task : waiting) {
							if (_distance[firstTask[countUAV]][_task] < _distance[task][_task])
								_distance[firstTask[countUAV]][_task] = _distance[task][_task];
						}
						break;
					} else {
						insertTabu(task);
						roulette.sum -= roulette.poss[task];
					}
				}
				
				// update parameters
				uav.boundary++;
				if (uav.boundary == uav.nodeSeq.size())
					break;
				uav.node = uav.nodeSeq.get(uav.boundary);
				vis[uav.node]++;
			}
			uavList.add(uav);
		}
	}
	
	private int chooseFirstTask(Roulette roulette, Colony colony) {
		int size = waiting.size(), task;
		for (int i = 0; i < size; i++) {
			task = waiting.get(i);
			roulette.poss[task] = Roulette.calcProbForFirstTask(colony.tauFT1[task], colony.tauFT2[task], Roulette.eta_beta[task], lambda);
		}
		return roulette.choose(waiting, tabu);
	}
	
	private int chooseNextTask(Roulette roulette, UAV uav, Colony colony) {
		if (tabu.isEmpty()) {
			/*
			 * eta1: distance -> dimension is time
			 * eta2: deadline (urgency) -> dimension is time
			 */
			double eta1, eta2;
			int size = waiting.size(), task, state;
			for (int i = 0; i < size; i++) {
				task = waiting.get(i);
				eta1 = _distance[firstTask[countUAV]][task] / UAV.SPEED;
				eta2 = Map.nodes[task].getSlotTime() - Map.distance[uav.node][task] / UAV.SPEED;
				eta[task] = 1.0 / (Math.pow(eta1, lambda) * Math.pow(eta2, 1 - lambda));
				state = (vis[uav.node] - 1) << 1;
				roulette.poss[task] = Roulette.calcProbForFirstTask(colony.tau1[uav.node][task][state], colony.tau2[uav.node][task][state], eta[task], lambda);
			}
		}
		return roulette.choose(waiting, tabu);
	}
	
	private int insertCheck(int task, UAV uav, Colony colony) throws CloneNotSupportedException {
		UAV _uav = uav.clone();
		_uav.nodeSeq.add(_uav.boundary + 1, task);
		
		int[] _vis = vis.clone();
		int[] pos = new int[10];
		double[] val = new double[10];
		for (int i = 0; i < 5; i++) {
			pos[i] = -1;
			val[i] = 0.0;
		}
		
		/*
		 * Select the best 5 candidate insertion positions
		 * Assume the value of position size is infinite
		 */
		int candidateNum = 5;// 5 candidates totally (including the position size)
		int size = _uav.nodeSeq.size();
		int u, v, uState, vState;
		double uValue, vValue, value;
		v = _uav.nodeSeq.get(_uav.boundary + 1);
		_vis[v]++;
		for (int i = _uav.boundary + 2, k; i < size; i++) {
			u = v;
			v = _uav.nodeSeq.get(i);
			_vis[v]++;
			
			uState = ((_vis[u] - 1) << 1) | (_vis[task] - 1);
			vState = ((_vis[task] - 1) << 1) | (_vis[v] - 1);
			
			uValue = Math.pow(colony.tau1[u][task][uState], lambda) * Math.pow(colony.tau2[u][task][uState], 1 - lambda);
			vValue = Math.pow(colony.tau1[task][v][vState], lambda) * Math.pow(colony.tau2[task][v][vState], 1 - lambda);
			value = uValue + vValue;
			
			// sort
			for (k = candidateNum - 2; k >= 0; k--) {
				if (value > val[k]) {
					val[k + 1] = val[k];
					pos[k + 1] = pos[k];
				}
			}
			val[k + 1] = value;
			pos[k + 1] = i;
		}
		
		pos[candidateNum - 1] = size;
		int bestPos = -1;
		double minTimeConsume = Double.MAX_VALUE;
		for (int k = 0; k < candidateNum; k++) {
			if (pos[k] == -1) continue;
			_uav.nodeSeq.add(pos[k], task);
			if (_uav.calcTimeConsume()) {
				if (_uav.timeConsume < minTimeConsume) {
					bestPos = pos[k];
					minTimeConsume = _uav.timeConsume;
				}
			}
			_uav.nodeSeq.remove(pos[k]);
		}
		return bestPos;
	}
	
	/**
	 * Binary insertion into taboo list
	 * @param task
	 */
	private void insertTabu(int task) {
		if (tabu.isEmpty()) {
			tabu.add(task);
			return;
		}
		int size = tabu.size();
		if (task > tabu.get(size - 1)) {
			tabu.add(task);
			return;
		}
		int left = 0, right = size - 1, mid, pos = 0;
		while (left <= right) {
			mid = (left + right) >> 1;
			if (tabu.get(mid) > task) {
				pos = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		tabu.add(pos, task);
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}
	
	public double getLambda() {
		return lambda;
	}
	
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
}
