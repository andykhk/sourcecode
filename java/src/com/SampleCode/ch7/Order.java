package com.SampleCode.ch7;

import org.json.JSONObject;

public class Order extends JSONObject {

	public Order (int ordNum, String type) {
		super();
		put("ordernum", ordNum);
		put("type", type);
	}
	
}
