package edu.berkeley.gamesman.api;

import java.util.List;
import java.util.Map;


/**
 * The core interface that defines the back-end Gamesman API.
 * <p>
 * Ideally, implementations of the API will be thread-safe since a single API
 * object may need to service multiple concurrent requests. In the event that
 * an API implementation is <em>not</em> thread-safe, users of the class must
 * synchronize access to the API.
 *
 * @author James Ide
 */
public interface GamesmanApi {
	
	public static class RequestException extends Exception {
		private static final long serialVersionUID = -5206159430755716630L;

		public RequestException(String msg, Exception e) {
			super(msg, e);
		}
	}

	public Map<String, ApiPositionValue> getNextPositionValues(GameVariant var, String board) throws RequestException;

	public ApiPositionValue getInitialPositionValue(GameVariant var) throws RequestException;

	public Map<String, ApiPositionValue> getPositionValues(GameVariant var, List<String> board) throws RequestException;
}
