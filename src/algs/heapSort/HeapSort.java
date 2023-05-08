package algs.heapSort;

import algs.Resource.UAV;

import java.util.ArrayList;

public class HeapSort {//取出最小的n个数

    public static void heapSort(ArrayList<UAV> array, int n) {
        for (int i = array.size() / 2; i >= 0; --i)
            adjust(array, i, array.size());
        UAV tmp;
        for (int i = array.size() - 1; i >= array.size() - n - 1; --i) {
            tmp = array.get(i);
            array.set(i, array.get(0));
            array.set(0, tmp);
            adjust(array, 0, i);
        }

    }

    public static void heapSort1(ArrayList<UAV> array, int n) {
        for (int i = array.size() / 2; i >= 0; --i)
            adjust1(array, i, array.size());
        UAV tmp;
        for (int i = array.size() - 1; i >= array.size() - n - 1; --i) {
            tmp = array.get(i);
            array.set(i, array.get(0));
            array.set(0, tmp);
            adjust1(array, 0, i);
        }

    }


    private static void adjust(ArrayList<UAV> array, int i, int length) {
        int child;
        UAV temp;
        for (temp = array.get(i); 2 * i + 1 < length; i = child) {
            child = 2 * i + 1;

            if (child < length - 1 && array.get(child + 1).timeToCompleteAllTasks > array.get(child).timeToCompleteAllTasks)
                ++child;
            if (temp.timeToCompleteAllTasks < array.get(child).timeToCompleteAllTasks) {
                array.set(i, array.get(child));
                array.set(child, temp);
            } else
                break;
        }
    }

    private static void adjust1(ArrayList<UAV> array, int i, int length) {
        int child;
        UAV temp;
        for (temp = array.get(i); 2 * i + 1 < length; i = child) {
            child = 2 * i + 1;

            if (child < length - 1 && array.get(child + 1).timeToCompleteAllTasks / array.get(child + 1).nodeSet.size() > array.get(child).timeToCompleteAllTasks / array.get(child).nodeSet.size())
                ++child;
            if (temp.timeToCompleteAllTasks / temp.nodeSet.size() < array.get(child).timeToCompleteAllTasks / temp.nodeSet.size()) {
                array.set(i, array.get(child));
                array.set(child, temp);
            } else
                break;
        }
    }

}
