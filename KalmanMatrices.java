
/**
 * Stores the matrices for the Kalman filter.
 * Initialized to values for tracking a sine wave.
 *
 * @author Seon Kang
 *
 */

public class KalmanMatrices {

    // x_{t} - x, y, x', y'
    double[][] x_t =
            { { 0, 0 }, // position
              { 0, 0 } };// velocity

    // F - adds x' to x and y' to y and keeps x' and y' the same
    double[][] F =
            { { 1, 1 },
              { 0, 1 } };

    // B - just the identity
    double[][] B =
            { { 1, 0 },
              { 0, 1 } };

    // Q - just the identity
    double[][] Q =
            { { 0.02, 0.02 },
              { 0.019, 0.02 } };

    // u_{t+1} - this is inputed by the agent
    double[][] u_t =
            { { 0, 0 },
              { 0, 0 } };
    // H
    double[][] H =
            { { 1, 0 },
              { 0, 1 } };
    // z_{t} - observation
    double[][] z_t =
            { { 0, 0 },
              { 0, 0 } };

    // Mean estimate - x hat
    double[][] meanEstimate =
            { { 0, 0 },
              { 0, 0 } };

    // Mean prediction - x p
    double[][] meanPrediction =
            { { 0, 0 },
              { 0, 0 } };

    // Covariance estimate p
    double[][] covarianceEstimate =
            { { 0, 0 },
              { 0, 0 } };

    // Covariance prediction p
    double[][] covariancePrediction =
            { { 0, 0 },
              { 0, 0 } };

    // Computes y_{t+1}
    double[][] y_t =
            { { 0, 0 },
              { 0, 0 } };

    // Computes K_{t+1}
    double[][] K =
            { { 0, 0 },
              { 0, 0 } };

    // Computes S_{t+1}
    double[][] S =
            { { 0, 0 },
              { 0, 0 } };
}
