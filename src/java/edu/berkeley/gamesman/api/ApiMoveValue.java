package edu.berkeley.gamesman.api;

public class ApiMoveValue extends ApiPositionValue {
	public String moveValue;

	public ApiMoveValue(String parentPosition, String pos, String v, Integer r) {
		super(pos, v, r);

		int player = getPlayer(pos);
		moveValue = null;
		if (parentPosition != null) {
			moveValue = value;
			if (player != getPlayer(parentPosition)) {
				if (value.equals("win")) {
					moveValue = "lose";
				} else if (value.equals("lose")) {
					moveValue = "win";
				}
			}
		}
	}
}
