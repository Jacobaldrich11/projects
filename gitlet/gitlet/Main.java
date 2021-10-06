package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Jacob Aldrich
 */


public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */


    public static void main(String[] args) {
        // If no arguments, return
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        Init initiateGitlet = new Init();
        Commit initialCommit = new Commit("initial commit");

        switch(firstArg) {
            // Initiate .gitlet file.
            case "init":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                if (Init.GITLET.exists()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    return;
                }
                initiateGitlet.init();
                initiateGitlet.commit(initialCommit);
                return;


            // Adds file to stagingArea.
            case "add":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.add(args[1]);
                }
                return;


            // Commits using args[1] and uses head pointer as previous commit.
            case "commit":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length == 1 || args[1].equals("")) {
                        System.out.println("Please enter a commit message.");
                        return;
                    } else if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                    }
                    Commit newCommit = new Commit(args[1]);
                    initiateGitlet.commit(newCommit);
                }
                return;


            // Removes file from stagingArea or deletes file and adds it to removalStagingArea.
            case "rm":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.remove(args[1]);
                }
                return;


            // Shows log of commits starting from head.
            case "log":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length > 1) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.log();
                }
                return;


            // Shows all commits in commits folder.
            case "global-log":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length > 1) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.globalLog();
                }
                return;


            // Finds commit with message args[1].
            case "find":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.find(args[1]);
                }
                return;


            // Shows pointers, files in stagingArea, and files in removalStagingArea.
            case "status":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length > 1) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.status();
                }
                return;


            // Reverts file back to 1. Head Commit, 2. Commit ID, or converts CWD to branch and makes
            // branch the head pointer.
            case "checkout":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                if (args.length == 1) {
                    System.out.println("Incorrect operands.");
                    return;
                } else if (args.length == 2) {
                    initiateGitlet.checkout3(args[1]);
                    return;
                } else if (args[1].equals("--")) {
                    if (args.length != 3) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.checkout1(args[2]);
                    return;
                } else if (args[2].equals("--")) {
                    if (args.length != 4) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.checkout2(args[3], args[1]);
                    return;
                }


            // Creates another pointer.
            case "branch":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.branch(args[1]);
                }
                return;


            // Removes a pointer, but keeps commits.
            case "rm-branch":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.rmBranch(args[1]);
                }
                return;


            // Performs 'checkout' on every file in a commit.
            case "reset":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    initiateGitlet.reset(args[1]);
                }
                return;


            // Big boy merge.
            case "merge":
                if (!Init.GITLET.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                } else {
                    if (args.length != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    Commit c = initiateGitlet.merge(args[1]);
                    if (c != null) {
                        initiateGitlet.commit(c);
                    }
                }
                break;


            // If command is unrecognized, return.
            default:
                System.out.println("No command with that name exists.");
                break;
        }
    }

}
