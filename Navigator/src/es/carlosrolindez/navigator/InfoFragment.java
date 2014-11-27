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
import android.widget.TextView;
import android.widget.Toast;








public class InfoFragment extends Fragment implements LoaderCallbacks<ArrayList<Product>> 
{

	private String query;

	public static InfoFragment newInstance(String filter) {
		InfoFragment  fragment = new InfoFragment();
		fragment.query = filter;
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
    	
   		LoaderManager lm = getLoaderManager();  
   	    Bundle searchString = new Bundle();
   	    searchString.putString(NavisionTool.QUERY, query);  	    
   	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_BOM, searchString, this);	     	
    }    	
    
	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle filter)
	{
		return new ProductListLoader(getActivity(),id,filter);
	}
	
	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> loader,ArrayList<Product> productList)
	{   			

		
    	Log.e("Fragment OnLoadFinished:", "PRODUCT_GENERAL_INFO");  
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
	    	showProductGeneralInfo(productList);
	    }
	}
	
	@Override 
	public void onLoaderReset(Loader<ArrayList<Product>> loader)
	{
		showProductGeneralInfo(null);
	}
	
	private void showProductGeneralInfo(ArrayList<Product> productList)
	{
		
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
