package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class InUseFragment extends Fragment 
{
	private ArrayList<Product> productList;
	private ListView list;
	private CrListAdapter listAdapter;
	
	public static InUseFragment newInstance() 
	{
		InUseFragment  fragment = new InUseFragment();
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {  
    	return  inflater.inflate(R.layout.in_use_layout, container, false);    
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) {	
    	super.onActivityCreated(savedInstanceState);
    	
    	if (savedInstanceState != null) 
        	productList = savedInstanceState.getParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY);
        else
        	productList=null;

  	    list=(ListView)getActivity().findViewById(R.id.in_use_list);    	
	    listAdapter = new CrListAdapter(getActivity(),productList);
	    list.setAdapter(listAdapter);
	    list.setOnItemClickListener(onItemClickListener);    
 
	    if (productList==null)
       		showResultSet(productList);
   
    }
    
	OnItemClickListener onItemClickListener = new OnItemClickListener() 
	{
		@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
    	{ 			
	    	Intent intent = new Intent (view.getContext(), InfoActivity.class);
	        Product product = (Product)parent.getItemAtPosition(position);
        	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, product.reference);        	
        	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, product.description);      
        	intent.putExtra(NavisionTool.LAUNCH_INFO_MODE, NavisionTool.INFO_MODE_FULL);
        	startActivity(intent);
        	
//        	Toast.makeText(view.getContext(), "List Item", Toast.LENGTH_SHORT).show();
    	}
	};

    @Override
	public void onSaveInstanceState(Bundle savedState) 
	{
	    super.onSaveInstanceState(savedState);
	    savedState.putParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY, productList);
	}    

	void showResultSet(ArrayList<Product> productListLoaded)
	{
		if (productListLoaded == null) 
			productList = null;
		else
		{
	   		productList = new ArrayList<Product>();
			for (Product item : productListLoaded)
			{
				if (item.itemMode == NavisionTool.LOADER_PRODUCT_IN_USE)
					productList.add(item);
			}
		}
		listAdapter.showResultSet(productList);
	}
}
