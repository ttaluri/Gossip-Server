/* 
1. Name: Tejasree Taluri
2. Date: 05/21/2023
3. Java version : 20
4. Precise command-line compilation examples / instructions: 
> javac GossipStarter.java
 
5. Precise examples / instructions to run this program:
In separate shell windows:
> java GossipStarter

6. Full list of files needed for running the program: 
 a. GossipStarter.java

7. Notes: 
Thank You professor for providing the starter code, which helped me to begin with and with the help of my previous assignments I could complete my Gossip server assigment today.

*/




import java.io.*;
import java.net.*;
import java.util.*;

public class GossipData  implements Serializable
{
	// This class is written to create the data objects which will be required by other classes
	private static final long serialVersionUID = 1L;
	int nodeNumber;  //This variable is used to store the node numbers of the nodes that are generated
    int average;     //This variable is used to store average value of the values among all the nodes
	int highValue;  // This variable is used to store highest value of the values among all the nodes
	int lowValue;   //This variable is used to store lowest value of the values among all the nodes
	String userString;  // This variable is used to store the input entered in the console/terminal
	public int nodeValue;  //This variable is used to store values of all the nodes
}





//GossipWorker class is used to spawn the threads when user enters a string
//it can spawn upto 10 nodes and when spawned these will be assigned with random values and nodeIDs

class GossipWorker extends Thread 
{  
	  GossipData gspObject; // creating the object of the Gossip data class to access it's attributes.
	  List<GossipWorker> otherWorkers;   // creating a list of type gossip worker
	  List<Integer> neighborNodeValues = new ArrayList<>(); //Creating a list of type integer to store neighbor node values here
	  
	  GossipWorker(GossipData c, List<GossipWorker> otherWorkers)  
	  {
	    gspObject = c;  // storing the gossip data class's data into the gossip obj 
	    this.otherWorkers = otherWorkers;  //this keyword is used to store the other worker thread
	  }

	  //here Run() method is written to display gossip messages and the node numbers respectively
	  public void run() {
	    System.out.println("\nGW: In Gossip worker: " + gspObject.userString + "\n"); // displaying the string entered by the user, also called as gossip message

	    // Display node values
	    System.out.println("Node Number: " + gspObject.nodeNumber); //displaying the node numbers to which is gossip message has been sent
	    
	    while(true) // for all cases while the condition is true
	    {
	    	try
	    	{
	    		DatagramSocket DGSocket = new DatagramSocket(); // generating an object for datagram socket connection
	    		byte[] receiveData = new byte[1024];  // setting the size of the data that can be received.
	    		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); //calling receive packet to get the input through the connection
	    		DGSocket.receive(receivePacket);  // the received packet will be stored on to the Datagram socket's object
	    		byte[] receivedBytes = receivePacket.getData();
	    		ByteArrayInputStream byteStream = new ByteArrayInputStream(receivedBytes);   // input stream gets the input in the form of bytes
	    		ObjectInputStream is = new ObjectInputStream(byteStream); // output stream sends the data as bytes
	    		GossipData receivedData = (GossipData) is.readObject(); 
	    		int neighborNodeNumber = receivedData.nodeNumber; // this variable is used to store the node number that has been received through the UDP connection
	    		int neighborNodeValue = receivedData.nodeValue;  //this variable is used to store the node value that has been received through the UDP connection
	    		neighborNodeValues.add(neighborNodeValue); // this method retrieves the values of the neighbor nodes
	    	}catch(IOException ioe) // this block catches and handles the Input and output exceptions
	    	{
	    		ioe.printStackTrace();
	    	}catch(ClassNotFoundException cnf) // this block catches and handles the exceptions thrown when classes are not found
	    	{
	    		cnf.printStackTrace();
	    	}
	    }
	  }
}

	    // Pass message to other nodes
	/*    
	 * for (GossipWorker  actions based on the message
	  }
	}
	*/









// Gossip Starter is the class which handles the the thread spawning and datagram socket connection and all the data related to this client server code
public class GossipStarter 
{
	  public static int serverPort = 45565;   // variable created to assign port number of the server
	  public static int NodeNumber = 0;  // creating and initializing the node number to zero in the beginning

