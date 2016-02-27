package my.Menu.Object;

import java.util.ArrayList;

public class Plat {

	@SuppressWarnings("unused")
	private static String TAG = "EKA.Plat";
	
	private long idPlat;
	private String nomPlat;
	private ArrayList<Ingredient> listIng;
	private String dtMaj;

	public Plat(long idPlat, String nomPlat, ArrayList<Ingredient> listIng, String dtMaj){
		this.idPlat = idPlat;
		this.nomPlat = nomPlat;
		this.listIng = listIng;
		//TODO : Gérer la dtMaj
		this.dtMaj = dtMaj;
	}
	
	public long getIdPlat() {
		return idPlat;
	}
	public void setIdPlat(long idPlat) {
		this.idPlat = idPlat;
	}
	public String getNomPlat() {
		return nomPlat;
	}
	public void setNomPlat(String nomPlat) {
		this.nomPlat = nomPlat;
	}
	public ArrayList<Ingredient> getListIng() {
		return listIng;
	}
	public void setListIng(ArrayList<Ingredient> listIng) {
		this.listIng = listIng;
	}
	public void addIng(Ingredient ing) {
		if (this.listIng == null){
			this.listIng = new ArrayList<Ingredient>();
		}
		this.listIng.add(ing);
	}
	public void removeIng(Ingredient ing) {
		this.listIng.remove(ing);
	}
	public void contains(Ingredient ing) {
		this.listIng.contains(ing);
	}
	public String getDtMaj() {
		return dtMaj;
	}
	public void setDtMaj(String dtMaj) {
		this.dtMaj = dtMaj;
	}
	
	public String toString(){
		return "Plat " + idPlat + "-"+nomPlat + " (" + listIng.size() + " ingrédients associés)";
	}

	
}
