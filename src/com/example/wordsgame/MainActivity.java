package com.example.wordsgame;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.R.color;
import android.R.string;
import android.os.Bundle;
import android.os.Environment;
import android.provider.UserDictionary.Words;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	static final int CountOfLongestWord = 42;
	static final int LengthOfLine = 7;
	Random NoOfLetters = new Random();
	Random ForColor = new Random();
	int Score = 0;
	int WidthOfLetters = 0;
	int CountOfLettersInWord = 0;
	int[] PositionOfLetters = new int[CountOfLongestWord];
	String LettersBuffer = null;
	String TextString = "";
	String Word = "";
	ArrayList<String> WordRecord = new ArrayList<String>();
	FileReader FR = null;
	InputStream InputStreamOfWordlists=null;
	BufferedReader Buffer = null;
	Toast ToastOfShowingResultAndScore=null;

	private TextView[] Letters = new TextView[CountOfLongestWord];
	private TextView[] LettersOfWord = new TextView[CountOfLongestWord];
	private Button Check = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// setContentView(R.layout.processing);
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		WidthOfLetters = (int) (DM.xdpi * DM.density / LengthOfLine);
		Check = (Button) findViewById(R.id.Check);
		for (int i = 0; i < CountOfLongestWord; i++) {
			Letters[i] = (TextView) findViewById(R.id.letter1 + i);
			LettersOfWord[i] = (TextView) findViewById(R.id.letterOfWord1 + i);
			Letters[i].setWidth(WidthOfLetters);
			LettersOfWord[i].setWidth(WidthOfLetters);
			TextString = (char) ('A' + NoOfLetters.nextInt(26)) + "";
			Letters[i].setText(TextString);
			LettersOfWord[i].setText("");
			PositionOfLetters[i] = -1;
			Letters[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					for (int i = 0; i < CountOfLongestWord; i++)
						if (Letters[i].getId() == v.getId()) {
							if (Letters[i].getText().toString() == "")
								return;
							Log.d("TEST", Letters[i].getText().toString());
							LettersOfWord[CountOfLettersInWord]
									.setText(Letters[i].getText().toString());
							PositionOfLetters[CountOfLettersInWord] = i;
							Letters[i].setText("");
							CountOfLettersInWord++;
							break;
						}
				}
			});
			LettersOfWord[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for (int i = 0; i < CountOfLettersInWord; i++) {
						if (LettersOfWord[i].getId() == v.getId()) {
							if (LettersOfWord[i].getText().toString() == "")
								return;
							Letters[PositionOfLetters[i]]
									.setText(LettersOfWord[i].getText());
							PositionOfLetters[i] = -1;
							LettersOfWord[i].setText("");
							ArrangeLettersOfWord();
							CountOfLettersInWord--;
							break;
						}
					}
				}
			});
			Letters[i].setBackgroundColor(Color.rgb(ForColor.nextInt(256),
					ForColor.nextInt(256), ForColor.nextInt(256)));
			LettersOfWord[i].setBackgroundColor(Color.rgb(
					ForColor.nextInt(256), ForColor.nextInt(256),
					ForColor.nextInt(256)));
		}
		Check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (LettersOfWord[0].getText().toString() == "")
					return;
				GatherAllLetters();
				if (WordRecord.indexOf(Word.toString().toLowerCase()) != -1) {
					Toast.makeText(getApplicationContext(),
							"This Word has been used", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				String FileName = "wordlists/";
				try {
					InputStreamOfWordlists=getResources().getAssets().open(FileName
							+ LettersOfWord[0].getText().toString().toLowerCase() + "words.txt");
					InputStreamReader ReaderInputStreamOfWordlists=new InputStreamReader(InputStreamOfWordlists);
					Buffer = new BufferedReader(ReaderInputStreamOfWordlists);
					String StringLine = Buffer.readLine();
					int i=0;
					while (StringLine!=null&&StringLine.compareToIgnoreCase(Word.toLowerCase().toString())<=0) {
						Log.v("Comparing Result:", ""+StringLine.compareToIgnoreCase(Word.toLowerCase().toString()));
						Log.v("Number of Wordlists", ""+i);
						Log.v("The word:", ""+StringLine);
						Log.v("Target:", Word.toLowerCase());
						i++;
						if (StringLine != null&&StringLine.toString().equalsIgnoreCase(Word.toLowerCase().toString())) {
							if(Word.length()%7!=0)Score += (Word.length()/7) * 10+1;
							else Score += (Word.length()/7) * 10+0;
							WordRecord.add(StringLine.toString());
							Clear();
							ToastOfShowingResultAndScore=Toast.makeText(getApplicationContext(),
									"Right Answer!", Toast.LENGTH_SHORT);
							ToastOfShowingResultAndScore.show();
							ToastOfShowingResultAndScore=Toast.makeText(getApplicationContext(),
									"Score:"+Score, Toast.LENGTH_SHORT);
							ToastOfShowingResultAndScore.setGravity(Gravity.CENTER, 0, 0);;
							ToastOfShowingResultAndScore.show();
							return;
						}
						StringLine = Buffer.readLine();
					}
					Toast.makeText(getApplicationContext(),
							"Your Answer is Wrong and Game is Over!!!",
							Toast.LENGTH_SHORT).show();
					Lose();
					//Clear();
					//Score = 0;
				} catch (Exception e) {
					// TODO: handle exception
					Log.v("What is error!", "" + e.toString());
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void ArrangeLettersOfWord() {
		String[] Word = new String[CountOfLettersInWord];
		int[] SavePosition = new int[CountOfLettersInWord];
		for (int i = 0; i < CountOfLettersInWord; i++) {
			Word[i] = "";
			SavePosition[i] = -1;
		}
		int CountOfWord = 0;
		int CountOfSavePosition = 0;
		for (int i = 0; i < CountOfLettersInWord; i++) {
			if (LettersOfWord[i].getText().toString() != "") {
				Word[CountOfWord] = LettersOfWord[i].getText().toString();
				CountOfWord++;
			}
			if (PositionOfLetters[i] != -1) {
				SavePosition[CountOfSavePosition] = PositionOfLetters[i];
				CountOfSavePosition++;
			}
		}
		for (int i = 0; i < CountOfLettersInWord; i++) {
			LettersOfWord[i].setText(Word[i]);
			PositionOfLetters[i] = SavePosition[i];
		}

	}

	public void GatherAllLetters() {
		Word = "";
		for (int i = 0; i < CountOfLettersInWord; i++) {
			Word += LettersOfWord[i].getText().toString();
		}
	}

	public void Clear() {
		for (int i = 0; i < CountOfLongestWord; i++) {
			TextString = (char) ('A' + NoOfLetters.nextInt(26)) + "";
			Letters[i].setText(TextString);
			LettersOfWord[i].setText("");
			PositionOfLetters[i] = -1;
			CountOfLettersInWord = 0;
		}
	}
	
	public void Lose(){
		Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ResultInterface.class);
        intent.putExtra("Score",Score);
        startActivity(intent);
	}

}
