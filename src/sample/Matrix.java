package sample;


/**
 * This program is used to calculate the matrix
 * @author Jacky Xu
 * @version Last update 3/18/2020
 */
public class Matrix {

    /**
     * Addition of two matrices
     * @param first the first matrix
     * @param second the second matrix
     * @return the result
     */
    public static double[][] sum(double[][] first, double[][] second) throws MatrixDoesNotMatchException{
        double[][] result = new double[first.length][first[0].length];
        if(!checkForSameSize(first, second)) {
            throw new MatrixDoesNotMatchException("Matrix does not Match. They should be the same size");
        } else {
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    result[i][j] = first[i][j] + second[i][j];
                }
            }
        }
        return result;
    }

    /**
     * Subtraction of two matrices
     * @param first the first matrix
     * @param second the second matrix
     * @return the result
     */
    public static double[][] subtract(double[][] first, double[][] second) throws MatrixDoesNotMatchException{
        double[][] result = new double[first.length][first[0].length];
        if(!checkForSameSize(first, second)) {
            throw new MatrixDoesNotMatchException("Matrix does not match. They should be the same size");
        } else {
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    result[i][j] = first[i][j] - second[i][j];
                }
            }
        }
        return result;
    }

    /**
     * Multiplication of two matrices
     * @param first the first matrix
     * @param second the second matrix
     * @return the result
     */
    public static double[][] multiply(double[][] first, double[][] second) throws MatrixDoesNotMatchException{
        double[][] result = new double[first.length][second[0].length];
        if (first[0].length != second.length) {
            throw new MatrixDoesNotMatchException("Matrix A's # of columns should be the sames Matrix B's # of rows");
        } else {
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    for (int k = 0; k < second.length; k++) { // second.length should equal to first[0].length
                        result[i][j] += first[i][k] * second[k][j];
                    }
                }
            }
        }
        return result;
    }

    /*
     * Get the row echelon form of a matrix
     * @return the 2-D array that represents a row echelon form of a matrix
     */
    public static double[][] ref(double[][] matrix) {
        int min = Math.min(matrix.length, matrix[0].length);
        for (int i = 0; i < min; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                if(matrix[i][i] != 0.0 && matrix[j][i] != 0.0) {
                    double coeffcient = matrix[j][i] / matrix[i][i];
                    double[] temp = rowOperation(getRow(i, matrix), getRow(j, matrix), coeffcient);
                    matrix[j] = temp;
                } else if(matrix[i][i] == 0) {
                    rowExchange(matrix, i, j);
                }
            }
        }

        // check for zero rows, if found, then put it to the last row
        for(int i = 0; i < matrix.length; i++) {
            if(zeroRow(getRow(i,matrix))) {
                int index = i + 1;
                while(index < matrix.length) {
                    if(!zeroRow(getRow(index,matrix))) {
                        rowExchange(matrix,index,i);
                        break;
                    }
                    index++;
                }
            }
        }
        return matrix;
    }

    /**
     * Return the row reduced echelon form of a matrix
     * @return the rref;
     */
     public static double[][] rref(double[][] matrix) {
        double[][] rref = Matrix.ref(matrix);
        int min = Math.min(rref.length, rref[0].length); // Now we need to think whether row is greater or column is greater
         // When row number is greater or equal to column number
         if (min == rref[0].length) {
             for (int i = min - 1; i > 0; i--) {
                 for (int j = 0; j < i; j++) {
                     if (rref[j + 1][i] != 0.0 && rref[j][i] != 0.0) {
                         double coefficient = rref[j][i] / rref[j + 1][i];
                         double[] temp = rowOperation(getRow(j + 1, rref), getRow(j, rref), coefficient);
                         rref[j] = temp;
                     }
                 }
             }
         } else {
             // This step is to reduce the part of entries that are in those columns, which are greater than the row length
             // For example, 2 * 4 matrix, this step eliminate the entries above pivot in column 2 - 3 (inclusive)
             for (int i = rref[0].length - 1; i >= rref.length; i--) {
                 for (int j = 0; j < rref.length - 1; j++) {
                     if (rref[j + 1][i] != 0.0 && rref[j][i] != 0.0) {
                         double coefficient = rref[j][i] / rref[j + 1][i];
                         double[] temp = rowOperation(getRow(j + 1, rref), getRow(j, rref), coefficient);
                         rref[j] = temp;
                     }
                 }
             }
             // The step is to row reduce the rest of the entries that we are supposed to reduce
             for (int i = rref.length - 1; i > 0; i--) {
                 for (int j = 0; j < i; j++) {
                     if (rref[j + 1][i] != 0.0 && rref[j][i] != 0.0) {
                         double coefficient = rref[j][i] / rref[j + 1][i];
                         double[] temp = rowOperation(getRow(j + 1, rref), getRow(j, rref), coefficient);
                         rref[j] = temp;
                     }
                 }
             }
         }
         // Simplifies every pivot to 1
        for(int i = 0; i < rref.length; i++) {
            double pivot = getPivot(i,rref);
            if(pivot != 0 )
                rref[i] = rowOperation_2(rref[i],pivot);
        }

         // Check if two rows are multiple for each other and perform row operation to them if they are.
         // Temporarily did not find any situation that needs this method
        for(int i = 0; i < rref.length - 1; i++) {
            if(checkRow(rref[i],rref[i+1])) {
                double coefficient = getPivot(i+1,rref) / getPivot(i,rref);
                rref[i+1] = rowOperation(getRow(i,rref), getRow(i+1,rref), coefficient);
            }
        }
        return rref;
    }

    /**
     * Solve the equation Ax = b
     * @param b b
     * @return the solution
     */
    public static String solve(double[][] matrix, double[] b) {
        String solution = "";
        if (matrix.length != b.length) {
            return "There is no solution!";
        } else {
            double[][]augmented = augmented(matrix, b);
            augmented = Matrix.rref(augmented);
            if (!haveSolution(augmented)) {
                return "There is no solution! Since[000...0|1] exists";
            } else {
                double[] pSolution = new double[matrix[0].length];
                int[] freeColumn = getFreeColumn(getCoefficientMatrix(augmented));

                //The situation where there is no free variables
                if(freeColumn.length == 0) {
                    for (int i = 0; i < pSolution.length; i++) {
                        pSolution[i] = augmented[i][augmented[0].length-1];
                    }
                    return getSolutionSet(pSolution);
                } else {
                    solution += "The complete solution will be Xnull + Xp \n";
                    solution += "----------------------------------------\n";

                    // Deal with the situation with free variables
                    for (int i = 0; i < freeColumn.length; i++) {
                        double[] freeSolution = new double[matrix[0].length];
                        int free = freeColumn[i];
                        freeSolution[free] = 1;
                        for (int row = 0; row < augmented.length; row++) {
                            int pivotPlace = getPivotColumnNumber(row, augmented);
                            if (pivotPlace != -1) {
                                freeSolution[pivotPlace] = -augmented[row][free];
                            }
                        }
                        solution += "Xnull: with X" + (free+1) + " as free variables\n"
                                + getSolutionSet(freeSolution);
                    }
                    double[] particular = new double[matrix[0].length];
                    for (int row = 0; row < augmented.length; row++) {
                        int pivotPlace = getPivotColumnNumber(row, augmented);
                        if (pivotPlace != -1) {
                            particular[pivotPlace] = augmented[row][augmented[0].length - 1];
                        }
                    }
                    solution += "Xp: \n" + getSolutionSet(particular);
                }
                return solution;
            }
        }
    }

    /**
     * Find the eigenvalues of a given matrix. 2 * 2 only
     * @param matrix input matrix
     * @return eigenvalue in a array
     */
    public static double[] getEigenvalue(double[][] matrix) throws MatrixDoesNotMatchException{
        if (matrix.length > 2 || matrix[0].length > 2) {
            throw new UnsupportedOperationException("Matrices other than 2 * 2 is not supported");
        }
            double b = - (matrix[0][0] + matrix[1][1]);
            double c = Matrix.determinant(matrix);
            return solveLinearEquation(1.0, b, c);
        }

       /**
          * This method is used to get the inverse of a matrix
          * @return the inverse
          */
    public static double[][] getInverse(double[][] matrix) {
        if(matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Not correct dimension");
        }
        double[][] temp = new double[matrix.length][matrix.length * 2];
        for(int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length / 2; j++) {
                temp[i][j] = matrix[i][j];
            }
        }
        for (int i = 0; i < temp.length; i++) {
            for (int j = temp[0].length / 2; j < temp[0].length; j++) {
                if(i == j - temp[0].length / 2) {
                    temp[i][j] = 1;
                }
            }
        }
        double[][] IAinverse = Matrix.rref(temp);
        double[][] result = new double[matrix.length][matrix.length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = IAinverse[i][j + result.length];
            }
        }
        return result;
    }

    /**
     * Get the determinant of a given matrix
     * @param matrix the given matrix
     * @return the determinant
     */
    public static double determinant(double[][] matrix) throws MatrixDoesNotMatchException{
        if (matrix.length != matrix[0].length) {
            throw new MatrixDoesNotMatchException();
        }
        if (matrix.length == 1)
            return matrix[0][0];
        if (matrix.length == 2)
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        long det = 0l;
        int flag = 0;
        for (int i = 0; i < matrix.length; i++) {
            double[][] temp = new double[matrix.length - 1][matrix.length - 1];
            for (int j = 0; j < temp.length; j++) {
                int index = 0;
                for (int j2 = 0; j2 < temp.length; j2++) {
                    if (i == index)
                        index++;
                    temp[j][j2] = matrix[j + 1][index++];
                }
            }
            det += matrix[0][i] * Math.pow(-1, flag++) * determinant(temp);
        }
        return (double) det;
    }

    /**
     * The first kind of row operation. row-i = row-i - k*row-j
     * @param a the first row
     * @param b the second roew
     * @param coefficient the multiplier between two rwo
     * @return the result of this row after row operation
     */
    private static double[] rowOperation(double[] a, double[] b, double coefficient) {
        double[] result = new double[a.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = b[i] - a[i]*coefficient;
        }
        return result;
    }

    /**
     * Second type of row operation row-i = row-i / k
     * @param a the row number
     * @param coefficient the multiplier
     * @return the result row
     */
    private static double[] rowOperation_2(double[] a, double coefficient) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] / coefficient;
        }
        return result;
    }

    /**
     * Row exchange
     * @param row1 first row
     * @param row2 second row
     */
    private static void rowExchange(double[][] matrix, int row1, int row2) {
        double[] temp = new double[matrix[row1].length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = matrix[row1][i];
        }
        for (int i = 0; i < temp.length; i++) {
            matrix[row1][i] = matrix[row2][i];
        }
        for(int i = 0; i < temp.length; i++) {
            matrix[row2][i] = temp[i];
        }
    }

    /**
     * This method is designed to create a augmented matrix that will be useful when operating
     * row reduction for getting the final solution
     * @param matrix the matrix
     * @param b b
     * @return the augmented matrix
     */
    private static double[][] augmented(double[][] matrix, double[] b) {
        double[][] tested = new double[matrix.length][matrix[0].length + 1];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                tested[i][j] = matrix[i][j];
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            tested[i][tested[0].length-1] = b[i];
        }
        return tested;
    }

    /**
     * This method is designed to get the coefficient matrix from the augmented matrix
     * @param matrix the original matrix
     * @return the coefficient matrix
     */
    private static double[][] getCoefficientMatrix(double[][] matrix) {
        double[][] result = new double[matrix.length][matrix[0].length-1];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }

    /**
     * This is a helper method for solving linear equation with two unknonws in the format ax^2+bx+c
     * @param a a
     * @param b b
     * @param c c
     * @return the solution array
     */
    private static double[] solveLinearEquation(double a, double b, double c) {
        double[] sol = new double[2];
        double testNumber = Math.sqrt(b * b - 4 * a * c);
        if (testNumber >= 0) {
            sol[0] = (-b + testNumber) / (2 * a);
            sol[1] = (-b - testNumber) / (2 * a);
        } else {
            sol[0] = 0;
            sol[1] = 0;
        }
        return sol;
    }

    /**
     * Get the row of one matrix
     * @param index the row number we want to select
     * @param matrix the matrix
     * @return the row from specified index
     */
    private static double[] getRow(int index, double[][]matrix) {
        return matrix[index];
    }

    /**
     * Get a column of one matrix
     * @param index the column number we want to select
     * @param matrix the matrix
     * @return the column specified by the index
     */
    private static double[] getColumn(int index, double[][]matrix) {
        double[] column = new double[matrix.length];
        for(int i = 0; i < column.length; i++) {
            column[i] = matrix[i][index];
        }
        return column;
    }

    /**
     * This method will return an array which includes the column number of those free columns
     * @param matrix a given matrix in RREF form
     * @return described as above
     */
    public static int[] getPivotColumn(double[][] matrix) {
        int[] pivotColumn = new int[countPivot(matrix)];
        int index = 0;
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (matrix[row][column] != 0.0) {
                    pivotColumn[index++] = column;
                    break;
                }
            }
        }
        return pivotColumn;
    }

    /**
     * This method is a helper method for getting the first non zero number in one row
     * @param index the row number
     * @param matrix the given matrix
     * @return the pivot
     */
    private static double getPivot(int index, double[][]matrix) {
        for(int i = 0; i < matrix[0].length; i++) {
            if(matrix[index][i] != 0) {
                return matrix[index][i];
            }
        }
        return 0.0;
    }

    private static int getPivotColumnNumber(int row, double[][] matrix) {
        for(int i = 0; i < matrix[0].length; i++) {
            if(matrix[row][i] != 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method can tell you the number of pivot in this matrix
     * @param matrix the given matrix
     * @return the pivot number
     */
    private static int countPivot(double[][] matrix) {
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            if(getPivot(i,matrix) != 0.0) {
                count++;
             }
        }
        return count;
    }

    /**
     * Get the freecolumn number ( like column1 , column 3)
     * @param matrix given matrix
     * @return an array
     */
    public static int[] getFreeColumn(double[][] matrix) {
        int[] freeColumn = new int[matrix[0].length - countPivot(matrix)];
        int[] pivotColumn = getPivotColumn(matrix);
        int index = 0;
        for (int column = 0; column < matrix[0].length; column++) {
               if(!contains(pivotColumn, column)) {
                    freeColumn[index++] = column;
                }
        }
        return freeColumn;
    }

    /**
     * If an array contains an integer
     * @param array a given array
     * @param v integer v
     * @return true or false
     */
    private static boolean contains(int[] array, int v) {
        boolean result = false;
        for(int i : array){
            if(i == v){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Check if two matrices have the same size
     * @param first first matrix
     * @param second second matrix
     * @return true of false if they have the same size
     */
    private static boolean checkForSameSize(double[][] first, double[][] second) {
        return first.length == second.length &&
                first[0].length == second[0].length;
    }

    /**
     * Check if one row is a multiple of others
     * @param a the first row
     * @param b the second row
     * @return the boolean value
     */
    private static boolean checkRow(double[] a, double[] b) {
        double coefficient = a[0] / b[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] / b[i] != coefficient)
                return false;
        }
        return true;
    }

    /**
     * Designed to determine whether Ax=b has a solution, which means that whether it has a [000|1] or not
     * @param matrix the tested matrix
     * @return the result
     */
    private static boolean haveSolution(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            if(zeroRowForCoef(getRow(i,matrix)) && matrix[i][matrix[0].length-1]==1) {
                return false;
            }
        }
        return true;
    }

    /**
     * This is a helper method. Intended to decide whether the matrix has a zero row
     * @param a every row
     * @return the result whether it is a zero row or not
     */
    public static boolean zeroRow(double[] a) {
        if(a == null) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if(Math.abs(a[i]) > 1e-10f) {
                return false;
            }
        }
        for (int i = 0; i < a.length; i++) {
            a[i] = 0.0;
        }
        return true;
    }

    /**
     * This is a helper method. Intended to decide whether the coefficient matrix has a zero row
     * @param a the rwo need to be tested
     * @return the result whether it is a zero row or not
     */
    private static boolean zeroRowForCoef(double[] a) {
        for (int i = 0; i < a.length-1; i++) {
            if(Math.abs(a[i]) > 1e-10f) {
                return false;
            }
        }
        for (int i = 0; i < a.length-1; i++) {
            a[i] = 0.0;
        }
        return true;
    }

    public static String getSolutionSet(double[] sol) {
        String solution = "";
        for (int i = 0; i < sol.length; i++) {
            solution = solution + String.format("%.2f", sol[i]) + "\n";
        }
        solution += "\n";
        return solution;
    }
}