package edu.berkeley.gamesman.api;

import java.util.HashMap;
import java.util.Map;

public class GameVariant {
	public static class VersionNotFoundException extends Exception {
		private static final long serialVersionUID = 6840075088359858277L;

		public VersionNotFoundException(Map<String, String> variant) {
			super("Variant not found in "+variant);
		}
		public VersionNotFoundException(String badVersion) {
			super("Variant not found in "+badVersion);
		}
	}
	
	public static class UnsupportedVersionException extends Exception {
		private static final long serialVersionUID = -4752678163470363042L;

		public UnsupportedVersionException(String gameName, int badVersion) {
			super("Version "+badVersion+" not supported for game "+gameName);
		}
	}
	
	public static String VERSION_KEY = "v";
	
	public static int getVersion(Map<String, String> variant)
			throws VersionNotFoundException {
		String versionStr;
		try {
			versionStr = variant.get(VERSION_KEY);
			try {
				return Integer.parseInt(versionStr);
			} catch (NumberFormatException e) {
				throw new VersionNotFoundException(versionStr);
			}
		} catch (NullPointerException e) {
			throw new VersionNotFoundException(variant);
		}
	}

	public static GameVariant makeVersioned(String game,
			Map<String, String> variant)
			throws VersionNotFoundException, UnsupportedVersionException {
		Map<CharSequence, CharSequence> varMap =
			new HashMap<CharSequence, CharSequence>();
		int version = getVersion(variant);
		if (version != 0) {
			throw new UnsupportedVersionException(game, version);
		}
		for (String key : variant.keySet()) {
			varMap.put(key, variant.get(key));
		}
		return new GameVariant(game, varMap);
	}

	private Map<CharSequence, CharSequence> variant;
	private String game;

	public GameVariant(String game,
			Map<CharSequence, CharSequence> variant) {
		this.game = game;
		this.variant = variant;
	}

	String getGame() {
		return this.game;
	}
	
	Map<CharSequence, CharSequence> getVariant() {
		return this.variant;
	}

	public Map<CharSequence, CharSequence> getForVersion(int version)
			throws UnsupportedVersionException {
		if (version != 0) {
			throw new UnsupportedVersionException(this.game, version);
		}
		return this.variant;
	}

}
