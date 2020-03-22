package sample;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This is a JUnit test to test the functionality of Matrix class and its counterpart, markovMatrix
 */
public class MatrixJUnitTest {
    private static final int TIMEOUT = 200;
    private final double error_range = 1e-10;

    @Test(timeout = TIMEOUT)
    public void testAddition() {
        double[][] matrix_1 = {{2,3,4},
                               {3,3,-2},
                               {-5,0,9}};
        double[][] matrix_2 = {{9,2,1},
                               {6,-3,0},
                               {10,-4,5}};

        double[][] result = Matrix.sum(matrix_1, matrix_2);
        double[] firstrow = {11,5,5};
        double[] secondrow = {9,0,-2};
        double[] thirdrow = {5,-4,14};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
    }

    @Test(expected = MatrixDoesNotMatchException.class, timeout = TIMEOUT)
    public void testAdditionWithDifferentSize() {
        double[][] matrix_1 = {{2,3,4},
                               {3,3,-2}};
        double[][] matrix_2 = {{9,2,1},
                               {6,-3,0},
                               {10,-4,5}};
        Matrix.sum(matrix_1, matrix_2);
    }

    @Test (timeout = TIMEOUT)
    public void testSubtraction() {
        double[][] matrix_1 = {{2,3,4},
                               {3,3,-2},
                               {-5,0,9}};
        double[][] matrix_2 = {{9,2,1},
                               {6,-3,0},
                               {10,-4,5}};

        double[][] result = Matrix.subtract(matrix_1, matrix_2);
        double[] firstrow = {-7,1,3};
        double[] secondrow = {-3,6,-2};
        double[] thirdrow = {-15,4,4};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
    }

    @Test(expected = MatrixDoesNotMatchException.class, timeout = TIMEOUT)
    public void testSubtractionWithDifferentSize() {
        double[][] matrix_1 = {{2,3,4},
                               {3,3,-2}};
        double[][] matrix_2 = {{9,2,1},
                               {6,-3,0},
                               {10,-4,5}};
        Matrix.subtract(matrix_1, matrix_2);
    }

