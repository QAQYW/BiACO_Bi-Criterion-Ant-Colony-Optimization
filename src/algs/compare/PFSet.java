package algs.compare;

import java.util.*;

public class PFSet {

	public ArrayList<PF> set;
	int num;//不同值的数量

	public double getbestCost() {
		int n = set.size();
		double mincost = set.get(0).getCost();
		for (int i = 1; i < n; i++) {
			double cost = set.get(i).getCost();
			if (mincost > cost) mincost = cost;
		}
		return mincost;
	}

	public void computeNormalization(PFRange pfrange) {
		int n = this.set.size();
		for (int i = 0; i < n; i++) {
			this.set.get(i).CostNomalization(pfrange.getMaxcost(), pfrange.getMincost());
			this.set.get(i).DDNomalization(pfrange.getMaxdd(), pfrange.getMindd());
		}
	}

	public double AverageQuality()//值越小越好
	{

		double AQ = 0;
		int size = set.size();
		for (int i = 0; i <= 50; i++) {
			double lambda1 = (double) i / 50;
			double lambda2 = (double) (50 - i) / 50;
			double minvalue = 20000000;
			for (int j = 0; j < size; j++) {
				PF x = set.get(j);
				double value = Math.max(lambda1 * x.getCostNormalization(), lambda2 * x.getDDNormalization()) + 0.01 * (lambda1 * x.getCostNormalization() + lambda2 * x.getDDNormalization());
				if (minvalue > value) minvalue = value;
			}
			AQ += minvalue;
		}
		AQ = AQ / 51;
		return AQ;
	}

	//该pfset中和所有pfset混合起来筛选后的optset有多少重复
	public PFSet intersection(PFSet optSet) {
		PFSet pfset = new PFSet();
		int size = optSet.getSet().size();
		String contri = this.set.get(0).getContributor();
		for (int i = 0; i < size; i++) {
			PF pf = optSet.getSet().get(i);
			if (pf.getContributor().equalsIgnoreCase(contri)) {
				pfset.addPF(pf);
			}
//			else System.out.println(contri+"vs"+pf.getContributor());
		}
		return pfset;
	}

	public double MaximumSpread(PFSet optSet)//值越大越好[0,1]
	{
		//把当前SET中与optSet交集的部分取出来计算
//		optSet.print();
//		System.out.println("------------------------");
//		this.print();
//		System.out.println("-----求交集---------");
//		optSet.printNormal();
//		System.out.println("-----XXX---------");
//		this.printNormal();
		PFSet intersaction = this.intersection(optSet);
//		System.out.println("000000000000000000");
//		intersaction.printNormal();
		if (intersaction.getNum() == 0) return 0;
		double minRcost = optSet.getSet().get(0).getCostNormalization();
		double minEcost = intersaction.getSet().get(0).getCostNormalization();
		double maxRcost = optSet.getSet().get(0).getCostNormalization();
		double maxEcost = intersaction.getSet().get(0).getCostNormalization();
		double minRdd = optSet.getSet().get(0).getDDNormalization();
		double minEdd = intersaction.getSet().get(0).getDDNormalization();
		double maxRdd = optSet.getSet().get(0).getDDNormalization();
		double maxEdd = intersaction.getSet().get(0).getDDNormalization();
		int sizeE = intersaction.getSet().size();
		for (int i = 1; i < sizeE; i++) {
			PF pf = intersaction.getSet().get(i);
			if (pf.getCostNormalization() > maxEcost) maxEcost = pf.getCostNormalization();
			if (minEcost > pf.getCostNormalization()) minEcost = pf.getCostNormalization();
			if (pf.getDDNormalization() > maxEdd) maxEdd = pf.getDDNormalization();
			if (minEdd > pf.getDDNormalization()) minEdd = pf.getDDNormalization();
		}
		int sizeopt = optSet.getSet().size();
		for (int i = 0; i < sizeopt; i++) {
			PF pf = optSet.getSet().get(i);
			if (pf.getCostNormalization() > maxRcost) maxRcost = pf.getCostNormalization();
			if (minRcost > pf.getCostNormalization()) minRcost = pf.getCostNormalization();
			if (pf.getDDNormalization() > maxRdd) maxRdd = pf.getDDNormalization();
			if (minRdd > pf.getDDNormalization()) minRdd = pf.getDDNormalization();
		}
//		System.out.println("Math.pow(("+maxEcost+"-"+minEcost+")/("+maxRcost+"-"+minRcost+"), 2)+Math.pow(("+maxEdd+"-"+minEdd+")/("+maxRdd+"-"+minRdd+"), 2)");
		if (maxRcost - minRcost == 0 || maxRdd - minRdd == 0)//如果最优集cost值只有一个，那么交集也就只有一个
		{
			return 1;
		}
		double MS = Math.sqrt((Math.pow((maxEcost - minEcost) / (maxRcost - minRcost), 2) + Math.pow((maxEdd - minEdd) / (maxRdd - minRdd), 2)) / 2);
		if (MS > 1) {
			System.out.println("Strange value");
		}
		return MS;
	}

