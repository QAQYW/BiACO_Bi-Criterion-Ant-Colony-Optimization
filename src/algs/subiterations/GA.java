package algs.subiterations;

import algs.Resource.Map;
import algs.Resource.UAV;
import algs.method.Calfitness;
import algs.method.TheWholeArrangement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GA {
    public static void ga(UAV u, Map map) throws CloneNotSupportedException {
        if (u.nodeSet.size() < 4)//如果数量小于5的话，直接全排列就行了
        {
            TheWholeArrangement.theWholeArrangement(u, map);
        } else {
            //设置种群数量
            int populationNum = 20;
            //迭代次数
            int iterations = 30;
            ArrayList<UAV> father = new ArrayList<UAV>(populationNum);

            UAV uxx = new UAV();
            uxx.nodeSequence.addAll(u.nodeSequence);
            uxx.nodeSet.addAll(u.nodeSet);
            uxx.timeToCompleteAllTasks = u.timeToCompleteAllTasks;
            uxx.length = u.length;
            father.add(uxx);
            for (int i = 1; i < populationNum; i++) {
                UAV ux = new UAV();
                ux.nodeSet.addAll(u.nodeSet);
                ux.nodeSequence.addAll(u.nodeSet);
                ux.nodeSequence.addAll(u.nodeSet);
                Collections.shuffle(ux.nodeSequence);
                Calfitness.calfitness(map, ux);
                father.add(ux);
            }
            for (int j = 0; j < iterations; j++) {
                ArrayList<UAV> child = new ArrayList<UAV>();
                for (int i = 0; i < populationNum / 2; i++) {
                    ArrayList<UAV> arr = new ArrayList<UAV>();
                    Variation.variation(father.get(i));
                    Calfitness.calfitness(map, father.get(i));
                    Variation.variation(father.get(i + populationNum / 2));
                    Calfitness.calfitness(map, father.get(i + populationNum / 2));
                    arr = exchange2(father.get(i), father.get(i + populationNum / 2), map);

                    child.add((UAV) arr.get(0));
                    child.add((UAV) arr.get(1));
                }
                father.addAll(child);
                child.clear();
                father.sort((o1, o2) -> {
                    if (o1.timeToCompleteAllTasks > o2.timeToCompleteAllTasks) return 1;
                    else if (o1.timeToCompleteAllTasks < o2.timeToCompleteAllTasks) return -1;
                    return 0;


                });
                for (int i = populationNum - 1; i > populationNum / 2 - 1; i--) {
                    father.remove(i);
                }

            }
            //u.nodeSequence=father.get(0).nodeSequence;
            u.nodeSequence.clear();
            u.nodeSequence.addAll(father.get(0).nodeSequence);
            u.timeToCompleteAllTasks = father.get(0).timeToCompleteAllTasks;
        }
    }


    private static ArrayList<UAV> exchange2(UAV f1, UAV f2, Map map) throws CloneNotSupportedException {
        UAV u1 = new UAV();
        UAV u2 = new UAV();
        u1.nodeSet.addAll(f1.nodeSet);
        u2.nodeSet.addAll(f2.nodeSet);
        u1.nodeSequence.addAll(u1.nodeSet);
        u1.nodeSequence.addAll(u1.nodeSet);
        u2.nodeSequence.addAll(u1.nodeSet);
        u2.nodeSequence.addAll(u1.nodeSet);
        Collections.shuffle(u1.nodeSequence);
        Collections.shuffle(u2.nodeSequence);
        Calfitness.calfitness(map, u1);
        Calfitness.calfitness(map, u2);
        ArrayList<UAV> arr = new ArrayList();
        arr.add(u1);
        arr.add(u2);
        return arr;
    }


    private static UAV Variation(UAV ux, Map map) throws CloneNotSupportedException {
        UAV u = new UAV();
        u.nodeSet.addAll(ux.nodeSet);
        u.nodeSequence.addAll(ux.nodeSequence);
        int size = u.nodeSequence.size();
        int x = (int) (size * Math.random());
        int y = (int) (size * Math.random());
        int temp = u.nodeSequence.get(x);
        u.nodeSequence.set(x, u.nodeSequence.get(y));
        u.nodeSequence.set(y, temp);
        Calfitness.calfitness(map, u);
        return u;
    }

    private static ArrayList<UAV> exchange(UAV u1, UAV u2, Map map) throws CloneNotSupportedException {
        if (u1.nodeSequence.size() == 2 || u2.nodeSequence.size() == 2) return null;

        UAV child1 = new UAV();
        UAV child2 = new UAV();
        child1.nodeSet.addAll(u1.nodeSet);
        child2.nodeSet.addAll(u1.nodeSet);
        int i;
        HashMap<Integer, Count> aaa = new HashMap();
        for (Integer x : child1.nodeSet) {
            Count p = new Count();
            p.count = 0;
            aaa.put(x, p);
        }
        //取父亲1的前一半
        for (i = 0; i < u1.nodeSet.size(); i++) {

            child1.nodeSequence.add(u1.nodeSequence.get(i));
            aaa.get(u1.nodeSequence.get(i)).count++;

        }
        //取父亲2的后一半
        for (i = u1.nodeSet.size(); i < u1.nodeSet.size() * 2; i++) {

            child1.nodeSequence.add(u2.nodeSequence.get(i));
            aaa.get(u2.nodeSequence.get(i)).count++;
        }
        for (i = 0; i < u1.nodeSequence.size(); i++) {
            if (child1.nodeSequence.size() != u1.nodeSequence.size()) {
                System.out.println(child1.nodeSequence);
                System.out.println(child1.nodeSet);
                System.out.println(u1.nodeSequence);
                System.out.println(u1.nodeSet);
            }
            while (aaa.get(child1.nodeSequence.get(i)).count > 2) {
                for (Integer p : child1.nodeSet) {
                    if (aaa.get(p).count < 2) {
                        aaa.get(child1.nodeSequence.get(i)).count--;
                        aaa.get(p).count++;
                        child1.nodeSequence.set(i, p);
                        break;
                    }
                }

            }
        }
        aaa.clear();
        for (Integer x : child2.nodeSet) {
            Count p = new Count();
            p.count = 0;
            aaa.put(x, p);
        }
        for (i = 0; i < u1.nodeSequence.size() / 2; i++) {
            child2.nodeSequence.add(u2.nodeSequence.get(i));
            aaa.get(u2.nodeSequence.get(i)).count++;
        }
        for (; i < u1.nodeSequence.size(); i++) {
            child2.nodeSequence.add(u1.nodeSequence.get(i));
            aaa.get(u1.nodeSequence.get(i)).count++;
        }

        for (i = 0; i < u1.nodeSequence.size(); i++) {
            while (aaa.get(child2.nodeSequence.get(i)).count > 2) {
                for (Integer p : child2.nodeSet) {
                    if (aaa.get(p).count < 2) {
                        aaa.get(child2.nodeSequence.get(i)).count--;
                        aaa.get(p).count++;
                        child2.nodeSequence.set(i, p);
                        break;
                    }
                }
            }
        }
        ArrayList<UAV> result = new ArrayList();
        Calfitness.calfitness(map, child1);

        Calfitness.calfitness(map, child2);

        result.add(child1);
        result.add(child2);
        return result;
    }


}


class Count {

    int count;
}