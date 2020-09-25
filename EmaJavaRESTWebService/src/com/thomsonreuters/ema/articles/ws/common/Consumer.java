package com.thomsonreuters.ema.articles.ws.common;

import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmConsumerConfig;
import com.thomsonreuters.ema.access.OmmException;

public class Consumer {
	public static final String HOST = "192.168.27.46:14002";
	public static final String USER = "192.168.27.46:14002";
	
	private static OmmConsumer consumer = null;
	static {
		try
		{
			OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();
			consumer  = EmaFactory.createOmmConsumer(config.host(HOST).username(USER));
		}
		catch (OmmException excp)
		{
			System.out.println(excp.getMessage());
		}
		finally 
		{
//			if (consumer != null) consumer.uninitialize();
		}
	}
	
	private Consumer() {}
	
	public static OmmConsumer getConsumer() {
		return consumer;
	}
}
