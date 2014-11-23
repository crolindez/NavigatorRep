package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;








public class BOMFragment extends Fragment implements LoaderCallbacks<ArrayList<Product>> 
{
//	private ArrayList<Product> productList;
	private ListView list;
	private CrListAdapter listAdapter;
	private String query;
	private int loaderMode;
//	private final String PRODUCT_LIST_KEY = "ProductListKey";	
	
	public static BOMFragment newInstance(String filter,int lm) {
		BOMFragment  fragment = new BOMFragment();
		fragment.loaderMode = lm;
		fragment.query = filter;
		Log.e("New Frament:", "loaderMode: " + lm + " query: " + filter);  
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//	    	setRetainInstance(true);
//    	Bundle bundle=getArguments();
//	    query = bundle.getString(SearchManager.QUERY);
//    	Log.e("Fragment OnCreate:", "query: "+query);  
//	    loaderMode = bundle.getInt("LOADER_MODE");
//    	Log.e("Fragment OnCreate:", "loaderMode: "+loaderMode);  
	}
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	Log.e("Fragment OnCreateView:", "Inflater");  

        
 /*   	if (savedInstanceState != null) {
        	productList = savedInstanceState.getParcelableArrayList(PRODUCT_LIST_KEY);
        	Log.e("Fragment OnCreateView:", "Saved Instanced " + query);
        }
        else
        {	
        	productList=null;
        	Log.e("Fragment OnCreateView:", "No Instance " + query);
        }*/
    	
        return inflater.inflate(R.layout.list_layout, container, false);  
       

        
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) {
    	
    	
    	super.onActivityCreated(savedInstanceState);
        
        

        		
    	list=(ListView)getActivity().findViewById(R.id.list);    		
        listAdapter = new CrListAdapter(getActivity()/*,productList*/);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(onItemClickListener);    
    	
//        if (productList==null)
        {
    		Log.e("Fragment OnActivityCreated:","loaderMode: " + loaderMode + " query: " + query);  
    		LoaderManager lm = getLoaderManager();  
    		getActivity().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);  
    	    Bundle searchString = new Bundle();
    	    searchString.putString(NavisionTool.QUERY, query);  	    
    	    lm.restartLoader(loaderMode, searchString, this);	     	
        }
    }
    
/*    public void onSaveInstanceState(Bundle savedState) {
    	ArrayList<Product> productList;
    	
        super.onSaveInstanceState(savedState);
    	Log.e("Fragment OnSaveInstance", "savedState");  
        // Note: getValues() is a method in your ArrayAdaptor subclass
        productList = listAdapter.getProductList(); 
        savedState.putParcelableArrayList(PRODUCT_LIST_KEY, productList);

    }   */ 
    
	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle filter)
	{
		return new ProductListLoader(getActivity(),id,filter);
	}
	
	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> loader,ArrayList<Product> productList)
	{   		
    	Log.e("Fragment OnLoadFinished", "loaderMode: " + loaderMode + " ID: " + loader.getId());  		
    	switch(loader.getId())
		{	
			case NavisionTool.LOADER_PRODUCT_INFO:
		    	Log.e("Fragment OnLoadFinished:", "PRODUCT_INFO");  
				getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
			    if (productList==null)
			    {	
			    	LayoutInflater inflater = getActivity().getLayoutInflater();
			    	View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
	
			    	TextView text = (TextView) layout.findViewById(R.id.text_layout);
			    	text.setText("SQL server not found");
	
			    	Toast toast = new Toast(getActivity().getApplicationContext());
			    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			    	toast.setDuration(Toast.LENGTH_LONG);
			    	toast.setView(layout);
			    	toast.show();			    	
			    }
			    else
			    {
			    	listAdapter.showResultSet(productList);
			    }
				break;
				
			case NavisionTool.LOADER_PRODUCT_BOM:

		    	Log.e("Fragment OnLoadFinished:", "PRODUCT_BOM");  
				getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
			    if (productList==null)
			    {	
			    	LayoutInflater inflater = getActivity().getLayoutInflater();
			    	View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
	
			    	TextView text = (TextView) layout.findViewById(R.id.text_layout);
			    	text.setText("SQL server not found");
	
			    	Toast toast = new Toast(getActivity().getApplicationContext());
			    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			    	toast.setDuration(Toast.LENGTH_LONG);
			    	toast.setView(layout);
			    	toast.show();			    	
			    }
			    else
			    {
			    	listAdapter.showResultSet(productList);
			    }
				break;
			case NavisionTool.LOADER_PRODUCT_IN_USE:
		    	Log.e("Fragment OnLoadFinished:", "PRODUCT_IN_USE");  
				getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
			    if (productList==null)
			    {	
			    	LayoutInflater inflater = getActivity().getLayoutInflater();
			    	View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
	
			    	TextView text = (TextView) layout.findViewById(R.id.text_layout);
			    	text.setText("SQL server not found");
	
			    	Toast toast = new Toast(getActivity().getApplicationContext());
			    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			    	toast.setDuration(Toast.LENGTH_LONG);
			    	toast.setView(layout);
			    	toast.show();			    	
			    }
			    else
			    {
			    	listAdapter.showResultSet(productList);
			    }
				break;
		
		}
	}
	
	@Override 
	public void onLoaderReset(Loader<ArrayList<Product>> loader)
	{
		listAdapter.showResultSet(null);
	}
	
	OnItemClickListener onItemClickListener = new OnItemClickListener() 
	{
		@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
    	{ 			
	    /*	Intent intent = new Intent (view.getContext(), ProductListBomActivity.class);
	        ProductList productList = (ProductList)parent.getItemAtPosition(position);
        	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, productList.reference);        	
        	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, productList.description);
        	startActivity(intent);*/
        	
        	Toast.makeText(view.getContext(), "List Item", Toast.LENGTH_SHORT).show();
    	}
	};

}