	public double distanceMetricAvg(PFSet optSet)//值越小越好
	{
//		R.print();
//		System.out.println("------------------------");
//		this.print();
		double costRange = 0;
		double mincost = set.get(0).getCostNormalization();
		double maxcost = set.get(0).getCostNormalization();
		double ddRange = 0;
		double mindd = set.get(0).getDDNormalization();
		double maxdd = set.get(0).getDDNormalization();

		int sizeE = set.size();
		for (int i = 1; i < sizeE; i++) {
			PF pf = set.get(i);
			if (pf.getCostNormalization() > maxcost) maxcost = pf.getCostNormalization();
			if (mincost > pf.getCostNormalization()) mincost = pf.getCostNormalization();
			if (pf.getDDNormalization() > maxdd) maxdd = pf.getDDNormalization();
			if (mindd > pf.getDDNormalization()) mindd = pf.getDDNormalization();
		}
		int sizeR = optSet.getSet().size();
		for (int i = 0; i < sizeR; i++) {
			PF pf = optSet.getSet().get(i);
			if (pf.getCostNormalization() > maxcost) maxcost = pf.getCostNormalization();
			if (mincost > pf.getCostNormalization()) mincost = pf.getCostNormalization();
			if (pf.getDDNormalization() > maxdd) maxdd = pf.getDDNormalization();
			if (mindd > pf.getDDNormalization()) mindd = pf.getDDNormalization();
		}

		costRange = maxcost - mincost;
		ddRange = maxdd - mindd;
//		if(costRange==0||ddRange==0) return -1;
//		System.out.println("costrange="+costRange+" ddRange="+ddRange);

		double disSummary = 0;
		HashSet<String> explored = new HashSet<String>();

		for (int i = 0; i < sizeR; i++) {
			PF xR = optSet.getSet().get(i);
			if (explored.contains(xR.toValueString())) continue;
			else explored.add(xR.toValueString());
//			System.out.println("xR="+xR.toString());
			double mindistance = 10000;
			for (int j = 0; j < sizeE; j++) {
				PF x = set.get(j);
//				System.out.println("x="+x.toString());
				double distanceOncost = -1;
				if (costRange == 0) distanceOncost = 0;
				else {
					distanceOncost = (x.getCostNormalization() - xR.getCostNormalization()) / costRange;
				}
				double distanceOndd = -1;
				if (ddRange == 0)
					distanceOndd = 0;
				else distanceOndd = (x.getDDNormalization() - xR.getDDNormalization()) / ddRange;
				double distance = Math.max(distanceOncost, distanceOndd);
				if (mindistance > distance) mindistance = distance;
//				System.out.println("distanceOncost="+distanceOncost+" distanceOndd="+distanceOndd+"  distance="+distance+" mindistance="+mindistance);
			}
			disSummary += mindistance;
		}
		return disSummary / optSet.getNum();
	}

