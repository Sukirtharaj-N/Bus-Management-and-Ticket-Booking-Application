package busApp;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Bus {
	Dbconnection db = new Dbconnection();
	DateFormat formatter = new SimpleDateFormat("HH:mm");
	Time timeValue;


	
	public int stopsDetails(int busId, int n, String[] stops, String[] time) throws SQLException {
		int flag=0;
		String columns="bid,stopno,stopname,timing";
		for(int i=0;i<n;i++) {
			ArrayList<Object> query=new ArrayList<Object>();
			query.add(busId);
			query.add(i+1);
			query.add(stops[i]);
			try {
				timeValue=new Time(formatter.parse(time[i]).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			query.add(timeValue);
			flag+=db.insert("stops",columns,query);
		}
		return flag;
	}

	public int updateBus(String busName, String[] update, Object[] set) throws SQLException {
		//sample-->update bus set ac='ac' where bname='shakthi'; //need to validate access of the username to the bus
				ArrayList<Format> where=new ArrayList<Format>();
			    where.add(new Format("bname",busName));
				int flag=db.update("bus",update,set,where);
				return flag;
	}

	public ResultSet getStopDetails(int busId) throws SQLException {
		String sql="select stopno,stopname,timing from stops where bid=? order by stopno";
		Object values[]= {busId};
		ResultSet result=db.select(sql,values);
		return result;
	}

	public int deleteBuses(String busName) throws SQLException {
		String sql="delete from bus where bname=?";
		Object values[]= {busName};
		int flag=db.delete(sql,values);
		return flag;
	}

	public boolean verifyStops(String source, String destination, String busName) throws SQLException {
		ResultSet result=null;
		int busId=0;
		Object values1[]= {busName};
		result=db.select("select bid from bus where bname=?",values1);
		while(result.next()) {
			busId=result.getInt("bid");
		}
		String sql="select s1.bid from stops as s1 inner join stops as s2 on s1.bid=s2.bid and s1.stopname=? and s2.stopname=? and s1.stopno<=s2.stopno and s1.bid=?";
		Object values[]= {source,destination,busId};
		result=db.select(sql,values);
		return result.next();
	}
	
}
