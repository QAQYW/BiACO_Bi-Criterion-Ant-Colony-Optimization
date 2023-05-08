package algs.compare;

import java.text.DecimalFormat;


public class PF {

    double cost;
    double dd;//dissatisfaction degree，dd的单位是100%
    double normalcost = -1;
    double normaldd = -1;
    String contributor;


    public void CostNomalization(double maxcost, double mincost) {
        if (cost > maxcost) {
            System.out.println("Strange value: max=" + maxcost + " min=" + mincost + " cost=" + cost);
        }
        if (maxcost == mincost) normalcost = 0;
        else normalcost = (cost - mincost) / (maxcost - mincost);
    }

    public void DDNomalization(double maxdd, double mindd) {
        if (maxdd == mindd) normaldd = 0;
        else normaldd = (dd - mindd) / (maxdd - mindd);
    }

    public PF(double c, double dd, String con) {
        DecimalFormat format_3 = new java.text.DecimalFormat("#.###");

        cost = Double.parseDouble(format_3.format(c));
//		System.out.println("c="+c+" cost="+cost);
        this.dd = Double.parseDouble(format_3.format(dd));
        contributor = con;
    }

    public String toString() {
        return "cost=" + cost + " dd=" + dd + " M=" + contributor;
    }

    public String toValueString() {
        return cost + "," + dd;
    }

    public int compare(PF obj) {
        if (cost == obj.getCost() && dd == obj.getDD()) return 0;
        else if (cost <= obj.getCost() && dd <= obj.getDD()) return -1;
        else if (cost >= obj.getCost() && dd >= obj.getDD()) return 1;
        return 2;
    }

    public String getContributor() {
        return contributor;
    }

    public String getvaluestring() {
        return cost + "," + dd;
    }

    public double getCostNormalization() {
        return normalcost;
    }

    public double getCost() {
        return cost;
    }

    public double getDDNormalization() {
        return normaldd;
    }

    public double getDD() {
        return dd;
    }

    public void print() {
        System.out.println("cost=" + cost + " dd=" + dd + " M=" + contributor);
    }

    public void printNormal() {
        DecimalFormat format_4 = new java.text.DecimalFormat("#.####");
        System.out.println("narmalcost=" + format_4.format(this.normalcost) + " normaldd=" + format_4.format(this.normaldd) + " M=" + contributor);
    }
}