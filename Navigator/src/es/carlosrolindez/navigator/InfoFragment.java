package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;


public class InfoFragment extends Fragment
{
	private ArrayList<Product> productList;
	private boolean progressAllowed;
	private boolean progressPending;
	private GraphView graphView; 
	
	public static InfoFragment newInstance() 
	{
		InfoFragment  fragment = new InfoFragment();
		fragment.productList = null;
		fragment.progressAllowed = false;
		fragment.progressPending = false;
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) 
    {
    	return inflater.inflate(R.layout.general_info_enh_layout, container, false);    
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) 
    {	
    	super.onActivityCreated(savedInstanceState);
    	if (savedInstanceState != null) 
        	productList = savedInstanceState.getParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY);
	    
	    progressAllowed = true;
        if (progressPending) showProgress (true);
 
	    if (productList!=null) 
       		showResultSet(productList);
    }    	
   
    @Override
    public void onSaveInstanceState(Bundle savedState) 
	{
	    super.onSaveInstanceState(savedState);
	    savedState.putParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY, productList);
	}   

    public void showProgress(boolean progress)
	{
    	if (progressAllowed )
    	{
        	if (progress) getActivity().findViewById(R.id.loadingPanel_general_info).setVisibility(View.VISIBLE);
        	else getActivity().findViewById(R.id.loadingPanel_general_info).setVisibility(View.GONE);
        	progressPending = false;     	   
        }
    	else progressPending = progress;
	}

	public void showResultSet(ArrayList<Product> productListLoaded)
	{
		Product product = null;
		float stockValue;
		float costValue;
		float priceValue;
		float purchaseValue;
		float inProductionValue;
		float inPlannedProductionValue;
		float saleValue;
		float usedInProductionValue;
		float transferValue;
		float usedInPlannedProductionValue;		
		float orderPointValue;

		
		if (productListLoaded == null) 
		{
			productList = null;
		}
		else
		{
	   		productList = new ArrayList<Product>();
			for (Product item : productListLoaded)
			{
				if (item.itemMode == NavisionTool.LOADER_PRODUCT_INFO)
				{
					productList.add(item);
					product = item;
				}
			}
			if (product != null && getActivity()!=null)
			{

				TextView stock = (TextView)getActivity().findViewById(R.id.general_info_stock);
				stockValue = Float.parseFloat(product.stock);
				stock.setText(String.format("%.1f un.",stockValue));
				
				TextView orderPoint = (TextView)getActivity().findViewById(R.id.general_info_order_point);
				orderPointValue = Float.parseFloat(product.orderPoint);
				orderPoint.setText(String.format("  (%.1f)",orderPointValue));

				TextView price = (TextView)getActivity().findViewById(R.id.general_info_price);
				priceValue = Float.parseFloat(product.price);
				price.setText(String.format("%.2f €",priceValue));

				TextView cost = (TextView)getActivity().findViewById(R.id.general_info_cost);
				costValue = Float.parseFloat(product.cost);
				if ( (costValue>0) && ( priceValue>0) )
					cost.setText(String.format("%.2f € (%.2f%%)",costValue,100*(costValue/priceValue)));
				else
					cost.setText(String.format("%.2f €",costValue));

				
				TextView purchase = (TextView)getActivity().findViewById(R.id.general_info_purchase);
				purchaseValue = Float.parseFloat(product.purchase);
				if (purchaseValue>0)
					purchase.setText(String.format("%.1f un.",purchaseValue));
				else
					getActivity().findViewById(R.id.purchase_layout).setVisibility(View.GONE);
				
				TextView inProduction = (TextView)getActivity().findViewById(R.id.general_info_in_production);
				inProductionValue = Float.parseFloat(product.inProduction);
				if (inProductionValue>0)
					inProduction.setText(String.format("%.1f un.",inProductionValue));
				else
					getActivity().findViewById(R.id.production_layout).setVisibility(View.GONE);

				TextView inPlannedProduction = (TextView)getActivity().findViewById(R.id.general_info_in_planned_productions);
				inPlannedProductionValue = Float.parseFloat(product.inPlannedProduction);
				if (inPlannedProductionValue>0)
					inPlannedProduction.setText(String.format("%.1f un.",inPlannedProductionValue));
				else
					getActivity().findViewById(R.id.in_planned_productions_layout).setVisibility(View.GONE);

				TextView sale = (TextView)getActivity().findViewById(R.id.general_info_sales);
				saleValue = Float.parseFloat(product.sale);
				if (saleValue>0)
					sale.setText(String.format("%.1f un.",saleValue));
				else
					getActivity().findViewById(R.id.sales_layout).setVisibility(View.GONE);
				
				TextView usedInProduction = (TextView)getActivity().findViewById(R.id.general_info_used_in_production);
				usedInProductionValue = Float.parseFloat(product.usedInProduction);
				if (usedInProductionValue>0)
					usedInProduction.setText(String.format("%.1f un.",usedInProductionValue));
				else
					getActivity().findViewById(R.id.used_in_production_layout).setVisibility(View.GONE);

				TextView transfer = (TextView)getActivity().findViewById(R.id.general_info_transfer);
				transferValue = Float.parseFloat(product.transfer);
				if (transferValue>0)
					transfer.setText(String.format("%.1f un.",transferValue));
				else
					getActivity().findViewById(R.id.transfer_layout).setVisibility(View.GONE);

				TextView usedInPlannedProduction = (TextView)getActivity().findViewById(R.id.general_info_used_in_planned_productions);
				usedInPlannedProductionValue = Float.parseFloat(product.usedInPlannedProduction);
				if (usedInPlannedProductionValue>0)
					usedInPlannedProduction.setText(String.format("%.1f un.",usedInPlannedProductionValue));
				else
					getActivity().findViewById(R.id.used_in_planned_productions_layout).setVisibility(View.GONE);
				
				if ( (stockValue + purchaseValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue))
					getActivity().findViewById(R.id.stock_layout).setBackground(getResources().getDrawable(R.drawable.consume_bg));
				else if ( (stockValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue))
					getActivity().findViewById(R.id.stock_layout).setBackground(getResources().getDrawable(R.drawable.stock_bg));
				else if ( (stockValue + purchaseValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue + orderPointValue))
					getActivity().findViewById(R.id.stock_layout).setBackground(getResources().getDrawable(R.drawable.danger_bg));
			

				final String reference = product.reference;
				final String description = product.description;		
				
	        	View inLayout = getActivity().findViewById(R.id.in_layout);
	        	inLayout.setOnClickListener(new View.OnClickListener() {      
	        	    @Override
	        	    public void onClick(View v) 
	        	    {
	        	    	Intent intent = new Intent (v.getContext(), InOutActivity.class);
	                	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, reference);        	
	                	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, description);  
	                	intent.putExtra(NavisionTool.LAUNCH_IN_OUT_MODE, NavisionTool.IN_OUT_MODE_IN);
	                	startActivity(intent);
	}

	        	});

	        	View outLayout = getActivity().findViewById(R.id.out_layout);
	        	outLayout.setOnClickListener(new View.OnClickListener() {      
	        	    @Override
	        	    public void onClick(View v) 
	        	    {
	        	    	Intent intent = new Intent (v.getContext(), InOutActivity.class);
	                	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, reference);        	
	                	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, description);  
	                	intent.putExtra(NavisionTool.LAUNCH_IN_OUT_MODE, NavisionTool.IN_OUT_MODE_OUT);
	                	startActivity(intent);
	        	    }

	        	});
	        	
	        	GraphViewData[] graphViewData = new GraphViewData[Product.NUMBER_OF_MONTHS];	        	
	        	GraphViewData[] graphViewData2 = new GraphViewData[Product.NUMBER_OF_MONTHS];

				for (int i=0; i < Product.NUMBER_OF_MONTHS; i++)
				{	
					graphViewData[i] = new GraphViewData(i+1,-Double.parseDouble(product.consumeByMonth[i]));
					graphViewData2[i] = new GraphViewData(i+1,0.0d);
				}
				GraphViewSeries graph = new GraphViewSeries(graphViewData);
				GraphViewSeries graph2 = new GraphViewSeries(graphViewData2);
				graphView = new BarGraphView( getActivity() , "Consume");
				graphView.addSeries(graph); 
				graphView.addSeries(graph2); 
					
				graphView.getGraphViewStyle().setNumHorizontalLabels(Product.NUMBER_OF_MONTHS); 
				LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graph_view_layout);
				layout.addView(graphView);
				
			}
		}
	}
	
	
}
