package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import static gitlet.Utils.*;

import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable, Dumpable{
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

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
    /* TODO: fill in the rest of this class. */

    public String getHash(){
        return sha1(this.message, this.timestamp, this.parent, this.sideParent, this.blobs.toString());
    }

    public static Commit fromFile(String name) {
        // TODO (hint: look at the Utils file)
        File commitName = join(COMMITS, name);
        Commit thisCommit = readObject(commitName, Commit.class);
        return thisCommit;
    }

    public TreeMap<String, String> getBlobsCopy(){
        return new TreeMap<>(this.blobs);
    }

    public boolean blobExist(String filename){
        return blobs.containsKey(filename);
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

    public String getParentName(){
        return parent;
    }

    public Boolean parentExist(){
        return (!parent.equals(""));
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
                            "\n", hash, parent.substring(0, 6), sideParent.substring(0, 6), timestamp, message
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

