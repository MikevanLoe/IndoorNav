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


    /**
     *
     * @param action action name
     * @param toNodeID specifies which node this edgeaction refers to
     * @param rightDoors  amount of rightDoors
     * @param leftDoors amount of leftDoors
     * @param leftTurns amount of leftTurns
     * @param rightTurns amount of rightTurns
     */
    public edgeActions(String action, int toNodeID, int rightDoors, int leftDoors, int leftTurns, int rightTurns) {
        this.action = action;
        this.toNodeID = toNodeID;
        this.leftTurns = leftTurns;
        this.rightTurns = rightTurns;
        this.leftDoors = leftDoors;
        this.rightDoors = rightDoors;
    }

    /**
     *
     * @return ToNodeID
     */
    public int getToNodeID() {
        return toNodeID;
    }

    /**
     *
     * @return action
     */
    public String getAction() {
        return action;
    }
}

