package algs.nsga2;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.kmeans.Kmean;
import algs.kmeans.Point;
import algs.method.Calfitness;
import algs.method.DetermineIfTheDroneCanAccomplishItsMission;
import algs.subiterations.GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Variation {
    //�������˻�����
    public static void variation(Individual individual, Map map) throws CloneNotSupportedException {

        int x = individual.uav.size();
        x = x - 3;

        Kmean kmean = new Kmean(x, 200);
        //��ʼ���㼯��KMeansCluster����
        kmean.init();
        //ʹ��KMeansCluster������о���
        kmean.runKmeans();
        individual.uav.clear();
        UAV u;
        //ֻҪ�㼯����
        for (int i = 0; i < x; i++) {
            u = new UAV();
            individual.uav.add(u);
        }
        Point point;
        //�����hashmap���ڲ���
        HashMap<Point, Integer> hash = new HashMap<>();
        for (int i = 0; i < kmean.points.size(); i++) {
            hash.put(kmean.points.get(i), map.csNum + i);
        }
        int p;
        UAV u1;
        ArrayList<UAV> uavCopy = new ArrayList<UAV>();
        //���㼯����
        Collections.shuffle(kmean.points);
        //���ɵ�����
        Iterator<Point> it = kmean.points.iterator();
        //�������нڵ�
        while (it.hasNext()) {   //ȡ��һ���㣬p���������map.node[]�е�λ��
            point = (Point) it.next();
            p = hash.get(point);
            //�ҵ��ýڵ��������˻�
            u = individual.uav.get(point.getClusterID() - 1);
            uavCopy.clear();
            uavCopy.addAll(individual.uav);
            Iterator<UAV> it1 = uavCopy.iterator();


            //�¸ĵ�
            UAV uav111 = new UAV();
            uav111.nodeSet.addAll(u.nodeSet);
            // u.nodeSet.add(p);
            uav111.nodeSet.add(p);
            uav111.nodeSequence.addAll(u.nodeSequence);
            if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uav111, map) == true) {

                u.nodeSequence = uav111.nodeSequence;
                u.nodeSet = uav111.nodeSet;
                u.length = uav111.length;
                u.timeToCompleteAllTasks = uav111.timeToCompleteAllTasks;
            } else {

                boolean flag = false;
                while (it1.hasNext()) {

                    u1 = (UAV) it1.next();
                    if (u == u1) continue;
                    UAV uav1 = new UAV();
                    uav1.nodeSet.addAll(u1.nodeSet);
                    uav1.nodeSet.add(p);
                    uav1.nodeSequence.addAll(u1.nodeSequence);

                    if (DetermineIfTheDroneCanAccomplishItsMission.determineIfTheDroneCanAccomplishItsMission(uav1, map) == true) {
                        u1.nodeSequence = uav1.nodeSequence;
                        u1.length = uav1.length;
                        u1.nodeSet = uav1.nodeSet;
                        u1.timeToCompleteAllTasks = uav1.timeToCompleteAllTasks;

                        flag = true;
                        break;
                    } else {

                    }
                }
                //�������˻����޷����
                if (flag == false) {
                    u1 = new UAV();
                    individual.uav.add(u1);
                    u1.nodeSet.add(p);
                    u1.nodeSequence.add(p);
                    u1.nodeSequence.add(p);
                    Calfitness.calfitness(map, u1);
//        		if(u1.length==0)System.out.println("u1+variation");
                }

            }
        }

        for (int t = individual.uav.size() - 1; t >= 0; t--) {
            if (individual.uav.get(t).nodeSet.size() == 0)
                individual.uav.remove(t);
        }

        for (UAV uxx : individual.uav) {

            GA.ga(uxx, map);
        }

        individual.UAVNum = individual.uav.size();

        individual.calfitness1();
        individual.calfitness2();
    }
}
