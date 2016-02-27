package my.Menu.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import my.Menu.App.R;
import my.Menu.Object.Ingredient;

public class ListIngredientAdapter extends ArrayAdapter<Ingredient> // implements
															// OnClickListener
{
	@SuppressWarnings("unused")
	private static String TAG = "EKA.ListIngredientAdapter";
	private LayoutInflater mInflater;

	public ListIngredientAdapter(Context context, int resource, int to,ArrayList<Ingredient> listIng) {
		super(context, resource, to, listIng);
		mInflater = LayoutInflater.from(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		// The child views in each row.
		TextView textView;

		Ingredient ingredient = (Ingredient)this.getItem( position );  
		// Create a new row view
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_ingredient, null);
			textView = (TextView) convertView.findViewById(R.id.nomIngredient);

			// Optimization: Tag the row with it's child views, so we don't have
			// to call findViewById() later when we reuse the row.
			convertView.setTag(textView);

		}
		// Reuse existing row view
		else {
			// Because we use a ViewHolder, we avoid having to call findViewById().
			textView = (TextView)convertView.getTag();
		}

		// Display  data
		textView.setText(ingredient.getNomIngredient());
		return convertView;
	}
}