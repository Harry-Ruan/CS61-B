package deque;

import afu.org.checkerframework.checker.igj.qual.I;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque{
    private Comparator<Item> myComparator;

    public MaxArrayDeque(Comparator<Item> c){
        super();
        myComparator = c;
    }


    public Item max(Comparator<Item> c){
        Item maximum = (Item) get(0);
        if (isEmpty()){
            return null;
        }
        for (int i = 1; i < size(); i++){
            Item I = (Item) get(i);
            if (c.compare(maximum, I) < 0){
                maximum = I;
            }
        }
        return maximum;
    }

    public Item max(){
        return max(myComparator);
    }
}
