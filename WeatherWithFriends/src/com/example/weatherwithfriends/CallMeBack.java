package com.example.weatherwithfriends;

public interface CallMeBack {
	public void onTaskDone(String[] result);
	public void onTaskError();
	public byte[] onTaskFinished(byte[] result);
}
