package com.alcomat.protocol;

import org.json.simple.JSONObject;

public class ProtocolElement {

	private String identifier;
	private JSONObject object;
	
	public ProtocolElement(){ }
	
	public ProtocolElement(String name, JSONObject object)
	{
		this.identifier = name;
		this.object = object;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public JSONObject getObject() {
		return object;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setObject(JSONObject object) {
		this.object = object;
	}
}
