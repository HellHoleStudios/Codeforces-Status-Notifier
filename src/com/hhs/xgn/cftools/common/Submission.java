package com.hhs.xgn.cftools.common;

public class Submission {
	public int id;
	public int contest;
	public String index;
	public String lang;
	public String verdict;
	public Submission(){
		
	}
	public Submission(int id, int contest, String index, String lang, String verdict) {
		super();
		this.id = id;
		this.contest = contest;
		this.index = index;
		this.lang = lang;
		this.verdict = verdict;
	}
	@Override
	public String toString() {
		return "Submission [id=" + id + ", contest=" + contest + ", index=" + index + ", lang=" + lang + ", verdict="
				+ verdict + "]";
	}
	
}
