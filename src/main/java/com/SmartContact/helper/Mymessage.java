package com.SmartContact.helper;

public class Mymessage {
     
	
    private String content;
	private String type;
	public Mymessage(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	@Override
	public String toString() {
		return "Mymessage [content=" + content + ", type=" + type + "]";
	}
	
	public Mymessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
}
