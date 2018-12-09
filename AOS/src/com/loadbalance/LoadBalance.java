package com.loadbalance;

import java.util.ArrayList;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.juddi.v3.client.config.UDDIClerk;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.config.UDDIClientContainer;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;

import com.publish.*;




public class LoadBalance {
	
	// servers list is the list of servers we are going to load balance across 
	
	public static ArrayList<ServerObject> servers = new ArrayList<ServerObject>();

	
	
	public static void main(String[] args) {
		
		
		Find findServers = new Find();
		
		
		findServers.find();
		
		
		//Parameters - int port, String host, int maxConnections, double rating
		
		
		servers.add(new ServerObject(8081, "localhost", 4000, 1.00));
		servers.add(new ServerObject(8082, "server1.com", 4000, 1.00));
	
		
		
		
		// Construct listener and select port here
		
		
		LoadBalanceListener listener = new LoadBalanceListener(15100);
		
		
		System.out.println("load balancer configure");
		
		// Pass in configuration options here
		listener.configure();
		
		
		System.out.println("load balancer run");
		
		Thread listenerThread = new Thread(listener);
		listenerThread.run();
		
		System.out.println("load balancer end");
		
		
			

	}

}
