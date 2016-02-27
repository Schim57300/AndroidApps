package my.Menu.App;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;
import my.Menu.Object.Ingredient;
import my.Menu.Object.Plat;
import my.Menu.Object.RowIngredient;

public class DBAdapter {

	private static String TAG = "DBAdapter";
	DatabaseHelper DBHelper;
	Context context;
	SQLiteDatabase db;
	
	/**
	 * Composition d'un plat
	 * @return  ArrayList<Ingredient> 
	 */
	private String reqCompoPlat = "SELECT c.idIngredient, i.nomIngredient, i.dtMaj, c.idPlat FROM ingredient i, composition c WHERE c.idIngredient=i.idIngredient AND idPlat=? ";
	
	/**
	 * Liste des ingr�dients avec �tat (checked/unchecked) par rapport � un plat
	 * @return ArrayList<RowIngredient>
	 */
	private String reqListIngForPlat = "SELECT i.idIngredient, i.nomIngredient, i.dtMaj, c.idPlat FROM ingredient i LEFT JOIN composition c ON (c.idIngredient=i.idIngredient AND idPlat=?) ";

	/**
	 * Liste des plats li�s � un ingr�dient
	 * @return ArrayList<Plat>
	 */
	private String reqPlatWithIng = "SELECT p.idPlat, p.nomPlat, p.dtMaj FROM plat p, composition c WHERE c.idIngredient = ? AND c.idPlat=p.idPlat ";

	
	public DBAdapter(Context context) {
		this.context = context;
		DBHelper = new DatabaseHelper(context);
	}

	public class DatabaseHelper extends SQLiteOpenHelper {

		Context context;

		public DatabaseHelper(Context context) {
			super(context, "menu", null, 1);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table ingredient (idIngredient integer primary key autoincrement, "
					+ "nomIngredient text not null, " + "dtMaj long not null, " + "cdEtat text not null" + ");");

			db.execSQL("create table plat (idPlat integer primary key autoincrement, " + "nomPlat text not null, "
					+ "dtMaj long not null, " + "cdEtat text not null" + ");");

			db.execSQL("create table composition (idCompo integer primary key autoincrement, " + "idPlat integer not null, "
					+ "idIngredient integer not null " + ");");

			createDummyIngList();
		}

		public void createDummyIngList() {
			insererUnIngredient(new Ingredient("Carottes"));
			insererUnIngredient(new Ingredient("Riz"));
			insererUnIngredient(new Ingredient("Oignons"));
			insererUnIngredient(new Ingredient("Sauce fond de veau"));
			insererUnIngredient(new Ingredient("Tomates"));
			insererUnIngredient(new Ingredient("Pommes de terre"));
			insererUnIngredient(new Ingredient("Lardons"));
			insererUnIngredient(new Ingredient("Cr�me fraiche"));
			insererUnIngredient(new Ingredient("Laitue"));
			insererUnIngredient(new Ingredient("Poivrons surgel�s"));
			insererUnIngredient(new Ingredient("L�gumes couscous surgel�s"));
			insererUnIngredient(new Ingredient("Gnocchis"));
			insererUnIngredient(new Ingredient("Lentilles"));
			insererUnIngredient(new Ingredient("Knackies"));
			Plat dummyPlat = new Plat(0, "Plat de test",null, "");
			dummyPlat.addIng(new Ingredient(1,"pipo",""));
			dummyPlat.addIng(new Ingredient(5,"pipo",""));
			Log.d(DBAdapter.TAG, "dummyPlat ajout�: "+dummyPlat);
			insererUnPlat(dummyPlat);

		}

