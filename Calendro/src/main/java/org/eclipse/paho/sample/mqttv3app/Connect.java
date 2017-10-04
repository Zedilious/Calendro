//package for mqtt version 3
package org.eclipse.paho.sample.mqttv3app;

//import necessary libraries
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Connect 
{
	//set up global variables
	int qos             = 2; 								//quality of service number
	String broker       = "tcp://broker.hivemq.com:1883"; 	//broker that hosts the mqtt connection and storage of data
	String clientId     = "CalendroMainBoard"; 				//what this connection shall be identified as
	String subTopic		= "ac573∕Feeds∕Calendro"; 			//topic to subscribe to and receieve the data from
    
	MemoryPersistence persistence;
	MqttClient Client;
    
	//empty, nothing needed in here really
	public Connect(){}
        
	/*
	 *try to connect to the mqtt broker, and subscribing to the topic specified above 
	 */
	public boolean tryCon()
    {
		try 
        {
			//create the memory persistance to be sent with broker and client id
			persistence = new MemoryPersistence();
			
			//create a new mqtt client with the broker url, client id, and persistence
			Client = new MqttClient(broker, clientId, persistence);
              
			//connect to the client
            System.out.println("Connecting to broker: " + broker);
            Client.connect();
            System.out.println("Connected: " + Client.isConnected() +"\n");
                
            //subscribe to the topic specified
            System.out.println("Subscribing to " + subTopic);
            Client.subscribe(subTopic);
            System.out.println("Subscription Successful\n");

            //activate the mqttcallback method, effectively a listener that waits for the mqtt to send any new inputs
            Client.setCallback(new MqttCallback() 
            {
            	//message has arrived, receive topic and the new message
                public void messageArrived(String topic, MqttMessage message)throws Exception 
                {
                	//call the newMessage process in the main class, send the received message in string form
                	CalendroM.newMessage(message.toString());
                	
                	//if the message is greater than 100 chars, then it is a base64 string and so the publish image can be called with no errors 
                    /*if (message.toString().length() > 100)
                    {
                    	//call the publishImage process in the main class
                    	CalendroM.publishImage();
                    }
                    */
                }
                
                //eclipse wanted this here to fully satisfy the MqttCallback method, signals that the delivery of the message is complete.
				public void deliveryComplete(IMqttDeliveryToken token) 
				{
					System.out.println("Token: " + token.toString());
				}
				
				//if connection is lost, attempt to reconnect.
				public void connectionLost(Throwable cause) 
				{
					tryCon();
				}
            });
                
            return true;
                
        } 
		
		//catch these specific sources that mqtt documentation recommends
        catch(MqttException me) 
        {
        	System.out.println("Reason: "			+ me.getReasonCode());
            System.out.println("Message: " 			+ me.getMessage());
            System.out.println("Local Message: " 	+ me.getLocalizedMessage());
            System.out.println("Cause:  " 			+ me.getCause());
            System.out.println("Exception: " 		+ me);
            me.printStackTrace();
            return false;
        }
    }          
        
	/*
	 * Shouldn't be called but here just for testing in the beginning, nice to have here i guess
	 */
    public boolean disconnect()
    {
    	System.out.println("Disconnecting...");
        //try disconnection sequence
    	try 
        {
    		//try disconnect
			Client.disconnect();
	        System.out.println("Disconnection Successful");
	        return true;
		} 
        catch (MqttException e)
        {
			e.printStackTrace();
			return false;
		}
    }
        
    /*
     *publishes a message to the mqtt broker, was used in early development and testing, henceforth is not called by the main code
     */
    public boolean publish(String content)
    {
    	System.out.println("Publishing message: \"" + content + "\" to: " + subTopic);
    	
    	//create new mqtt message object with the provided content
        MqttMessage message = new MqttMessage(content.getBytes());
        
        //set the quality of service for the message to be sent with
        message.setQos(qos);
        
        //try publishing the message
        try 
        {
			Client.publish(subTopic, message);
		} 
        catch (MqttPersistenceException e) 
        {
			e.printStackTrace();
		}
        catch (MqttException e) 
        {
			e.printStackTrace();
		}
        
        System.out.println("Message published\n");
        return true;
    }
}