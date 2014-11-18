package com.itheima.mobilesafe.domain;

public class BlackNumberInfo {

	private String number;
	private String model;
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	@Override
	public String toString() {
		return "BlackNumberInfo [number=" + number + ", model=" + model + "]";
	}
	
}