	public double distanceMetricMax(PFSet R)//值越小越好
	{
		HashSet<String> explored = new HashSet<String>();
		double costRange = 0;
		double mincost = set.get(0).getCostNormalization();
		double maxcost = set.get(0).getCostNormalization();
		double ddRange = 0;
		double mindd = set.get(0).getDDNormalization();
		double maxdd = set.get(0).getDDNormalization();

		int sizeE = set.size();
		for (int i = 1; i < sizeE; i++) {
			PF pf = set.get(i);

			if (pf.getCostNormalization() > maxcost) maxcost = pf.getCostNormalization();
			if (mincost > pf.getCostNormalization()) mincost = pf.getCostNormalization();
			if (pf.getDDNormalization() > maxdd) maxdd = pf.getDDNormalization();
			if (mindd > pf.getDDNormalization()) mindd = pf.getDDNormalization();
		}
		int sizeR = R.getSet().size();
		for (int i = 0; i < sizeR; i++) {
			PF pf = R.getSet().get(i);
			if (pf.getCostNormalization() > maxcost) maxcost = pf.getCostNormalization();
			if (mincost > pf.getCostNormalization()) mincost = pf.getCostNormalization();
			if (pf.getDDNormalization() > maxdd) maxdd = pf.getDDNormalization();
			if (mindd > pf.getDDNormalization()) mindd = pf.getDDNormalization();
		}

		costRange = maxcost - mincost;
		ddRange = maxdd - mindd;
//		if(costRange==0||ddRange==0) return -1;
		double maxdistance = 0;
		for (int i = 0; i < sizeR; i++) {
			PF xR = R.getSet().get(i);
			if (explored.contains(xR.toValueString())) continue;
			else explored.add(xR.toValueString());

			double mindistance = 10000;
			for (int j = 0; j < sizeE; j++) {
				PF x = set.get(j);
				double distanceOncost = -1;
				if (costRange == 0) distanceOncost = 0;
				else {
					distanceOncost = (x.getCostNormalization() - xR.getCostNormalization()) / costRange;
				}
				double distanceOndd = -1;
				if (ddRange == 0)
					distanceOndd = 0;
				else distanceOndd = (x.getDDNormalization() - xR.getDDNormalization()) / ddRange;
				double distance = distanceOncost > distanceOndd ? distanceOncost : distanceOndd;
				if (mindistance > distance) mindistance = distance;
			}
			if (mindistance > maxdistance) maxdistance = mindistance;
		}
		return maxdistance;
	}

	public double CMetric(PFSet E)//比较两两算法之间的好坏的，不合适和最优解比较,值越大越好
	{
		double metric = 0;
		int sizeE = E.getSet().size();
		int size = this.set.size();
		for (int i = 0; i < sizeE; i++) {
			PF pf = E.getSet().get(i);
			for (int j = 0; j < size; j++) {
				PF x = set.get(j);
				//x是this的解，pf是E的解
				if (x.compare(pf) == -1) {
					metric++;
					break;
				}
			}
		}
		return metric / sizeE;
	}

	public void print() {
		for (int i = 0; i < set.size(); i++)
			set.get(i).print();
	}

	public void printNormal() {
		for (int i = 0; i < set.size(); i++)
			set.get(i).printNormal();
	}

	public PFSet() {
		set = new ArrayList<PF>();
		num = 0;
	}

	public void addpfset(PFSet pf) {
		this.set.addAll(pf.set);
	}

	public void mergeWithSet(PFSet set) {
		Iterator<PF> it = set.getSet().iterator();
		while (it.hasNext()) {
			PF pf = it.next();
			this.set.add(pf);
		}
	}

	public ArrayList<PF> getSet() {
		return set;
	}

	public int getNum() {
		return num;
	}


