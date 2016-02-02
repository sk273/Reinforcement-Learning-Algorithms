
/**
 * Hard-coded sensor readings for the 5x5 taxi domain.
 * 
 * @author Seon Kang
 *
 */
public class SensorReadings {
        
        /*
         * First array of reading[][] is the top left square. 
         * 
         * Sensors readings for each square are in the form:
         * {up, right, down, left}
         * 
         * 1 means there is a wall, 0 means there isn't.
         */
        int[][] readings = {{1,0,0,1},{1,1,0,0},{1,0,0,1},{1,0,0,0},{1,1,0,0},
                            {0,0,0,1},{0,1,0,0},{0,0,0,1},{0,0,0,0},{0,1,0,0},
                            {0,0,0,1},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,1,0,0},
                            {0,1,0,1},{0,0,0,1},{0,1,0,0},{0,0,1,0},{0,1,0,0},
                            {0,1,1,1},{0,0,1,1},{0,1,1,0},{0,0,1,1},{0,1,1,0}};
}
