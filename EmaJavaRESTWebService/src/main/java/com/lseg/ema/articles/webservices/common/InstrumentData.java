package com.lseg.ema.articles.webservices.common;
 

public class InstrumentData {
	public static final String LINEBREAK = "\n";
	public static final String TAB = "\t";
	private String ric;
	private String streamStatus;
	private String statusText;
	private String bid;
	private String ask;
	 
	public String getRic() {
		return ric;
	}
	public void setRic(String ric) {
		this.ric = ric;
	}
	public String getStreamStatus() {
		return streamStatus;
	}
	public void setStreamStatus(String streamStatus) {
		this.streamStatus = streamStatus;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getAsk() {
		return ask;
	}
	public void setAsk(String ask) {
		this.ask = ask;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("RIC:");
		sb.append(TAB);
		sb.append(ric);
		sb.append(LINEBREAK);
		sb.append("Status:");
		sb.append(TAB);
		sb.append(streamStatus);
		sb.append("/");
		sb.append(statusText);
		sb.append(LINEBREAK);
		sb.append("Fields:");
		sb.append(LINEBREAK);
		sb.append(TAB);
		sb.append("BID: ");
		sb.append(bid);
		sb.append(LINEBREAK);
		sb.append(TAB);
		sb.append("ASK: ");
		sb.append(ask);
		sb.append(LINEBREAK);
		 
		return sb.toString();
	}
}