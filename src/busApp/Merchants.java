package busApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Merchants {
	Dbconnection db = new Dbconnection();

	public int addBus(String busName, String busType, String ac, int seats, float fare, int uid) throws SQLException {
		int flag=0;
		String columns="bname,btype,ac,seats,fare,uid";
		ArrayList<Object> query=new ArrayList<Object>();
		query.add(busName);
		query.add(busType);
		query.add(ac);
		query.add(seats);
		query.add(fare);
		query.add(uid);
		db.insert("bus",columns,query);
		String sql = "Select bid from bus order by timestamps desc limit 1";
	    ResultSet result=db.select(sql);
	   	while(result.next()) {
			flag=result.getInt("bid");
		}
		return flag;
	}

	public ResultSet getMyBus(int uid) throws SQLException{
		ResultSet result=null;
		String columns="bid,bname,btype,ac,seats,fare";
		ArrayList<Format> list=new ArrayList<Format>();
	    list.add(new Format("uid",uid));
		result=db.select("bus",columns,list);
		return result;
	}

	public boolean haveAccess(int uid, String busName) throws SQLException {
		String columns="uid,bname";
		ArrayList<Format> list=new ArrayList<Format>();
	    list.add(new Format("uid",uid));
	    list.add(new Format("bname",busName));
		ResultSet result=db.select("bus",columns,list);
		return result.next();
	}

}
