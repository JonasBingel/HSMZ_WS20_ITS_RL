package reinforcementLearning;

import java.text.DecimalFormat;

public class ReinforcementLearning {
	private static final int DIMENSIONS = 9;
	private static final int NUMBER_OF_POLICY_ACTIONS = PolicyAction.values().length;
	private static final double GAMMA = 0.99;
	private static final double REWARD = -0.02;
	private static final double VALUE_CHARGE_UNIT = 1;
	private static final double VALUE_ROBOT_SHREDDER = -1;
	private static final double EPSILON = 1.0E-10;

	private static final double P_CONFORMITY = 0.8;
	private static final double P_NONCONFORMITY_LEFT = 0.1;
	private static final double P_NONCONFORMITY_RIGHT = 0.1;

	private static double valuesOld[];
	private static double valuesNewOptimal[];
	private static double valuesToPolicyAction[][]; // Value Policy per state
	private static PolicyAction policy[];

	private static final DecimalFormat DF = new DecimalFormat("0.00");

	private static void initializeValues() {
		valuesOld = new double[DIMENSIONS];
		valuesNewOptimal = new double[DIMENSIONS];
		valuesToPolicyAction = new double[DIMENSIONS][NUMBER_OF_POLICY_ACTIONS];
	}

	private static void initialize() {
		policy = new PolicyAction[DIMENSIONS];

	}

