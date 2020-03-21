package sample;

/**
 * @author Junqi Xu
 * @version 2.0 Last updated on 3/19/2020
 * Markov Matrix is a special form of matrix. It has the following properties:
 * 1. Every columns add up to 1
 * 2. It is a square matrix
 * 3. It can be used to describe Markov chain
 * Let A be a n*n markov matrix, and X0 be a n*1 matrix. X(n+1) = A*X(n)
 * If the column of X0 also adds up to 1, then Xn where n is infinity is called the steady state vector.
 */
public class markovMatrix{
    private static boolean testMarkov(double[][]matrix) {
        for (int i = 0; i < matrix[0].length; i++) {
            double sum = 0.0;
            for (int j = 0; j < matrix.length; j++) {
                sum+=matrix[j][i];
            }
            if(sum != 1) {
                return false;
            }
        }
        return true;
    }

    public static String getSteadyState(double[][] matrix) throws NotMarkovMatrixException, MatrixDoesNotMatchException {
        if (!testMarkov(matrix)) {
            throw new NotMarkovMatrixException("This is not a proper markov matrix");
        }
        double[][] identityMatrix = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < identityMatrix.length; i++) {
            for (int j = 0; j < identityMatrix[0].length; j++) {
                if (i == j) {
                    identityMatrix[i][j] = 1;
                } else {
                    identityMatrix[i][j] = 0;
                }
            }
        }
        double[] zeroVectors = new double[matrix.length];
        double[][] result = Matrix.subtract(matrix, identityMatrix);
        return Matrix.solve(result, zeroVectors) + "\n" + "Wait, Sorry about that. But you should " +
                "divide by the length of this vector by yourself";
    }
}
