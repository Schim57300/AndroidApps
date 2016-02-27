package my.Menu.Object;

import android.widget.CheckBox;
import android.widget.TextView;

    
  /** Holds child views for one row. */  
  public class RowIngredientHolder {  
    
	@SuppressWarnings("unused")
	private static final String TAG = "EKA.RowIngredientHolder";
	private CheckBox checkBox ;  
    private TextView textView ;  
    
    public RowIngredientHolder() {}
    
    public RowIngredientHolder(CheckBox checkBox, TextView textView ) {  
      this.checkBox = checkBox ;  
      this.textView = textView ;
    }
    
    public CheckBox getCheckBox() {  
      return checkBox;  
    } 
    
    public void setCheckBox(CheckBox checkBox) {  
      this.checkBox = checkBox;  
    }
    
    public TextView getTextView() {  
      return textView;  
    }
    
    public void setTextView(TextView textView) {  
      this.textView = textView;  
    }
  }  