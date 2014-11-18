package es.carlosrolindez.navigator;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.util.Log;





public class NavisionTool 
{
	
	public static final int LOADER_PRODUCT_SEARCH = 1;
	public static final int LOADER_PRODUCT_IN_USE = 2;
	public static final int LOADER_PRODUCT_BOM = 3;
	public static final int LOADER_PRODUCT_INFO = 4;
	public static final String LAUNCH_REFERENCE = "es.carlosrolindez.navisiontool.LAUNCH_REFERENCE";
	public static final String QUERY = "QUERY";
	
	
    static String connString;
    static String username;
    static String password;
    static Connection conn;

	static {
		conn = null;
		connString = "jdbc:jtds:sqlserver://192.168.1.4:1433/LittleNavision";
		username = "sa";
		password = "Julia2009";
	}
	
	static public void changeConn(String newConnString,String newUserName,String newPassword)
	{
		connString=newConnString;
		username=newUserName;
		password=newPassword;
	}
	
	
	static public Connection openConnection()
	{
	    if (conn!= null)  
	    {
	    	try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	conn = null;
	    }
	    
	    try 
	    {
		    String driver = "net.sourceforge.jtds.jdbc.Driver";
		    Class.forName(driver).newInstance();
		    DriverManager.setLoginTimeout(15);
		    conn = DriverManager.getConnection(NavisionTool.connString,NavisionTool.username,NavisionTool.password);
		    return conn;		    
	    } 
	    catch (SQLException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return null;
	}
	
	static public void closeConnection()
	{
		if (conn!=null) 
		{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
		
	}
	
	static public ResultSet queryList(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	
		    String headSqlString = "SELECT TOP 1000 [No_],[Description],[Description 2],[Unit Cost],[Production BOM No_] FROM [EIS$Item] WHERE ([No_] like '%";
		    String middleSqlString = "%') or (UPPER([Description]+[Description 2]) like '%";
		    String tailSqlSring = "%')  ORDER BY [No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + middleSqlString + filterString + tailSqlSring);
		    return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public ResultSet queryListBOM(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	
		    String headSqlString = "SELECT TOP 1000 [No_],[Description],[Quantity],[Description 2] FROM [EIS$Production BOM Line] WHERE [Production BOM No_] = '";	    
		    String tailSqlSring = "' AND [Version Code]=''  ORDER BY [No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	static public ResultSet queryListInBOM(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	
		    String headSqlString = "SELECT TOP 1000 [Production BOM No_], [Quantity] FROM [EIS$Production BOM Line] WHERE [No_] = '";	    
		    String tailSqlSring = "' AND [Version Code]=''  ORDER BY [Production BOM No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	static public Boolean queryHasBOM(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	
		    String headSqlString = "SELECT TOP 1000 [No_] FROM [EIS$Production BOM Line] WHERE [Production BOM No_] = '";	    
		    String tailSqlSring = "' AND [Version Code]=''  ORDER BY [No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    return (result.isBeforeFirst());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public Boolean queryInBOM(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	
		    String headSqlString = "SELECT TOP 1000  [Production BOM No_] FROM [EIS$Production BOM Line] WHERE [No_] = '";	    
		    String tailSqlSring = "' AND [Version Code]=''  ORDER BY [Production BOM No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    return (result.isBeforeFirst());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public String queryDescription(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	
		    String headSqlString = "SELECT [Description],[Description 2] FROM [EIS$Item] WHERE ([No_] = '";
		    String tailSqlSring = "')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	return (result.getString(1) + result.getString(2));
		    }
		    else
		    	return "";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public String queryCost(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	
		    String headSqlString = "SELECT [Unit Cost] FROM [EIS$Item] WHERE ([No_] = '";
		    String tailSqlSring = "')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	return (result.getString(1));
		    }
		    else
		    	return "0.0";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public String queryStock(String filterString)
	{
	    Statement stmt;
		try 
		{
			Log.e("stocking ", filterString);
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
            String headSqlString = "SELECT sum([Quantity]) FROM  [EIS$Item Ledger Entry] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) AND  ([Location Code]='01') GROUP BY [Item No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	Log.e("stocking", " ok");
		    	return (result.getString(1));
		    }
		    else
		    {
		    	Log.e("stocking", " void");
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	Log.e("stocking", " exception");
		return null;
	}


}
