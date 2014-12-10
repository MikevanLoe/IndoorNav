package project.movinindoor.Graph.Graph;

/**
 * Created by Wietse on 24-11-2014.
 */
public class Path implements Comparable<Path> {

    private Vertex dest;
    private double cost;

    public Path(Vertex dest, double cost) {
        this.dest = dest;
        this.cost = cost;
    }

    public Vertex getDest() {
        return dest;
    }

    public double getCost() {
        return cost;
    }


    @Override
    public int compareTo(Path p) {
        double otherCost = p.cost;
        if(cost < otherCost){
            return -1;
        }else{
            if(cost > otherCost){
                return 1;
            }else{
                return 0;
            }
        }
    }

}
