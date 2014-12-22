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
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;



public class SearchActivity extends FragmentActivity implements LoaderCallbacks<ArrayList<Product>>
{
	
	private SearchFragment searchFragment;
//	private SearchView searchView;
//	private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
    	
    	super.onCreate(savedInstanceState);
 
    	Log.e("SearchActivity","onCreate");
    	loadPreferences();
		if (searchFragment!=null)
		{
	    	Log.e("SearchActivity","onCreate already saved");			
		}
	    if (savedInstanceState != null)
	    {
	    	Log.e("SearchActivity","onCreate saved");
    		searchFragment = (SearchFragment)getSupportFragmentManager().getFragment(savedInstanceState, "searchFragment");
    		if (searchFragment!=null)
    			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();
     
        }
        else
        	handleIntent();
    }

	@Override
	protected void onNewIntent(Intent intent) 
	{ 
    	Log.e("SearchActivity","onNewIntent");
    	loadPreferences();
    	setIntent(intent);
        handleIntent();
	}
	
	private void handleIntent() 
	{
    	Intent intent = getIntent(); 	 
    	Log.e("SearchActivity","handleIntent");
    	if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
    	{
        	Log.e("SearchActivity","handleIntent " + "Search");
	    	searchFragment = SearchFragment.newInstance();
	    	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();
	    	
	    	String query=intent.getStringExtra(SearchManager.QUERY);	    	
	      	getActionBar().setTitle(query);   
	      	
			LoaderManager lm = getSupportLoaderManager();  
		    Bundle searchString = new Bundle();
		    searchString.putString(NavisionTool.QUERY, query);  	
        	Log.e("SearchActivity","handleIntent " + "Search " + query);
		    lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH, searchString, this);	
//		    searchFragment.showProgress(true);
    	}
    	else if (intent.getStringExtra(NavisionTool.LAUNCH_REFERENCE)!=null)
    	{
        	Log.e("SearchActivity","handleIntent " + "BOM_IN_USE");
	    	searchFragment = SearchFragment.newInstance();
	        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit(); 
	        
    		String reference = intent.getStringExtra(NavisionTool.LAUNCH_REFERENCE);	  	    
    		String description = intent.getStringExtra(NavisionTool.LAUNCH_DESCRIPTION);	
    		int infoMode = intent.getIntExtra(NavisionTool.LAUNCH_INFO_MODE,NavisionTool.INFO_MODE_SEARCH_BOM);
        	Log.e("SearchActivity","handleIntent " + reference);
        	Log.e("SearchActivity","handleIntent " + description);
        	Log.e("SearchActivity","handleIntent " + infoMode);
        	
        	
	      	LoaderManager lm = getSupportLoaderManager();  
	      	Bundle searchString = new Bundle();
       	    searchString.putString(NavisionTool.QUERY, reference);  	  
 
		    switch (infoMode)
		    {
		    case NavisionTool.INFO_MODE_SEARCH_BOM:
	        	Log.e("SearchActivity","handleIntent " + "LOADER_BOM");
		    	getActionBar().setTitle("BOM " + reference);   
	       	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH_BOM, searchString, this);	
//	       	    searchFragment.showProgress(true);
		        break;
		        
		    case NavisionTool.INFO_MODE_SERACH_IN_USE:
	        	Log.e("SearchActivity","handleIntent " + "LOADER_IN_USE");
		      	getActionBar().setTitle(reference + " used in:");  
	       	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH_IN_USE, searchString, this);	
//	       	    searchFragment.showProgress(true);
		        break;
		    }	        
    	}
    }
	
	private void loadPreferences()
	{
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
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	SearchView searchView;
    	MenuItem searchMenuItem;
    	Log.e("SearchActivity","onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.search, menu);
        
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchMenuItem = menu.findItem(R.id.action_search);
	    searchView = (SearchView) searchMenuItem.getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	    searchView.setIconifiedByDefault(true); 
    
     
	    return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//    	    searchView.clearFocus();
 	    	Intent intent = new Intent (this, SettingsActivity.class);
        	startActivity(intent);    
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
    @Override
    public void onSaveInstanceState(Bundle savedState) 
    {
    	if (searchFragment!=null)
    	{
    		getSupportFragmentManager().putFragment(savedState, "searchFragment", searchFragment);
        	Log.e("SearchActivity","onSaveInstanceState saved");
    	}
	   	super.onSaveInstanceState(savedState);
	}   

    
	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle filter)
	{
    	Log.e("SearchActivity","onCreateLoader");
		return new ProductListLoader(this,id,filter);
	}
	
	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> loader,ArrayList<Product> productList)
	{
    	Log.e("SearchActivity","onLoadFinished");
//	    searchFragment.showProgress(false);
	    if (productList==null)
	    {	
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
    	searchFragment.showResultSet(productList);
	}
	
	@Override 
	public void onLoaderReset(Loader<ArrayList<Product>> loader)
	{

	}
}
