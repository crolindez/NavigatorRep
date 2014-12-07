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
				//stock.setText(String.format("%,6.2f un.",stockValue));
				stock.setText(product.stock);
			}
		

		}
	}
}