	  public static void main(String[] args) throws Exception 
	  {
		  // displaying the message to show that the server is startingg
	    System.out.println("Tejasree's Gossip Server 1.0 starting up, listening at port " + GossipStarter.serverPort + ".\n");
	    ConsoleLooper CL = new ConsoleLooper(); // console looper class's object is created here
	    Thread t = new Thread(CL); // thread is spawned based on the input given in the console looper class
	    t.start();  // start fuction will start the process of the thread

	    boolean loop_control = true;      // assigning the loop control variable as true
	    // as long the value is true the threads are ready to listen and
	    //when the boolean value changes to false for loop control it will stop listening

	    //adding the code in try block helps us in handling the internal exceptions
	    try {
	      DatagramSocket DtGrmSocket = new DatagramSocket(GossipStarter.serverPort);  // creating an object for datagram socket connection
	      System.out.println("SERVER: Receive Buffer size: " + DtGrmSocket.getReceiveBufferSize() + "\n");  // displaying the size of the buffer on to the terminal
	      byte[] incomingData = new byte[1024];   // setting the size for the incoming data
	      InetAddress IPAddress = InetAddress.getByName("localhost");   //if the connection is within the system then the server name will be assigned as local host

	      //version 5 changes add ping
	      List<GossipWorker> workers = new ArrayList<>();  //this list is created to access it's neighbor nodes
	      
	      while (loop_control) // the below block of code executes in all cases, while true
	      {
	        DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);  // object for socket connection establishment to send or receive data
	        DtGrmSocket.receive(incomingPacket);  // data will be recieved from the datagram socket using the dg socket
	        byte[] data = incomingPacket.getData(); // the recieved data through connection will be stored in this varible
	        ByteArrayInputStream bin = new ByteArrayInputStream(data);  // creating an object for the byte array input stream 
	        ObjectInputStream ois = new ObjectInputStream(bin);   // creating an object for the object input stream 
	        try {
	          GossipData gossipObj = (GossipData) ois.readObject();    // an object of the class gossip data is created to access it's variables
	          if (gossipObj.userString.indexOf("stopserver") > -1)  // if the entered input string is stopserver then the connection will be closed udp will stop listening
	          { 
	            System.out.println("SERVER: Stopping UDP listener now.\n");   //displaying the message that the udp protocol listener is stopping
	            loop_control = false;  // and once the udp stops listening the loop control will be assigned with boolean false
	            DtGrmSocket.close();   // and the socket connection will be closed 
	          }
	          //if the string netered is not quit/stopserver then the below message will be displayed with server node number and the string that has been received
	          System.out.println("\nSERVER: Node " +gossipObj.nodeNumber + " Gossip object received = " + gossipObj.userString + "\n");
	          // V4 -> new GossipWorker(gossipObj).start();
	          // Create a new GossipWorker thread
	          GossipWorker worker = new GossipWorker(gossipObj, workers); // once the string is received the new threads will be spawned 
	          worker.start(); // started the thread
	          workers.add(worker); // and this worker will be stored on to the gossip object's worker list
	          // which holds all the processes that are spawned here
	          
	        } catch (ClassNotFoundException e) { // this block catches and handles the exceptions thrown when classes are not found
	          e.printStackTrace();
	        }catch (ConcurrentModificationException cme) { // this block catches and handles the exceptions thrown when there is concurrent modification exception
	        	cme.printStackTrace();
	        }
	      }
	    } catch (SocketException e) {
	      e.printStackTrace();
	    } catch (IOException i) {  // this block catches and handles the Input and output exceptions
	      i.printStackTrace();
	    }catch (ConcurrentModificationException cme) {  // this block catches and handles the exceptions thrown when there is concurrent modification exception
        	cme.printStackTrace();
	    }
	  }
}








