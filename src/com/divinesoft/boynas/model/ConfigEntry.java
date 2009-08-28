package com.divinesoft.boynas.model;

public class ConfigEntry {
	String macAddress;
	String extension;
	String password;
	String lines;
	
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLines() {
		return lines;
	}
	public void setLines(String lines) {
		this.lines = lines;
	}
	
	@Override
	public String toString(){
		return 	"[MAC: " + macAddress +
				", EXT: "+extension+
				", PAS: "+password+
				", LIN: "+lines+"]";
	}
}
