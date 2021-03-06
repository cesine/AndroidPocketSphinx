package ca.ilanguage.labs.pocketsphinx.ui;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import ca.ilanguage.labs.pocketsphinx.preference.PreferenceConstants;
import ca.ilanguage.labs.pocketsphinx.R;

public class TestPocketSphinxAndAndroidASR extends Activity{
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    String mAudioFile;
	Context mContext;
	Boolean mSpeechRecognitionOkay;
	Boolean mUsePocektSphinxASR;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transparent_activity);
		
		//mAudioFile = audiofilename;
		SharedPreferences prefs = getSharedPreferences(
				PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		mUsePocektSphinxASR = prefs.getBoolean(PreferenceConstants.PREFERENCE_USE_POCKETSPHINX_ASR, false);
		if (mUsePocektSphinxASR){
			Toast.makeText(TestPocketSphinxAndAndroidASR.this, "Would be working offline, using PocketSphinx Speech recognizer. However it is not yet tied into the system and implemented and registered as a Speech Recognizer.", Toast.LENGTH_LONG).show();
     	}else{
     		Toast.makeText(TestPocketSphinxAndAndroidASR.this, "Working online, Using system speech recognizer (Google speech recognition server). ", Toast.LENGTH_LONG).show();
        }
		PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
        	Toast.makeText(TestPocketSphinxAndAndroidASR.this, "Speech recognizer is present. ", Toast.LENGTH_LONG).show();
        	mSpeechRecognitionOkay = true;
            //speakButton.setOnClickListener(this);
        } else {
        	Toast.makeText(TestPocketSphinxAndAndroidASR.this, "Speech recognizer is not present. Taking you to the market and install it. ", Toast.LENGTH_LONG).show();
        	Intent goToMarket = new Intent(Intent.ACTION_VIEW)
	            .setData(Uri.parse("market://details?id=com.google.android.voicesearch"));
	        startActivity(goToMarket);
	        mSpeechRecognitionOkay = false;
            //speakButton.setEnabled(false);
            //speakButton.setText("Recognizer not present");
        }
		
		startVoiceRecognitionActivity();
	
	}
	
	private void startVoiceRecognitionActivity() {
		if (mSpeechRecognitionOkay){
	        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "no prompt");
	        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		}else{
			finish();
		}
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(TestPocketSphinxAndAndroidASR.this, "Possible recognitions: "+matches.toString(), Toast.LENGTH_LONG).show();

//            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//                    matches));
            finish();
        }
        
        super.onActivityResult(requestCode, resultCode, data);
        return;
    }
}
