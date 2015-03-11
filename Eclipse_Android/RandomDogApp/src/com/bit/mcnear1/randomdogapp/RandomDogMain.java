package com.bit.mcnear1.randomdogapp;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class RandomDogMain extends Activity {

	//Creating an array that holds all the different types of dog breeds
	protected String[] dogBreeds = {"Poodle", "Labrador", "Shar Pei", "NewfoundLand"};
	//Creating a text view that will be used to display the random dog breed
	protected TextView txtRandomString;
	//Creating a random object to produce all random numbers for the activity
	protected Random rGen;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_dog_main);
        
        //Initializing my random object
        rGen = new Random();
        
        //Getting the text view from the xml id
        txtRandomString = (TextView)findViewById(R.id.txtDisplay);
        
        //Generating a random dog to go into the text view
        generateRandomDog(null);	//Handing in null due to method being for an event what requires a view 
        							//for the object that sent it. This is probably bad form
    }


    protected String getRandomDogBreed()
    {
    	//Randomly generating a index with the dog breeds array
    	int dogIndex = rGen.nextInt(dogBreeds.length);
    	
    	//Returning the dog breed that matches that index
    	return dogBreeds[dogIndex];
    }
    
    public void generateRandomDog(View v)
    {
    	//Checking that the text view has been link to my code
        if(txtRandomString != null)
        {
        	//Setting the text to be a random dog breed
        	txtRandomString.setText(getRandomDogBreed());
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.random_dog_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
