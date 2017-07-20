package com.softwaremagico.tm.character;

public enum OccultismType {
	PSI("psi", "urge"),

	THEURGY("theurgy", "hubris");

	private String tag;
	private String darkSideTag;

	private OccultismType(String tag, String darkSideTag) {
		this.tag = tag;
		this.darkSideTag = darkSideTag;
	}

	public String getTag() {
		return tag;
	}

	public String getDarkSideTag() {
		return darkSideTag;
	}

}
