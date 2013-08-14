package ifm10.tasks;

import ifm10.items.TI;
import ifm10.main.R;
import ifm10.main.TNActv;
import ifm10.utils.CONS;
import ifm10.utils.MethodsFTP;
import ifm10.utils.Methods_IFM9;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//public class TaskFTP extends AsyncTask<String, Integer, String> {
public class TaskFTP extends AsyncTask<String, Integer, Integer> {

	Activity actv;
	
	TI ti;
	
	boolean delete;
	
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
				
				boolean result = Methods_IFM9.delete_TI_with_files(actv, ti);
				
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
