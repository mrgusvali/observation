package com.mv.observe.db.model;

import java.io.Serializable;

import net.vesilik.fakeobservationgen.domain.MsgLevel;

public class FilterCriteria implements Serializable {
	private static final long serialVersionUID = 1L;
	int ageMinutes;
	MsgLevel level;
	
	@Override
	public String toString() {
		return String.format("%s, recent %d minutes", level, ageMinutes);
	}

	public int getAgeMinutes() {
		return ageMinutes;
	}

	public void setAgeMinutes(int ageMinutes) {
		this.ageMinutes = ageMinutes;
	}

	public MsgLevel getLevel() {
		return level;
	}

	public void setLevel(MsgLevel level) {
		this.level = level;
	}
	
	
}
