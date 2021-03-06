package ifm10.main;

import ifm10.adapters.MainListAdapter;
import ifm10.listeners.CustomOnItemLongClickListener;
import ifm10.listeners.button.ButtonOnClickListener;
import ifm10.listeners.button.ButtonOnTouchListener;
import ifm10.utils.CONS;
import ifm10.utils.DBUtils;
import ifm10.utils.Methods;
import ifm10.utils.Methods_dlg;
import ifm10.utils.Tags;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;



public class MainActv extends ListActivity {
	
	public static Vibrator vib;

	/*********************************
	 * Intent data labels
	 *********************************/
	public static String intent_label_file_ids = "file_ids";		// Methods.show_history()
	
	public static String intent_label_table_names = "table_names";	// Methods.show_history()

	public static String intent_label_searchedItems_table_names = "string_searchedItems_table_names";
	
	/*********************************
	 * Prefs
	 *********************************/
	private static SharedPreferences prefs;

//	public static String prefs_current_path = "current_path";
	public static String prefs_current_path = "ifm9_master_current_path";
	
	public static String prefName_tnActv = "pref_tn_actv";
	
	public static String prefName_tnActv_current_image_position = "pref_tn_actv_current_image_position";
	
	// MainActv
	// history
	public static String prefName_mainActv = "pref_main_actv";
	
	public static String prefName_mainActv_history_mode = "history_mode";
	
	public static final int HISTORY_MODE_ON = 1;
	
	public static final int HISTORY_MODE_OFF = 0;
	
	/*----------------------------
	 * Paths and names
		----------------------------*/
	public static String dirName_ExternalStorage = "/mnt/sdcard-ext";

//	public static String  dirName_base = "IFM8";
	public static String  dirName_base = "IFM10";
//	public static String  dirName_base = "ifm9";

	public static String dirPath_base = dirName_ExternalStorage + File.separator + dirName_base;

	public static String dirPath_current = null;
	
	/*----------------------------
	 * Others
		----------------------------*/
	// Used => create_list_file()
	public static String listFileName = "list.txt";

	// Used => 
	public static List<String> file_names = null;

//	public static ArrayAdapter<String> adapter = null;
	
	public static MainListAdapter adapter = null;

	public static boolean move_mode = false;
	
	public static String machine_name_tablet = "ASUS Pad TF300T";
//	11-01 15:05:05.230: D/MainActv.java[236](17428): device_name=ASUS Pad TF300T
//
	public static String machine_name_phone = "ISW11M";
//	11-01 15:05:10.994: D/MainActv.java[237](5081): device_name=ISW11M

	/*----------------------------
	 * DB
		----------------------------*/
//	public static String dbName = "IFM8";
	public static String dbName = "ifm9.db";
	
	// Table names
	public static String tableName_refreshLog = "refresh_log";
	public static String tableName_memo_patterns = "memo_patterns";

	// show_history
	public static String tableName_show_history = "show_history";
	
//	public static String[] cols_show_history = {
//		"file_id", "table_name"
//	};
//	
//	public static String[] col_types_show_history = {
//		"INTEGER", "TEXT"
//	};

	public static String tableName_separator = "__";

	// Backup
	public static String dirPath_db = "/data/data/ifm10.main/databases";
	
	public static String fileName_db = "ifm9.db";

	public static String dirPath_db_backup = dirName_ExternalStorage + "/IFM10_backup";
	
	public static String fileName_db_backup_trunk = "ifm10_backup";
	public static String fileName_db_backup_ext = ".bk";

//	public static String tname_main = "IFM9";
	public static String tname_main = "IFM10";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*----------------------------
		 * 1. super
		 * 2. Set content
		 * 2-2. Set title
		 * 3. Initialize => vib
		 * 
		 *  4. Set list
		 *  5. Set listener => Image buttons
		 *  6. Set path label
		 *  
		 *  7. Initialize preferences
		 *  
		 *  8. Refresh DB
			----------------------------*/
		
        super.onCreate(savedInstanceState);
        
    }//public void onCreate(Bundle savedInstanceState)

    private void setup_tablet() {
		/*********************************
		 * 1. Set => External storage
		 * 
		 * 2. Setup => database
		 *********************************/
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starts => setup_tablet");
		
    	String device_name = android.os.Build.MODEL;

    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "device_name=" + device_name);
    	
		if (device_name.equals(machine_name_tablet)) {
			
			dirName_ExternalStorage = "/mnt/sdcard";
			
			dirPath_base = dirName_ExternalStorage + File.separator + dirName_base;
			
			dirPath_db_backup = dirName_ExternalStorage + "/IFM10_backup";
			
		}//if (device_name == condition)
		
		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "dirName_ExternalStorage=" + dirName_ExternalStorage);
		
//    	if (device_name.equals(this.dirName_ExternalStorage)) {
//			
//		}//if (device_name == condition)
		
		/*********************************
		 * 2. Setup => database
		 *********************************/
		File f_db = new File(this.dirPath_db);
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "f_db.getAbsolutePath()=" + f_db.getAbsolutePath());

