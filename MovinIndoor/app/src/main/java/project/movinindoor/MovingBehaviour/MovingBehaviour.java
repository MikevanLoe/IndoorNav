package project.movinindoor.MovingBehaviour;

/**
 * Created by 5736z454 on 7-1-2015.
 */
public abstract class MovingBehaviour {
    static int WALKING_SPEED;
    final static int MINUTE_IN_SEC = 3600;

    public String calculateMovingSpeed(double cost){
        float walkingspeedPerSecond = ((float) WALKING_SPEED) / MINUTE_IN_SEC;
        double time = (cost / walkingspeedPerSecond);
        int minute = (int) time / 60;
        int second = (int) time % 60;
        return String.format("%dm%02ds", minute, second);
    }
}
