package algs.kmeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KMeansCluster
{
    // 聚类中心数
    public int k ;

    // 迭代最大次数
    public int maxIter = 50;

    // 测试点集
    public List<Point> points;

    // 中心点
    public List<Point> centers;

    public static final double MINDISTANCE = 10000.00;

    public KMeansCluster(int k, int maxIter, List<Point> points) {
        this.k = k;
        this.maxIter = maxIter;
        this.points = points;

        //初始化中心点
        initCenters();
    }

    /*
     * 初始化聚类中心
     * 这里的选取策略是，从点集中按序列抽取K个作为初始聚类中心
     */
    public void initCenters()
    {
        centers = new ArrayList<>(k);
        ArrayList<Integer>arr=new ArrayList();
        for(int i=0;i<points.size();i++)
        {
        	arr.add(i);
        }
        Collections.shuffle(arr);
        for (int i = 0; i < k; i++)
        {
            Point tmPoint = points.get(arr.get(i));
            Point center = new Point(tmPoint.getX(), tmPoint.getY());
            center.setClusterID(i + 1);
            centers.add(center);
        }
    }


    /*
     * 停止条件是满足迭代次数
     */
    public void runKmeans()
    {
        // 已迭代次数
        int count = 1;

        while (count++ <= maxIter)
        {
            // 遍历每个点，确定其所属簇
            for (Point point : points)
            {
                assignPointToCluster(point);
            }

            //调整中心点
            adjustCenters();
        }
    }



    /*
     * 调整聚类中心，按照求平衡点的方法获得新的簇心
     */
    public void adjustCenters()
    {
        double sumx[] = new double[k];
        double sumy[] = new double[k];
        int count[] = new int[k];

        // 保存每个簇的横纵坐标之和
        for (int i = 0; i < k; i++)
        {
            sumx[i] = 0.0;
            sumy[i] = 0.0;
            count[i] = 0;
        }

        // 计算每个簇的横纵坐标总和、记录每个簇的个数
        for (Point point : points)
        {
            int clusterID = point.getClusterID();

            // System.out.println(clusterID);
            sumx[clusterID - 1] += point.getX();
            sumy[clusterID - 1] += point.getY();
            count[clusterID - 1]++;
        }

        // 更新簇心坐标
        for (int i = 0; i < k; i++)
        {
            Point tmpPoint = centers.get(i);
            tmpPoint.setX(sumx[i] / count[i]);
            tmpPoint.setY(sumy[i] / count[i]);
            tmpPoint.setClusterID(i + 1);

            centers.set(i, tmpPoint);
        }
    }


    /*划分点到某个簇中，欧式距离标准
     * 对传入的每个点，找到与其最近的簇中心点，将此点加入到簇
     */
    public void assignPointToCluster(Point point)
    {
        double minDistance = MINDISTANCE;

        int clusterID = -1;

        for (Point center : centers)
        {
            double dis = EurDistance(point, center);
            if (dis < minDistance)
            {
                minDistance = dis;
                clusterID = center.getClusterID();
            }
        }
        point.setClusterID(clusterID);

    }

    //欧式距离，计算两点距离
    public double EurDistance(Point point, Point center)
    {
        double detX = point.getX() - center.getX();
        double detY = point.getY() - center.getY();

        return Math.sqrt(detX * detX + detY * detY);
    }
}
