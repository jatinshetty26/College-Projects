
package com.publish;

import org.uddi.api_v3.*;
import org.apache.juddi.api_v3.*;
import org.apache.juddi.v3.client.config.UDDIClerk;
import org.apache.juddi.v3.client.config.UDDIClient;


import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class SimplePublishClerk {

        private static UDDIClerk clerk = null;


        public  SimplePublishClerk() {
        	
        	System.out.println("***************************** Starting Registration to jUDDI **************************");
        	
			
			
			
			 try {
                	System.out.println("Reading configuration from uddi.xml ************");
                      
                        UDDIClient uddiClient = new UDDIClient("META-INF/uddi.xml");
                        System.out.println("***************uddiClient "+uddiClient);
                      
						System.out.println("Getting the user that will be used to publish the webservice");
                        clerk = uddiClient.getClerk("default");
                        System.out.println("***************clerk " +clerk);
                        
                        
                        if (clerk==null)
                                throw new Exception("the clerk wasn't found, check the config file!");
            
				
                        System.out.println("Starting business logic");
                       
                	
	                	System.out.println("************* Checking if the business already exists");
	                	
	                	System.out.println("Utilizing uddi:www.umbcproject.com:testkey as the business");
	                	BusinessEntity businessEntity = clerk.getBusinessDetail("uddi:www.umbcproject.com:testkey");
	                	
	                	
	                	System.out.println("******* The Business *******" +businessEntity);                	
	                	 
	                	
	                	BusinessEntity register = new BusinessEntity();
	                	
	                	
                	 
                	 
                	if(null==businessEntity){
                		
					System.out.println("***************** The business does not exist. Creating a new one.");
					System.out.println("For the purposes of this project we will add all services to the umbcproject business");
                	
                	
                        BusinessEntity myBusEntity = new BusinessEntity();
                        Name myBusName = new Name();
                        myBusName.setValue("UMBC Business");
                        myBusEntity.getName().add(myBusName);
                        myBusEntity.setBusinessKey("uddi:www.umbcproject.com:testKey");
                        System.out.println("Adding the business entity to the structure, using our publisher's authentication info and saving away." +clerk);
						
						
						
                        register = clerk.register(myBusEntity);
                        
                        	System.out.println("+++++++++++=register : "+register);
						
                        if (register == null) {
                                System.out.println("Save failed!");
                                //System.exit(1);
                        }
						
                        
			}else {
				
				System.out.println("The business already exists in jUDDI.");
				System.out.println("Registering the service to the existing business."); 
				
				register = clerk.register(businessEntity);
				 
				 System.out.println("+++++++++++=register : "+register);
					
                 if (register == null) {
                         System.out.println("Save failed!");
                         //System.exit(1);
                 }
					
			}
						
					
						System.out.println("+++++++++++=1");
						
                        String myBusKey = register.getBusinessKey();
                        System.out.println("myBusiness key:  " + myBusKey);

                        // Creating a service to save.  Only adding the minimum data: the parent business key retrieved from saving the business 
                        // above and a single name.
                        BusinessService myService = new BusinessService();
                        myService.setBusinessKey(myBusKey);
						
						System.out.println("+++++++++++=2");
						
                        Name myServName = new Name();
                        myServName.setValue("My Auto Service1");
                        myService.getName().add(myServName);
						
						System.out.println("+++++++++++=3");
						
						
						
						
						System.out.println("Getting IP address to construct the URL");
						
						
						
						
						
						
						
						 InetAddress ip = null;
					        String hostname;
					        try {
					            ip = InetAddress.getLocalHost();
					            hostname = ip.getHostName();
					            System.out.println("Your current IP address : " + ip.getHostAddress());
					            System.out.println("Your current Hostname : " + hostname);
					 
					        } catch (UnknownHostException e) {
					 
					            e.printStackTrace();
					        }
						
						
						
						
						
						
						
						

                        // Add binding templates, etc...
                        BindingTemplate myBindingTemplate = new BindingTemplate();
                        AccessPoint accessPoint = new AccessPoint();
                        accessPoint.setUseType(AccessPointType.WSDL_DEPLOYMENT.toString());
                        accessPoint.setValue("http://"+ip.getHostAddress()+":8081/12_7AOS/services/newservice?wsdl");
                        myBindingTemplate.setAccessPoint(accessPoint);
						
						System.out.println("+++++++++++=4");
						
						
                        BindingTemplates myBindingTemplates = new BindingTemplates();
                        //optional but recommended step, this annotations our binding with all the standard SOAP tModel instance infos
                        myBindingTemplate = UDDIClient.addSOAPtModels(myBindingTemplate);
                        myBindingTemplates.getBindingTemplate().add(myBindingTemplate);
                        myService.setBindingTemplates(myBindingTemplates);
                        // Adding the service to the "save" structure, using our publisher's authentication info and saving away.
                        BusinessService svc = clerk.register(myService);
                        if (svc==null){
                                System.out.println("Save failed!");
                                System.exit(1);
                        }
                        
                        String myServKey = svc.getServiceKey();
                        System.out.println("myService key:  " + myServKey);

                        clerk.discardAuthToken();
                        // Now you have a business and service via 
                        // the jUDDI API!
                        System.out.println("Success!");

                } catch (Exception e) {
                        e.printStackTrace();
                }
				
			
      
                
                System.out.println("***************Here3************");
        }
        
        public static void main(String args[]) {
        	System.out.println("*********** THE MAIN METHOD *****************");
        	
        	SimplePublishClerk sp = new SimplePublishClerk();
        	
        	System.out.println("FINISHED");
        	
        }
        
        
        
  
        
}