//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "f_db.exists()=" + f_db.exists());
	
		DBUtils dbu = new DBUtils(this, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		wdb.close();
		
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "f_db.getAbsolutePath()=" + f_db.getAbsolutePath());
//
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "f_db.exists()=" + f_db.exists());
		
	}//private void setup_tablet()

	/*********************************
	 * 
	 * 
	 *********************************/
    private void debug_b22() {
    	
    	setContentView(R.layout.main);
        
        /*----------------------------
		 * 2-2. Set title
			----------------------------*/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
        // REF => http://stackoverflow.com/questions/6579968/how-can-i-get-the-device-name-in-android
        String device_name = android.os.Build.MODEL;
        
        // Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "device_name=" + device_name);
        
	}//private void debug_b22()

	private void setup() {
        setContentView(R.layout.main);
        
        /*----------------------------
		 * 2-2. Set title
			----------------------------*/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        
        /*----------------------------
		 * 4. Set list
			----------------------------*/
        set_initial_dir_list();
        
        /*----------------------------
		 * 5. Set listener => Image buttons
			----------------------------*/
		set_listeners();
		
		/*----------------------------
		 * 6. Set path label
			----------------------------*/
		Methods.updatePathLabel(this);
		
		/*********************************
		 * 7. Initialize preferences
		 *********************************/
		init_prefs();
		
		/*********************************
		 * 8. Refresh DB
		 *********************************/
//		refresh_db();
	}//private void setup()

	private void do_debug() {
		
		/***************************************
		 * Processing
		 ***************************************/
		File dname = new File("/mnt/sdcard-ext/processing");
		
		if (dname.exists()) {
			
			for (String fname : dname.list()) {
				
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber()
						+ ":"
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName() + "]",
					"fname=" + fname);
				
			}
			
		} else {//if (dname.exists())
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", dname.getName() + " => Doesn't exist");
			
		}//if (dname.exists())
		
		/*********************************
		 * 6. Drop table
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//		copy_db_file();
//		test_simple_format();
//		restore_db("ifm9_backup_20121001_140224.bk");
//		check_db();
//		show_column_list("IFM9__Android");
//		10-01 15:05:54.408: D/MainActv.java[260](14946): New col added to: IFM9__Android

    	/*********************************
		 * 6. Drop table
		 *********************************/
//    	Methods.drop_table(this, MainActv.dbName, MainActv.tableName_show_history);
    	
    	/*********************************
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//    	add_new_col_last_viewed_at();
    	
    	
	}//private void do_debug()

	private void add_new_col_last_viewed_at() {
		/*********************************
		 * 1. Get table list
		 * 2. Add new col
		 *********************************/
		List<String> table_list = Methods.get_table_list(this, "IFM9");
		
//		//debug
//		for (String name : table_list) {
//			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "t_name=" + name);
//			
//		}//for (String name : table_list)
		
		/*********************************
		 * 2. Add new col
		 *********************************/
		for (String t_name : table_list) {
			
			boolean res = Methods.add_column_to_table(
									this,
									MainActv.dbName,
									t_name,
									"last_viewed_at",
									"INTEGER");
			if (res == true) {
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "New col added to: " + t_name);
			} else {//if (res == true)
				// Log
				Log.e("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Add new col => Failed: " + t_name);
			}//if (res == true)
			
			
		}//for (String t_name : table_list)
		
		
//		for (String name : table_list) {
//			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "table name=" + name);
//			
//		}
		
//		String table_5 = table_list.get(5);
//		
//		String[] col_names = Methods.get_column_list(this, MainActv.dbName, table_5);
//		
//		for (String name : col_names) {
//		
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col name=" + name);
//		
//		}
//
//		String table_6 = table_list.get(6);
//		
//		String[] col_names_6 = Methods.get_column_list(this, MainActv.dbName, table_6);
//		
//		for (String name_6 : col_names_6) {
//		
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "col name=" + name_6);
//
//		}
		
				
		
	}//private void add_new_col_last_viewed_at()

	private void init_prefs() {
    	/*********************************
		 * 1. history_mode
		 *********************************/
//		int current_history_mode = Methods.get_pref(
//				this, 
//				MainActv.prefName_mainActv, 
//				MainActv.prefName_mainActv_history_mode,
//				-1);
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "onCreate: current_history_mode=" + current_history_mode);
		
		boolean res = Methods.set_pref(
				this, 
				MainActv.prefName_mainActv, 
				MainActv.prefName_mainActv_history_mode,
				MainActv.HISTORY_MODE_OFF);

//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", 
//				"history_mode set => MainActv.HISTORY_MODE_OFF"
//				+ "(" + MainActv.HISTORY_MODE_OFF + ")");
		
	}//private void init_prefs()

	private void show_column_list() {
		/*********************************
		 * memo
		 *********************************/
    	String[] col_names = Methods.get_column_list(this, MainActv.dbName, "IFM9");
    	
    	for (String name : col_names) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col=" + name);
    		
		}//for (String name : col_names)
		
	}

	private void show_column_list(String table_name) {
		/*********************************
		 * memo
		 *********************************/
    	String[] col_names = Methods.get_column_list(this, MainActv.dbName, table_name);
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Table: " + table_name);
		
    	for (String name : col_names) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col=" + name);
    		
		}//for (String name : col_names)
		
	}

	private void check_db() {
    	/*********************************
		 * 1. Clear preferences
		 *********************************/
		prefs = 
				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();

		editor.clear();
		editor.commit();
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Prefs cleared");

		
    	String dst = "/data/data/ifm10.main/databases" + MainActv.dbName;
    	
    	File f = new File(dst);
    	
    	File db_dir = new File("/data/data/ifm10.main/databases");
    	
    	for (String name : db_dir.list()) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file name=" + name);
    		
		}//for (String name : db_dir.list())
    	
