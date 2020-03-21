package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class matrixCalculator extends Stage {

    private ComboBox<String> rowNumber = new ComboBox<>();
    private ComboBox<String> columnNumber = new ComboBox<>();
    private ComboBox<String> matrixTypeSelector = new ComboBox<>();
    private ComboBox<String> operationSelector = new ComboBox<>();

    matrixCalculator(){
        String[]matrixTypelist = {"Regular", "Markov"};
        String[]operationTypelist_R = {"GetRREF", "Solve","Determinant","Eigenvalues",
                "Addition","Subtraction","Multiplication","Inverse"};
        String[]operationTypelist_M = {"Get steady state"};
        String[]rows = {"2","3","4","5","6"};
        String[]cols = {"2","3","4","5","6"};

        BorderPane calculator = new BorderPane(); // The largest pane
        calculator.setId("calculator");

        // Creating the top of the Borderpane
        VBox selector = new VBox(); // There will be three part in the VBox selector
        selector.setSpacing(15);
        selector.setId("selector");
        // 1. to choose the type of matrix
        // 2. to choose the operation the users want to perform
        // 2. to choose the size of matrix

        // 1. Here is to choose the type of matrix
        Button toChooseOperation = new Button("Go"); // press when finished choosing the type of the matrix
        selectMatrixType(selector, matrixTypelist, toChooseOperation);

        Button chooseSize = new Button("Choose Size");
        // 2. Here is to choose the operation type if the user choose regular matrix
        toChooseOperation.setOnAction(e -> {
            if (matrixTypeSelector.getValue().equals("Regular")) {
                operationOption(selector, operationTypelist_R, chooseSize);
            } else if (matrixTypeSelector.getValue().equals("Markov")) {
                operationOption(selector, operationTypelist_M, chooseSize);
            }
        });

        // 3. Here is to choose the size of the matrix
        Button submitSize = new Button("Go");
        Button clear = new Button("Reset");
        chooseSize.setOnAction(e -> {
            sizeOption(selector, rows, cols, submitSize, clear);
        });

        // Put the VBox selctor to the top of the BoaderPane
        calculator.setTop(selector);

        // To create a VBox to hold the original matrix and the result matrix
        VBox original_result = new VBox();
        original_result.setSpacing(30);

        // To create a matrix that allows the users to put input in it and then click rref
        GridPane original = new GridPane();
        original.setId("original");
        original.setVgap(5);
        original.setHgap(8);
        VBox.setMargin(original, new Insets(20,0,0,0)); // Set the margin

        // for the original input matrix and the selector
        Button toResult = new Button("GetRREF");
        submitSize.setOnAction(e -> {
            if (operationSelector.getValue().equals("GetRREF") || operationSelector.getValue().equals("Inverse")) {
                addToMatrix(rowNumber, columnNumber, original, toResult);
            } else if (operationSelector.getValue().equals("Solve")) {
                augmentMatrix(rowNumber, columnNumber, original, toResult);
            } else if(operationSelector.getValue().equals("Determinant")) {
                addToMatrix(rowNumber,columnNumber,original,toResult);
            } else if(operationSelector.getValue().equals("Get steady state")) {
                addToMatrix(rowNumber, columnNumber, original, toResult);
            } else if(operationSelector.getValue().equals("Eigenvalues")) {
                addToMatrix(rowNumber, columnNumber, original, toResult);
            } else {
                calculationMatrix(rowNumber, columnNumber, original, toResult);
            }
        });
        original_result.getChildren().add(original);

        //Read the input from Textfield and put it into the matrix, and then get the answer matrix
        GridPane finalResult = new GridPane();
        finalResult.setVgap(5);
        finalResult.setHgap(8);

        toResult.setOnAction(e -> {
            try {
                getResultMatrix(rowNumber, columnNumber, original, finalResult);
            } catch (MatrixDoesNotMatchException error) {
                error.getMessage();
            }
        });
        original_result.getChildren().add(finalResult);

        // Put the VBox into the center of the BoarderPane
        calculator.setCenter(new ScrollPane(original_result));

        // How the clear button can reset the matrix and users are allowed to enter again
        clear.setOnAction(e -> {
           clear(selector, original_result);
        });

        // Set up the scene for this matrix calculator
        Scene scene = new Scene(calculator,500,500);
        scene.getStylesheets().add(matrixCalculator.class.getResource("stylesheet.css").toExternalForm());
        setScene(scene);
        setTitle("Matrix Calculator");
        setResizable(false); // Fixed the window
        show();
    }

    /**
     * This method takes in the big VBox, and then create a HBox allowing users to choose matrixType
     * and then put this HBox into the VBox
     * @param selector the VBox
     * @param matrixType the String list with the names of matrix types
     * @param toChooseOperation a button that once press will allow users to choose next phase, which
     * is to select operations
     */
    private void selectMatrixType(VBox selector, String[] matrixType, Button toChooseOperation) {
        HBox selectorForMatrix = new HBox();
        selectorForMatrix.setSpacing(20);
        selectorForMatrix.setAlignment(Pos.CENTER);
        ObservableList<String> getMatrixType = FXCollections.observableArrayList(matrixType);
        matrixTypeSelector.getItems().addAll(getMatrixType);
        matrixTypeSelector.setValue("Choose matrix type");
        selectorForMatrix.getChildren().addAll(matrixTypeSelector, toChooseOperation);
        selector.getChildren().add(selectorForMatrix);
    }

    /**
     * This method also takes in a VBox. It will create a HBox, allowing the users to choose what
     * operations they would like to perform and put that HBox into the given VBox
     * @param selector the VBox, which is used for storing HBox
     * @param list a string list containing the operation options
     * @param toChooseSize a button, once pressed, allows the users to go to the next phase, which is
     * to choose the size of the matrix
     */
    private void operationOption(VBox selector, String[] list, Button toChooseSize) {
        HBox operation = new HBox();
        operation.setSpacing(20);
        operation.setAlignment(Pos.CENTER);
        ObservableList<String> getOperation = FXCollections.observableArrayList(list);
        operationSelector.setValue("Choose operation");
        this.operationSelector.getItems().addAll(getOperation);
        operation.getChildren().addAll(operationSelector, toChooseSize);
        selector.getChildren().add(operation);
    }

    /**
     * A method takes in a VBox. It will create a HBox, allowing the users to choose the size of the matrix
     * (namely the number of rows and columns). It wil then put that HBox into the VBox
     * @param selector the given VBox
     * @param row the string list showing the number of rows provided
     * @param col the string list showing the number of columns provided
     * @param submitSize the button, once pressed, will submit the size of the matrix
     * @param clear the button, once pressed, will clear everything on the matrix, and allow the users to
     * to choose the size again.
     */
    private void sizeOption(VBox selector, String[] row, String[] col, Button submitSize, Button clear) {
        HBox selectorForSize = new HBox();
        selectorForSize.setSpacing(10);
        selectorForSize.getChildren().add(new Label("Row"));
        selectorForSize.getChildren().add(new Label("Column"));

        // Set the ComboBox, Button for Submission and Reset, and put it in the GridPane "GetSize"
        ObservableList<String> getRow = FXCollections.observableArrayList(row);
        ObservableList<String> getColumns = FXCollections.observableArrayList(col);
        rowNumber.getItems().addAll(getRow);
        columnNumber.getItems().addAll(getColumns);
        rowNumber.setValue("2");
        columnNumber.setValue("2");
        rowNumber.setPrefWidth(60);
        columnNumber.setPrefWidth(60);
        selectorForSize.getChildren().add(rowNumber);
        selectorForSize.getChildren().add(columnNumber);
        selectorForSize.getChildren().add(submitSize);
        selectorForSize.getChildren().add(clear);
        selectorForSize.setAlignment(Pos.CENTER);

        selector.getChildren().add(selectorForSize);
    }

    /**
     * This method reads the value of the "size box" and create a matrix with the given size
     * @param a ComboBox that contains the value for the number of rows
     * @param b ComboBox that contains the value for the number of columns
     * @param pane GridPane that is used to put the matrix inside
     * @param bt Button that allow the users to perform the operation they'd like to do
     */
    private void addToMatrix(ComboBox<String> a, ComboBox<String> b, GridPane pane, Button bt) {
        bt.setText(operationSelector.getValue());
        int row = Integer.parseInt(a.getValue());
        int column = Integer.parseInt(b.getValue());
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                TextField inputNumber = new TextField("1");
                inputNumber.setPrefWidth(100);
                pane.add(inputNumber, j, i);
            }
        }
        pane.add(bt,row, column);
    }

    /**
     * This method reads the size of the "size box" and create a matrix with a given and an extra column
     *  for users to enter the b in Ax = b
     * @param a ComboBox refers to row
     * @param b ComboBox refers to column
     * @param pane pane where the matrix is put on
     * @param bt Button that allow the users to perform the operation they'd like to do
     */
    private void augmentMatrix(ComboBox<String> a, ComboBox<String> b, GridPane pane, Button bt) {
        addToMatrix(a, b, pane, bt);
        int row = Integer.parseInt(a.getValue());
        int column = Integer.parseInt(b.getValue());
        for (int i = 0; i < row; i++) {
            TextField inputNumber = new TextField("1");
            inputNumber.setPrefWidth(100);
            pane.add(inputNumber, column, i);
        }
    }

    private void calculationMatrix(ComboBox<String> a, ComboBox<String> b, GridPane pane, Button bt) {
        addToMatrix(a, b, pane, bt);
        bt.setText(operationSelector.getValue());
        int row = Integer.parseInt(a.getValue());
        int column = Integer.parseInt(b.getValue());
        for (int i = 0; i < row; i++) {
            for (int j = column + 5; j < 2 * column + 5; j++) {
                TextField inputNumber = new TextField("1");
                inputNumber.setPrefWidth(100);
                pane.add(inputNumber, j, i);
            }
        }
    }

    /**
     * This will set the GridPane result with the result matrix, or a solution text depending on what operations
     * the users would like to perform.
     * @param a ComboBox for row
     * @param b ComboBox for column
     * @param original original pane. We need to read the input from it.
     * @param result result pane. The pane we will use to show the result
     */
    private void getResultMatrix(ComboBox<String> a, ComboBox<String> b, GridPane original,
                                 GridPane result) throws MatrixDoesNotMatchException{
        double[][] matrix = new double[Integer.parseInt(a.getValue())]
                [Integer.parseInt(b.getValue())];
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(getNodeByRowColumnIndex(i,j,original) != null) {
                    matrix[i][j] = Double.parseDouble(((TextField)
                            (getNodeByRowColumnIndex(i, j, original))).getText());
                }
            }
        }
        if (operationSelector.getValue().equals("GetRREF")) {
            double[][] resultMatrix = Matrix.rref(matrix);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    TextField resultNumber = new TextField(resultMatrix[i][j] + " ");
                    resultNumber.setPrefWidth(100);
                    result.add(resultNumber, j, i);
                }
            }
        } else if (operationSelector.getValue().equals("Solve")) {
            double[] image = new double[Integer.parseInt(a.getValue())];
            for (int i = 0; i < matrix.length; i++) {
                image[i] = Double.parseDouble(((TextField) (getNodeByRowColumnIndex(i,
                        matrix[0].length, original))).getText());
            }
            Text solution = new Text(Matrix.solve(matrix, image));
            result.add(solution,1,1);
        } else if(operationSelector.getValue().equals("Determinant")) {
            try {
                double determinant = Matrix.determinant(matrix);
                result.add(new Text(determinant+""),1,1);
            } catch (MatrixDoesNotMatchException e) {
                Text error=  new Text("Wrong matrix, enter it again");
                result.add(error,1,1);
            }
        } else if (operationSelector.getValue().equals("Get steady state")) {
            try {
                Text solution = new Text(markovMatrix.getSteadyState(matrix));
                result.add(solution,1,1);
            } catch (NotMarkovMatrixException e) {
                Text error=  new Text("Wrong matrix, enter it again");
                result.add(error,1,1);
            }
        } else if (operationSelector.getValue().equals("Eigenvalues")) {
            double[] eigenvalue = Matrix.getEigenvalue(matrix);
            result.add(new Text(eigenvalue[0] + ""), 1, 1);
            result.add(new Text(eigenvalue[1] + ""), 2, 1);
        } else if (operationSelector.getValue().equals("Inverse")) {
            double[][] resultMatrix = Matrix.getInverse(matrix);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    TextField resultNumber = new TextField(resultMatrix[i][j] + " ");
                    resultNumber.setPrefWidth(100);
                    result.add(resultNumber, j, i);
                }
            }
        } else {
            // Read the input from the second matrix
            double[][] matrix_second = new double[Integer.parseInt(a.getValue())]
                    [Integer.parseInt(b.getValue())];
            for(int i = 0; i < matrix_second.length; i++) {
                for (int j = 0; j < matrix_second[0].length; j++) {
                    if(getNodeByRowColumnIndex(i,j + matrix_second[0].length + 5,original) != null) {
                        matrix_second[i][j] = Double.parseDouble(((TextField)
                                (getNodeByRowColumnIndex(i, j + matrix_second[0].length + 5, original)))
                                .getText());
                    }
                }
            }

            double[][] resultMatrix = new double[matrix.length][matrix[0].length];

            // Do the operation (Addition / Subtraction / Multiplication)
            if (operationSelector.getValue().equals("Addition")) {
                resultMatrix = Matrix.sum(matrix, matrix_second);
            } else if (operationSelector.getValue().equals("Subtraction")) {
                resultMatrix = Matrix.subtract(matrix, matrix_second);
            } else if(operationSelector.getValue().equals("Multiplication")){
                resultMatrix = Matrix.multiply(matrix, matrix_second);
            }
            // Put the content in the matrix on the screen (pane)
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    TextField resultNumber = new TextField(resultMatrix[i][j] + " ");
                    resultNumber.setPrefWidth(100);
                    result.add(resultNumber, j, i);
                }
            }
        }
    }

    /**
     * This method is used to clear everything in the matrix, enabling the users to enter the size
     * of the matrix again
     * @param a first pane, which usually represent the original matrix
     * @param b second pane, which usually represent the result matrix
     */
    private void clear(Pane a, Pane b) {
        a.getChildren().clear();
        b.getChildren().clear();
        new WelcomePage();
        close();
    }

    /**
     * Helper method for getting the node in a GridPane by entering its index
     * @param row row number
     * @param column column number
     * @param gridPane GridPane we want to get node from
     * @return the node we want to get
     */
    private Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}