package ca.ualberta.cs.shmeittodolist;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import ca.ualberta.cs.shmeitas1todolist.R;


public class CustomAdapter extends ArrayAdapter<ListItems>{
	
	

	public CustomAdapter(Context context, ArrayList<ListItems> list) {
		super(context, R.layout.custom_row2, list);
		// TODO Auto-generated constructor stub
	}
	
	static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }
	
	@Override
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater theInflater = LayoutInflater.from(getContext());
		
		View theView = theInflater.inflate(R.layout.custom_row2,parent, false );
		
		String toDos = getItem(position).getText();
		
		TextView theTextView = (TextView) theView.findViewById(R.id.textView1);
		
		theTextView.setText(toDos);
		
		CheckBox todoCheckBox = (CheckBox) theView.findViewById(R.id.todoCheckbox1);
		
		todoCheckBox.setOnCheckedChangeListener(null);
		
		todoCheckBox.setChecked(getItem(position).getChecked());
		
		todoCheckBox.setOnCheckedChangeListener(new CheckBoxListener(position));
		
		return theView;
		
	}
	
		public class CheckBoxListener implements OnCheckedChangeListener{
			int checkPosition;
			public CheckBoxListener(int position){
				checkPosition = position;
			}
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				getItem(checkPosition).setChecked(isChecked);
				
			}

		}
	
	

}