//    	boolean res = f.exists();
//    	
//    	// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "res=" + res);
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "f.getAbsolutePath(): " + f.getAbsolutePath());
	}

	private void restore_db() {
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: restore_db()");
    	
		String src = "/mnt/sdcard-ext/IFM9_backup/ifm9_backup_20120929_075009.bk";
		String dst = StringUtils.join(new String[]{"/data/data/ifm10.main/databases", MainActv.dbName}, File.separator);
		
//		String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
		boolean res = Methods.restore_db(this, MainActv.dbName, src, dst);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
	}//private void restore_db()

	private void restore_db(String dbFile_name) {
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: restore_db()");
    	
		String src = "/mnt/sdcard-ext/IFM10_backup/" + dbFile_name;
		String dst = StringUtils.join(new String[]{"/data/data/ifm10.main/databases", MainActv.dbName}, File.separator);
		
//		String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
		boolean res = Methods.restore_db(this, MainActv.dbName, src, dst);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "[restore_db()] res=" + res);
		
	}//private void restore_db()

	private void test_simple_format() {
    	
    	long t = Methods.getMillSeconds_now();
    	
    	String time_label = Methods.get_TimeLabel(t);
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "time_label: " + time_label);
		
    }//private void test_simple_format()
    
	private void set_listeners() {
		/*----------------------------
		 * 1. Get view
		 * 2. Set enables
		 * 
		 * 3. Set listeners to buttons => Click
		 * 4. Set listener => Long click
		 * 
		 * 5. On touch
			----------------------------*/
		
		ImageButton ib_up = (ImageButton) findViewById(R.id.v1_bt_up);
		
		/*----------------------------
		 * 2. Set enables
			----------------------------*/
		String curDirPath = Methods.get_currentPath_from_prefs(this);
		
		if (curDirPath.equals(dirPath_base)) {
			
			ib_up.setEnabled(false);
			
			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
			
		} else {//if (this.currentDirPath == this.baseDirPath)
		
			ib_up.setEnabled(true);
			
			ib_up.setImageResource(R.drawable.ifm8_up);
		
		}//if (this.currentDirPath == this.baseDirPath)
		
		/*----------------------------
		 * 3. Listeners => Click
			----------------------------*/
		ib_up.setTag(Tags.ButtonTags.ib_up);
		
		ib_up.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_up.setOnClickListener(new ButtonOnClickListener(this));
		
		/*********************************
		 * 5. On touch
		 *********************************/
//		ListView lv = this.getListView();
//		
//		a
//		
		
	}//private void set_listeners()

	private boolean set_initial_dir_list() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		set_initial_dir_list_part1();
//		set_initial_dir_list_part2();
		
		return false;
	}//private boolean set_initial_dir_list()
	

	private boolean set_initial_dir_list_part1() {
		/*----------------------------
		 * Steps
		 * 1. Create root dir
		 * 1-2. Create "list.txt"
		 * 2. Set variables => currentDirPath, baseDirPath
		 * 3. Get file list
		 * 3-1. Sort file list
		 * 4. Set list to adapter
		 * 5. Set adapter to list view
		 * 
		 * 6. Set listener to list
		 * 
		 * 9. Return
			----------------------------*/
		/*----------------------------
		 * 1. Create root dir
			----------------------------*/
		File file = create_root_dir();
		
		if (file == null) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file == null");
			
			return false;
		}//if (file == null)
		
		/*----------------------------
		 * 1-2. Create "list.txt"
			----------------------------*/
		boolean res = create_list_file(file);
		
		if (res == false) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			return false;
		}//if (res == false)

		/*----------------------------
		 * 2. Set variables => currentDirPath, baseDirPath
			----------------------------*/
		init_prefs_current_path();
//
		/*----------------------------
		 * 3. Get file list
			----------------------------*/
		if (file_names == null) {
			
			init_file_list(file);	// Build a list for MainActv
			
		}//if (this.file_names == null)
			
		/*----------------------------
		 * 4. Set list to adapter
			----------------------------*/
		res = set_list_to_adapter();
		
		if (res == false)
			return false;
		
		/*----------------------------
		 * 6. Set listener to list
			----------------------------*/
		set_listener_to_list();
		
		/*----------------------------
		 * 9. Return
			----------------------------*/
		return false;
		
	}//private boolean set_initial_dir_list_part1() {

	private void set_listener_to_list() {
		/*********************************
		 * 1. Long click
		 * 
		 * 2. On touch
		 *********************************/
		ListView lv = this.getListView();
		
//		lv.setTag(Methods.ItemTags.dir_list);
		lv.setTag(Tags.ListTags.actv_main_lv);
		
		lv.setOnItemLongClickListener(new CustomOnItemLongClickListener(this));
		
		/*********************************
		 * 2. On touch
		 *********************************/
		
		
	}//private void set_listener_to_list()

	private boolean set_list_to_adapter() {
		
//		adapter = new ArrayAdapter<String>(
//				this,
//				android.R.layout.simple_list_item_1,
//				file_names
//				);
		
		adapter = new MainListAdapter(
				this,
				R.layout.list_row_main_list,
				file_names
				);

		this.setListAdapter(adapter);
		
//		// Log
//		Log.d("MainActv.java" + "["
//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//		+ "]", "adapter => set");
		
		if (adapter == null) {
//			// Log
//			Log.d("MainActv.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "adapter => null");
			
			return false;
			
		} else {//if (adapter == null)
//			// Log
//			Log.d("MainActv.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "adapter => Not null");
			
			return true;
			
		}//if (adapter == null)
		


	}//private boolean set_list_to_adapter()

	private void init_file_list(File file) {
		/*----------------------------
		 * 1. Get file array
		 * 2. Sort the array
		 * 3. Return
			----------------------------*/
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "file path: " + file.getAbsolutePath());
		
		File[] files = null;
		
		String path_in_prefs = Methods.get_currentPath_from_prefs(this);
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "path_in_prefs: " + path_in_prefs);
		

		if (path_in_prefs == null) {
			
			files = file.listFiles();
			
		} else {//if (path_in_prefs == null)
			
			files = new File(path_in_prefs).listFiles();
			
		}//if (path_in_prefs == null)
		
		//debug
		if (files == null) {
			
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "files => null");
			
		}//if (files == null)
		
		/*----------------------------
		 * 2. Sort the array
			----------------------------*/
		Methods.sortFileList(files);
		