		//TODO Implementer onUpgrade
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Toast.makeText(context, "Mise � jour de la Base de donn�es version " + oldVersion + " vers " + newVersion,
					Toast.LENGTH_SHORT).show();
			db.execSQL("DROP TABLE IF EXISTS ingredient");
			db.execSQL("DROP TABLE IF EXISTS plat");
			db.execSQL("DROP TABLE IF EXISTS composition");
			onCreate(db);
		}

	}

	public DBAdapter open() {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public void Truncate() {
		db.execSQL("DELETE FROM ingredient");
		db.execSQL("DELETE FROM plat");
		db.execSQL("DELETE FROM composition");
	}

	/**
	 * 
	 * REQUETES INGREDIENTS
	 */
	public long insererUnIngredient(Ingredient currentIng) {
		ContentValues values = new ContentValues();
		values.put("nomIngredient", currentIng.getNomIngredient());
		// TODO checker la date
		values.put("dtMaj",
				DateUtils.formatDateTime(null, SystemClock.currentThreadTimeMillis(), DateUtils.FORMAT_NUMERIC_DATE));
		values.put("cdEtat", "ACT");
		Log.d(DBAdapter.TAG, "Value pour la date :" + values.get("dtMaj") + ".");
		return db.insert("ingredient", null, values);
	}
	
	/**
	 * Supprime un ingredient de la table
	 * @param ing
	 * @return "" si ok / "list des plats associ�" si nok
	 */
	public String supprimerIngredient(Ingredient ing) {
		ArrayList<Plat> listPlats = recupererLaListeDesPlats(ing);
		Log.d(DBAdapter.TAG, "listPlats.size():" + listPlats.size() + ".");
		String result = "";
		if (listPlats.isEmpty()) {
			ContentValues values = new ContentValues();
			values.put("cdEtat", "SUP");
			db.update("ingredient", values, "idIngredient=" + ing.getIdIngredient(), null);
		} else {
			for (Plat relatedPlat : listPlats){
				result += relatedPlat.getNomPlat();
				result += "\n";
			}
			Log.d(DBAdapter.TAG, "suppression impossible.");
		}
		return result;
	}

	public void modifierIngredient(Ingredient currentIng) {
		ContentValues values = new ContentValues();
		values.put("nomIngredient", currentIng.getNomIngredient());
		//TODO dtMaj?
		db.update("ingredient", values, "idIngredient=" + currentIng.getIdIngredient(), null);
	}

	/**
	 * 
	 * @param selectedPlat : 
	 * 	Si non NULL, retourne tous les ingr�dients et leur �tat ( checked/unchecked) par rapport � ce plat
	 *  Si NULL, retourne tous les ingr�dients
	 * @param filter
	 *  Si true, ne retourne que les ingr�dients associ�s au plat pass� en param�tre 
	 * @return un Cursor avec la liste des ingr�dients
	 */
	private Cursor getListeIngredients(Plat selectedPlat, boolean filter) {
		Cursor c = null;
		if (selectedPlat==null){
			//Si pas de plat, on retourne la liste de tous les ingr�dients
			c =db.query("ingredient", new String[] { "idIngredient", "nomIngredient", "dtMaj" }, "cdEtat=\"ACT\"", null, null,
					null, "nomIngredient");
		} else if (filter){
			//On revoit la liste des ingr�dients associ�s � un plat
			c = db.rawQuery(this.reqCompoPlat, new String[]{""+selectedPlat.getIdPlat()});
		} else {
			//On renvoit la liste de tous les ingr�dients et leur �tat (checked/unchecked) par rapport au plat
			c = db.rawQuery(this.reqListIngForPlat, new String[]{""+selectedPlat.getIdPlat()});
		}
		
		return c;
	}

	/**
	 * 
	 * @param selectedPlat : 
	 * 	Si non NULL, retourne tous les ingr�dients associ�s � ce plat
	 *  Si NULL, retourne tous les ingr�dients
	 * @param filter
	 *  Si true, ne retourne que les ingr�dients associ�s au plat pass� en param�tre 
	 * @return une List<Ingredient>avec la liste des ingr�dients
	 */
	public ArrayList<Ingredient> recupererListIngredient(Plat selectedPlat, boolean filter) {
		ArrayList<Ingredient> listIng = new ArrayList<Ingredient>();
		SQLiteCursor c = (SQLiteCursor) getListeIngredients(selectedPlat, filter);

		if (c.moveToFirst()) {
			do {
				listIng.add(new Ingredient(c.getInt(0), c.getString(1), c.getString(2)));
			} while (c.moveToNext());
		}

		c.close();
		return listIng;
	}
	

	/**
	 * @param selectedPlat
	 * @return une liste de RowIngredient avec les ingredients associ�s � un plat (ArrayList<RowIngredient>)
	 */
	public ArrayList<RowIngredient> recupererListRowIngredient(Plat selectedPlat, boolean filter){
		ArrayList<RowIngredient> listRowIng = new ArrayList<RowIngredient>();
		Cursor c = this.getListeIngredients(selectedPlat, filter);
		if (c.moveToFirst()) {
			do {
				listRowIng.add(new RowIngredient(
						c.getInt(0), 
						c.getString(1), 
						c.getString(2),
						(selectedPlat!=null?c.getString(3)!=null:false)
				));
			} while (c.moveToNext());
		}
		c.close();

		return listRowIng;
	}
	


	/**
	 * 
	 * REQUETES PLATS
	 */
	public long insererUnPlat(Plat currentPlat) {

		long resultInsertPlat = 0;
		db.beginTransaction();
		try {

			ContentValues values = new ContentValues();
			values.put("nomPlat", currentPlat.getNomPlat());
			// TODO checker la date
			String dtMaj = DateUtils.formatDateTime(null, SystemClock.currentThreadTimeMillis(),
					DateUtils.FORMAT_NUMERIC_DATE);
			values.put("dtMaj", dtMaj);
			values.put("cdEtat", "ACT");
			Log.d(DBAdapter.TAG, "Value pour le date :" + values.get("dtMaj") + ".");
			resultInsertPlat = db.insert("plat", null, values);
			if (resultInsertPlat < 0)
				throw new Exception();
			currentPlat.setIdPlat(resultInsertPlat);
			currentPlat.setDtMaj(dtMaj);
			// Associer Ingr�dients au plat
			long resultInsertIng = associerIngredients(currentPlat);
			if (resultInsertIng < 0)
				throw new Exception();
			
			db.setTransactionSuccessful();
		} catch (Exception e) {

		} finally {
			db.endTransaction();
		}

		return resultInsertPlat;
	}
	
	
	private Cursor getListePlat(Ingredient currentIng) {
		
		Cursor cursor;
		if (currentIng==null){
			cursor =  db.query("plat", new String[] { "idPlat", "nomPlat", "dtMaj" }, "cdEtat=\"ACT\"", null, null, null,
					"nomPLat");
		}else {
			cursor = db.rawQuery(this.reqPlatWithIng, new String[]{""+currentIng.getIdIngredient()});
		}
		return cursor;
			
		
			
	}
	
	/**
	 * @return la liste des plats contenant l'ingredient pass� en param�tre (ArrayList<Plat>)
	 * Si le param�tre est null, on retourne tous les plats
	 * 
	 */
	public ArrayList<Plat> recupererLaListeDesPlats(Ingredient currentIng) {
		ArrayList<Plat> listPlat = new ArrayList<Plat>();
		SQLiteCursor c = (SQLiteCursor) getListePlat(currentIng);
		
		if (c.moveToFirst()) {
			do {
				Plat plat = new Plat(c.getInt(0), c.getString(1), new ArrayList<Ingredient>(),c.getString(2));
				plat.setListIng(this.recupererListIngredient(plat,true));
				listPlat.add(plat);
			} while (c.moveToNext());
		}
		c.close();
		return listPlat;
	}


	public long associerIngredients(Plat currentPlat) throws Exception {

		long resultInsert = 0;
		for (Ingredient currentIng : currentPlat.getListIng()) {
			ContentValues values = new ContentValues();
			values.put("idPlat", currentPlat.getIdPlat());
			values.put("idIngredient", currentIng.getIdIngredient());
			resultInsert = db.insert("composition", null, values);
		}
		return resultInsert;
	}
	
	public long supprimerPlat(Plat currentPlat) {
		return 0;
	}

	public void dropBDD() {
		DBHelper.onUpgrade(db, 0, 1);
	}

}
