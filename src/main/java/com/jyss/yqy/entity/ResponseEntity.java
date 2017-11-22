package com.jyss.yqy.entity;

public class ResponseEntity {
	private String status;

	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseEntity(String status, String message) {
		this.status = status;
		this.message = message;
	}

}
