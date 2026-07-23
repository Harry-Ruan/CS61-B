package gitlet;

import java.io.File;
import static gitlet.Utils.*;
import static gitlet.Utils.readContents;

import java.util.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    static final File GITLET_DIR = join(CWD, ".gitlet");
    /** staging area. */
    static final File STAGE = join(CWD, ".gitlet", "stage");
    /** head */
    static final File HEAD = join(CWD,".gitlet", "HEAD");
    /** commits */
    static final File COMMITS = join(CWD, ".gitlet", "commits");
    /** blobs committed */
    static final File BLOBS = join(CWD, ".gitlet", "blobs");
    /** refs */
    static final File REFS = join(CWD, ".gitlet", "refs");
    /** branches */
    static final File BRANCHES = join(CWD, ".gitlet", "refs", "branches");
    /** TODO: fill in the rest of this class. */

    public void init(){
        /** check if initialized */
        if (!initialized()){
            GITLET_DIR.mkdir();
        }
        else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
        if (!STAGE.exists()){
            writeObject(STAGE, new Stage());
        }
        if (!COMMITS.exists()){
            COMMITS.mkdir();
        }
        if (!BLOBS.exists()){
            BLOBS.mkdir();
        }
        if (!REFS.exists()){
            REFS.mkdir();
        }
        if (!BRANCHES.exists()){
            BRANCHES.mkdir();
        }
        if (!HEAD.exists()){
            saveHead(initialCommit(), "master");
        }
    }

    private String initialCommit(){
        Commit init = new Commit("initial commit", "Thu Jan 1 00:00:00 1970 +0800", "", "", new TreeMap<>());
        String hash = init.getHash();
        init.saveCommit(hash);
        return hash;
    }

    public boolean initialized(){
        return GITLET_DIR.exists();
    }

    public void add(String filename) {
        /** check if initialized */
        if (!initialized()){
            System.out.println("haven't initialize");
            return;
        }
        Stage stage = getStage();
        Commit headCommit = getHead();
        File workFile = join(CWD, filename);
        if (!workFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        String workHash = sha1(readContents(workFile));

        if (workHash.equals(headCommit.getBlobHash(filename))) {
            stage.removeAdded(filename);
            stage.removeRemoved(filename);
        } else {
            /* write */
            stage.add(filename, workHash);
            File blobFile = join(BLOBS, workHash);
            if (!blobFile.exists()) {
                writeContents(blobFile, readContents(workFile));
            }
        }

        stage.save();
    }

    public void commit(String message){
        Stage stage = getStage();
        /** check if staged */
        if (!stage.hasStaged()){
            System.out.println("No changes added to the commit.");
        }
        /** check message */
        if (message.equals("")){
            System.out.println("Please enter a commit message.");
            return;
        }
        Commit headCommit = getHead();
        TreeMap<String, String> blobs = headCommit.getBlobsCopy();
        Map<String, String> added = stage.getAdded();
        Set<String> removed = stage.getRemoved();

        Commit newCommit = new Commit(message, getTime(), getHeadHash(), "", blobs);

        /** for every files in added, add then to blobs */
        for (Map.Entry<String, String> addedEntry : added.entrySet()) {
            /** if existed, override. if not, write new */
            newCommit.addBlob(addedEntry.getKey(), addedEntry.getValue());
        }
        /** for every files in removed, remove then from blobs to untrack */
        for (String removedName : removed) {
            /** if existed, remove. if not, continue */
            newCommit.removeBlob(removedName);
        }
        newCommit.saveCommit(newCommit.getHash());
        saveHead(newCommit.getHash(), getHeadBranch());
        stage.clear();
        stage.save();
    }

    public void rm(String filename){
        Stage stage = getStage();
        /** check if tracked */
        if (!fileTracked(filename)){
            System.out.println("No reason to remove the file.");
            return;
        }
        /** unstaged from added */
        stage.removeAdded(filename);
        /** add it to removed if headcommit contains it */
        if (getHead().blobExist(filename)){
            stage.addRemoved(filename);
        }
        /** remove from working directory */
        removeWorkingFile(filename);
        /** save stage */
        stage.save();
    }

    public void removeWorkingFile(String filename){
        join(CWD, filename).delete();
    }

    public void log(){
        Commit curr = getHead();
        String currName = getHeadHash();
        while(!currName.equals("")){
            System.out.print(curr.toString(currName));
            currName = curr.getParentName();
            if (!currName.equals("")){
                curr = curr.getParent();
            }
        }
    }

    public void global_log(){
        List<String> plainFiles = plainFilenamesIn(COMMITS);
        for (String fileName : plainFiles){
            Commit commit = readObject(join(COMMITS, fileName), Commit.class);
            System.out.print(commit.toString(fileName));
        }
    }

    public void find(String message){
        List<String> plainFiles = plainFilenamesIn(COMMITS);
        Boolean found = false;
        for (String commitHash : plainFiles){
            Commit commit = readObject(join(COMMITS, commitHash), Commit.class);
            if (commit.getMessage().equals(message)){
                System.out.print(String.format("commit %s", commit.getHash()));
                found = true;
            }
        }
        if (!found){
            System.out.println("Found no commit with that message.");
        }
    }

    public void status(){
        printCommon();
        printUnstaged();
        printUntracked();
    }

    private void printCommon(){
        List<String> branches = plainFilenamesIn(BRANCHES);
        String headBranch = getHeadBranch();
        System.out.println("=== Branches ===");
        for (String branch : branches){
            if (branch.equals(headBranch)){
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println("");
        Stage stage = getStage();
        List<String> added = new ArrayList<>(stage.getAdded().keySet());
        List<String> removed = new ArrayList<>(stage.getRemoved()) ;
        Collections.sort(added);
        Collections.sort(removed);
        System.out.println("=== Staged Files ===");
        for (String fileAdded : added){
            System.out.println(fileAdded);
        }
        System.out.println("");
        System.out.println("=== Removed Files ===");
        for (String fileRemoved : removed){
            System.out.println(fileRemoved);
        }
        System.out.println("");
    }

    private void printUnstaged(){
        Commit headCommit = getHead();
        Stage stage = getStage();
        Set<String> allFiles = new HashSet<>();
        Map<String, String> unstagedConditions = new HashMap<>();
        List<String> workFiles = plainFilenamesIn(CWD);
        Set<String> commitFiles = headCommit.getBlobsCopy().keySet();
        Set<String> addedFiles = stage.getAdded().keySet();
        Set<String> removedFiles = stage.getRemoved();
        allFiles.addAll(workFiles);
        allFiles.addAll(commitFiles);
        allFiles.addAll(addedFiles);
        allFiles.addAll(removedFiles);
        for (String file : allFiles){
            /** Tracked in the current commit, changed in the working directory, but not staged */
            if (workFiles.contains(file) && commitFiles.contains(file) && !headCommit.getBlobHash(file).equals(sha1(readContents(join(CWD, file)))) && !addedFiles.contains(file)){
                unstagedConditions.put(file, " (modified)");
            }
            /** Staged for addition, but with different contents than in the working directory; */
            else if(workFiles.contains(file) && addedFiles.contains(file) && !stage.getAdded().get(file).equals(sha1(readContents(join(CWD, file))))){
                unstagedConditions.put(file, " (modified)");
            }
            /** Staged for addition, but deleted in the working directory */
            else if (addedFiles.contains(file) && stage.getAdded().containsKey(file) && !workFiles.contains(file)){
                unstagedConditions.put(file, " (deleted)");
            }
            /** Not staged for removal, but tracked in the current commit and deleted from the working directory. */
            else if (!removedFiles.contains(file) && commitFiles.contains(file) && !workFiles.contains(file)){
                unstagedConditions.put(file, " (deleted)");
            }
        }
        List<String> unstagedFiles = new ArrayList<>(unstagedConditions.keySet());
        Collections.sort(unstagedFiles);
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String unstagedFile : unstagedFiles){
            System.out.println(unstagedFile + unstagedConditions.get(unstagedFile));
        }
        System.out.println("");
    }

    private void printUntracked(){
        /**  files present in the working directory but neither staged for addition nor tracked */
        Commit headCommit = getHead();
        Stage stage = getStage();
        Set<String> commitFiles = headCommit.getBlobsCopy().keySet();
        Set<String> addedFiles = stage.getAdded().keySet();
        List<String> workFiles = plainFilenamesIn(CWD);
        System.out.println("=== Untracked Files ===");
        for (String workFile : workFiles){
            if (!commitFiles.contains(workFile) && !addedFiles.contains(workFile)){
                System.out.println(workFile);
            }
        }
        System.out.println("");
    }

    public void checkoutFile(String commitID, String targetFileName){
        if (!join(COMMITS, commitID).exists()){
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit targetCommit = getCommit(commitID);
        if (!targetCommit.blobExist(targetFileName)){
            System.out.println("File does not exist in that commit.");
            return;
        }
        String targetHash = targetCommit.getBlobHash(targetFileName);
        writeContents(join(CWD, targetFileName), readContents(join(BLOBS, targetHash)));
    }

    public void checkoutHeadFile(String targetFile){
        checkoutFile(getHeadHash(), targetFile);
    }

    public void checkoutBranch(String branchName){
        /** If no branch with that name exists */
        if (!join(BRANCHES, branchName).exists()){
            System.out.println("No such branch exists.");
            return;
        }
        /**  If that branch is the current branch */
        if (branchName.equals(readContentsAsString(HEAD))){
            System.out.println("No need to checkout the current branch.");
            return;
        }
        checkoutCommit(readContentsAsString(join(BRANCHES, branchName)));
        /** at the end of this command, the given branch will now be considered the current branch (HEAD) */
        writeContents(HEAD, branchName);
    }

    private void checkoutCommit(String commitID){
        Stage stage = getStage();
        Commit currHead = getHead();
        Map<String, String> currBlobs = currHead.getBlobsCopy();
        Commit targetCommit = getCommit(commitID);
        Map<String, String> targetBlobs = targetCommit.getBlobsCopy();
        /** if a working file is untracked in the current branch and would be overwritten by the checkout */
        for (String fileToWrite : targetBlobs.keySet()){
            if (plainFilenamesIn(CWD).contains(fileToWrite) && !currBlobs.containsKey(fileToWrite)){
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        /** Takes all files in the commit at the head of the given branch, and puts them in the working directory, overwriting the versions of the files that are already there if they exist. */
        for (Map.Entry<String, String> blobEntry : targetBlobs.entrySet()){
            writeContents(join(CWD, blobEntry.getValue()), readContents(join(BLOBS, blobEntry.getValue())));
        }
        /** Any files that are tracked in the current branch but are not present in the checked-out branch are deleted. */
        for (String currBlob : currBlobs.keySet()){
            if (!targetBlobs.containsKey(currBlob)){
                removeWorkingFile(currBlob);
            }
        }
        /** The staging area is cleared, unless the checked-out branch is the current branch */
        stage.clear();
        stage.save();
    }

    private String AbbInterpreter(String commitAbbID){
        for (String commit : plainFilenamesIn(COMMITS)) {
            if (commit.startsWith(commitAbbID)) {
                return commit;
            }
        }
        System.out.println("No commit with that id exists.");
        return "not found";
    }

    public void branch(String branchName){
        if (join(BRANCHES, branchName).exists()){
            System.out.println("A branch with that name already exists.");
            return;
        }
        writeContents(join(BRANCHES, branchName), getHeadHash());
    }

    public void rm_branch(String branchName){
        if (!join(BRANCHES, branchName).exists()){
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (getHeadBranch().equals(branchName)){
            System.out.println("Cannot remove the current branch.");
            return;
        }
        join(BRANCHES, branchName).delete();
    }

    public void reset(String commitAbbID){
        String commitID = AbbInterpreter(commitAbbID.substring(0, 6));
        if (commitID.equals("not found")){
            return;
        }
        checkoutCommit(commitID);
        writeContents(join(BRANCHES, getHeadBranch()), commitID);
    }

    public void merge(String branchName){
        /** check if any staged */
        if (getStage().hasStaged()){
            System.out.println("You have uncommitted changes.");
        }
        /** check branch exists */
        if (!join(BRANCHES, branchName).exists()){
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(getHeadBranch())){
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        /** get both commits */
        Commit currHead = getCommit(getHeadHash());
        Map<String, String> currBlobs = currHead.getBlobsCopy();
        String branchHeadHash = readContentsAsString(join(BRANCHES, branchName));
        Commit branchHead = getCommit(branchHeadHash);
        Map<String, String> branchBlobs = branchHead.getBlobsCopy();
        /** get common ancestor */
        Commit ancestor = getCommonAncestor(currHead, branchHead);
        Map<String, String> ancestorBlobs = ancestor.getBlobsCopy();
        /** If the split point is the same commit as the given branch -> do nothing*/
        if (branchHead.equals(ancestor)){
            System.out.println("Given branch is an ancestor of the current branch.");
        }
        /** If the split point is the current branch -> checkout */
        if (currHead.equals(ancestor)){
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
        }
        for (Map.Entry<String, String> branchBlob : branchBlobs.entrySet()){
            String branchBlobName = branchBlob.getKey();
            String branchBlobHash = branchBlob.getValue();
            /** 1. modified in the branch but not in the current -> checkout files */
            if (ancestorBlobs.containsKey(branchBlobName) && !branchBlobHash.equals(ancestorBlobs.get(branchBlobName)) && ancestorBlobs.get(branchBlobName).equals(currBlobs.get(branchBlobName))){
                checkoutFile(branchHeadHash.substring(0, 6), branchBlobName);
            }
            /** 2. current changed but not in branch -> stay the same */
            /** 3. changes in the sam way -> stay the same */
            /** 4. new file in current -> stay the same */
            /** 5. new file in branch -> checkout */
            else if (!ancestorBlobs.containsKey(branchBlobName) && !currBlobs.containsKey(branchBlobName)){
                checkoutFile(branchHeadHash.substring(0, 6), branchBlobName);
            }
            /** 7.removed in current and no change in branch -> stay the same */
            /** 8.both changed (not add or remove) -> merge */
            else if (ancestorBlobs.containsKey(branchBlobName) && currBlobs.containsKey(branchBlobName) && !ancestorBlobs.get(branchBlobName).equals(branchBlobHash) && !ancestorBlobs.get(branchBlobName).equals(currBlobs.get(branchBlobName))){
                conflictMerge(currBlobs.get(branchBlobName), branchBlobHash, branchBlobName);
            }
            /** 9.curr and branch both add same new files -> merge */
            else if (!ancestorBlobs.containsKey(branchBlobName) && currBlobs.containsKey(branchBlobName) && !currBlobs.get(branchBlobName).equals(branchBlobHash)){
                conflictMerge(currBlobs.get(branchBlobName), branchBlobHash, branchBlobName);
            }
            add(branchBlobName);
        }
        for (Map.Entry<String, String> ancestorBlob : ancestorBlobs.entrySet()){
            String ancestorBlobName = ancestorBlob.getKey();
            /** 6.removed in given and no change in current -> remove current */
            if (!branchBlobs.containsKey(ancestorBlobName) && currBlobs.containsKey(ancestorBlobName) && ancestorBlobs.get(ancestorBlobName).equals(currBlobs.get(ancestorBlobName))){
                rm(ancestorBlobName);
            }
        }
        /** get stage and check if staged  */
        Stage stage = getStage();
        if (!stage.hasStaged()){
            System.out.println("No changes added to the commit.");
        }
        Map<String, String> added = stage.getAdded();
        Set<String> removed = stage.getRemoved();
        Commit newCommit = new Commit(String.format("Merged %s into %s", branchName, getHeadBranch()), new Date().toString(), getHeadHash(), branchHeadHash, (TreeMap<String, String>) currBlobs);
        /** for every files in added, add then to blobs */
        for (Map.Entry<String, String> addedEntry : added.entrySet()) {
            /** if existed, override. if not, write new */
            newCommit.addBlob(addedEntry.getKey(), addedEntry.getValue());
        }
        /** for every files in removed, remove then from blobs to untrack */
        for (String removedName : removed) {
            /** if existed, remove. if not, continue */
            newCommit.removeBlob(removedName);
        }
        newCommit.saveCommit(newCommit.getHash());
        saveHead(newCommit.getHash(), getHeadBranch());
        stage.clear();
        stage.save();
    }

    private void conflictMerge(String currBlobHash, String branchBlobHash, String commonName) {
        String currBlob = readContentsAsString(join(BLOBS, currBlobHash));
        String branchBlob = readContentsAsString(join(BLOBS, branchBlobHash));
        writeContents(join(CWD, commonName), "<<<<<<< HEAD\n", currBlob, "\n", "=======\n", branchBlob, "\n", ">>>>>>>");
        System.out.println("Encountered a merge conflict.");
    }

    private Commit getCommonAncestor(Commit commit1, Commit commit2){
        HashSet<String> ancestors1 = new HashSet<>();
        Commit cursor1 = commit1;
        Commit cursor2 = commit2;
        while(true){
            ancestors1.add(cursor1.getHash());
            if (!cursor1.parentExist()){
                break;
            }
            cursor1 = cursor1.getParent();
        }
        while(true){
            if (ancestors1.contains(cursor2.getHash())){
                return cursor2;
            }
            if (!cursor2.parentExist()){
                return null;
            }
            cursor2 = cursor2.getParent();
        }
    }

    public boolean fileTracked(String filename){
        return getStage().fileStaged(filename) || getHead().blobExist(filename);
    }

    private Commit getCommit(String commitID){
        return readObject(join(COMMITS, commitID), Commit.class);
    }

    private Stage getStage(){
        return readObject(STAGE, Stage.class);
    }

    private void saveHead(String headHash, String branch){
        writeContents(HEAD, branch);
        writeContents(join(BRANCHES, branch), headHash);
    }

    private Commit getHead(){
        return getCommit(getHeadHash());
    }

    private String getHeadHash(){
        return readContentsAsString(join(BRANCHES, getHeadBranch()));
    }

    private String getHeadBranch(){
        return readContentsAsString(HEAD);
    }

    private String getTime(){
        java.time.ZonedDateTime now = java.time.ZonedDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z", java.util.Locale.US);
        return now.format(formatter);
    }
}
