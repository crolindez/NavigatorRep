package es.carlosrolindez.navigator;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;





public class NavisionTool 
{
	
	public static final int LOADER_PRODUCT_SEARCH = 3;
	public static final int LOADER_PRODUCT_IN_USE = 0;
	public static final int LOADER_PRODUCT_BOM = 2;
	public static final int LOADER_PRODUCT_INFO = 1;
	public static final int LOADER_PRODUCT_SEARCH_IN_USE = 4;
	public static final int LOADER_PRODUCT_SEARCH_BOM = 5;	
	public static final int LOADER_PRODUCT_IN_OUT = 6;
	
	public static final String LAUNCH_REFERENCE = "es.carlosrolindez.navisiontool.LAUNCH_REFERENCE";	
	public static final String LAUNCH_DESCRIPTION = "es.carlosrolindez.navisiontool.LAUNCH_DESCRIPTION";
	public static final String LAUNCH_INFO_MODE = "es.carlosrolindez.navisiontool.LAUNCH_INFO_MODE";	
	public static final String LAUNCH_IN_OUT_MODE = "es.carlosrolindez.navisiontool.LAUNCH_IN_OUT_MODE";
	
	public static final int INFO_MODE_IN_USE = 0;
	public static final int INFO_MODE_FULL = 1;
	public static final int INFO_MODE_BOM = 2;		
	public static final int INFO_MODE_SERACH_IN_USE = 3;
	public static final int INFO_MODE_SEARCH_BOM = 4;	
	public static final int IN_OUT_MODE_IN = 5;
	public static final int IN_OUT_MODE_OUT = 6;
	
	public static final String QUERY = "QUERY";
	
	public static final int MODE_EMULATOR = 0;
	public static final int MODE_REAL = 1;


	public static final String PRODUCT_LIST_KEY = "ProductListKey";
	public static final String IN_OUT_LIST_KEY = "InOutListKey";
	
    private static String connString;
    private static String username;
    private static String password;
    private static Connection conn;
    private static int mode;

	static {
		conn = null;
		mode = MODE_REAL;
		connString = "jdbc:jtds:sqlserver://192.0.0.102:1855/EIS";
		username = "sa";
		password = "Advising,2007";
	}
	
	static public int readMode()
	{
		return mode;
	}
	
	static public void changeMode(int newMode)
	{
		mode = newMode;

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

		if (mode == MODE_EMULATOR) return null;
		
	    
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
	
		    String headSqlString = "SELECT TOP 1000 [No_],[Description],[Description 2],[Unit Cost],[Production BOM No_],[Reorder Point] FROM [EIS$Item] WHERE ([No_] Like '%";
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
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
            String headSqlString = "SELECT sum([Quantity]) FROM  [EIS$Item Ledger Entry] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) AND  ([Location Code]='01') GROUP BY [Item No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public String queryHandWorkCost(String filterString)
	{
	    Statement stmt;
	    float quantity;
	    float duration;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([Output Quantity]) FROM  [EIS$Capacity Ledger Entry] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) AND  ([Last Output Line]='1') GROUP BY [Item No_]";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    	{
		    		quantity = Float.parseFloat(result.getString(1));
		    		headSqlString = "SELECT sum([Quantity]) FROM  [EIS$Capacity Ledger Entry] WHERE  ([Item No_] = '";
		    	    tailSqlSring = "' )GROUP BY [Item No_]";
				    result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
			    
				    if (result.isBeforeFirst())
				    {
				    	result.next();
				    	if (result.getString(1)==null) 
				    		return "0.0";
				    	else
				    	{
				    		duration = Float.parseFloat(result.getString(1));
				    		return (String.format(Locale.US,"%,6.2f �",0.318667*duration/quantity));
				    	}
				    }
				    else
				    {
				    	return "0.0";
				    }
		    	}
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;

	}
	
	static public String queryOrderPoint(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
            String headSqlString = "SELECT [Reorder Point] FROM  [EIS$Item] WHERE  ([No_] = '";
    	    String tailSqlSring = "' )";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public String queryInPlannedProduction(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([Remaining Quantity]) FROM  [EIS$Prod_ Order Line] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) AND ([Status]='2')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public String queryInProduction(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([Remaining Quantity]) FROM  [EIS$Prod_ Order Line] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) AND ([Status]='3')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public String queryPurchase(String filterString)
	{       	   
		Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([Outstanding Quantity]) FROM  [EIS$Purchase Line] WHERE  ([No_] = '";
    	    String tailSqlSring = "' ) AND ([Document Type]='1')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	    
	}
	
	static public String queryTransfer(String filterString)
	{
		Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([EIS$Transfer Line].[Quantity]) FROM  [EIS$Transfer Line], [EIS$Transfer Header] WHERE  ([EIS$Transfer Line].[Document No_] = [EIS$Transfer Header].[No_]) AND  ([EIS$Transfer Line].[Item No_] = '";
    	    String tailSqlSring = "' ) AND ([EIS$Transfer Header].[Status] = 0) ";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	    
	}
	
	static public String querySale(String filterString)
	{
		Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([Outstanding Quantity]) FROM  [EIS$Sales Line] WHERE  ([No_] = '";
    	    String tailSqlSring = "' ) AND ([Location Code]='01') AND ([Document Type]='1')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	    
	}
	
	static public String queryUsedInPlannedProduction(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([Remaining Quantity]) FROM  [EIS$Prod_ Order Component] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) AND ([Status]='2')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public String queryUsedInProduction(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT sum([Remaining Quantity]) FROM  [EIS$Prod_ Order Component] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) AND ([Status]='3')";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public String queryConsumeInPeriod(String filterString,String fromDate,String toDate)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT Sum([Quantity]) FROM   EIS.dbo.[EIS$Item Ledger Entry] WHERE  ([Item No_] = '";
    	    String tailSqlString1 = "' ) AND ([Quantity]<'0') AND ([Location Code]='01') AND ([Posting Date]>={ts '";
    	    String tailSqlString2 = "'} AND [Posting Date]<={ts '"; 
    	    String tailSqlString3 = "'})";
    	    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlString1 + fromDate + tailSqlString2 + toDate + tailSqlString3);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public String queryRetailPrice(String filterString)
	{
	    Statement stmt;
		try 
		{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String headSqlString = "SELECT max([Unit Price]) FROM  [EIS$Sales Price] WHERE  ([Item No_] = '";
    	    String tailSqlSring = "' ) ";
		    
		    ResultSet result = stmt.executeQuery(headSqlString + filterString + tailSqlSring);
		    if (result.isBeforeFirst())
		    {
		    	result.next();
		    	if (result.getString(1)==null) 
		    		return "0.0";
		    	else
		    		return (result.getString(1));
		    }
		    else
		    {
		    	return "0.0";
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public ResultSet queryListPurchase(String filterString)
	{
		return null;
	}
	
	static public ResultSet queryListFabrication(String filterString)
	{
		return null;
	}

	static public ResultSet queryListPlannedFabrication(String filterString)
	{
		return null;
	}	
	
	static public ResultSet queryListSales(String filterString)
	{
		return null;
	}

	static public ResultSet queryListTransfer(String filterString)
	{
		return null;
	}	
	
	static public ResultSet queryListInFabrication(String filterString)
	{
		return null;
	}

	static public ResultSet queryListInPlannedFabrication(String filterString)
	{
		return null;
	}


}
