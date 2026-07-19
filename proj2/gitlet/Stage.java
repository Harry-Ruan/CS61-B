package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import static gitlet.Utils.*;


public class Stage implements Serializable, Dumpable{
    static final File CWD = new File(System.getProperty("user.dir"));
    static final File STAGE = join(CWD, ".gitlet", "stage");
    private TreeMap<String, String> added = new TreeMap<>();
    private HashSet<String> removed = new HashSet<>();


    public void add(String filename, String hash){
        removed.remove(filename);
        added.put(filename, hash);
    }

    public String getStagingHash(String filename){
        return added.getOrDefault(filename, null);
    }

    public void removeAdded(String filename){
        added.remove(filename);
    }

    public void addRemoved(String filename){
        removed.add(filename);
    }

    public boolean hasStaged(){
        return !added.isEmpty() || !removed.isEmpty();
    }

    public boolean fileStaged(String filename){
        return added.containsKey(filename) || removed.contains(filename);
    }

    public Map<String, String> getAdded(){
        return Collections.unmodifiableMap(added);
    }

    public Set<String> getRemoved(){
        return Collections.unmodifiableSet(removed);
    }

    public void clear(){
        added = new TreeMap<>();
        removed = new HashSet<>();
    }

    public void save(){
        writeObject(STAGE, this);
    }

    @Override
    public void dump() {

    }
}
