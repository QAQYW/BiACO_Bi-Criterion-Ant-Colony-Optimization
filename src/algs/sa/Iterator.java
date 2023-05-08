package algs.sa;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.nsga2.Individual;

import java.util.ArrayList;


public class Iterator {
    public static double T = 1;

    public static double coefficient = 0.9999;
    public static double maxFitness1 = Double.MIN_VALUE;
    public static double minFitness1 = Double.MAX_VALUE;
    public static double maxFitness2 = Double.MIN_VALUE;
    public static double minFitness2 = Double.MAX_VALUE;

    public static Population iterator(Population population, Map map) throws CloneNotSupportedException {

        Population p = new Population();
        //这两层for循环是用来复制原来的
        for (int i = 0; i < population.map.size(); i++) {
            /*
             * 接下来执行复制操作
             */
            Individual individual;
            individual = population.map.get(i).clone();

            individual.uav = new ArrayList<UAV>();
            for (UAV u1 : population.map.get(i).uav) {
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
            // System.out.println(individual.fitness1+" "+individual.fitness2);
//    		 if(individual.fitness1==Float.MAX_VALUE||individual.fitness2==Float.MAX_VALUE)
//    		 {
//    			 for(int t=0;t<individual.uav.size();t++)
//    			 System.out.println(individual.uav.get(t).nodeSequence);
//    		 }
        }

        for (int i = 0; i < p.map.size(); i++) {
            Variation2.variation2(p.map.get(i), map);
            if (p.map.get(i).fitness1 > maxFitness1) maxFitness1 = p.map.get(i).fitness1;
            if (p.map.get(i).fitness2 > maxFitness2) maxFitness2 = p.map.get(i).fitness2;
            if (p.map.get(i).fitness1 < minFitness1) minFitness1 = p.map.get(i).fitness1;
            if (p.map.get(i).fitness2 < minFitness2) minFitness2 = p.map.get(i).fitness2;
        }
        for (int i = 0; i < p.map.size(); i++) {
            if ((p.map.get(i).fitness1 < population.map.get(i).fitness1 && p.map.get(i).fitness2 < population.map.get(i).fitness2) ||
                    (p.map.get(i).fitness1 <= population.map.get(i).fitness1 && p.map.get(i).fitness2 < population.map.get(i).fitness2) ||
                    (p.map.get(i).fitness1 < population.map.get(i).fitness1 && p.map.get(i).fitness2 <= population.map.get(i).fitness2)) {
                population.map.put(i, p.map.get(i));
            } else {
                float rand = (float) Math.random();
                float deltFitness1 = (float) Math.pow((p.map.get(i).fitness1 - minFitness1) / (maxFitness1 - minFitness1), 2);
                float deltFitness2 = (float) Math.pow((p.map.get(i).fitness2 - minFitness2) / (maxFitness2 - minFitness2), 2);

                float de = (float) Math.pow(deltFitness1 + deltFitness2, 0.5);
                double w = Math.pow(Math.E, -de / T);
                System.out.println(w);
//    			 System.out.println(deltFitness1+" "+deltFitness2);
//    			 System.out.println(maxFitness1+" "+maxFitness2+" "+minFitness1+" "+minFitness2);
                // System.out.println(w);
                if (rand < w) {
                    population.map.put(i, p.map.get(i));
                }
            }
        }
        T *= coefficient;
        return population;
    }
}