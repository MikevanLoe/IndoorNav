package project.movinindoor;

/**
 * Created by Davey on 10-12-2014.
 */
public class CalcMath {

    /**
     *  function to calculate the distance between two latlong points.
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first poitn
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @return the meters in a double
     */
    public static double measureMeters(double lat1,double lon1,double lat2,double lon2){
        double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * java.lang.Math.PI / 180;
        double dLon = (lon2 - lon1) * java.lang.Math.PI / 180;
        double a = java.lang.Math.sin(dLat / 2) * java.lang.Math.sin(dLat / 2) +
                java.lang.Math.cos(lat1 * java.lang.Math.PI / 180) * java.lang.Math.cos(lat2 * java.lang.Math.PI / 180) *
                        java.lang.Math.sin(dLon / 2) * java.lang.Math.sin(dLon / 2);
        double c = 2 * java.lang.Math.atan2(java.lang.Math.sqrt(a), java.lang.Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000; // meters
    }
}