class ConsoleLooper implements Runnable 
{
	String stringInput1, stringInput2; //variables to take input and commands from the user
	GossipWorker gw; 
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));  // creating a buffer
    List<GossipWorker> workers = new ArrayList<>();  // an array list is created to keep track of the workers spawned 
    
    public void run() 
    {
    	int count = 0; // variable is created to keep track of count
        System.out.println("CL: In the Console Looper Thread"); // when the server enters into this method this message will be displayed
        try {
        do    // enters do block for all cases
        {
        System.out.print("CL: Enter a string here to send it to the gossip server (or, quit/stopserver): "); // prompts user to enter a string to spawn nodes or to enter quit or stop server to exit network
        //System.out.flush();
        stringInput1 = in.readLine();  // reading the input from the user
        
        if (stringInput1.indexOf("quit") > -1) // if the entered input string is quit or stop server then enters this block
        { 
          System.out.println("CL: Exiting now by user request.\n"); // this message is displayed and server connection will be exited
          System.exit(0); // exits from the system 
        } 
		else  // if the entered input is not quit or stop server then enters else block and executes...
		{
          try {
        	count ++; // to keep track of the gossips passed among nodes 
            System.out.println("CL: Preparing the datagram packet now..."); // displaying a message when datagram packet is being sent
            DatagramSocket DtGrmSocket = new DatagramSocket(); // creating an object for socket connection establishment
            InetAddress IPAddress = InetAddress.getByName("localhost"); // assigning the server name as local host

            // Generate random node values
            Random random = new Random(); // calling and creating the random method 
            int nodeValue = 0; //random.nextInt(99) + 1;
            int[] nodeValueArray = new int[10];  // creating an Array list to store the node values
            int[] nodeIdArray = new int[10];   // creating an Array list to store the node IDs 
            
            for (int nodeId = 1; nodeId <= nodeValueArray.length; nodeId++)  // for the iteration we are creating the node IDs and node values
			{
              GossipData gspObject = new GossipData(); // creating an object to access it's data
              nodeValue = random.nextInt(99) + 1;  // randomly generating the node value using the random method.
              gspObject.nodeNumber = nodeId;  // storing node IDs to gossip object data
              gspObject.userString = stringInput1;  // storing user input to gossip object data
              gspObject.average = 0; // assigning zero to the average variable currently
              gspObject.highValue = 0; // assigning zero to the high variable currently
              gspObject.lowValue = 0; // assigning zero to the low variable currently

              ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  // creating an object to the input stream 
              ObjectOutputStream os = new ObjectOutputStream(outputStream);  // creating an object to the output stream 
              os.writeObject(gspObject);   // wring the data onto the gossip object
              byte[] data = outputStream.toByteArray(); // creating the memory size to display the output on console
              DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, GossipStarter.serverPort);   // sending the data packet through datagram socket connection
              DtGrmSocket.send(sendPacket);  // storing the data packet sent through connection, in the socket
              System.out.println("CL: Datagram sent to the Node ID: " + nodeId + " with the Node value: " + nodeValue); // displaying the node IDs and node values that are randomly generated 
              //ArrayForNodeValue[nodeId] = nodeValue;
              nodeValueArray[nodeId - 1] = nodeValue; // storing the node values generated newly onto the node value array for further refence
              nodeIdArray[nodeId - 1] = nodeId; // storing the node Id onto the gossip object data
              gspObject.nodeNumber = nodeId; // storing the node Id onto the gossip object data
			} 
        	System.out.print("\n\nCL: Enter a command \nt - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, \nv - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): \n");
        	// the list of commands available will be displayed here for user to select and enter the command to perform respective operations
	        do {	 
	        	GossipData gspObject = new GossipData(); // object is created to access it's data
	        	System.out.print("\n\nCL: Enter a command \nt - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, \nv - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): \n");
	        
	                System.out.flush(); // clears the console and stores the data if present, onto some buffer storge
	                stringInput2 = in.readLine();  // the user will enter the command to perform operation of their own from the list of commands given
	                count++; // count variable is used to hold the track of the gossip count
	                switch (stringInput2) // the user input will enter here and checks with all the cases below
					{
					  case "t":  // this case executes when user enters t, displays all the commands available that can perform each operations respectively.  
						  	System.out.println("\nt - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, \nv - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit : \n");
						  	break;
					  case "l":  // this case executes when user enters l, which will display the list of nodes with their respective node IDs and node values 
		                    System.out.println("The local node values are : ");  // display message
		                    System.out.println("\nNode Values:"); // display message
	                        for (int i = 0; i < nodeValueArray.length; i++) // going through the iteration using for loop we will access and display all the nods and their values
	                        {
	                            System.out.println("Node " + (i + 1) + ": " + nodeValueArray[i]); // displays the node ID and their node values
	                        }
		                    break;
					  case "p":    // this case executes when user enters p, which will communicate with it's neighbors an gather's their node values and displays those values here
		                    System.out.println("The above & below neighbor node's values");  // displaying the message here
		                    for (GossipWorker worker : workers)  //calling the workers from the gossip worker
		                    {
		                        List<Integer> neighborNodeValues = worker.neighborNodeValues;  // creating a list of type integer to store the neighbor node values that will be returned from other class
		                        int nodeNum = random.nextInt(worker.gspObject.nodeNumber);  // taking one random node number to get their neighbor node values
		                        System.out.println("Neighbor Node Values for Node " + nodeNum + ":");  //displaying the message to show values of the node's neighbors
		                        for (int i = 0; i < neighborNodeValues.size(); i++)  // iterating through loop to check for the neighbor nodes to get their values
		                        {
		                            System.out.println("Neighbor " + (i + 1) + ": " + neighborNodeValues.get(i));   //displaying the neighbor node values of the chosen node 
		                        }
		                    }       
		                    break;
					  case "m":      // this case executes when user enters m, which will display the minimum and maximum values among all node values 
		                    System.out.println("Min and max values compared among all nodes.");
		                    Arrays.sort(nodeValueArray);  // sorting the array of node values to get the min and max values
		                    // The last element of the sorted array will be the maximum value
		                    int maximValue = nodeValueArray[nodeValueArray.length - 1];   // retrieving the last value in the array after sorting to get the max value
		                    gspObject.highValue = maximValue;  // storing the maximum value on to the data object of gossip server
		                    int minimValue = nodeValueArray[0];  // retrieving the first value in the array after sorting to get the max value
		                    gspObject.lowValue = minimValue;  // storing the minimum value on to the data object of gossip server
		                    System.out.println("Maximum Node value: " + maximValue); // displaying the minimum value among all the node values present
		                    System.out.println("Minimum Node value: " + minimValue); // displaying the maximum value among all the node values present
		                    break;
					  case "a":   // this case executes when user enters a, which will display the average of all node values combined altogether
		                    System.out.println("The average value among nodes.");  // displaying a statement
		                    int sum = 0;  // creating a variable to store sum of values
		                    // Calculate the sum of numbers
		                    for (int number : nodeValueArray) {  // storing node values to number variable, temp variable to calculate
		                        sum += number;       // sum of all the numbers stored on to number variable 
		                    }
		                    // Calculate the average
		                    double average = (double) sum / nodeValueArray.length;  // performing the calculations to get the average
		                    System.out.println("Average Node value: " + average);  // displaying here the average value of all node values together
		                    break;
					  case "z":  // this case executes when user enters z, which will display the size of the network, that is the no of nodes present in this network
		                    System.out.println("The current network size is : " +nodeValueArray.length); // displaying network size here
		                    break;
					  case "v": // this case executes when user enters v, which will create new node values and then display the old node values and new node values along with the node IDs
		                    System.out.println("Creating new values to nodes and displaying old and new values."); // message is being displayed
		                    for (int nodeId = 1; nodeId <= nodeValueArray.length; nodeId++) // the for loop goes through the array list to access the node values
		        			{ 
		                      System.out.println("CL: Old Node Value for Node ID: " + nodeId + " old value: " + nodeValue);  // displaying the old node values along with it's node IDs
		                      nodeValue = random.nextInt(99) + 1;  // new node values will be randomly assigned using the random method
		                      gspObject.nodeNumber = nodeId;      // storing the node ID onto the object
		                      System.out.println("CL: New Node Value for Node ID: " + nodeId + " new value: " + nodeValue);  // displaying the new node values along with it's node IDs
		                      nodeValueArray[nodeId - 1] = nodeValue;  //storing these new node values to the node value array
		                      gspObject.nodeValue = nodeValue; // node values storing to gossip object
		                      gspObject.nodeNumber = nodeId;  // node id storing to gossip object
		        			}
		                    break;
					  case "d":   // this case executes when user enters d, which will delete a node which user requests to delete
		                    System.out.println("Deleting a node..."); 
		                    
		                    try {
		                        System.out.print("CL: Enter the node number to delete: "); // prompting the user to enter the node number he/she wishes to delete
		                        //System.out.flush();
		                        String nodeNumberStr = in.readLine(); // reading the user input using the readline method
		                        int nodeNumber = Integer.parseInt(nodeNumberStr); // converting this string type input taken into integer type and assigning it to a variable
		                        //List<GossipWorker> workers = new ArrayList<>();
		                        // Find and remove the worker associated with the node number
		                        GossipWorker workerToRemove = null;  // creating a variable of type gossip worker class and assigning it's value as null
		                        for (GossipWorker worker : workers)  // calling the worker processors stored in the gossip worker object
		                        {
		                          if (worker.gspObject.nodeNumber == nodeNumber)   //comparing the node number entered by the user with the node numbers present in the gossip worker objects list 
		                          {
		                            workerToRemove = worker; // if the node number is present then it is assigned to worker to remove variable
		                            break;  
		                          }
		                        }

		                        if (workerToRemove != null) // if the variable worker to remove is not null the enter the block
		                        {
		                          workerToRemove.interrupt(); // interrupts the node, stops its process
		                          workers.remove(workerToRemove); //once it is interrupted and process is stopped then the node is removed
		                          System.out.println("CL: Node " + nodeNumber + " has been deleted.");  // once it is deleted the message will be displayed
		                        } else {
		                          System.out.println("CL: Node " + nodeNumber + " not found."); // if not found then this message will be displayed
		                        }
		                      } catch (NumberFormatException e) { // if the given input is in not right format this exception handles it.
		                        System.out.println("CL: Invalid node number. Please enter a valid integer.");  // and this message will be displayed to enter valid format of node number
		                      } catch (IOException e) { // this block catches and handles the Input and output exceptions
		                        e.printStackTrace();
		                      }
		                    break;
					  case "k": // this case executes when user enters k, which will kill all the nodes also called as threads which kills the entire network
		                    System.out.println("Will delete entire network here.");
		                    System.exit(0);
		                    break;
					  case "y":   // this case executes when user enters y, which will display the num of cycles
		                    System.out.println("Num of cycles : " +count);
		                    break;
	                  case "n":   // this case executes when user enters n, which will return the max and min values in the array list of all node values
	                	  	System.out.println("Num of gossips passed : " +count);
	                	  	break;
	                  case "q":   // this case executes when user enters q, which will quit the connection
	                	  	System.out.println("Exiting now by user request.\n"); //displaying the message when exiting
	                	  	System.exit(0);
	                  default:
	                	  	run(); // by default it executes the main command list to display the options
	                	  	//System.out.println("Invalid command.");
	                	  	break;
	                }
	                DtGrmSocket.close();   // closing the datagram socket connection to avoid the data leak
	        }while (true); // the loop executes until it is true, which is in all cases it will be true
          } catch (UnknownHostException UH) {  // this block catches and handles the unknown host exceptions
            System.out.println("\nCL: Unknown Host problem.\n");
            UH.printStackTrace();
          } catch (IOException io) {  // this block catches and handles the Input and output exceptions
    	      io.printStackTrace();
  	    }catch (ConcurrentModificationException cme) { // this block catches and handles the exceptions thrown when there is concurrent modification exception
        	cme.printStackTrace(); }
		}
        }while(true);
        }catch (IOException x) {   // this block catches and handles the Input and output exceptions
	        x.printStackTrace();
        
    }catch (ConcurrentModificationException cme) { // this block catches and handles the exceptions thrown when there is concurrent modification exception
    	cme.printStackTrace();}
    }
    
}




