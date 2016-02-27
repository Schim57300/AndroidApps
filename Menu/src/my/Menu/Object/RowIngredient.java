package my.Menu.Object;

public class RowIngredient {
	
	@SuppressWarnings("unused")
	private static String TAG = "EKA.RowIngredient";
	private Ingredient ingredient;
	private boolean checked = false;


	public void setChecked(boolean abChecked){
		this.checked = abChecked;
	}
	
	public void toggleChecked() {
		this.checked = ! this.checked;
	}

	public boolean isChecked() {
		return this.checked;
	}
	
	public RowIngredient(Ingredient ing, boolean isChecked) {
		this.ingredient = ing;
		this.checked = isChecked;
	}
	public RowIngredient(int idIng, String nomIng, String dtMajIng, boolean isChecked) {
		this.ingredient = new Ingredient(idIng,nomIng,dtMajIng);
		this.checked = isChecked;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	
	public String toString(){
		return "RowIngredient : " + this.ingredient + " " + this.checked;
	}
	
	public String getNomIngredient() {
		return this.ingredient.getNomIngredient();
	}
}
