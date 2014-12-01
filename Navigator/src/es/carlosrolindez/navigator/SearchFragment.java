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



public class SearchFragment extends Fragment 
{
	private ListView list;
	private CrListAdapter listAdapter;
	ArrayList<Product> productList;
	
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
	 
	 public ArrayList<Product> getProductList() {
	        return productList;
	 } 
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        listAdapter = new CrListAdapter(getActivity());
    	
        return inflater.inflate(R.layout.list_layout, container, false);         
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) 
    {
      	super.onActivityCreated(savedInstanceState);
		
    	list=(ListView)getActivity().findViewById(R.id.list);    		
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(onItemClickListener);    
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
    
    
    public void onSaveInstanceState(Bundle savedState) 
    {
    	ArrayList<Product> productList;
    	
        super.onSaveInstanceState(savedState);

        productList = listAdapter.getProductList(); 
        savedState.putParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY, productList);

    }  
    
	void showResultSet(ArrayList<Product> productListLoaded)
	{

		if (productListLoaded == null) listAdapter.showResultSet(null);
		else
		{
			ArrayList<Product> localProductList = new ArrayList<Product>();
			for (Product item : productListLoaded)
			{
				if (item.itemMode == NavisionTool.LOADER_PRODUCT_SEARCH)
					localProductList.add(item);
			}
			listAdapter.showResultSet(localProductList);		
		}

	}
}
