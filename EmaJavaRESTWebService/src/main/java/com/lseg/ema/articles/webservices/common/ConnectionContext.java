package com.lseg.ema.articles.webservices.common;
 
import com.refinitiv.ema.access.EmaFactory;
import com.refinitiv.ema.access.OmmConsumer;
import com.refinitiv.ema.access.OmmConsumerConfig;
import com.refinitiv.ema.access.OmmException;
 
public class ConnectionContext {
public static final String HOST = "127.0.0.1:14002";
	public static final String USER = "user01";
	private static OmmConsumer consumer = null;
	 
	static {
	try
		{
			OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();
			consumer  = EmaFactory.createOmmConsumer(config.host(HOST).username(USER));
		}
		catch (OmmException excp)
		{
			excp.printStackTrace();
		}
	}
	 
	private ConnectionContext() {}
	 
	public static OmmConsumer getConsumer() {
		return consumer;
	}
 
}