//		List<String> file_names = new ArrayList<String>();
		file_names = new ArrayList<String>();

		for (File item : files) {
//			if (item.getName().equals(MainActv.listFileName)) {
//				
//				String tname = Methods.convert_path_into_table_name(this);
//				
//				int num_of_entries = Methods.get_num_of_entries(this, tname);
//				
////				String name = item.getName() + Methods.get_num_of_entries(this, )
//				file_names.add(item.getName() + "(" + num_of_entries + ")");
//				
//				// Log
//				Log.d("MainActv.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]",
//						"Table name=" + Methods.convert_path_into_table_name(this));
//				
//			} else {//if (item.getName().equals(MainActv.listFileName))
//				
//				file_names.add(item.getName());
//				
//			}//if (item.getName().equals(MainActv.listFileName))
			
			file_names.add(item.getName());
		}
				
	}//private void init_file_list(File file)

	private void init_prefs_current_path() {
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Starts => init_prefs_current_path()");
		
		/*----------------------------
		 * If the preference already set, then no operation
		 * 
		 * 1. Get preference
		 * 0. Prefs set already?
		 * 2. Get editor
		 * 3. Set value
			----------------------------*/
		
		
		/*----------------------------
		 * 1. Get preference
			----------------------------*/
		prefs = 
				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);

		/*----------------------------
		 * 0. Prefs set already?
			----------------------------*/
		String temp = prefs.getString(prefs_current_path, null);
		
		if (temp != null && !temp.equals("IFM8")) {
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Prefs already set: " + temp);
			
			return;
			
		}//if (temp == null)
		
		
		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "dirPath_base=" + dirPath_base);
		
		editor.putString(prefs_current_path, dirPath_base);
		
		editor.commit();
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Prefs init => " + prefs_current_path + "/" + dirPath_base);
		
	}//private void init_prefs_current_path()

	/*********************************
	 * @return true => File exists or created<br>
	 * 			false => File not created
	 *********************************/
	private boolean create_list_file(File file) {
		File list_file = new File(file, listFileName);
		
		if (list_file.exists()) {
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "\"list.txt\" => Exists");
			
			return true;
			
		} else {//if (list_file.exists())
			try {
				BufferedWriter br = new BufferedWriter(new FileWriter(list_file));
				br.close();
				
				return true;
				
			} catch (IOException e) {
				// Log
				Log.e("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "BufferedWriter: Exception => " + e.toString());
				
				return false;
			}
		}//if (list_file.exists())
		
	}//private boolean create_list_file()

	private File create_root_dir() {
		String baseDirPath = StringUtils.join(
				new String[]{
						dirName_ExternalStorage, dirName_base},
				File.separator);

		File file = new File(baseDirPath);
		
		if (!file.exists()) {
			try {
				file.mkdir();
				
				// Log
				Log.d("MainActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Dir created => " + file.getAbsolutePath());
				
				return file;
				
			} catch (Exception e) {
				// Log
				Log.e("MainActv.java"
				+ "["
				+ Thread.currentThread().getStackTrace()[2]
						.getLineNumber() + "]", "Exception => " + e.toString());
				
				return null;
			}
			
		} else {//if (file.exists())
			// Log
//			Log.d("MainActv.java"
//			+ "["
//			+ Thread.currentThread().getStackTrace()[2]
//				.getLineNumber() + "]", "Dir exists => " + file.getAbsolutePath());
			
			return file;
		}//if (file.exists())

	}//private void create_root_dir()

	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 0. Vibrate
		 * 
		 * 1. Get item name
		 * 2. Get file object
		 * 3. Is a directory?
		 * 		=> If yes, update the current path
			----------------------------*/
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onListItemClick()");
		//
		vib.vibrate(CONS.vibLength_click);
		
		String itemName = (String) lv.getItemAtPosition(position);
		
		/*----------------------------
		 * 2. Get file object
			----------------------------*/
		File target = get_file_object(itemName);
		
		/*----------------------------
		 * 3. Is a directory?
			----------------------------*/
		if (!target.exists()) {
			// debug
			Toast.makeText(this, "This item doesn't exist in the directory: " + itemName, 
					2000)
					.show();
			
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"This item doesn't exist in the directory: " + itemName);

			return;
		}//if (!target.exists())
		
		//
		if (target.isDirectory()) {
			
			Methods.enterDir(this, target);
			
//			// debug
//			Toast.makeText(this, "Enter directory: " + itemName, 
//					2000)
//					.show();
			
		} else if (target.isFile()) {//if (target.isDirectory())
			
			Methods.startThumbnailActivity(this, target.getName());
			
//			// Log
//			Log.d("MainActv.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "target.getName()=" + target.getName());
			
//			Methods.toastAndLog(this, "This is a file: " + itemName, 2000);
			
//			// debug
//			Toast.makeText(this, "This is a file: " + itemName, 
//					2000)
//					.show();
			
		}//if (target.isDirectory())
		
		
		super.onListItemClick(lv, v, position, id);
	}//protected void onListItemClick(ListView l, View v, int position, long id)

	private File get_file_object(String itemName) {
		/*----------------------------
		 * 1. 
			----------------------------*/
		
		dirPath_current = prefs.getString(prefs_current_path, null);
		
		if (dirPath_current == null) {
			
			init_prefs_current_path();
			
			dirPath_current = prefs.getString(prefs_current_path, null);
			
		}//if (dirPath_current == null)
		
		File target = new File(dirPath_current, itemName);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "dirPath_current: " + dirPath_current);
		
		return target;
	}//private File get_file_object(String itemName)

	@Override
	protected void onDestroy() {
		/*----------------------------
		 * 1. Reconfirm if the user means to quit
		 * 2. super
		 * 3. Clear => prefs
		 * 4. Clear => file_names
			----------------------------*/
		
		super.onDestroy();
		
		/*----------------------------
		 * 3. Clear => prefs
			----------------------------*/
		prefs = 
				this.getSharedPreferences(prefs_current_path, MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();

		editor.clear();
		editor.commit();
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Prefs cleared");
		
		/*----------------------------
		 * 4. Clear => file_names
			----------------------------*/
		file_names = null;
		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "file_names => Set to null");
		
	}//protected void onDestroy()

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Methods.confirm_quit(this, keyCode);
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.main_opt_menu_create_folder://----------------------------------
			
			Methods_dlg.dlg_createFolder(this);
			
			break;// case R.id.main_opt_menu_create_folder
			
		case R.id.main_opt_menu_db_activity://----------------------------------
			
			Methods_dlg.dlg_db_activity(this);
			
			break;// case R.id.main_opt_menu_db_activity

		case R.id.main_opt_menu_search://-----------------------------------------------
			
			Methods_dlg.dlg_seratchItem(this);
			
			break;// case R.id.main_opt_menu_search
			
		case R.id.main_opt_menu_preferences://-----------------------------------------------
			
			Methods.start_PrefActv(this);
			
			break;// case R.id.main_opt_menu_search
			
		case R.id.main_opt_menu_history://-----------------------------------------------
			
			Methods.show_history(this);
			
			break;// case R.id.main_opt_menu_history

		case R.id.main_opt_menu_ftp://-----------------------------------------------
			
			Intent i = new Intent();
			
			i.setClass(this, FTPActv.class);
			
			i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			
			this.startActivity(i);

			
			break;// case R.id.main_opt_menu_ftp

		}//switch (item.getItemId())
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)

	@Override
	protected void onPause() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPause();

		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onPause()");

