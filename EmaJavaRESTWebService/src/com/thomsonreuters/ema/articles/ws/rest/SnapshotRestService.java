package com.thomsonreuters.ema.articles.ws.rest;

import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.thomsonreuters.ema.access.AckMsg;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.FieldEntry;
import com.thomsonreuters.ema.access.GenericMsg;
import com.thomsonreuters.ema.access.Msg;
import com.thomsonreuters.ema.access.OmmConsumerClient;
import com.thomsonreuters.ema.access.OmmConsumerEvent;
import com.thomsonreuters.ema.access.RefreshMsg;
import com.thomsonreuters.ema.access.ReqMsg;
import com.thomsonreuters.ema.access.StatusMsg;
import com.thomsonreuters.ema.access.UpdateMsg;
import com.thomsonreuters.ema.articles.ws.common.Consumer;
import com.thomsonreuters.ema.articles.ws.common.Instrument;

@Path("/snapshotRestService")
public class SnapshotRestService implements OmmConsumerClient {
	private static ReqMsg reqMsg = EmaFactory.createReqMsg();
	private Instrument result = new Instrument();
	private Msg msg = null;
	
	@GET
	@Path("{ric}")
	@Produces(MediaType.APPLICATION_JSON) 
	public Instrument subscribe(@PathParam("ric") String ric) {
		synchronized (result) {
			System.out.println("ric: " + ric);
			if (ric == null || ric.length() <= 0) throw new RuntimeException("An item name is required!");
			msg = null;
			result.clear();
			Consumer.getConsumer().registerClient(reqMsg.serviceName("API_ELEKTRON_EPD_RSSL").name(ric).interestAfterRefresh(false), this);
			
			try {
				result.wait(5000);
				
				result.setRic(ric);
				if (msg instanceof RefreshMsg) {
					RefreshMsg refreshMsg = (RefreshMsg) msg;
					result.setSuccess(true);
					result.setStatusText(refreshMsg.state().statusText());
					
					Iterator<FieldEntry> iter = refreshMsg.payload().fieldList().iterator();
					FieldEntry fieldEntry;
					while (iter.hasNext())
					{
						fieldEntry = iter.next();
						System.out.println("Fid: " + fieldEntry.fieldId() + " Name: " + fieldEntry.name() + " value: " + fieldEntry.load());
						switch (fieldEntry.name()) {
						case "BID":
							result.setBid(fieldEntry.real().toString());
							break;
						case "ASK":
							result.setAsk(fieldEntry.real().toString());
							break;
						case "TRDPRC_1":
							result.setTradePrice(fieldEntry.real().toString());
							break;
						}
					}
				} else if (msg instanceof StatusMsg) {
					StatusMsg statusMsg = (StatusMsg) msg;
					result.setSuccess(false);
					result.setStatusText(statusMsg.state().statusText());
				}
				
			} catch (RuntimeException | InterruptedException e) {
				result.setSuccess(false);
				result.setStatusText(e.getMessage());
			}
			return result;
		}
	}
	
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
//		System.out.println(refreshMsg);
		msg = refreshMsg;
		synchronized (result) {
			result.notify();
		}
		
	}
	
	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) 
	{
//		System.out.println(updateMsg);
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) 
	{
//		System.out.println(statusMsg);
		msg = statusMsg;
		synchronized (result) {
			result.notify();
		}
	}
	
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}
}
