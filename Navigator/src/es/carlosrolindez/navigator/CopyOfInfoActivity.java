package es.carlosrolindez.navigator;

import android.support.v4.app.FragmentActivity;

public class CopyOfInfoActivity extends FragmentActivity 
{
/*    private InfoFragmentAdapter mAdapter;
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
//		    	inUseFragment = (InUseFragment)getSupportFragmentManager().getFragment(savedInstanceState, "inUseFragment");
//		    	infoFragment = (InfoFragment)getSupportFragmentManager().getFragment(savedInstanceState, "infoFragment");
//		    	bomFragment = (BOMFragment)getSupportFragmentManager().getFragment(savedInstanceState, "bomFragment");
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
		    case NavisionTool.SEARCH_MODE_BOM:
//		    	bomFragment = (BOMFragment)getSupportFragmentManager().getFragment(savedInstanceState, "bomFragment");
		        setContentView(R.layout.single_frame_layout);	 
		        
//		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bomFragment).commit(); 
		      	getActionBar().setTitle("BOM " + reference);    
		        break;
		        
		    case NavisionTool.INFO_MODE_IN_USE:
		    case NavisionTool.SEARCH_MODE_IN_USE:
//		    	inUseFragment = (InUseFragment)getSupportFragmentManager().getFragment(savedInstanceState, "inUseFragment");
		        setContentView(R.layout.single_frame_layout);	 
		    	
//		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, inUseFragment).commit();    
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
		    case NavisionTool.SEARCH_MODE_BOM:
		        setContentView(R.layout.single_frame_layout);	 
		        bomFragment = BOMFragment.newInstance();
		    	
		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bomFragment).commit(); 
		      	getActionBar().setTitle("BOM " + reference);   
	    
	       	    if (infoMode==NavisionTool.INFO_MODE_BOM)
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_BOM, searchString, this);	  
	       	    else
	       	    {
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH_BOM, searchString, this);	
	       	    }
	       	    bomFragment.showProgress(true);

		      	
		        break;
		        
		    case NavisionTool.INFO_MODE_IN_USE:
		    case NavisionTool.SEARCH_MODE_IN_USE:
		    	
				setContentView(R.layout.single_frame_layout);	 
		        
		        inUseFragment = InUseFragment.newInstance();
		    	
		        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, inUseFragment).commit();    
		      	getActionBar().setTitle(reference + " used in:");    
    
	       	    if (infoMode==NavisionTool.INFO_MODE_IN_USE)
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_IN_USE, searchString, this);	
	       	    else
	       	    	lm.restartLoader(NavisionTool.LOADER_PRODUCT_SEARCH_IN_USE, searchString, this);		       	    	

	       	    inUseFragment.showProgress(true);
		      	
		        break;
		    }	        
	    
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
				case NavisionTool.LOADER_PRODUCT_SEARCH_BOM:
				    {
				    	bomFragment.showResultSet(productList);
				    }
				    break;
				case NavisionTool.LOADER_PRODUCT_IN_USE:
				case NavisionTool.LOADER_PRODUCT_SEARCH_IN_USE:
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
*/
}
