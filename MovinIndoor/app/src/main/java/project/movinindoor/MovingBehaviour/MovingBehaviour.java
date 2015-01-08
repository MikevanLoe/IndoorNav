package project.movinindoor.MovingBehaviour;

/**
 * Created by 5736z454 on 7-1-2015.
 */
public abstract class MovingBehaviour {
    // The speed in meter per hour
    private static int walkingSpeed;
    // A single hour in seconds
    final static int HOUR_IN_SEC = 3600;

    /**
     * Returns the walkingSpeed
     *
     * @return The set walkingSpeed
     */
    public int getWalkingSpeed(){
        return walkingSpeed;
    }

    /**
     * Sets the walkingSpeed
     *
     * @param speed The new speed of the behaviour
     */
    public void setWalkingSpeed(int speed){
        walkingSpeed = speed;
    }

    /**
     * Calculates the time it will take to walk the given distance
     *  based on the specified WALKING_SPEED
     *
     * @param cost The length of the path
     * @return A string showing the time in the mm:ss format.
     */
    public String calculateMovingSpeed(double cost){
        float walkingSpeedPerSecond = ((float) getWalkingSpeed()) / HOUR_IN_SEC;
        double time = (cost / walkingSpeedPerSecond);
        int minute = (int) time / 60;
        int second = (int) time % 60;
        return String.format("%dm%02ds", minute, second);
    }
}
