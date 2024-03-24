package com.example.weather_app;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
	@JsonProperty("coord")
	private Coordinate coordinate;
	private Weather[] weather;
	private Main main;
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	public Weather[] getWeather() {
		return weather;
	}
	public void setWeather(Weather[] weather) {
		this.weather = weather;
	}
	public Main getMain() {
		return main;
	}
	public void setMain(Main main) {
		this.main = main;
	}}