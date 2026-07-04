package deque;

import afu.org.checkerframework.checker.igj.qual.I;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque{
    private Comparator<T> myComparator;

    public MaxArrayDeque(Comparator<T> c){
        super();
        myComparator = c;
    }


    public T max(Comparator<T> c){
        T maximum = (T) get(0);
        if (isEmpty()){
            return null;
        }
        for (int i = 1; i < size(); i++){
            T I = (T) get(i);
            if (c.compare(maximum, I) < 0){
                maximum = I;
            }
        }
        return maximum;
    }

    public T max(){
        return max(myComparator);
    }
}
