package de.htwdd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.htwdd.R;


public class Wizard1 extends Activity
{
    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard);

        // Button b1=(Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);


//        b1.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				Intent nextScreen = new Intent(getApplicationContext(), Heute.class);            
//	            startActivity(nextScreen);
//			}
//		});        

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent nextScreen = new Intent(Wizard1.this, Wizard3.class);
                startActivity(nextScreen);
                finish();


            }
        });


    }


}