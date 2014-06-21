package de.htwdd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class WizardWelcome extends Activity
{
    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_welcome);

        Button b2 = (Button) findViewById(R.id.button2);

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent nextScreen = new Intent(WizardWelcome.this, WizardMatrikel.class);
                startActivity(nextScreen);
                finish();
            }
        });
    }
}