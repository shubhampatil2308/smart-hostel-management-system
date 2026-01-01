package com.SmartHostel.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mess_menu")
public class MessMenu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;
	private String dayOfWeek;
	private String breakfast;
	private String lunch;
	private String dinner;
	private String snacks;
	private Double messFee;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}

	public String getLunch() {
		return lunch;
	}

	public void setLunch(String lunch) {
		this.lunch = lunch;
	}

	public String getDinner() {
		return dinner;
	}

	public void setDinner(String dinner) {
		this.dinner = dinner;
	}

	public String getSnacks() {
		return snacks;
	}

	public void setSnacks(String snacks) {
		this.snacks = snacks;
	}

	public Double getMessFee() {
		return messFee;
	}

	public void setMessFee(Double messFee) {
		this.messFee = messFee;
	}
}

