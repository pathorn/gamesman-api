package edu.berkeley.gamesman.api;

import edu.berkeley.gamesman.api.GamesmanApi;
import edu.berkeley.gamesman.avro.Fields;
import edu.berkeley.gamesman.avro.GamesmanProvider;
import edu.berkeley.gamesman.avro.PositionValue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.ipc.AvroRemoteException;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.specific.SpecificRequestor;

public class AvroApi implements GamesmanApi {
	public static final String GAMESMAN_JAVA = "http://nyc.cs.berkeley.edu:8042/";

	private GamesmanProvider connect() throws RequestException {
		try {
			return (GamesmanProvider)SpecificRequestor.getClient(
					GamesmanProvider.class,
					new HttpTransceiver(new URL(GAMESMAN_JAVA)));
		} catch (MalformedURLException e) {
			throw new RequestException("Gamesman Java URI invalid "+GAMESMAN_JAVA, e);
		} catch (IOException e) {
			throw new RequestException("Failed to connect to HttpTransceiver at "+GAMESMAN_JAVA, e);
		}
	}
	
	public static Fields getDefaultFields() {
		Fields ret = new Fields();
		ret.value = true;
		ret.remoteness = true;
		ret.winBy = true;
		return ret;
	}
	
	@Override
	public Map<String, ApiPositionValue>
	getNextPositionValues(GameVariant var, String board)
	throws RequestException {
		GamesmanProvider req = connect();
		try {
			HashMap<String, ApiPositionValue> resultMap = new HashMap<String, ApiPositionValue>();
			for (Map.Entry<CharSequence, PositionValue> ent :
					req.getNextPositionValues(
							var.getGame(), var.getVariant(),
							board, getDefaultFields()).entrySet()) {
				ApiPositionValue pv = new ApiMoveValue(
						board,
						ent.getValue().position.toString(),
						ent.getValue().value.toString(),
						ent.getValue().remoteness);
				resultMap.put(ent.getKey().toString(), pv);
			}
			return resultMap;
		} catch (AvroRemoteException e) {
			throw new RequestException("Failed to call RPC getNextPositionValues", e);
		}
	}

	@Override
	public ApiPositionValue
	getInitialPositionValue(GameVariant var)
	throws RequestException {
		GamesmanProvider req = connect();
		try {
			PositionValue val = req.getInitialPositionValue(
					var.getGame(), var.getVariant(),
					getDefaultFields());
			ApiPositionValue pv = new ApiPositionValue(
					val.position.toString(),
					val.value.toString(),
					val.remoteness);
			return pv;
		} catch (AvroRemoteException e) {
			throw new RequestException("Failed to call RPC getNextPositionValues", e);
		}
	}

	@Override
	public Map<String, ApiPositionValue>
	getPositionValues(GameVariant var, List<String> boards)
	throws RequestException {
		GamesmanProvider req = connect();
		List<CharSequence> boardsCopy = Collections.<CharSequence>unmodifiableList(boards);
		try {
			HashMap<String, ApiPositionValue> resultMap = new HashMap<String, ApiPositionValue>();
			for (Map.Entry<CharSequence, PositionValue> ent :
					req.getPositionValues(
							var.getGame(), var.getVariant(),
							boardsCopy, getDefaultFields()).entrySet()) {
				ApiPositionValue pv = new ApiPositionValue(
						ent.getValue().position.toString(),
						ent.getValue().value.toString(),
						ent.getValue().remoteness);
				resultMap.put(ent.getKey().toString(), pv);
			}
			return resultMap;
		} catch (AvroRemoteException e) {
			throw new RequestException("Failed to call RPC getNextPositionValues", e);
		}
	}

}
