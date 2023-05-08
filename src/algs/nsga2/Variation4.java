package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;
import algs.subiterations.GA;
import algs.subiterations.SubGreedy;
import algs.subiterations.SubSa;

import java.util.ArrayList;

//找出两个耗时最长的无人机相互交换
public class Variation4 {
    static int times = 20;

    public static void variation4(Individual individual, Map map) throws CloneNotSupportedException {
        if (individual.uav.size() <= 1) return;
        //u1是耗时最长的无人机
        UAV u1 = individual.uav.get(0);
        //u2是耗时第二长的无人机
        UAV u2 = individual.uav.get(1);
        //找出这两台无人机
        for (int i = 2; i < individual.uav.size(); i++) {
            UAV u = individual.uav.get(i);
            if (u.timeToCompleteAllTasks > u1.timeToCompleteAllTasks) {
                u2 = u1;
                u1 = u;
            } else if (u.timeToCompleteAllTasks < u1.timeToCompleteAllTasks && u.timeToCompleteAllTasks > u2.timeToCompleteAllTasks) {
                u2 = u;
            }
        }

        ArrayList<Integer> arr1 = new ArrayList<>(u1.nodeSet);
        ArrayList<Integer> arr2 = new ArrayList<>(u2.nodeSet);
        for (int i = 0; i < times; i++) {
            UAV u11 = new UAV();
            UAV u22 = new UAV();
            int rand1 = (int) (Math.random() * arr1.size());
            int rand2 = (int) (Math.random() * arr2.size());
            rand1 = arr1.get(rand1);
            rand2 = arr2.get(rand2);
            u11.nodeSet.addAll(u1.nodeSet);
            u22.nodeSet.addAll(u2.nodeSet);
            u11.nodeSet.remove(rand1);
            u11.nodeSet.add(rand2);
            u11.nodeSequence.addAll(u1.nodeSequence);
            u22.nodeSequence.addAll(u2.nodeSequence);

            for (int i1 = u11.nodeSequence.size() - 1; i1 >= 0; i1--) {
                if (u11.nodeSequence.get(i1) == rand1) u11.nodeSequence.remove(i1);
            }
            for (int i1 = u22.nodeSequence.size() - 1; i1 >= 0; i1--) {
                if (u22.nodeSequence.get(i1) == rand2) u22.nodeSequence.remove(i1);
            }

            u22.nodeSet.remove(rand2);
            u22.nodeSet.add(rand1);

            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u11, map)
                    && DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u22, map)
                    && u11.timeToCompleteAllTasks + u22.timeToCompleteAllTasks < u1.timeToCompleteAllTasks + u2.timeToCompleteAllTasks) {
                if (u11.nodeSet.size() > 3)
                    switch (Exchange4.subIterator) {
                        case "sa":
                            SubSa.subSa(u11, map);
                            break;
                        case "Greedy":
                            SubGreedy.subGreedy(u11, map);
                            break;
                        case "ga":
                            GA.ga(u11, map);
                            break;
                        case "no":
                            break;
                    }

                if (u22.nodeSet.size() > 3)
                    switch (Exchange4.subIterator) {
                        case "sa":
                            SubSa.subSa(u22, map);
                            break;
                        case "Greedy":
                            SubGreedy.subGreedy(u22, map);
                            break;
                        case "ga":
                            GA.ga(u22, map);
                            break;
                        case "no":
                            break;
                    }

                u1.nodeSet.clear();
                u1.nodeSequence.clear();
                u2.nodeSet.clear();
                u2.nodeSequence.clear();
                u1.nodeSequence.addAll(u11.nodeSequence);
                u1.nodeSet.addAll(u11.nodeSet);
                u2.nodeSequence.addAll(u22.nodeSequence);
                u2.nodeSet.addAll(u22.nodeSet);
                u1.timeToCompleteAllTasks = u11.timeToCompleteAllTasks;
                u2.timeToCompleteAllTasks = u22.timeToCompleteAllTasks;
                break;
            }
        }
        individual.calfitness1();
        individual.calfitness2();
    }
}
