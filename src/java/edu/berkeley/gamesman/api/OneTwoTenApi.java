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
			ret.put("1", new ApiPositionValue("10", "lose"));
		} else {
			ret.put("1", new ApiPositionValue(Integer.toString(value + 1), (value%3==0) ? "lose" : "win"));
			ret.put("2", new ApiPositionValue(Integer.toString(value + 2), (value%3==2) ? "lose" : "win"));
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
				ret.put(boardStr, new ApiPositionValue(boardStr, "lose"));
			} else if (value % 3 != 1) {
				ret.put(boardStr, new ApiPositionValue(boardStr, "win"));
			} else {
				ret.put(boardStr, new ApiPositionValue(boardStr, "lose"));
			}
		}
		return ret;
	}

	@Override
	public ApiPositionValue
	getInitialPositionValue(GameVariant var)
	throws RequestException {
		return new ApiPositionValue("0", "win");
	}

}
