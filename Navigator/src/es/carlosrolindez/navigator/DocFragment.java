package es.carlosrolindez.navigator;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DocFragment extends Fragment
{
	private ArrayList<FileDescription> fileDescriptionList;
	private ListView list;
	private DocListAdapter docListAdapter;
	private boolean progressAllowed;
	private boolean progressPending;
	
	public static DocFragment newInstance() 
	{
		DocFragment  fragment = new DocFragment();
		fragment.fileDescriptionList=null;
		fragment.progressAllowed = false;
		fragment.progressPending = false;
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	return  inflater.inflate(R.layout.doc_layout, container, false);    
    }

    @Override    
    public void onActivityCreated(Bundle savedInstanceState) 
    {	
    	super.onActivityCreated(savedInstanceState);

    	if (savedInstanceState != null) 
    		fileDescriptionList = savedInstanceState.getParcelableArrayList(NavisionTool.DOC_LIST_KEY);
   	
 	    list=(ListView)getActivity().findViewById(R.id.doc_list);    

 	    docListAdapter = new DocListAdapter(getActivity(),fileDescriptionList);

	    list.setAdapter(docListAdapter);
 
        progressAllowed = true;
        if (progressPending) showProgress (true);
        
	    if (fileDescriptionList!=null) 
       		showResultSet(fileDescriptionList);
    }
    

	
    @Override
    public void onSaveInstanceState(Bundle savedState) 
	{
	    super.onSaveInstanceState(savedState);
	    savedState.putParcelableArrayList(NavisionTool.DOC_LIST_KEY, fileDescriptionList);
	}   

    public void showProgress(boolean progress)
	{
    	if (progressAllowed)
    	{
        	if (progress)    getActivity().findViewById(R.id.loadingPanel_docList).setVisibility(View.VISIBLE);
        	else getActivity().findViewById(R.id.loadingPanel_docList).setVisibility(View.GONE);
        	progressPending = false;
    	}
    	else progressPending = progress;
	}

	void showResultSet(ArrayList<FileDescription> fileDescriptionListLoaded)
	{
		if (fileDescriptionListLoaded == null) 
			fileDescriptionList = null;
		else
		{
			fileDescriptionList = new ArrayList<FileDescription>();
			for (FileDescription item : fileDescriptionListLoaded)
			{
					fileDescriptionList.add(item);
			}
		}

		if (docListAdapter!=null) 
			docListAdapter.showResultSet(fileDescriptionList);

	}
}
    
