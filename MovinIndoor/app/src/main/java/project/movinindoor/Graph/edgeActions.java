package project.movinindoor.Graph;

/**
 * Created by Wietse on 18-12-2014.
 */
public class edgeActions {
    private String action;
    private int toNodeID;
    private int leftTurns;
    private int rightTurns;
    private int leftDoors;
    private int rightDoors;

    public edgeActions(String action, int toNodeID, int rightDoors, int leftDoors, int leftTurns, int rightTurns) {
        this.action = action;
        this.toNodeID = toNodeID;
        this.leftTurns = leftTurns;
        this.rightTurns = rightTurns;
        this.leftDoors = leftDoors;
        this.rightDoors = rightDoors;
    }

    public int getToNodeID() {
        return toNodeID;
    }

    public String getAction() {
        return action;
    }
}

