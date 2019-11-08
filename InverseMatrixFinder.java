import java.util.*;

public class InverseMatrixFinder {
	public static void main(String[] args) {

		enter();
		System.out.println("This is an Inverse Matrix Finder.");
		System.out.println("How it works is simple.");
		System.out.println("We first set [A|I], and convert it to [I|B] with Gausssian Elimination and Back Substitution.");
		System.out.println("Then, B is the inverse matrix of A!");
		enter();

		// Reading User Data, Setting Matrices.
		Scanner sio = new Scanner(System.in);
		System.out.print("How many columns and rows will the matrix have? : ");
		int varNum = sio.nextInt();
		Matrix matrixA = new Matrix(varNum, varNum);
		Matrix matrixI = new Matrix(varNum, varNum);
		System.out.println("For Matrix A, Initialize Random values?");
		System.out.println("1      : Yes");
		System.out.println("Others : No");
		System.out.print("Decide : ");
		int askInitializeRandom = sio.nextInt();
		if (askInitializeRandom != 1) {
			for (int i = 0; i < varNum; i++) {
				for (int k = 0; k < varNum; k++) {
					System.out.print("Matrix A: What is the value of row " + i + " and column " + k + "? : ");
					int c = sio.nextInt();
					matrixA.changeElement(i, k, c);
				}
			}
		}
		System.out.println("For Matrix I, Initialize Identity?");
		System.out.println("1      : Yes");
		System.out.println("Others : No");
		System.out.print("Decide : ");
		askInitializeRandom = sio.nextInt();
		if (askInitializeRandom != 1) {
			for (int ia = 0; ia < varNum; ia++) {
				for (int ka = 0; ka < varNum; ka++) {
					System.out.print("Matrix I: What is the value of row " + ia + " and column " + ka + "? : ");
					int ca = sio.nextInt();
					matrixI.changeElement(ia, ka, ca);
				}
			}
		} else
			setAsIdentity(matrixI);

		// Can't use gaussian subtraction if the diagonal element is 0?
		for (int col = 0; col < varNum; col++) {
			for (int row = 0; row < varNum; row++) {
				if (matrixA.element(col, col) == 0) {
					matrixI.switchRow(col, row);
					matrixA.switchRow(col, row);
				}
			}
		}
		enter();
		System.out.println("This is A in [A|I]:");
		matrixA.printMatrix();
		enter();
		System.out.println("This is I in [A|I]:");
		matrixI.printMatrix();
		enter();

		long start = System.currentTimeMillis();

		// Copying Data to New Matrices for Data Conservation.
		Matrix matrixA_copy = new Matrix(varNum, varNum);
		Matrix matrixI_copy = new Matrix(varNum, varNum);

		for (int rowTemp = 0; rowTemp < varNum; rowTemp++) {
			for (int colTemp = 0; colTemp < varNum; colTemp++) {
				matrixA_copy.changeElement(rowTemp, colTemp, matrixA.element(rowTemp, colTemp));
			}
		}

		for (int rowTemp = 0; rowTemp < varNum; rowTemp++) {
			for (int colTemp = 0; colTemp < varNum; colTemp++) {
				matrixI_copy.changeElement(rowTemp, colTemp, matrixI.element(rowTemp, colTemp));
			}
		}

		// Pivoting Row.
		for (int k = 0; k < varNum; k++) {
			matrixI_copy.multiplyRow(k, 1.0 / matrixA_copy.element(k, k));
			matrixA_copy.multiplyRow(k, 1.0 / matrixA_copy.element(k, k));
		}

		// Checking Pivot Situation
		/*
		 * System.out.println("After Pivoting matrixA_copy and matrixI_copy");
		 * System.out.println("matrixA_copy:"); matrixA_copy.printMatrix(); enter();
		 * System.out.println("matrixI_copy:"); matrixI_copy.printMatrix(); enter();
		 */

		// Making Plate to identity and Applying same orders of calculation to Inverse.
		makeItIdentity(matrixA_copy, matrixI_copy, varNum);

		// INVERSE PRINTING
		System.out.println("***** After converting [A|I] to [I|B] form *****");
		enter();
		System.out.println("This is I in [I|B]:");
		matrixA_copy.printMatrix();
		enter();
		System.out.println("This is B in [I|B], the inverse matrix of A.");
		matrixI_copy.printMatrix();

		// Checking Errors and Calculation Time
		enter();
		System.out.println("Error: " + checkAccuracy(matrixA, matrixI_copy));
		long end = System.currentTimeMillis();
		System.out.println("Calculation time = " + ((end - start) / 1000.0) + "s");
		enter();
	}

	public static void makeItIdentity(Matrix mat1, Matrix mat2, int varNum) {
		for (int col = 0; col < varNum; col++) {
			mat2.multiplyRow(col, 1 / mat1.element(col, col));
			mat1.multiplyRow(col, 1 / mat1.element(col, col));
			for (int row = 0; row < varNum; row++) {
				if (col != row) {
					double factor = mat1.element(row, col);
					mat2.subtractRow(row, col, factor);
					mat1.subtractRow(row, col, factor);
				}
			}
		}
	}

	public static void setAsIdentity(Matrix a) {
		for (int row = 0; row < a.rowNum(); row++) {
			for (int col = 0; col < a.colNum(); col++) {
				if (row == col)
					a.changeElement(row, col, 1);
				else
					a.changeElement(row, col, 0);
			}
		}
	}

	public static void enter() {
		System.out.println();
	}

	public static double checkAccuracy(Matrix a, Matrix inverse) {
		Matrix identity = new Matrix(a.rowNum(), a.colNum());
		setAsIdentity(identity);
		Matrix crossed = a.multiply(inverse);
		enter();
		System.out.println("Check if A * A^(-1) returns I: ");
		crossed.printMatrix();
		double error = 0;
		for (int row = 0; row < crossed.rowNum(); row++) {
			for (int col = 0; col < crossed.colNum(); col++) {
				error += crossed.element(row, col) - identity.element(row, col);
			}
		}
		return error / (a.rowNum() * a.colNum());
	}
}