	public HashMap<String, Double> Statistic(boolean print) {
		this.print();
		System.out.println("-------------------");
		if (num == 0) {
			System.out.println("Empty Pareto Frontier Set");
			return null;
		}
		if (this.validation() == false) {
			System.out.println("WRONG Pareto Frontier Set");

			return null;
		}
		if (num == 0) return null;
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		Iterator<PF> it = set.iterator();
		while (it.hasNext()) {
			PF pf = it.next();
			String contributor = pf.getContributor();
			if (result.containsKey(contributor))
				result.put(contributor, result.get(contributor) + 1);
			else result.put(contributor, 1);
		}
		HashMap<String, Double> finalresult = new HashMap<String, Double>();
		Iterator<String> cit = result.keySet().iterator();
		while (cit.hasNext()) {
			String method = cit.next();
			int connum = result.get(method);
			double ratio = (double) connum / (double) num;
			if (print) {
				System.out.println(method + "\t" + ratio);
			}
			finalresult.put(method, ratio);
		}
		return finalresult;
	}

	public boolean validation() {

		int size = set.size();
		for (int i = 0; i < size; i++) {
			PF pf1 = set.get(i);
			for (int j = i + 1; j < size; j++) {
				PF pf2 = set.get(j);
				int compare = pf1.compare(pf2);
				if (compare == 0 && !pf1.getContributor().equalsIgnoreCase(pf2.getContributor())) continue;
				if (compare != 2) {
					System.out.println("Validation Error: " + pf1.toString() + " <> " + pf2.toString());
					this.print();
					return false;
				}
			}
		}
		return true;
	}

	public boolean addPF(PF pf) {
		HashSet<String> forcount = new HashSet<String>();
		if (num == 0) {
			set.add(pf);
			num++;
			return true;
		}

		int size = set.size();
		int comparecase = -5;//一个不可能的值
		boolean iscomparable = false;
		int stopindex = -1;
		for (int i = 0; i < size; i++)//pf太大的情况
		{

			PF obj = set.get(i);
			int compare = obj.compare(pf);
			forcount.add(obj.getvaluestring());
			if (compare == -1) {
				return false;//遇到比pf小的，说明pf大，肯定加不进来
			}
			if (compare == 0) {
				if (obj.contributor.equalsIgnoreCase(pf.contributor)) {
					return false;//如果贡献者是同一个人，则不要加进来
				} else {

					comparecase = 0;
					iscomparable = true;//遇到可比较的情况
				}
			}
			if (compare == 1)//pf是更小的情况
			{

				comparecase = 1;
				iscomparable = true;//遇到可比较的情况
				stopindex = i;
				break;
			}
		}

		if (!iscomparable)//说明都不可比较
		{
			set.add(pf);
			num++;
			return true;
		}

		if (comparecase == 0)//相同，但是不同贡献者
		{
			set.add(pf);
			return true;
		}
		if (comparecase == 1) {
			PF toremove = set.get(stopindex);
			forcount.remove(toremove.getvaluestring());
			forcount.add(pf.getvaluestring());
			set.set(stopindex, pf);
			for (int i = stopindex + 1; i < size; i++) {
				if (set.get(i).compare(pf) == 1) {
					forcount.remove(set.get(i).getvaluestring());
					set.remove(i);
					i--;
					size--;
				} else {
					forcount.add(set.get(i).getvaluestring());
				}
			}
			num = forcount.size();
			return true;
		}

		return false;
	}


	public static void main(String args[]) {
		String[] methods = {"M1", "M2", "M3", "M4"};
		Random r = new Random();
		PFSet pfset = new PFSet();
		PF pf1 = new PF(0.0, 0.0, methods[r.nextInt(4)]);
		pf1.print();
		PF pf2 = new PF(0.0, 0.2, methods[r.nextInt(4)]);
		System.out.println(pfset.addPF(pf1) + " num=" + pfset.getNum() + "------------------");
		pfset.print();
		System.out.println(pfset.addPF(pf2) + " num=" + pfset.getNum() + "------------------");
		pfset.print();

		System.out.println("totalnum=" + pfset.getNum());
		pfset.Statistic(true);
	}

