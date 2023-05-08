package algs.nsga2;

import algs.Resource.UAV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Individual implements Cloneable {
    public static int selfOwnedNum = 20;
    public static int priceMagnification = 3;
    //多目标优化，这是适应度
    public float fitness1 = (float) (Math.random() * 10);
    public float fitness2 = (float) (Math.random() * 10);
    //	int num;//无人机的数量
    public int n = 0;//种群中有多少个体支配该个体
    public ArrayList<Individual> dominate = new ArrayList<Individual>();//用来存放该个体支配的个体
    /*
     * 上面的参数是nsga2用的
     */
    //本个体无人机的数量
    public int UAVNum;
    public ArrayList<UAV> uav = new ArrayList<>();
    float distance = 0;//解的距离


    //    public int [] 	selectTheUAVForTheNode;//数组的index代表节点的序号，数组存放的是第index个节点被第n个无人机访问
    public Individual() {

    }

    public Individual clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        Individual in = new Individual();
        in.fitness1 = this.fitness1;
        in.fitness2 = this.fitness2;
        for (UAV u1 : this.uav) {
            UAV u = new UAV();
            u.nodeSequence.addAll(u1.nodeSequence);
            u.nodeSet.addAll(u1.nodeSet);
            u.timeToCompleteAllTasks = u1.timeToCompleteAllTasks;

            in.uav.add(u);
        }
        return in;
    }


    //适应度1是无人机飞行总时间
    public void calfitness1() {
        this.fitness1 = 0;
        ArrayList<UAV> p = new ArrayList();
        p.addAll(this.uav);
        Collections.sort(p, new Comparator<UAV>() {

            @Override
            public int compare(UAV o1, UAV o2) {
                // TODO Auto-generated method stub
                if (o1.timeToCompleteAllTasks == o2.timeToCompleteAllTasks) {
                    return 0;
                }
                return o1.timeToCompleteAllTasks > o2.timeToCompleteAllTasks ? -1 : 1;
            }

        });
        for (int i = 0; i < p.size(); i++) {
            if (i >= selfOwnedNum) {
                this.fitness1 += priceMagnification * p.get(i).timeToCompleteAllTasks;
//                System.out.println("priceMagnification = " + Integer.toString(priceMagnification));
            } else {
                this.fitness1 += p.get(i).timeToCompleteAllTasks;
            }
        }
    }

    //适应度2是最迟飞回基地的无人机所耗费时间
    public void calfitness2() {
        float x = Float.MIN_VALUE;
        for (UAV u : this.uav) {
            if (u.timeToCompleteAllTasks > x) {
                x = (float) u.timeToCompleteAllTasks;
            }
        }
        fitness2 = x;
    }


}
