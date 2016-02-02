import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Dynamic programming with prioritized sweeping for the taxi domain.
 * 
 * @author Seon Kang
 *
 */
public class DynamicProgramming {

    /**
     * Runs a particular instance of taxi domain.
     * 
     * @param args Unused.
     */
    public static void main(String[] args) {
        int[] colors = {0, 4, 20, 23}; // Location of colored squares.
        DynamicProgramming myDynaProgram = new DynamicProgramming();
        double epsilon = 0.0001;
        int[][][] policy1 = myDynaProgram.prioritizedSweeping(epsilon, 0);
        int[][][] policy2 = myDynaProgram.prioritizedSweeping(epsilon, 4);
        int[][][] policy3 = myDynaProgram.prioritizedSweeping(epsilon, 20);
        int[][][] policy4 = myDynaProgram.prioritizedSweeping(epsilon, 23);
        ArrayList<int[][][]> policies = new ArrayList<int[][][]>();
        policies.add(policy1);
        policies.add(policy2);
        policies.add(policy3);
        policies.add(policy4);
        for (int i = 0 ; i < 4 ; i++) { // Passenger start
            for (int j = 0; j < 4 ; j++) { // Goal square
                if (j != i) {
                    for (int k = 0; k < 4; k++) { // Taxi start
                        System.out.println("taxi: " + colors[k] + " passenger: " 
                                + colors[i] + " goal: " + colors[j]);
                        myDynaProgram.simulate(policies.get(j), colors[i], colors[j], colors[k]);
                    }
                }       
            }
        }
    }

    /**
     * Simulates the agent interacting with the domain.
     * 
     * @param policy Control policy for taxi.
     * @param passenger Location of passenger.
     * @param goal Location of goal.
     * @param taxi Location of taxi.
     */
    public void simulate(int[][][] policy, int passenger, int goal, int taxi) {
        int[] state = new int[3];
        state[0] = taxi;
        state[1] = passenger;
        state[2] = 0;
        int steps = 0;
        while (steps < 25) { // Edit out this magic number.
            if (state[1] == goal && state[2] == 0) {
                break;
            }
            steps++;
            state = transition(state, policy[state[0]][state[1]][state[2]]);
        }
        System.out.println(steps);
    }

