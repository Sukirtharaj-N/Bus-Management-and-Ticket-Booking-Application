package busApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Time;


public class Mainpage {
	public static Scanner in = new Scanner(System.in);
	Accounts account = new Accounts();
	Bus bus=new Bus();
	Merchants merchant=new Merchants();
	Customers customer=new Customers();
	
	public static String username,password;
	public static int uid;
	
	public boolean signDetails(int identity,int sign) throws SQLException {
		boolean flag=false;
		System.out.println("Enter username: ");
		username=in.nextLine();
		ResultSet result=account.getUsername(username);
		if(result.next()) {
			uid=result.getInt("uid");
			flag= true;
		}
//		System.out.println("flag= "+flag);
		if(sign==1) {
			System.out.println("Enter password: ");
			password=in.nextLine();
			flag = account.verify(identity,username,password);
			if(flag) 
				System.out.println("Login Successfull.");
			else
				System.out.println("Invalid credentials.");			
		}
		else {
			if(flag) {
				System.out.println("Username already exits!");
				flag=false;
			}
			else {
				System.out.println("Enter password: ");
				password=in.nextLine();
			  flag=moreDetails(identity);
			}
		}
		return flag;
	}

	public boolean moreDetails(int identity) throws SQLException {
		
		System.out.println("Enter age: ");
		int age=in.nextInt();
		in.nextLine();
		System.out.println("Enter gender: ");
		String gender=in.nextLine();
		System.out.println("Enter email address: ");
		String email=in.nextLine();
		System.out.println("Enter phone number: ");
		String phone=in.nextLine();
				
		int flag=account.signUp(identity,username, password,age,gender,email,phone);
		if(flag>0) {
			System.out.println("Account created successfully.");
//			return true;
		}
		return false;
	}	
		
	public int busDetails() throws SQLException {
		int flag=0;
		System.out.println("Enter bus name: ");
		String busName=in.nextLine();
		System.out.println("Enter type (Sleeper or semi-sleeper): ");
		String busType=in.nextLine();
		System.out.println("AC or non-AC: ");
		String ac=in.nextLine();
		System.out.println("Enter number of available seats: ");
		int seats=in.nextInt();
		System.out.println("Enter bus fare: ");
		float fare=in.nextFloat();
		flag=merchant.addBus(busName, busType, ac, seats, fare,uid);
		System.out.println("Bus added successfully");
		return flag;
	}
	
	public void addStops(int busId) throws SQLException {
		System.out.println("Enter number of stops: ");
		int n=in.nextInt();
		in.nextLine();
		String stops[]=new String[n];
		String time[]=new String[n];
		for(int i=0;i<n;i++) {
			System.out.println("Enter stop "+ (i+1) +" name: ");
			stops[i]=in.nextLine();
			System.out.println("Enter arrival time at stop "+(i+1)+": ");
			time[i]=in.nextLine();
		}
		int flag=bus.stopsDetails(busId,n,stops,time);
		if(flag==n)
			System.out.println("All stops added successfully");
		else
		{
			System.out.println("Failed to insert stops");
			//delete all rows with bid=id to remove inconsititent stop entries...
		}
	}
	
	public void updateBusDetails() throws SQLException {
		
		System.out.println("Enter bus name: ");
		String busName=in.nextLine();
		//check whether the username has access to the busname!!
		if(!merchant.haveAccess(uid,busName)) {
			System.out.println("You donot have access rights to this bus");
			return;
		}
		System.out.println("Enter no. of fields to update: ");
		int n=in.nextInt();
		in.nextLine();
		String update[]=new String[n];
		Object set[]=new Object[n];
		for(int i=0;i<n;i++) {
			System.out.println("Enter field name: ");
			update[i]=in.nextLine();
			System.out.println("Enter update value: ");
			if(update[i].equals("seats"))
				set[i]=in.nextInt();
			else if(update[i].equals("fare"))
				set[i]=in.nextFloat();
			else
				set[i]=in.nextLine();
		}
		int flag=bus.updateBus(busName,update,set);
		if(flag>0)
			System.out.println("Bus details updated successfully");
		else
			System.out.println("Couldn't update bus details");
	}
	
	public void deleteBus() throws SQLException {
		
		System.out.println("Enter bus name: ");
		String busName=in.nextLine();
		//check whether the username has access to the busname!!
		if(!merchant.haveAccess(uid,busName)) {
			System.out.println("You donot have access rights to this bus");
			return;
		}
		int flag=bus.deleteBuses(busName);
		if(flag>0)
			System.out.println("Bus deleted successfully");
		else
			System.out.println("Couldn't delete bus details");
	}
	
	public void displayBusDetails(int identity) throws SQLException {
		ResultSet result=null;
		if(identity==1)
		{
			System.out.println("Enter option - 1. Your bus list  2.All bus list");
			int choice=in.nextInt();
			in.nextLine();
			if(choice==1) {
				result=merchant.getMyBus(uid);
				displayBusInfo(result);
			}
			else
				showBuses();
		}
		else if(identity==2)
			showBuses();
	}
	
