package algs.nsga2;

import java.util.ArrayList;
import java.util.HashMap;


public class NSGA2Select {
    /*
     * nsga2的选择部分
     */
    public static Population nsga2(Population father, Population child) { // 两代种群的所有个体都参与筛选
        HashMap<Integer, Individual> fatherAndChild = new HashMap<Integer, Individual>();
        fatherAndChild.putAll(father.map);
        for (int i = 0; i < Population.MAXSIZE; i++) {
            fatherAndChild.put(i + father.map.size(), child.map.get(i));
        }


        // 对每个个体进行处理，找出被它支配和支配它的个体
        fatherAndChild.forEach((key, in) -> {
            in.n = 0;
            in.dominate.clear();
        });
        fatherAndChild.forEach((key1, in) -> {
            fatherAndChild.forEach((key, o) -> {
                if (((in.fitness1 < o.fitness1) && (in.fitness2 < o.fitness2)) || ((in.fitness1 <= o.fitness1) && (in.fitness2 < o.fitness2)) || ((in.fitness1 < o.fitness1) && (in.fitness2 <= o.fitness2)))// 找出in支配的个体放入dominate中
                {
                    in.dominate.add(o);
                } else if (((in.fitness1 > o.fitness1) && (in.fitness2 > o.fitness2)) || ((in.fitness1 >= o.fitness1) && (in.fitness2 > o.fitness2)) || ((in.fitness1 > o.fitness1) && (in.fitness2 >= o.fitness2)))// 碰上一个支配in的个体就++n
                {
                    in.n++;
                }
            });
        });


        ArrayList<ArrayList<Individual>> rank = new ArrayList<ArrayList<Individual>>();// 对种群进行分级别
        // 外层的ArrayList是存放每一个rank，内层的ArrayList是存放该rank对应的个体

        while (!fatherAndChild.isEmpty())// 本函数将所有个体进行分级，分级后放入rank数组中
        {
            ArrayList<Individual> po = new ArrayList<Individual>();// 用来存放第x rank的个体，这里的x是while的第x个循环
            for (int i = 0; i < Population.MAXSIZE * 2; i++)// 对哈希表中的每个个体进行遍历
            {
                Individual indi = fatherAndChild.get(i);
                if (indi == null)
                    continue;
                if (indi.n == 0)// 如果发现该个体的n为0，即没有支配该个体的其他个体
                {
                    po.add(indi);// 将该个体放入第n rank的集合中
                    fatherAndChild.remove(i);// 把该个体从哈希表中去除

                }
            }
            for (Individual indi : po)
                for (Individual in : indi.dominate) {
                    in.n--;
                }
            rank.add(po);// 该rank的所有个体都放入ArrayList后将该ArrayList放入对应rank的位置
        }

        // 查找哪个rank放入新生一代之后会超出MAXSIZE
        int num = Population.MAXSIZE;
        int index = 0;// 这是用来指示第几rank的
        while (true) {
            if (num - rank.get(index).size() > 0) {
                num = num - rank.get(index).size();
                ++index;

            } else {
                break;
            }
        }


        // 找出该rank
        ArrayList<Individual> po = rank.get(index);
        //System.out.println("最后一层有"+po.size()+"个");
        // 对该rank根据fitness1的大小进行排序，根据fitness2也可以

        po.sort((Individual o1, Individual o2) -> { // TODO Auto-generated method stub
//			int i = o1.fitness1 > o2.fitness1 ? 1 : -1;
//			return i;
                    if (o1.fitness1 > o2.fitness1) {
                        return 1;
                    } else if (o1.fitness1 < o2.fitness1) {
                        return -1;
                    } else {
                        return 0;
                    }

                }

        );

        //归一化
        double fitness1sum = 0, fitness2sum = 0;
        int numSum = 0;
        for (Individual o : po) {
            fitness1sum += o.fitness1;
            fitness2sum += o.fitness2;
            numSum += o.uav.size();
        }


        // 对该rank的每一个个体计算其distance
        for (int i = 0; i < po.size(); i++) {
            if (i == 0 || i == po.size() - 1) {
                po.get(i).distance = (float) Double.MAX_VALUE;
            } else {
                po.get(i).distance = (float) (Math.abs((po.get(i - 1).fitness1 - po.get(i + 1).fitness1) / fitness1sum)
                        + Math.abs(po.get(i - 1).fitness2 - po.get(i + 1).fitness2) / fitness2sum
                );
            }
        }

        // 对该rank根据distance大小进行排序
        po.sort((Individual o1, Individual o2) -> {
            // TODO Auto-generated method stub
//			int i = o1.distance > o2.distance ? -1 : 1;
//			return i;
            if (o1.distance > o2.distance) return -1;
            else if (o1.distance < o2.distance) return 1;
            return 0;
        });
        // 将所有选中的个体放入新的ArrayList中
        ArrayList<Individual> newpo = new ArrayList<Individual>();
        for (int i = 0; i < index; i++) {
            newpo.addAll(rank.get(i));
        }
        // 同上
        for (int i = 0; i < num; i++) {
            newpo.add(po.get(i));
        }
        //System.out.println("放入"+num+"个");
        // 创建一个种群，将刚才筛选出的所有个体作为该种群的组成部分
        Population pp = new Population();
        for (int i = 0; i < Population.MAXSIZE; i++) {
            pp.map.put(i, newpo.get(i));
        }
        return pp;// 返回该种群
    }

}
