package com.thomsonreuters.ema.articles.ws.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Instrument {
	private String ric;
	private String bid;
	private String ask;
	private String tradePrice;
	private boolean isSuccess;
	private String statusText;
	public String getRic() {
		return ric;
	}
	public void setRic(String ric) {
		this.ric = ric;
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
	public String getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(String tradePrice) {
		this.tradePrice = tradePrice;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public void clear() {
		setRic(null);
		setBid(null);
		setAsk(null);
		setTradePrice(null);
		setSuccess(false);
		setStatusText("Initialized");
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("RIC: ");
		s.append(getRic());
		s.append("{");
		s.append("\n\tSuccess: ");
		s.append(isSuccess);
		s.append("\n\tStatusText: ");
		s.append(getStatusText());
		s.append("\n\tBID: ");
		s.append(getBid());
		s.append("\n\tASK: ");
		s.append(getAsk());
		s.append("\n\tTRADE_PRICE: ");
		s.append(getTradePrice());
		s.append("\n}");
		return s.toString();
	}
}
