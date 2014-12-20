package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

public class InfoActivity extends FragmentActivity implements LoaderCallbacks<ArrayList<Product>>
{
    private InfoFragmentAdapter mAdapter;
    private ViewPager mPager;
    private TabPageIndicator mIndicator;

    InUseFragment inUseFragment;
    InfoFragment infoFragment;
    BOMFragment bomFragment;
    
    private String reference;    
    private String description;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

//    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//    	requestWindowFeature(Window.FEATURE_PROGRESS);

    	Intent myIntent = getIntent();
    	
	    reference = myIntent.getStringExtra(NavisionTool.LAUNCH_REFERENCE);	  	    
	    description = myIntent.getStringExtra(NavisionTool.LAUNCH_DESCRIPTION);	
	    int infoMode = myIntent.getIntExtra(NavisionTool.LAUNCH_INFO_MODE,NavisionTool.INFO_MODE_FULL);
	    
	    if (savedInstanceState != null)
	    {   
		    switch (infoMode)
		    {
		    case NavisionTool.INFO_MODE_FULL:
		    default:  
		    	inUseFragment = (InUseFragment)getSupportFragmentManager().getFragment(savedInstanceState, "inUseFragment");
		    	infoFragment = (InfoFragment)getSupportFragmentManager().getFragment(savedInstanceState, "infoFragment");
		    	bomFragment = (BOMFragment)getSupportFragmentManager().getFragment(savedInstanceState, "bomFragment");
		        setContentView(R.layout.simple_tabs);
		        		        
		        mAdapter = new InfoFragmentAdapter(getSupportFragmentManager(),inUseFragment,infoFragment,bomFragment);
		
		        mPager = (ViewPager)findViewById(R.id.pager);
		        mPager.setAdapter(mAdapter);
		
		        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
		        mIndicator.setViewPager(mPager);	
		        mIndicator.setCurrentItem(1);
		        
		      	getActionBar().setTitle(reference + " " + description);    
		        break;
		        
		    case NavisionTool.INFO_MODE_BOM:
		    case NavisionTool.INFO_MODE_BOM_QUICK:
		    	bomFragment = (BOMFragment)getSupportFragmentManager().getFragment(savedInstanceState, "bomFragment");
		        setContentView(R.layout.single_frame_layout);	 
		        
		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bomFragment).commit(); 
		      	getActionBar().setTitle("BOM " + reference);    
		        break;
		        
		    case NavisionTool.INFO_MODE_IN_USE:
		    case NavisionTool.INFO_MODE_IN_USE_QUICK:
		    	inUseFragment = (InUseFragment)getSupportFragmentManager().getFragment(savedInstanceState, "inUseFragment");
		        setContentView(R.layout.single_frame_layout);	 
		    	
		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, inUseFragment).commit();    
		      	getActionBar().setTitle(reference + " used in:");    
		        break;
		    }
	    }
	    else
	    {
//	    	setProgressBarIndeterminateVisibility(true);
//		    setProgressBarVisibility(true);    
       	    
	      	LoaderManager lm = getSupportLoaderManager();  
	      	Bundle searchString = new Bundle();
       	    searchString.putString(NavisionTool.QUERY, reference);  	  
		    
		    switch (infoMode)
		    {
		    case NavisionTool.INFO_MODE_FULL:
		    default:
		        setContentView(R.layout.simple_tabs);
		        
		        bomFragment = BOMFragment.newInstance();
		        infoFragment = InfoFragment.newInstance();
		        inUseFragment = InUseFragment.newInstance();
		        
		        mAdapter = new InfoFragmentAdapter(getSupportFragmentManager(),inUseFragment,infoFragment,bomFragment);
		
		        mPager = (ViewPager)findViewById(R.id.pager);
		        mPager.setAdapter(mAdapter);
		
		        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
		        mIndicator.setViewPager(mPager);	
		        mIndicator.setCurrentItem(1);
		        
		      	getActionBar().setTitle(reference + " " + description);    

	       	    lm.restartLoader(NavisionTool.LOADER_PRODUCT_INFO, searchString, this);	     

	       	    bomFragment.showProgress(true);
	       	    infoFragment.showProgress(true);
	       	    inUseFragment.showProgress(true);

		        break;
		        
		    case NavisionTool.INFO_MODE_BOM:
		    case NavisionTool.INFO_MODE_BOM_QUICK:
		        setContentView(R.layout.single_frame_layout);	 
		        bomFragment = BOMFragment.newInstance();
		    	
		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bomFragment).commit(); 
		      	getActionBar().setTitle("BOM " + reference);   
	    
	       	    if (infoMode==NavisionTool.INFO_MODE_BOM)
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_BOM, searchString, this);	  
	       	    else
	       	    {
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_BOM_QUICK, searchString, this);	
	       	    }
	       	    bomFragment.showProgress(true);

		      	
		        break;
		        
		    case NavisionTool.INFO_MODE_IN_USE:
		    case NavisionTool.INFO_MODE_IN_USE_QUICK:
		    	
				setContentView(R.layout.single_frame_layout);	 
		        
		        inUseFragment = InUseFragment.newInstance();
		    	
		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, inUseFragment).commit();    
		      	getActionBar().setTitle(reference + " used in:");    
    
	       	    if (infoMode==NavisionTool.INFO_MODE_IN_USE)
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_IN_USE, searchString, this);	
	       	    else
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_IN_USE_QUICK, searchString, this);		       	    	

	       	    inUseFragment.showProgress(true);
		      	
		        break;
		    }	        
	    
	    }
	}
    
    @Override
    public void onSaveInstanceState(Bundle savedState) 
    {
    	if (inUseFragment!=null)
    		getSupportFragmentManager().putFragment(savedState, "inUseFragment", inUseFragment);
    	if (infoFragment!=null)
    		getSupportFragmentManager().putFragment(savedState, "infoFragment", infoFragment);
    	if (bomFragment!=null)
    		getSupportFragmentManager().putFragment(savedState, "bomFragment", bomFragment);
    	
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
		if (bomFragment!=null) bomFragment.showProgress(false);
   	    if (infoFragment!=null) infoFragment.showProgress(false);
   	    if (inUseFragment!=null) inUseFragment.showProgress(false);
   	    
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
	        switch(loader.getId())
			{	
				case NavisionTool.LOADER_PRODUCT_INFO:
				    {
				    	inUseFragment.showResultSet(productList);			    	
				    	infoFragment.showResultSet(productList);
				    	bomFragment.showResultSet(productList);
				    }
					break;
					
				case NavisionTool.LOADER_PRODUCT_BOM:				
				case NavisionTool.LOADER_PRODUCT_BOM_QUICK:
				    {
				    	bomFragment.showResultSet(productList);
				    }
				    break;
				case NavisionTool.LOADER_PRODUCT_IN_USE:
				case NavisionTool.LOADER_PRODUCT_IN_USE_QUICK:
					{
				    	inUseFragment.showResultSet(productList);			
				    }
					break;
			}
	    }
	}
	
	@Override 
	public void onLoaderReset(Loader<ArrayList<Product>> loader)
	{
  /*  	if (inUseFragment!=null)
    		inUseFragment.showResultSet(null);			    	
    	if (infoFragment!=null)
    	   	infoFragment.showResultSet(null);
    	if (inUseFragment!=null)
        	bomFragment.showResultSet(null);*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.info_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_in_use:
	    	intent = new Intent (this, InfoActivity.class);
        	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, reference);        	
        	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, description);  
    		intent.putExtra(NavisionTool.LAUNCH_INFO_MODE, NavisionTool.INFO_MODE_IN_USE);	    			
        	startActivity(intent);	     

			return true;
		case R.id.action_bom:
	    	intent = new Intent (this, InfoActivity.class);
        	intent.putExtra(NavisionTool.LAUNCH_REFERENCE, reference);        	
        	intent.putExtra(NavisionTool.LAUNCH_DESCRIPTION, description);  
    		intent.putExtra(NavisionTool.LAUNCH_INFO_MODE, NavisionTool.INFO_MODE_BOM);	    			
        	startActivity(intent);	     
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    

	
}
