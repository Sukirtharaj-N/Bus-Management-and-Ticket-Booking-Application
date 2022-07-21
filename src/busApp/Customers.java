package busApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Customers {
	Dbconnection db = new Dbconnection();
	Bus bus=new Bus();

	@SuppressWarnings("null")
	public ResultSet getBuses(String[] conditions, Object[] value) throws SQLException { 
		ResultSet result=null;
		Object source=null, destination=null;
		String sql=null,sql1="select bid,bname,btype,ac,seats,fare from bus where ";
		for(int i=0;i<conditions.length;i++) {
			if(conditions[i].equals("source")) 
				source=value[i];
			else if(conditions[i].equals("destination")) 
				destination=value[i];
			else {
				if(i!=conditions.length-1) {
					if(conditions[i].equals("btype")||conditions[i].equals("ac")||conditions[i].equals("bname"))
						sql1+=conditions[i]+"='"+value[i]+"' and ";
					else				
					    sql1+=conditions[i]+ " "+value[i]+" and ";
				}
				else {
					if(conditions[i].equals("btype")||conditions[i].equals("ac")||conditions[i].equals("bname"))
						sql1+=conditions[i]+"='"+value[i]+"'";
					else
						sql1+=conditions[i]+" "+value[i];
				}
			}
		}
		if(source!=null && destination!=null) 
			sql="select s1.bid from stops as s1 inner join stops as s2 on s1.bid=s2.bid and s1.stopname='"+source+"' and s2.stopname='"+destination+"' and s1.stopno<=s2.stopno";
		else if(source!=null)
			sql= "Select bid from stops where stopname='"+source+"'";
		else if(destination!=null)
			sql="Select bid from stops where stopname='"+destination+"'";
//		System.out.println("sql= "+sql);
		if(sql!=null) {
			result=db.select(sql);
			if(result!=null) {
				int id[]=new int[100], n=0;
				sql1 += " and bid in (";
				while(result.next()) {
					 id[n++]=result.getInt("bid"); 
				}
				for(int i=0;i<n;i++) {
					if(i==n-1)
						sql1+= id[i]+")";
					else
						sql1+= id[i]+",";
				}
			}
		}
//		System.out.println("sql1= "+sql1);
		result=db.select(sql1);
		return result;
	}
	
	public ResultSet allBuses() throws SQLException {
		ResultSet result=null;
		String sql="select bid,bname,btype,ac,seats,fare from bus";
		result=db.select(sql);
		return result;
	}

	public int bookTickets(int uid,String busName, String source, String destination, int tickets) throws SQLException {
		int flag=0,busId=0,seats=0;
		float fare=0;
		Object values[]= {busName};
		ResultSet result=db.select("select bid,fare,seats from bus where bname=?",values);
		while(result.next()) {
			busId=result.getInt("bid");
		    fare=result.getFloat("fare");
		    seats=result.getInt("seats");
		}
		if(seats-tickets<0)
			flag=-1;
		else {
			String columns="uid,bid,bname,source,destination,tickets,fare";
			ArrayList<Object> query=new ArrayList<Object>();
			query.add(uid);
			query.add(busId);
			query.add(busName);
			query.add(source);
			query.add(destination);
			query.add(tickets);
			query.add(tickets*fare);
			flag=db.insert("tickets",columns,query);
			ArrayList<Format> where=new ArrayList<Format>();
			where.add(new Format("bid",busId));
			String update[]= {"seats"};
			Object set[]= {(seats-tickets)};
			flag=db.update("bus",update,set,where);
		}
		
		return flag;
	}

	public ResultSet displayTickets(int uid) throws SQLException {
		
		String sql="select * from tickets where uid=?";
		Object values[]= {uid};
		ResultSet result=db.select(sql,values);
		return result;
	}

	public boolean haveAccess(int uid, int ticketId) throws SQLException {
		String columns="uid,ticketid";
		ArrayList<Format> list=new ArrayList<Format>();
	    list.add(new Format("uid",uid));
	    list.add(new Format("ticketid",ticketId));
		ResultSet result=db.select("tickets",columns,list);
		return result.next();
	}

	public int deleteTicket(int ticketId) throws SQLException {
		ResultSet result=null;
		String sql="select bid,tickets from tickets where ticketid=?";
		Object values[]= {ticketId};
		int busId = 0,tickets = 0,seats = 0,flag=0;
		result=db.select(sql,values);
		while(result.next()) {
			busId=result.getInt("bid");
			tickets=result.getInt("tickets");		
		}
		
		Object values1[]= {busId};
		result=db.select("select seats from bus where bid=?",values1);
		while(result.next()) {
			seats=result.getInt("seats");
		}
		
		ArrayList<Format> where=new ArrayList<Format>();
		where.add(new Format("bid",busId));
		String update[]= {"seats"};
		Object set[]= {(seats+tickets)};
		flag=db.update("bus",update,set,where);
		
		String sql1="delete from tickets where ticketid=?";
		flag=db.delete(sql1,values);
		return flag;
	}
}
