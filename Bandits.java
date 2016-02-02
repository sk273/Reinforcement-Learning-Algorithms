import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Implementations of epsilon-greedy and UCB1 algorithms for the n-armed bandit problem.
 *
 * @author Seon Kang
 *
 */

public class Bandits {

    /**
     * Constructs an arm of the bandit. Arms have a normal distribution associated with it.
     */
    
    public class Arm {
        double mu; // Mean of arm's normal distribution.
        double sigma; // Standard deviation of arm's normal distribution
        double estimate; // Current estimate of arm's value.
        int timesPulled; // Keeps track of the number of times the arm was pulled.

        public Arm(double myMu, double mySigma) {
            mu = myMu;
            sigma = mySigma;
        }

        public double getMu() {
            return mu;
        }

        public double getSigma() {
            return sigma;
        }

        public double getEstimate() {
            return estimate;
        }

        public void setEstimate(double increment) {
            estimate = increment;
        }

        public int getTimesPulled() {
            return timesPulled;
        }

        public void incrementTimesPulled(int increment) {
            timesPulled += increment;
        }

        public double getUcb(int round) { // Calculates the upper confidence bound.
            return estimate + Math.sqrt(2 * Math.log(round) / timesPulled);
        }

    }

    /**
     * Picks a random arm from a list.
     * 
     * @param arms List of arms from which random one is chosen.
     * @return Random arm from list of arms.
     */
    public Arm chooseRandomArm(ArrayList<Arm> arms) {
        Random random = new Random();
        int chosen = random.nextInt(arms.size());
        return arms.get(chosen);
    }
    
    /**
     * Chooses arm with the maximum estimated value. Random tie-breaker.
     * 
     * @param arms List of arms from which max is chosen.
     * @return Arm with maximum estimated value.
     */
    public Arm chooseMax(ArrayList<Arm> arms) {
        Random random = new Random();
        Arm max = arms.get(random.nextInt(arms.size()));
        for (Arm a : arms) {
            if (a.getEstimate() > max.getEstimate()) {
                max = a;
            }
        }
        return max;
    }

    /**
     * Chooses arm with the maximum UCB value. Random tie-breaker.
     * 
     * @param arms List of arms from which max UCB is chosen.
     * @param round The current round of arm pulling.
     * @return Arm with maximum estimated UCB.
     */
    public Arm chooseMaxUcb(ArrayList<Arm> arms, int round) {
        Random random = new Random();
        Arm max = arms.get(random.nextInt(arms.size()));
        for (Arm a : arms) {
            if (a.getUcb(round) > max.getUcb(round)) {
                max = a;
            }
        }
        return max;
    }

    /**
     * Implements an epsilon-greedy algorithm for the n-armed bandit problem. Algorithm chooses the
     * next arm greedily
     * 
     * @param arms Arms on the bandit.
     * @param init Initial value estimates for every arm.
     * @param epsilon Chance of pulling random arm.
     * @param rounds Number of pulls / rounds.
     * @return Total sum of rewards.
     */
    public double epsilonGreedy(ArrayList<Arm> arms, double init, double epsilon, int rounds) {
        Random myRandom = new Random();
        double rewardSum = 0;
        for (Arm a: arms) {
            a.setEstimate(init);
        }
        for (int i = 0; i < rounds; i++) {
            Arm pull; // The arm that will be pulled.
            if (myRandom.nextDouble() < epsilon) {
                pull = chooseRandomArm(arms);
            }
            else {
                pull = chooseMax(arms);
            }
            NormalDistribution myDist = new NormalDistribution(pull.getMu(), pull.getSigma());
            double reward = myDist.sample(); // From the distribution.
            rewardSum += reward;
            // Update estimate according to what we just observed.
            pull.setEstimate((pull.getEstimate() * (i + 1) + reward) / (i + 2));
        }
        return rewardSum;
    }


    /**
     * UCB1 algorithm for the n-armed bandit problem.
     * 
     * @param arms Arms on the bandit.
     * @param rounds Number of pulls / rounds.
     * @return Total sum of rewards.
     */
    public double ucb1(ArrayList<Arm> arms, int rounds) {
        long rewardSum = 0;
        for (Arm a: arms) {
            NormalDistribution armDistribution = new NormalDistribution(a.getMu(), a.getSigma());
            a.setEstimate(armDistribution.sample());
            a.incrementTimesPulled(1);
            rewardSum += a.getEstimate();
        }
        // We start at arms.size() round because we have pulled each once already.
        for (int round = arms.size(); round < rounds; round++) {
            Arm pull = chooseMaxUcb(arms, round);
            NormalDistribution myDist = new NormalDistribution(pull.getMu(), pull.getSigma());
            double reward = myDist.sample(); // From the distribution
            rewardSum += reward;
            // Update estimate according to what we just observed.
            pull.setEstimate((pull.getEstimate() * (round + 1) + reward) / (round + 2)); 
            pull.incrementTimesPulled(1); // Increment pull count.
        }
        return rewardSum;
    }

}
