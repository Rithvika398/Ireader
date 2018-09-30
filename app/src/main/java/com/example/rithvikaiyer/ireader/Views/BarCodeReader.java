package com.example.rithvikaiyer.ireader.Views;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rithvikaiyer.ireader.R;

import java.util.Locale;

/**
 * Created by Ritgvika Iyer on 25-06-2018.
 */

public class BarCodeReader extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    TextToSpeech ttsobj;
    int result;
    EditText et;
    String text;
    String contents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodereader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ttsobj= new TextToSpeech(BarCodeReader.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS)
                {
                    result=ttsobj.setLanguage(Locale.ENGLISH);
                }
                else
                {
                    Toast.makeText(BarCodeReader.this,"Feature not supported by this version",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void speakmethod(View v)
    {
        switch (v.getId()) {
            case R.id.startspeak:
                if(result==TextToSpeech.LANG_NOT_SUPPORTED|| result==TextToSpeech.LANG_MISSING_DATA)
                {
                    Toast.makeText(BarCodeReader.this,"Feature not supported by this version",Toast.LENGTH_SHORT).show();

                }
                else {
                    //text=et.getText().toString();
                    //text =getResources().getString(R.string.welcomeintro);
                    ttsobj.speak(contents, TextToSpeech.QUEUE_FLUSH, null);
                }
                break;

            case R.id.stopspeak:
                if(ttsobj!=null)
                {
                    ttsobj.stop();
                }
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ttsobj!=null)
        {
            ttsobj.stop();
        }
        ttsobj.shutdown();

    }

    public void scanBar(View v)
    {

        try{
            Intent intent=new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent,0);

        }
        catch(ActivityNotFoundException e)
        {
            showDialog(BarCodeReader.this, "No Scanner Found", "Download a Scanner?","Yes","No" ).show();
        }
    }

    public void scanQR(View v)
    {
        try{
            Intent intent=new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent,0);

        }
        catch(ActivityNotFoundException e)
        {
            showDialog(BarCodeReader.this, "No Scanner Found", "Download a Scanner?","Yes","No" ).show();
        }

    }
    private static AlertDialog showDialog(final AppCompatActivity act,CharSequence title, CharSequence message,CharSequence ButtonYes, CharSequence ButtonNo)

    {
         AlertDialog.Builder downloadDialog= new AlertDialog.Builder(act);
         downloadDialog.setTitle(title).setMessage(message).setPositiveButton(ButtonYes, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 Uri uri=Uri.parse("market://search?q=pname:"+"com.google.zxing.client.android");
                 Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                 try{
                     act.startActivity(intent);
                 }
                 catch(ActivityNotFoundException e)
                 {

                 }

             }
         }).setNegativeButton(ButtonNo, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         });
         return downloadDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode==0)
        {
            if(resultCode==RESULT_OK)
            {
                 contents=intent.getStringExtra("SCAN_RESULT");
                String format=intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText(this, "Content:"+contents+ "Format:"+format, Toast.LENGTH_LONG).show();
                Log.d("check", "-------------------------------------------------------"+contents);


            }
        }
    }
}
