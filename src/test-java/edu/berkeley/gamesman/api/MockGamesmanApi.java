package edu.berkeley.gamesman.api;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MockGamesmanApi implements GamesmanApi {

	@Override
	public Map<String, ApiPositionValue>
	getNextPositionValues(GameVariant var, String board)
	throws RequestException {
		Map<String, ApiPositionValue> ret = new TreeMap<String, ApiPositionValue>();
		ret.put("null", new ApiMoveValue("foo", "test", "awesome", null));
		return ret;
	}

	@Override
	public Map<String, ApiPositionValue>
	getPositionValues(GameVariant var, List<String> boards)
	throws RequestException {
		Map<String, ApiPositionValue> ret = new TreeMap<String, ApiPositionValue>();
		ret.put("test", new ApiPositionValue("test", "awesome", null));
		return ret;
	}

	@Override
	public ApiPositionValue
	getInitialPositionValue(GameVariant var)
	throws RequestException {
		return new ApiPositionValue("test", "awesome", null);
	}
}
