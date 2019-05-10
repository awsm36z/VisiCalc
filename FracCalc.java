public class FracCalc{

	static String BAD_INPUT_ERROR = "NaN";

		public FracCalc(){

		}

    public String solveFullEquation(String input) {

		if (isMissingOperation(input)) {
			System.out.println("Invalid! Your opperation is missing! Make sure to add a space before and after!");
		}
		
		String answer = mathSolverWithParanthesis(input);

		// now the equation string has the solution
		return answer;
	}

	private static String mathSolverWithParanthesis(String input) {
		
		if (input.contains("(")) {
			int openParamInd = input.indexOf("(");
			int endParamInd = input.lastIndexOf(")");
			if (endParamInd == -1) {
				return BAD_INPUT_ERROR + "";
			}

			int subEquationStart = openParamInd + 1;
			int subEquationEnd = endParamInd;

			String tempAnswer = mathSolverWithParanthesis(input.substring(subEquationStart, subEquationEnd));
			String simplifiedEq = input.substring(0, openParamInd) + tempAnswer;
			if (endParamInd != (input.length() - 1)) {
				simplifiedEq = simplifiedEq + input.substring(endParamInd + 1);
			}

			return mathSolverWithParanthesis(simplifiedEq);
		}
		
		if(isMissingOperation(input)) {
			return input;
		}

		return mathSolver(input);
	}

	private static String mathSolver(String equation) {
		int operationIndex = 0;

		// let's solve the high order operations first (* and /) and substitute that
		// operation with their solution.
		// this will keep shortening the equation string by solving one operation at a
		// time.
		String opp = getHighOrderOpperation(equation);
		while (!opp.equals("") && !equation.equals(BAD_INPUT_ERROR)) {
			operationIndex = equation.indexOf(opp);
			equation = solveOperation(equation, operationIndex, opp);
			opp = getHighOrderOpperation(equation);
		}

		// now equaiton string has only low order operations remaining to solve (+ and
		// -)
		opp = getLowOrderOpperation(equation);
		while (!opp.equals("") && !equation.equals(BAD_INPUT_ERROR)) {
			operationIndex = equation.indexOf(opp);
			equation = solveOperation(equation, operationIndex, opp);
			opp = getLowOrderOpperation(equation);
		}

		return equation;

	}

	private static boolean isMissingOperation(String equation) {
		if (getHighOrderOpperation(equation).equals("") && getLowOrderOpperation(equation).equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * @param input: input string
	 * @param opperation: index of operator
	 * @param opp: operator string
	 * @return String: result of operation
	 */
	private static String solveOperation(String input, int operationIndex, String opp) {
		int numOneIndex = getIndexOfLeftNumber(input, operationIndex, opp);
		String numOne = input.substring(numOneIndex, operationIndex);

		int numTwoIndex = getIndexOfRightNumber(input, operationIndex, opp);
		String numTwo = input.substring(operationIndex + opp.length(), numTwoIndex);

		if (isValidNumber(numOne) && isValidNumber(numTwo)) {
			return input.substring(0, numOneIndex) + mixedNums(numOne, numTwo, opp, operationIndex)
					+ input.substring(operationIndex + opp.length() + numTwo.length());
		} else {
			return "Bad Input!!!!";
		}
	}

	// return index of index of right number
	private static int getIndexOfRightNumber(String input, int opperation, String opp) {

		// we start from the end of the current operation string, and we look for the
		// next operation
		int startInd = opperation + opp.length();
		String right = input.substring(startInd, input.length());

		// look for next +
		int addInd = right.indexOf(" + ");
		// if we can't find +, we set the index to end of string
		if (addInd == -1) {
			addInd = input.length();
		}

		// look for next -
		int subInd = right.indexOf(" - ");
		// if we can't find -, we set the index to end of string
		if (subInd == -1) {
			subInd = input.length();
		}

		// look for next *
		int mulInd = right.indexOf(" * ");
		// if we can't find *, we set the index to end of string
		if (mulInd == -1)
			mulInd = input.length();

		// look for next /
		int divInd = right.indexOf(" / ");
		// if we can't find /, we set the index to end of string
		if (divInd == -1)
			divInd = input.length();

		//
		int modInd = right.indexOf("%");
		if (modInd == -1)
		modInd = input.length();

		int expInd = right.indexOf("^");
		if (expInd == -1)
		expInd = input.length();

		// find the next operation (min of next indexes), or end of string if no
		// operation is remaining
		int rightNumberIndex = Math.min(Math.min(addInd, subInd), Math.min(Math.min(expInd, modInd), (Math.min(divInd, mulInd))));

		if (rightNumberIndex == input.length()) {
			return rightNumberIndex;
		} else {
			return opperation + opp.length() + rightNumberIndex;
		}

	}

	// returns the index of the number before the current operation
	private static int getIndexOfLeftNumber(String input, int opperation, String opp) {
		String left = input.substring(0, opperation);
		int previousNumberIndex = Math.max(Math.max(left.lastIndexOf(" + "), left.lastIndexOf(" / ")),
				Math.max(left.lastIndexOf(" - "), left.lastIndexOf(" * ")));

		if (previousNumberIndex > opperation || previousNumberIndex == -1)
			return 0;

		return previousNumberIndex + opp.length();
	}

	// returns first multiplication or division operation found in string (left to
	// right)
	// those operations are processed first
	private static String getHighOrderOpperation(String input) {
		// finds first index of either multiplication or division
		int indDiv = input.indexOf(" / ");
		int indMul = input.indexOf(" * ");

		if (indDiv == -1 && indMul == -1) {
			return "";
		}

		if (indDiv == -1) {
			return " * ";
		}

		if (indMul == -1) {
			return " / ";
		}

		if (indMul < indDiv) {
			return " * ";
		}

		return " / ";
	}

	// returns first addition or substraction operation found in string (left to
	// right)
	// those operations are processed after multiplication or division
	private static String getLowOrderOpperation(String input) {
		// finds first index of either multiplication or division
		int indAdd = input.indexOf(" + ");
		int indSub = input.indexOf(" - ");

		if (indAdd == -1 && indSub == -1) {
			return "";
		}

		if (indAdd == -1) {
			return " - ";
		}

		if (indSub == -1) {
			return " + ";
		}

		if (indSub < indAdd) {
			return " - ";
		}

		return " + ";
	}

	private static String mixedNums(String numOne, String numTwo, String opp, int opperation) {
		String improperFractionOne = "";
		String improperFractionTwo = "";

		improperFractionOne = getNumOneFrac(numOne, opperation, improperFractionOne);
		improperFractionTwo = getNumTwoFrac(numTwo, improperFractionTwo);
		if (opp.equals(" + ")) {
			return fracAdd(improperFractionOne, improperFractionTwo);
		} else if (opp.equals(" * ")) {
			return multiplyFractions(improperFractionOne, improperFractionTwo);
		} else if (opp.equals(" / ")) {
			return divideFractions(improperFractionOne, improperFractionTwo);
		} else if (opp.equals(" - ")) {
			return subtractFractions(improperFractionOne, improperFractionTwo);
		} else {
			return "invalid, \nHere are some examples: 1 + 1 or 1_2/3 - 2/5";
		}
	}

	/*
	 * The method 'getNumOneFrac' has two purposes. One, it gets the first number as
	 * an improper fraction and has it all become uniform. It also cleans up the
	 * code in other methods.
	 */

	private static String getNumOneFrac(String numOne, int opperation, String improperFractionOne) {
		if (numOne.contains("/")) {
			// if it is improper, convert it to proper.
			if (numOne.contains("_")) {
				int space = numOne.indexOf("_");
				int frac = numOne.indexOf("/");

				// Create the separate pieces for the improper fraction.
				String denomenatorOneString = numOne.substring(frac + 1);
				String wholeNumberOne = numOne.substring(0, space);
				String numeratorOneString = numOne.substring(space + 1, frac);

				// converting the strings into ints so that we can do math with them.
				int wholeNumber = Integer.parseInt(wholeNumberOne);
				int numeratorOne = Integer.parseInt(numeratorOneString);
				int denomenatorOne = Integer.parseInt(denomenatorOneString);

				int numeratorTopImproper = (wholeNumber * denomenatorOne) + numeratorOne;
				improperFractionOne = numeratorTopImproper + "/" + denomenatorOne;

				// deals with the input if the first number is just a regular fraction.
			} else {
				int frac = numOne.indexOf("/");

				String denomenatorOneString = numOne.substring(frac + 1);
				String numeratorOneString = numOne.substring(0, frac);

				improperFractionOne = numeratorOneString + "/" + denomenatorOneString;
			}
		} else {
			improperFractionOne = numOne + "/1";
		}
		return improperFractionOne;
	}

	/*
	 * The method 'getNumTwoFrac' has two purposes. One, it gets the second number
	 * as an improper fraction and has it all become uniform. It also cleans up the
	 * code in other methods.
	 */

	private static String getNumTwoFrac(String numTwo, String improperFractionTwo) {
		if (numTwo.contains("/")) {
			if (numTwo.contains("_")) {
				int fracTwo = numTwo.indexOf("/");
				int spaceTwo = numTwo.indexOf("_");

				/*
				 * This part of the code is here to break up the the part of the String that
				 * will be part of the second input. This input will be broken into three parts,
				 * the denominator, numerator, and the whole number.
				 */
				String denomenatorTwoString = numTwo.substring(fracTwo + 1);
				String wholeNumberTwoString = numTwo.substring(0, spaceTwo);
				String numeratorTwoString = numTwo.substring(spaceTwo + 1, fracTwo);

				// Create the separate pieces for the improper fraction.
				int wholeNumberTwo = Integer.parseInt(wholeNumberTwoString);
				int numeratorTwo = Integer.parseInt(numeratorTwoString);
				int denomenatorTwo = Integer.parseInt(denomenatorTwoString);

				int numeratorTopImproperTwo = (wholeNumberTwo * denomenatorTwo) + numeratorTwo;
				improperFractionTwo = numeratorTopImproperTwo + "/" + denomenatorTwo;

			} else {
				// deals with the input if the first number is just a regular fraction.
				int fracTwo = numTwo.indexOf("/");

				String denomenatorTwoString = numTwo.substring(fracTwo + 1);
				String numeratorTwoString = numTwo.substring(0, fracTwo);

				improperFractionTwo = numeratorTwoString + "/" + denomenatorTwoString;
			}
		} else {
			improperFractionTwo = numTwo + "/1";
		}
		return improperFractionTwo;
	}

	private static boolean isValidNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != '1' && str.charAt(i) != '2' && str.charAt(i) != '3' && str.charAt(i) != '4'
					&& str.charAt(i) != '5' && str.charAt(i) != '6' && str.charAt(i) != '7' && str.charAt(i) != '8'
					&& str.charAt(i) != '9' && str.charAt(i) != '0' && str.charAt(0) != '-' && str.charAt(i) != '/'
					&& str.charAt(i) != '_') {
				return false;
			}
		}
		return true;
	}

	// Method gets the numerator of number called
	// The met
	private static int getNumerator(String improperFraction) {
		String nom = improperFraction.substring(0, improperFraction.indexOf("/"));
		return Integer.parseInt(nom);
	}

	// method gets the denominator of the number called
	private static int getDenomenator(String improperFraction) {
		String denom = improperFraction.substring(improperFraction.indexOf("/") + 1);
		return Integer.parseInt(denom);
	}

	/*
	 * method adds fractions by getting a common denominator and adding from there
	 */

	private static String fracAdd(String improperFractionOne, String improperFractionTwo) {
		int nom1 = getNumerator(improperFractionOne);
		int nom2 = getNumerator(improperFractionTwo);
		int denom1 = getDenomenator(improperFractionOne);
		int denom2 = getDenomenator(improperFractionTwo);

		int sumNom = nom1 * denom2 + nom2 * denom1;
		int sumDenom = denom1 * denom2;

		return simplify(sumNom, sumDenom);
	}

	/*
	 * Method simplifies the final answer by First: it checks to see if the
	 * denominator is equal to zero. If it is, then it returns
	 * "cannot divide by zero!" Second: it checks to see if the numerator can be
	 * divided by the denominator. If it can be, then it will see if it can be
	 * simplified to a whole number by checking to see when the numerator is divided
	 * by the Denominator there is a remainder of 0. If there is a remainder of 0,
	 * then the method returns the whole number result. if there is not a remainder
	 * of 0, then the method writes the equation simplified by dividing both
	 * numerator and denominator by the GCF which is found in the method of getGCF.
	 */
	private static String simplify(int nom, int denom) {
		String answer = "";
		if (denom == 0) {
			answer = "cannot divide by zero!";
		} else {
			if (Math.abs(nom) >= denom) {
				if (nom % denom == 0) {
					answer = nom / denom + "";
				} else {
					answer = nom / getGCF(nom, denom) + "/" + denom / getGCF(nom, denom);
				}
			} else {
				answer = nom / getGCF(nom, denom) + "/" + denom / getGCF(nom, denom);
			}
		}
		return answer;

	}

	/*
	 * The following method takes the numerator and denominator and creates a
	 * 'checker' variable that checks to see if it is the greatest common factor. It
	 * does so by starting at the value of the denominator. If the denominator is
	 * larger than then the numerator then the count will go down one, and repeats
	 * the algorithm until it reaches a value that is divisible by both the
	 * numerator and the denominator.
	 */
	private static int getGCF(int nom, int denom) {
		int count = denom;
		while (count > 0) {
			if (denom % count == 0 && nom % count == 0) {
				return count;
			} else {
				count--;
			}
		}
		return count;
	}

	/*
	 * This method takes the numerator of the first fraction and multiplies it by
	 * the numerator of the second fraction. Then it takes the denominator of the
	 * first fraction and multiplies that by the denominator of the second fraction.
	 * Finally it takes the new numerator and denominator and simplifies them using
	 * the simplify method.
	 */
	private static String multiplyFractions(String improperFractionOne, String improperFractionTwo) {
		int numeratorTotal = 0;
		int denomenatorTotal = 0;
		int nom1 = getNumerator(improperFractionOne);
		int nom2 = getNumerator(improperFractionTwo);
		int denom1 = getDenomenator(improperFractionOne);
		int denom2 = getDenomenator(improperFractionTwo);

		numeratorTotal = nom1 * nom2;
		denomenatorTotal = denom1 * denom2;

		return simplify(numeratorTotal, denomenatorTotal);
	}

	/*
	 * This method takes the first fraction and keeps it the same. It then takes the
	 * second fraction and flips the numerator and denominator. The method then goes
	 * on to multiply the fractions as normal. You divide fractions by taking the
	 * reciprocal of one fraction and multiply them together. Finally it simplifies
	 * the new fraction using the simplify method.
	 */
	private static String divideFractions(String improperFractionOne, String improperFractionTwo) {
		int numeratorTotal = 0;
		int denomenatorTotal = 0;
		int nom1 = getNumerator(improperFractionOne);
		int nom2 = getDenomenator(improperFractionTwo);
		int denom1 = getDenomenator(improperFractionOne);
		int denom2 = getNumerator(improperFractionTwo);

		numeratorTotal = nom1 * nom2;
		denomenatorTotal = denom1 * denom2;

		return simplify(numeratorTotal, denomenatorTotal);
	}

	/*
	 * This method takes one fraction and makes it negative by multiplying the
	 * numerator by -1 and then adds the the fractions as done by the add fractions
	 * method. Finally it simplifies the fraction by using the simplify method.
	 */
	private static String subtractFractions(String improperFractionOne, String improperFractionTwo) {
		int nom1 = getNumerator(improperFractionOne);
		int nom2 = -1 * getNumerator(improperFractionTwo);
		int denom1 = getDenomenator(improperFractionOne);
		int denom2 = getDenomenator(improperFractionTwo);

		int sumNom = nom1 * denom2 + nom2 * denom1;
		int sumDenom = denom1 * denom2;

		return simplify(sumNom, sumDenom);
	}
}