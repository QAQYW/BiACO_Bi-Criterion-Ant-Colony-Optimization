package algs.nsga2;

import algs.Resource.Map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public class Population {
    public static final int MAXSIZE = 100;//��Ⱥ����
    //�����MATH.pow(2,9)����ǰ�趨��hashmap�Ĵ�С�������ع�
    public HashMap<Integer, Individual> map = new HashMap<Integer, Individual>((int) Math.pow(2, 10));//���ڴ�Ÿ���Ⱥ�����и���


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
