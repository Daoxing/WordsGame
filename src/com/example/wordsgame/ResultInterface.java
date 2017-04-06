package com.example.wordsgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultInterface extends Activity {
	
	private Intent DataFromMainActivity=new Intent();
	private TextView ScoreOfTextView=null;
	private Button TryAgain=null;
	private Button Exit=null;
	private int ScoreOfNumber=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultinterface);
		ScoreOfTextView=(TextView)findViewById(R.id.Score);
		TryAgain=(Button)findViewById(R.id.TryAgain);
		Exit=(Button)findViewById(R.id.Exit);
		DataFromMainActivity=getIntent();
		ScoreOfNumber=DataFromMainActivity.getIntExtra("Score",ScoreOfNumber);
		ScoreOfTextView.setText("Score:"+ScoreOfNumber);
		TryAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.setClass(getApplicationContext(),MainActivity.class);
		        startActivity(intent);
			}
		});
		Exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
				java.lang.System.exit(0);
			}
		});
		
	}

}
