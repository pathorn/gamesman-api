package edu.berkeley.gamesman.api;

public class ApiPositionValue {
	public String position;
	public String value;
	public Integer remoteness;

	static protected int getPlayer(String position) {
		if (position == null) {
			return -1;
		}
		int playerSep = position.indexOf(':');
		int player = 0;
		if (playerSep != -1) {
			player = Integer.parseInt(position.substring(0, playerSep));
		}
		return player;
	}

	public ApiPositionValue(String pos, String v, Integer r) {
		position = pos;
		value = v.toLowerCase();
		remoteness = r;
	}
}
