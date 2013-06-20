package ifm10.main;

import ifm10.tasks.TaskFTP;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FTPActv extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/********************************
		 * 
		 ********************************/

		super.onCreate(savedInstanceState);
		
		setup_1();

//		setContentView(R.layout.main);
//
//		this.setTitle(this.getClass().getName());

	}//public void onCreate(Bundle savedInstanceState)

	private void setup_1() {
		/*********************************
		 * memo
		 *********************************/
		setContentView(R.layout.activity_ftp);

		this.setTitle(this.getClass().getName());
		
		TaskFTP task = new TaskFTP(this);
		
		task.execute("ABC");
//		Methods.ftp_connect_disconnect(this);
		
//		MethodsFTP.ftp_connect_disconnect(this);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
	}
	
}//public class FTPActv extends Activity
