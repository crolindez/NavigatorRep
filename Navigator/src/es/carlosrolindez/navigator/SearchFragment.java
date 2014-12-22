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
//	private boolean progressAllowed;
//	private boolean progressPending;
	
	public static SearchFragment newInstance() 
	{
    	Log.e("SearchFragment","newInstance");
		SearchFragment  fragment = new SearchFragment();
//		fragment.progressAllowed = false;
//		fragment.progressPending = false;
		return fragment;
	}

	 @Override
    public void onCreate(Bundle savedInstanceState) {
	    	Log.e("SearchFragment","onCreate");
        super.onCreate(savedInstanceState);
    }
	 	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	Log.e("SearchFragment","onCreateView");
        return inflater.inflate(R.layout.list_layout, container, false);         
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) 
    {
      	super.onActivityCreated(savedInstanceState);

      	if (savedInstanceState != null) 
      	{
        	Log.e("SearchFragment","onActivityCreated saved");
        	productList = savedInstanceState.getParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY);
      	}
      	else
      	{
        	Log.e("SearchFragment","onActivityCreated new");
      		productList=null;
      	}
		
    	list=(ListView)getActivity().findViewById(R.id.list);  
    	listAdapter = new CrListAdapter(getActivity(),productList);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(onItemClickListener);  
	    
 //       progressAllowed = true;
 //       if (progressPending) showProgress (true);
        

        if (productList!=null) 
        {
       		showResultSet(productList);
        	Log.e("SearchFragment","onActivityCreated showResultSet");
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
    
    
    public void onSaveInstanceState(Bundle savedState) 
    {
        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY, productList);

    }  
   
   // public void showProgress(boolean progress)
	//{
   // 	if (progressAllowed)
    //	{
    //    	if (progress)    getActivity().findViewById(R.id.loadingPanel_list).setVisibility(View.VISIBLE);
    //    	else getActivity().findViewById(R.id.loadingPanel_list).setVisibility(View.GONE);
     //   	progressPending = false;
    //	}
   // 	else progressPending = progress;
	//}

	void showResultSet(ArrayList<Product> productListLoaded)
	{
    	Log.e("SearchFragment","showResultSet ");
		if (productListLoaded == null) 
			productList = null;
		else
		{
	   		productList = new ArrayList<Product>();
			for (Product item : productListLoaded)
			{
				if ( 	(item.itemMode == NavisionTool.LOADER_PRODUCT_SEARCH) || 
						(item.itemMode == NavisionTool.LOADER_PRODUCT_SEARCH_BOM) || 
						(item.itemMode == NavisionTool.LOADER_PRODUCT_SEARCH_IN_USE) ) 
					productList.add(item);
			}
		}
		listAdapter.showResultSet(productList);
	}
}
