package ifm10.listeners.dialog;

import ifm10.items.TI;
import ifm10.main.MainActv;
import ifm10.main.R;
import ifm10.tasks.RefreshDBTask;
import ifm10.tasks.TaskFTP;
import ifm10.tasks.Task_FixTableNameValue;
import ifm10.tasks.Task_add_table_name;
import ifm10.utils.CONS;
import ifm10.utils.DBUtils;
import ifm10.utils.Methods;
import ifm10.utils.Methods_dlg;
import ifm10.utils.Tags;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class
DialogOnItemClickListener implements OnItemClickListener {

	//
	Activity actv;
	Dialog dlg1;
	Dialog dlg2;

	TI ti;
	
	//
	Vibrator vib;
	
	//
//	Methods.DialogTags dlgTag = null;

	public DialogOnItemClickListener(Activity actv, Dialog dlg1) {
		// 
		this.actv = actv;
		this.dlg1 = dlg1;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
	}//public DialogOnItemClickListener(Activity actv, Dialog dlg)

	public DialogOnItemClickListener(Activity actv, Dialog dlg1, Dialog dlg2) {
		// 
		this.actv = actv;
		this.dlg1 = dlg1;
		this.dlg2 = dlg2;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
	}//public DialogOnItemClickListener(Activity actv, Dialog dlg)

	public DialogOnItemClickListener(Activity actv, Dialog dlg1, TI ti) {
		// TODO Auto-generated constructor stub
		this.actv = actv;
		this.dlg1 = dlg1;
		this.ti = ti;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);

	}

	//	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 1. Get tag
		 * 2. Vibrate
		 * 3. Switching
			----------------------------*/
		
		Tags.DialogItemTags tag = (Tags.DialogItemTags) parent.getTag();
