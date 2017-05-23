package com.netty.controllers;

import com.dyuproject.protostuff.Tag;

public class UserInfo {
	@Tag(value =1)
	public String acount;
	@Tag(value=2)
	public String id;
	@Tag(value=3)
	public int currentLevel;
	@Tag(value=4)
	public int maxLevel;
	@Tag(value=5)
	public int hintCount;
}