package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void ArrayDequeTest()
    {
        // 同时创建正确列表、待测试bug列表
        ArrayDeque<Integer> buggyDeque2 = new ArrayDeque<>();

        int N = 50000;
        for (
                int i = 0;
                i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // 0: addLast，无返回值，同步执行
                int randVal = StdRandom.uniform(0, 100);
                buggyDeque2.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");

            }
            else if (operationNumber == 4) {
                // 0: addLast，无返回值，同步执行
                int randVal = StdRandom.uniform(0, 100);
                buggyDeque2.addFirst(randVal);
                System.out.println("addFirst(" + randVal + ")");
            }

            else if (operationNumber == 5) {
                // 3: removeLast，非空才执行，对比返回值
                if (buggyDeque2.size() > 0) {
                    int bugRemoved = buggyDeque2.removeFirst();
                    System.out.println("removeFirst:" + bugRemoved);
                } else {
                    System.out.println("removeFirst skipped (list empty)");
                }
            }

            else if (operationNumber == 1) {
                    // 1: size，有返回值，对比结果
                    int bugSize = buggyDeque2.size();
                    System.out.println("size: bug=" + bugSize);
                }
            else if (operationNumber == 2) {
                    // 2: getLast，非空才执行，对比返回值
                    if (buggyDeque2.size() > 0) {
                        int bugLast = buggyDeque2.get(buggyDeque2.size() - 1);
                        System.out.println("last=" + bugLast);
                    } else {
                        System.out.println("getLast skipped (list empty)");
                    }
                } else if (operationNumber == 3) {
                    // 3: removeLast，非空才执行，对比返回值
                    if (buggyDeque2.size() > 0) {
                        int bugRemoved = buggyDeque2.removeLast();
                        System.out.println("removeLast:" + bugRemoved);
                    } else {
                        System.out.println("removeLast skipped (list empty)");
                    }
                }
            }
        }
    }

