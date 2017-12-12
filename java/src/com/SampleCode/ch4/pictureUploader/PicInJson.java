package com.SampleCode.ch4.pictureUploader;

import org.json.JSONObject;

public class PicInJson extends JSONObject {

	
	public PicInJson(String image_id, String user_id, String image_path) {
		super();
		this.put("image_id", image_id);
		this.put("user_id", user_id);
		this.put("image_path", image_path);
	}
	
	
	public byte[] getBytes() {
		return toString( ).getBytes();
	}
	
	
	
	
	
}
