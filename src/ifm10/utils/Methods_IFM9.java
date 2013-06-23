package ifm10.utils;

import java.io.File;

import ifm10.items.TI;
import ifm10.main.MainActv;
import ifm10.main.R;
import ifm10.tasks.TaskHTTP;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class Methods_IFM9 {

	public static boolean
	delete_TI(Activity actv, TI ti) {
		// TODO Auto-generated method stub
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
				
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 2. Query
			----------------------------*/
		String tableName = ti.getTable_name();
		
		if (tableName == null) {
		
			tableName = "IFM9";
			
		}//if (tableName == null)
		
//		String sql = "DELETE FROM " + ti.getTable_name() +
		String sql = "DELETE FROM " + tableName +
							" WHERE " + CONS.cols[0] + " = " + ti.getFileId();
		
		try {
			wdb.execSQL(sql);

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TI item deleted from db: " + ti.getFile_name());
		
			/*----------------------------
			 * 3. Dismiss dialogues
				----------------------------*/
			wdb.close();
			
			return true;

		} catch (SQLException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TI item deletion => Failed:  " + ti.getFile_name());
			
			wdb.close();
			
			return false;
			
		}
		
	}//delete_TI(Activity actv, Dialog dlg1, Dialog dlg2, TI ti)

	
	public static int
	postFileNameToLollipopSite(Activity actv, TI ti) {
		
		TaskHTTP task = new TaskHTTP(actv, ti);
		
		task.execute(actv.getString(R.string.http_post_file_name_lollipop));

		
		return 1;
	}//postFileNameToLollipopSite(Activity actv, TI ti)
	


	public static Integer postFileNameToRailsSite(Activity actv, TI ti) {
		
		TaskHTTP task = new TaskHTTP(actv, ti);
		
		task.execute(actv.getString(R.string.http_post_image_data_rails));

		
		return null;
	}

	/*********************************
	 * @return true => Item deleted from DB, also removed from memory<br>
	 * 			false => Item either not deleted from DB, or not removed from memory
	 *********************************/
	public static boolean delete_TI_with_files(Activity actv, TI ti) {
		/*********************************
		 * Steps
		 * 
		 * Delete from db
		 * Delete from memory
		 *********************************/
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 2. Query
			----------------------------*/
		String tableName = ti.getTable_name();
		
		if (tableName == null) {
		
			tableName = "IFM9";
			
		}//if (tableName == null)
		
//		String sql = "DELETE FROM " + ti.getTable_name() +
		String sql = "DELETE FROM " + tableName +
							" WHERE " + CONS.cols[0] + " = " + ti.getFileId();
		
		try {
			wdb.execSQL(sql);

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TI item deleted from db: " + ti.getFile_name());
		
			/*----------------------------
			 * 3. Dismiss dialogues
				----------------------------*/
			wdb.close();
			
			

		} catch (SQLException e) {
			
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "TI item deletion => Failed:  " + ti.getFile_name());
			
			wdb.close();
			
			return false;
			
		}//try
		
		/*********************************
		 * Delete from memory
		 *********************************/
//		File f = new File(ti.getFile_path(), ti.getFile_name());
		File f = new File(ti.getFile_path());
		
		if (f.exists()) {

			// Log
			Log.d("["
					+ "Methods_IFM9.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "File exists: " + f.getAbsolutePath());
			
			// REF http://stackoverflow.com/questions/1248292/how-to-delete-a-file-from-sd-card Niko Gamulin
			boolean res = f.delete();
			
			if (res == true) {
				
				// debug
				Toast.makeText(actv, "File deleted: " + f.getName(), Toast.LENGTH_SHORT).show();
				
				// Log
				Log.d("["
						+ "Methods_IFM9.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "File deleted: " + f.getName());
				
				return true;
				
			} else {//if (res == true)
			
				// debug
				Toast.makeText(actv, "File deletion => Failed : " + f.getName(), Toast.LENGTH_SHORT).show();
				
				// Log
				Log.d("["
						+ "Methods_IFM9.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"File deletion => Failed : " + f.getName());
				
				return false;


			}//if (res == true)
			
		} else {//if (f.exists())
			
			// Log
			Log.d("["
					+ "Methods_IFM9.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "File => Doesn't exist: " + f.getAbsolutePath());
			
			// debug
			Toast.makeText(actv, "File => Doesn't exist: " + f.getName(), Toast.LENGTH_SHORT).show();
			
			return false;
			
		}//if (f.exists())
		
	}//public static boolean delete_TI_with_files(Activity actv, TI ti)

}//public class Methods_IFM9
