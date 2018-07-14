package com.hhs.xgn.cftools.uc;

import java.util.*;
import java.util.Map.Entry;

import com.google.gson.*;
import com.hhs.xgn.cftools.common.FullProblem;
import com.hhs.xgn.cftools.common.FullSubmission;
import com.hhs.xgn.cftools.common.WebPageSource;

/*
Submission

Represents a submission.
Field	Description
id	Integer.
contestId	Integer. Can be absent.
creationTimeSeconds	Integer. Time, when submission was created, in unix-format.
relativeTimeSeconds	Integer. Number of seconds, passed after the start of the contest (or a virtual start for virtual parties), before the submission.
problem	Problem object.
author	Party object.
programmingLanguage	String.
verdict	Enum: FAILED, OK, PARTIAL, COMPILATION_ERROR, RUNTIME_ERROR, WRONG_ANSWER, PRESENTATION_ERROR, TIME_LIMIT_EXCEEDED, MEMORY_LIMIT_EXCEEDED, IDLENESS_LIMIT_EXCEEDED, SECURITY_VIOLATED, CRASHED, INPUT_PREPARATION_CRASHED, CHALLENGED, SKIPPED, TESTING, REJECTED. Can be absent.
testset	Enum: SAMPLES, PRETESTS, TESTS, CHALLENGES, TESTS1, ..., TESTS10. Testset used for judging the submission.
passedTestCount	Integer. Number of passed tests.
timeConsumedMillis	Integer. Maximum time in milliseconds, consumed by solution for one test.
memoryConsumedBytes	Integer. Maximum memory in bytes, consumed by solution for one test.

Problem

Represents a problem.
Field	Description
contestId	Integer. Can be absent. Id of the contest, containing the problem.
problemsetName	String. Can be absent. Short name of the problemset the problem belongs to.
index	String. Usually a letter of a letter, followed by a digit, that represent a problem index in a contest.
name	String. Localized.
type	Enum: PROGRAMMING, QUESTION.
points	Floating point number. Can be absent. Maximum ammount of points for the problem.
tags	String list. Problem tags.
 */
public class UserCollect {
	public static void main(String[] args) throws Exception {
		UserCollecter uc=new UserCollecter();
		uc.run();
		
	}

}


class UserCollecter{
	String handle;
	
	List<FullSubmission> subs=new ArrayList<FullSubmission>();
	
	Map<String,Integer> lang=new HashMap<String,Integer>();
	Map<String,Integer> verd=new HashMap<String,Integer>();
	Map<String,Integer> tagc=new HashMap<String,Integer>();
	Map<String,Integer> indx=new HashMap<String,Integer>();
	Set<FullProblem> prob=new HashSet<FullProblem>();
	int latest;
	
	long totTimeCost=0,totMemCost=0;
	
	void run() throws Exception{

		
		Scanner s=new Scanner(System.in);
		
		System.out.println("Input user handle:");
		
		handle=s.nextLine();
		s.close();
		
		String source=WebPageSource.get("http://codeforces.com/api/user.status?handle=" + handle);
		
		JsonParser jp=new JsonParser();
		JsonObject jo=(JsonObject) jp.parse(source);
		
		if(jo.get("status").getAsString().equals("OK")==false){
			throw new Exception(jo.get("status").getAsString());
		}
		
		JsonArray ja=jo.get("result").getAsJsonArray();
		
		for(int i=0;i<ja.size();i++){
			JsonObject sub=ja.get(i).getAsJsonObject();
			
			subs.add(new FullSubmission(sub));
		}
		
		//Get Language Usage
		for(FullSubmission fs:subs){
			int v=lang.getOrDefault(fs.language, 0);
			lang.put(fs.language, v+1);
		}
		
		
		//Get latest submit
		latest=0;
		for(FullSubmission fs:subs){
			latest=Math.max(latest, fs.creationTimeSeconds);
		}
		
		//Status Count
		for(FullSubmission fs:subs){
			int v=verd.getOrDefault(fs.verdict, 0);
			verd.put(fs.verdict, v+1);
		}
		
		//Tags count & Problems Count
		for(FullSubmission fs:subs){
			
			if(prob.contains(fs.problem)==false){
				prob.add(fs.problem);
				for(int i=0;i<fs.problem.tags.size();i++){
					int v=tagc.getOrDefault(fs.problem.tags.get(i).getAsString(), 0);
					tagc.put(fs.problem.tags.get(i).getAsString(), v+1);
				}
			}
			
		}
		
		//Index count
		for(FullSubmission fs:subs){
			int v=indx.getOrDefault(fs.problem.index, 0);
			indx.put(fs.problem.index, v+1);
		}
		
		//Memory cost
		
		for(FullSubmission fs:subs){
			totTimeCost+=fs.timeConsumedMillis*fs.passedTestCount;
			totMemCost+=fs.memoryConsumedBytes*fs.passedTestCount;
		}
		
		//Output
		output();
	}
	
	void output(){
		System.out.println("User:"+handle);
		System.out.println("********************************");
		System.out.println("Solved problems: ("+prob.size()+" in total)");
		for(FullProblem fp:prob){
			System.out.println(fp.contestId+fp.index+":"+fp.name);
		}
		
		System.out.println("********************************");
		
		List<Entry<String,Integer>> tmp1=new ArrayList<Entry<String,Integer>>();
		for(Entry<String,Integer> e:tagc.entrySet()){
			tmp1.add(e);
		}
		
		tmp1.sort(new Comparator<Entry<String,Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				if(o1.getValue()>o2.getValue()) return -1;
				if(o1.getValue()<o2.getValue()) return 1;
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		System.out.println("Tag count:");
		for(Entry<String,Integer> e:tmp1){
			System.out.println(e.getKey()+" * "+e.getValue());
		}
		
		System.out.println("********************************");
		
		List<Entry<String,Integer>> tmp2=new ArrayList<Entry<String,Integer>>();
		for(Entry<String,Integer> e:verd.entrySet()){
			tmp2.add(e);
		}
		
		tmp2.sort(new Comparator<Entry<String,Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				if(o1.getValue()>o2.getValue()) return -1;
				if(o1.getValue()<o2.getValue()) return 1;
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		System.out.println("Status Count:  ("+subs.size()+" submissions)");
		for(Entry<String,Integer> e:tmp2){
			System.out.println(e.getKey()+" * "+e.getValue());
		}
		
		System.out.println("********************************");
		
		List<Entry<String,Integer>> tmp3=new ArrayList<Entry<String,Integer>>();
		for(Entry<String,Integer> e:lang.entrySet()){
			tmp3.add(e);
		}
		
		tmp3.sort(new Comparator<Entry<String,Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				if(o1.getValue()>o2.getValue()) return -1;
				if(o1.getValue()<o2.getValue()) return 1;
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		System.out.println("Language Count:");
		for(Entry<String,Integer> e:tmp3){
			System.out.println(e.getKey()+" * "+e.getValue());
		}
		
		System.out.println("********************************");
		System.out.println("Index Count:");
		for(Entry<String,Integer> e:indx.entrySet()){
			System.out.println(e.getKey()+" * "+e.getValue());
		}
		System.out.println("********************************");
		
		
		System.out.println("Total (maybe) cost CF server:"+totTimeCost/1000.0f+" seconds");
		System.out.println("Total (maybe) cost CF server:"+totMemCost/1024.0f/1024.0f+" MB");
		System.out.println("Last Submission:"+new Date(latest*1000l));
	}
}