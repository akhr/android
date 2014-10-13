package com.thesaka.ralarm.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class CustomNumberPicker extends android.widget.NumberPicker {

	public CustomNumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void addView(View child) {
		super.addView(child);
		updateView(child);
	}

	@Override
	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		params.height = (int) (params.height * 1.5);
		super.addView(child, index, params);
		updateView(child);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		params.height = (int) (params.height * 1.5);
		super.addView(child, params);
		updateView(child);
	}

	private void updateView(View view) {
		if(view instanceof EditText){
			((EditText) view).setTextSize(60);
			((EditText) view).setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
			((EditText) view).setTextColor(Color.parseColor("#FFFFFF"));
		}
	}

}
