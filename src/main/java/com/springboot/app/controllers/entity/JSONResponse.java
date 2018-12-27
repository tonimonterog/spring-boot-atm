package com.springboot.app.controllers.entity;

import java.util.ArrayList;
import java.util.List;

public class JSONResponse {
	private String status;
	private Object resp;
	private List<Error> errors;
	
	
	public JSONResponse() {
		errors = new ArrayList<Error>();
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getResp() {
		return resp;
	}
	public void setResp(Object resp) {
		this.resp = resp;
	}
	public List<Error> getErrors() {
		return errors;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	public void addError(Error error) {
		this.errors.add(error);
	}
}
