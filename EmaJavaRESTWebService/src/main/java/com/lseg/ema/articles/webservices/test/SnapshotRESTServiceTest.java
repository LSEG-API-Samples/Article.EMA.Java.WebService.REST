package com.lseg.ema.articles.webservices.test;
 
import com.lseg.ema.articles.webservices.common.InstrumentData;
import com.lseg.ema.articles.webservices.rest.SnapshotRESTService;
 
public class SnapshotRESTServiceTest {
	public static void main(String[] args) {
		SnapshotRESTService obj = new SnapshotRESTService();
		InstrumentData result = obj.subscribe("LSEG.L");
		System.out.println(result);
	}
}