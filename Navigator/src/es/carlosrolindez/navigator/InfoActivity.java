package es.carlosrolindez.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.TabPageIndicator;

public class InfoActivity extends FragmentActivity {
    private InfoFragmentAdapter mAdapter;
    private ViewPager mPager;
    private TabPageIndicator mIndicator;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.e("Activity OnCreate: ","Before Super");   
    	super.onCreate(savedInstanceState);
        
        Intent myIntent = getIntent();
	    String reference = myIntent.getStringExtra(NavisionTool.LAUNCH_REFERENCE);	  	    
	    String description = myIntent.getStringExtra(NavisionTool.LAUNCH_DESCRIPTION);	
	    int infoMode = myIntent.getIntExtra(NavisionTool.LAUNCH_INFO_MODE,NavisionTool.INFO_MODE_FULL);
	    
 

	    switch (infoMode)
	    {
	    case NavisionTool.INFO_MODE_FULL:
	    default:
	        setContentView(R.layout.simple_tabs);
	        
	        mAdapter = new InfoFragmentAdapter(getSupportFragmentManager(),reference);
	
	        mPager = (ViewPager)findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);
	
	        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
	        mIndicator.setViewPager(mPager);	
	        mIndicator.setCurrentItem(1);
	        
	      	getActionBar().setTitle(reference + " " + description);    
	        break;
	        
	    case NavisionTool.INFO_MODE_BOM:
	        setContentView(R.layout.single_frame_layout);	 
	        
	        BOMFragment bomFragment = BOMFragment.newInstance(reference);
	    	
	        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, bomFragment).commit(); 
	      	getActionBar().setTitle("BOM " + reference);    
	        break;
	        
	    case NavisionTool.INFO_MODE_IN_USE:
	        setContentView(R.layout.single_frame_layout);	 
	        
	        InUseFragment inUseFragment = InUseFragment.newInstance(reference);
	    	
	        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, inUseFragment).commit();    
	      	getActionBar().setTitle(reference + " used in:");    
	        break;
	        
	    
	    }
	}
}
