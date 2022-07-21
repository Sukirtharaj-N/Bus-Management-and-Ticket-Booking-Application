package busApp;

import java.sql.*;
import java.util.*;


public class Dbconnection{
    public static final String url = "jdbc:postgresql://127.0.0.1:5432/busApp";
    public static final String user="postgres";
    public static final String pass="password";
    
    public int insert(String tableName, String columns, ArrayList<Object> values) throws SQLException{
    	int flag=0,i;
    	Connection connection = null;
        try {
        	String sql = "INSERT INTO " + tableName + " (" + columns + ")" + " VALUES (";
        	for (i=0;i<values.size()-1;i++)
        		sql+="?,";
        	sql+="?);";
        		
        	connection = DriverManager.getConnection(url,user,pass); 
        	PreparedStatement statement = connection.prepareStatement(sql);
        	for (i=0;i<values.size();i++)
        		statement.setObject(i+1, values.get(i));
            flag = statement.executeUpdate();
            } catch (SQLException e) {
            	System.out.println(e);
              }
        finally {
        	connection.close();
        }
        return flag;
    }

	public ResultSet select(String tableName, String columns,ArrayList<Format> conditions) throws SQLException{
		ResultSet result=null;
		Connection connection = null;
		try {
			int i;
        	String sql = "SELECT " + columns + " FROM " + tableName + " WHERE ";
        	for(i=0;i<conditions.size()-1;i++)
        		sql+=conditions.get(i).getCondition() + "=? AND ";
        	sql+=conditions.get(i).getCondition() + "=?";
//        	System.out.println(sql);

        	connection = DriverManager.getConnection(url,user,pass); 
        	PreparedStatement statement = connection.prepareStatement(sql);
        	for(i=0;i<conditions.size();i++)
        		statement.setObject(i+1,conditions.get(i).getValue());
            result = statement.executeQuery();
            } catch (SQLException e) {
            	System.out.println(e);
            	}
		finally {
			connection.close();
		}
		return result;
	}
	
	public ResultSet select(String sql) throws SQLException {
		ResultSet result=null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url,user,pass); 
        	PreparedStatement statement = connection.prepareStatement(sql);
            result = statement.executeQuery();
            } catch (SQLException e) {
            	System.out.println(e);
            	}
		finally {
			connection.close();
		}
		return result;
	}
	
	public ResultSet select(String sql, Object values[]) throws SQLException {
		ResultSet result=null;
		Connection connection = null;
		try {
			int i;
//        	System.out.println(sql);
        	connection = DriverManager.getConnection(url,user,pass); 
        	PreparedStatement statement = connection.prepareStatement(sql);
        	for(i=0;i<values.length;i++)
        		statement.setObject(i+1,values[i]);
            result = statement.executeQuery();
            } catch (SQLException e) {
            	System.out.println(e);
            	}
		finally {
			connection.close();
		}
		return result;
		
	}

	public int update(String tableName, String[] update, Object[] set, ArrayList<Format> where) throws SQLException {
		int flag=0,i;
		Connection connection = null;
        try {
        	String sql = "UPDATE " + tableName + " SET ";
        	for (i=0;i<(update.length)-1;i++)
        		sql+=update[i]+"=?,";
        	sql+=update[i]+"=? WHERE ";
        	for(i=0;i<where.size()-1;i++)
        		sql+=where.get(i).getCondition() + "=? AND ";
        	sql+=where.get(i).getCondition() + "=?";
        		
        	connection = DriverManager.getConnection(url,user,pass); 
        	PreparedStatement statement = connection.prepareStatement(sql);
        	for (i=0;i<update.length;i++)
        		statement.setObject(i+1, set[i]);
        	for(int j=0;j<where.size();j++,i++)
        		statement.setObject(i+1, where.get(j).getValue());
//        	System.out.println(sql);
            flag = statement.executeUpdate();
            } catch (SQLException e) {
            	System.out.println(e);
               }
        finally {
			connection.close();
		}
        return flag;
	}
	
	public int delete(String sql,Object values[]) throws SQLException{
    	int flag=0,i;
    	Connection connection = null;
        try {		
        	connection = DriverManager.getConnection(url,user,pass); 
        	PreparedStatement statement = connection.prepareStatement(sql);
        	for(i=0;i<values.length;i++)
        		statement.setObject(i+1,values[i]);
            flag = statement.executeUpdate();
            } catch (SQLException e) {
            	System.out.println(e);
              }
        finally {
        	connection.close();
        }
        return flag;
    }
    
}
