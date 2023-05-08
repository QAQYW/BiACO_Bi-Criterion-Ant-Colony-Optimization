package algs.kmeans;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Kmean {
    // 用来聚类的点集
    public ArrayList<Point> points;

    // 将聚类结果保存到文件
    FileWriter out = null;
    FileWriter out1 = null;

    // 格式化double类型的输出，保留两位小数
    DecimalFormat dFormat = new DecimalFormat("00.00");

    // 具体执行聚类的对象
    public KMeansCluster kMeansCluster;

    // 簇的数量，迭代次数
    public int numCluster;
    public int numIterator = 200;

    // 点集的数量，生成指定数量的点集
    public int numPoints;

    //节点来源的文件路径
    public static String sourceName = "att532.txt";

    //聚类结果保存路径
    public static String FILEPATH = "D:\\java代码\\nsga2\\新建文本文档 (2).txt";

    public static void main(String[] args) throws IOException {
        //指定点集个数，簇的个数，迭代次数
        Kmean kmeans = new Kmean(53, 200);

        //初始化点集、KMeansCluster对象
        kmeans.init();

        //使用KMeansCluster对象进行聚类
        kmeans.runKmeans();

        kmeans.printRes(sourceName);
        kmeans.saveResToFile(FILEPATH);
    }

    public Kmean(int cluster_number, int iterrator_number) {
        this.numCluster = cluster_number;
        this.numIterator = iterrator_number;
    }

    public void init() {
        try {
            this.initPoints();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kMeansCluster = new KMeansCluster(numCluster, numIterator, points);
    }

    public void runKmeans() {
        kMeansCluster.runKmeans();
    }

    // 初始化点集
    public void initPoints() throws IOException {
        Point tmpPoint;
        int[] x;
        int[] y;
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(
                new FileInputStream(sourceName)));

        strbuff = data.readLine();
        numPoints = Integer.valueOf(strbuff);//文件第一行是城市数量
        points = new ArrayList<>(numPoints);
        x = new int[numPoints];
        y = new int[numPoints];
        for (int i = 0; i < numPoints; i++) {
            strbuff = data.readLine();// 从第二行数据开始读取，数据格式1 6734 1453
            String[] strcol = strbuff.split(" +");// 字符分割
            BigDecimal db1 = new BigDecimal(strcol[1]);
            BigDecimal db2 = new BigDecimal(strcol[2]);


            x[i] = new Double(Double.valueOf(db1.toPlainString())).intValue();// x坐标
            y[i] = new Double(Double.valueOf(db2.toPlainString())).intValue();// y坐标
            tmpPoint = new Point(x[i], y[i]);
            points.add(tmpPoint);
        }
        data.close();
    }

    public void printRes(String filePath) throws IOException {

        System.out.println("==================Centers-I====================");
        out1 = new FileWriter(new File(FILEPATH));
        out = new FileWriter((new File(FILEPATH)), true);
        out1.write(" ");
        out.write("the number of total" + "\r\n");
        out.write(String.valueOf(numPoints + this.numCluster) + "\r\n");
        out.write("the number of charge station" + "\r\n");
        out.write(String.valueOf(this.numCluster) + "\r\n");
        out.write("==================Centers-I====================");


        for (Point center : kMeansCluster.centers) {
            System.out.println(center.toString());
            out.write("\r\n");
            out.write(String.valueOf(center.getClusterID()));
            out.write("  ");

            out.write(dFormat.format(center.getX()));
            out.write("  ");
            out.write(dFormat.format(center.getY()));
            out.flush();
        }
        out.write("\r\n");
        out.write("==================Centers-I====================");
        out.write("\r\n");
        out.close();
        out1.close();

        System.out.println("==================Points====================");

        for (Point point : points) {
            System.out.println(point.toString());
        }
    }

    public void saveResToFile(String filePath) {
        try {
            out = new FileWriter(new File(FILEPATH), true);

            for (Point point : points) {
                out.write(String.valueOf(point.getClusterID()));
                out.write("  ");

                out.write(dFormat.format(point.getX()));
                out.write("  ");
                out.write(dFormat.format(point.getY()));
                out.write("\r\n");
            }

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


