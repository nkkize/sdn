/**
 * 
 */
package com.talentica.sdn.model;

/**
 * @author NarenderK
 *
 */
public class DataDictionary {

	private String timeString;

	private int byteCount;

	private int tpSource;
	
	private String newSource;
	
	private String newDest;
	
	private int tpDest;

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public int getByteCount() {
		return byteCount;
	}

	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}

	public int getTpSource() {
		return tpSource;
	}

	public void setTpSource(int tpSource) {
		this.tpSource = tpSource;
	}

	public String getNewSource() {
		return newSource;
	}

	public void setNewSource(String newSource) {
		this.newSource = newSource;
	}

	public String getNewDest() {
		return newDest;
	}

	public void setNewDest(String newDest) {
		this.newDest = newDest;
	}

	public int getTpDest() {
		return tpDest;
	}

	public void setTpDest(int tpDest) {
		this.tpDest = tpDest;
	}
}
