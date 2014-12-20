package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;


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
    public void onActivityCreated(Bundle savedInstanceState) {	
    	super.onActivityCreated(savedInstanceState);
    	if (savedInstanceState != null) 
        	productList = savedInstanceState.getParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY);

	    if (productList!=null) 
       		showResultSet(productList);
	    
	    progressAllowed = true;
        if (progressPending) showProgress (true);
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

				TextView price = (TextView)getActivity().findViewById(R.id.general_info_price);
				priceValue = Float.parseFloat(product.price);
				price.setText(String.format("%.2f €",priceValue));

				TextView cost = (TextView)getActivity().findViewById(R.id.general_info_cost);
				costValue = Float.parseFloat(product.cost);
				if ( (costValue>0) && ( priceValue>0) )
					cost.setText(String.format("%.2f € (%.2f%%)",costValue,(costValue/priceValue)));
				else
					cost.setText(String.format("%.2f €",costValue));

				
				TextView purchase = (TextView)getActivity().findViewById(R.id.general_info_purchase);
				purchaseValue = Float.parseFloat(product.purchase);
				purchase.setText(String.format("%.1f un.",purchaseValue));

				TextView inProduction = (TextView)getActivity().findViewById(R.id.general_info_in_production);
				inProductionValue = Float.parseFloat(product.inProduction);
				inProduction.setText(String.format("%.1f un.",inProductionValue));

				TextView inPlannedProduction = (TextView)getActivity().findViewById(R.id.general_info_in_planned_produccions);
				inPlannedProductionValue = Float.parseFloat(product.inPlannedProduction);
				inPlannedProduction.setText(String.format("%.1f un.",inPlannedProductionValue));

				TextView sale = (TextView)getActivity().findViewById(R.id.general_info_sales);
				saleValue = Float.parseFloat(product.sale);
				sale.setText(String.format("%.1f un.",saleValue));

				TextView usedInProduction = (TextView)getActivity().findViewById(R.id.general_info_used_in_production);
				usedInProductionValue = Float.parseFloat(product.usedInProduction);
				usedInProduction.setText(String.format("%.1f un.",usedInProductionValue));

				TextView transfer = (TextView)getActivity().findViewById(R.id.general_info_transfer);
				transferValue = Float.parseFloat(product.transfer);
				transfer.setText(String.format("%.1f un.",transferValue));

				TextView usedInPlannedProduction = (TextView)getActivity().findViewById(R.id.general_info_used_in_planned_produccions);
				usedInPlannedProductionValue = Float.parseFloat(product.usedInPlannedProduction);
				usedInPlannedProduction.setText(String.format("%.1f un.",usedInPlannedProductionValue));

				orderPointValue = Float.parseFloat(product.orderPoint);
				
				if ( (stockValue + purchaseValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue))
					getActivity().findViewById(R.id.stock_relative_layout).setBackground(getResources().getDrawable(R.drawable.consume_bg));
				else if ( (stockValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue))
					getActivity().findViewById(R.id.stock_relative_layout).setBackground(getResources().getDrawable(R.drawable.stock_bg));
				else if ( (stockValue + inProductionValue) < (saleValue + transferValue + usedInProductionValue + orderPointValue))
					getActivity().findViewById(R.id.stock_relative_layout).setBackground(getResources().getDrawable(R.drawable.danger_bg));
				
	        	View stockLayout = getActivity().findViewById(R.id.stock_relative_layout);
	        	stockLayout.setOnClickListener(new View.OnClickListener() {      
	        	    @Override
	        	    public void onClick(View v) 
	        	    {
	        	   		LayoutInflater inflater = getActivity().getLayoutInflater();
	        	    	View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) getActivity().findViewById(R.id.toast_layout_root));

	        	    	TextView text = (TextView) layout.findViewById(R.id.text_layout);
	        	    	text.setText("Stock Pressed");

	        	    	Toast toast = new Toast(getActivity().getApplicationContext());
	        	    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	        	    	toast.setDuration(Toast.LENGTH_SHORT);
	        	    	toast.setView(layout);
	        	    	toast.show();		
	        	    }

	        	});
	   				
				
				GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
				    new GraphViewData(1, 2.0d)
				    , new GraphViewData(2, 1.5d)
				    , new GraphViewData(3, 2.5d)
				    , new GraphViewData(4, 1.0d)
				});
				 
				graphView = new LineGraphView(
				    getActivity()     // context
				    , "Consume" // heading
				);
				graphView.addSeries(exampleSeries); // data
				 
				LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graph_view_layout);
				layout.addView(graphView);
				
			}
		

		}
	}
	
	
}
