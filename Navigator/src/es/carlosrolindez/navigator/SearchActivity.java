package es.carlosrolindez.navigator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;


public class SearchActivity extends Activity {
	
	private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.e("Activity OnCreate: ","Before Super");   
    	super.onCreate(savedInstanceState);

	    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.layout_search);
        
    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	String server = sharedPref.getString("server", "");
    	if (server.matches("Navision"))
    	{
    		NavisionTool.changeConn("jdbc:jtds:sqlserver://192.0.0.102:1855/EIS", "sa", "Advising,2007");
    	}
    	else
    	{
    		NavisionTool.changeConn("jdbc:jtds:sqlserver://192.168.1.4:1433/LittleNavision", "sa", "Julia2009") ;   		
    	}
    
	    Intent myIntent = getIntent();
		handleIntent(myIntent);

    }

	@Override
	protected void onNewIntent(Intent intent) {
    	Log.e("Activity OnNewIntent:","First line");     
	    setIntent(intent);

	    handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) 
	{
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
	    {
		    
	    	Log.e("Activity handleIntent:","Intent Action Search"); 

	        FragmentManager fm = getFragmentManager();
	        searchFragment = (SearchFragment) fm.findFragmentById(R.id.fragment_container);

 //           if (searchFragment==null) 
            {
    	    	Log.e("Activity handleIntent:","new searchFragment"); 
            	searchFragment = new SearchFragment();
            	Bundle bundle = intent.getExtras();
            	bundle.putInt("LOADER_MODE", NavisionTool.LOADER_PRODUCT_SEARCH);
            	searchFragment.setArguments(bundle);  
            	// Add the fragment to the 'fragment_container' FrameLayout
            	getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, searchFragment).commit();    
            }
  	    }

	}
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
    


}
