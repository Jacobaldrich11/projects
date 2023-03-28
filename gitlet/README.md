

========================
Gitlet Class Reference
========================

--------------
class Commit
--------------
	Overview: 
		Abstract class representing a commit in the Git version control system. Contains a message, time of commit, parent, a hashValue, currentTime, and mergeParents.
    	

	Calling: 
		Commit c = new Commit(“Updated File 1. Ready for code review.”);

    	
	Arguments: 
		String message - Message associated with the commit function.
    	
--------------
class Init
--------------
	Overview: 
		Abstract class representing the space which our Git version control system needs to keep track of. This includes: Folders/Files necessary for the control system to work, a branchMap to keep track of branches, and a headBlopsMap to keep track of our blobs (Small fragments of our files).
    	

	Calling: 
		Init In = new Init();

    	
	Arguments: 
		None.

    	
	Functions:
		commit(Commit c) - Check if we are ready to make a commit. If we are, allocate all necessary files to perform a commit. Then, save all relevant files to their respective locations in our file system. Finally, update our existing file system to match Git’s behavior.

		add(String name) - Adds a file to our staging area. Perform checks to ensure specified behavior.

		remove(String name) - Removes file from our staging area or deletes file from a previous commit. 

		log() - Create a log from our head commit.

		globalLog() - Create a global log of all our commits.

		find(String message) - Find the commit with the same message.

		status() - Prints out the status of our Branches, Staged Files, Removed Files, Modifications not Staged for Commit, and Untracked files.

		checkout1(String name) - Checkout function for behavior 1.

		checkout2(String name, String id) - Checkout function for behavior 2.

		checkout3(String name) - Checkout function for behavior 3. 

		branch(String name) - Create a new branch.

		rmBranch(String name) - Remove an existing branch.

		reset(String id) - Checks out all files in a given ID. Removes files not tracked in commit.

		merge(String branchName) - Merge files from the given branch to the current branch.

		mergeList(Set<String> allFiles, String branchName, HashMap<String, String> blobsMapCurrentCommit, HashMap<String, String> blobsMapBranchCommit, HashMap<String, 
		String> blobsMapSplitCommit) - Helper function to iterate over List during merge.

		mergeErrorCheck(String branchName) - Helper function to check if there is an error before merging our files.

		mergeHelper(String name, String current, String branch, String split, String branchName) - Helper function to perform the actual merge of our files.

		findSplitPoint(Commit headCommit, Commit branchSecond) - Helper function to find the split point of our branch directory.

		showSizeCommit(Commit c) - Tells us the size of a commit.

		showUntrackedFiles() - Helper function to show untracked files.

		showModifiedNotStaged() - Helper function to show us modified, but not staged files.

		getBlobsMap(String id) - Helper function to retrieve our BlobsMap with the associated ID. 

		getHeadBlobsMap() - Helper function to retrieve our Head BlopsMap.

		createFile(File f) - Helper function to create a file.

		createDirectory(File f) - Helper function to create a directory.

		getHashValue(Serializable s) - Gives us the hash value of a serializable Object s. Uses my defined hash function. 

		getFileHashValue(File f) - Gets the hash value of a File F.

		getHeadCommit() - Returns head commit as a commit file.

		getHeadPointerString() - Gets head pointer as a String.

		getHeadPointerFile() - Gets head pointer as File.

		swapHeadPointers(String name) - Switches String name to head pointer, moving previous head to Directory pointers.

		logHelper(Commit newHead) - Helper function to print out commit details.

		logHelperWithMerge(Commit newHead) - Helper function to print out commit details.

    	
--------------
class Main
--------------
	Overview: 
		Driver class for Gitlet, a subset of the Git version-control system. Makes use of the Commit class and Init class, along with many other smaller classes. Uses text input from STDIN to make changes to our files, much like the Git version control system. Sends output to STDOUT, also similar to the Git version control system. 
    	

	Calling [In Terminal]: 
		java gitlet.Main [command]

    	
	Arguments: 
	String[] args - Arguments passed in from STDIN, or the terminal. These can be:
		init
		add
		commit
		rm
		log
		global-log
		status
		find
		checkout
		branch
		rm-branch
		reset
		merge
		
--------------
classes DumpObj, Dumpable, GitletException, Utils
--------------
	Overview: 
		Various extraneous classes for easier implementation of other classes. Nothing much to see here.



    	
========================
Gitlet Testing Reference
========================
    	
--------------
To Test
--------------
	Write “make check” in the terminal to run unit tests automatically. We make use of a python script “tester.py” to run our tests automatically. 
	
	Write “make check TESTER_FLAGS=’--verbose’” to get more info about tests. 
	
	To run a certain test and keep the file directory, write “python3 tester.py --verbose --keep FILE.in [File Name]”.

--------------
Tests
--------------
	test01-init.in
	test02-basic-checkout.in
	test03-basic-log.in
	test04-prev-checkout.in
	definitions.inc
	merge-test-case-from-quiz.in
	student-test-01.in

