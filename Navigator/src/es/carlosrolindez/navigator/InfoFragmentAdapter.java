package es.carlosrolindez.navigator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;






class InfoFragmentAdapter extends FragmentPagerAdapter {

    private int mCount;
    private String query;

    public InfoFragmentAdapter(FragmentManager fm,String filter) {
        super(fm);
        mCount = 2;
        query = filter;
    }

    @Override
    public Fragment getItem(int position) {
    	switch (position) 
    	{
    		case 0: return BOMFragment.newInstance(query,NavisionTool.LOADER_PRODUCT_IN_USE);
//    		case 2: return InfoFragment.newInstance(query,NavisionTool.LOADER_PRODUCT_IN_USE);
    		case 1:
    		default:	return BOMFragment.newInstance(query,NavisionTool.LOADER_PRODUCT_BOM);
    			
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
    		case 0: return "InBOM";
    		case 2: return "BOM";
    		case 1:
    		default:	return "Info";
    			
    	}
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}