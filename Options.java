
/**
 * Contains hard-coded options for reach every square, as well
 * as a method that returns the desired option given a goal location.
 * 
 * @author Seon Kang
 */

public class Options {
        
    /*
     * Hard-coded policies to get to each of the 25 squares. 
     */
    int[][] policies = {
                         { 4, 3, 5, 3, 3, // goal location = 0
                           4, 4, 5, 3, 3,
                           4, 4, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 2, 4, 5, 3, 3, // goal location = 1
                           4, 4, 5, 3, 3,
                           4, 4, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 4, 3, 3, // goal location = 2
                           5, 5, 4, 3, 3,
                           2, 2, 4, 3, 3,
                           4, 4, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 2, 4, 3, // goal location= 3
                           5, 5, 4, 4, 3,
                           2, 2, 4, 4, 3,
                           4, 4, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 2, 2, 4, // goal location = 4
                           5, 5, 4, 4, 4,
                           2, 2, 4, 4, 4,
                           4, 4, 4, 4, 4,
                           4, 4, 4, 4, 4 },

                         { 5, 5, 5, 3, 3, // goal location = 5
                           4, 3, 5, 3, 3,
                           4, 4, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 6
                           2, 2, 5, 3, 3,
                           4, 4, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 7
                           5, 5, 4, 3, 3,
                           2, 2, 4, 3, 3,
                           4, 4, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 5, 5, 3, // goal location = 8
                           5, 5, 2, 3, 3,
                           2, 2, 4, 4, 3,
                           4, 4, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 5, 5, 5, // goal location = 9
                           5, 5, 2, 2, 2,
                           2, 2, 4, 4, 4,
                           4, 4, 4, 4, 4,
                           4, 4, 4, 4, 4 },

                         { 5, 5, 5, 3, 3, // goal location = 10
                           5, 3, 5, 3, 3,
                           3, 3, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 11
                           5, 5, 5, 3, 3,
                           2, 3, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 12
                           5, 5, 5, 3, 3,
                           2, 2, 3, 3, 3,
                           4, 4, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 5, 5, 3, // goal location = 13
                           5, 5, 5, 5, 3,
                           2, 2, 2, 3, 3,
                           4, 4, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 5, 5, 5, // goal location = 14
                           5, 5, 5, 5, 5,
                           2, 2, 2, 2, 3,
                           4, 4, 4, 4, 4,
                           4, 4, 4, 4, 4 },

                         { 5, 5, 5, 3, 3, // goal location = 15
                           5, 5, 5, 3, 3,
                           5, 3, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 16
                           5, 5, 5, 3, 3,
                           2, 5, 3, 3, 3,
                           4, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 17
                           5, 5, 5, 3, 3,
                           2, 5, 5, 3, 3,
                           4, 2, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 5, 5, 3, // goal location = 18
                           5, 5, 5, 5, 3,
                           2, 2, 2, 5, 3,
                           4, 4, 4, 4, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 5, 5, 5, // goal location = 19
                           5, 5, 5, 5, 5,
                           2, 2, 2, 5, 5,
                           4, 4, 4, 2, 5,
                           4, 4, 4, 4, 4 },

                         { 5, 5, 5, 3, 3, // goal location = 20
                           5, 5, 5, 3, 3,
                           5, 3, 3, 3, 3,
                           5, 4, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 21
                           5, 5, 5, 3, 3,
                           2, 5, 3, 3, 3,
                           4, 5, 3, 4, 3,
                           4, 4, 3, 4, 3 },

                         { 5, 5, 5, 3, 3, // goal location = 22
                           5, 5, 5, 3, 3,
                           2, 5, 5, 3, 3,
                           4, 2, 5, 4, 3,
                           4, 2, 4, 4, 3 },

                         { 5, 5, 5, 5, 3, // goal location = 23
                           5, 5, 5, 5, 3,
                           2, 2, 2, 5, 3,
                           4, 4, 4, 5, 3,
                           4, 4, 4, 4, 3 },

                         { 5, 5, 5, 5, 5, // goal location = 24
                           5, 5, 5, 5, 5,
                           2, 2, 2, 5, 5,
                           4, 4, 4, 5, 5,
                           4, 4, 4, 2, 4 }
    };
    
    /**
     * Returns the option that leads to goalLocation.
     * 
     * @param goalLocation the desired goal.
     * @return Option that leads to goal location.
     */
    
    public int[] getPolicyByGoal(int goalLocation) { 
                return policies[goalLocation];
    }

        
}
