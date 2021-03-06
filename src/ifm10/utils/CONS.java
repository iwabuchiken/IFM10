package ifm10.utils;

import java.io.File;

import ifm10.main.MainActv;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

public class CONS {

	public static enum MoveMode {
		// TIListAdapter.java
		ON, OFF,
		
	}//public static enum MoveMode

	public static enum PrefenceLabels {
		
		CURRENT_PATH,
		
		thumb_actv,
		
		chosen_list_item,
		
	}//public static enum PrefenceLabels

	/*********************************
	 * DB
	 *********************************/
	 // DB name
	static String dbName = null;

	// Database
	SQLiteDatabase db = null;

	//
	static String[] cols_with_index = {
				// 0-2
				android.provider.BaseColumns._ID,
				// 3-6
				"file_id", 		"file_path", "file_name", "date_added",
				"date_modified", "memos", "tags"};
	
	static String[] col_types_with_index =
				{	"INTEGER", "TEXT", 	"TEXT",		"INTEGER",
					"INTEGER",		"TEXT",	"TEXT"};

	// Main data
	public static String[] cols =
		//column number: 3-6
		//Table index
		//	0			1			2				3
		{"file_id", "file_path", "file_name", 	"date_added",
		//column number: 7-10
		//	4				5			6			7
		"date_modified",	"memos", "tags", 	"last_viewed_at",
		//column number: 11
		//	8
		"table_name"};
//	"date_modified", "memos", "tags"};

	public static String[] cols_full =
			//column number: 3-6
			//Table index
		{
			//	0
			android.provider.BaseColumns._ID,
			//	1			2
			"created_at", "modified_at",
			//	3		4				5
			"file_id", "file_path", "file_name",
			//	6			7
			"date_added", "date_modified",
			//	8		9
			"memos", "tags",
			//	10
			"last_viewed_at",
			//	11
			"table_name"
		};

	public static String[] col_types =
		{"INTEGER", "TEXT", 	"TEXT",			"INTEGER",
		"INTEGER",			"TEXT",	"TEXT",		"INTEGER",
		"String"};

	static String[] cols_for_insert_data = {
				"file_id", "file_path",
				"file_name",
				"date_added", "date_modified",
				"table_name"};
//	{"file_id", 		"file_path", "file_name", "date_added", "date_modified"};

	// Proj
	static String[] proj = {
		MediaStore.Images.Media._ID, 
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.DATE_ADDED,
		MediaStore.Images.Media.DATE_MODIFIED,
		};

	static String[] proj_for_get_data = {
		MediaStore.Images.Media._ID, 
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.DATE_ADDED,
		MediaStore.Images.Media.DATE_MODIFIED,
		"memos",
		"tags"
		};

	static String[] cols_refresh_log = {
		"last_refreshed", "num_of_items_added"
	};
	
	static String[] col_types_refresh_log = {
		"INTEGER", 			"INTEGER"
	};

	static String[] cols_memo_patterns = {"word", "table_name"};
	static String[] col_types_memo_patterns = {"TEXT", "TEXT"};
	
	static String table_name_memo_patterns = "memo_patterns";

	public static String[] cols_show_history = {
		"file_id", "table_name"
	};
	
	public static String[] col_types_show_history = {
		"INTEGER", "TEXT"
	};

	/****************************************
	 * Vars
	 ****************************************/
	public static final int vibLength_click = 35;

	static int tempRecordNum = 20;


//	public static enum ListTags {
//		// MainActivity.java
//		actv_main_lv,
//		
//		// Main
//		main_list_adapter,
//		
//	}//public static enum ListTags

	public static class DBAdmin {
//		created_at INTEGER, modified_at INTEGER,
		public static final
		String[] timeStamps = {"created_at", "modified_at"};
		
		public static final
		String tname_purchaseSchedule = "purchase_schedule";

		public static
		String[] col_purchaseSchedule =
				{"store_name", "due_date", "amount", "memo", "items"};

		public static
		String[] colTypes_purchaseSchedule =
				{"TEXT",		"INTEGER", "INTEGER", "TEXT", "TEXT"};

	}

	public static class HTTP_Response {
		
		public static final int OK = 200;
		
		public static final int CREATED = 201;
		
		public static final int NOT_CREATED = -201;
		
		
		
	}

	public static class Admin {
		
		public static
		String dpath_Log = MainActv.dirName_ExternalStorage
							+ File.separator
							+ "IFM10_log";
		
		public static
		String fname_log = "ifm10.log";

		public static String
		appName = "ifm10";
		
		public static final int
		vibLength_Long = 100;
		
		
	}//public static class Admin {

	public static class FTP {
		
		public static String
		serverName = "ftp.benfranklin.chips.jp";
		
		public static String
		userName = "chips.jp-benfranklin";
		
		public static String
		passWord = "9x9jh4";

		public static String
		dPath_remote_db = "android_app_data/IFM10";
		
	}//public static class FTP
	
	public static class DB {
		
		public static String
		dPath_db = "/data/data/ifm10.main/databases";
		
		public static String
		fName_db = "ifm9.db";
		
		
		
		
	}//public static class DB {

}
