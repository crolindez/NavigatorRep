package es.carlosrolindez.navigator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;




public class ProductListLoader extends AsyncTaskLoader<ArrayList<Product>> {
	private ArrayList<Product> productList;
	private String filterString;
	private int loaderMode;
	

	
	public ProductListLoader(Context ctx,int id, Bundle filter)
	{
		super(ctx);
		filterString = filter.getString(NavisionTool.QUERY,"").toUpperCase(Locale.US);
		loaderMode = id;
	}
	
	@Override
	public ArrayList<Product> loadInBackground()
	{
		/*  Connection */
	    Connection conn = NavisionTool.openConnection();
	    if (conn!=null) 
	    {
			productList = new ArrayList<Product>();
		    try 
		    {
		    	ResultSet result;
		    	switch (loaderMode)
		    	{

	    		case NavisionTool.LOADER_PRODUCT_IN_USE:
	    			result = NavisionTool.queryListInBOM(filterString);
				    while(result.next())
				    {
				    	Product product = new Product();
				    	product.reference = result.getString(1);	
				    	product.quantity = result.getString(2);		
				    	product.description = NavisionTool.queryDescription(product.reference);	
						product.stock = NavisionTool.queryStock(product.reference);
				    	product.cost = NavisionTool.queryCost(product.reference);
				    	product.inBOM = NavisionTool.queryInBOM(product.reference);
				    	product.hasBOM = true;
				    	productList.add(product);
				    }
					break;

	    		case NavisionTool.LOADER_PRODUCT_BOM:
	    			result = NavisionTool.queryListBOM(filterString);
	      			while(result.next())
					{					
						
				    	Product product = new Product();
				    	product.reference = result.getString(1);	
				    	product.description = result.getString(2) + result.getString(4);	
				    	product.quantity = result.getString(3);		
						product.stock = NavisionTool.queryStock(product.reference);
						product.cost = NavisionTool.queryCost(product.reference);
				    	product.inBOM = true;
				    	product.hasBOM = NavisionTool.queryHasBOM(product.reference);
				    	productList.add(product);
					}	    		   
			 				break;

	    		case NavisionTool.LOADER_PRODUCT_SEARCH:
	    		default:
	    		    result = NavisionTool.queryList(filterString);
	      			while(result.next())
					{					
						Product product = new Product(); 
						product.reference = result.getString(1);
						product.description = result.getString(2) + result.getString(3);	
						product.stock = NavisionTool.queryStock(product.reference);
						product.cost = result.getString(4);
						product.hasBOM = !(result.getString(5).isEmpty());
		    			product.inBOM = NavisionTool.queryInBOM(product.reference);
						productList.add(product);
					}
					break;
		    	}		    	

     	
			} catch (SQLException e) {
				e.printStackTrace();
			}

		    NavisionTool.closeConnection();
		    return productList;
	    } 
	    return null;
 
	}
	
	
	@Override
	public void deliverResult(ArrayList<Product> arrayList)
	{
	    productList = arrayList;
		
		if (isStarted())
		{
			super.deliverResult(arrayList);
		}
	}
	
	@Override
	protected void onStartLoading()
	{
		if (productList != null)
		{
			deliverResult(productList);
		}
		else
		{
			forceLoad();
		}
		
	}
	
	@Override
	protected void onStopLoading()
	{
		cancelLoad();
	}
	
	@Override
	protected void onReset()
	{
		    if (productList!=null)
	    {
	    	productList.clear();
		    productList = null;
    	}
		onStopLoading();
	}
	
	@Override
	public void onCanceled(ArrayList<Product> arrayList)
	{
		super.onCanceled(arrayList);
	}
	
}
