package com.example.complexscreencontrols;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConfirmLessionsAlert extends DialogFragment {

	private ScreenControlsMain parentActivity;
	
	public ConfirmLessionsAlert() {}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new AlertDialog.Builder(getActivity());
		
		parentActivity = (ScreenControlsMain)getActivity();
		
		builder.setTitle("Are you sure?");
		builder.setPositiveButton("Yes", new YesButtonHandler());
		builder.setNegativeButton("No", new NoButtonHandler());
		Dialog customDialog = builder.create();
		
		return customDialog;
	}
	
	public class YesButtonHandler implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			parentActivity.setConfirmInstument(true);
		}
		
	}
	
	public class NoButtonHandler implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			parentActivity.setConfirmInstument(false);
		}
		
	}
}
