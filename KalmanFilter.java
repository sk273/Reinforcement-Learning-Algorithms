import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Implements a simple Kalman filter. More in-depth information on Kalman
 * filters is available in the README file.
 * 
 * @author Seon Kang
 *
 */

public class KalmanFilter {
    public static double Q = 0.02;
    public static double R = 0.02;
    double[] kalmanX = new double[64];
    double[] kalmanY = new double[64];

    /**
     * Runs the Kalman Filter for a sine wave.
     * 
     * @param args Unused.
     */
    public static void main(String[] args) {
        double[] noisyControlX = new double[64];
        double[] noisyControlY = new double[64];
        double[] noisyObservationX = new double[64];
        double[] noisyObservationY = new double[64];
        KalmanFilter myKalmanFilter = new KalmanFilter();
        KalmanMatrices myMatrices = new KalmanMatrices();
        MatrixUtility myUtilities = new MatrixUtility();
        double step = 0.1;
        for (double t = 0; t <= 6.3; t += step) {
            System.out.println("time: " + t);
            myMatrices.x_t = myKalmanFilter.planeControl(t, myUtilities, myMatrices);
            /*
             * System.out.println("Noiseless control");
             * System.out.println("x: " + myMatrices.x_t[0][0]);
             * System.out.println("y: " + myMatrices.x_t[0][1]);
             * System.out.println("sin(t): " + Math.sin(t));
             * System.out.println("x': " + myMatrices.x_t[1][0]);
             * System.out.println("y': " + myMatrices.x_t[1][1]);
             * System.out.println("u x': " + myMatrices.u_t[1][0]);
             * System.out.println("u y': " + myMatrices.u_t[1][1]);
             */     
            // Add transition noise.
            myMatrices.x_t = myKalmanFilter.addNoise(myMatrices.x_t, 0, Q);
            /*
             * System.out.println("Noisy control");
             * System.out.println("x: " + myMatrices.x_t[0][0]);
             * System.out.println("y: " + myMatrices.x_t[0][1]);
             * System.out.println("x': " + myMatrices.x_t[1][0]);
             * System.out.println("y': " + myMatrices.x_t[1][1]);
             */
            noisyControlX[(int) Math.round(t * 10)] = myMatrices.x_t[0][0];
            noisyControlY[(int) Math.round(t * 10)] = myMatrices.x_t[0][1];
            // Add observation noise.
            myMatrices.z_t = myUtilities.copy(myUtilities.multiply(myMatrices.H, myMatrices.x_t));
            myMatrices.z_t = myKalmanFilter.addNoise(myMatrices.z_t, 0, R);
            /*
             * System.out.println("Noisy observations");
             * System.out.println("x: " + myMatrices.z_t[0][0]);
             * System.out.println("y: " + myMatrices.z_t[0][1]);
             * System.out.println("x': " + myMatrices.z_t[1][0]);
             * System.out.println("y': " + myMatrices.z_t[1][1]);
             */
            noisyObservationX[(int) Math.round(t * 10)] = myMatrices.z_t[0][0];
            noisyObservationY[(int) Math.round(t * 10)] = myMatrices.z_t[0][1];
            // The actual Kalman Filter.
            myKalmanFilter.filter(t, myUtilities, myMatrices, myKalmanFilter);
        }
        /*
         * System.out.println("noisy control x");
         * for (double d: noisyControlX) System.out.println(d);
         * System.out.println("noisy control y");
         * for (double d: noisyControlY) System.out.println(d);
         * System.out.println("noisy observation x");
         * for (double d: noisyObservationX) System.out.println(d);
         * System.out.println("noisy observation y");
         * for (double d: noisyObservationY) System.out.println(d);
         * System.out.println("Kalman x");
         * for (double d: myKF.KalmanX) System.out.println(d);
         * System.out.println("Kalman y");
         * for (double d: myKF.KalmanY) System.out.println(d);
         */
    }

    /**
     * Updates the plane's position. Simulates control.
     * 
     * @param time Time step of simulation.
     * @param myUtilities Source of the matrix operations used. 
     * @param myMatrices Source of the matrices used.
     * @return New position of the plane
     */
    public double[][] planeControl(double time,
                                    MatrixUtility myUtilities,
                                    KalmanMatrices myMatrices) { /* Tries to accelerate to
                                                                    form sin wave
                                                                    calculate the u_t necessary 
                                                                    to stay 
                                                                    on sin wave, leave x 
                                                                    and y alone.*/
        myMatrices.u_t[1][0] = time - myMatrices.x_t[0][0] - myMatrices.x_t[1][0]; // x'
        myMatrices.u_t[1][1] = Math.sin(time) - myMatrices.x_t[0][1] - myMatrices.x_t[1][1]; // y'
        myMatrices.u_t[0][0] = myMatrices.u_t[1][0];
        myMatrices.u_t[0][1] = myMatrices.u_t[1][1];
        double[][] pos =
                myUtilities.add(myUtilities.multiply(myMatrices.F, myMatrices.x_t),
                                myUtilities.multiply(myMatrices.B, myMatrices.u_t));
        return pos;
    }

