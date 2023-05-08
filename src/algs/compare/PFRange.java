package algs.compare;

//import algs.compare.PFSet;

public class PFRange {
	double maxcost = -1;
	double mincost = -1;
	double maxdd = -1;
	double mindd = -1;
	
	public PFRange()
	{
		
	}
	
	public void print()
	{
		System.out.println("costrange=["+mincost+","+maxcost+"] ddrange=["+mindd+","+maxdd+"]");
	}
	
	public void firstupdate(PFSet R)
	{
		int n = R.getSet().size();
		mincost = R.getSet().get(0).getCost();
		maxcost = R.getSet().get(0).getCost();
		mindd = R.getSet().get(0).getDD();
		maxdd = R.getSet().get(0).getDD();
		for(int i=1;i<n;i++)
		{
			double cost = R.getSet().get(i).getCost();
			double dd = R.getSet().get(i).getDD();
			if(mincost>cost) mincost = cost;
			if(maxcost<cost) maxcost = cost;
			if(mindd>dd) mindd = dd;
			if(maxdd<dd) maxdd = dd;
		}		
	}
	
	public void update(PFSet R)
	{
		int n = R.getSet().size();
		for(int i=0;i<n;i++)
		{
			double cost = R.getSet().get(i).getCost();
			double dd = R.getSet().get(i).getDD();
			if(mincost>cost) mincost = cost;
			if(maxcost<cost) maxcost = cost;
			if(mindd>dd) mindd = dd;
			if(maxdd<dd) maxdd = dd;
		}		
	}

	public double getMaxcost() {
		return maxcost;
	}

	public void setMaxcost(double maxcost) {
		this.maxcost = maxcost;
	}

	public double getMincost() {
		return mincost;
	}

	public void setMincost(double mincost) {
		this.mincost = mincost;
	}

	public double getMaxdd() {
		return maxdd;
	}

	public void setMaxdd(double maxdd) {
		this.maxdd = maxdd;
	}

	public double getMindd() {
		return mindd;
	}

	public void setMindd(double mindd) {
		this.mindd = mindd;
	}
}

