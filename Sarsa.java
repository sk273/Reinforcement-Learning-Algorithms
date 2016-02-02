import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Implementation of SARSA with options.
 * 
 * @author Seon Kang
 *
 */

public class Sarsa {
    static int episodes = 100;
    public double[] rewardAverage = new double[episodes];
    // As of now there are just two sets of options, 0 and 1.
    public int optionSet = 1;
    
    /**
     * Runs program and prints out relevant information.
     * 
     * @param args Unused.
     */
    public static void main(String[] args) {
        Sarsa mySarsa = new Sarsa();
        System.out.println("--------------------------------------------------------");
        /*
         * Q[taxi][passenger][together][action]
         * together indicates whether or not the passenger is on the taxi.
         */
        double[][][][] Q = mySarsa.sarsaLambda(0, 0.1, 1, episodes); // lambda = 0, alpha, gamma = 1
        // Episodes
        for (int i = 0; i < 25; i++) {
            int max = 0;
            for (int j = 0; j < 10; j++) {
                if (Q[i][4][0][j] > Q[i][4][0][max]) {
                    max = j;
                }

            }
            System.out.println(max);
        }
    }

    /**
     * Simple rounding function.
     * 
     * @param value Number to be rounded.
     * @param places Number of places to round to.
     * @return Rounded number.
     */
    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Option simulation loop.
     * 
     * @param state Initiation state.
     * @param action Action or option taken.
     * @param successChance Probability that a given action succeeds.
     * @param goal Goal for the episode.
     * @return Array that encodes the reward, taxi, passenger, together, and action.
     */
    public int[] executeOption(int[] state, int action, double successChance, int goal) {
        int[] res = new int[5]; // {reward, taxi, passenger, together, action}
        TaxiDomain myTaxiDomain = new TaxiDomain();
        int[] reverse = myTaxiDomain.getReverseLocationIDs();
        Random random = new Random();
        int[] colors = { 0, 4, 20, 23 }; // colored squares
        res[1] = colors[action]; // This is where the taxi ends up at the end
        res[2] = state[1]; // passenger
        res[3] = state[2]; // together
        res[4] = action + 6; // action
        int reward = 0;
        int taxi = state[1];
        Options myOptions = new Options();
        int[] policy = myOptions.getPolicyByGoal(colors[action]);
        if (optionSet == 0) { // Option to move taxi to a depot.
            while (taxi != colors[action]) {
                if (random.nextDouble() < successChance) {
                    taxi = reverse[myTaxiDomain.locationIDs[taxi] 
                            + myTaxiDomain.goodActions[taxi][policy[taxi]]];
                }
                else {
                    taxi = reverse[myTaxiDomain.locationIDs[taxi]
                            + myTaxiDomain.goodActions[taxi][random.nextInt(4) + 2]];
                }
                reward += -1;
            }
            res[0] = reward;
        }
        else if (optionSet == 1) { // Option to move passenger to a depot.
            if (state[2] == 0) { // Pick up passenger if necessary.
                /*
                 * Policy for getting to the passenger. Does not necessarily need to be
                 * one of the colored squares.
                 */
                int[] tempPolicy = myOptions.getPolicyByGoal(res[2]);
                while (taxi != res[2]) { // Taxi to passenger
                    if (random.nextDouble() < successChance) {
                        taxi =
                        reverse[myTaxiDomain.locationIDs[taxi] 
                                + myTaxiDomain.goodActions[taxi][tempPolicy[taxi]]];
                    }
                    else {
                        taxi =
                        reverse[myTaxiDomain.locationIDs[taxi] 
                                + myTaxiDomain.goodActions[taxi][random.nextInt(4) + 2]];
                    }
                    reward += -1;
                }
            }
            reward += -1; // Minus reward for picking up passenger
            while (taxi != colors[action]) { // Go to target
                if (random.nextDouble() < successChance) {
                    taxi = reverse[myTaxiDomain.locationIDs[taxi] 
                            + myTaxiDomain.goodActions[taxi][policy[taxi]]];
                }
                else {
                    taxi = reverse[myTaxiDomain.locationIDs[taxi] 
                            + myTaxiDomain.goodActions[taxi][random.nextInt(4) + 2]];
                }
                reward += -1;
            }
            res[2] = res[1]; // passenger
            res[3] = 0; // together
            reward += -1; // Dropping off the passenger
            if (res[2] == goal) { // If you drop him off at goal
                reward += 21;
            }
            res[0] = reward;
        }
        return res;
    }

