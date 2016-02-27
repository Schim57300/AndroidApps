package my.Menu.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private static final int REQUEST_DISPLAY_CONFIG = 1;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btConfig = (Button)findViewById(R.id.btConfig);
        btConfig.setOnClickListener(new Button.OnClickListener() {        	
            public void onClick(View v) {
            	showConfigPage();
            }
        });
        Button btExit = (Button)findViewById(R.id.btExit);
        btExit.setOnClickListener(new Button.OnClickListener() {        	
            public void onClick(View v) {
            	MainActivity.this.finish();
            }
        });

    }
    
    
    /**
     * Affiche la page de configuration
     */
    public void showConfigPage() {
    	
	 	Intent intent = new Intent(MainActivity.this,DisplayConfigPage.class);
	 	//intent.putExtra("Login", ((EditText) findViewById(R.id.etLogin)).getText().toString());
	 	//intent.putExtra("Password", ((EditText) findViewById(R.id.etPassword)).getText().toString());
	 	//intent.putExtra("OTP", ((EditText) findViewById(R.id.etOTP)).getText().toString());

	 	startActivityForResult(intent,REQUEST_DISPLAY_CONFIG);
    }
    
}