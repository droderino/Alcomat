package com.alcomat.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ProtocolWriter {

	@SuppressWarnings("unchecked")
	public String fromProtocolElement(ProtocolElement element)
	{
		String output = null;
		
		JSONObject object = new JSONObject();
		object.put(element.getIdentifier(), element.getObject());
		
		output = object.toJSONString();
		
		return output;
	}
	
	public String fromProtocolElements(List<ProtocolElement> elements)
	{
		String output = new String();
		
		for(ProtocolElement e : elements)
			output.concat(this.fromProtocolElement(e));
		
		return output;
	}
	
	public List<ProtocolElement> getProtocolElements(String input)
	{
		List<ProtocolElement> elements = new ArrayList<ProtocolElement>();
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(input);
			JSONObject json = (JSONObject)obj;
			
			for(String s : this.parseIdentifiers(json))
				elements.add( new ProtocolElement(s, (JSONObject)json.get(s)) );
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return elements;
	}
	
	private List<String> parseIdentifiers(JSONObject obj)
	{
		List<String> identifiers = ProtocolKeys.getIdentifiers();
		List<String> containing = new ArrayList<String>();
		
		for(String s : identifiers)
		{
			if(obj.containsKey(s))
				containing.add(s);
		}
		return containing;
	}
}
