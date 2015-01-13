package es.carlosrolindez.navigator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	    list.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	    	{ 			
	   			FileDescription fileDescription = (FileDescription) parent.getItemAtPosition(position);

    			AsyncFileAccess copyAsyncTask = new AsyncFileAccess();
    			copyAsyncTask.execute(fileDescription.fileName, fileDescription.type, FileTool.getAddress(), FileTool.getDomain(), FileTool.getUsername(), FileTool.getPassword());
	    	}
		});
 
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
	public class AsyncFileAccess extends AsyncTask<String, Integer, File> 
	{
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMessage("Loading");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);   
			dialog.setMax(100);
			dialog.setProgress(100);
			dialog.show();
	    }
		
	    @Override 
	    protected File doInBackground(String... strPCPath /* path, address, domain, username, password */) {
	    	 
		    SmbFile smbFolderToDownload = null;   
		    
		    try 
		    {
		        File localFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/Temporalfolder");

		        // create sdcard path if not exist.
		        if (!localFilePath.isDirectory()) 
		        {
		            localFilePath.mkdir();
		        }
		        try 
		        {         
		        	int index = 0;
		        	for (String typeItem : FileDescription.typeList)
		        	{
		        		if (typeItem.equals(strPCPath[1]))
		        			break;
		        		index++;
		        	}
		        	
					NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(strPCPath[3], strPCPath[4], strPCPath[5]);
		        	
		        	Log.e("path", "smb://" + strPCPath[2] + '/' + FileDescription.pathList[index] + '/');

					String urlPath =  "smb://" + strPCPath[2] + '/' + FileDescription.pathList[index] + '/';		
					
		            smbFolderToDownload = new SmbFile(urlPath , auth);
		            
					for(SmbFile smbFileToDownload : smbFolderToDownload.listFiles()) 
					{
			        	Log.e("filename", smbFileToDownload.getName());
					    if(smbFileToDownload.getName().contains(strPCPath[0]))
					    {
				        	Log.e("copying", smbFileToDownload.getName());
			                InputStream inputStream = smbFileToDownload.getInputStream();

			                File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/Temporalfolder/"+smbFileToDownload.getName());
			                long fileLength = smbFileToDownload.length();
			                
			                OutputStream out = new FileOutputStream(localFile);
			                
			                byte buf[] = new byte[16 * 1024];
			                int len;
			                int cicles = 0;
			                while ((len = inputStream.read(buf)) > 0) 
			                {
			                    out.write(buf, 0, len);
			                    cicles++;
			                    int percentage = (100*cicles)/((int)(fileLength / (16*1024)) + 1);
			                    publishProgress(percentage);
			                }
			                out.flush();
			                out.close();
			                inputStream.close();
			                return localFile;

					    }
					
					}
					return null;

		            /*smbFileToDownload = new SmbFile(url , auth);
		            String smbFileName = smbFileToDownload.getName();
	                InputStream inputStream = smbFileToDownload.getInputStream();

	                File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/Temporalfolder/"+smbFileName);
	                long fileLength = smbFileToDownload.length();
	                
	                OutputStream out = new FileOutputStream(localFile);
	                
	                byte buf[] = new byte[16 * 1024];
	                int len;
	                int cicles = 0;
	                while ((len = inputStream.read(buf)) > 0) 
	                {
	                    out.write(buf, 0, len);
	                    cicles++;
	                    int percentage = (100*cicles)/((int)(fileLength / (16*1024)) + 1);
	                    publishProgress(percentage);
	                }
	                out.flush();
	                out.close();
	                inputStream.close();
	                return localFile;*/
		        }
		        catch (Exception e) 
		        {
		            e.printStackTrace();
			        return null;
		        }
		    } 
		    catch (Exception e) 
		    {
		        e.printStackTrace();
		        return null;
		    }   

	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... values) {
	        int progress = values[0].intValue();
	        dialog.setProgress(progress);
	    }
	    
	   
	    @Override protected void onPostExecute(File file) {
	    	dialog.dismiss();
	    	Uri uri = Uri.fromFile(file);
	        
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// Check what kind of file you are trying to open, by comparing the url with extensions.
			// When the if condition is matched, plugin sets the correct intent (mime) type, 
			// so Android knew what application to use to open the file
			if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
			    // Word document
			    intent.setDataAndType(uri, "application/msword");
			} else if(file.toString().toLowerCase().contains(".pdf")) {
			    // PDF file
			    intent.setDataAndType(uri, "application/pdf");
			} else if(file.toString().toLowerCase().contains(".xls") || file.toString().toLowerCase().contains(".xlsx")) {
			    // Excel file
			    intent.setDataAndType(uri, "application/vnd.ms-excel");
			} else if(file.toString().toLowerCase().contains(".zip") || file.toString().toLowerCase().contains(".rar"))  {
			    // ZIP Files
			    intent.setDataAndType(uri, "application/zip");
			} else if(file.toString().toLowerCase().contains(".rtf")) {
			    // RTF file
			    intent.setDataAndType(uri, "application/rtf");
			} else if(file.toString().toLowerCase().contains(".gif")) {
			    // GIF file
			    intent.setDataAndType(uri, "image/gif");
			} else if(file.toString().toLowerCase().contains(".jpg") || file.toString().toLowerCase().contains(".jpeg") || file.toString().toLowerCase().contains(".png")) {
			    // JPG file
			    intent.setDataAndType(uri, "image/jpeg");
			} else if(file.toString().toLowerCase().contains(".txt")) {
			    // Text file
			    intent.setDataAndType(uri, "text/plain");
			} else {
			    //if you want you can also define the intent type for any other file
			    
			    //additionally use else clause below, to manage other unknown extensions
			    //in this case, Android will show all applications installed on the device
			    //so you can choose which application to use
			    intent.setDataAndType(uri, "*/*");
			}
	        
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        startActivity(intent);
	    }

	    @Override
	    protected void onCancelled() {
	    	 dialog.dismiss();
	    }


	}

}
    
