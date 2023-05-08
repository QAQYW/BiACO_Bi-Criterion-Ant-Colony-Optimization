package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness2;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;
import algs.subiterations.GA;
import algs.subiterations.SubGreedy;
import algs.subiterations.SubSa;

import java.util.ArrayList;
import java.util.Collections;

public class Variation2 {
    //随机找一台无人机，拿出一个节点，放入其他无人机
    public static void variation2(Individual individual, Map map) throws CloneNotSupportedException {
        if (individual.uav.size() == 0 || individual.uav.size() == 1) return;
        //x是无人机的编号。随机生成
        int x = (int) (Math.random() * individual.uav.size());
        //这是对应的无人机
        UAV u = individual.uav.get(x);

        //改bug
        if (u.timeToCompleteAllTasks == Float.MAX_VALUE) {
            try {
                throw new Exception("variation2无人机进来就出错了");
            } catch (Exception e) {
                System.out.println(u.nodeSequence + "位置1");
            }
        }
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
            Calfitness2.calfitness(map, u);

            if (u.timeToCompleteAllTasks == Float.MAX_VALUE) {
                try {
                    throw new Exception("variation2无人机减少两个节点后反而出错了");
                } catch (Exception e) {
                    System.out.println(u.nodeSequence + "位置2");
                }
            }
        }

        boolean flag = false;
        ArrayList<UAV> uavCopy = new ArrayList<UAV>(individual.uav);
        Collections.shuffle(uavCopy);
        for (UAV ux : uavCopy) {
            if (ux == u) continue;
            UAV u1 = new UAV();
            u1.nodeSet.addAll(ux.nodeSet);
            u1.nodeSet.add(x);
            u1.nodeSequence.addAll(ux.nodeSequence);
            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u1, map)) {

                if (u1.nodeSet.size() > 3)
                    switch (Exchange4.subIterator) {
                        case "sa":
                            SubSa.subSa(u1, map);
                            break;
                        case "Greedy":
                            SubGreedy.subGreedy(u1, map);
                            break;
                        case "ga":
                            GA.ga(u1, map);
                            break;
                        case "no":
                            break;
                    }


                ux.nodeSequence = u1.nodeSequence;
                ux.length = u1.length;
                ux.nodeSet = u1.nodeSet;
                ux.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;
                // if(ux.timeToCompleteAllTasks==Float.MAX_VALUE)System.out.println("variation2,2");
                flag = true;
                break;
            }

        }
        //如果无人机组中找不到一个可以完成该任务的无人机，则新生成一个无人机
        if (!flag) {
            UAV u1 = new UAV();
            u1.nodeSet.add(x);
            u1.nodeSequence.add(x);
            u1.nodeSequence.add(x);
            Calfitness2.calfitness(map, u1);
            if (u1.timeToCompleteAllTasks == Float.MAX_VALUE) {
                System.out.println(u.nodeSequence + "位置5");
                System.out.println("deadline为" + map.node[u.nodeSequence.get(0)].deadline);
                System.out.println("飞行距离+计算时间为" + map.distance[map.whichChargingStationIsCloser[u.nodeSequence.get(0)]][u.nodeSequence.get(0)] / UAV.speed * 2 + map.node[u.nodeSequence.get(0)].missionTime);
            }
            individual.uav.add(u1);
        }

        individual.UAVNum = individual.uav.size();
        individual.calfitness1();
        individual.calfitness2();
    }

}
