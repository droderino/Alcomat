package com.alcomat.test;

import static org.junit.Assert.*;

import java.util.List;

import org.json.simple.JSONObject;
import org.junit.Test;

import com.alcomat.protocol.ProtocolElement;
import com.alcomat.protocol.ProtocolKeys;
import com.alcomat.protocol.ProtocolWriter;

public class ProtocolTest {

	@Test
	public void test() {
		ProtocolElement gameElement = setUpSampleElement();
		
		ProtocolWriter writer = new ProtocolWriter();
		String output = writer.fromProtocolElement(gameElement);
		
		System.out.println(output);
		
		List<ProtocolElement> elements = writer.getProtocolElements(output);
		ProtocolElement element = elements.get(0);
		
		assertEquals(element.getIdentifier(), gameElement.getIdentifier());
		assertEquals(element.getObject().toJSONString(), gameElement.getObject().toJSONString());
		
		assertTrue(element.getObject().containsKey(ProtocolKeys.game_antwort));
	}

	private ProtocolElement setUpSampleElement() {
		JSONObject gameSample = new JSONObject();
		gameSample.put(ProtocolKeys.game_gameId, 1);
		gameSample.put(ProtocolKeys.game_frage, "foo");
		gameSample.put(ProtocolKeys.game_antwort, "bar");
		
		ProtocolElement gameElement = new ProtocolElement(ProtocolKeys.identifier_game, gameSample);
		return gameElement;
	}

}
