package project.movinindoor.Graph;

/**
 * Created by Wietse on 18-12-2014.
 */
public class edgeActions {
    public String action;
    public int toNodeID;
    public int leftTurns;
    public int rightTurns;
    public int leftDoors;
    public int rightDoors;

    public edgeActions(String action, int toNodeID, int rightDoors, int leftDoors, int leftTurns, int rightTurns) {
        this.action = action;
        this.toNodeID = toNodeID;
        this.leftTurns = leftTurns;
        this.rightTurns = rightTurns;
        this.leftDoors = leftDoors;
        this.rightDoors = rightDoors;
    }
}

