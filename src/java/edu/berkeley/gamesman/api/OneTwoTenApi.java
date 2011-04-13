package edu.berkeley.gamesman.api;

import edu.berkeley.gamesman.api.GamesmanApi;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

public class OneTwoTenApi implements GamesmanApi {

	@Override
	public Map<String, ApiPositionValue>
	getNextPositionValues(GameVariant var, String board)
	throws RequestException {
		Map<String, ApiPositionValue> ret = new TreeMap<String, ApiPositionValue>();
		int value = 0;
		if (board != null) {
			value = Integer.parseInt(board);
		}
		if (value == 10) {
		} else if (value == 9) {
			ret.put("1", new ApiMoveValue(board, "10", "lose", null));
		} else {
			ret.put("1", new ApiMoveValue(board, Integer.toString(value + 1), (value%3==0) ? "lose" : "win", null));
			ret.put("2", new ApiMoveValue(board, Integer.toString(value + 2), (value%3==2) ? "lose" : "win", null));
		}
		return ret;
	}

	@Override
	public Map<String, ApiPositionValue>
	getPositionValues(GameVariant var, List<String> boards)
	throws RequestException {
		Map<String, ApiPositionValue> ret = new HashMap<String, ApiPositionValue>();
		int value = 0;
		for (String board : boards) {
			if (board != null) {
				value = Integer.parseInt(board);
			}
			String boardStr = Integer.toString(value);
			if (value == 10) {
				ret.put(boardStr, new ApiPositionValue(boardStr, "lose", null));
			} else if (value % 3 != 1) {
				ret.put(boardStr, new ApiPositionValue(boardStr, "win", null));
			} else {
				ret.put(boardStr, new ApiPositionValue(boardStr, "lose", null));
			}
		}
		return ret;
	}

	@Override
	public ApiPositionValue
	getInitialPositionValue(GameVariant var)
	throws RequestException {
		return new ApiPositionValue("0", "win", null);
	}

}
