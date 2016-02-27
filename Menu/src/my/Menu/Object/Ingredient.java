package my.Menu.Object;


public class Ingredient {

	@SuppressWarnings("unused")
	private static String TAG = "EKA.Ingredient";
	private String nomIngredient;
	private long idIngredient;
	private String dtMaj;
	
	public Ingredient(String nomIng) {
		this.idIngredient = 0;
		this.nomIngredient = nomIng;
	}
	
	public Ingredient(long aIdIng, String aNomIng, String dtMaj) {
		this.idIngredient = aIdIng;
		this.nomIngredient = aNomIng;
		this.dtMaj = dtMaj;
	}
	
	public String getNomIngredient() {
		return nomIngredient;
	}
	
	public void setNomIngredient(String nomIngrdient) {
		this.nomIngredient = nomIngrdient;
	}
	
	public long getIdIngredient() {
		return idIngredient;
	}
	
	public void setIdIngredient(long idIngredient) {
		this.idIngredient = idIngredient;
	}
	
	public String getDtMaj() {
		return dtMaj;
	}

	public void setDtMaj(String dtMaj) {
		this.dtMaj = dtMaj;
	}

	public String toString() {
		return "Ingredient " + idIngredient + "-"+nomIngredient + "/"+dtMaj;
	}
}
