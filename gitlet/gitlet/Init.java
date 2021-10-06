package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

/** Main body of gitlet system.
 *  includes all methods required for functionality.
 *
 *  @author Jacob Aldrich
 */

public class Init implements Serializable {

    // Directories needed for gitlet
    protected static final File CWD = new File(System.getProperty("user.dir"));
    protected static final File GITLET = Utils.join(CWD, ".gitlet");
    protected static final File COMMITS = Utils.join(GITLET, "commits");
    protected static final File TRUEFILES = Utils.join(GITLET, "trueFiles");
    protected static final File STAGINGAREA = Utils.join(GITLET, "stagingArea");
    protected static final File REMOVALSTAGINGAREA = Utils.join(GITLET, "removalStagingArea");
    protected static final File POINTERS = Utils.join(GITLET, "pointers");
    protected static final File HEADPOINTER = Utils.join(POINTERS, "headPointer");
    protected static final File BLOBSMAPS = Utils.join(GITLET, "blobsMaps");
    protected static final File BRANCHES = Utils.join(GITLET, "branches");
    protected static File MASTER;

    protected static HashMap<String, Commit> branchMap;
    protected static HashMap<String, String> headBlobsMap = new HashMap<>();


    // Creates 5 directories. In main, creates initial commit.
    public void init() {
        createDirectory(GITLET);
        createDirectory(COMMITS);
        createDirectory(TRUEFILES);
        createDirectory(STAGINGAREA);
        createDirectory(REMOVALSTAGINGAREA);
        createDirectory(POINTERS);
        createDirectory(HEADPOINTER);
        createDirectory(BLOBSMAPS);
        createFile(BRANCHES);
        MASTER = Utils.join(HEADPOINTER, "master");
        createFile(MASTER);
        branchMap = new HashMap<>();
        Utils.writeObject(BRANCHES, branchMap);
    }

    public void commit(Commit c) {
        // If there is nothing in staging area and not initial commit, return.
        List<String> stagingAreaList = Utils.plainFilenamesIn(STAGINGAREA);
        List<String> removalStagingAreaList = Utils.plainFilenamesIn(REMOVALSTAGINGAREA);
        if (stagingAreaList.size() == 0 && c.parent != null && removalStagingAreaList.size() == 0) {
            System.out.println("No changes added to the commit");
            return;
        }

        // If parent is null, set blobsMap of commit.
        if (c.parent == null) {
            File bMap = Utils.join(BLOBSMAPS, c.hashValue);
            createFile(bMap);
            Utils.writeObject(bMap, headBlobsMap);
        } else {
            headBlobsMap = getHeadBlobsMap();
        }

        // Puts new commit in branches file.
        branchMap = Utils.readObject(BRANCHES, HashMap.class);
        branchMap.put(c.hashValue, c);
        Utils.writeObject(BRANCHES, branchMap);

        // Create file with name = hashValue in commits, make contents commit as serializable.
        File f = Utils.join(COMMITS, c.hashValue);
        createFile(f);
        Utils.writeObject(f, c);

        // Iterate through stagingArea and add files to blobsMap.
        // If equal, continue, else put in blobsMap.
        for (String h : stagingAreaList) {
            File add = Utils.join(STAGINGAREA, h);
            String fileHashCode = getFileHashValue(add);

            // Put file and hashcode into blobsMap.
            if (headBlobsMap.get(h) == null) {
                File q = Utils.join(TRUEFILES, fileHashCode);
                createFile(q);
                Utils.writeContents(q, Utils.readContentsAsString(add));
                headBlobsMap.put(h, fileHashCode);
                add.delete();
                continue;
            } else if (!headBlobsMap.get(h).equals(fileHashCode)) {
                // If blobsMap.get is not equal to hashCode, overwrite blobsMap.
                headBlobsMap.remove(h);
                File q = Utils.join(TRUEFILES, fileHashCode);
                createFile(q);
                Utils.writeContents(q, Utils.readContentsAsString(add));
                headBlobsMap.put(h, fileHashCode);
                add.delete();
                continue;
            } else {
                // If commit blobsMap of string is equal to the hashcode of the file, pass.
                add.delete();
                continue;
            }
        }

        // Removes files from blobsMap that are in removalStagingArea
        List<String> removalStagingAreaIterator = Utils.plainFilenamesIn(REMOVALSTAGINGAREA);
        for (String r : removalStagingAreaIterator) {
            File removal = Utils.join(REMOVALSTAGINGAREA, r);
            headBlobsMap.remove(r);
            removal.delete();
        }

        // Puts blobsMap in blobsMap directory with commit name. cblobsMap is from file blobsMaps.
        File blobsMapFile = Utils.join(BLOBSMAPS, c.hashValue);
        Utils.writeObject(blobsMapFile, headBlobsMap);

        // Set head equal to hashValue. Reset staging area.
        File head = getHeadPointerFile();
        Utils.writeContents(head, c.hashValue);
        STAGINGAREA.delete();
        createDirectory(STAGINGAREA);
        REMOVALSTAGINGAREA.delete();
        createDirectory(REMOVALSTAGINGAREA);
    }


