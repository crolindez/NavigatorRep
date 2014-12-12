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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;



public class SearchActivity extends FragmentActivity implements LoaderCallbacks<ArrayList<Product>>
{
	
	private SearchFragment searchFragment;
	private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
    	
    	super.onCreate(savedInstanceState);
    	  	
    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    	requestWindowFeature(Window.FEATURE_PROGRESS);
 
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
		    if (savedInstanceState != null)
		    {
	    		searchFragment = (SearchFragment)getSupportFragmentManager().getFragment(savedInstanceState, "searchFragment");
	    		if (searchFragment!=null)
	    			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();
	     
	        }
	        else
	        	handleIntent(intent);
  	    }
    }

	@Override
	protected void onNewIntent(Intent intent) 
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
    	
    	setIntent(intent);
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
	    {
        	handleIntent(intent);
  	    }

	}
	
	private void handleIntent(Intent intent) 
	{
    	String query=intent.getStringExtra(SearchManager.QUERY);
   	   
    	setProgressBarIndeterminateVisibility(true);
	    setProgressBarVisibility(true);
    	
      	searchFragment = SearchFragment.newInstance();
    	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment).commit();
		LoaderManager lm = getSupportLoaderManager();  
	    Bundle searchString = new Bundle();
	    searchString.putString(NavisionTool.QUERY, query);  	    
	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH, searchString, this);	     	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
	    return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	searchView.setIconified(true);
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
    		getSupportFragmentManager().putFragment(savedState, "searchFragment", searchFragment);
	   	super.onSaveInstanceState(savedState);
	}   

    
	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle filter)
	{
		return new ProductListLoader(this,id,filter);
	}
	
	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> loader,ArrayList<Product> productList)
	{
        setProgressBarIndeterminateVisibility(false);
        setProgressBarVisibility(false);
        
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
//		searchFragment.showResultSet(null);
	}
}
