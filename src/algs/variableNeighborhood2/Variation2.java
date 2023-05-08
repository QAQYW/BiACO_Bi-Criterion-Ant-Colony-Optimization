package algs.variableNeighborhood2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;
import algs.nsga2.Individual;

import java.util.ArrayList;
import java.util.Collections;

public class Variation2 {
    public static void variation2(Individual individual, Map map) throws CloneNotSupportedException {
        if (individual.uav.size() == 0 || individual.uav.size() == 1) return;
        //x是无人机的编号。随机生成
        int x = (int) (Math.random() * individual.uav.size());
        //这是对应的无人机
        UAV u = individual.uav.get(x);
        //用于存放无人机要遍历的节点
        ArrayList<Integer> arr = new ArrayList<Integer>();
        //将无人机要遍历的节点放入arr中
        arr.addAll(u.nodeSet);
        //x是节点在arr中的位置，随机生成
        x = (int) Math.random() * arr.size();
        //x是节点的编号
        x = (int) arr.get(x);

        u.nodeSet.remove(x);

        if (u.nodeSet.isEmpty()) individual.uav.remove(u);
        else {

            // System.out.println(u.nodeSequence+"start");
            for (int t = u.nodeSequence.size() - 1; t >= 0; t--) {
                if (u.nodeSequence.get(t) == x)
                    u.nodeSequence.remove(t);
            }
            // System.out.println(x);

            Calfitness.calfitness(map, u);
        }

        if (Math.random() > 0.8) {

            UAV u11 = new UAV();
            u11.nodeSet.add(x);
            u11.nodeSequence.add(x);
            u11.nodeSequence.add(x);
//        	 System.out.println(u1.nodeSet);
            Calfitness.calfitness(map, u11);
            //if(u1.nodeSequence.size()!=u1.nodeSet.size()*2)System.out.println("新生成的无人机错误");
            individual.uav.add(u11);
            individual.UAVNum = individual.uav.size();
            individual.calfitness1();
            individual.calfitness2();
            return;
        }

        boolean flag = false;
        ArrayList<UAV> uavCopy = new ArrayList<UAV>();
        uavCopy.addAll(individual.uav);
        Collections.shuffle(uavCopy);

        UAV ux = uavCopy.get(0);

        if (ux == u) {
            if (uavCopy.size() == 1) {
                UAV uxx = new UAV();
                uxx.nodeSet.add(x);
                uxx.nodeSequence.add(x);
                uxx.nodeSequence.add(x);
                Calfitness.calfitness(map, uxx);
                //if(u1.nodeSequence.size()!=u1.nodeSet.size()*2)System.out.println("新生成的无人机错误");
                individual.uav.add(uxx);
                individual.UAVNum = individual.uav.size();
                individual.calfitness1();
                individual.calfitness2();
                return;
            } else {
                ux = uavCopy.get(1);
            }
        }

        UAV u1 = new UAV();
        u1.nodeSet.addAll(ux.nodeSet);
        u1.nodeSet.add(x);
        if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u1, map)) {
            ux.nodeSequence = u1.nodeSequence;
            ux.length = u1.length;
            ux.nodeSet = u1.nodeSet;
            ux.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;
            flag = true;

        } else {
            UAV u11 = new UAV();
            u11.nodeSet.add(x);
            u11.nodeSequence.add(x);
            u11.nodeSequence.add(x);
//            	 System.out.println(u1.nodeSet);
            Calfitness.calfitness(map, u11);
            //if(u1.nodeSequence.size()!=u1.nodeSet.size()*2)System.out.println("新生成的无人机错误");
            individual.uav.add(u11);
        }

        individual.UAVNum = individual.uav.size();
        individual.calfitness1();
        individual.calfitness2();

        return;
    }


}