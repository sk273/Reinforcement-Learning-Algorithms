import java.util.Random;

/**
 * POMDPs are partially observable Markov decision processes.
 * 
 * @author Seon Kang
 *
 */

public class Pomdp {
    
    /**
     * Runs simulation for various cases.
     * 
     * @param args Unused.
     */
    public static void main(String[] args) {
        Pomdp myPomdp = new Pomdp();
        Options myOptions = new Options();

        int[] colors = {0, 4, 20, 23}; // Colored squares
        for (int i = 0; i < colors.length; i++) { // Starting square
            for (int j = 0; j < colors.length; j++) { // Goal square
                if (i != j ) {
                    System.out.println("-----------------------------------" 
                         + "-----------------------------");
                    System.out.println("Start: " + colors[i] + " Goal: " + colors[j]);
                    myPomdp.simulate(colors[i], colors[j], myOptions.getPolicyByGoal(colors[j]));
                }
            }
        }
    }

    /**
     * Takes the current state of the agent as well as the action it takes
     * and returns the sensor readings after simulating the action.
     * 
     * @param state Current agent state.
     * @param action Action that agent takes.
     * @return Sensor readings (observation).
     */
    public int[] transition(int state, int action) { /* Take the location and action, update and
                                                        return the observation. */
        int[] observation = new int[5]; /* First index is the new location, next four are the sensor
                                           readings. */
        Random random = new Random();
        SensorReadings mySensorReadings = new SensorReadings();
        TaxiDomain myTaxiDomain = new TaxiDomain();
        int[] reverse = myTaxiDomain.getReverseLocationIDs();
        // Update the new true location.
        observation[0] = reverse[myTaxiDomain.locationIDs[state] 
                + myTaxiDomain.goodActions[state][action]]; 
        for (int i = 0; i < 4; i++) {
            observation[i + 1] = mySensorReadings.readings[observation[0]][i];/* Take the readings
                                                                                based on the
                                                                                actual state and
                                                                                pass them along. */
        }
        if (random.nextDouble() < 0.2) { // Simulating possible error in sensor reading.
            int flip = 1 + random.nextInt(4);
            observation[flip] = ~observation[flip]; // Flip a random bit.
            System.out.println("flipped bit: " + (flip - 1));
        }
        return observation;
    }

    /**
     * Simulates the taxi agent and sensor observations.
     * 
     * @param init True initial location of the taxi.
     * @param goal The goal location.
     * @param policy The policy used in this simulation.
     */
    public void simulate(int init, int goal, int[] policy) { /* Simulates the taxi navigating from
                                                                 depot to depot. */
        Random random = new Random();
        SensorReadings mySensorReadings = new SensorReadings();
        TaxiDomain myTaxiDomain = new TaxiDomain();
        int[] reverse = myTaxiDomain.getReverseLocationIDs(); /* Input the location value 
                                                               and get the index back. */
        int location = init; // True location of the taxi.
        double[] belief = new double[25]; // Taxi's belief distribution.
        for (int i = 0; i < belief.length; i++) { // Initially, everywhere is equal.
            belief[i] = 1.0 / belief.length;
        }
        int steps = 0;
        while (true) { // Main update loop.
            System.out.println("Step: " + steps);
            steps++;
            if (location == goal) { // Check for termination
                break; 
            }
            int max = random.nextInt(25);
            for (int i = 0; i < belief.length; i++) { // picks the most likely state
                if (belief[i] > belief[max]) {
                    max = i;
                }
            }
            int action = policy[max]; /* Determines action from the given policy using the most
                                         likely state. */
            System.out.println(belief[max]);
            System.out.println("True location: " + location);
            System.out.println("Believed location: " + max);
            if (random.nextDouble() < 0.15) { // Chooses a random direction with probability 0.15
                action = random.nextInt(4) + 2;
                System.out.println("random action taken");
            } 
            int[] observation = transition(location, action); /* First index is the new true
                                                                 location and the next four are the
                                                                 sensor readings. */
            double sum = 0; // Normalizing factor
            double[] tempBelief = new double[25];
            for (int i = 0; i < belief.length; i++) { /* Update the beliefs based
                                                        on the observations. */ 
                double transitionProbability = 0;
                double observationProbability = 0.8;
                int errors = 0;
                // Computes the probability of seeing this observation for this state.
                for (int j = 0; j < 4; j++) {
                    //If the sensor reading is off and you have yet to see an error.
                    if (errors == 0 && observation[j + 1] != mySensorReadings.readings[i][j]) {
                        observationProbability = 0.2;
                        errors++;
                    }
                    else if (errors > 0 && observation[j + 1] != mySensorReadings.readings[i][j]) {
                        observationProbability = 0;
                        break;
                    }
                }
                /*
                 * Sum over all states of the belief at that state times the transition
                 * probability.
                 */
                for (int j = 0; j < belief.length; j++) {
                    // Probability that state j with action leads to state i.
                    double transitionFunction = 0;
                    // This is when the action succeeds.
                    if (i == reverse[myTaxiDomain.locationIDs[j] 
                            + myTaxiDomain.goodActions[j][action]]) {
                        transitionFunction = 0.85 + (0.15 / 4);
                    }
                    else { // Case where a random action is taken.
                        for (int k = 2; k < 6; k++) {
                            if (i == reverse[myTaxiDomain.locationIDs[j] 
                                    + myTaxiDomain.goodActions[j][k]]
                                && k != action) { // go through all actions except the desired one
                                transitionFunction = 0.15 - (0.15 / 4);
                            }
                        }
                    }
                    transitionProbability += belief[j] * transitionFunction;
                }
                // Key update of belief state.
                tempBelief[i] = observationProbability * transitionProbability;
                sum += tempBelief[i]; // Keeping track of the normalization factor.
            }
            // Normalize the belief values so they add up to 1.
            for (int i = 0; i < belief.length; i++) {
                belief[i] = tempBelief[i] / sum;
            }
            location = observation[0]; // Update the true location of the taxi.

        }
    }
}
