package es.carlosrolindez.navigator;


import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity 
{
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

            this.overridePendingTransition(R.anim.right_to_left,
                    R.anim.right_to_left);  
	        
	        // Display the fragment as the main content.
	        getFragmentManager().beginTransaction()
	                .add(android.R.id.content, new SettingsFragment())
	                .commit();
	    }
	  @Override
	  public void finish()
	  {
		  super.finish();
          this.overridePendingTransition(R.anim.left_to_right,
                  R.anim.left_to_right);  
	  }
}
