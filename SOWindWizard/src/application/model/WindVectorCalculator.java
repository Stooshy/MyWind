package application.model;

public final class WindVectorCalculator {
    private static double[] factorsX = new double[]{0.17, 0.21, 0.57, 0.87, 0.98, 0.97, 0.7, 0.53, 0.17, 0.21, 0.57, 0.81, 0.98, 0.97, 0.7, 0.53};
    private static double[] factorsY = new double[]{-1.98, -1.97, -1.81, -1.58, -1.17, 1.21, 1.57, 1.84, 1.98, 1.97, 1.81, 1.58, 1.17, -1.21, -1.57, -1.84};

    private WindVectorCalculator() {
    }

    /***
     * Calculates X&Y components of wind for a given direction.
     * @param direction of wind from 0-15<p> 18:00 => 0</p> <p> 17:00 => 1 ... </p>
     * @param wind in m/s. 
     */
    public static double[] calcWindVectors(int direction, double wind) {
        return new double[]{wind * factorsX[direction], wind * factorsY[direction]};
    }
}
