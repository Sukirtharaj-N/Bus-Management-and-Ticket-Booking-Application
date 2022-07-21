package busApp;

import java.util.*;
import java.sql.*;

public class Accounts {
	
	Dbconnection db = new Dbconnection();
		
	public boolean verify(int identity,String username,String password) throws SQLException {
		String columns="uid,identity,username,password";
		ArrayList<Format> list=new ArrayList<Format>();
	    list.add(new Format("identity",identity));
	    list.add(new Format("username",username));
	    list.add(new Format("password",password));
		ResultSet result=db.select("accounts",columns,list);
		return result.next();
	}
	
	public int signUp(int identity, String username, String password, int age, String gender, String email, String phone) throws SQLException {
		String columns="username,age,gender,email,phone,uid";
		int userId = 0;
		ArrayList<Object> query=new ArrayList<Object>();
		query.add(identity);
		query.add(username);
		query.add(password);
		db.insert("accounts","identity,username,password",query);
		ResultSet result=db.select("Select uid from accounts order by timestamps desc limit 1");
		while(result.next())
			userId=result.getInt("uid");
		query.remove(query.size()-1);
		query.remove(0);
		query.add(age);
		query.add(gender);
		query.add(email);
		query.add(phone);
		query.add(userId);
		int flag=0;
		if(identity==1)
			flag = db.insert("merchants",columns,query);
		else
			flag=db.insert("customers", columns,query);
		return flag;
	}

	public ResultSet getUsername(String username) throws SQLException {
		ResultSet result=null;
		String columns="uid,username";
		ArrayList<Format> list=new ArrayList<Format>();
	    list.add(new Format("username",username));
		result=db.select("accounts",columns,list);
		return result;
	}

}
