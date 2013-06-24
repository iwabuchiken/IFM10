package ifm10.tasks;

import ifm10.main.MainActv;
import ifm10.utils.CONS;
import ifm10.utils.DBUtils;
import ifm10.utils.Methods;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Task_FixTableNameValue extends AsyncTask<String[], Integer, Integer> {

	Activity actv;
	
	public Task_FixTableNameValue(Activity actv) {
		
		this.actv = actv;
		
	}

	@Override
	protected Integer doInBackground(String[]... args) {
		
		List<String> tnames = Methods.get_table_list(actv, "IFM10%");
		
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		int counter = 0;
		
		for (int i = 0; i < tnames.size(); i++) {
			
			String tname = tnames.get(i);

			Cursor c = wdb.query(tname, CONS.cols_full, null, null, null, null, null);
			
			// If the query returns null, then the next table
			if (c == null) {
				
				// Log
				Log.d("["
						+ "Task_FixTableNameValue.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c == null");
				
				continue;
				
			}//if (c == null)
			
//			// Move the cursor to the first
//			c.moveToFirst();
			
			while (c.moveToNext()) {
				// Log
				Log.d("["
						+ "Task_FixTableNameValue.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"Processing... => " + tname
						+ "(" + String.valueOf(c.getLong(0)) + ")");
				
				counter += 1;
				
				String val = c.getString(11);
//				String val = c.getString(8);
				
				// Log
				Log.d("["
						+ "Task_FixTableNameValue.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"table name=" + tname
						+ "/" + "'table_name'=" + val);
				
				
				if (!tname.equals(val)) {
					
					// Log
					Log.d("["
							+ "Task_FixTableNameValue.java : "
							+ +Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "tname != val");
					
					boolean res = DBUtils.updateData_TI_tableName(actv, wdb, tname, c.getLong(0), tname);
					
				} else {//if (tname.equals(val))

					// Log
					Log.d("["
							+ "Task_FixTableNameValue.java : "
							+ +Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "tname == val");

				}//if (tname.equals(val))
				
			}//while (c.moveToNext()) {
			
			// Publish progress
			this.publishProgress(new Integer[]{tnames.size(), counter});
			
		}//for (int i = 0; i < tnames.size(); i++)
		
		wdb.close();
		
		return 1;
	}//protected Integer doInBackground(String[]... args)

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if (result == 1) {
			
			// debug
			Toast.makeText(actv, "Fix table names => Done", Toast.LENGTH_SHORT).show();
			
			// Log
			Log.d("["
					+ "Task_FixTableNameValue.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Fix table names => Done");
			
		} else {//if (result == 1)

			// debug
			Toast.makeText(actv, "Fix table names => Failed", Toast.LENGTH_SHORT).show();
			
			// Log
			Log.d("["
					+ "Task_FixTableNameValue.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Fix table names => Failed");

		}//if (result == 1)
		
	}//protected void onPostExecute(Integer result) {

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		// debug
		Toast.makeText(actv, "Fixing starting ...", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		
		// debug
		Toast.makeText(
					actv,
					"Progress: " + String.valueOf(values[1]) + "/" + String.valueOf(values[0]),
					Toast.LENGTH_SHORT).show();
		
	}

}//public class Task_FixTableNameValue extends AsyncTask<String[], Integer, Integer>