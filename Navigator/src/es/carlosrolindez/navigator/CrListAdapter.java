package es.carlosrolindez.navigator;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CrListAdapter extends BaseAdapter {
//	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<Product> mProductList;
	
	public CrListAdapter(Activity activity,ArrayList<Product> productList)
	{
		mProductList = productList;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount()
	{
		if (mProductList == null)
			return 0;
		else
			return mProductList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		if (mProductList == null)
			return 0;
		else			
			return mProductList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
			return position;
	}
	
	public ArrayList<Product>getProductList() 
	{
		return mProductList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{	
		final Product product;
		float costValue;
		float stockValue;
		float quantityValue;
	
		float purchaseValue;
		float inProductionValue;
		float saleValue;
		float usedInProductionValue;
		float transferValue;		
		float orderPointValue;
		
	    if (mProductList == null)
	    	return null;
		View localView = convertView;
		if (localView==null)
		{
			localView = inflater.inflate(R.layout.list_row, parent, false);
		}
		

		TextView reference = (TextView)localView.findViewById(R.id.reference_list);
		TextView description = (TextView)localView.findViewById(R.id.description_list);
		TextView quantity = (TextView)localView.findViewById(R.id.quantity_list);
		TextView stock = (TextView)localView.findViewById(R.id.stock_list);
		TextView cost =  (TextView)localView.findViewById(R.id.cost_list);
		
		ImageButton hasBoom = (ImageButton)localView.findViewById(R.id.arrowRight_list);
		ImageButton inBoom = (ImageButton)localView.findViewById(R.id.arrowLeft_list);
		product = mProductList.get(position);
		
		reference.setText(product.reference);
		description.setText(product.description);
		quantity.setText("");
		
		
	
		stockValue = Float.parseFloat(product.stock);
		if (stock != null) 
		{
			stock.setText(String.format("%,6.1f un.",stockValue));
		}	
		costValue = Float.parseFloat(product.cost);
		if (cost != null)
		{
			cost.setText(String.format("%,6.2f €",costValue));
		}
		
		if ((quantity != null) && (product.quantity != ""))
		{
			quantityValue = Float.parseFloat(product.quantity);
			quantity.setText(String.format("x%,6.2f",quantityValue));
		}	
	
			
		if (product.inBOM)
			inBoom.setVisibility(View.VISIBLE);
		else
			inBoom.setVisibility(View.INVISIBLE);
		
		if (product.hasBOM)
			hasBoom.setVisibility(View.VISIBLE);
		else
			hasBoom.setVisibility(View.INVISIBLE);
	
		if ((quantity != null) && (product.quantity != ""))
			purchaseValue =  Float.parseFloat(product.purchase);
		else
			purchaseValue = 0;
			
			
		inProductionValue =  Float.parseFloat(product.inProduction);
		saleValue =  Float.parseFloat(product.sale);
		usedInProductionValue =  Float.parseFloat(product.usedInProduction);
		transferValue =  Float.parseFloat(product.transfer);
		orderPointValue = Float.parseFloat(product.orderPoint);
		
		if ( (stockValue + purchaseValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue))
			localView.setBackground(localView.getResources().getDrawable(R.drawable.consume_bg));
		else if ( (stockValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue))
			localView.setBackground(localView.getResources().getDrawable(R.drawable.stock_bg));			
		else if ( (stockValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue + orderPointValue))
			localView.setBackground(localView.getResources().getDrawable(R.drawable.danger_bg));	
			
		
		
		inBoom.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
		    	Intent intent = new Intent (v.getContext(), InfoActivity.class);
	        	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, product.reference);        	
	        	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, product.description);  
	        	intent.putExtra(NavisionTool.LAUNCH_INFO_MODE, NavisionTool.INFO_MODE_IN_USE);
	        	v.getContext().startActivity(intent);	        	
			}
		});
		
		hasBoom.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
		    	Intent intent = new Intent (v.getContext(), InfoActivity.class);
	        	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, product.reference);        	
	        	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, product.description);  
	        	intent.putExtra(NavisionTool.LAUNCH_INFO_MODE, NavisionTool.INFO_MODE_BOM);
	        	v.getContext().startActivity(intent);	     
			}

		});
		
		return localView;
	}
	

	public  void showResultSet(  ArrayList<Product> productList)
	{
	    mProductList = productList;
	    notifyDataSetChanged();		
	}
	
}
