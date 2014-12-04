package es.carlosrolindez.navigator;



import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;



public class SearchFragment extends Fragment 
{
	private ListView list;
	private CrListAdapter listAdapter;
	private ArrayList<Product> productList;
	
	public static SearchFragment newInstance(ArrayList<Product> productList) 
	{
		SearchFragment  fragment = new SearchFragment();
		fragment.productList = productList;
		return fragment;
	}


	
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//	    	setRetainInstance(true);

    }
	 
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
		ArrayList<Product> localProductList = new ArrayList<Product>();
		
		if (productList!=null)
        {
        	Log.e("SearchFragment onCreateView","productList");
			for (Product item : productList)
			{
				if (item.itemMode == NavisionTool.LOADER_PRODUCT_SEARCH)
					localProductList.add(item);
		   		Log.e("SearchFragment showResultSet",item.description + " " + item.itemMode);
				
			}
        	listAdapter = new CrListAdapter(getActivity(),localProductList);
        }
        else       	
        	listAdapter = new CrListAdapter(getActivity(),null);
        
    	
        return inflater.inflate(R.layout.list_layout, container, false);         
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) 
    {
      	super.onActivityCreated(savedInstanceState);
		
    	list=(ListView)getActivity().findViewById(R.id.list);    		
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(onItemClickListener);  
        

        if (productList!=null) 
        {
       		Log.e("SearchFragment onActivityCreated","productList");
       		showResultSet(productList);
        }
        	
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
    
    
/*    public void onSaveInstanceState(Bundle savedState) 
    {
    	ArrayList<Product> productList;
    	
        super.onSaveInstanceState(savedState);

        productList = listAdapter.getProductList(); 
        savedState.putParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY, productList);

    }  
 */  
	void showResultSet(ArrayList<Product> productListLoaded)
	{
   		Log.e("SearchFragment showResultSet"," ");
		getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
		if (productListLoaded == null) listAdapter.showResultSet(null);
		else
		{
	   		Log.e("SearchFragment showResultSet","productList");
			ArrayList<Product> localProductList = new ArrayList<Product>();
			for (Product item : productListLoaded)
			{
				if (item.itemMode == NavisionTool.LOADER_PRODUCT_SEARCH)
					localProductList.add(item);
		   		Log.e("SearchFragment showResultSet",item.description + " " + item.itemMode);
				
			}
			listAdapter.showResultSet(localProductList);		
		}

	}
}
