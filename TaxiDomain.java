/**
 * Holds methods for interacting with the 5x5 taxi domain.
 * 
 * @author Seon Kang
 *
 */

public class TaxiDomain {

    /*
     * Location IDs are different. Are different from locations.
     * 
     * Locations simply go from 0 to 24 starting from the upper left corner.
     * Location IDs start at the bottom left at 0 and increase by 10 going
     * to the right and 1 going up. Location IDs are meant to make transitions
     * calculations easier.
     */
    int[] locationIDs = { 4, 14, 24, 34, 44,
                          3, 13, 23, 33, 43,
                          2, 12, 22, 32, 42,
                          1, 11, 21, 31, 41,
                          0, 10, 20, 30, 40 };

    /*
     * The possible actions are:
     * - move up, down, right, left
     * - pick up passenger, drop off passenger
     * 
     * Good actions are the ones that are available at a given location.
     * Element arrays are in the following form:
     * {Pick up, drop off, right, left, up, down}
     * Start with {100, -100, 10, -10, 1, -1}, replace 0 if that move is not allowed.
     */
    int[][] goodActions =
            { { 100, -100, 10, 0, 0, -1 }, { 100, -100, 0, -10, 0, -1 },
              { 100, -100, 10, 0, 0, -1 }, { 100, -100, 10, -10, 0, -1 },
              { 100, -100, 0, -10, 0, -1 },
              { 100, -100, 10, 0, 1, -1 }, { 100, -100, 0, -10, 1, -1 },
              { 100, -100, 10, 0, 1, -1 }, { 100, -100, 10, -10, -1, 1 },
              { 100, -100, 0, -10, 1, -1 },
              { 100, -100, 10, 0, 1, -1 }, { 100, -100, 10, -10, 1, -1 },
              { 100, -100, 10, -10, 1, -1 }, { 100, -100, 10, -10, 1, -1 },
              { 100, -100, 0, -10, 1, -1 },
              { 100, -100, 0, 0, 1, -1 }, { 100, -100, 10, 0, 1, -1 }, { 100, -100, 0, -10, 1, -1 },
              { 100, -100, 10, 0, 1, -1 }, { 100, -100, 0, -10, 1, -1 },
              { 100, -100, 0, 0, 1, 0 }, { 100, -100, 10, 0, 1, 0 }, { 100, -100, 0, -10, 1, 0 },
              { 100, -100, 10, 0, 1, 0 }, { 100, -100, 0, -10, 1, 0 } };
        
    public int[] getLocationIDs() {
        return locationIDs;
    }

    public int[][] getGoodActions() {
        return goodActions;
    }
        
    /**
     * Provides an array that can be used to look up locations
     * at the index of the ID.
     * 
     * @return An array that maps location IDs to locations
     */
    public int[] getReverseLocationIDs() {
        int[] res = new int[45];
        for (int i = 0 ; i < 25; i++) {
            res[locationIDs[i]] = i;
        }
        return res;
    }
}
