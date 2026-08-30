package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Utils.*;

import java.util.*;

/** A persistent snapshot of tracked filenames and their content-addressed blobs. */
public class Commit implements Serializable, Dumpable{
    static final File CWD = new File(System.getProperty("user.dir"));
    static final File COMMITS = join(CWD, ".gitlet", "commits");
    /** The message of this Commit. */
    private String message;
    /** timestamp */
    private String timestamp;
    /** parent reference */
    private String parent;
    /** second parent */
    private String sideParent;
    /** mapping real name and sha1 */
    private TreeMap<String, String> blobs;


    Commit(String m, String t, String p, String s, TreeMap<String, String> b){
        message = m;
        timestamp = t;
        parent = p;
        sideParent = s;
        blobs = b;
    }
    public String getHash(){
        return sha1(this.message, this.timestamp, this.parent, this.sideParent, this.blobs.toString());
    }

    public static Commit fromFile(String name) {
        File commitName = join(COMMITS, name);
        Commit thisCommit = readObject(commitName, Commit.class);
        return thisCommit;
    }

    public TreeMap<String, String> getBlobsCopy(){
        return new TreeMap<>(this.blobs);
    }

    public boolean blobExist(String filename){
        Map<String, String> checkBlobs = blobs;
        return checkBlobs.containsKey(filename);
    }

    public String getBlobHash(String filename){
        return blobs.getOrDefault(filename, null);
    }

    public void addBlob(String filename, String hash){
        blobs.put(filename, hash);
    }

    public void removeBlob(String filename){
        if (blobs.containsKey(filename)){
            blobs.remove(filename);
        }
    }

    public String getMessage(){
        return message;
    }

    public Commit getParent(){
        return fromFile(parent);
    }

    public List<String> getParents(){return List.of(parent, sideParent);}

    public String getParentName(){
        return parent;
    }

    public Boolean parentExist(){
        return (!parent.equals("") || !sideParent.equals(""));
    }

    public String toString(String hash){
        /* if no merge */
        if (sideParent == ""){
            return String.format(
                    "===\n" +
                            "commit %s\n" +
                            "Date: %s\n" +
                            "%s\n"+
                            "\n", hash, timestamp, message

            );
        }
        /* if merged */
        else {
           return String.format(
                   "===\n" +
                           "commit %s\n" +
                           "Merge: %s %s\n" +
                           "Date: %s\n" +
                           "%s\n" +
                            "\n", hash, parent.substring(0, 7), sideParent.substring(0, 7), timestamp, message
           );
        }
    }

    public void saveCommit(String hash){
        writeObject(join(COMMITS, hash), this);
    }

    @Override
    public void dump() {
        System.out.printf("size: %d%nparent: %s%n", blobs.size(), parent);
    }
}
