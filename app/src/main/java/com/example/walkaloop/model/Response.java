package com.example.walkaloop.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response{
	@SerializedName("Place")
	private String Place;
	@SerializedName("Latitude")
	private String Latitude;
	@SerializedName("Longitude")
	private String Longitude;
	@SerializedName("Isdefault")
	private int Isdefault;
	@SerializedName("SubPlace")
	private List<SubPlaceItem> SubPlace;
	@SerializedName("NoofPlaces")
	private int NoofPlaces;

	public String getPlace() {
		return Place;
	}

	public void setPlace(String place) {
		Place = place;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public int getIsdefault() {
		return Isdefault;
	}

	public void setIsdefault(int isdefault) {
		Isdefault = isdefault;
	}

	public List<SubPlaceItem> getSubPlace() {
		return SubPlace;
	}

	public void setSubPlace(List<SubPlaceItem> subPlace) {
		SubPlace = subPlace;
	}

	public int getNoofPlaces() {
		return NoofPlaces;
	}

	public void setNoofPlaces(int noofPlaces) {
		NoofPlaces = noofPlaces;
	}
}