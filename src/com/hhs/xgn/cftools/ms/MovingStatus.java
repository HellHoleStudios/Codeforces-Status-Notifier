package com.hhs.xgn.cftools.ms;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hhs.xgn.cftools.common.FullSubmission;
import com.hhs.xgn.cftools.common.WebPageSource;

public class MovingStatus {

	static List<MovingWindow> mw = new ArrayList<MovingWindow>();
	static Map<Integer, FullSubmission> fs = new HashMap<Integer, FullSubmission>();
	static long startTime;
	static Set<Integer> appear = new HashSet<Integer>();

	public static void run4(String handle) throws Exception {
		// fs=new HashMap<Integer,FullSubmission>();

		System.out.println("Getting:" + handle);

		String s = WebPageSource.get("http://codeforces.com/api/user.status?handle=" + handle + "&count=20");

		JsonParser jp = new JsonParser();
		JsonObject root = (JsonObject) jp.parse(s);

		String statue = root.get("status").getAsString();

		if (statue.equals("OK") == false) {
			System.out.println("Get " + handle + " Failed");
			return;
		}

		JsonArray ja = root.get("result").getAsJsonArray();

		for (int i = 0; i < ja.size(); i++) {
			JsonObject sub = ja.get(i).getAsJsonObject();

			FullSubmission fs = new FullSubmission(sub);

			// System.out.println(fs);

			if (fs.creationTimeSeconds * 1000l < startTime) {
				break;
			}
			MovingStatus.fs.put(fs.id, fs);
			if (appear.contains(fs.id) == false) {
				appear.add(fs.id);
				add(fs.id, handle);
			}
		}

		// System.out.println(fs);

	}

	public static void main(String[] args) {
		add(1,"Test");

		try {
			String handle = "";
			if (args.length == 0) {
				Scanner s = new Scanner(System.in);
				System.out.println("Please input the user you are going to watch:");
				handle = s.nextLine();
				s.close();
			} else {
				handle = args[0];
			}
			startTime = System.currentTimeMillis();

			String[] handles = handle.split(" ");

			int delta = 2000;
			while (true) {
				Thread.sleep(delta);
				for (String hh : handles) {
					run4(hh);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void add(int id, String handle) {
		mw.add(new MovingWindow(id, mw.size() + 1, handle));
		// System.out.println(mw.size());
	}

	public static void del(int id) {
		// System.out.println("Remove:"+id);
		mw.remove(id - 1);
		for (int i = 0; i < mw.size(); i++) {
			mw.get(i).setId(i + 1);
		}
	}
}
