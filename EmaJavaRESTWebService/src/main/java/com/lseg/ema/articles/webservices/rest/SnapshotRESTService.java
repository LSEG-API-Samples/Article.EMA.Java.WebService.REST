package com.lseg.ema.articles.webservices.rest;
 
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
 
import com.refinitiv.ema.access.AckMsg;
import com.refinitiv.ema.access.DataType;
import com.refinitiv.ema.access.EmaFactory;
import com.refinitiv.ema.access.FieldEntry;
import com.refinitiv.ema.access.GenericMsg;
import com.refinitiv.ema.access.Msg;
import com.refinitiv.ema.access.OmmConsumer;
import com.refinitiv.ema.access.OmmConsumerClient;
import com.refinitiv.ema.access.OmmConsumerEvent;
import com.refinitiv.ema.access.RefreshMsg;
import com.refinitiv.ema.access.ReqMsg;
import com.refinitiv.ema.access.StatusMsg;
import com.refinitiv.ema.access.UpdateMsg;
import com.lseg.ema.articles.webservices.common.ConnectionContext;
import com.lseg.ema.articles.webservices.common.InstrumentData;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType; 

@Path("/SnapshotRESTService")
public class SnapshotRESTService {
	public static final int TIMEOUT = 5;
	public static final String SERVICE_NAME = "ELEKTRON_DD";
	private InstrumentData data = new InstrumentData();
	
	// add more two following methods.
	@Path("/subscribeTextPlain/{ric}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String subscribeTextPlain(@PathParam("ric") String ric) {
		return subscribe(ric).toString();
	}

	@Path("/subscribeJson/{ric}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public InstrumentData subscribeJson(@PathParam("ric") String ric) {
		return subscribe(ric);
	}
	
	
	public synchronized InstrumentData subscribe(String ric) {
		
		synchronized (data) {
		AppClient appClient = new AppClient();
			OmmConsumer consumer = ConnectionContext.getConsumer();
			ReqMsg reqMsg = EmaFactory.createReqMsg();
			reqMsg.clear();
			 
			try {
				if (consumer == null) throw new RuntimeException("Connection: Cannot retrieve OmmConsumer instance. Unable to make a connection to server.");
				consumer.registerClient(reqMsg.serviceName(SERVICE_NAME).name(ric).interestAfterRefresh(false), appClient);
				data.wait(TIMEOUT * 1000);
				if (data == null) throw new TimeoutException("Timeout: Cannot retrieve data within " + TIMEOUT + " seconds");
			} catch (Exception e) {
				data.setRic(ric);
				data.setStreamStatus("SUSPECT");
				data.setStatusText(e.getClass().getName() + ":" + e.getMessage());
			}
			return data;
		}
	}
 
class AppClient implements OmmConsumerClient
{
	public static final int FID_BID = 22;
	public static final int FID_ASK = 25;
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		synchronized (data) {
			data.setRic(refreshMsg.name());
			data.setStreamStatus(refreshMsg.state().dataStateAsString());
			data.setStatusText(refreshMsg.state().statusText());
			if (DataType.DataTypes.FIELD_LIST == refreshMsg.payload().dataType()) {
			Iterator<FieldEntry> iter = refreshMsg.payload().fieldList().iterator();
				FieldEntry fieldEntry;
				while (iter.hasNext())
				{
					fieldEntry = iter.next();
					if (fieldEntry.fieldId() == FID_BID) {
						data.setBid(fieldEntry.load().toString());
						continue;
					} else if (fieldEntry.fieldId() == FID_ASK) {
						data.setAsk(fieldEntry.load().toString());
						continue;
					} else if (data.getBid() != null && data.getAsk() != null) break;
				}
			}
			data.notify();
		}
	}
 
	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) 
	{
		data.setRic(statusMsg.name());
		data.setStreamStatus(statusMsg.state().dataStateAsString());
		data.setStatusText(statusMsg.state().statusText());
	}
 
	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {}
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}
	
	
	
	}
}