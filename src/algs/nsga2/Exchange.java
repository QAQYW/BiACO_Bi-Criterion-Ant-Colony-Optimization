package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;

import java.util.ArrayList;
import java.util.Collections;

/*
 * 交换算子，父亲a和父亲b
 */
public class Exchange {
    public static ArrayList<Individual> exchange(Individual a, Individual b, Map map) throws CloneNotSupportedException {
        int i, j;
        int[] aselectTheUAVForTheNode = new int[map.totalNum];
        int[] bselectTheUAVForTheNode = new int[map.totalNum];
        for (i = 0; i < a.uav.size(); i++) {
            for (Integer xxx : a.uav.get(i).nodeSet) {
                aselectTheUAVForTheNode[xxx] = i;
            }
        }

        for (i = 0; i < b.uav.size(); i++) {
            for (Integer xxx : b.uav.get(i).nodeSet) {
                bselectTheUAVForTheNode[xxx] = i;
            }
        }
        
        //交叉之后生成两个孩子
        Individual son1 = new Individual();
        Individual son2 = new Individual();
        int greater = a.uav.size() > b.uav.size() ? a.uav.size() : b.uav.size();//无人机数量较多的个体的无人机数量
        int less = a.uav.size() < b.uav.size() ? a.uav.size() : b.uav.size();//无人机数量较小的个体的无人机数量

        //son1是无人机较多个体的儿子
        for (i = 0; i < greater; i++) {
            UAV u = new UAV();
            son1.uav.add(u);
        }


        //son2是无人机较少个体的儿子
        for (i = 0; i < less; i++) {
            UAV u = new UAV();
            son2.uav.add(u);
        }

        UAV u;
        /*
         * 处理son1
         */
        //先放入前半部分的节点//不会出现有无人机无法完成的情况
        for (i = map.csNum; i < (map.totalNum - map.csNum) / 2 + map.csNum; i++) {

            u = son1.uav.get(aselectTheUAVForTheNode[i]);
            u.nodeSet.add(i);
            DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u, map);
        }
        //放入后半部分的时候就会出现无人机无法完成的情况
        for (; i < map.totalNum; i++) {
            /*
             * 改bug专用
             */
            u = son1.uav.get(bselectTheUAVForTheNode[i]);
            UAV uavcopy = new UAV();
            uavcopy.nodeSet.addAll(u.nodeSet);
            uavcopy.nodeSet.add(i);
            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uavcopy, map)) {
                u.nodeSequence = uavcopy.nodeSequence;
                u.nodeSet = uavcopy.nodeSet;
                u.length = uavcopy.length;
                u.timeToCompleteAllTasks = uavcopy.timeToCompleteAllTasks;
            } else {

                boolean flag = false;
                ArrayList<UAV> arr = new ArrayList();
                arr.addAll(son1.uav);
                Collections.shuffle(arr);
                for (j = 0; j < son1.uav.size(); j++) {
                    u = arr.get(j);
                    if (u == son1.uav.get(bselectTheUAVForTheNode[i])) continue;
                    UAV uavcopy1 = new UAV();
                    uavcopy1.nodeSet.addAll(u.nodeSet);
                    uavcopy1.nodeSet.add(i);
                    if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uavcopy1, map)) {
                        u.nodeSequence = uavcopy1.nodeSequence;
                        u.nodeSet = uavcopy1.nodeSet;
                        u.length = uavcopy1.length;
                        u.timeToCompleteAllTasks = uavcopy1.timeToCompleteAllTasks;
                        flag = true;
                        break;
                    } else {

                    }
                }
                if (flag == false) {
                    u = new UAV();
                    u.nodeSet.add(i);
                    son1.uav.add(u);
                    DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u, map);
                }
            }
        }
        for (int i1 = son1.uav.size() - 1; i1 >= 0; i1--) {
            if (son1.uav.get(i1).nodeSet.size() == 0)
                son1.uav.remove(i1);
        }

        /*
         * 处理son2
         */
        int x;
        for (i = map.csNum; i < (map.totalNum - map.csNum) / 2 + map.csNum; i++) {
            if (bselectTheUAVForTheNode[i] >= less)
                x = (int) (Math.random() * less);
            else
                x = bselectTheUAVForTheNode[i];

            u = son2.uav.get(x);
            UAV uavcopy = new UAV();
            uavcopy.nodeSet.addAll(u.nodeSet);
            uavcopy.nodeSet.add(i);

            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uavcopy, map)) {
                u.nodeSequence = uavcopy.nodeSequence;
                u.nodeSet = uavcopy.nodeSet;
                u.length = uavcopy.length;
                u.timeToCompleteAllTasks = uavcopy.timeToCompleteAllTasks;
            } else {

                boolean flag = false;
                ArrayList<UAV> arr = new ArrayList();
                arr.addAll(son2.uav);
                Collections.shuffle(arr);
                for (j = 0; j < son2.uav.size(); j++) {
                    u = arr.get(j);
                    if (u == son2.uav.get(x)) continue;
                    UAV uav = new UAV();
                    uav.nodeSet.addAll(u.nodeSet);
                    uav.nodeSet.add(i);
                    if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uav, map)) {
                        u.nodeSequence = uav.nodeSequence;
                        u.nodeSet = uav.nodeSet;
                        u.timeToCompleteAllTasks = uav.timeToCompleteAllTasks;
                        u.length = uav.length;
                        flag = true;
                        break;
                    } else {

                    }
                }
                if (flag == false) {
                    u = new UAV();
                    u.nodeSet.add(i);
                    son1.uav.add(u);
                    DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u, map);
                }
            }
        }

        for (i = (map.totalNum - map.csNum) / 2 + map.csNum; i < map.totalNum; i++) {
            if (aselectTheUAVForTheNode[i] >= less)
                x = (int) (Math.random() * less);
            else
                x = aselectTheUAVForTheNode[i];

            u = son2.uav.get(x);
            UAV uavcopy = new UAV();
            uavcopy.nodeSet.addAll(u.nodeSet);
            uavcopy.nodeSet.add(i);

            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uavcopy, map)) {
                u.nodeSequence = uavcopy.nodeSequence;
                u.nodeSet = uavcopy.nodeSet;
                u.length = uavcopy.length;
                u.timeToCompleteAllTasks = uavcopy.timeToCompleteAllTasks;
            } else {

                boolean flag = false;
                ArrayList<UAV> arr = new ArrayList();
                arr.addAll(son2.uav);
                Collections.shuffle(arr);
                for (j = 0; j < son2.uav.size(); j++) {
                    u = arr.get(j);
                    if (u == son2.uav.get(x)) continue;
                    UAV uav = new UAV();
                    uav.nodeSet.addAll(u.nodeSet);
                    uav.nodeSet.add(i);
                    if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uav, map)) {
                        u.nodeSequence = uav.nodeSequence;
                        u.nodeSet = uav.nodeSet;
                        u.timeToCompleteAllTasks = uav.timeToCompleteAllTasks;
                        u.length = uav.length;
                        flag = true;
                        break;
                    } else {

                    }
                }
                if (flag == false) {
                    u = new UAV();
                    u.nodeSet.add(i);
                    son1.uav.add(u);
                    DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(u, map);
                }
            }
        }


        for (int y = son2.uav.size() - 1; y >= 0; y--) {
            if (son2.uav.get(y).nodeSet.size() == 0)
                son2.uav.remove(y);
        }
        son1.calfitness1();
        son1.calfitness2();
        son2.calfitness1();
        son2.calfitness2();
        ArrayList<Individual> uList = new ArrayList<>();
        uList.add(son1);
        uList.add(son2);
        son1.UAVNum = son1.uav.size();
        son2.UAVNum = son2.uav.size();
        son1.UAVNum = son1.uav.size();
        son2.UAVNum = son2.uav.size();
        return uList;
    }

}

