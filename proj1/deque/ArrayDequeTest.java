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
        ArrayDeque<Integer> buggyDeque = new ArrayDeque<>();
        LinkedListDeque<Integer> goodDeque = new LinkedListDeque<>();

        int N = 50000;
        for (
                int i = 0;
                i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);

            if (operationNumber == 0) {
                // 0: addLast，无返回值，同步执行
                int randVal = StdRandom.uniform(0, 100);
                buggyDeque.addLast(randVal);
                goodDeque.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
                System.out.println("head:"+buggyDeque.head+"tail:"+buggyDeque.tail);


            }
            else if (operationNumber == 4) {
                // 0: addFirst，无返回值，同步执行
                int randVal = StdRandom.uniform(0, 100);
                buggyDeque.addFirst(randVal);
                goodDeque.addFirst(randVal);
                System.out.println("addFirst(" + randVal + ")");
                System.out.println("head:"+buggyDeque.head+"tail:"+buggyDeque.tail);
            }

            else if (operationNumber == 5) {
                // 3: removeFirst，非空才执行，对比返回值
                if (buggyDeque.size() > 0) {
                    int bugRemoved = buggyDeque.removeFirst();
                    int goodRemoved = goodDeque.removeFirst();
                    System.out.println("removeFirst:" + bugRemoved);
                    System.out.println("head:"+buggyDeque.head+"tail:"+buggyDeque.tail);
                    assertEquals("bug in removeFirst", goodRemoved, bugRemoved);
                } else {
                    System.out.println("removeFirst skipped (list empty)");
                }
            }

            else if (operationNumber == 1) {
                    // 1: size，有返回值，对比结果
                    int bugSize = buggyDeque.size();
                    int goodSize = goodDeque.size();
                    System.out.println("size: bug=" + bugSize);
                    System.out.println("head:"+buggyDeque.head+"tail:"+buggyDeque.tail);
                    assertEquals("bug in size", goodSize,bugSize);
                }
            else if (operationNumber == 2) {
                    // 2: getLast，非空才执行，对比返回值
                    if (buggyDeque.size() > 0) {
                        int bugLast = buggyDeque.get(buggyDeque.size() - 1);
                        int goodLast = goodDeque.get(goodDeque.size() - 1);
                        System.out.println("getLast=" + bugLast);
                        System.out.println("head:"+buggyDeque.head+"tail:"+buggyDeque.tail);
                        assertEquals("bug in getLast", goodLast, bugLast);
                    } else {
                        System.out.println("getLast skipped (list empty)");
                    }
                } else if (operationNumber == 3) {
                    // 3: removeLast，非空才执行，对比返回值
                    if (buggyDeque.size() > 0) {
                        int bugRemoved = buggyDeque.removeLast();
                        int goodRemoved = goodDeque.removeLast();
                        System.out.println("removeLast:" + bugRemoved);
                        System.out.println("head:"+buggyDeque.head+"tail:"+buggyDeque.tail);
                        assertEquals("bug in removeLast", goodRemoved, bugRemoved);
                    } else {
                        System.out.println("removeLast skipped (list empty)");
                    }
                }
            }
        }
    }