	private static void calculateValuesToPolicyAction() {
		// State 0
		valuesToPolicyAction[0][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[4]
				+ P_NONCONFORMITY_LEFT * valuesOld[0] + P_NONCONFORMITY_RIGHT * valuesOld[1];
		valuesToPolicyAction[0][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * valuesOld[1]
				+ P_NONCONFORMITY_LEFT * valuesOld[4] + P_NONCONFORMITY_RIGHT * valuesOld[0];
		valuesToPolicyAction[0][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[0]
				+ P_NONCONFORMITY_LEFT * valuesOld[0] + P_NONCONFORMITY_RIGHT * valuesOld[4];
		valuesToPolicyAction[0][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[0]
				+ P_NONCONFORMITY_LEFT * valuesOld[1] + P_NONCONFORMITY_RIGHT * valuesOld[0];

		// State 1
		valuesToPolicyAction[1][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[1]
				+ P_NONCONFORMITY_LEFT * valuesOld[0] + P_NONCONFORMITY_RIGHT * valuesOld[2];
		valuesToPolicyAction[1][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * valuesOld[2]
				+ P_NONCONFORMITY_LEFT * valuesOld[1] + P_NONCONFORMITY_RIGHT * valuesOld[1];
		valuesToPolicyAction[1][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[0]
				+ P_NONCONFORMITY_LEFT * valuesOld[0] + P_NONCONFORMITY_RIGHT * valuesOld[1];
		valuesToPolicyAction[1][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[1]
				+ P_NONCONFORMITY_LEFT * valuesOld[2] + P_NONCONFORMITY_RIGHT * valuesOld[0];

		// State 2
		valuesToPolicyAction[2][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[5]
				+ P_NONCONFORMITY_LEFT * valuesOld[1] + P_NONCONFORMITY_RIGHT * valuesOld[3];
		valuesToPolicyAction[2][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * valuesOld[3]
				+ P_NONCONFORMITY_LEFT * valuesOld[5] + P_NONCONFORMITY_RIGHT * valuesOld[2];
		valuesToPolicyAction[2][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[1]
				+ P_NONCONFORMITY_LEFT * valuesOld[2] + P_NONCONFORMITY_RIGHT * valuesOld[5];
		valuesToPolicyAction[2][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[2]
				+ P_NONCONFORMITY_LEFT * valuesOld[3] + P_NONCONFORMITY_RIGHT * valuesOld[1];

		// State 3
		valuesToPolicyAction[3][PolicyAction.UP.ordinal()] = P_CONFORMITY * VALUE_ROBOT_SHREDDER
				+ P_NONCONFORMITY_LEFT * valuesOld[2] + P_NONCONFORMITY_RIGHT * valuesOld[3];
		valuesToPolicyAction[3][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * valuesOld[3]
				+ P_NONCONFORMITY_LEFT * VALUE_ROBOT_SHREDDER + P_NONCONFORMITY_RIGHT * valuesOld[3];
		valuesToPolicyAction[3][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[2]
				+ P_NONCONFORMITY_LEFT * valuesOld[3] + P_NONCONFORMITY_RIGHT * VALUE_ROBOT_SHREDDER;
		valuesToPolicyAction[3][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[3]
				+ P_NONCONFORMITY_LEFT * valuesOld[3] + P_NONCONFORMITY_RIGHT * valuesOld[2];

		// State 4
		valuesToPolicyAction[4][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[6]
				+ P_NONCONFORMITY_LEFT * valuesOld[4] + P_NONCONFORMITY_RIGHT * valuesOld[4];
		valuesToPolicyAction[4][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * valuesOld[4]
				+ P_NONCONFORMITY_LEFT * valuesOld[6] + P_NONCONFORMITY_RIGHT * valuesOld[0];
		valuesToPolicyAction[4][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[4]
				+ P_NONCONFORMITY_LEFT * valuesOld[0] + P_NONCONFORMITY_RIGHT * valuesOld[6];
		valuesToPolicyAction[4][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[0]
				+ P_NONCONFORMITY_LEFT * valuesOld[4] + P_NONCONFORMITY_RIGHT * valuesOld[4];

		// State 5
		valuesToPolicyAction[5][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[8]
				+ P_NONCONFORMITY_LEFT * valuesOld[5] + P_NONCONFORMITY_RIGHT * VALUE_ROBOT_SHREDDER;
		valuesToPolicyAction[5][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * VALUE_ROBOT_SHREDDER
				+ P_NONCONFORMITY_LEFT * valuesOld[8] + P_NONCONFORMITY_RIGHT * valuesOld[2];
		valuesToPolicyAction[5][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[5]
				+ P_NONCONFORMITY_LEFT * valuesOld[2] + P_NONCONFORMITY_RIGHT * valuesOld[8];
		valuesToPolicyAction[5][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[2]
				+ P_NONCONFORMITY_LEFT * VALUE_ROBOT_SHREDDER + P_NONCONFORMITY_RIGHT * valuesOld[5];

		// State 6
		valuesToPolicyAction[6][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[6]
				+ P_NONCONFORMITY_LEFT * valuesOld[6] + P_NONCONFORMITY_RIGHT * valuesOld[7];
		valuesToPolicyAction[6][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * valuesOld[7]
				+ P_NONCONFORMITY_LEFT * valuesOld[6] + P_NONCONFORMITY_RIGHT * valuesOld[4];
		valuesToPolicyAction[6][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[6]
				+ P_NONCONFORMITY_LEFT * valuesOld[4] + P_NONCONFORMITY_RIGHT * valuesOld[6];
		valuesToPolicyAction[6][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[4]
				+ P_NONCONFORMITY_LEFT * valuesOld[7] + P_NONCONFORMITY_RIGHT * valuesOld[6];

		// State 7
		valuesToPolicyAction[7][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[7]
				+ P_NONCONFORMITY_LEFT * valuesOld[6] + P_NONCONFORMITY_RIGHT * valuesOld[8];
		valuesToPolicyAction[7][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * valuesOld[8]
				+ P_NONCONFORMITY_LEFT * valuesOld[7] + P_NONCONFORMITY_RIGHT * valuesOld[7];
		valuesToPolicyAction[7][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[6]
				+ P_NONCONFORMITY_LEFT * valuesOld[7] + P_NONCONFORMITY_RIGHT * valuesOld[7];
		valuesToPolicyAction[7][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[7]
				+ P_NONCONFORMITY_LEFT * valuesOld[8] + P_NONCONFORMITY_RIGHT * valuesOld[6];

		// State 8
		valuesToPolicyAction[8][PolicyAction.UP.ordinal()] = P_CONFORMITY * valuesOld[8]
				+ P_NONCONFORMITY_LEFT * valuesOld[7] + P_NONCONFORMITY_RIGHT * VALUE_CHARGE_UNIT;
		valuesToPolicyAction[8][PolicyAction.RIGHT.ordinal()] = P_CONFORMITY * VALUE_CHARGE_UNIT
				+ P_NONCONFORMITY_LEFT * valuesOld[8] + P_NONCONFORMITY_RIGHT * valuesOld[5];
		valuesToPolicyAction[8][PolicyAction.LEFT.ordinal()] = P_CONFORMITY * valuesOld[7]
				+ P_NONCONFORMITY_LEFT * valuesOld[5] + P_NONCONFORMITY_RIGHT * valuesOld[8];
		valuesToPolicyAction[8][PolicyAction.DOWN.ordinal()] = P_CONFORMITY * valuesOld[5]
				+ P_NONCONFORMITY_LEFT * VALUE_CHARGE_UNIT + P_NONCONFORMITY_RIGHT * valuesOld[7];

	}

	private static double determineMaxValueForState(int stateIndex) {
		double maxValue = valuesToPolicyAction[stateIndex][0];
		policy[stateIndex] = PolicyAction.values()[0];

		for (int i = 1; i < NUMBER_OF_POLICY_ACTIONS; i++) {
			if (valuesToPolicyAction[stateIndex][i] > maxValue) {
				maxValue = valuesToPolicyAction[stateIndex][i];
				policy[stateIndex] = PolicyAction.values()[i];
			}
		}
		return maxValue;
	}

	private static void calculateVauluesNewOptimal() {
		for (int i = 0; i < DIMENSIONS; i++) {
			valuesNewOptimal[i] = REWARD + GAMMA * determineMaxValueForState(i);
		}
	}

	private static double calculateDelta() {
		// https://math.stackexchange.com/questions/1229555/how-to-find-the-two-norm-of-the-difference-between-two-vectors
		// calculating the p-norm of the old and new vectors, for p= 2 the exponent and
		// sqrt cancel each other
		double delta = 0;
		for (int i = 0; i < DIMENSIONS; i++) {
			delta += Math.pow(valuesNewOptimal[i] - valuesOld[i], 2);
		}
		return delta;
	}

	private static void updateValuesOld() {
		valuesOld = valuesNewOptimal.clone();
	}

	private static void printValueAndPolicy() {
		for (int i = 0; i < DIMENSIONS; i++) {
			System.out.println("State: " + i + " Policy: " + policy[i] + " Value: " + DF.format(valuesNewOptimal[i]));
		}
	}

	private static void printStateGrid() {
		String output = getGridBorder();
		output = getGridRow("0", "1", "2", "3") + System.lineSeparator() + output;
		output = getGridBorder() + System.lineSeparator() + output;
		output = getGridRow("4", "X", "5", "S") + System.lineSeparator() + output;
		output = getGridBorder() + System.lineSeparator() + output;
		output = getGridRow("6", "7", "8", "C") + System.lineSeparator() + output;
		output = getGridBorder() + System.lineSeparator() + output;
		System.out.println("X - Obstacle; S- Shredder; C - Charge Unit");
		System.out.println(output);
	}

	private static void printPolicyGrid() {
		String output = getGridBorder();
		output = getGridRow(policy[0].toString(), policy[1].toString(), policy[2].toString(), policy[3].toString())
				+ System.lineSeparator() + output;
		output = getGridBorder() + System.lineSeparator() + output;
		output = getGridRow(policy[4].toString(), "X", policy[5].toString(), "S") + System.lineSeparator() + output;
		output = getGridBorder() + System.lineSeparator() + output;
		output = getGridRow(policy[6].toString(), policy[7].toString(), policy[8].toString(), "C")
				+ System.lineSeparator() + output;
		output = getGridBorder() + System.lineSeparator() + output;
		System.out.println("U - UP; R - Right; D - DOWN, L - Left");
		System.out.println(output);
	}

	private static String getGridBorder() {
		return "+---+---+---+---+";
	}

	private static String getGridRow(String pos1, String pos2, String pos3, String pos4) {
		return "| " + pos1 + " | " + pos2 + " | " + pos3 + " | " + pos4 + " |";
	}

	private static void outputResults() {
		printValueAndPolicy();
		System.out.println();
		printStateGrid();
		System.out.println();

		printPolicyGrid();
	}

	public static void outputProbabilityError() {
		System.err.println("Sum of probabilities does not equal one");
	}

	public static void calculateOptimalPolicy() {

		if (P_CONFORMITY + P_NONCONFORMITY_LEFT + P_NONCONFORMITY_RIGHT == 1) {
			initialize();
			initializeValues();
			double delta;
			do {
				calculateValuesToPolicyAction();
				calculateVauluesNewOptimal();
				delta = calculateDelta();
				updateValuesOld();
			} while (delta > EPSILON);
			outputResults();
		} else
			outputProbabilityError();
	}

	public static void main(String[] args) {
		ReinforcementLearning.calculateOptimalPolicy();
	}

}
