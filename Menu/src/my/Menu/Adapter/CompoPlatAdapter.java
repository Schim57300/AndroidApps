package my.Menu.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import my.Menu.App.DisplayGestPlatPage;
import my.Menu.App.R;
import my.Menu.Object.RowIngredient;
import my.Menu.Object.RowIngredientHolder;

public class CompoPlatAdapter extends ArrayAdapter<RowIngredient> // implements
															// OnClickListener
{
	
	@SuppressWarnings("unused")
	private static String TAG = "EKA.CompoPlatAdapter";
	private LayoutInflater mInflater;
	private int mMode;

	public CompoPlatAdapter(Context context, int resource, int to, ArrayList<RowIngredient> listRowIng, int mode) {
		super(context, resource, to, listRowIng);
		mInflater = LayoutInflater.from(context);
		mMode = mode;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		// The child views in each row.
		CheckBox checkBox;
		TextView textView;

		RowIngredient rowIngredient = (RowIngredient)this.getItem( position );  
		// Create a new row view
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_ing_alertdialog_plat, null);
			checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxIng);
			textView = (TextView) convertView.findViewById(R.id.nomIng);

			// Optimization: Tag the row with it's child views, so we don't have
			// to call findViewById() later when we reuse the row.
			convertView.setTag(new RowIngredientHolder(checkBox, textView));

			// If CheckBox is toggled, update the rowIngredient it is tagged with.
			checkBox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					RowIngredient rowIngredient = (RowIngredient) cb.getTag();
					rowIngredient.setChecked(cb.isChecked());
				}
			});
		}
		// Reuse existing row view
		else {
			// Because we use a ViewHolder, we avoid having to call findViewById().
			RowIngredientHolder viewHolder = (RowIngredientHolder) convertView.getTag();
			checkBox = viewHolder.getCheckBox();
			textView = viewHolder.getTextView();
		}

		// Tag the CheckBox with the RowIngredient it is displaying, so that we can
		// access the item in onClick() when the CheckBox is toggled.
		checkBox.setTag(rowIngredient);
		// Display  data
		checkBox.setChecked(rowIngredient.isChecked());
		if (mMode!=DisplayGestPlatPage.MODIFIER &&
				mMode!=DisplayGestPlatPage.AJOUT)
			checkBox.setVisibility(View.INVISIBLE);
		textView.setText(rowIngredient.getNomIngredient());
		return convertView;
	}
}