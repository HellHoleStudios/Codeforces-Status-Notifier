package com.hhs.xgn.cftools.acn;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hhs.xgn.cftools.common.FullSubmission;
import com.hhs.xgn.cftools.common.InfoUtil;
import com.hhs.xgn.cftools.common.Submission;
import com.hhs.xgn.cftools.common.WebPageSource;

public class SubmitNotice {
	
	public static void main(String[] args) throws Exception{
		
		sn ss=new sn();
		ss.solve(args);
	}
}

class sn{
	
	String[] handles;
	
	Map<String,Set<FullSubmission>> subm=new HashMap<String, Set<FullSubmission>>();
	
	long startTime,td;
	
	InfoUtil iu=new InfoUtil();
	public void solve(String[] args) throws Exception{
		
		try{
			startTime=System.currentTimeMillis();
			System.out.println("Starting time:"+startTime);
			
		if(args.length==0){
			System.out.println("oo");
			Scanner s=new Scanner(System.in);
			System.out.println("Input the user handle u want to watch. Use space between names");
			String raw=s.nextLine();
			handles=raw.split(" ");
			System.out.println("Time Between Watching in seconds:");
			td=s.nextLong();
			td*=1000;
			s.close();
		}else{
			handles=args[0].split(" ");
			td=Long.parseLong(args[1]);
			td*=1000;
		}
		
		iu.show("Running", "Welcome to use Submit Noticer!");
		
		
		
		
		while(true){
			Thread.sleep(td);
			
			System.out.println("Start checking.");
			for(int i=0;i<handles.length;i++){
				run4(handles[i]);
			}
			System.out.println("End checking.");
		}
		
		}catch(Exception e){
			iu.show("Error!","Error occurred:"+e+"\nProgram will exit");
			System.exit(1);
		}
	}
	
	public void run4(String handle) throws Exception{
		
		System.out.println("User:"+handle);
		//System.out.println(subm.get(handle));
		
		String s=WebPageSource.get("http://codeforces.com/api/user.status?handle=" + handle+"&count=10");
		
		JsonParser jp = new JsonParser();

		JsonObject root = (JsonObject) jp.parse(s);

		String statue = root.get("status").getAsString();

		if (statue.equals("OK") == false) {
			System.out.println("Get "+handle+" Failed");
			return;
		}

		JsonArray ja=root.get("result").getAsJsonArray();
		
		for(int i=0;i<ja.size();i++){
			JsonObject sub=ja.get(i).getAsJsonObject();
			
			FullSubmission fs=new FullSubmission(sub);
			
			System.out.println(fs);
			
			if(fs.verdict.equals("TESTING")){
				System.out.println("Exit because of testing");
				break;
			}
			
			
			if(fs.creationTimeSeconds*1000l<startTime){
				System.out.println("Exit because of too old");
				System.out.println(fs.creationTimeSeconds*1000l+" <> "+startTime);
				break;
			}
			
			if(subm.getOrDefault(handle,new HashSet<FullSubmission>()).contains(fs)){
				System.out.println("Exit because of visited");
				break;
			}
			
			if(subm.get(handle)==null){
				subm.put(handle, new HashSet<FullSubmission>());
			}
			
			
			subm.get(handle).add(fs);
			iu.show(handle+"'s Submission!", handle+" got "+getV(fs.verdict,fs.passedTestCount)+" on " +fs.contestId+fs.problem.index+":"+fs.problem.name+" just now!","https://codeforces.com/contest/"+fs.contestId+"/submission/"+fs.id);
		}
		
	}

	private String getV(String verdict, int passedTestCount) {
		if(verdict.equals("OK")){
			return "Accepted";
		}
		if(verdict.equals("COMPILATION_ERROR") ){
			return "Compilation Error";
		}
		return verdict+" on test "+(passedTestCount+1);
	}
}