//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "prefs: " + Methods.get_currentPath_from_prefs(this));
		
		
	}

	@Override
	protected void onResume() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onResume();

//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "onResume()");
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "prefs: " + Methods.get_currentPath_from_prefs(this));
//		
//				/*----------------------------
//		 * 2. Set enables
//			----------------------------*/
//		ImageButton ib_up = (ImageButton) findViewById(R.id.v1_bt_up);
//		
//		String curDirPath = Methods.get_currentPath_from_prefs(this);
//		
//		if (curDirPath.equals(dirPath_base)) {
//			
//			ib_up.setEnabled(false);
//			
//			ib_up.setImageResource(R.drawable.ifm8_up_disenabled);
//			
//		} else {//if (this.currentDirPath == this.baseDirPath)
//		
//			ib_up.setEnabled(true);
//
//			
//			ib_up.setImageResource(R.drawable.ifm8_up);
//		
//		}//if (this.currentDirPath == this.baseDirPath)
		
	}//protected void onResume()

	private void copy_db_file() {
		/*----------------------------
		 * 1. db setup
		 * 2. Setup files
			----------------------------*/
		
		DBUtils dbu = new DBUtils(this, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "rdb.getPath(): " + rdb.getPath());
		
		String dbPath = rdb.getPath();
		
		rdb.close();
		
		/*----------------------------
		 * 2. Setup files
			----------------------------*/
		
		String dstPath = "/mnt/sdcard-ext";
		
		File src = new File(dbPath);
//		File dst = new File(dstPath);
		File dst = new File(dstPath, src.getName());
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "src: " + src.getAbsolutePath() + "/" + "dst: " + dst.getAbsolutePath());
		
		
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied");
			
		} catch (FileNotFoundException e) {
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try

	}//copy_db_file()

	@Override
	protected void onStart() {
		
//		debugs();
		
//		setup_tablet();
		
		setup();
		
//		debug_B29_ftp();
//		
		super.onStart();
		
		// Log
		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "onStart!");
		
