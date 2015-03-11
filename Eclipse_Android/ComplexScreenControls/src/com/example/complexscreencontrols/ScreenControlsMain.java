package com.example.complexscreencontrols;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;


public class ScreenControlsMain extends Activity {

	protected TextView txtOutput;
	protected RadioGroup instumentSelect;
	protected String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    protected Spinner monthSpinner;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_controls_main);
        
        txtOutput = (TextView)findViewById(R.id.txtOutput);
        instumentSelect = (RadioGroup)findViewById(R.id.radioGroup_Instruments);
        monthSpinner = (Spinner)findViewById(R.id.spinnerMonth);
        
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, months);
        monthSpinner.setAdapter(monthAdapter);
        
        Button submitButton = (Button)findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(new SubmitBtnHandler());
    }


    public class SubmitBtnHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) {
			String output = "";
			
			//Getting the selected radio button
			int checkedRadioBtnID = instumentSelect.getCheckedRadioButtonId();
			RadioButton checkedRadioButton = (RadioButton)findViewById(checkedRadioBtnID);
			
			String selectedMonth = (String)monthSpinner.getSelectedItem();
			
			output += "You have seleceted the " + checkedRadioButton.getText() + " to play. Lessions will start in " + selectedMonth;
			txtOutput.setText(output);
		}
    	
    }
    public class RadioGroupHandler implements OnCheckedChangeListener
    {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			RadioButton checkedRadioButton = (RadioButton)findViewById(checkedId);
			
			txtOutput.setText(checkedRadioButton.getText().toString());
			
		}
    	
    }
}
