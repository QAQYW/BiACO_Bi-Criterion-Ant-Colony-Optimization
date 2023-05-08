package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GenerateNextPopulation {
    public static String iterator = "variation2";

    public static Population generateNextPopulation(Population pop, Map map, int iteratorTimes) throws CloneNotSupportedException {
        HashSet<Integer> hs = new HashSet<>(300);
        float x1 = (float) 0, x2 = (float) 0.3, x3 = (float) 0.8, x4 = (float) 0.8, t = (float) 0.3;
        switch (iterator) {
            case "variation2":
                x1 = 0;
                x2 = 0;
                x3 = 1;
                break;
            case "variation2+variation4":
                x1 = 0;
                x2 = (float) 0.4;
                x3 = (float) 1;
                break;
            case "variation2+variation3+variation4":
                x1 = 0;
                x2 = (float) 0.3;
                x3 = (float) 0.7;
                x4 = (float) 0.95;
                break;
            case "ReduceTime":
                x1 = 0;
                x2 = 0;
                x3 = 0;
                x4 = 0;
                break;
            case "variation4":
                x1 = 0;
                x2 = 1;
                break;
        }

        Population p = new Population();
        //这两层for循环是用来复制原来的
        for (int i = 0; i < pop.map.size(); i++) {
//        	System.out.println("in GenerateNextPopulation: i = " + i);
        /*
         *首先把这些individual的length和time放入hashset中
         */
//		 String s=String.valueOf(pop.map.get(i).fitness1)+String.valueOf(pop.map.get(i).fitness2);
//		 hs.add(s.hashCode());
//		 
            /*
             * 接下来执行复制操作
             */
            Individual individual;
            individual = pop.map.get(i).clone();
            individual.dominate = new ArrayList<>();
            individual.uav = new ArrayList<>();
//		 System.out.println("开始");
            for (UAV u1 : pop.map.get(i).uav) {
//			 System.out.println("generateNextPopulation"+u1.nodeSequence);
                UAV ux = new UAV();
                ux.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;
                ux.length = u1.length;
                ux.nodeSequence.addAll(u1.nodeSequence);
                ux.nodeSet.addAll(u1.nodeSet);

                individual.uav.add(ux);
            }

            p.map.put(i, individual);
            String s = individual.fitness1 + "" + individual.fitness2;
            hs.add(s.hashCode());

        }
        
//        System.out.println("finish copy");

        float rand;
        float rand2 = (float) Math.random();

        if (rand2 > t) {
//        	System.out.println("-- 1");
            for (int i = 0; i < p.map.size(); i++) {
                rand = (float) Math.random();
                if (rand > x1 && rand < x2) {
                    Variation4.variation4(p.map.get(i), map);

                    String s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                    int count = 0;
                    while (hs.contains(s.hashCode())) {
                        Variation2.variation2(p.map.get(i), map);
                        s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                        if (count++ > 100) {
                            ReduceTime.reduceTime(p.map.get(i), map);
                            s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                        }
                    }
                } else if (rand > x2 && rand < x3) {
                    Variation2.variation2(p.map.get(i), map);
                    String s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                    int count = 0;
                    while (hs.contains(s.hashCode())) {
                        Variation2.variation2(p.map.get(i), map);
                        s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                        if (count++ > 100) {
                            ReduceTime.reduceTime(p.map.get(i), map);
                            s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                        }
                    }
                } else if (rand > x3 && rand < x4) {
                    InitIndividual3.initIndividual3(p.map.get(i), map);

                    String s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                    int count = 0;
                    while (hs.contains(s.hashCode())) {
                        Variation2.variation2(p.map.get(i), map);
                        s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                        if (count++ > 100) {
                            ReduceTime.reduceTime(p.map.get(i), map);
                            s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                        }
                    }
                } else {
                    ReduceTime.reduceTime(p.map.get(i), map);

                    String s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                    while (hs.contains(s.hashCode())) {
                        Variation2.variation2(p.map.get(i), map);
                        s = p.map.get(i).fitness1 + "" + p.map.get(i).fitness2;
                    }
                }

            }
        }
        else {
//        	System.out.println("-- 2");
            ArrayList<Integer> array = new ArrayList<>();
            for (int j = 0; j < pop.map.size(); j++) {
                array.add(j);
            }
            Collections.shuffle(array);
            String s;
            for (int i = 0; i < p.map.size() / 2; i++) {
                ArrayList<Individual> arr = Exchange4.exchange4(p.map.get(array.get(array.size() - 1)), p.map.get(array.get(array.size() - 2)), map);

                s = p.map.get(array.get(array.size() - 1)).fitness1 + "" + p.map.get(array.get(array.size() - 1)).fitness2;
                
//                System.out.println("---------------");
                int count = 0;
                while (hs.contains(s.hashCode())) {
//                	System.out.println("in while");
                    Variation2.variation2(p.map.get(array.get(array.size() - 1)), map);
//                    System.out.println("finish variation2");
                    s = p.map.get(array.get(array.size() - 1)).fitness1 + "" + p.map.get(array.get(array.size() - 1)).fitness2;
                    if (count++ > 100) {
//                    	System.out.println("start reduce time");
                        ReduceTime.reduceTime(p.map.get(i), map);
//                        System.out.println("finish reduce time");
                        s = p.map.get(array.get(array.size() - 1)).fitness1 + "" + p.map.get(array.get(array.size() - 1)).fitness2;
                    }
                }

                s = p.map.get(array.get(array.size() - 2)).fitness1 + "" + p.map.get(array.get(array.size() - 2)).fitness2;
                int count1 = 0;
//                System.out.println("---------------");
                while (hs.contains(s.hashCode())) {
//                	System.out.println("in while");
                    Variation2.variation2(p.map.get(array.get(array.size() - 2)), map);
//                    System.out.println("finish variation2");
                    s = p.map.get(array.get(array.size() - 2)).fitness1 + "" + p.map.get(array.get(array.size() - 2)).fitness2;
                    if (count1++ > 100) {
//                    	System.out.println("start reduce time");
                        ReduceTime.reduceTime(p.map.get(array.get(array.size() - 2)), map);
//                        System.out.println("finish reduce time");
                        s = p.map.get(array.get(array.size() - 2)).fitness1 + "" + p.map.get(array.get(array.size() - 2)).fitness2;
                    }
                }

                array.remove(array.size() - 1);
                array.remove(array.size() - 1);
            }

        }
        return p;
    }
}
