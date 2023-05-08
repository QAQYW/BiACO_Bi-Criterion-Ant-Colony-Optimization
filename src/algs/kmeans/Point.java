package algs.kmeans;

public class Point
{
    // 点的坐标
    private double x;
    private double y;

    // 所在类ID
    private int clusterID = -1;

    public Point(double x, double y) {

        this.x = x;
        this.y = y;
    }

   

	@Override
    public String toString()
    {
        return String.valueOf(getClusterID()) + " " + String.valueOf(this.x) + " " + String.valueOf(this.y);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public int getClusterID()
    {
        return clusterID;
    }

    public void setClusterID(int clusterID)
    {
        this.clusterID = clusterID;
    }
}
