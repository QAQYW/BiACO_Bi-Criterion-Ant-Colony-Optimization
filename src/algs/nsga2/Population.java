package algs.nsga2;

import algs.Resource.Map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public class Population {
    public static final int MAXSIZE = 100;//种群数量
    //这里的MATH.pow(2,9)是提前设定好hashmap的大小，避免重构
    public HashMap<Integer, Individual> map = new HashMap<Integer, Individual>((int) Math.pow(2, 10));//用于存放该种群的所有个体


    public void init(Map m) throws CloneNotSupportedException {

        HashSet<String> hs = new HashSet<>();
        for (int i = 0; i < Population.MAXSIZE; i++) {
            Individual in = new Individual();
            InitIndividual3.initIndividual3(in, m);
            String s = in.fitness1 + " " + in.fitness2;

            int x = 0;
            while (hs.contains(s)) {
                Variation2.variation2(in, m);
                s = in.fitness1 + " " + in.fitness2;
                if (x++ > 100) {

                    ReduceTime.reduceTime(in, m);
                    s = in.fitness1 + " " + in.fitness2;
                }
            }
            hs.add(s);
            map.put(i, in);
        }
    }
}
