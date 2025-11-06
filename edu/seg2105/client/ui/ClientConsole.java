package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String loginId) 
  {
    try 
    {
      client= new ChatClient(host, port, this, loginId);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        
        if (message.startsWith("#")){
            handleCommand(message);
        }else{
        	client.handleMessageFromClientUI(message);
        }
      } 
    }
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  
  
  private void handleCommand(String message) {
	  try {
	    if (message.equalsIgnoreCase("#quit")) {
	      display("Closing client.");
	      client.quit();
	    } 
	    else if (message.equalsIgnoreCase("#logoff")) {
	      if (client.isConnected()) {
	        client.closeConnection();
	        display("Disconnected from server.");
	      }else{
	        display("Is already logged off.");
	      }
	    } 
	    else if (message.startsWith("#sethost")) {
	      if (!client.isConnected()) {
	        String[] parts = message.split(" ");
	        if (parts.length > 1) {
	          client.setHost(parts[1]);
	          display("Host set to " + parts[1]);
	        } else {
	          display("Usage: #sethost <host>");
	        }
	      } else{
	        display("Must log off before changing host.");
	      }
	    } 
	    else if (message.startsWith("#setport")) {
	      if (!client.isConnected()) {
	        String[] parts = message.split(" ");
	        if (parts.length > 1) {
	          try {
	            int newPort = Integer.parseInt(parts[1]);
	            client.setPort(newPort);
	            display("Port set to " + newPort);
	          } catch (NumberFormatException e) {
	            display("Invalid port number.");
	          }
	        }else{
	          display("Usage: #setport <port>");
	        }
	      }else{
	        display("Must log off before changing port.");
	      }
	    } 
	    else if (message.equalsIgnoreCase("#login")) {
	      if (!client.isConnected()) {
	        client.openConnection();
	        display("Reconnected to server.");
	      }else {
	        display("Already connected.");
	      }
	    } 
	    else if (message.equalsIgnoreCase("#gethost")) {
	      display("Current host: " + client.getHost());
	    } 
	    else if (message.equalsIgnoreCase("#getport")) {
	      display("Current port: " + client.getPort());
	    }else {
	      display("Unknown command: " + message);
	    }
	  }catch (IOException e) {
	    display("Error processing command: " + e.getMessage());
	  }
	}


  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
	if (args.length < 1) {
		System.out.println("ERROR - No login ID specified. Connection aborted.");
		System.exit(1);
	}

	String loginId = args[0];
	String host = "localhost";
	int port = DEFAULT_PORT;

	try {
		if (args.length > 1)
			host = args[1];
		if (args.length > 2)
			port = Integer.parseInt(args[2]);
    }catch (Exception e){
    	System.out.println("Invalid host/port. Using defaults.");
    }
    ClientConsole chat= new ClientConsole(host, port, loginId);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