    @Test (timeout = TIMEOUT)
    public void testMultiplication_2_2() {
        double[][] matrix_1 = {{2,3},
                               {-5,4}};
        double[][] matrix_2 = {{-1,8},
                               {2, 5}};

        double[][] result = Matrix.multiply(matrix_1, matrix_2);
        double[] firstrow = {4, 31};
        double[] secondrow = {13, -20};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testMultiplication_2_3() {
        double[][] matrix_1 = {{2,3,1},
                               {-5,4,-3}};
        double[][] matrix_2 = {{-1,8},
                               {2,5},
                               {6,2}};

        double[][] result = Matrix.multiply(matrix_1, matrix_2);
        double[] firstrow = {10, 33};
        double[] secondrow = {-5, -26};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testMultiplication_3_2() {
        double[][] matrix_1 = {{2,3},
                               {-5,4},
                               {6, 0}};
        double[][] matrix_2 = {{-1,8,5},
                               {2,5,-9}};

        double[][] result = Matrix.multiply(matrix_1, matrix_2);
        double[] firstrow = {4, 31, -17};
        double[] secondrow = {13, -20, -61};
        double[] thirdrow = {-6, 48, 30};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
    }

    @Test (expected = MatrixDoesNotMatchException.class, timeout = TIMEOUT)
    public void testMultiplicationException() {
        double[][] matrix_1 = {{2,3},
                               {-5,4}};
        double[][] matrix_2 = {{-1,8}};

        Matrix.multiply(matrix_1, matrix_2);
    }

    @Test (timeout = TIMEOUT)
    public void testREF_1() {
        double[][] matrix = {{1,2},
                             {3,4}};
        double[][] result = Matrix.ref(matrix);
        double[] firstrow = {1,2};
        double[] secondrow = {0,-2};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testREF_2() {
        double[][] matrix = {{3,2},
                             {1,4}};
        double[][] result = Matrix.ref(matrix);
        double[] firstrow = {3, 2};
        double[] secondrow = {0, 10.0/3};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testREF_3() {
        double[][] matrix = {{0,2},
                             {1,4}};
        double[][] result = Matrix.ref(matrix);
        double[] firstrow = {1, 4};
        double[] secondrow = {0, 2};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testREF_4() {
        double[][] matrix = {{0,2},
                             {5,4},
                             {3,2}};
        double[][] result = Matrix.ref(matrix);
        double[] firstrow = {5, 4};
        double[] secondrow = {0, 2};
        double[] thirdrow = {0,0};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testREF_5() {
        double[][] matrix = {{0,2,6},
                             {5,4,1}};
        double[][] result = Matrix.ref(matrix);
        double[] firstrow = {5,4,1};
        double[] secondrow = {0,2,6};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testREF_6() {
        double[][] matrix = {{0,0},
                             {0,0},
                             {3,2},
                             {-2,1}};
        double[][] result = Matrix.ref(matrix);
        double[] firstrow = {3,2};
        double[] secondrow = {0,7.0/3};
        double[] thirdrow = {0,0};
        double[] fourthrow = {0,0};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
        assertArrayEquals(fourthrow, result[3], error_range);
    }

    @Test (timeout =  TIMEOUT)
    public void testDeterminant2_2() {
        double[][] matrix = {{0,2},
                             {1,4}};
        double d = Matrix.determinant(matrix);
        assertEquals(-2, d, error_range);
    }

    @Test (timeout =  TIMEOUT)
    public void testDeterminant3_3() {
        double[][] matrix = {{0,2,9},
                             {1,4,-5},
                             {-3,6,7}};
        double d = Matrix.determinant(matrix);
        assertEquals(178, d, error_range);
    }

    @Test (timeout =  TIMEOUT)
    public void testDeterminant4_4() {
        double[][] matrix = {{1,5,6,-1},
                             {-3,9,2,0},
                             {1,0,0,5},
                             {2,3,-7,1}};
        double d = Matrix.determinant(matrix);
        assertEquals(1605, d, error_range);
    }

    @Test (expected = MatrixDoesNotMatchException.class, timeout = TIMEOUT)
    public void testDeterminantException() {
        double[][] matrix = {{1,5,6,-1},
                {-3,9,2,0},
                {1,0,0,5}};
        Matrix.determinant(matrix);
    }

    @Test (timeout = TIMEOUT)
    public void testRREF_1() {
        double[][] matrix = {{0,0},
                             {0,0},
                             {3,2}};
        double[][] result = Matrix.rref(matrix);
        double[] firstrow = {1,2.0/3};
        double[] secondrow = {0,0};
        double[] thirdrow = {0,0};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testRREF_2() {
        double[][] matrix = {{0,0},
                             {0,0},
                             {3,2},
                             {-2,1}};
        double[][] result = Matrix.rref(matrix);
        double[] firstrow = {1,0};
        double[] secondrow = {0,1};
        double[] thirdrow = {0,0};
        double[] fourthrow = {0,0};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
        assertArrayEquals(fourthrow, result[3], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testRREF_3() {
        double[][] matrix = {{0,1,3},
                             {2,0,7}};
        double[][] result = Matrix.rref(matrix);
        double[] firstrow = {1,0,3.5};
        double[] secondrow = {0,1,3};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testRREF_4() {
        double[][] matrix = {{0,0,3,-8},
                             {6,1,7,2}};
        double[][] result = Matrix.rref(matrix);
        double[] firstrow = {1,1.0/6,0, 31.0/9};
        double[] secondrow = {0,0,1,-8.0/3};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testRREF_5() {
        double[][] matrix = {{0,0,3},
                             {6,1,7},
                             {2,-3,9}};
        double[][] result = Matrix.rref(matrix);
        double[] firstrow = {1,0,0};
        double[] secondrow = {0,1,0};
        double[] thirdrow = {0,0,1};
        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testEigenValue_1() {
        double[][] matrix = {{1,2},
                             {2,4}};
        double[] value = Matrix.getEigenvalue(matrix);
        double[] expected = {5,0};
        assertArrayEquals(expected, value, error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testEigenValue_2() {
        double[][] matrix = {{-1,2},
                {6,4}};
        double[] value = Matrix.getEigenvalue(matrix);
        double[] expected = {(3 + Math.sqrt(73))/2, (3 - Math.sqrt(73))/2};
        assertArrayEquals(expected, value, error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testEigenValue_3() {
        double[][] matrix = {{-1,1},
                             {-6.25,4}};
        double[] value = Matrix.getEigenvalue(matrix);
        double[] expected = {1.5, 1.5};
        assertArrayEquals(expected, value, error_range);
    }

    @Test (expected = UnsupportedOperationException.class, timeout = TIMEOUT)
    public void testEigenValueException() {
        double[][] matrix = {{-1,1,8},
                             {-6.25,4,0}};
        Matrix.getEigenvalue(matrix);
    }

    @Test (timeout = TIMEOUT)
    public void testInverse2_2() {
        double[][] matrix = {{1,-2},
                             {2,4}};
        double[][] result = Matrix.getInverse(matrix);
        double[] firstrow = {0.5, 0.25};
        double[] secondrow = {-0.25, 0.125};

        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
    }

    @Test (timeout = TIMEOUT)
    public void testInverse3_3() {
        double[][] matrix = {{0,-2,6},
                             {2,1,8},
                             {1,-5,3}};
        double[][] result = Matrix.getInverse(matrix);
        double[] firstrow = {-43.0/70, 12.0/35, 11.0/35};
        double[] secondrow = {-1.0/35, 3.0/35, -6.0/35};
        double[] thirdrow = {11.0/70, 1.0/35, -2.0/35};

        assertArrayEquals(firstrow, result[0], error_range);
        assertArrayEquals(secondrow, result[1], error_range);
        assertArrayEquals(thirdrow, result[2], error_range);
    }

    @Test (expected = IllegalArgumentException.class, timeout = TIMEOUT)
    public void testInverseException() {
        double[][] matrix = {{0,-2,6},
                             {2,1,8}};
        Matrix.getInverse(matrix);
    }
}
