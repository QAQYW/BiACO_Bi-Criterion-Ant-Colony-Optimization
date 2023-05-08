package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Exchange3 {
    public static ArrayList<Individual> exchange3(Individual a, Individual b, Map map) throws CloneNotSupportedException {
        int x = a.uav.size();
        int y = b.uav.size();
        ArrayList<Integer> uava = new ArrayList<Integer>();
        ArrayList<Integer> uavb = new ArrayList<Integer>();
        for (int i = 0; i < x; i++) {
            uava.add(i);
        }
        for (int i = 0; i < y; i++) {
            uavb.add(i);
        }
        Collections.shuffle(uava);
        Collections.shuffle(uavb);

        int x1 = x / 3 * 2;
        int y1 = y / 3 * 2;
        Individual son1 = new Individual();
        Individual son2 = new Individual();
        for (int i = 0; i < x1; i++) {
            int t = uava.get(i);
            UAV u = a.uav.get(t);
            UAV u1 = new UAV();
            u1.timeToCompleteAllTasks = u.timeToCompleteAllTasks;
            u1.length = u.length;
            u1.nodeSequence.addAll(u.nodeSequence);
            u1.nodeSet.addAll(u.nodeSet);
            son1.uav.add(u);
        }

        for (int i = 0; i < y1; i++) {
            int t = uavb.get(i);
            UAV u = b.uav.get(t);
            UAV u1 = new UAV();
            u1.timeToCompleteAllTasks = u.timeToCompleteAllTasks;
            u1.length = u.length;
            u1.nodeSequence.addAll(u.nodeSequence);
            u1.nodeSet.addAll(u.nodeSet);
            son2.uav.add(u);
        }

        //这两个数组是用来存放这两个个体还没放入的节点
        ArrayList<Integer> aleft = new ArrayList<Integer>();
        ArrayList<Integer> bleft = new ArrayList<Integer>();

        for (int i = x1; i < a.uav.size(); i++) {
            int t = uava.get(i);
            UAV u = a.uav.get(t);
            aleft.addAll(u.nodeSet);
        }

        for (int i = y1; i < b.uav.size(); i++) {
            int t = uavb.get(i);
            UAV u = b.uav.get(t);
            bleft.addAll(u.nodeSet);
        }

        Collections.shuffle(aleft);
        Collections.shuffle(bleft);


        son1.uav.add(new UAV());
        son2.uav.add(new UAV());
        Iterator<Integer> it = aleft.iterator();
        while (it.hasNext()) {
            int k = it.next();
            UAV u = son1.uav.get(son1.uav.size() - 1);
            UAV ucopy = new UAV();
            ucopy.nodeSet.addAll(u.nodeSet);
            ucopy.nodeSet.add(k);
            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(ucopy, map)) {
                u.nodeSequence.addAll(ucopy.nodeSequence);
                u.nodeSet.addAll(ucopy.nodeSet);
                u.length = ucopy.length;
                u.timeToCompleteAllTasks = ucopy.timeToCompleteAllTasks;
            } else {
                UAV u2 = new UAV();
                u2.nodeSet.add(k);
                u2.nodeSequence.add(k);
                u2.nodeSequence.add(k);
                Calfitness.calfitness(map, u2);
                son1.uav.add(u2);
            }

        }

        it = bleft.iterator();
        while (it.hasNext()) {
            int k = it.next();
            UAV u = son2.uav.get(son2.uav.size() - 1);
            UAV ucopy = new UAV();
            ucopy.nodeSet.addAll(u.nodeSet);
            ucopy.nodeSet.add(k);
            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(ucopy, map)) {
                u.nodeSequence.addAll(ucopy.nodeSequence);
                u.nodeSet.addAll(ucopy.nodeSet);
                u.length = ucopy.length;
                u.timeToCompleteAllTasks = ucopy.timeToCompleteAllTasks;

            } else {
                UAV u2 = new UAV();
                u2.nodeSet.add(k);
                u2.nodeSequence.add(k);
                u2.nodeSequence.add(k);
                Calfitness.calfitness(map, u2);
                son2.uav.add(u2);
            }

        }
        ArrayList<Individual> result = new ArrayList<Individual>();
        if (son1.uav.get(son1.uav.size() - 1).nodeSet.isEmpty()) {
            son1.uav.remove(son1.uav.size() - 1);
        }

        if (son2.uav.get(son2.uav.size() - 1).nodeSet.isEmpty()) {
            son2.uav.remove(son2.uav.size() - 1);
        }
        son1.calfitness1();
        son1.calfitness2();
        son2.calfitness1();
        son2.calfitness2();
        result.add(son1);
        result.add(son2);
        return result;

    }
}
