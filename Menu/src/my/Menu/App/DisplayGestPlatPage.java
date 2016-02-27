package my.Menu.App;

import java.util.ArrayList;
import java.util.Date;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import my.Menu.Adapter.CompoPlatAdapter;
import my.Menu.Adapter.ListPlatAdapter;
import my.Menu.Object.Ingredient;
import my.Menu.Object.Plat;
import my.Menu.Object.RowIngredient;
import my.Menu.Object.RowIngredientHolder;

public class DisplayGestPlatPage  extends ListActivity implements OnClickListener, OnLongClickListener, android.content.DialogInterface.OnClickListener{

	private static final String TAG = "EKA.DisplayGestPlatPage";
	public static final int AJOUT = 1;
	public static final int MODIFIER = 2;
	public static final int SUPPRIMER = 3;
	public static final int CONSULTER = 4;


	private DBAdapter db;
	//AlertDialog
	private View alertView;
	private View convertView;
	private ListView mainListView ;  
	
    @Override // Création du menu principal
    public boolean onCreateOptionsMenu(Menu menu) {    	
    	menu.add(0,100,0,R.string.libAjouterPlat);
    	return true;
    }
    
    @Override // Selection d'un item du menu principal
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	//Ajouter un ingrédient
    	case 100: 
    		showAlertDialog(DisplayGestPlatPage.AJOUT, null);
    		break;    	
    	}
    	return true;
    }


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gestion_plats);
		registerForContextMenu(getListView());

		db = new DBAdapter(this);
        db.open();
        DataBind();
	        
	    Button btExit = (Button)findViewById(R.id.btRetour);
	    btExit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				DisplayGestPlatPage.this.finish();
			}       	
        });  
	}

    public void DataBind(){
    	ArrayList<Plat> maListePlat = db.recupererLaListeDesPlats(null);
    	ListPlatAdapter adapter = new ListPlatAdapter(this.getBaseContext(),
    	R.layout.list_plat,
    	R.id.nomPlat, 
    	maListePlat);
    	setListAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
    	db.close();
    	super.onDestroy();
    }


	@Override
	public boolean onLongClick(View arg0) {
		arg0.showContextMenu();
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// Auto-generated method stub
		//Log.d(DisplayGestPlatPage.TAG,"onClickView");		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add(0,100,0,R.string.libConsulterPlat);
		menu.add(0,200,0,R.string.libModifierPlat);
    	menu.add(0,300,0,R.string.libSupprimerPlat);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
    	Log.d(DisplayGestPlatPage.TAG, "Le menu contextuel est utilisé: "+item.getItemId());
    	
    	AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	//Long id = getListAdapter().getItemId(info.position);/*what item was selected is ListView*/
    	
    	Plat currentPlat = (Plat) getListAdapter().getItem(info.position); 
    	Log.d(DisplayGestPlatPage.TAG,"cursor.toString()="+currentPlat);
    	switch(item.getItemId()){
    	//Modifier un ingrédient
    	case 100: 
    		showAlertDialog(DisplayGestPlatPage.CONSULTER, currentPlat);
    		break;
    	//Supprimer un ingrédient
    	case 200: 
    		//Toast.makeText(this, "MODIFIER ELEMENT "+idIng + "-"+nomIng, Toast.LENGTH_SHORT).show();
    		showAlertDialog(DisplayGestPlatPage.MODIFIER, currentPlat);
    		break;
    	//Supprimer un ingrédient
    	case 300: 
    		//Toast.makeText(this, "SUPPRIMER ELEMENT "+idIng + "-"+nomIng, Toast.LENGTH_SHORT).show();
    		showAlertDialog(DisplayGestPlatPage.SUPPRIMER, currentPlat);
    		break;	    			
    	}

		return super.onContextItemSelected(item);
	}

	private void showAlertDialog(int mode, Plat selectedPlat) {
		//TODO ShowAlertDialog
		String textDuBouton="";
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
	    ArrayList<RowIngredient> rowIngredientList = new ArrayList<RowIngredient>();  

	    
		alertView = LayoutInflater.from(this).inflate(R.layout.alertdialog_plat,null);
		EditText et = (EditText)alertView.findViewById(R.id.etNomPlat);
		ad.setView(alertView);
		switch(mode){
    	//Ajouter un ingrédient
    	case DisplayGestPlatPage.AJOUT:
    		ad.setIcon(R.drawable.ic_plat_ajout_2);
    		ad.setTitle(R.string.libAjouterPlat);
    		textDuBouton = getResources().getString(R.string.libBtAjouter);
    		rowIngredientList = db.recupererListRowIngredient(selectedPlat, false);
    		alertView.setTag(DisplayGestPlatPage.AJOUT);
    		break;    	
    	case DisplayGestPlatPage.CONSULTER:
    		//TODO : Changer l'icone de consultation
			ad.setIcon(R.drawable.ic_plat_modif_2);
    		ad.setTitle(R.string.libConsulterPlat);
    		textDuBouton = getResources().getString(R.string.libBtRetour);
    		et.setText(selectedPlat.getNomPlat());
    		et.setEnabled(false);
    		et.setTag(selectedPlat);
    		rowIngredientList = db.recupererListRowIngredient(selectedPlat,true);
    		alertView.setTag(DisplayGestPlatPage.CONSULTER);
    		break;    	
    	case DisplayGestPlatPage.MODIFIER:
			ad.setIcon(R.drawable.ic_plat_modif_2);
    		ad.setTitle(R.string.libModifierPlat);
    		textDuBouton = getResources().getString(R.string.libBtModifier);
    		et.setText(selectedPlat.getNomPlat());
    		et.setTag(selectedPlat);
    		rowIngredientList = db.recupererListRowIngredient(selectedPlat, false);
    		alertView.setTag(DisplayGestPlatPage.MODIFIER);
    		break;    	
    	case DisplayGestPlatPage.SUPPRIMER: 
    		ad.setIcon(R.drawable.ic_plat_suppr_2);
    		ad.setTitle(R.string.libSupprimerPlat);
    		textDuBouton = getResources().getString(R.string.libBtSupprimer);
			et.setText(selectedPlat.getNomPlat());
			et.setTag(selectedPlat);
			et.setEnabled(false);
    		rowIngredientList = db.recupererListRowIngredient(selectedPlat, true);
			alertView.setTag(DisplayGestPlatPage.SUPPRIMER);
    		break;    	
    	}

		///////////////////////////////////////////////////////////////////////////

	    
	    CompoPlatAdapter mIngList = new CompoPlatAdapter(this.getBaseContext(), 
				R.layout.list_ing_alertdialog_plat, R.id.nomIng, rowIngredientList, mode);	     
	    // Find the ListView resource.   
	    mainListView = (ListView) alertView.findViewById( R.id.list );  
	    mainListView.setAdapter(mIngList);  
	    
		///////////////////////////////////////////////////////////////////////////

	    //Si autre mode que CONSULTATION => Ajout d'un second bouton
	    if (mode != DisplayGestPlatPage.CONSULTER) {
			ad.setNegativeButton(getResources().getString(R.string.libBtAnnuler), 
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						Log.d(DisplayGestPlatPage.TAG, "ANNULER/Action annulée");
					}
				}
			);
	    }
		
		ad.setPositiveButton(textDuBouton, 
			new android.content.DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {
					gererRetourAlertDialog();
					Log.d(DisplayGestPlatPage.TAG, "OK-AJOUTE");
				}
			}
		);
			
		ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface dialog) {
				Log.d(DisplayGestPlatPage.TAG, "CANCEL/Action annulée");			
			}}
		);				
		ad.show();
	}

	protected void gererRetourAlertDialog() {
		
		EditText et = (EditText)alertView.findViewById(R.id.etNomPlat);
		String nomPlat = et.getText().toString().trim();
		Log.d(DisplayGestPlatPage.TAG, "Contenu du EditText apres validation : "+ nomPlat+".");
		if (nomPlat.equals(""))
			Toast.makeText(this, getResources().getString(R.string.errorNomPlat), Toast.LENGTH_SHORT).show();
		else {
			//On récupère le type d'Alertview (Ajout, Modif ou Suppr)
			Integer avTag = (Integer) alertView.getTag();
			//TODO Gestion du retour de l'AV
			CompoPlatAdapter lpAdapter = (CompoPlatAdapter)mainListView.getAdapter();
			Plat currentPlat = new Plat(avTag.longValue(), nomPlat, new ArrayList<Ingredient>(), new Date().toString() );
			for (int i = 0;i<lpAdapter.getCount();i++) {
				
				convertView = lpAdapter.getView(i, convertView , mainListView);
				RowIngredientHolder rowIngHolder = (RowIngredientHolder)convertView.getTag();
				CheckBox cb = rowIngHolder.getCheckBox(); 
				if (cb.isChecked()){ 
					RowIngredient rowIng = (RowIngredient) cb.getTag();  
					Log.d(DisplayGestPlatPage.TAG, "    rowIng : " + (rowIng));
					currentPlat.addIng(rowIng.getIngredient());
				}
			}		
			switch (avTag.intValue()) {
			case DisplayGestPlatPage.AJOUT:
				//db.insererUnIngredient(nomIngredient);
				break;
			case DisplayGestPlatPage.MODIFIER:
				//db.modifierIngredient(idIng, nomIngredient);
				break;
			case DisplayGestPlatPage.SUPPRIMER:
				//db.supprimerIngredient(idIng);
				break;	
			}
			
		}
			
		DataBind();

	}

	@Override
	public void onClick(DialogInterface paramDialogInterface, int paramInt) {
		// Auto-generated method stub
		
	}	
   //Fonction appelée au clic d'une des checkbox
//	public void handlerCheckBox(View v) {
//		//TODO Mon Handler
//		CheckBox cb = (CheckBox) v;
//		//on récupère la position à l'aide du tag défini dans la classe MyListAdapter
//		Log.d(DisplayGestPlatPage.TAG, "    cb.getTag="+cb.getTag()+".");
//		int position = Integer.parseInt(cb.getTag().toString());
//		// On récupère l'élément sur lequel on va changer la couleur
//		View o = listChkIng.getChildAt(position).findViewById(R.id.blocCheck);
//		//On change la couleur
//		if (cb.isChecked()) {
//			o.setBackgroundResource(R.color.blue);
//		} else {
//			o.setBackgroundResource(0);
//		}
		
		//On récupère la checkbox pour en changer l'état.
//		convertView = listChkIng.getAdapter().getView(position, convertView , listChkIng);
//		RowIngredient rowIng = (RowIngredient)convertView.getTag();
//		rowIng.setItemChecked(cb.isChecked());
//		Log.d(DisplayGestPlatPage.TAG, "    Dans le handler, rowIng "+ position +": " + (rowIng));
		//convertView.setTag(rowIng);
//	}
}
