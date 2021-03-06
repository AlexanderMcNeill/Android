package com.bit.mcnear1.languagetrainer;


public class Question {
	private String question;
	private String imageName;
	private eGender wordGender;
	
	public Question(String question, String imageName, eGender wordGender)
	{
		this.question = question;
		this.imageName = imageName;
		this.wordGender = wordGender;
	}
	
	public boolean checkAnswer(eGender userAnswer)
	{
		if(userAnswer == wordGender)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getAnswerString()
	{
		return wordGender.toString();
	}
	
	public String getImageName()
	{
		return imageName;
	}
	
	public String getQuestion()
	{
		return question;
	}
	
	public eGender getWordGender()
	{
		return wordGender;
	}
}
