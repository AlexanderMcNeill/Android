package bit.mcnear1_using_web_services;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Artist {
	private String name;
	private int playCount;
	private Bitmap artistImage;
	
	public Artist(String name, int playCount, Bitmap artistImage)
	{
		this.name = name;
		this.playCount = playCount;
		this.setArtistImage(artistImage);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPlayCount() {
		return playCount;
	}
	
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public Bitmap getArtistImage() {
		return artistImage;
	}

	public void setArtistImage(Bitmap artistImage) {
		this.artistImage = artistImage;
	}
}
