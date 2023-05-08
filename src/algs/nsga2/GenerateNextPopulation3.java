package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GenerateNextPopulation3 {
    public static Population generateNextPopulation3(Population pop, Map map, int iteratorTimes) throws CloneNotSupportedException {
        HashSet<Integer> hs = new HashSet<Integer>(300);


        Population p = new Population();
        //这两层for循环是用来复制原来的
        for (int i = 0; i < pop.map.size(); i++) {

            /*
             * 接下来执行复制操作
             */
            Individual individual;
            individual = pop.map.get(i).clone();
            individual.dominate = new ArrayList<Individual>();
            individual.uav = new ArrayList<UAV>();
            for (UAV u1 : pop.map.get(i).uav) {
                UAV ux = new UAV();
                ux.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;
                ux.length = u1.length;
                for (int ttt = 0; ttt < u1.nodeSequence.size(); ttt++) {
                    ux.nodeSequence.add(u1.nodeSequence.get(ttt));
                }
                for (Integer x : u1.nodeSet) {
                    ux.nodeSet.add(x);
                }

                individual.uav.add(ux);
            }

            p.map.put(i, individual);

        }


        float rand2 = (float) Math.random();

        if (rand2 > 0.5)
            for (int i = 0; i < p.map.size(); i++) {
                ReduceTime.reduceTime(p.map.get(i), map);
            }
        else {
            ArrayList<Integer> array = new ArrayList();
            for (int j = 0; j < pop.map.size(); j++) {
                array.add(j);
            }
            Collections.shuffle(array);
            for (int i = 0; i < p.map.size() / 2; i++) {
                ArrayList<Individual> arr = Exchange.exchange(p.map.get(array.size() - 1), p.map.get(array.size() - 2), map);
                array.remove(array.size() - 1);
                array.remove(array.size() - 1);
                p.map.put(i, (Individual) arr.get(0));
                p.map.put(i + p.map.size() / 2, (Individual) arr.get(1));
            }
        }
        return p;
    }
}
