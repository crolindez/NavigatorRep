package es.carlosrolindez.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import com.viewpagerindicator.TabPageIndicator;







public class InfoActivity extends FragmentActivity {
    private InfoFragmentAdapter mAdapter;
    private ViewPager mPager;
    private TabPageIndicator mIndicator;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.e("Activity OnCreate: ","Before Super");   
    	super.onCreate(savedInstanceState);

  //      setContentView(R.layout.simple_circles);
        setContentView(R.layout.simple_tabs);

        
        Intent myIntent = getIntent();
	    String reference = myIntent.getStringExtra(NavisionTool.LAUNCH_REFERENCE);	  

        mAdapter = new InfoFragmentAdapter(getSupportFragmentManager(),reference);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);	
        mIndicator.setCurrentItem(1);
        
      	getActionBar().setTitle(reference);       
        
        
	/*	String reference;
		    
    	Log.e("Activity handleIntent:","Intent Action Search"); 
    	
        FragmentManager fm = getFragmentManager();
//        searchFragment = (SearchFragment) fm.findFragmentById(R.id.fragment_container);


	      
//  	actionBar = getActionBar();
//   	actionBar.setTitle("BOM of " + reference);
             
	    inBOMFragment = new InBOMFragment();
	    bomFragment = new BOMFragment();
	    infoFragment = new InfoFragment();
	    
    	Bundle bundle = intent.getExtras();       
    	bundle.putInt("LOADER_MODE", NavisionTool.LOADER_PRODUCT_BOM);
    	inBOMFragment.setArguments(bundle);  
    	// Add the fragment to the 'fragment_container' FrameLayout
    	getFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, inBOMFragment).commit();    
	*/
	}
	
	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        

	    return super.onCreateOptionsMenu(menu);
    }

    

}
