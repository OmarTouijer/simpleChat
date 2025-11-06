package edu.seg2105.edu.server.ui;
import java.util.Scanner;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole {
	private EchoServer server;
	  private Scanner fromConsole;

	  public ServerConsole(int port) {
	    server = new EchoServer(port);
	    
	    fromConsole = new Scanner(System.in);
	  }

	  public void accept() {
	    try {
	      while (true) {
	        String line = fromConsole.nextLine();

	        if (line.startsWith("#")) {
	          handleCommand(line);
	        } else {
	          String msg = "SERVER MSG> " + line;
	          System.out.println(msg);
	          
	          server.sendToAllClients(msg);
	        }
	      }
	    } catch (Exception e) {
	      System.out.println("Unexpected error reading from console");
	    }
	  }

	  private void handleCommand(String cmd) {
		  
	    String[] parts = cmd.trim().split("\\s+");
	    String op = parts[0].toLowerCase();

	    try {
	      switch (op) {
	        case "#quit":
	          try { server.close(); } catch (Exception ignore) {}
	          System.out.println("Server shutting down.");
	          
	          System.exit(0);
	          break;

	        case "#stop":
	          server.stopListening();
	          System.out.println("Server stopped listening for new clients.");
	          break;

	        case "#close":
	          server.close();
	          System.out.println("Server closed and all clients disconnected.");
	          break;

	        case "#setport":
	          if (server.isListening()) {
	            System.out.println("Close the server before changing the port.");
	          } else if (parts.length < 2) {
	            System.out.println("Usage: #setport <port>");
	          } else {
	            int p = Integer.parseInt(parts[1]);
	            server.setPort(p);
	            
	            System.out.println("Port set to " + p);
	          }
	          break;

	        case "#start":
	          if (server.isListening()) {
	            System.out.println("Server already listening.");
	          } else {
	            server.listen();
	            //System.out.println("Server started listening for connections on port " + server.getPort());
	          }
	          break;

	        case "#getport":
	          System.out.println("Server port: " + server.getPort());
	          break;

	        default:
	          System.out.println("Unknown command: " + cmd);
	      }
	    } catch (Exception e) {
	      System.out.println("Command error: " + e.getMessage());
	    }
	  }

	public static void main(String[] args) {
		int port = 5555;
		
	    try {
	      if (args.length > 0)
	        port = Integer.parseInt(args[0]);
	    } catch (Exception ignore) {}

	    ServerConsole sc = new ServerConsole(port);
	    try {
	      sc.server.listen();
	      //System.out.println("Server listening for connections on port " + port);
	    } catch (Exception e) {
	      System.out.println("ERROR - Could not listen for clients!");
	      return;
	    }
	    sc.accept();

	}

}
