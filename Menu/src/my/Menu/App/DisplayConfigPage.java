package my.Menu.App;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 
public class DisplayConfigPage extends Activity
{
	@SuppressWarnings("unused")
	private static final String TAG = "EKA.DisplayConfigPage";
	private DBAdapter db;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_page);
	    
	    Button btExit = (Button)findViewById(R.id.btRetour);
	    btExit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				DisplayConfigPage.this.finish();
			}       	
        });
	    

	    Button btGererDB = (Button)findViewById(R.id.btBdd);
	    btGererDB.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				dropBDD();
			}       	
        });	  
	    
	    Button btGererIngredient = (Button)findViewById(R.id.btGestIng);
	    btGererIngredient.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showGestIngPage();
			}       	
        });	  

	    Button btGererPlat = (Button)findViewById(R.id.btGestPlat);
	    btGererPlat.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showGestPlatPage();
			}       	
        });	
	}
    /**
     * Affiche la page de configuration des ingrédients
     */
    public void showGestIngPage() {
    	
	 	Intent intent = new Intent(DisplayConfigPage.this,DisplayGestIngPage.class);
	 	//TODO set REQUEST_CODE
	 	startActivityForResult(intent,0);
    }
    
    /**
     * Affiche la page de configuration des plats
     */
    public void showGestPlatPage() {
    	
	 	Intent intent = new Intent(DisplayConfigPage.this,DisplayGestPlatPage.class);
	 	//TODO set REQUEST_CODE
	 	startActivityForResult(intent,0);
    }    
    
    public void dropBDD() {
       db = new DBAdapter(this);
       db.open();
       db.dropBDD();
       db.close();

    }
}