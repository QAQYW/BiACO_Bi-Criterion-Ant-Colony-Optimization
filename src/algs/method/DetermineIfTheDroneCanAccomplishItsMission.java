package algs.method;

import algs.Resource.Map;
import algs.Resource.UAV;

import java.util.ArrayList;
import java.util.HashSet;

/*
 * �ж����˻��Ƿ��ܹ���ɷָ���������
 */
public class DetermineIfTheDroneCanAccomplishItsMission {
	public static boolean determineIfTheDroneCanAccomplishItsMission(UAV u, Map map) throws CloneNotSupportedException {
		//���������������
		ArrayList<Integer> cunchu = new ArrayList();
//		System.out.println(u.nodeSequence+"determineIfTheDroneCanAccomplishItsMission");
		cunchu.addAll(u.nodeSequence);
		HashSet<Integer> hs2 = new HashSet();
		hs2.addAll(u.nodeSet);
		//�鿴�������ĸ��ڵ��½�����
		hs2.removeAll(cunchu);
		int p = -1;
		//��������£�hs2����ֻ����һ���ڵ�//����exchange4��������ֶ���ڵ�

		for (int x : hs2) {
			p = x;
		}
		//���û�нڵ���ô�ͳ�������//�����exchange4����û�нڵ���û�������
		if (hs2.size() < 0) {
			Calfitness2.calfitness(map, u);
			return true;
		}

		ArrayList<?> arr;
		//���ֻ��һ��Ԫ�أ�ֱ���������
		if (u.nodeSet.size() == 1) {
			u.nodeSequence.clear();
			u.nodeSequence.addAll(u.nodeSet);
			u.nodeSequence.addAll(u.nodeSet);

			return (Calfitness2.calfitness(map, u) != null);
		}


		//����Ǹ�������Ľ�ֹ����ǰ������������˳��
		if (DetermineTheTraversalOrderInOrderOfTheDeadline.determineTheTraversalOrderInOrderOfTheDeadline(u, map)) {
			if (u.nodeSequence.size() != u.nodeSet.size() * 2) {
				System.out.println("judge+1");
			}
			return true;

		}
		//���̶�����������˳��//����ʱ��;���
		if (ThePathGeneratedByARouletteWheelOfTimeAndDistance.thePathGeneratedByARouletteWheelOfTimeAndDistance(u, map)) {
			if (u.nodeSequence.size() != u.nodeSet.size() * 2) {
				System.out.println("judge+2");
			}
			return true;
		}


		u.nodeSequence.clear();
		u.nodeSequence.addAll(cunchu);
		//��������
		if (hs2.size() == 1)
			if (InsertIntoArrange.insertIntoArrange(u, map, p)) {
				if (u.nodeSequence.size() != u.nodeSet.size() * 2) {
					System.out.println("judge+3");
				}
				return true;
			}

		if (hs2.size() > 1)
			if (InsertIntoArrange.insertIntoArrange(u, map, hs2)) {
				if (u.nodeSequence.size() != u.nodeSet.size() * 2) {
					System.out.println("judge+4");
				}
				return true;
			}
		return false;
	}
}
