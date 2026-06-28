package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */

public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
    public void TestBuggyAList(){
        // 同时创建正确列表、待测试bug列表
        AListNoResizing<Integer> goodList = new AListNoResizing<>();
        BuggyAList<Integer> buggyList = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // 0: addLast，无返回值，同步执行
                int randVal = StdRandom.uniform(0, 100);
                goodList.addLast(randVal);
                buggyList.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // 1: size，有返回值，对比结果
                int goodSize = goodList.size();
                int bugSize = buggyList.size();
                System.out.println("size: good=" + goodSize + ", bug=" + bugSize);
                assertEquals(goodSize, bugSize);
            } else if (operationNumber == 2) {
                // 2: getLast，非空才执行，对比返回值
                if (goodList.size() > 0) {
                    int goodLast = goodList.getLast();
                    int bugLast = buggyList.getLast();
                    System.out.println("getLast: good=" + goodLast + ", bug=" + bugLast);
                    assertEquals(goodLast, bugLast);
                } else {
                    System.out.println("getLast skipped (list empty)");
                }
            } else if (operationNumber == 3) {
                // 3: removeLast，非空才执行，对比返回值
                if (goodList.size() > 0) {
                    int goodRemoved = goodList.removeLast();
                    int bugRemoved = buggyList.removeLast();
                    System.out.println("removeLast: good=" + goodRemoved + ", bug=" + bugRemoved);
                    assertEquals(goodRemoved, bugRemoved);
                } else {
                    System.out.println("removeLast skipped (list empty)");
                }
            }
        }
    }
}
