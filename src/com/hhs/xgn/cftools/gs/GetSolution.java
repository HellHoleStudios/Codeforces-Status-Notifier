package com.hhs.xgn.cftools.gs;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hhs.xgn.cftools.common.Submission;
import com.hhs.xgn.cftools.common.WebPageSource;

import java.io.File;
import java.io.PrintWriter;

public class GetSolution {

	public static void main(String[] args) throws Exception {

		try {
			Scanner s = new Scanner(System.in);
			System.out.println("Input the user handle:");
			String user = s.nextLine();
			System.out.print("AC only? (y/n)");
			String ac = s.nextLine();

			s.close();

			boolean aco = (ac.equalsIgnoreCase("y") ? true : false);

			System.out.println("Please wait... Getting Resources");

			String gt = WebPageSource.get("http://codeforces.com/api/user.status?handle=" + user);

			System.out.println("Parsing JSON...");

			JsonParser jp = new JsonParser();

			JsonObject root = (JsonObject) jp.parse(gt);

			String statue = root.get("status").getAsString();

			if (statue.equals("OK") == false) {
				fail(statue, root.get("comment").getAsString());
			}

			List<Submission> all=new ArrayList<Submission>();
			
			JsonArray result=root.get("result").getAsJsonArray();
			
			for(int i=0;i<result.size();i++){
				JsonObject submission=result.get(i).getAsJsonObject();
				JsonObject problem=submission.get("problem").getAsJsonObject();
				
				Submission sb=new Submission(submission.get("id").getAsInt(), problem.get("contestId").getAsInt(), problem.get("index").getAsString(), submission.get("programmingLanguage").getAsString(), submission.get("verdict").getAsString());
				
				all.add(sb);
			}
			
			System.out.println(all);
			
			System.out.println("Downloading each solutions");
			
			for(Submission sb:all){
				if(!aco || sb.verdict.equals("OK")){
					Thread t=new DownloadThread(sb);
					t.start();
				}
			}
			
			System.out.println("Download ok!");
		} catch (Exception e) {
			fail("Exception occurred", e.getMessage());
		}

	}

	private static void fail(String statue, String asString) {
		System.out.println("\n\n\n***COPY FAILED***");
		System.out.println("Reason:" + statue);
		System.out.println("Info:" + asString);
		System.exit(1);
	}

}

class DownloadThread extends Thread{
	Submission sb;
	public DownloadThread(Submission sb){
		this.sb=sb;
	}
	
	public void o(String mes){
		System.out.println("[DownloadThread-"+sb.id+"]"+mes);
	}
	public void run(){
		
		try{
			o("Start");
			
			o("Connecting Start");
			
			String s=WebPageSource.get("http://codeforces.com/contest/"+sb.contest+"/submission/"+sb.id);
			
			String[] sa=s.split("\n");
			
			String ppp="";
			boolean start=false;
			
			for(String sad:sa){
				if(start){
					ppp+=sad+"\n";
				}
				if(sad.matches(".*</pre>.*")){
					start=false;
				}
				if(sad.matches(".*<pre id=\"program-source-text\" class=\"prettyprint lang-[a-z ]+-source\" style=\"padding: 0.5em;\">.*")){
					start=true;
				}
			}
			
			o("Start Formatting");
			String p2=ppp;
			ppp="";
			boolean in=false;
			for(int i=0;i<p2.length();i++){
				if(p2.charAt(i)=='<'){
					in=true;
				}
				if(!in){
					ppp+=p2.charAt(i);
				}
				if(p2.charAt(i)=='>'){
					in=false;
				}
			}
			
			o("Start Escaping");
			ppp=WebPageSource.htmlReplace(ppp);
			
			o("Start Saving");
			File f=new File("download");
			if(f.exists()==false){
				f.mkdirs();
			}
			
			File f2=new File("download/"+sb.contest+sb.index);
			if(f2.exists()==false){
				f2.mkdirs();
			}
			
			File f3=new File(f2.getAbsolutePath()+"/"+sb.verdict+" - "+sb.id+".txt");
			PrintWriter pw=new PrintWriter(f3);
			pw.println(ppp);
			pw.close();
			o("Finished at "+f3.getAbsolutePath());
		}catch(Exception e){
			o("*****DOWNLOAD FAILED:"+e);
		}
	}
	
}