    /**
     * Transition function given state and action. Calls executeOption
     * if the action is actually an option.
     * 
     * @param state Current state.
     * @param action Current action.
     * @param successChance Probability that action will successfully execute.
     * @param goal Goal location for episode.
     * @return Array encoding the reward, taxi, passenger, together, action.
     */
    public int[] transition(int[] state, int action, double successChance, int goal) {
        if (action > 5) {
            return executeOption(state, action - 6, successChance, goal);
        } // Index 6-9 will be the option selects
        TaxiDomain myTaxiDomain = new TaxiDomain();
        Random random = new Random();
        int[][] goodActions = myTaxiDomain.getGoodActions();
        int[] locations = myTaxiDomain.getLocationIDs();
        int[] reverseLocations = myTaxiDomain.getReverseLocationIDs();
        int[] res = new int[5]; // reward, s0, s1, s2, action
        res[0] = -1; // reward
        res[1] = state[0]; // taxi
        res[2] = state[1]; // passenger
        res[3] = state[2]; // together
        res[4] = action; // action
        if (action == 0) { // pick up attempt
            if (state[0] != state[1]) {
                res[0] = -10;
            }
            else {
                res[3] = 1;
            }
        }
        else if (action == 1) { // Drop off attempt.
            if (res[3] == 0) {
                res[0] = -10;
            }
            else {
                res[3] = 0;
                if (state[1] == goal) {
                    res[0] = 20;
                }
            }
        }
        else { // movement action
            if (random.nextDouble() > successChance) { // picks random movement
                action = random.nextInt(4) + 2;
                res[4] = action;
            }
            if (state[2] == 1) { // together
                res[1] = reverseLocations[locations[state[0]] + goodActions[state[0]][action]];
                res[2] = res[1];
            }
            else { // move just the taxi
                res[1] = reverseLocations[locations[state[0]] + goodActions[state[0]][action]];
            }
        }
        return res;
    }

    /**
     * Choose greedy action based on the given state and Q. May choose a random
     * action (but not option) with probability epsilon. Will also check if
     * an option is available before choosing it.
     * 
     * @param state Current state.
     * @param Q The agent's current Q values.
     * @param epsilon Probability of random action.
     * @return Action the agent will take.
     */
    public int greedy(int[] state, double[][][][] Q, double epsilon) {
        Random random = new Random();
        int action = random.nextInt(6);
        if (random.nextDouble() > epsilon) {
            return action;
        }
        for (int i = 0; i < 10; i++) {
            if (Q[state[0]][state[1]][state[2]][i] > Q[state[0]][state[1]][state[2]][action]) {
                action = i;
            }
        }
        return action;
    }
    
    /**
     * Implementation of SARSA(lambda) with options.
     * 
     * @param lambda Eligibility trace.
     * @param alpha Learning rate.
     * @param gamma Discount factor.
     * @param episodes Number of episodes to run.
     * @return New Q values.
     */
    public double[][][][] sarsaLambda(double lambda, double alpha, double gamma, int episodes) { 
        int[] colors = { 0, 4, 20, 23 }; // colored squares
        double[][][][] Q = new double[25][25][2][10];
        // Modified to examine one specific case for assignment write up. Change back.
        for (int i = 1; i == 1; i++) { // passenger start
            for (int j = 3; j == 3; j++) { // goal square
                if (j != i) {
                    for (int k = 0; k == 0; k++) { // Taxi start.
                        Q = new double[25][25][2][10];// Initial Q, all values start at 0.
                        for (int episode = 0; episode < episodes; episode++) {
                            // All values initially 0.
                            double[][][][] eligibilityTrace = new double[25][25][2][10];
                            int rewardSum = 0;
                            int[] state = { colors[k], colors[i], 0 }; // initial s
                            int action = greedy(state, Q, 0.85); // initial a
                            while (true) {
                                int[] statePrime = new int[3];
                                /*
                                 * Gives {reward, new state, action taken}.
                                 */
                                int[] transition = transition(state, action, 0.85, colors[j]);
                                statePrime[0] = transition[1]; // taxi
                                statePrime[1] = transition[2]; // passenger
                                statePrime[2] = transition[3]; // together
                                action = transition[4]; /* We switch from the action attempted
                                                      to the action observed. */
                                int reward = transition[0];
                                rewardSum += reward;
                                /*
                                 * Termination conditions, if they are not
                                 * together and the passenger is at goal.
                                 */
                                if (statePrime[2] == 0 && statePrime[1] == colors[j]) { 
                                    Q[state[0]][state[1]][state[2]][action] += reward;
                                    break;
                                }
                                int actionPrime = 0;
                                actionPrime = greedy(statePrime, Q, 0.85); // Greedily get a'
                                double delta =
                                        round(reward
                                              + Q[statePrime[0]][statePrime[1]][statePrime[2]]
                                                      [actionPrime]
                                              * gamma
                                              - Q[state[0]][state[1]][state[2]][action], 5);
                                eligibilityTrace[state[0]][state[1]][state[2]][action] += 1;
                                for (int taxi = 0; taxi < 25; taxi++) {
                                    for (int passenger = 0; passenger < 25; passenger++) {
                                        for (int together = 0; together < 2; together++) {
                                            for (int actions = 0; actions < 10; actions++) {
                                                Q[taxi][passenger][together][actions] +=
                                                        eligibilityTrace[taxi][passenger]
                                                                [together][actions]
                                                        * alpha
                                                        * delta;
                                                Q[taxi][passenger][together][actions] =
                                                        round(Q[taxi][passenger][together][actions],
                                                              5);
                                                if (state[0] != taxi || state[1] != passenger 
                                                        || state[2] != together 
                                                        || action != actions) {
                                                    eligibilityTrace[taxi][passenger]
                                                            [together][actions] =
                                                    round(eligibilityTrace[taxi][passenger]
                                                            [together][actions]
                                                          * lambda, 10);
                                                }
                                            }
                                        }
                                    }
                                }
                                state = statePrime;
                                action = actionPrime;
                            }
                            rewardAverage[episode] += (double) rewardSum;
                        }
                        return Q;
                    }

                }
            }
        }
        return Q;
    }
}
