package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;



public class SearchActivity extends FragmentActivity implements LoaderCallbacks<ArrayList<Product>>
{
	
	private SearchFragment searchFragment;
	private String query;
	private ArrayList<Product> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) { 
    	
    	super.onCreate(savedInstanceState);
	    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.search_layout);
        
    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	String  server = sharedPref.getString("server", "");
    	if (server.matches("Navision"))
    	{
    		NavisionTool.changeMode(NavisionTool.MODE_REAL);
    	}
    	else
    	{
    		NavisionTool.changeMode(NavisionTool.MODE_EMULATOR);  		
    	}
    	
	    Intent intent = getIntent();
	    	    
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
	    {
		    query = intent.getStringExtra(SearchManager.QUERY);	  	    
	   
		    if (savedInstanceState != null) {
	       		Log.e("SearchActivity OnCreate","load instance");
	        	productList = savedInstanceState.getParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY);
	    		for (Product item : productList)
	    	   		Log.e("SearchActivity onCreate",item.description + " " + item.itemMode);
	    		searchFragment = (SearchFragment)getSupportFragmentManager().getFragment(savedInstanceState, "searchFragment");
            	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();
	     
	        }
	        else
	        {	
	       		Log.e("SearchActivity OnCreate","NOT saved instance");
//	        	productList=null;
//	        }
	   		    
 //         if (searchFragment==null) 
 //           {
           		Log.e("SearchActivity OnCreate","new searchFragment");
            	searchFragment = SearchFragment.newInstance(productList);
            	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();

 //           }
  //          if (productList==null)
  //          { 
    	   		Log.e("SearchActivity handleIntent","Launched loader");
        		LoaderManager lm = getSupportLoaderManager();  
        	    Bundle searchString = new Bundle();
        	    searchString.putString(NavisionTool.QUERY, query);  	    
        	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH, searchString, this);	     	
            }

            
  	    }
    }

	@Override
	protected void onNewIntent(Intent intent) { 
   		Log.e("SearchActivity onNewIntent"," ");
	    setIntent(intent);
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
	    {
	   		Log.e("SearchActivity handleIntent","ACTION_SEARCH");
		    query = intent.getStringExtra(SearchManager.QUERY);	  	    		    
 //         if (searchFragment==null) 
            {
            	searchFragment = SearchFragment.newInstance(null);
            	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();    
            }

	   		Log.e("SearchActivity handleIntent","Launched loader");
    		LoaderManager lm = getSupportLoaderManager();  
    	    Bundle searchString = new Bundle();
    	    searchString.putString(NavisionTool.QUERY, query);  	    
    	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH, searchString, this);	     	

  	    }

	}
	
/*	private void handleIntent(Intent intent) 
	{
   		Log.e("SearchActivity handleIntent"," ");
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
	    {
	   		Log.e("SearchActivity handleIntent","ACTION_SEARCH");
	   		Intent myIntent = getIntent();
		    query = myIntent.getStringExtra(SearchManager.QUERY);	  	    		    
 //         if (searchFragment==null) 
            {
            	searchFragment = new SearchFragment();
            	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();    
            }
            if (productList==null)
            { 
    	   		Log.e("SearchActivity handleIntent","Launched loader");
        		LoaderManager lm = getSupportLoaderManager();  
        	    Bundle searchString = new Bundle();
        	    searchString.putString(NavisionTool.QUERY, query);  	    
        	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH, searchString, this);	     	
            }
            else
            {
    	   		Log.e("SearchActivity handleIntent","productList reused");
    	   		
        		findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    	    	searchFragment.showResultSet(productList);	
            }
            
  	    }

	}*/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

	    return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
	    	Intent intent = new Intent (this, SettingsActivity.class);
        	startActivity(intent);               	
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedState) 
    {
    	Log.e("SearchActivity onSaveInstanceState","saving instance list");
    	savedState.putParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY, productList);
		for (Product item : productList)
	   		Log.e("SearchActivity onSaveInstanceState",item.description + " " + item.itemMode);
		
		ArrayList<Product> localProductList = savedState.getParcelableArrayList(NavisionTool.PRODUCT_LIST_KEY);
		for (Product item : localProductList)
	   		Log.e("SearchActivity onSavedInstanceState",item.description + " " + item.itemMode);
		getSupportFragmentManager().putFragment(savedState, "searchFragment", searchFragment);
	   	super.onSaveInstanceState(savedState);
	}   

    
	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle filter)
	{
   		Log.e("SearchActivity onCreateLoader"," ");
		return new ProductListLoader(this,id,filter);
	}
	
	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> loader,ArrayList<Product> productList)
	{

		findViewById(R.id.loadingPanel).setVisibility(View.GONE);
	    if (productList==null)
	    {	
	   		Log.e("SearchActivity onLoadFinished","productList null");
	   		LayoutInflater inflater = getLayoutInflater();
	    	View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

	    	TextView text = (TextView) layout.findViewById(R.id.text_layout);
	    	text.setText("SQL server not found");

	    	Toast toast = new Toast(getApplicationContext());
	    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    	toast.setDuration(Toast.LENGTH_LONG);
	    	toast.setView(layout);
	    	toast.show();			    	
	    }
	    else
	    {
	   		Log.e("SearchActivity onLoadFinished","new productList");
	    	this.productList = productList;
	    }
    	searchFragment.showResultSet(productList);
	}
	
	@Override 
	public void onLoaderReset(Loader<ArrayList<Product>> loader)
	{
   		Log.e("SearchActivity onLoaderReset"," ");
		searchFragment.showResultSet(null);
	}
}
