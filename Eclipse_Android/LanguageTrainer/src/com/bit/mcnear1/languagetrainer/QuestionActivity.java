package com.bit.mcnear1.languagetrainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class QuestionActivity extends Activity {
	
	public final static String SCORE = "com.bit.mcnear1.languagetrainer.SCORE";
	
	Resources resourceResolver;
	Random rand;
	
	int score = 0;
	int currentQuestion  = 0;
	List<Question> questions;
	
	ImageView questionImage;
	TextView txtQuestion;
	RadioGroup genderOptions;
	List<View> answerTabs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        
        ImageView btnNext = (ImageView)findViewById(R.id.nextBtn);
        btnNext.setOnClickListener(new NextBtnHandler());
        
        init();
        shuffleQuestions();
        displayNextQuestion();
    }
    
    public void init()
    {
    	//Initializing variables
        rand = new Random();
        resourceResolver = getResources();
        
        //Grabbing all of the elements from the xml that i will be using throughout the activity
        questionImage = (ImageView)findViewById(R.id.imgQuestion);
        txtQuestion = (TextView)findViewById(R.id.txtQuestion);
        genderOptions = (RadioGroup)findViewById(R.id.radioGroup_GenderOptions);
        
        initQuestions();
        //initResultsBar(); was unable to get this to work so just hard coded it in the xml
        initAnswersTabs();
    }
    
    //Method that gets all of the answer tabs from the xml. 
    //Answer tabs will be used to display how the user is going throughout the quiz
    public void initAnswersTabs()
    {
    	answerTabs = new ArrayList<View>();
    	
    	LinearLayout tabContainer = (LinearLayout)findViewById(R.id.resultsPanel);
    	
    	for(int i = 0; i< tabContainer.getChildCount(); i++)
    	{
    		answerTabs.add(tabContainer.getChildAt(i));
    	}
    }
    
    //Adding all the questions to the question list
    public void initQuestions()
    {
    	questions = new ArrayList<Question>();
    	
    	questions.add(new Question("Apfel : Apple", "der_apfel", eGender.Masculine));
    	questions.add(new Question("Auto : Car", "das_auto", eGender.Neutral));
    	questions.add(new Question("Baum : Tree", "der_baum", eGender.Masculine));
    	questions.add(new Question("Ente : Duck", "die_ente", eGender.Feminine));
    	questions.add(new Question("Haus : House", "das_haus", eGender.Neutral));
    	questions.add(new Question("Hexe : Witch", "die_hexe", eGender.Feminine));
    	questions.add(new Question("Kuh : Cow", "die_kuh", eGender.Feminine));
    	questions.add(new Question("Milch : Milk", "die_milch", eGender.Feminine));
    	questions.add(new Question("Schaf : Sheep", "das_schaf", eGender.Neutral));
    	questions.add(new Question("Strasse : Street", "die_strasse", eGender.Feminine));
    	questions.add(new Question("Stuhl : Chair", "der_stuhl", eGender.Masculine));
    }
    
    //Putting the questions list into a random order
    public void shuffleQuestions()
    {
    	for(int i = 0; i < questions.size() * 10; i++)
    	{
    		int indexOne = rand.nextInt(questions.size());
    		int indexTwo = rand.nextInt(questions.size());
    		
    		Question temp = questions.get(indexOne);
    		questions.set(indexOne, questions.get(indexTwo));
    		questions.set(indexTwo, temp);
    	}
    }
    
    //Not working properly
    //Method that creates the results bar that will display the users progress throughout the quiz
    public void initResultsBar()
    {
    	 LinearLayout resultsBar = (LinearLayout)findViewById(R.id.resultsPanel);
    	 
    	 //Deriving the size each tab should be from the width of the results bar and the amount of questions
    	 int tabWidth = resultsBar.getWidth() / questions.size();
    	 
    	 //Looping through each of the questions and making a matching tab
    	 for(int i = 0; i < questions.size(); i++)
    	 {
    		 View newTab = new View(this);
    		 newTab.setMinimumHeight(resultsBar.getHeight());
    		 newTab.setMinimumWidth(tabWidth);
    		 newTab.setBackgroundColor(resourceResolver.getColor(R.color.unanswered));
    		 
    		 resultsBar.addView(newTab);
    	 }
    	 
    }
    
    //Method that displays the next question to the screen
    public void displayNextQuestion()
    {
    	txtQuestion.setText(questions.get(currentQuestion).getQuestion());
    	
    	int imageID = resourceResolver.getIdentifier(questions.get(currentQuestion).getImageName(), "drawable", getPackageName());
    	questionImage.setImageResource(imageID);
    }
    
    //Method that checks if the users answer was correct
    public boolean checkAnswer()
    {
    	eGender answer = eGender.Masculine;
    	
    	int selectedRadioBtnId = genderOptions.getCheckedRadioButtonId();
    	RadioButton selectedRadioBtn = (RadioButton)findViewById(selectedRadioBtnId);
    	
    	String answerString = selectedRadioBtn.getText().toString();
    	
    	if(answerString.contains("Der"))
    	{
    		answer = eGender.Masculine;
    	}
    	else if(answerString.contains("Das"))
    	{
    		answer = eGender.Neutral;
    	}
    	else if(answerString.contains("Die"))
    	{
    		answer = eGender.Feminine;
    	}
    	
    	return questions.get(currentQuestion).checkAnswer(answer);
    }
    
    //Method that displays the result of the users answer
    public void displayResult(String result)
    {
    	AlertDialog.Builder resultDialog  = new AlertDialog.Builder(this);
    	resultDialog.setMessage(result);
    	resultDialog.setPositiveButton("Next Question", new ResultDialogHandler());
    	resultDialog.setCancelable(false);
    	resultDialog.create().show();
    }
    
    
    //Class that handles all of the events of the next button
	public class NextBtnHandler implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			
			boolean correct = checkAnswer();
			
			if(correct)
			{
				score++;
				answerTabs.get(currentQuestion).setBackgroundColor(resourceResolver.getColor(R.color.correctAnswer));
				displayResult("Correct!!!");
			}
			else
			{
				answerTabs.get(currentQuestion).setBackgroundColor(resourceResolver.getColor(R.color.incorrectAnswer));
				String correctAnswer = questions.get(currentQuestion).getAnswerString();
				displayResult("Incorrect :(. The correct answer was " + correctAnswer);
			}
			
		}
		
	}
	
	//Method that handles all the evets of the result dialog
	public class ResultDialogHandler implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			currentQuestion++;
			
			//Checking if the quiz is over
			if(currentQuestion < questions.size())
			{
				displayNextQuestion();
			}
			else
			{
				//If the quiz is over moving through to the results activity
				Intent startQuizIntent = new Intent(QuestionActivity.this, Results.class);
				startQuizIntent.putExtra(SCORE, score + "/" + questions.size());
				startActivity(startQuizIntent);
			}
		}
		
	}
}
