package algs.method;

import algs.Resource.Map;
import algs.compare.PF;
import algs.compare.PFSet;
import algs.kmeans.Kmean;
import algs.nsga2.*;
import algs.tool.Read;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Nsga2Main {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {

        /*
         * 下面的程序用来看程序开始运行时的时间
         */
        Calendar Cld = Calendar.getInstance();
        int YY = Cld.get(Calendar.YEAR);
        int MM = Cld.get(Calendar.MONTH) + 1;
        int DD = Cld.get(Calendar.DATE);
        int HH = Cld.get(Calendar.HOUR_OF_DAY);
        int mm = Cld.get(Calendar.MINUTE);
        int SS = Cld.get(Calendar.SECOND);
        int MI = Cld.get(Calendar.MILLISECOND);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Date before = new Date();
        Date now = new Date();
        System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));


        /*
         * 主程序
         */
        Map map = new Map();

        Kmean kmean = new Kmean(5, 200);

        //初始化点集、KMeansCluster对象
        kmean.init();
        //使用KMeansCluster对象进行聚类
        kmean.runKmeans();

        Read.distribution = "normal";
        for (int n = 0; n < 20; n++) {
            System.out.println(n);
            Read.FILEOUTPATH = "D:\\NAGA2实例2\\node1173" + Read.distribution + n + ".txt";
            Read.read(map);
            Population father = new Population();

            father.init(map);
            System.out.println("init");
            ArrayList<Individual> firstRank = SelectTheFirstRank.selectTheFirstRank(father);
            System.out.println("first");
            for (int i = 0; i < 0; i++) {
                Population child = GenerateNextPopulation.generateNextPopulation(father, map, i);
                father = NSGA2Select.nsga2(father, child);
                System.out.println(i);
                if ((i) % 50 == 0) {
                    firstRank = SelectTheFirstRank.selectTheFirstRank(father);
                    for (Individual in : firstRank) {
                        System.out.println("总耗时 " + in.fitness1 + " 时间" + in.fitness2 + " 无人机数量" + in.uav.size());
                    }
                }
                firstRank = SelectTheFirstRank.selectTheFirstRank(father);
            }
            for (Individual in : firstRank) {
                System.out.println("总耗时 " + in.fitness1 + " 时间" + in.fitness2 + " 无人机数量" + in.uav.size());
            }
        }


        /*
         * 以下的代码用来看程序运行完的时间
         */

        cal = Calendar.getInstance();
        date = cal.getTime();
        System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));
        System.out.println("charge");
    }


    public static PFSet nsga2() throws IOException, CloneNotSupportedException {
        System.out.println("运行nsga2");
        /*
         * 下面的程序用来看程序开始运行时的时间
         */
        Calendar Cld = Calendar.getInstance();
        int YY = Cld.get(Calendar.YEAR);
        int MM = Cld.get(Calendar.MONTH) + 1;
        int DD = Cld.get(Calendar.DATE);
        int HH = Cld.get(Calendar.HOUR_OF_DAY);
        int mm = Cld.get(Calendar.MINUTE);
        int SS = Cld.get(Calendar.SECOND);
        int MI = Cld.get(Calendar.MILLISECOND);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Date before = new Date();
        Date now = new Date();
        System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));



        /*
         * 主程序
         */
        Map map = new Map();

        Kmean kmean = new Kmean(5, 200);

        //初始化点集、KMeansCluster对象
        kmean.init();

        //使用KMeansCluster对象进行聚类
        kmean.runKmeans();

        Read.read(map);


        Population father = new Population();

        father.init(map);
        System.out.println("init");
        ArrayList<Individual> firstRank = SelectTheFirstRank.selectTheFirstRank(father);
        System.out.println("first");
        for (int i = 0; i < 1000; i++) {
            Population child = GenerateNextPopulation.generateNextPopulation(father, map, i);
            father = NSGA2Select.nsga2(father, child);
            firstRank = SelectTheFirstRank.selectTheFirstRank(father);
        }
        for (Individual in : firstRank) {
            System.out.println("总耗时 " + in.fitness1 + " 时间" + in.fitness2 + " 无人机数量" + in.uav.size());
        }


        /*
         * 以下的代码用来看程序运行完的时间
         */

        cal = Calendar.getInstance();
        date = cal.getTime();
        System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));

        PFSet pfset = new PFSet();
        for (Individual in : firstRank) {
            PF pf = new PF(in.fitness1, in.fitness2, "nsga2");
            pfset.addPF(pf);
        }


        /*
         * 以下的代码用来看程序运行完的时间
         */

        cal = Calendar.getInstance();
        date = cal.getTime();

        return pfset;
    }

}