	public double distanceMetricAvg2(PFSet optSet)//值越小越好
	{
		double costRange = 0;
		double mincost = set.get(0).getCost();
		double maxcost = set.get(0).getCost();
		double ddRange = 0;
		double mindd = set.get(0).getDD();
		double maxdd = set.get(0).getDD();

		int sizeE = set.size();
		for (int i = 1; i < sizeE; i++) {
			PF pf = set.get(i);
			if (pf.getCost() > maxcost) maxcost = pf.getCost();
			if (mincost > pf.getCost()) mincost = pf.getCost();
			if (pf.getDD() > maxdd) maxdd = pf.getDD();
			if (mindd > pf.getDD()) mindd = pf.getDD();
		}
		int sizeR = optSet.getSet().size();
		for (int i = 0; i < sizeR; i++) {
			PF pf = optSet.getSet().get(i);
			if (pf.getCost() > maxcost) maxcost = pf.getCost();
			if (mincost > pf.getCost()) mincost = pf.getCost();
			if (pf.getDD() > maxdd) maxdd = pf.getDD();
			if (mindd > pf.getDD()) mindd = pf.getDD();
		}

		costRange = maxcost - mincost;
		ddRange = maxdd - mindd;

		double disSummary = 0;
		HashSet<String> explored = new HashSet<String>();

		for (int i = 0; i < sizeR; i++) {
			PF xR = optSet.getSet().get(i);
			if (explored.contains(xR.toValueString())) continue;
			else explored.add(xR.toValueString());
			double mindistance = 10000;
			for (int j = 0; j < sizeE; j++) {
				PF x = set.get(j);
				double distanceOncost = -1;
				if (costRange == 0) distanceOncost = 0;
				else {
					distanceOncost = (x.getCost() - xR.getCost()) / costRange;
				}
				double distanceOndd = -1;
				if (ddRange == 0)
					distanceOndd = 0;
				else distanceOndd = (x.getDD() - xR.getDD()) / ddRange;
				double distance = Math.max(distanceOncost, distanceOndd);
				if (mindistance > distance) mindistance = distance;
			}
			disSummary += mindistance;
		}
		return disSummary / optSet.set.size();
	}

	public double distanceMetricMax2(PFSet R)//值越小越好
	{
		HashSet<String> explored = new HashSet<String>();
		double costRange = 0;
		double mincost = set.get(0).getCost();
		double maxcost = set.get(0).getCost();
		double ddRange = 0;
		double mindd = set.get(0).getDD();
		double maxdd = set.get(0).getDD();

		int sizeE = set.size();
		for (int i = 1; i < sizeE; i++) {
			PF pf = set.get(i);

			if (pf.getCost() > maxcost) maxcost = pf.getCost();
			if (mincost > pf.getCost()) mincost = pf.getCost();
			if (pf.getDD() > maxdd) maxdd = pf.getDD();
			if (mindd > pf.getDD()) mindd = pf.getDD();
		}
		int sizeR = R.getSet().size();
		for (int i = 0; i < sizeR; i++) {
			PF pf = R.getSet().get(i);
			if (pf.getCost() > maxcost) maxcost = pf.getCost();
			if (mincost > pf.getCost()) mincost = pf.getCost();
			if (pf.getDD() > maxdd) maxdd = pf.getDD();
			if (mindd > pf.getDD()) mindd = pf.getDD();
		}

		costRange = maxcost - mincost;
		ddRange = maxdd - mindd;
		double maxdistance = 0;
		for (int i = 0; i < sizeR; i++) {
			PF xR = R.getSet().get(i);
			if (explored.contains(xR.toValueString())) continue;
			else explored.add(xR.toValueString());

			double mindistance = 10000;
			for (int j = 0; j < sizeE; j++) {
				PF x = set.get(j);
				double distanceOncost = -1;
				if (costRange == 0) distanceOncost = 0;
				else {
					distanceOncost = (x.getCost() - xR.getCost()) / costRange;
				}
				double distanceOndd = -1;
				if (ddRange == 0)
					distanceOndd = 0;
				else distanceOndd = (x.getDD() - xR.getDD()) / ddRange;
				double distance = distanceOncost > distanceOndd ? distanceOncost : distanceOndd;
				if (mindistance > distance) mindistance = distance;
			}
			if (mindistance > maxdistance) maxdistance = mindistance;
		}
		return maxdistance;
	}

}

