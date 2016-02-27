package my.Menu.App;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import my.Menu.Adapter.ListIngredientAdapter;
import my.Menu.Object.Ingredient;

public class DisplayGestIngPage extends ListActivity implements OnClickListener, OnLongClickListener
{
		
		private static final String TAG = "EKA.DisplayGestIngPage";
		private static final int AJOUT = 1;
		private static final int RENOMMER = 2;
		private static final int SUPPRIMER = 3;
		
		
		private DBAdapter db;
		//AlertDialog
		private View alertView;
		
	    @Override // Création du menu principal
	    public boolean onCreateOptionsMenu(Menu menu) {    	
	    	menu.add(0,100,0,R.string.libAjouterIngredient);
	    	return true;
	    }
	    
	    @Override // Selection d'un item du menu principal
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	Log.d(DisplayGestIngPage.TAG, "Le menu est utilisé: "+item.getItemId());
	    	switch(item.getItemId()){
	    	//Ajouter un ingrédient
	    	case 100: 
	    		showAlertDialog(DisplayGestIngPage.AJOUT, null);
	    		break;    	
	    	}
	    	return true;
	    }


		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.gestion_ingredients);
			registerForContextMenu(getListView());

			db = new DBAdapter(this);
	        db.open();
	        DataBind();
		        
		    Button btExit = (Button)findViewById(R.id.btRetour);
		    btExit.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					DisplayGestIngPage.this.finish();
				}       	
	        });
		}

	    @Override
	    protected void onDestroy() {
	    	db.close();
	    	super.onDestroy();
	    }

	    public void DataBind(){
	    	ArrayList<Ingredient> listIng = db.recupererListIngredient(null,false);
	    	ListIngredientAdapter adapter = new ListIngredientAdapter(this.getBaseContext(),
	    			R.layout.list_ingredient,
	    			R.id.nomIngredient,listIng)
	    	;
	    	setListAdapter(adapter);
	    }

		@Override
		public void onClick(View v) {
			Log.d(DisplayGestIngPage.TAG,"onClickView");
			// Auto-generated method stub
			
		}
		@Override
		public boolean onLongClick(View arg0) {
			arg0.showContextMenu();
			return false;
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			menu.add(0,200,0,R.string.libModifierIngredient);
	    	menu.add(0,300,0,R.string.libSupprimerIngredient);
			super.onCreateContextMenu(menu, v, menuInfo);
		}
		
		@Override
		public boolean onContextItemSelected(MenuItem item) {
	    	
	    	AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	    	//Long id = getListAdapter().getItemId(info.position);/*what item was selected is ListView*/
	    	
	    	Ingredient selectedIng = (Ingredient)getListAdapter().getItem(info.position);
	    	switch(item.getItemId()){
	    	//Modifier un ingrédient
	    	case 200: 
	    		//Toast.makeText(this, "MODIFIER ELEMENT "+idIng + "-"+nomIng, Toast.LENGTH_SHORT).show();
	    		showAlertDialog(DisplayGestIngPage.RENOMMER, selectedIng);
	    		break;
	    	//Supprimer un ingrédient
	    	case 300: 
	    		//Toast.makeText(this, "SUPPRIMER ELEMENT "+idIng + "-"+nomIng, Toast.LENGTH_SHORT).show();
	    		showAlertDialog(DisplayGestIngPage.SUPPRIMER, selectedIng);
	    		break;	    			
	    	}

			return super.onContextItemSelected(item);
		}

		private void showAlertDialog(int mode, Ingredient selectedIng) {
			String textDuBouton="";
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
			
			
			alertView = LayoutInflater.from(this).inflate(R.layout.alertdialog_ingredient,null);
			EditText et = (EditText)alertView.findViewById(R.id.etNomIngredient);
			ad.setView(alertView);
			
			switch(mode){
	    	//Ajouter un ingrédient
	    	case DisplayGestIngPage.AJOUT:
	    		ad.setIcon(R.drawable.ic_ing_ajout_2);
	    		ad.setTitle(R.string.libAjouterIngredient);
	    		textDuBouton = getResources().getString(R.string.libBtAjouter);
	    		alertView.setTag(DisplayGestIngPage.AJOUT);
	    		break;    	
			case DisplayGestIngPage.RENOMMER:
				ad.setIcon(R.drawable.ic_ing_modif_2);
	    		ad.setTitle(R.string.libModifierIngredient);
	    		textDuBouton = getResources().getString(R.string.libBtModifier);
	    		et.setText(selectedIng.getNomIngredient());
	    		et.setTag(selectedIng.getIdIngredient());
	    		alertView.setTag(DisplayGestIngPage.RENOMMER);
	    		break;    	
	    	case DisplayGestIngPage.SUPPRIMER: 
	    		ad.setIcon(R.drawable.ic_ing_suppr_2);
	    		ad.setTitle(R.string.libSupprimerIngredient);
	    		textDuBouton = getResources().getString(R.string.libBtSupprimer);
				et.setText(selectedIng.getNomIngredient());
				et.setTag(selectedIng.getIdIngredient());
				et.setEnabled(false);
				alertView.setTag(DisplayGestIngPage.SUPPRIMER);
	    		break;    	
	    	}

			ad.setNegativeButton(getResources().getString(R.string.libBtAnnuler), 
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						Log.d(DisplayGestIngPage.TAG, "ANNULER/Action annulée");
					}
				}
			);
			
			
			ad.setPositiveButton(textDuBouton, 
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						gererRetourAlertDialog();
					}
				}
			);
				
			ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
				public void onCancel(DialogInterface dialog) {
					Log.d(DisplayGestIngPage.TAG, "CANCEL/Action annulée");			
				}}
			);				
			ad.show();
		}

		protected void gererRetourAlertDialog() {
			EditText et = (EditText)alertView.findViewById(R.id.etNomIngredient);
			Ingredient currentIngredient = new Ingredient((Long)et.getTag(),
					et.getText().toString().trim(),
					null);
			if (currentIngredient.getNomIngredient().equals(""))
				Toast.makeText(this, getResources().getString(R.string.errorNomIngredient), Toast.LENGTH_SHORT).show();
			else {
				Integer avTag = (Integer) alertView.getTag();
				switch (avTag.intValue()) {
				case DisplayGestIngPage.AJOUT:
					db.insererUnIngredient(currentIngredient);
					break;
				case DisplayGestIngPage.RENOMMER:
					db.modifierIngredient(currentIngredient);
					break;
				case DisplayGestIngPage.SUPPRIMER:
					String resultDel = db.supprimerIngredient(currentIngredient);
					if (!resultDel.equalsIgnoreCase("")){
						Toast.makeText(this, getResources().getString(R.string.errorIngInUse) + resultDel, Toast.LENGTH_LONG).show();
					}
					break;	
				}
				
			}
			DataBind();
		}
}