    /**
     * Helper function for copying V.
     * 
     * @param input Array to be copied.
     * @return Copy of input.
     */
    public double[][][] copy(double[][][] input) {
        double[][][] res = new double[25][25][2];
        for (int i = 0 ; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                for (int k = 0; k < 2; k++) {
                    res[i][j][k] = input[i][j][k];
                }
            }
        }
        return res;
    }

    /**
     * Takes the state and action and gives you the next state.
     * 
     * @param state Current state.
     * @param action Action taken.
     * @return An array with information about the state of the taxi, passenger, 
     *     and whether or not the passenger is in the taxi.
     */
    public int[] transition(int[] state, int action) {
        Sarsa mySarsa = new Sarsa();
        int[] transition = mySarsa.transition(state, action, 1, -1);
        int[] res = new int[3];
        res[0] = transition[1]; // Taxi 
        res[1] = transition[2]; // passenger
        res[2] = transition[3]; // Together (passenger in taxi?)
        return res;
    }

    /**
     * Returns the reward given the state, action, and goal.
     * 
     * @param state Current state.
     * @param action Action taken.
     * @param goal Goal for episode.
     * @return Reward.
     */
    public int reward(int[] state, int action, int goal) {
        Sarsa mySarsa = new Sarsa();
        int[] transition = mySarsa.transition(state, action, 1, goal);
        return transition[0];
    }

    /**
     * Simple comparator for double arrays that compare sizes of the first element.
     * 
     * @author Seon Kang
     *
     */
    public class Priority implements Comparator<double[]> {
        /**
         * Compares sizes of the first element.
         */
        public int compare(double[] arrayA, double[] arrayB) {
            if (arrayA[0] > arrayB[0]) {
                return 1;
            }
            else if (arrayA[0] < arrayB[0]) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    /**
     * Alternating policy and value iteration and returns the policy.
     * 
     * @param epsilon  Determines how small changes have to be
     *     in order to stop iterating.
     * @param goal Location of goal.
     * @return The current optimal policy.
     */
    public int[][][] prioritizedSweeping(double epsilon, int goal) {
        Random random = new Random();
        double[][][] V = new double[25][25][2]; // Initial Q, all values start at 0.
        int[][][] policy = new int[25][25][2]; /* Initial policy, we will 
                                                populate with random actions. */
        for (int i = 0 ; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                for (int k = 0; k < 2; k++) {
                    if (k == 1 && i != j) { // Not allowed.
                        continue;
                    }
                    else if (k == 0 && j == goal) { // Not allowed.
                        continue;
                    }
                    policy[i][j][k] = random.nextInt(6);
                }
            }
        }
        V[goal][goal][0] = 25; // 
        int count = 0;
        double delta = 1;
        PriorityQueue<double[]> pq = new PriorityQueue<double[]>(100, new Priority());
        while (true) {
            boolean[][][] inQ = new boolean[25][25][2];
            delta = 0; // Keep max delta.
            for (int i = 0 ; i < 25; i++) { // taxi
                for (int j = 0; j < 25; j++) { // passenger
                    for (int k = 0; k < 2; k++) { // together
                        if (k == 1 && i != j) { // Not allowed.
                            continue;
                        }
                        else if (k == 0 && j == goal) { // Terminal state check.
                            continue;
                        }
                        int[] state = new int[3];
                        state[0] = i;
                        state[1] = j;
                        state[2] = k;
                        int[] next = transition(state, policy[i][j][k]);
                        double v = V[next[0]][next[1]][next[2]];
                        if (next[0] == goal && next[1] == goal && next[2] == 0) {
                            v = 0;
                        }
                        else if (next[0] == i && next[1] == j && next[2] == k) {
                            v = 0;
                        }
                        double curValue = Math.abs( (reward(state, policy[i][j][k], goal)  
                                + v * 0.85) 
                                                    - V[i][j][k]) * 0.85;
                        if (true) {
                            double[] entry = new double[4];
                            entry[0] = curValue;
                            entry[1] = i;
                            entry[2] = j;
                            entry[3] = k;
                            pq.offer(entry);
                            inQ[i][j][k] = true;
                        }
                        while (pq.size() > 0) {
                            double[] top = pq.poll();
                            int i1 = (int) top[1];
                            int j1 = (int) top[2];
                            int k1 = (int) top[3];
                            int[] cur = new int[3];
                            cur[0] = i1;
                            cur[1] = j1;
                            cur[2] = k1;
                            next = transition(cur, policy[i1][j1][k1]);
                            v = V[next[0]][next[1]][next[2]];
                            if (next[0] == goal && next[1] == goal && next[2] == 0) {
                                v = 0;
                            }
                            // Disregard transitions that do nothing.
                            else if (next[0] == i1 && next[1] == j1 && next[2] == k1) {
                                v = 0;
                            }
                            if (next[0] != i1 ||  next[1] != j1 || next[2] != k1) {
                                delta = Math.max(delta, Math.abs((reward(cur, 
                                                                         policy[i1][j1][k1], goal)  
                                        + v * 0.85) - V[i1][j1][k1]));
                            }
                            V[i1][j1][k1] = reward(cur, policy[i1][j1][k1], goal)  
                                    + v * 0.85; // This is the update.
                            count++;

                            /* Comment out this code below + get rid of 
                             * the p requirement to get vanilla DP. */

                            // Now we see if we should add more to the priority queue.
                            for (int n = 0; n < 6; n++) { 
                                int[] candidate = transition(cur, n);
                                if (candidate[2] == 0 && candidate[1] == goal) { // Terminal state.
                                    continue;
                                }
                                int[] check = transition(candidate, 
                                                         policy[candidate[0]][candidate[1]][candidate[2]]);
                                if ((check[0] == cur[0] 
                                        && check[1] == cur[1] && check[2] == cur[2]) 
                                        && (candidate[0] != cur[0] || candidate[1] 
                                                != cur[1] || candidate[2] != cur[2] ) ) {
                                    curValue = Math.abs( (reward(candidate, 
                                                                 policy[candidate[0]][candidate[1]][candidate[2]], 
                                                                 goal)  
                                            + V[cur[0]][cur[1]][cur[2]] * 0.85) 
                                                         - V[candidate[0]][candidate[1]][candidate[2]]) 
                                            * 0.85;
                                    if (curValue > 0.0001 
                                            && inQ[candidate[0]][candidate[1]][candidate[2]] 
                                                    == false) {
                                        double[] entry = new double[4];
                                        entry[0] = curValue;
                                        entry[1] = candidate[0];
                                        entry[2] = candidate[1];
                                        entry[3] = candidate[2];
                                        pq.offer(entry);
                                        inQ[candidate[0]][candidate[1]][candidate[2]] = true;
                                    }
                                }
                            }

                            // inQ[i1][j1][k1] = false;

                            /* Comment out the above for vanilla dynamic programming. */
                        }
                    }
                }
            }
            for (int i = 0 ; i < 25; i++) { // Taxi
                for (int j = 0; j < 25; j++) { // Passenger
                    for (int k = 0; k < 2; k++) { // Together
                        if (k == 1 && i != j) {
                            continue;
                        }
                        else if (k == 0 && j == goal) {
                            continue;
                        }
                        int greedy = random.nextInt(6);
                        int[] state = new int[3];
                        state[0] = i;
                        state[1] = j;
                        state[2] = k;
                        for (int a = 0; a < 6; a++) {
                            int[] nextAction = transition(state, a);
                            int[] nextGreedyAction = transition(state, greedy);
                            if (V[nextAction[0]][nextAction[1]][nextAction[2]] 
                                    > V[nextGreedyAction[0]][nextGreedyAction[1]][nextGreedyAction[2]]) {
                                greedy = a;
                            }
                        }
                        policy[i][j][k] = greedy;
                    }
                }
            }
            if (delta < epsilon) {
                System.out.println(count);
                break;
            }
        }
        return policy;
    }
}
