package es.carlosrolindez.navigator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;






class InfoFragmentAdapter extends FragmentPagerAdapter {

    private int mCount;
    InUseFragment inUseFragment;
    BOMFragment bomFragment;
    InfoFragment infoFragment;

    public InfoFragmentAdapter(FragmentManager fm,InUseFragment inUseFragment, InfoFragment infoFragment, BOMFragment bomFragment) 
    {
        super(fm);
        mCount = 3;
        this.inUseFragment = inUseFragment;
        this.infoFragment = infoFragment;
        this.bomFragment = bomFragment;
    }

    @Override
    public Fragment getItem(int position) {
    	Log.e("getItem","page:" + position); 
    	switch (position) 
    	{
    		case 0: 	Log.e("Loader In use","started"); return inUseFragment;
    		case 1:		Log.e("Loader Info","started"); return infoFragment;
    		case 2: 	
    		default:	Log.e("Loader BOM","started"); return bomFragment;
    			
    	}
        
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      	switch (position) 
    	{
    		case 0: 	return "InBOM";
    		case 1: 	return "Info";
    		case 2: 
    		default:	return "BOM";

    			
    	}
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}