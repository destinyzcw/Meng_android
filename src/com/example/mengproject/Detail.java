package com.example.mengproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class Detail extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		final TextView title = (TextView) findViewById(R.id.title);
		final TextView author = (TextView) findViewById(R.id.author);
		final TextView edition = (TextView) findViewById(R.id.edition);
		final TextView publisher = (TextView) findViewById(R.id.publisher);
		
		Intent intent = getIntent();
		
		title.setText("Title : " + intent.getStringExtra("title"));
		author.setText("Author : " + intent.getStringExtra("author"));
		edition.setText("Edition : " + intent.getStringExtra("edition"));
		publisher.setText("Publisher : " + intent.getStringExtra("publisher"));
		int location = Integer.parseInt(intent.getStringExtra("location"));
		
		final ImageView hand = (ImageView) findViewById(R.id.hand);
		final ImageView shelf = (ImageView) findViewById(R.id.bookshelf);
		LayoutParams shelfparams = (LayoutParams) shelf.getLayoutParams();
		int left = location == 0 ? 0 : 300;
		int top = shelfparams.height / 2;
		LayoutParams handparams = new LayoutParams(30, 30);
//		handparams.setMargins(left, top, 0, 0);
		handparams.setMargins(100 + left, 100 + top, 0, 0);
		hand.setLayoutParams(handparams);
	}
}
