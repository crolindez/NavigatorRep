package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends FragmentActivity implements LoaderCallbacks<ArrayList<Product>>
{
    private InfoFragment infoFragment;
    
    private String reference;    
    private String description;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	Intent myIntent = getIntent();
    	
	    reference = myIntent.getStringExtra(NavisionTool.LAUNCH_REFERENCE);	  	    
	    description = myIntent.getStringExtra(NavisionTool.LAUNCH_DESCRIPTION);	
	    int infoMode = myIntent.getIntExtra(NavisionTool.LAUNCH_INFO_MODE,NavisionTool.INFO_MODE_FULL);
	    
	    if (savedInstanceState == null)
	    {   
	      	LoaderManager lm = getSupportLoaderManager();  
	      	Bundle searchString = new Bundle();
       	    searchString.putString(NavisionTool.QUERY, reference);  	  

       	    infoFragment = InfoFragment.newInstance();  		
	        if (infoFragment!=null)
    			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, infoFragment).commit();
	        	        
	      	getActionBar().setTitle(reference + " " + description);    

       	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_INFO, searchString, this);	     

       	    infoFragment.showProgress(true);
   
	    }
	}
    
    @Override
    public void onSaveInstanceState(Bundle savedState) 
    {	
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
//        setProgressBarIndeterminateVisibility(false);
//        setProgressBarVisibility(false);
   	    if (infoFragment!=null) infoFragment.showProgress(false);
   	    
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
	    else
	    {
	    	infoFragment.showResultSet(productList);
	    }
	}
	
	@Override 
	public void onLoaderReset(Loader<ArrayList<Product>> loader)
	{
  /*  	if (infoFragment!=null)
    	   	infoFragment.showResultSet(null);;*/
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//	    	    searchView.clearFocus();
 	    	Intent intent = new Intent (this, SettingsActivity.class);
        	startActivity(intent);    
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