//		debugs();
		
	}//protected void onStart()

	private void debugs() {
		
//		debug_master_v3_0__SetupDB();
		
//		debug_master_v3_2__CreateSubDirs();
		
//		debug_master_v4_2__ChangeTableNames();
//		debug_master_v4_2_e2_t1__RestoreDBFile();
		
		_debug_show_db_dir_list();
		
	}//private void debugs() {

	private void _debug_show_db_dir_list() {
		// TODO Auto-generated method stub
		
		File f = new File(CONS.DB.dPath_db);
		
		String[] fNames = f.list();
		
		for (String fName : fNames) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + " : "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "]", "fName=" + fName);
			
		}
		
	}

	private void debug_master_v4_2_e2_t1__RestoreDBFile() {
		
//		String src = "/mnt/sdcard-ext/IFM10_backup/ifm10_backup_20130620_165036.bk";
	String src = "/mnt/sdcard-ext/IFM10_backup/ifm10_backup_20130928_205859.bk";
		String dst = StringUtils.join(new String[]{"/data/data/ifm10.main/databases", MainActv.dbName}, File.separator);
		
//		String dst = "/data/data/ifm9.main/databases" + MainActv.dbName;
		boolean res = Methods.restore_db(this, MainActv.dbName, src, dst);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
	}//private void debug_master_v4_2_e2_t1__RestoreDBFile() {

	private void debug_master_v4_2__ChangeTableNames() {
		
		List<String> tnames = Methods.get_table_list(this, "IFM9%");
		
		
		
		DBUtils dbu = new DBUtils(this, MainActv.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		int counter = 0;
		
		for (int i = 0; i < tnames.size(); i++) {
			
			String tname = tnames.get(i);
			
			//REF http://stackoverflow.com/questions/426495/how-do-you-rename-a-table-in-sqlite-3-0 answered Jan 8 '09 at 23:41
//			String sql = "ALTER TABLE " + tname + " RENAME TO " + tname.replace("IFM9", "IFM10");
			String sql = "ALTER TABLE " + tname + " RENAME TO " + tname.replaceFirst("IFM9", "IFM10");
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "sql=" + sql);
			
			wdb.execSQL(sql);
			
//			String sql = "ALTER TABLE ? RENAME TO ?";
//			
//			String[] args = new String[]{tname, tname.replace("IFM9", "IFM10")};
////			String[] args = {tname, tname.replace("IFM9", "IFM10")};
//			
//			wdb.execSQL(sql, args);
			
			counter += 1;
			
		}//for (int i = 0; i < tnames.size(); i++)
		
		wdb.close();
		
		// Log
		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Renaming => Done: " + counter + " item(s)");
		
	}//private void debug_master_v4_2__ChangeTableNames() {

	/*********************************
	 * @return true => <br>
	 * 			false => Can't get table names list, or the size of the list being less than 1
	 *********************************/
	private boolean debug_master_v3_2__CreateSubDirs() {
		/*********************************
		 * Get => Table names list
		 * Build => dir paths
		 * 
		 * Create => Subdirs using the newly-created subdir paths
		 *********************************/
		List<String> tnames = Methods.get_table_list(this, "IFM9%");
		
		if (tnames == null) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "tnames == null");
			
			return false;
			
		} else if (tnames.size() < 1) {//if (tnames == null)
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "tnames.size < 1");
			
			return false;
			
		}//if (tnames == null)
		
		for (int i = 0; i < tnames.size(); i++) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "table name=" + tnames.get(i));
			
		}//for (int i = 0; i < tnames.size(); i++)
		
		/*********************************
		 * Build => dir paths
		 *********************************/
		List<String> dirPaths = new ArrayList<String>();
		
		for (int i = 0; i < tnames.size(); i++) {
			
			String[] tnameSplit = tnames.get(i).split("__");
			
			String dpath_Each = null;
			
			if (tnameSplit.length > 1) {
				
				dpath_Each = StringUtils.join(
								Arrays.copyOfRange(tnameSplit, 1, tnameSplit.length)
								,
								File.separator);
				
			} else {//if (tnameSplit.length > 1)
				
				dpath_Each = tnameSplit[0];
				
			}//if (tnameSplit.length > 1)
			
			
			String dpaths = StringUtils.join(
								new String[]{
										MainActv.dirPath_base,
										dpath_Each
								},
								File.separator);

//			// Log
//			Log.d("["
//					+ "MainActv.java : "
//					+ +Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "dpath_Each=" + dpath_Each);
//			
//			// Log
//			Log.d("["
//					+ "MainActv.java : "
//					+ +Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "dpaths=" + dpaths);
			
			// Add path to the list
			dirPaths.add(dpaths);
			
		}//for (int i = 0; i < tnames.size(); i++)
		
