package gitlet;


import java.io.Serializable;
import java.util.Date;

/** Represents a gitlet commit object.
 *
 *  @author Jacob Aldrich
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    protected String message;
    protected String time;
    protected String parent;
    protected String hashValue;
    protected Date currentTime;
    protected String mergeParents;


    // Creates commit using message as args[1], and head as parent.
    public Commit(String message) {
        this.message = message;
        if (message.equals("initial commit")) {
            this.parent = null;
        } else {
            String s = Utils.readContentsAsString(Init.getHeadPointerFile());
            this.parent = s;
        }

        // If initial commit, set Date as initial date.
        if (this.parent == null) {
            currentTime = new Date(0);
        } else {
            currentTime = new Date();
        }
        this.time = String.format("Date: %1$ta %1$tb %1$te %1$tT %1$tY %1$tz", currentTime);
        this.hashValue = Init.getHashValue(this);
        this.mergeParents = null;
    }
}