    // Adds file to staging area.
    public void add(String name) {
        File originalFile = Utils.join(CWD, name);
        File newFile = Utils.join(STAGINGAREA, name);
        File removedFile = Utils.join(REMOVALSTAGINGAREA, name);
        headBlobsMap = getHeadBlobsMap();

        // If file is in removalStagingArea, restore it.
        if (removedFile.exists()) {
            if (!originalFile.exists()) {
                createFile(originalFile);
                String hashValue = headBlobsMap.get(name);
                if (hashValue != null) {
                    File f = Utils.join(TRUEFILES, hashValue);
                    Utils.writeContents(originalFile, Utils.readContentsAsString(f));
                }
            }
            removedFile.delete();
        }

        // If file not in working directory, return.
        if (!originalFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        // If file already in staging area AND same file, return
        String hashValue = getFileHashValue(originalFile);
        if (newFile.exists()) {
            String newFileHash = getFileHashValue(newFile);
            if (newFileHash.equals(hashValue)) {
                return;
            }
        }

        // If file already in current commit, remove from staging area and return.
        if (hashValue.equals(headBlobsMap.get(name))) {
            newFile.delete();
            return;
        }

        // Write contents of file to stagingArea. If file in removalStagingArea, take it out.
        createFile(newFile);
        Utils.writeContents(newFile, Utils.readContentsAsString(originalFile));
    }


    // Removes file from staging area if not in head commit.
    // If in head commit, adds file to removalStagingArea and deletes in next commit.
    public void remove(String name) {
        File originalFile = Utils.join(CWD, name);
        File stagingAreaFile = Utils.join(STAGINGAREA, name);
        File removalStagingAreaFile = Utils.join(REMOVALSTAGINGAREA, name);
        headBlobsMap = getHeadBlobsMap();

        // If file not in staging area or head commit, return.
        if (!stagingAreaFile.exists() && (headBlobsMap.get(name) == null)) {
            System.out.println("No reason to remove the file.");
        } else {
            // If file in blobsMap of head commit, delete file and add to removalstagingArea.
            if (headBlobsMap.get(name) != null) {
                createFile(removalStagingAreaFile);
                Utils.writeContents(removalStagingAreaFile, headBlobsMap.get(name));
                originalFile.delete();
            }
            stagingAreaFile.delete();
        }
    }


    // Create log from head commit, moving backwards.
    public void log() {
        branchMap = Utils.readObject(BRANCHES, HashMap.class);
        Commit newHead = getHeadCommit();
        while (newHead != null) {
            if (newHead.mergeParents != null) {
                logHelperWithMerge(newHead);
                newHead = branchMap.get(newHead.parent);
            } else {
                logHelper(newHead);
                newHead = branchMap.get(newHead.parent);
            }
        }
    }


    // Create log of every commit in commits.
    public static void globalLog() {
        List<String> files = Utils.plainFilenamesIn(COMMITS);
        for (String s : files) {
            File commit = Utils.join(COMMITS, s);
            Commit c = Utils.readObject(commit, Commit.class);
            if (c.mergeParents != null) {
                logHelperWithMerge(c);
            } else {
                logHelper(c);
            }
        }
    }


    // Finds commit with the same message.
    public void find(String message) {
        List<String> files = Utils.plainFilenamesIn(COMMITS);
        int foundCommits = 0;
        for (String s : files) {
            File commit = Utils.join(COMMITS, s);
            Commit c = Utils.readObject(commit, Commit.class);
            if (c.message.equals(message)) {
                System.out.println(c.hashValue);
                foundCommits += 1;
            }
        }

        // If no commits found, return error message.
        if (foundCommits == 0) {
            System.out.println("Found no commit with that message.");
            return;
        }
    }


    // Prints status of gitlet; Branches, Staged Files, and Removed Files.
    public void status() {
        // Print head branch with * in front, then others.
        System.out.println("=== Branches ===");
        System.out.println("*" + getHeadPointerString());
        List<String> pointerList = Utils.plainFilenamesIn(POINTERS);
        for (String i : pointerList) {
            if (i.equals("headPointer")) {
                continue;
            }
            System.out.println(i);
        }
        System.out.println();

        // Print files in stagingArea
        System.out.println("=== Staged Files ===");
        List<String> fileList = Utils.plainFilenamesIn(STAGINGAREA);
        for (String s : fileList) {
            System.out.println(s);
        }
        System.out.println();

        // Print files in removeStagingArea
        System.out.println("=== Removed Files ===");
        List<String> removalFileListStaging = Utils.plainFilenamesIn(REMOVALSTAGINGAREA);
        for (String s : removalFileListStaging) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        List<String> notStaged = showModifiedNotStaged();
        for (String s : notStaged) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        List<String> untrackedFiles = showUntrackedFiles();
        for (String s : untrackedFiles) {
            System.out.println(s);
        }
        System.out.println();
    }


    // Takes the version of the file as it exists in the head commit and puts it in the
    // working directory, overwriting the version of the file that’s already there if there is one.
    public void checkout1(String name) {
        // Get head commit, serialize file from trueFiles using name and blobsMap to find hashValue.
        headBlobsMap = getHeadBlobsMap();
        String hashValueFile = headBlobsMap.get(name);

        if (hashValueFile == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File fileName = Utils.join(TRUEFILES, hashValueFile);

        // Delete and create file in working directory, writing contents to file.
        File workingDirectoryFile = Utils.join(CWD, name);
        workingDirectoryFile.delete();
        createFile(workingDirectoryFile);
        Utils.writeContents(workingDirectoryFile, Utils.readContentsAsString(fileName));
    }


    // Takes the version of the file as it exists in the commit with the given id, and puts it in
    // working directory, overwriting the version of the file that’s already there if there is one.
    public void checkout2(String name, String id) {
        // If id is shortened, find long id.
        if (id.length() < 40) {
            List<String> commitList = Utils.plainFilenamesIn(COMMITS);
            for (String commit : commitList) {
                String commitAdjusted = commit.substring(0, id.length());
                if (commitAdjusted.equals(id)) {
                    id = commit;
                    break;
                }
            }
        }

        // Get commit using ID.
        File commitFile = Utils.join(COMMITS, id);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        HashMap<String, String> cBlobsMap = getBlobsMap(id);

        // Get head commit, serialize file from trueFiles using name and blobsMap to find hashValue.
        String hashValueFile = cBlobsMap.get(name);

        if (hashValueFile == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File fileName = Utils.join(TRUEFILES, hashValueFile);

        // Delete and create file in working directory, writing contents to file.
        File workingDirectoryFile = Utils.join(CWD, name);
        workingDirectoryFile.delete();
        createFile(workingDirectoryFile);
        Utils.writeContents(workingDirectoryFile, Utils.readContentsAsString(fileName));
    }


    // Takes all files in the commit at the head of the given branch, and puts them in the
    // working directory, overwriting the versions of the files that are already there if
    // they exist. Also, at the end of this command, the given branch will now be considered
    // the current branch (HEAD). Any files that are tracked in the current branch but are \
    // not present in the checked-out branch are deleted.
    public void checkout3(String branch) {
        // If branch == current branch, return.
        if (branch.equals(getHeadPointerString())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        File pointer = Utils.join(POINTERS, branch);

        // If branch doesn't exist, return.
        if (!pointer.exists()) {
            System.out.println("No such branch exists.");
            return;
        }

        // If an untracked file exists, return.
        if (showUntrackedFiles().size() > 0) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            return;
        }

        // Else, use commit created by given branch.
        String commitHashValue = Utils.readContentsAsString(pointer);
        HashMap<String, String> cBlobsMap = getBlobsMap(commitHashValue);
        List<String> currentWorkingFiles = Utils.plainFilenamesIn(CWD);

        // Iterate through CWD, deleting and reinstating all files using the commits blobsMap.
        for (String s : currentWorkingFiles) {
            if (s.equals("gitlet") || s.equals(".gitlet")) {
                continue;
            }
            File f = Utils.join(CWD, s);
            f.delete();
        }

        // Iterates through blobsMap and adds all files to CWD.
        Set<String> hashMapFiles = cBlobsMap.keySet();
        for (String h : hashMapFiles) {
            String hashValue = cBlobsMap.get(h);
            File cwdFile = Utils.join(CWD, h);
            createFile(cwdFile);

            File getTrueFile = Utils.join(TRUEFILES, hashValue);
            Utils.writeContents(cwdFile, Utils.readContentsAsString(getTrueFile));
        }

        // Swap head pointers and clear stagingArea.
        swapHeadPointers(branch);
        File[] stagingAreaFiles = STAGINGAREA.listFiles();
        for (File q : stagingAreaFiles) {
            q.delete();
        }
        STAGINGAREA.delete();
        createDirectory(STAGINGAREA);
    }


    // Creates a new pointer, but does not make it the head pointer.
    public void branch(String name) {
        File newBranch = Utils.join(POINTERS, name);
        if (newBranch.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        createFile(newBranch);
        String headCommit = getHeadCommit().hashValue;
        Utils.writeContents(newBranch, headCommit);
    }


    // Removes branch with the given name.
    public void rmBranch(String branch) {
        if (branch.equals(getHeadPointerString())) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        File pointer = Utils.join(POINTERS, branch);
        if (!pointer.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        pointer.delete();
    }


    // Checks out all files in given ID. Removes files not tracked in commit.
    public void reset(String id) {
        File commit = Utils.join(COMMITS, id);
        if (!commit.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }

        // If an untracked file exists, return.
        if (showUntrackedFiles().size() > 0) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            return;
        }

        // Deletes all files in CWD except gitlet files.
        String[] currentWorkingFiles = CWD.list();
        for (String s : currentWorkingFiles) {
            if (s.equals("gitlet") || s.equals(".gitlet")) {
                continue;
            }
            File currentFile = Utils.join(CWD, s);
            currentFile.delete();
        }

        // Adds files from commit's blobsMap to CWD.
        HashMap<String, String> cBlobsMap = getBlobsMap(id);
        List<String> listFiles = new ArrayList<>(cBlobsMap.keySet());
        for (String s : listFiles) {
            checkout2(s, id);
        }
        File f = getHeadPointerFile();
        Utils.writeContents(f, id);

        // Clear staging area.
        File[] stagingAreaFiles = STAGINGAREA.listFiles();
        for (File q : stagingAreaFiles) {
            q.delete();
        }
        STAGINGAREA.delete();
        createDirectory(STAGINGAREA);
    }


    // Merge files from the given branch to the current branch.
    public Commit merge(String branchName) {
        Commit c = null;
        File mainBranch = getHeadPointerFile();
        File otherBranch = Utils.join(POINTERS, branchName);

        // If merge error, return.
        if (mergeErrorCheck(branchName)) {
            return null;
        }

        // Commits for splitPoint, commit of given branch, and commit of head branch.
        String givenBranchCommitHash = Utils.readContentsAsString(otherBranch);
        File givenBranchCommitFile = Utils.join(COMMITS, givenBranchCommitHash);
        Commit givenBranchCommit = Utils.readObject(givenBranchCommitFile, Commit.class);
        Commit currentCommit = getHeadCommit();
        Commit splitPoint = findSplitPoint(currentCommit, givenBranchCommit);

        // BlobsMap for both commits.
        HashMap<String, String> blobsMapCurrentCommit = getHeadBlobsMap();
        HashMap<String, String> blobsMapBranchCommit = getBlobsMap(givenBranchCommitHash);
        HashMap<String, String> blobsMapSplitCommit = getBlobsMap(splitPoint.hashValue);

        // Set of all values in hashMaps.
        Set<String> allFiles = new HashSet<>();
        allFiles.addAll(blobsMapCurrentCommit.keySet());
        allFiles.addAll(blobsMapBranchCommit.keySet());
        allFiles.addAll(blobsMapSplitCommit.keySet());

        // If the split point is the current branch, check out the given branch.
        if (splitPoint.hashValue.equals(currentCommit.hashValue)) {
            System.out.println("Current branch fast-forwarded.");
            checkout3(branchName);
            return null;
        }

        // If the split point is the same commit as the given branch, then we do nothing.
        if (splitPoint.hashValue.equals(givenBranchCommitHash)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return null;
        }

        int i = mergeList(allFiles, branchName,
                blobsMapCurrentCommit, blobsMapBranchCommit, blobsMapSplitCommit);

        c = new Commit(String.format("Merged %s into %s.", branchName,
                getHeadPointerString()));
        c.mergeParents = String.format("%s %s", currentCommit.hashValue, givenBranchCommitHash);

        if (i != 0) {
            System.out.println("Encountered a merge conflict.");
        }
        return c;
    }

    // Helper function to iterate over List
    public int mergeList(Set<String> allFiles, String branchName,
                                         HashMap<String, String> blobsMapCurrentCommit,
                                         HashMap<String, String> blobsMapBranchCommit,
                                         HashMap<String, String> blobsMapSplitCommit) {
        File otherBranch = Utils.join(POINTERS, branchName);
        String givenBranchCommitHash = Utils.readContentsAsString(otherBranch);
        int i = 0;

        for (String file : allFiles) {
            String current = blobsMapCurrentCommit.getOrDefault(file, "empty");
            String branch = blobsMapBranchCommit.getOrDefault(file, "empty");
            String split = blobsMapSplitCommit.getOrDefault(file, "empty");
            // Any files that have been modified in the given branch since the split point,
            // but not modified in the current branch since the split point should be changed
            // to their versions in the given branch.
            if (!branch.equals(current) && split.equals(current) && !split.equals(branch)
                    && !branch.equals("empty") && !split.equals("empty")
                    && !current.equals("empty")) {
                File newFile = Utils.join(STAGINGAREA, file);
                File originalFile = Utils.join(TRUEFILES, branch);
                createFile(newFile);
                Utils.writeContents(newFile, Utils.readContentsAsString(originalFile));
                checkout2(file, givenBranchCommitHash);
                continue;
            }
            // Any files that have been modified in the current branch but not in the given
            // branch since the split point should stay as they are.
            if (!branch.equals(current) && !split.equals(current) && split.equals(branch)
                    && !branch.equals("empty") && !split.equals("empty")
                    && !current.equals("empty")) {
                continue;
            }
            // Any files that have been modified in both the current and given branch in the
            // same way are left unchanged by the merge
            if (branch.equals(current)) {
                continue;
            }
            // Any files that were not present at the split point and are present only in the
            // current branch should remain as they are.
            if (split.equals("empty") && branch.equals("empty") && !current.equals("empty")) {
                continue;
            }
            // Any files that were not present at the split point and are present only in the
            // given branch should be checked out and staged.
            if (split.equals("empty") && !branch.equals("empty") && current.equals("empty")) {
                File newFile = Utils.join(STAGINGAREA, file);
                File originalFile = Utils.join(TRUEFILES, branch);
                createFile(newFile);
                Utils.writeContents(newFile, Utils.readContentsAsString(originalFile));
                checkout2(file, givenBranchCommitHash);
                continue;
            }
            // Any files present at the split point, unmodified in the current branch,
            // and absent in the given branch should be removed (and untracked).
            if (!split.equals("empty") && split.equals(current) && branch.equals("empty")) {
                File currentFile = Utils.join(CWD, file);
                currentFile.delete();
                File newFile = Utils.join(REMOVALSTAGINGAREA, file);
                createFile(newFile);
                Utils.writeContents(newFile, split);
                continue;
            }
            // Any files present at the split point, unmodified in the given branch,
            // and absent in the current branch should remain absent.
            if (!split.equals("empty") && split.equals(branch) && current.equals("empty")) {
                continue;
            }
            // Any files modified in different ways are in conflict.
            i += mergeHelper(file, current, branch, split, branchName);
        }
        return i;
    }


    // Helper function to check for merge errors.
    public static boolean mergeErrorCheck(String branchName) {
        File mainBranch = getHeadPointerFile();
        File otherBranch = Utils.join(POINTERS, branchName);

        // If there is an untracked file in the way, return error message.
        List<String> untrackedFiles = showUntrackedFiles();
        if (untrackedFiles.size() != 0) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            return true;
        }

        // If there are files in stagingArea or removalStagingArea, return error message.
        List<String> fileList = Utils.plainFilenamesIn(STAGINGAREA);
        List<String> removalFileList = Utils.plainFilenamesIn(REMOVALSTAGINGAREA);
        if (fileList.size() != 0 || removalFileList.size() != 0) {
            System.out.println("You have uncommitted changes.");
            return true;
        }

        // If head branch == branchName, return error message.
        if (branchName.equals(getHeadPointerString())) {
            System.out.println("Cannot merge a branch with itself.");
            return true;
        }

        // If branch DNE, return error message.
        if (!otherBranch.exists()) {
            System.out.println("A branch with that name does not exist.");
            return true;
        }

        return false;
    }


    // Helper function to do step 8 of merge.
    public static int mergeHelper(String name, String current, String branch,
                                      String split, String branchName) {
        // the contents of both are changed and different from other,
        boolean bool1 = (!current.equals(branch) && !current.equals(split)
                && !branch.equals(split));

        // or the contents of one are changed and the other file is deleted,
        boolean currentEmpty = (current.equals("empty") && !branch.equals(split)
                && !split.equals("empty"));
        boolean branchEmpty = (branch.equals("empty") && !current.equals(split)
                && !split.equals("empty"));

        // or the file was absent at the split point and has different contents in the branches
        boolean bool3 = (!current.equals(branch) && split.equals("empty"));

        // Commits for splitPoint, commit of given branch, and commit of head branch.
        File otherBranch = Utils.join(POINTERS, branchName);
        String givenBranchCommitHash = Utils.readContentsAsString(otherBranch);
        File givenBranchCommitFile = Utils.join(COMMITS, givenBranchCommitHash);
        Commit givenBranchCommit = Utils.readObject(givenBranchCommitFile, Commit.class);
        Commit currentCommit = getHeadCommit();
        Commit splitPoint = findSplitPoint(currentCommit, givenBranchCommit);

        // BlobsMap for both commits.
        HashMap<String, String> blobsMapCurrentCommit = getHeadBlobsMap();
        HashMap<String, String> blobsMapBranchCommit = getBlobsMap(givenBranchCommitHash);
        HashMap<String, String> blobsMapSplitCommit = getBlobsMap(splitPoint.hashValue);

        // In this case, replace the contents of the conflicted file with
        // <<<<<<< HEAD
        // contents of file in current branch
        // =======
        // contents of file in given branch
        // >>>>>>>
        if (bool1 || bool3 || currentEmpty || branchEmpty) {
            String s = "";
            if (currentEmpty) {
                File f = Utils.join(TRUEFILES, branch);
                s = "<<<<<<< HEAD\n" + "=======\n"
                        + Utils.readContentsAsString(f) + ">>>>>>>\n";
            } else if (branchEmpty) {
                File f = Utils.join(TRUEFILES, current);
                s = "<<<<<<< HEAD\n" + Utils.readContentsAsString(f)
                        + "=======\n" + ">>>>>>>\n";
            } else {
                File f = Utils.join(TRUEFILES, current);
                File g = Utils.join(TRUEFILES, branch);
                s = "<<<<<<< HEAD\n" + Utils.readContentsAsString(f) + "=======\n"
                        + Utils.readContentsAsString(g) + ">>>>>>>\n";
            }
            File h = Utils.join(CWD, name);
            createFile(h);
            Utils.writeContents(h, s);
            File newFile = Utils.join(STAGINGAREA, name);
            createFile(newFile);
            Utils.writeContents(newFile, s);
            return 1;
        }
        return 0;
    }


    // Helper function for finding split points in a commit tree.
    public static Commit findSplitPoint(Commit headCommit, Commit branchSecond) {
        // Get head commit and commit of given branch.
        branchMap = Utils.readObject(BRANCHES, HashMap.class);

        // Find size of each Commit.
        int size1 = showSizeCommit(headCommit);
        int size2 = showSizeCommit(branchSecond);

        // Iterate through parents until Commits are the same.
        while (!headCommit.hashValue.equals(branchSecond.hashValue)) {
            if (headCommit.mergeParents != null) {
                Commit aCommit =
                        branchMap.get(headCommit.mergeParents.substring(0, 40));
                Commit bCommit =
                        branchMap.get(headCommit.mergeParents.substring(41));
                Commit aCommit1 = findSplitPoint(aCommit, branchSecond);
                Commit bCommit1 = findSplitPoint(bCommit, branchSecond);
                int a = showSizeCommit(aCommit1);
                int b = showSizeCommit(bCommit1);
                if (a >= b) {
                    return aCommit1;
                } else {
                    return bCommit1;
                }
            }

            if (branchSecond.mergeParents != null) {
                Commit aCommit =
                        branchMap.get(branchSecond.mergeParents.substring(0, 40));
                Commit bCommit =
                        branchMap.get(branchSecond.mergeParents.substring(41));
                Commit aCommit1 = findSplitPoint(headCommit, aCommit);
                Commit bCommit1 = findSplitPoint(headCommit, bCommit);
                int a = showSizeCommit(aCommit1);
                int b = showSizeCommit(bCommit1);
                if (a >= b) {
                    return aCommit1;
                } else {
                    return bCommit1;
                }
            }

            if (size1 > size2) {
                size1 -= 1;
                headCommit = branchMap.get(headCommit.parent);
            } else if (size2 > size1) {
                size2 -= 1;
                branchSecond = branchMap.get(branchSecond.parent);
            } else if (size1 == size2) {
                size1 -= 1;
                headCommit = branchMap.get(headCommit.parent);
                size2 -= 1;
                branchSecond = branchMap.get(branchSecond.parent);
            }
        }
        return headCommit;
    }


    // Helper function to show size of Commit.
    public static int showSizeCommit(Commit c) {
        int size = 0;
        branchMap = Utils.readObject(BRANCHES, HashMap.class);
        while (c != null) {
            c = branchMap.get(c.parent);
            size += 1;
        }
        return size;
    }


    // Helper function to show untracked files.
    public static List<String> showUntrackedFiles() {
        // Files are untracked if they are present in CWD
        // but neither staged for addition nor tracked.
        HashMap<String, String> cBlobsMap = getHeadBlobsMap();
        List<String> L = new ArrayList<>();
        List<String> workingDirectoryFiles = Utils.plainFilenamesIn(CWD);
        List<String> stagingAreaFiles = Utils.plainFilenamesIn(STAGINGAREA);

        for (String s : workingDirectoryFiles) {
            if (s.equals(".gitlet") || s.equals("gitlet")) {
                continue;
            }
            if (cBlobsMap.get(s) != null) {
                continue;
            }
            if (stagingAreaFiles.contains(s)) {
                continue;
            } else {
                L.add(s);
            }
        }
        return L;
    }



    // Shows files that are modified but not staged.
    public static List<String> showModifiedNotStaged() {

        HashMap<String, String> cBlobsMap = getHeadBlobsMap();
        List<String> stagingAreaFiles = Utils.plainFilenamesIn(STAGINGAREA);
        List<String> currentWorkingFiles = Utils.plainFilenamesIn(CWD);
        Set<String> allFiles = new HashSet<>();
        List<String> L = new ArrayList<>();

        allFiles.addAll(stagingAreaFiles);
        allFiles.addAll(currentWorkingFiles);
        allFiles.addAll(cBlobsMap.keySet());

        for (String s : allFiles) {
            if (s.equals(".gitlet") || s.equals("gitlet")) {
                continue;
            }

            File stagedFile = Utils.join(STAGINGAREA, s);
            File mainDirectoryFile = Utils.join(CWD, s);
            File removedFile = Utils.join(REMOVALSTAGINGAREA, s);
            String committedHashValue = cBlobsMap.get(s);

            String workingHashValue = null;
            String stagedHashValue = null;
            if (mainDirectoryFile.exists()) {
                workingHashValue = getFileHashValue(mainDirectoryFile);
            }
            if (stagedFile.exists()) {
                stagedHashValue = getFileHashValue(stagedFile);
            }

            // Tracked in the current commit, changed in the working
            // directory, but not staged
            if (committedHashValue != null && !committedHashValue.equals(workingHashValue)
                    && !stagedFile.exists() && workingHashValue != null) {
                L.add(s + " (modified)");
                continue;
            }

            // Staged for addition, but with different contents
            // than in the working directory
            if (stagedFile.exists() && !workingHashValue.equals(stagedHashValue)
                    && workingHashValue != null) {
                L.add(s + " (modified)");
                continue;
            }

            // Staged for addition, but deleted in the working directory
            if (stagedFile.exists() && !mainDirectoryFile.exists()) {
                L.add(s + " (deleted)");
                continue;
            }

            // Not staged for removal, but tracked in the current commit
            // and deleted from the working directory
            if (!removedFile.exists() && committedHashValue != null
                    && !mainDirectoryFile.exists()) {
                L.add(s + " (deleted)");
                continue;
            }
        }
        Collections.sort(L);
        return L;
    }



    // Helper function to get blobsMap of commit ID.
    public static HashMap<String, String> getBlobsMap(String id) {
        File f = Utils.join(BLOBSMAPS, id);
        if (!f.exists()) {
            System.out.println("Commit id does not exist.");
            return null;
        }
        return Utils.readObject(f, HashMap.class);
    }


    // Helper function to get blobsMap of head.
    public static HashMap<String, String> getHeadBlobsMap() {
        return getBlobsMap(getHeadCommit().hashValue);
    }


    // Helper function to create file.
    public static void createFile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Helper function to create directory.
    public static void createDirectory(File f) {
        f.mkdir();
    }


    // Helper function to get hashValue of serializable, such as this commit.
    public static String getHashValue(Serializable s) {
        Serializable ser = Utils.serialize(s);
        return Utils.sha1(ser);
    }


    // Helper function to get hashValue of file.
    public static String getFileHashValue(File f) {
        Serializable s = Utils.readContents(f);
        if (s == null) {
            return getHashValue(f);
        }
        return Utils.sha1(s);
    }


    // Returns head commit as a commit file.
    public static Commit getHeadCommit() {
        File s = getHeadPointerFile();
        String sus = Utils.readContentsAsString(s);
        File f = Utils.join(COMMITS, sus);
        return Utils.readObject(f, Commit.class);
    }


    // Gets head pointer as String.
    public static String getHeadPointerString() {
        String[] headPointerList = HEADPOINTER.list();
        return headPointerList[0];
    }


    // Gets head pointer as File.
    public static File getHeadPointerFile() {
        File[] headPointerList = HEADPOINTER.listFiles();
        return headPointerList[0];
    }


    // Switches String name to head pointer, moving previous head to Directory pointers.
    public static void swapHeadPointers(String name) {
        File f = Utils.join(POINTERS, name);
        if (!f.exists()) {
            return; // Pointer DNE.
        } else {
            // Get head pointer, join pointers file and head pointer,
            // write contents to new pointer, delete old.
            File headPointers = getHeadPointerFile();
            String headPointerString = getHeadPointerString();
            File movePointer = Utils.join(POINTERS, headPointerString);
            createFile(movePointer);
            Utils.writeContents(movePointer, Utils.readContentsAsString(headPointers));
            headPointers.delete();

            // Create new pointer, write contents to pointer, delete old.
            File newPointer = Utils.join(HEADPOINTER, name);
            createFile(newPointer);
            Utils.writeContents(newPointer, Utils.readContentsAsString(f));
            f.delete();
        }
    }


    // Helper function to print out commit details.
    private static void logHelper(Commit newHead) {
        System.out.println("===");
        System.out.println("commit " + newHead.hashValue);
        System.out.println(newHead.time);
        System.out.println(newHead.message);
        System.out.println();
    }


    // Helper function to print out commit details.
    private static void logHelperWithMerge(Commit newHead) {
        System.out.println("===");
        System.out.println("commit " + newHead.hashValue);
        System.out.println(String.format("Merge: %s %s", newHead.mergeParents.substring(0, 7),
                newHead.mergeParents.substring(41, 48)));
        System.out.println(newHead.time);
        System.out.println(newHead.message);
        System.out.println();
    }


}