//		// Log
//		Log.d("[" + "MainActv.java : "
//				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]",
//				"dirPaths.size()=" + dirPaths.size()
//				+ "(tnames.size=" + tnames.size() + ")");
		
		/*********************************
		 * Create => Subdirs using the newly-created subdir paths
		 *********************************/
		for (int i = 0; i < dirPaths.size(); i++) {
			
			String fullPath = dirPaths.get(i);
			
			File f = new File(fullPath);
			
			if (f.exists()) {		// Folder exists
				// If the folder exists, then validate if the file "list.txt" also exists
				String listPath = StringUtils.join(
									new String[]{
											fullPath,
											MainActv.listFileName
									},
									File.separator); 
				
				File listFile = new File(listPath);
				
				if (listFile.exists()) {
					
				} else {//if (listFile.exists())	// The folder exists, but the list file doesn't
													// => Hence, create the file "list.txt"
					
					boolean res;
					
					try {
						
						res = listFile.createNewFile();
						
						// Log
						Log.d("["
								+ "MainActv.java : "
								+ +Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"list.txt => Created in: " + fullPath);
						
					} catch (IOException e) {
						
						// Log
						Log.d("["
								+ "MainActv.java : "
								+ +Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"list.txt => Not created in: " + fullPath);
						
					}
					
				}//if (listFile.exists())
				
				
			} else {//if (f.exists())		// Folder doesn't exist. Create one.
				
				boolean res = f.mkdirs();
				
				if (res == true) {			// The folder was created. Now create the list file.
					
					String listPath = StringUtils.join(
							new String[]{
									fullPath,
									MainActv.listFileName
							},
							File.separator); 
					
					File listFile = new File(listPath);
					
					try {
						
						res = listFile.createNewFile();
						
						// Log
						Log.d("["
								+ "MainActv.java : "
								+ +Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"list.txt => Created in: " + fullPath);
						
						Methods.writeLog(
									"list.txt => Created in: " + fullPath,
									"MainActv.java : "
										+ Thread.currentThread().getStackTrace()[2]
												.getMethodName()
										+ ":"
										+ Thread.currentThread().getStackTrace()[2]
												.getLineNumber()	
									);
						
					} catch (IOException e) {
						
						// Log
						Log.d("["
								+ "MainActv.java : "
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]",
								"list.txt => Not created in: " + fullPath);
						
					}
					
				} else {//if (res)
					
				}//if (res)
				
				
			}//if (f.exists())
			
			
		}//for (int i = 0; i < dirPaths.size(); i++)
		
		
		return true;
		
	}//private boolean debug_master_v3_2__CreateSubDirs() {

	private void debug_master_v3_0__SetupDB() {
		/*********************************
		 * Steps
		 * 
		 * 1. Create => Root dir
		 * 2. Create => Backup dir
		 * 
		 * 3. Create => database dir
		 * 
		 * 4. C/P => backup-ed db file
		 * 
		 * Backup db
		 * 
		 * Create => Sub dirs
		 *********************************/
		boolean res = debug_master_v3_0__SetupDB__1_CreateRootAndBackupDirs();
		
		if (res == false) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "res == false");
			
			return;
			
		}//if (res == false)

		/*********************************
		 * Create => database dir
		 *********************************/
		res = debug_master_v3_0__SetupDB__2_CreateDatabseDir();
		
		if (res == false) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "res == false");
			
			return;
			
		}//if (res == false)
		
		/*********************************
		 * 4. C/P => backup-ed db file
		 *********************************/
//		res = debug_master_v3_0__SetupDB__3_CopyPasteDBFile();
//		
//		if (res == false) {
//			
//			// Log
//			Log.d("["
//					+ "MainActv.java : "
//					+ +Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "res == false");
//			
//			return;
//			
//		}//if (res == false)
		
		/*********************************
		 * Backup db
		 *********************************/
		
		
		/*********************************
		 * Create => Sub dirs
		 *********************************/
