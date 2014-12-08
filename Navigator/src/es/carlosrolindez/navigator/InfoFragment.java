package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InfoFragment extends Fragment
{
	private ArrayList<Product> productList;

	public static InfoFragment newInstance() 
	{
		InfoFragment  fragment = new InfoFragment();
		fragment.productList = null;
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
    	return  inflater.inflate(R.layout.general_info_layout, container, false);    
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) {	
    	super.onActivityCreated(savedInstanceState);
    	
    	if (savedInstanceState != null) 
        	productList = savedInstanceState.getParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY);

	    if (productList!=null) 
       		showResultSet(productList);
      }    	
    
	public void showResultSet(ArrayList<Product> productListLoaded)
	{
		Product product = null;
		float stockValue;
		float orderPointValue;
		float costValue;
		float handWorkCostValue;
		float purchaseValue;
		float inProductionValue;
		float inPlannedProductionValue;
		float saleValue;
		float usedInProductionValue;
		float transferValue;
		float usedInPlannedProductionValue;

		
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
				stock.setText(String.format("%,6.2f un.",stockValue));

				TextView orderPoint = (TextView)getActivity().findViewById(R.id.general_info_pp);
				orderPointValue = Float.parseFloat(product.orderPoint);
				orderPoint.setText(String.format("%,6.2f un.",orderPointValue));

				TextView cost = (TextView)getActivity().findViewById(R.id.general_info_cost);
				costValue = Float.parseFloat(product.cost);
				cost.setText(String.format("%,6.2f un.",costValue));

				TextView handWorkCost = (TextView)getActivity().findViewById(R.id.general_info_handworkcost);
				handWorkCostValue = Float.parseFloat(product.handWorkCost);
				handWorkCost.setText(String.format("%,6.2f un.",handWorkCostValue));

				TextView purchase = (TextView)getActivity().findViewById(R.id.general_info_purchase);
				purchaseValue = Float.parseFloat(product.purchase);
				purchase.setText(String.format("%,6.2f un.",purchaseValue));

				TextView inProduction = (TextView)getActivity().findViewById(R.id.general_info_in_production);
				inProductionValue = Float.parseFloat(product.inProduction);
				inProduction.setText(String.format("%,6.2f un.",inProductionValue));

				TextView inPlannedProduction = (TextView)getActivity().findViewById(R.id.general_info_in_planned_produccions);
				inPlannedProductionValue = Float.parseFloat(product.inPlannedProduction);
				inPlannedProduction.setText(String.format("%,6.2f un.",inPlannedProductionValue));

				TextView sale = (TextView)getActivity().findViewById(R.id.general_info_sales);
				saleValue = Float.parseFloat(product.sale);
				sale.setText(String.format("%,6.2f un.",saleValue));

				TextView usedInProduction = (TextView)getActivity().findViewById(R.id.general_info_used_in_production);
				usedInProductionValue = Float.parseFloat(product.usedInProduction);
				usedInProduction.setText(String.format("%,6.2f un.",usedInProductionValue));

				TextView transfer = (TextView)getActivity().findViewById(R.id.general_info_transfer);
				transferValue = Float.parseFloat(product.transfer);
				transfer.setText(String.format("%,6.2f un.",transferValue));

				TextView usedInPlannedProduction = (TextView)getActivity().findViewById(R.id.general_info_used_in_planned_produccions);
				usedInPlannedProductionValue = Float.parseFloat(product.usedInPlannedProduction);
				usedInPlannedProduction.setText(String.format("%,6.2f un.",usedInPlannedProductionValue));

			}
		

		}
	}
}
