package my.Menu.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import my.Menu.App.R;
import my.Menu.Object.Plat;

public class ListPlatAdapter extends ArrayAdapter<Plat> // implements
															// OnClickListener
{
	@SuppressWarnings("unused")
	private static String TAG = "EKA.ListPlatAdapter";
	private LayoutInflater mInflater;

	public ListPlatAdapter(Context context, int resource, int to,ArrayList<Plat> listIng) {
		super(context, resource, to, listIng);
		mInflater = LayoutInflater.from(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		// The child views in each row.
		TextView textView;

		Plat plat = (Plat)this.getItem( position );  
		// Create a new row view
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_plat, null);
			textView = (TextView) convertView.findViewById(R.id.nomPlat);

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
		textView.setText(plat.getNomPlat());
		return convertView;
	}
}