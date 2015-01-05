package com.thesaka.fragments_poc.ui.fragments;

import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thesaka.fragments_poc.R;
import com.thesaka.fragments_poc.utility.Logger;

public class MyDialogFragment extends DialogFragment {

	public MyDialogFragment() {
		Logger.debug(this.getClass(), "MyDialogFragment- Constructor()");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.debug(this.getClass(), "onCreate()");
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.debug(this.getClass(), "onCreateView()");
        View v = inflater.inflate(R.layout.error_dialog_fragment, container, false);
        View tv = v.findViewById(R.id.dialogTitleText);
        ((TextView)tv).setText("Attention");

        // Watch for button clicks.
        Button button;
        button = (Button)v.findViewById(R.id.dialogPositiveBtn);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Positive Button Clicked", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        
        button = (Button)v.findViewById(R.id.dialogNegativeBtn);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Negative Button Clicked", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        
        button = (Button)v.findViewById(R.id.dialogNeturalBtn);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Neutral Button Clicked", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return v;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		Logger.debug(this.getClass(), "onDismiss()");
		super.onDismiss(dialog);
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		Logger.debug(this.getClass(), "onCancel()");
		super.onCancel(dialog);
	}

}
