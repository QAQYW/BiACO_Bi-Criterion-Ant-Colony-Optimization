package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness2;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;

import java.util.ArrayList;
import java.util.Collections;

public class InitIndividual3 {
    public static void initIndividual3(Individual individual, Map map) throws CloneNotSupportedException {
        //这个是用用来标记有没有被访问过的
        boolean[] flag = new boolean[map.distance.length];
        //把所有未分配的节点都塞进去//第一个未分配的节点没塞
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = map.csNum + 1; i < map.totalNum; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
//		
        ArrayList<UAV> tem = new ArrayList<>();
        UAV uu = new UAV();
        //第一个节点塞给第一台无人机
        uu.nodeSet.add(map.csNum);
        uu.nodeSequence.add(map.csNum);
        uu.nodeSequence.add(map.csNum);
        list.remove(new Integer(map.csNum));
        //将这个节点设为被访问过
        flag[map.csNum] = true;
        tem.add(uu);
        Calfitness2.calfitness(map, uu);
        //disCopy数组是用来帮助计算的
        float[][] disCopy = new float[map.totalNum][map.totalNum];
        for (int i = 0; i < map.distance.length; i++)
            for (int j = 0; j < map.distance.length; j++) {
                disCopy[i][j] = map.distance[i][j];
            }
        //用来记录每个无人机第一个任务是什么
        int[] first = new int[500];
        first[tem.size() - 1] = map.csNum;

        while (!list.isEmpty()) {
            UAV u = tem.get(tem.size() - 1);
            if (u.nodeSequence.size() != u.nodeSet.size() * 2) {
                System.out.println("init");
                System.out.println(u.nodeSequence);
                for (int x : u.nodeSet) {
                    System.out.print(x + " ");
                }
            }

            if (u.timeToCompleteAllTasks == Float.MAX_VALUE) {
                try {
                    throw new Exception("初始化错误");
                } catch (Exception e) {
                    System.out.println(u.nodeSequence);
                }
            }

            int min = list.get(0);
            for (int i = map.csNum + 1; i < map.totalNum; i++) {
                //找出当前距离第一个节点最近的节点
                if (disCopy[first[tem.size() - 1]][i] < disCopy[first[tem.size() - 1]][min]
                        && !flag[i]) {
                    min = i;
                }
            }
            list.remove(new Integer(min));
            //找出最近的，无论这台无人机能否完成，都把他当作下一个完成的目标。
            flag[min] = true;

            UAV copy = new UAV();
            copy.nodeSet.addAll(u.nodeSet);
            copy.nodeSequence.addAll(u.nodeSequence);

            copy.nodeSet.add(min);

            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(copy, map)) {

                u.nodeSequence.clear();
                u.nodeSequence.addAll(copy.nodeSequence);
                u.nodeSet.clear();
                u.nodeSet.addAll(copy.nodeSet);
                u.timeToCompleteAllTasks = copy.timeToCompleteAllTasks;
                for (int i = map.csNum + 1; i < map.totalNum; i++) {
                    //更新距离
                    if (disCopy[first[tem.size() - 1]][i] < disCopy[i][min] && !flag[i]) {
                        disCopy[first[tem.size() - 1]][i] = disCopy[i][min];
                    }
                }

            } else {
                disCopy = new float[map.totalNum][map.totalNum];
                for (int i = 0; i < map.distance.length; i++)
                    for (int j = 0; j < map.distance.length; j++) {
                        disCopy[i][j] = map.distance[i][j];
                    }
                u = new UAV();
                u.nodeSequence.add(min);
                u.nodeSequence.add(min);
                u.nodeSet.add(min);
                Calfitness2.calfitness(map, u);
                tem.add(u);
                first[tem.size() - 1] = min;
            }
        }
        individual.uav = tem;
        individual.calfitness1();
        individual.calfitness2();

    }
}