	public void displayBusInfo(ResultSet result) {
		//bid,bname,btype,ac,seats,fare
		try {
			while(result.next()){
			  int busId=result.getInt("bid");
			  String name=result.getString("bname");
			  String type=result.getString("btype");
			  int seats=result.getInt("seats");
			  float fare=result.getFloat("fare");
			  System.out.println("Bus name: "+name+"  Bus type: "+type+"  Seats available: "+seats+"  fare: "+fare);
			  displayStops(busId);
			  System.out.println();System.out.println();
           }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void displayStops(int busId) throws SQLException {
		  ResultSet result=bus.getStopDetails(busId);
		  System.out.println("Stop\tStop name\tTiming");
		  
		  try {
			while(result.next()) {
				  int stopNo=result.getInt("stopno");
				  String stopName=result.getString("stopname");
				  Time time=result.getTime("timing");		
				  if(stopName.length()<10)
					  System.out.println(stopNo+"\t"+stopName+"       \t"+time);
				  else
					  System.out.println(stopNo+"\t"+stopName+"\t"+time);
					  
			  }
		  } catch (SQLException e) {
			  e.printStackTrace();
		  	}
	}

	public void showBuses() throws SQLException {
        // get conditions and pass it to customer class
		ResultSet result=null;
		System.out.println("Do you have any filter conditions [yes|no]: ");
		String choice=in.nextLine();
		if(choice.equals("yes")) {
			System.out.println("Enter number of conditions:");
			int n=in.nextInt();
			in.nextLine();
			String conditions[]=new String[n];
			Object value[]=new Object[n];
			for(int i=0;i<n;i++) {
				System.out.println("Enter condition "+(i+1)+": ");
				conditions[i]=in.nextLine();
				System.out.println("Enter value: ");
				value[i]=in.nextLine();
			}
			result=customer.getBuses(conditions,value);
		}
		else {
			System.out.println("Dispalying all buses in database");
			result=customer.allBuses();
		}
		displayBusInfo(result);
	}
	
	public void getTicketDetails() throws SQLException {
		System.out.println("Enter bname: ");
		String busName=in.nextLine();
		System.out.println("Enter source: ");
		String source=in.nextLine();
		System.out.println("Enter destination: ");
		String destination=in.nextLine();
		boolean verify=bus.verifyStops(source,destination,busName);
		if(verify) {
		System.out.println("Enter number of tickets: ");
		int tickets=in.nextInt();
		in.nextLine();
		int flag= customer.bookTickets(uid,busName,source,destination,tickets);
		if(flag>0)
			System.out.println("Ticket booked successfully");
		else if(flag==-1)
			System.out.println("Cannot book more than available seats..");
		else
			System.out.println("Couldn't book tickets..try again later!");
		}
		else
			System.out.println("The bus doesn't operate for the given source and destination"); 
	}
	
	public void showTickets() throws SQLException {
		ResultSet result=customer.displayTickets(uid);
		
			
		try {
			if(!result.next()) {
				System.out.println("Your Booking list is empty");
				return;
			}
			do {
				System.out.print("Bus name: "+result.getString("bname"));
				System.out.print("\t\tTickets booked: "+result.getInt("tickets"));
				System.out.println("\tTotal fare: "+result.getString("fare"));
				System.out.print("Souce: "+result.getString("source"));
				System.out.print("\tDestination: "+result.getString("destination"));
				System.out.println("\tTicket ID:"+result.getInt("ticketid"));
			}while((result.next()));
		}catch(SQLException e) {
			System.out.println(e);
		}
	}
	
	public void cancelTicket() throws SQLException {
		
		System.out.println("Enter ticket tid to cancel: ");
		int ticketId=in.nextInt();
		//check whether the username has access to the busname!!
		if(!customer.haveAccess(uid,ticketId)) {
			System.out.println("The ticket Id is not found in you booking.");
			return;
		}
		int flag=customer.deleteTicket(ticketId);
		if(flag>0)
			System.out.println("Ticket cancellation successfully");
		else
			System.out.println("Couldn't cancel the ticket");
	}

	public static void main(String[] args) throws SQLException {
		Mainpage mp=new Mainpage();
		System.out.println("Select user type - 1.Merchant 	2.Customer");
		int userType=in.nextInt();
		System.out.println("Select option - 1.SignIn 	2.SignUp");
		int sign=in.nextInt();
		in.nextLine();
		boolean verified=mp.signDetails(userType,sign);
		if(verified) {
			boolean loop=true;
			if(userType==1) {
				while(loop) {
					System.out.println("\nSelect an option - 1.Add a Bus  2.Update Bus Details  3. Delete Bus  4.Show Bus Details  5.Exit");
					int choice=in.nextInt();
					in.nextLine();
					switch(choice) {
					case 1: 
						int id=mp.busDetails();
//				    	System.out.println("bid="+id);
						mp.addStops(id);
						break;
					case 2:
						mp.updateBusDetails();
						break;
					case 3:
						mp.deleteBus();
						break;
					case 4:
						mp.displayBusDetails(1);
						break;
					case 5: 
						loop=false;
						break;
					default: System.out.println("Invalid option");
					}
				}
			}	
			
			if(userType==2) {
				while(loop) {
					System.out.println("\nSelect an option - 1.View Buses  2.Book tickets  3.Cancel tickets  4.Show ticket details  5.Exit");
					int choice=in.nextInt();
					in.nextLine();
					switch(choice) {
					case 1:
						mp.displayBusDetails(2);
						break;
					case 2:
						mp.getTicketDetails();
						break;
					case 3:
						mp.cancelTicket();
						break;
					case 4:
						mp.showTickets();
						break;
					case 5: 
						loop=false;
						break;
					//completed till above................TESTED TOO
					default: System.out.println("Invalid option");
					}
				}
			}
			System.out.println("\nThanks for using this app...Byee!!");
		}
	}
}

		