    /**
     * Adds Gaussian noise to each element of matrix A
     * 
     * @param matrixA Matrix to which noise is applied.
     * @param mean Mean of the Gaussian distribution from which noise is sampled.
     * @param sigma Standard deviation of the Gaussian distribution from which noise is sampled.
     * @return Matrix A with Gaussian noise applied to each element.
     */
    public double[][] addNoise(double[][] matrixA, double mean, double sigma) {
        NormalDistribution myDist = new NormalDistribution(0, sigma);
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[0].length; j++) {
                matrixA[i][j] += myDist.sample();
            }
        }
        return matrixA;
    }

    /**
     * The main body of the Kalman Filter implementation.
     * 
     * @param time Time step of simulation.
     * @param myUtilities Source of the matrix operations used. 
     * @param myMatrices Source of the matrices used.
     * @param myKalmanFilter Instance of KalmanFilter class.
     */
    public void filter(double time,
                       MatrixUtility myUtilities,
                       KalmanMatrices myMatrices,
                       KalmanFilter myKalmanFilter) { // Updates mean and covariance.
        // Predict and print out values.
        myMatrices.meanPrediction =
                myUtilities.add(myUtilities.multiply(myMatrices.F, myMatrices.meanEstimate),
                                myUtilities.multiply(myMatrices.B, myMatrices.u_t));
        System.out.println("mean prediction");
        myUtilities.print(myMatrices.meanPrediction);
        myMatrices.covariancePrediction =
                myUtilities.add(myUtilities.multiply(
                                                     myUtilities.multiply(myMatrices.F,
                                                         myMatrices.covarianceEstimate),
                                                     myUtilities.transpose(myMatrices.F)),
                                myMatrices.Q);
        // Update and print out values.
        System.out.println("covariance prediction");
        myUtilities.print(myMatrices.covariancePrediction);
        calculateY(myUtilities, myMatrices);
        calculateS(myUtilities, myMatrices);
        calculateK(myUtilities, myMatrices);
        myMatrices.meanEstimate =
                myUtilities.add(myMatrices.meanPrediction,
                                myUtilities.multiply(myMatrices.K, myMatrices.y_t));
        myMatrices.covarianceEstimate =
                myUtilities.multiply(myUtilities
                                     .subtract(myUtilities.identity(myMatrices.H.length),
                                               myUtilities.multiply(myMatrices.K, myMatrices.H)),
                                     myMatrices.covariancePrediction);
        System.out.println("mean estimate");
        myKalmanFilter.kalmanX[(int) Math.round(time * 10)] = myMatrices.meanEstimate[0][0];
        myKalmanFilter.kalmanY[(int) Math.round(time * 10)] = myMatrices.meanEstimate[0][1];
        myUtilities.print(myMatrices.meanEstimate);
        System.out.println("covariance estimate");
        myUtilities.print(myMatrices.covarianceEstimate);
    }

    /**
     * Helper function to calculate Y.
     * 
     * @param myUtilities Source of the matrix operations used. 
     * @param myMatrices Source of the matrices used.
     */
    public void calculateY(MatrixUtility myUtilities, KalmanMatrices myMatrices) {
        myMatrices.y_t =
                myUtilities.subtract(myMatrices.z_t,
                                     myUtilities.multiply(myMatrices.H, myMatrices.meanPrediction));
    }

    /**
     * Helper function to calculate S.
     * 
     * @param myUtilities Source of the matrix operations used. 
     * @param myMatrices Source of the matrices used.
     */
    public void calculateS(MatrixUtility myUtilities, KalmanMatrices myMatrices) {
        myMatrices.S =
                myUtilities.add(myUtilities.multiply(
                                                     myUtilities.multiply(myMatrices.H,
                                                         myMatrices.covariancePrediction),
                                                     myUtilities.transpose(myMatrices.H)),
                                myUtilities.fill(myMatrices.S.length, myMatrices.S[0].length, R));
    }

    /**
     * Helper function to calculate K.
     * 
     * @param myUtilities Source of the matrix operations used. 
     * @param myMatrices Source of the matrices used.
     */
    public void calculateK(MatrixUtility myUtilities, KalmanMatrices myMatrices) {
        myMatrices.K =
                myUtilities.multiply(
                                     myUtilities.multiply(myMatrices.covariancePrediction,
                                                          myMatrices.H),
                                     myUtilities.invert(myMatrices.S));
    }
}
