package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest{

    private static class comparator1 implements Comparator<String>{
        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    }

    private static class comparator2 implements Comparator<Integer>{
        @Override
        public int compare(Integer i1, Integer i2) {
            return i1 - i2;
        }
    }

    @Test
    public void comparator1Test(){
        MaxArrayDeque<String> A = new MaxArrayDeque<>(new comparator1());
        A.addFirst("I love you");
        A.addLast("Me too");
        A.addFirst("OK");
        assertEquals("I love you", A.max());
    }

    @Test
    public void comparator2Test(){
        MaxArrayDeque<Integer> B = new MaxArrayDeque<>(new comparator2());
        B.addFirst(5);
        B.addLast(3);
        B.addFirst(2);
        assertEquals(5, (int) B.max());
    }
}
