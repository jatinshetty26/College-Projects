package com.publish;

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

public class Find {
	
	public List find() {
		
		List<String> loadList = new ArrayList();
		
		try {
			
			System.out.println("Starting find");
			
			
			
			UDDIClient uddiClient = new UDDIClient("META-INF/uddi.xml");
			
			System.out.println("The UDDI Client : "+uddiClient);
			
			UDDIClerk clerk = uddiClient.getClerk("default");
			
			System.out.println("The clerk : "+clerk);
        	
        	//BusinessEntity businessEntity = clerk.findBusiness("uddi:www.umbcproject.com:testkey");
        	
        	BusinessEntity businessEntity = clerk.getBusinessDetail("uddi:www.umbcproject.com:testkey");
        	
        	//clerk.getBusinessDetail(businessKey)
        	
        	System.out.println("businessEntity : "+businessEntity);
        	
        	//
        	if (businessEntity!=null) {
        		
	        	System.out.println("Found business with name " + businessEntity.getName().get(0).getValue());
	        	
	        	
	        	if (businessEntity.getBusinessServices() !=null) {
	        		
	        		
		        	System.out.println("Number of services: " + businessEntity.getBusinessServices().getBusinessService().size());
		        	
		        	List<BusinessService> listBS = businessEntity.getBusinessServices().getBusinessService();
		        	
		       
		        	for(BusinessService bsEach : listBS) {
		        		
		        		System.out.println("Each : "+bsEach.getName().get(0).getValue());
		        		
		        		 BindingTemplates bt = bsEach.getBindingTemplates();
		        		 
		        		 System.out.println("the BT : "+bt);
		        		 
		        		 
		        		List<BindingTemplate> btList = bt.getBindingTemplate();
		        		
		        		System.out.println("the btList : "+btList);
		        		
		        		
		        		
		        		
		        		for(BindingTemplate btListEach : btList) {
		        			
		        			System.out.println("The access point : " +btListEach.getAccessPoint());
		        			
		        			System.out.println("The value : " +btListEach.getAccessPoint().getValue());
		        			
		        			loadList.add(btListEach.getAccessPoint().getValue().replace("?wsdl", ""));
		        			
		        		}
		        		 
		        		
		        		
		        		
		        		
		        	}
		        	
		        	System.out.println("The list sent back to the load balancer : "+loadList);
		        	
		        	
		        	
		        
	        	}else {
	        		
	        		System.out.println("The business has no services");
	        		
	        	}
        	}else {
        		
        		System.out.println("Looks like its null");
        		
        	}
	        		
        	businessEntity.getBusinessServices();
        	
        	//TODO JUDDI-610
			//FindTModel findBindingTModel = WSDL2UDDI.createFindBindingTModelForPortType(portType, namespace);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	
		return loadList;
	}		

	public static void main (String args[]) {
		Find sp = new Find();
		sp.find();	
	}
}
