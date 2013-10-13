package ifm10.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import ifm10.items.TI;
import ifm10.main.R;
import ifm10.main.TNActv;
import ifm10.utils.CONS;
import ifm10.utils.MethodsFTP;
import ifm10.utils.Methods_IFM9;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//public class TaskFTP extends AsyncTask<String, Integer, String> {
public class TaskFTP extends AsyncTask<String, Integer, Integer> {

	Activity actv;
	
	TI ti;
	
	boolean delete;
	
	String ftpTag;//=> Use this field for ftp_upload_db_file

	Vibrator vib;
	
	public TaskFTP(Activity actv) {
		
		this.actv = actv;
		
	}
	
	public TaskFTP(Activity actv, TI ti) {
		// TODO Auto-generated constructor stub
		this.actv	= actv;
		this.ti		= ti;
		
	}

	
	/*********************************
	 * @param boolean delete<br>
	 * 			true => Delete the file when uploaded<br>
	 * 			false => Doesn't delete the file when uploaded
	 *********************************/
	public TaskFTP(Activity actv, TI ti, boolean delete) {

		this.actv	= actv;
		this.ti		= ti;
		
		this.delete	= delete;

	}

	@Override
//	protected String doInBackground(String... ftpTags) {
	protected Integer
	doInBackground(String... ftpTags) {
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
		// Log
		Log.d("TaskFTP.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "ftpTag[0]=" + ftpTags[0]);
		
		int res;
		
		if (ftpTags[0].equals(actv.getString(R.string.ftp_lollipop))) {

			res = MethodsFTP.ftp_connect_disconnect(actv, ti);
			
		} else if (ftpTags[0].equals(actv.getString(R.string.ftp_upload_db_file))) {
				
			this.ftpTag = actv.getString(R.string.ftp_upload_db_file);
			
			res = MethodsFTP.uploadDbFile(actv);
				
		} else {//if (ftpTag.equals(actv.getString(R.string.ftp_lollipop)))
			
			res = MethodsFTP.ftp_connect_disconnect(actv);
			
		}//if (ftpTag.equals(actv.getString(R.string.ftp_lollipop)))
		
		if (res > 0) {

			return res;
			
		} else {//if (res == true)
			
			return res;
			
		}//if (res == true)

	}//doInBackground(String... ftpTags)

	@Override
//	protected void onPostExecute(String result) {
	protected void onPostExecute(Integer res) {
		
		super.onPostExecute(res);
		
		// debug
		Toast.makeText(actv,
				"Result(FTP) => " + String.valueOf(res),
				Toast.LENGTH_SHORT).show();
		
		/*********************************
		 * Upload db file?
		 *********************************/
		if (this.ftpTag.equals(actv.getString(R.string.ftp_upload_db_file))) {
			
			_onPostExecute_UploadDbFile(res);
			
			return;
			
		}
		
		
		/*********************************
		 * Calling url: http://benfranklin.chips.jp/IFM10/create_thumbnails.php
		 *********************************/
		boolean result = _onPostExecute__1_create_thumbnails();
		
		/*********************************
		 * Posting data to the Rails site
		 *********************************/
		if (res > 0 && ti != null) {

			// debug
			Toast.makeText(actv,
					"Posting to the Rails site", Toast.LENGTH_SHORT).show();
			
			res = Methods_IFM9.postFileNameToRailsSite(actv, ti);

			/*********************************
			 * Delete files from table: ti.getTable_name
			 *********************************/
			if (delete == true) {
				
				result = Methods_IFM9.delete_TI_with_files(actv, ti);
				
				if (result == true) {
					
					// Log
					Log.d("["
							+ "TaskFTP.java : "
							+ +Thread.currentThread().getStackTrace()[2]
									.getLineNumber()
									+ " : "
									+ Thread.currentThread().getStackTrace()[2]
											.getMethodName() + "]",
							"TI instance => Deleted from the table: "
									+ ti.getTable_name());
					
					// debug
					Toast.makeText(actv,
							"TI instance => Deleted from the table: "
									+ ti.getTable_name(),
							Toast.LENGTH_LONG).show();
					
					/*********************************
					 * Delete files from the list
					 *********************************/
					TNActv.tiList.remove(ti);
					
					if (TNActv.aAdapter != null) {
						
						TNActv.aAdapter.notifyDataSetChanged();
						
					}//if (TNActv.aAdapter == condition)
					
					// debug
					Toast.makeText(actv,
							"Item deleted: " + ti.getFile_name(),
							Toast.LENGTH_LONG).show();
					
					/*********************************
					 * Delete files from table: show_history
					 *********************************/
//					int deletedHistories = 
					result = 
								Methods_IFM9.delete_TI_from_history(actv, ti);
					
				} else {//if (result == true)

					// Log
					Log.d("["
							+ "TaskFTP.java : "
							+ +Thread.currentThread().getStackTrace()[2]
									.getLineNumber()
									+ " : "
									+ Thread.currentThread().getStackTrace()[2]
											.getMethodName() + "]",
							"TI instance => Not deleted from the table: "
									+ ti.getTable_name());
					
					// debug
					Toast.makeText(actv,
							"TI instance => Not deleted from the table: "
									+ ti.getTable_name(),
									Toast.LENGTH_LONG).show();

				}//if (result == true)

			}//if (delete == true) {
//			} else {//if (delete == true) {
//
//				// debug
//				Toast.makeText(actv,
//						"Item deletion from DB => failed: " + ti.getFile_name(),
//						Toast.LENGTH_LONG).show();
//					
//			}//if (res == true)
			
		} else if (res > 0) {//if (res > 0 && ti != null) {
				
			// Log
			Log.d("["
					+ "TaskFTP.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber()
							+ " : "
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]",
									"res > 0");
			
		} else if (ti != null) {//if (res > 0 && ti != null) {

			// Log
			Log.d("["
					+ "TaskFTP.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber()
							+ " : "
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]",
									"res <= 0");

		} else {//if (res > 0 && ti != null) {
			
			// Log
			Log.d("["
					+ "TaskFTP.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber()
							+ " : "
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]",
					"res <= 0 && ti == null");
		
		}//if (res > 0 && ti != null) {
			
	}//protected void onPostExecute(String result)

	private void _onPostExecute_UploadDbFile(Integer res) {
		// TODO Auto-generated method stub
		
		vib.vibrate(CONS.Admin.vibLength_Long);
		
		if (res.intValue() > 0) {
			
			// debug
			Toast.makeText(actv,
					"Upload db => Done", Toast.LENGTH_SHORT).show();
			
		} else {//if (res.intValue() > 0)
			
			// debug
			Toast.makeText(actv,
					"Upload db => Failed", Toast.LENGTH_SHORT).show();
			
		}//if (res.intValue() > 0)
		
		
	}//private void _onPostExecute_UploadDbFile(Integer res) {

	private boolean
	_onPostExecute__1_create_thumbnails() {

		String url = "http://benfranklin.chips.jp/IFM10/create_thumbnails.php";
		
		// Log
		Log.d("[" + "TaskFTP.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " : "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]", "url=" + url);
		
		HttpGet httpGet = new HttpGet(url);
		
		DefaultHttpClient dhc = new DefaultHttpClient();
		
		HttpResponse hr = null;
		
		try {
			
			hr = dhc.execute(httpGet);
			
		} catch (ClientProtocolException e) {
		
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());

		} catch (IOException e) {
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
		}
		
		if (hr == null) {
			
//			// debug
//			Toast.makeText(actv, "hr == null", 2000).show();
			
			// Log
			Log.d("TaskHTTP.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "hr == null");
			
//			return CONS.Task_GetTexts.EXECUTE_POST_NULL;
			return false;
			
		} else {//if (hr == null)
			
			// Log
			Log.d("Task_GetTexts.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "Http response => Obtained");

			
//			return null;
			
		}//if (hr == null)
		
		/*********************************
		 * Status code
		 *********************************/
		int status = hr.getStatusLine().getStatusCode();
		
		if (status == CONS.HTTP_Response.CREATED
				|| status == CONS.HTTP_Response.OK) {

			// Log
			Log.d("Task_GetYomi.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "status=" + status);

//			return CONS.HTTP_Response.CREATED;
			
		} else {//if (status == CONS.HTTP_Response.CREATED)
			
			// Log
			Log.d("Task_GetTexts.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "status=" + status);
			
			return false;
			
		}//if (status == CONS.HTTP_Response.CREATED)

		return true;
		
	}//_onPostExecute__1_create_thumbnails()

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		if (this.ti != null) {
			
			// debug
			Toast.makeText(actv, "Uploading file: " + ti.getFile_name(), Toast.LENGTH_LONG).show();
			
		} else {//if (this.ti != null)

			// debug
			Toast.makeText(actv, "Uploading file", Toast.LENGTH_LONG).show();
			
		}//if (this.ti != null)
		
	}
	
}
