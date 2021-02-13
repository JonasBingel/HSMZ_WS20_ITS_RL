package reinforcementLearning;

public enum PolicyAction {
	UP("U"), RIGHT("R"), LEFT("L"), DOWN("D");

	private final String output;

	PolicyAction(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return this.output;
	}

}
