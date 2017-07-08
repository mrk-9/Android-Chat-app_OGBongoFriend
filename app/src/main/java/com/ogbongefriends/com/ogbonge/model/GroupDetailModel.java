package com.ogbongefriends.com.ogbonge.model;

import java.util.List;

public class GroupDetailModel {

	private UserDetail groupDetail;
	private List<UserDetail> memberDetail;

	public UserDetail getGroupDetail() {
		return groupDetail;
	}

	public void setGroupDetail(UserDetail groupDetail) {
		this.groupDetail = groupDetail;
	}

	public List<UserDetail> getMemberDetail() {
		return memberDetail;
	}

	public void setMemberDetail(List<UserDetail> memberDetail) {
		this.memberDetail = memberDetail;
	}

}
