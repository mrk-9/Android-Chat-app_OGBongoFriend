package com.ogbongefriends.com.ogbonge.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDetail implements Parcelable {

	private int userId, messageId;
	private String name, image, email, phoneNo, phoneCode, status, lastMessage,
			messageTime;

	private boolean isSelected;

	private boolean isGroup;
	private int adminId;

	private int groupId;
	private boolean isAdmin;

	@Override
	public int describeContents() {
		return 0;
	}

	public UserDetail() {
	}

	public static final Parcelable.Creator<UserDetail> CREATOR = new Creator<UserDetail>() {

		@Override
		public UserDetail[] newArray(int size) {
			return new UserDetail[size];
		}

		@Override
		public UserDetail createFromParcel(Parcel source) {
			return new UserDetail(source);
		}
	};

	public UserDetail(Parcel in) {
		userId = in.readInt();
		messageId = in.readInt();
		name = in.readString();
		image = in.readString();
		email = in.readString();
		phoneNo = in.readString();
		phoneCode = in.readString();
		status = in.readString();
		lastMessage = in.readString();
		messageTime = in.readString();
		isSelected = in.readInt() == 1;
		isGroup = in.readInt() == 1;
		adminId = in.readInt();
		groupId = in.readInt();
		isAdmin = in.readInt() == 1;

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(userId);
		dest.writeInt(messageId);
		dest.writeString(name);
		dest.writeString(image);
		dest.writeString(email);
		dest.writeString(phoneNo);
		dest.writeString(phoneCode);
		dest.writeString(status);
		dest.writeString(lastMessage);
		dest.writeString(messageTime);
		dest.writeInt(isSelected ? 1 : 0);
		dest.writeInt(isGroup ? 1 : 0);
		dest.writeInt(adminId);
		dest.writeInt(groupId);
		dest.writeInt(isAdmin ? 1 : 0);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getAdminID() {
		return this.adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public void setIsGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public int getGroupID() {
		return groupId;
	}

	public void setGroupId(int groupID) {
		this.groupId = groupID;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return name;
		// + "," + image + "," + email + "," + phoneNo + ","
		// + phoneCode + "," + status + "," + lastMessage + ","
		// + messageTime;
	}
}