//		dirPath_db_backup
		
	}//debug_master_v3_0__SetupDB()

	@SuppressWarnings("resource")
	private boolean debug_master_v3_0__SetupDB__3_CopyPasteDBFile() {
		/*********************************
		 * Build => File paths
		 * Validate => Files exist?
		 * Copy
		 *********************************/
		String src = StringUtils.join(
								new String[]{
										MainActv.dirPath_db_backup,
										"ifm9_backup_20130617_194313.bk"
								},
								File.separator);
		
		String dst = StringUtils.join(
								new String[]{
										MainActv.dirPath_db,
										MainActv.dbName
								},
								File.separator);
		
		// Log
		Log.d("[" + "MainActv.java : "
				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "src=" + src + "/" + "dst=" + dst);
		
		/*********************************
		 * Validate => Files exist?
		 *********************************/
		File f_src = new File(src);
		File f_dst = new File(dst);
		
		if (f_src.exists()) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "f_src => Exists");
			
			if (f_dst.exists()) {
				
				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "f_dst => Exists");
				
			} else {//if (f_dst.exists())
				
				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "f_dst => Doesn't exist");
				
				return false;
				
			}//if (f_dst.exists())
			
			
		} else {//if (f_src.exists())
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "f_src => Doesn't exist");
			
			return false;
			
		}//if (f_src.exists())
		
		/*********************************
		 * Copy
		 *********************************/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]",
					"File copied from: " + src
					+ "/ to: " + dst);
			
			if (Looper.myLooper() == Looper.getMainLooper()) {
				
				// debug
				Toast.makeText(this, "DB restoration => Done", Toast.LENGTH_LONG).show();
				
			} else {//if (condition)

				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "DB restoration => Done");
				
			}//if (condition)
			
		} catch (FileNotFoundException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		} catch (IOException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		}//try
		
		
		
		return true;

	}//private boolean debug_master_v3_0__SetupDB__3_CopyPasteDBFile() {

	/*********************************
	 * @return true	=> DB dir exists, or doesn't exist and was created<br>
	 * 			false => DB dir doesn't exist, and was not created
	 *********************************/
	private boolean debug_master_v3_0__SetupDB__2_CreateDatabseDir() {
		
		File f = new File(MainActv.dirPath_db);
		
		if (f.exists()) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "DB dir exists: " + f.getAbsolutePath());
			
			String[] fnames = f.list();
			
			if (fnames.length < 1) {
				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "No files in: " + f.getAbsolutePath());
			} else {//if (fnames.length < 1)
				
				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "fnames.length=" + fnames.length);
				
				for (int i = 0; i < fnames.length; i++) {
					
					// Log
					Log.d("["
							+ "MainActv.java : "
							+ +Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "fname=" + fnames[i]);
					
				}//for (int i = 0; i < fnames.length; i++)
				
			}//if (fnames.length < 1)
			
			
			return true;
			
		} else {//if (f.exists())
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "DB dir doesn't exist: " + f.getAbsolutePath());
			
			DBUtils dbu = new DBUtils(this, MainActv.dbName);
			
			SQLiteDatabase wdb = dbu.getWritableDatabase();
			
			
			wdb.close();
			
			if (f.exists()) {
				
				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "DB dir created: " + f.getAbsolutePath());
				
				return true;

			} else {//if (f.exists())

				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "DB dir not created: " + f.getAbsolutePath());
				
				return false;
				
			}//if (f.exists())
			
			
		}//if (f.exists())
		
	}//private boolean debug_master_v3_0__SetupDB__2_CreateDatabseDir() {

	/*********************************
	 * @return true => Root dir exists or got created. Backup dir also exists or got created.<br>
	 * 			false => Root dir not created, or the root dir was created but the backup dir was not.
	 *********************************/
	private boolean debug_master_v3_0__SetupDB__1_CreateRootAndBackupDirs() {

		boolean res;
		
		/*********************************
		 * Create => Root dir
		 *********************************/
		
		File file = create_root_dir();
		
		if (file == null) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file == null");
			
			return false;
		}//if (file == null)
		
		/*----------------------------
		 * 1-2. Create "list.txt"
			----------------------------*/
		res = create_list_file(file);
		
		if (res == false) {
			Log.e("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			return false;
		}//if (res == false)

		/*********************************
		 * Create => Backup dir
		 *********************************/
		File f = new File(MainActv.dirPath_db_backup);
		
		if (f.exists()) {
			
			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
					"Backup dir exists: " + f.getAbsolutePath());
			
		} else {//if (f.exists())

			// Log
			Log.d("["
					+ "MainActv.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]",
							"Backup dir doesn't exist: " + f.getAbsolutePath());
			
			res = f.mkdir();
			
			if (res == true) {
				
				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Backup dir created: " + f.getAbsolutePath());
				
			} else {//if (res == true)

				// Log
				Log.d("["
						+ "MainActv.java : "
						+ +Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"Create backup dir => Failed : " + f.getAbsolutePath());
				
				return false;
				
			}//if (res == true)
			
			
		}//if (f.exists())
		
		return true;
	}

	private void debug_b30() {
		
		String src = MainActv.dirPath_db_backup + File.separator
					+"ifm9_backup_20121110_102050.bk";
		
		String dst = MainActv.dirPath_db + File.separator
					+ MainActv.dbName;
		
		Methods.restore_db(this, MainActv.dbName, src, dst);
		
//		String current_path = Methods.get_pref(this, MainActv.prefs_current_path, null);
//		
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "current_path=" + current_path);
//		
	}

	private void debug_B27() {
		/*********************************
		 * memo
		 *********************************/
		SharedPreferences prefs =
				this.getSharedPreferences(
						this.getString(R.string.prefs_shared_prefs_name), 0);
		
		String pref_history_size = prefs.getString(this.getString(R.string.prefs_history_size_key), null);
		
		boolean res = Methods.is_numeric(pref_history_size);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "pref_history_size=" + pref_history_size
				+ "(is_numeric=" + res + ")");
		
	}//private void debug_B27()

	private void debug_B29_ftp() {
		/*********************************
		 * memo
		 *********************************/
		FTPClient ftpclient;
		
	}

	private void refresh_db() {
		SharedPreferences prefs =
				this.getSharedPreferences(this.getString(R.string.prefs_shared_prefs_name), 0);

		//// Log
		//Log.d("MainActv.java" + "["
		//	+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//	+ "]", "prefs: db_refresh => " + prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false));
		
		if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false)) {
		
			Methods.start_refreshDB(this);
		
		} else {//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
		//// Log
		//Log.d("MainActv.java" + "["
		//		+ Thread.currentThread().getStackTrace()[2].getLineNumber()
		//		+ "]", "Prefs: db_refresh => " + false);
		
		}//if(prefs.getBoolean(this.getString(R.string.prefs_db_refresh_key), false))
		
	}

}//public class MainActv extends Activity

//public class MainActv extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.actv_main);
//		
//		// Log
//		Log.d("[" + "MainActv.java : "
//				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "YES!!!");
//		
//		// Log
//		Log.d("[" + "MainActv.java : "
//				+ +Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]",
//			"message" + StringUtils.join(new String[]{"/data/data/ifm9.main/databases", "trillion"}, File.separator));
//		
////		DefaultHttpClient httpclient = new DefaultHttpClient();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main_actv, menu);
//		return true;
//	}
//
//}
