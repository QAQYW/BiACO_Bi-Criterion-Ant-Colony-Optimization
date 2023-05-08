package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/*
 * 初始化种群
 */
public class InitIndividual {
    //对现有无人机每一台都判断一次是否能够完成任务
    public static void initIndividual(Individual individual, Map map) throws CloneNotSupportedException {

        int count = 0;
        //

        //这是还没有分配的无线传感器节点候选集
        ArrayList<Integer> temp = new ArrayList<Integer>();
        //前几个是充电节点，后面才是无线传感器节点
        for (int i = map.csNum; i < map.totalNum; i++) {
            temp.add(i);
        }
        Collections.shuffle(temp);
        UAV u = new UAV();
        //无人机群
        ArrayList<UAV> uList = new ArrayList<>();

        //无人机群的复制版
        ArrayList<UAV> uListCopy = new ArrayList<UAV>();
        uList.add(u);
        int x;
        UAV u1;
        Iterator<Integer> it = temp.iterator();
        while (it.hasNext()) {

            //拿出一个节点
            x = it.next();
            //在已有的无人机中找到能够完成这个任务的无人机
            uListCopy.clear();    //这是副本
            uListCopy.addAll(uList);    //这是副本
            Collections.shuffle(uListCopy);
            boolean flag = false;
            Iterator<UAV> it1 = uListCopy.iterator();

            while (it1.hasNext()) {
                u1 = it1.next();
                UAV uxx = new UAV();
                uxx.nodeSet.addAll(u1.nodeSet);
                uxx.nodeSet.add(x);
                uxx.nodeSequence.addAll(u1.nodeSequence);
                if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uxx, map) == true) {

                    u1.length = uxx.length;
                    u1.nodeSequence = uxx.nodeSequence;
                    u1.nodeSet = uxx.nodeSet;
                    u1.timeToCompleteAllTasks = uxx.timeToCompleteAllTasks;
                    //跳出内层while循环
                    flag = true;
                    break;
                } else {

                }
            }
            if (flag == false)//如果所有的无人机都无法完成选定的任务
            {

                u1 = new UAV();
                u1.nodeSet.add(x);
                u1.nodeSequence.add(x);
                u1.nodeSequence.add(x);
                Calfitness.calfitness(map, u1);
                uList.add(u1);

            }
        }
        for (int k = uList.size() - 1; k >= 0; k--) {
            if (uList.get(k).nodeSet.size() == 0)
                uList.remove(k);
        }

        individual.uav = uList;
        individual.calfitness1();
        individual.calfitness2();
    }
}		
			
		
		  