//		
		vib.vibrate(CONS.vibLength_click);
		
		/*----------------------------
		 * 3. Switching
			----------------------------*/
		switch (tag) {
		
		case dlg_move_files://----------------------------------------------

			String folderPath = (String) parent.getItemAtPosition(position);
			
			Methods_dlg.dlg_confirm_moveFiles(actv, dlg1, folderPath);

//			// debug
//			Toast.makeText(actv, "Move files to: " + folderPath, 2000)
//					.show();
			
			break;// case dlg_move_files

		case dlg_add_memos_gv://----------------------------------------------
			
			String word = (String) parent.getItemAtPosition(position);
			
			Methods.add_pattern_to_text(dlg1, position, word);
			
			break;
			
		case dlg_db_admin_lv://----------------------------------------------
			/*----------------------------
			 * 1. Get chosen item name
			 * 2. Switching
				----------------------------*/
			case_dlg_db_admin_lv(parent, position);

			break;// case dlg_add_memos_gv

		case dlg_admin_patterns_lv://----------------------------------------------
			/*----------------------------
			 * 1. Get chosen item name
			 * 2. Switching
				----------------------------*/
			
			String item = (String) parent.getItemAtPosition(position);
			
//			// debug
//			Toast.makeText(actv, item, 2000).show();
			
			/*----------------------------
			 * 2. Switching
				----------------------------*/
			if (item.equals(actv.getString(R.string.generic_tv_register))) {
				
				Methods_dlg.dlg_register_patterns(actv, dlg1);
				
			} else if (item.equals(actv.getString(R.string.generic_tv_delete))) {

				Methods_dlg.dlg_delete_patterns(actv, dlg1);
				
			} else if (item.equals(actv.getString(R.string.generic_tv_edit))) {
				
			}
			
			break;// case dlg_admin_patterns_lv

		case dlg_delete_patterns_gv://----------------------------------------------
			
			item = (String) parent.getItemAtPosition(position);
			
//			// debug
//			Toast.makeText(actv, item, 2000).show();
			
			Methods_dlg.dlg_confirm_delete_patterns(actv, dlg1, dlg2, item);
			
			break;// case dlg_delete_patterns_gv
			
		case dlg_tn_list://--------------------------------------------
			
			item = (String) parent.getItemAtPosition(position);
			
			case_dlg_tn_list(item);
			
			break;// case dlg_tn_list
			
			
		default:
			break;
		}//switch (tag)
		
	}//public void onItemClick(AdapterView<?> parent, View v, int position, long id)

	private void
	case_dlg_db_admin_lv(AdapterView<?> parent, int position) {
		// TODO Auto-generated method stub

		String item = (String) parent.getItemAtPosition(position);
		
//		// debug
//		Toast.makeText(actv, item, 2000).show();
		
		/*----------------------------
		 * 2. Switching
			----------------------------*/
		if (item.equals(actv.getString(R.string.dlg_db_admin_item_backup_db))) {
			
			Methods.db_backup(actv, dlg1);
			
		} else if (item.equals(actv.getString(R.string.dlg_db_admin_item_refresh_db))){
			
			RefreshDBTask task_ = new RefreshDBTask(actv, dlg1);
			
			// debug
			Toast.makeText(actv, "Starting a task...", Toast.LENGTH_LONG)
					.show();
			
			task_.execute("Start");

			dlg1.dismiss();
			
		} else if (item.equals(actv.getString(R.string.dlg_db_admin_item_set_new_column))){
			
			//
			dlg_db_admin_item_set_new_column();
			
		} else if (item.equals(actv.getString(R.string.dlg_db_admin_item_restore_db))){
			
			//
			dlg_db_admin_item_restore_db();
			
		} else if (item.equals(actv.getString(
						R.string.dlg_db_admin_item_upload_db))){
			
			//
			dlg_db_admin_item_upload_db();
			
		} else if (item.equals(actv.getString(
				R.string.dlg_db_admin_item_fix_table_names))){
			
			//
			dlg_db_admin_item_fix_table_names();
			
		}
		
	}//private void case_dlg_db_admin_lv(AdapterView<?> parent, int position)

	private void dlg_db_admin_item_fix_table_names() {
		/*********************************
		 * Get a list of table names
		 *********************************/
		Task_FixTableNameValue task = new Task_FixTableNameValue(actv);
		
		task.execute(new String[]{"Start"});
		
		dlg1.dismiss();
		
	}//private void dlg_db_admin_item_fix_table_names()

	private void dlg_db_admin_item_upload_db() {

		TaskFTP task = new TaskFTP(actv);
		
		task.execute(actv.getString(R.string.ftp_upload_db_file));
		
		dlg1.dismiss();
		
//		// debug
//		Toast.makeText(actv, "dlg_db_admin_item_upload_db", Toast.LENGTH_LONG).show();
		
	}//private void dlg_db_admin_item_upload_db()

	private void case_dlg_tn_list(String item) {
		// TODO Auto-generated method stub
		if (item.equals(actv.getString(R.string.generic_tv_delete))) {
			
			Methods_dlg.dlg_confirm_DeleteTI(actv, dlg1, ti);
			
		} else if (item.equals(actv.getString(R.string.generic_tv_edit))) {//if (item.equals(actv.getString(R.string.generic_tv_delete))))
			
			Methods_dlg.dlg_editTI(actv, dlg1, ti);
			
		} else if (item.equals(actv.getString(
						R.string.generic_tv_upload))) {//if (item.equals(actv.getString(R.string.generic_tv_delete))))
			
//			Methods_dlg.uploadImageFile_main(actv, dlg1, ti);
			Methods_dlg.dlg_confirm_uploadImageFile(actv, dlg1, ti);
			
		}//if (item.equals(actv.getString(R.string.generic_tv_delete))))
		
	}

	private void dlg_db_admin_item_restore_db() {
		
		Methods.restore_db(actv);
		
//		// B28 v-1.2
//		String db_file_name = "ifm9_backup_20121226_125955.bk";
//		
//		Methods.restore_db(actv, db_file_name);
//		
//		dlg.dismiss();
	}

	private void dlg_db_admin_item_set_new_column() {
		// Dismiss dialog
		dlg1.dismiss();
		
		Task_add_table_name task = new Task_add_table_name(actv);
		
		task.execute("message");
		
//		// Strings
//		String t_name = "IFM9__TEST";
//		String col_name = "table_name";
//		String data_type = "String";
		
//////////////////////////////////////////////////
//		boolean res = 
//				Methods.update_table_add_new_column(
//						actv, 
//						MainActv.dbName,
//						t_name,
//						col_name,
//						data_type);
//		
//		// debug
//		Toast.makeText(actv, "Add new column => " + res, Toast.LENGTH_SHORT).show();
//		
//////////////////////////////////////////////////
		
//////////////////////////////////////////////////
//		// Get table name list
//		List<String> t_names = Methods.get_table_list(actv, "ifm");
//		
//		// Add a new column
//		boolean res = false;
//		String col_name = "table_name";
//		String data_type = "String";
//		
//		for (String name : t_names) {
//			
//			res = Methods.update_table_add_new_column(
//					actv, 
//					MainActv.dbName,
//					name,
//					col_name,
//					data_type);
//
//			// Log
//			Log.d("DialogOnItemClickListener.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "t_name=" + name + "(res=" + res + ")");
//
//		}
//		
//		// debug
////		Toast.makeText(actv, "Add new column => " + res, Toast.LENGTH_SHORT).show();
		
//////////////////////////////////////////////////
		
		//
		
//////////////////////////////////////////////////
//		// Get table name
////		String tableName = Methods.convert_path_into_table_name(actv);
//		
//		// Get column names
////		String[] col_names = Methods.get_column_list(actv, MainActv.dbName, tableName);
//		String[] col_names = Methods.get_column_list(actv, MainActv.dbName, t_name);
//		
//		// Show each name
//		for (String name : col_names) {
//			
//			// Log
//			Log.d("DialogOnItemClickListener.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col_name=" + name);
//			
//		}
//////////////////////////////////////////////////

	}//private void dlg_db_admin_item_set_new_column()

}//DialogOnItemClickListener implements OnItemClickListener
