import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Class that holds utility method for dealing with matrices.
 * 
 * @author Seon Kang
 */

public class MatrixUtility { 

    /**
     * Method that rounds to places.
     * 
     * @param value Number you want to round.
     * @param places The number of places you want to round to.
     * @return value rounded to places.
     */
    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Prints out the elements of a matrix.
     * 
     * @param matrix The matrix to be printed.
     */
    public void print(double[][] matrix) {
        int row = matrix.length;
        int column = matrix[0].length;
        for (int i = 0; i < row; i++) {
            String rowString = "";
            for (int j = 0; j < column; j++) {
                rowString += round(matrix[i][j], 5) + " ";
            }
            System.out.println(rowString);
        }
    }

    /**
     * Returns an identity matrix of requested size.
     * 
     * @param size Size of the desired identity matrix.
     * @return Identity matrix with requested size.
     */
    public double[][] identity(int size) {
        double[][] identity = new double[size][size];
        for (int i = 0; i < size; i++) {
            identity[i][i] = 1;
        }
        return identity;
    }

    /**
     * Fills row x column matrix with samples from a distribution with desired mean
     * and standard deviation.
     * 
     * @param row Number of rows in final matrix.
     * @param column Number of columns in final matrix.
     * @param mean Mean of normal distribution.
     * @param sigma Standard deviation of normal distribution.
     * @return row x column matrix with values drawn from normal distribution.
     */
    public double[][] fill(int row, int column, double mean, double sigma) {
        double[][] normalDistributionMatrix = new double[row][column];
        NormalDistribution myDist = new NormalDistribution(0, sigma);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                normalDistributionMatrix[i][j] = myDist.sample();
            }
        }
        return normalDistributionMatrix;
    }

    /**
     * Fills row x column matrix with value.
     * 
     * @param row Number of rows in output matrix.
     * @param column Number of columns in output matrix.
     * @param value The value used to fill every element of output matrix.
     * @return row x column matrix with each element equal to value.
     */
    public double[][] fill(int row, int column, double value) {
        double[][] outputMatrix = new double[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                outputMatrix[i][j] = value;
            }
        }
        return outputMatrix;
    }

    /**
     * Adds two matrices together.
     * 
     * @param matrixA First input matrix.
     * @param matrixB Second input matrix.
     * @return Returns the sum of the input matrices.
     */
    public double[][] add(double[][] matrixA, double[][] matrixB) { 
        int row = matrixA.length;
        int column = matrixA[0].length;
        double[][] matrixC = new double[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrixC[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return matrixC;
    }

    /**
     * Subtracts the second matrix from the first.
     * 
     * @param matrixA First input matrix.
     * @param matrixB Second input matrix.
     * @return Second matrix subtracted from the first matrix.
     */
    public double[][] subtract(double[][] matrixA, double[][] matrixB) { 
        int row = matrixA.length;
        int column = matrixA[0].length;
        double[][] matrixC = new double[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrixC[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }
        return matrixC;
    }

    /**
     * Multiplies two matrices. Returns null if the multiplication isn't possible.
     * 
     * @param matrixA The first input matrix.
     * @param matrixB The second input matrix.
     * @return Product of A and B if possible, null if impossible.
     */
    public double[][] multiply(double[][] matrixA, double[][] matrixB) {
        int ra = matrixA.length;
        int ca = matrixA[0].length;
        int rb = matrixB.length;
        int cb = matrixB[0].length;
        double[][] matrixC = new double[ra][cb];
        if (ca != rb) {
            return null; // return null if the multiplication isn't possible
        }
        for (int i = 0; i < ra; i++) {
            for (int j = 0; j < cb; j++) {
                double entry = 0;
                for (int k = 0; k < ca; k++) {
                    entry += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = entry;
            }
        }
        return matrixC;
    }

    /**
     * Transposes a matrix.
     * 
     * @param matrixA Matrix to be transposed.
     * @return The transpose of the input matrix.
     */
    public double[][] transpose(double[][] matrixA) {
        int row = matrixA.length;
        int column = matrixA[0].length;
        double[][] transpose = new double[column][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                transpose[j][i] = matrixA[i][j];
            }
        }
        return transpose;
    }

    /**
     * Multiplies a matrix by a scalar.
     * 
     * @param matrixA Matrix input.
     * @param scalar The scalar input.
     * @return Matrix A with each element multiplied by scalar.
     */
    public double[][] scalar(double[][] matrixA, double scalar) {
        int row = matrixA.length;
        int column = matrixA[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrixA[i][j] = matrixA[i][j] * scalar;
            }
        }
        return matrixA;
    }

    /**
     * Finds the determinant of a square matrix.
     * 
     * @param matrixA The input matrix.
     * @return The determinant of the input matrix.
     */
    public double determinant(double[][] matrixA) {
        int len = matrixA.length;
        double det = 0;
        if (len == 1) {
            return matrixA[0][0];
        }
        if (len == 2) {
            return matrixA[0][0] * matrixA[1][1] - matrixA[0][1] * matrixA[1][0];
        }
        else {
            int sign = 1;
            for (int i = 0; i < len; i++) {
                det += sign * matrixA[0][i] * determinant(minor(0, i, matrixA));
                sign = sign * (-1);
            }
        }
        return det;
    }

    /**
     * Returns the minor for (row, column, A).
     * 
     * @param row The row to be excluded.
     * @param column The column to be excluded.
     * @param matrix The input matrix.
     * @return Returns the minor matrix, which is matrix A excluding row and column. 
     */
    public double[][] minor(int row, int column, double[][] matrix) { 
        int len = matrix.length;
        double[][] minor = new double[len - 1][len - 1];
        int rowIncrement = 0;
        int colIncrement = 0;
        for (int i = 0; i < len; i++) { // row
            colIncrement = 0;
            for (int j = 0; j < len; j++) { // column
                if (i != row && j != column) {
                    minor[rowIncrement][colIncrement] = matrix[i][j];
                    colIncrement++;
                }
            }
            if (i != row) {
                rowIncrement++;
            }
        }
        return minor;
    }

    /**
     * Inverts a square matrix.
     * 
     * @param matrixA The matrix to be inverted.
     * @return The inverse of matrix A.
     */
    public double[][] invert(double[][] matrixA) {
        int length = matrixA.length;
        double[][] inverse = new double[length][length];
        // Matrix of minors
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                inverse[i][j] = determinant(minor(i, j, matrixA));
            }
        }
        // Matrix of co-factors
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                inverse[i][j] = inverse[i][j] * (1 + (-2 * ((i + j) % 2)));
            }
        }
        // transpose
        inverse = transpose(inverse);
        // divide by determinant
        inverse = scalar(inverse, 1.00 / (determinant(matrixA)));
        return inverse;
    }

    /**
     *  Return a copy of the input matrix.
     *  
     * @param matrixA The matrix to be copied.
     * @return Copy of matrix A.
     */
    public double[][] copy(double[][] matrixA) {
        int row = matrixA.length;
        int column = matrixA[0].length;
        double[][] copy = new double[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                copy[i][j] = matrixA[i][j];
            }
        }
        return copy;
    }

}
