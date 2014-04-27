package com.alcomat.protocol;

import java.util.ArrayList;
import java.util.List;

public class ProtocolKeys {

	public static final String type = "Type";
	public static final String identifier_game = "Game";
	public static final String identifier_settings = "Settings";
	public static final String identifier_command = "Command";
	
	public static final String game_timestamp = "Timestamp";
	public static final String game_gameId ="GameId";
	public static final String game_frageId = "FrageId";
	public static final String game_frage = "Frage";
	public static final String game_antwort = "Antwort";
	public static final String game_validation = "Validation";
	public static final String game_rankting = "Ranking";
	
	public static final String settings_roomId = "RoomId";
	public static final String settings_name = "Name";
	public static final String settings_interval = "Interval";
	public static final String settings_active = "Active";
	
	public static final String command_commando = "Commando";
	public static final String command_values = "Values";
	
	public static List<String> getIdentifiers()
	{
		List<String> identifiers = new ArrayList<String>();
		identifiers.add(identifier_command);
		identifiers.add(identifier_game);
		identifiers.add(identifier_settings);
		return identifiers;
	}
}