/*  ============= LOG FILE ============================================================================



Tejasree's Gossip Server 1.0 starting up, listening at port 45565.

CL: In the Console Looper Thread
CL: Enter a string here to send it to the gossip server (or, quit/stopserver): SERVER: Receive Buffer size: 65536

Hey, There.. Namastee...
CL: Preparing the datagram packet now...
CL: Datagram sent to the Node ID: 1 with the Node value: 35
CL: Datagram sent to the Node ID: 2 with the Node value: 20
CL: Datagram sent to the Node ID: 3 with the Node value: 95
CL: Datagram sent to the Node ID: 4 with the Node value: 13
CL: Datagram sent to the Node ID: 5 with the Node value: 40
CL: Datagram sent to the Node ID: 6 with the Node value: 22
CL: Datagram sent to the Node ID: 7 with the Node value: 45
CL: Datagram sent to the Node ID: 8 with the Node value: 4
CL: Datagram sent to the Node ID: 9 with the Node value: 79
CL: Datagram sent to the Node ID: 10 with the Node value: 88


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 

SERVER: Node 1 Gossip object received = Hey, There.. Namastee...


SERVER: Node 2 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 1

SERVER: Node 3 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 2

SERVER: Node 4 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 3

GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 4

SERVER: Node 5 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 5

SERVER: Node 6 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 6

SERVER: Node 7 Gossip object received = Hey, There.. Namastee...


SERVER: Node 8 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 7

SERVER: Node 9 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 8

SERVER: Node 10 Gossip object received = Hey, There.. Namastee...


GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 9

GW: In Gossip worker: Hey, There.. Namastee...

Node Number: 10
aaaaaaaaaaa
CL: In the Console Looper Thread
CL: Enter a string here to send it to the gossip server (or, quit/stopserver): bbbbbbbbbb
CL: Preparing the datagram packet now...
CL: Datagram sent to the Node ID: 1 with the Node value: 61
CL: Datagram sent to the Node ID: 2 with the Node value: 10

SERVER: Node 1 Gossip object received = bbbbbbbbbb

CL: Datagram sent to the Node ID: 3 with the Node value: 12
CL: Datagram sent to the Node ID: 4 with the Node value: 31
CL: Datagram sent to the Node ID: 5 with the Node value: 81

SERVER: Node 2 Gossip object received = bbbbbbbbbb


GW: In Gossip worker: bbbbbbbbbb

Node Number: 1
CL: Datagram sent to the Node ID: 6 with the Node value: 44
CL: Datagram sent to the Node ID: 7 with the Node value: 55

SERVER: Node 3 Gossip object received = bbbbbbbbbb

CL: Datagram sent to the Node ID: 8 with the Node value: 49

GW: In Gossip worker: bbbbbbbbbb

Node Number: 2
CL: Datagram sent to the Node ID: 9 with the Node value: 59

GW: In Gossip worker: bbbbbbbbbb

Node Number: 3

SERVER: Node 4 Gossip object received = bbbbbbbbbb

CL: Datagram sent to the Node ID: 10 with the Node value: 27


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 

GW: In Gossip worker: bbbbbbbbbb


SERVER: Node 5 Gossip object received = bbbbbbbbbb

Node Number: 4

GW: In Gossip worker: bbbbbbbbbb

Node Number: 5

SERVER: Node 6 Gossip object received = bbbbbbbbbb


GW: In Gossip worker: bbbbbbbbbb

Node Number: 6

SERVER: Node 7 Gossip object received = bbbbbbbbbb


GW: In Gossip worker: bbbbbbbbbb

Node Number: 7

SERVER: Node 8 Gossip object received = bbbbbbbbbb


GW: In Gossip worker: bbbbbbbbbb

Node Number: 8

SERVER: Node 9 Gossip object received = bbbbbbbbbb


GW: In Gossip worker: bbbbbbbbbb

Node Number: 9

SERVER: Node 10 Gossip object received = bbbbbbbbbb


GW: In Gossip worker: bbbbbbbbbb

Node Number: 10
cccccccccc
CL: In the Console Looper Thread
CL: Enter a string here to send it to the gossip server (or, quit/stopserver): dddddddd
CL: Preparing the datagram packet now...
CL: Datagram sent to the Node ID: 1 with the Node value: 74
CL: Datagram sent to the Node ID: 2 with the Node value: 74

SERVER: Node 1 Gossip object received = dddddddd

CL: Datagram sent to the Node ID: 3 with the Node value: 38
CL: Datagram sent to the Node ID: 4 with the Node value: 50
CL: Datagram sent to the Node ID: 5 with the Node value: 20

GW: In Gossip worker: dddddddd


SERVER: Node 2 Gossip object received = dddddddd

CL: Datagram sent to the Node ID: 6 with the Node value: 48
Node Number: 1
CL: Datagram sent to the Node ID: 7 with the Node value: 92

SERVER: Node 3 Gossip object received = dddddddd

CL: Datagram sent to the Node ID: 8 with the Node value: 82
CL: Datagram sent to the Node ID: 9 with the Node value: 95

GW: In Gossip worker: dddddddd

CL: Datagram sent to the Node ID: 10 with the Node value: 24


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 

SERVER: Node 4 Gossip object received = dddddddd

Node Number: 2

GW: In Gossip worker: dddddddd


SERVER: Node 5 Gossip object received = dddddddd

Node Number: 3

GW: In Gossip worker: dddddddd

Node Number: 4

SERVER: Node 6 Gossip object received = dddddddd


GW: In Gossip worker: dddddddd

Node Number: 5

GW: In Gossip worker: dddddddd


SERVER: Node 7 Gossip object received = dddddddd

Node Number: 6

GW: In Gossip worker: dddddddd

Node Number: 7

SERVER: Node 8 Gossip object received = dddddddd


GW: In Gossip worker: dddddddd

Node Number: 8

SERVER: Node 9 Gossip object received = dddddddd


GW: In Gossip worker: dddddddd

Node Number: 9

SERVER: Node 10 Gossip object received = dddddddd


GW: In Gossip worker: dddddddd

Node Number: 10
t

t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit : 



CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
l
The local node values are : 

Node Values:
Node 1: 74
Node 2: 74
Node 3: 38
Node 4: 50
Node 5: 20
Node 6: 48
Node 7: 92
Node 8: 82
Node 9: 95
Node 10: 24


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
p
The above & below neighbor node's values


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
m
Min and max values compared among all nodes.
Maximum Node value: 95
Minimum Node value: 20


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
a
The average value among nodes.
Average Node value: 59.7


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
z
The current network size is : 10


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
v
Creating new values to nodes and displaying old and new values.
CL: Old Node Value for Node ID: 1 old value: 24
CL: New Node Value for Node ID: 1 new value: 77
CL: Old Node Value for Node ID: 2 old value: 77
CL: New Node Value for Node ID: 2 new value: 1
CL: Old Node Value for Node ID: 3 old value: 1
CL: New Node Value for Node ID: 3 new value: 37
CL: Old Node Value for Node ID: 4 old value: 37
CL: New Node Value for Node ID: 4 new value: 3
CL: Old Node Value for Node ID: 5 old value: 3
CL: New Node Value for Node ID: 5 new value: 8
CL: Old Node Value for Node ID: 6 old value: 8
CL: New Node Value for Node ID: 6 new value: 9
CL: Old Node Value for Node ID: 7 old value: 9
CL: New Node Value for Node ID: 7 new value: 90
CL: Old Node Value for Node ID: 8 old value: 90
CL: New Node Value for Node ID: 8 new value: 23
CL: Old Node Value for Node ID: 9 old value: 23
CL: New Node Value for Node ID: 9 new value: 87
CL: Old Node Value for Node ID: 10 old value: 87
CL: New Node Value for Node ID: 10 new value: 41


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
d
Deleting a node...
CL: Enter the node number to delete: 25
CL: Node 25 not found.


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
y
Num of cycles : 10


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
n
Num of gossips passed : 11


CL: Enter a command 
t - Command List, l - local value of nodes, p - Ping, m - Min & Max value, a - Average, z - Network size, 
v - Create new node values, d - Delete node,  k - Kill Netwoek, y - Display no of cycles, n - No of Gossip msgs, q - quit): 
k
Deleting the entire network..






======= MY POSTS ON THE DISCUSSION FORM ===========

POSTING 1:
Hi Shivangi,
Thanks for sharing this link. 
Appreciate how everyone is eager to share what they learned online, with their fellow classmates. 
Most of my doubts were clarified just by reading the posts on discussion forms and going through such links. 
So, Thanks again :)
----------------------------
POSTING 2:
Hi Shashank,
I don't think we will alter the maximum value for every node. 
Instead, it would be better to store and update all the node values in a list 
and then compare and return the max value, when requested using the N command.
I believe it can be one way to do it.
--------------------------------
POSTING 3:
Thank you for sharing this link with us. 
It is really useful and I like how brief it is and also it helped clear some of my doubts.
Thanks once again.
-------------------------------------
*/





