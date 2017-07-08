package com.ogbongefriends.com.ogbonge.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageModel implements Parcelable {

	public static final int MESSAGE_TYPE_MESSAGE = 1;
	public static final int MESSAGE_TYPE_IMAGE = 2;
	public static final int MESSAGE_TYPE_VEDIO = 3;
	public static final int MESSAGE_TYPE_LOCATION = 4;
	public static final int MESSAGE_TYPE_STICKER = 5;
	public static final int MESSAGE_TYPE_CONTACT = 6;

	// for message status
	public static final int STATUS_SENDING = 1;
	public static final int STATUS_SEND = 2;
	public static final int STATUS_DELIVERED = 3;
	public static final int STATUS_DOWNLOADING = 4;
	public static final int STATUS_UPLOADING = 5;

	// 1=personal chat
	// 2=group chat
	// 3=message delivered
	// 4=join a group
	// 5=leave a group

	public static final int WHO_PERSONAL = 1;
	public static final int WHO_GROUP = 2;
	public static final int WHO_MESSAGE_DELIVERED = 3;
	public static final int WHO_JOIN_GROUP = 4;
	public static final int WHO_LEAVE_GROUP = 5;

	private int id, messageId, messageType, userID, friendId, messageStatus,
			who;
	private String message, time;

	private String displayName;

	public boolean isPersonalMessage = true;

	public MessageModel() {
	}

	public MessageModel(Parcel in) {
		id = in.readInt();
		messageId = in.readInt();
		messageType = in.readInt();
		userID = in.readInt();
		friendId = in.readInt();
		messageStatus = in.readInt();
		who = in.readInt();
		message = in.readString();
		time = in.readString();
		isPersonalMessage = in.readInt() == 1;
		displayName = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(messageId);
		dest.writeInt(messageType);
		dest.writeInt(userID);
		dest.writeInt(friendId);
		dest.writeInt(messageStatus);
		dest.writeInt(who);
		dest.writeString(message);
		dest.writeString(time);
		dest.writeInt(isPersonalMessage ? 1 : 0);
		dest.writeString(displayName);
	}

	public static final Parcelable.Creator<MessageModel> CREATOR = new Creator<MessageModel>() {

		@Override
		public MessageModel[] newArray(int size) {
			return new MessageModel[size];
		}

		@Override
		public MessageModel createFromParcel(Parcel source) {
			return new MessageModel(source);
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public int getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(int messageStatus) {
		this.messageStatus = messageStatus;
	}

	public void setWho(int who) {
		this.who = who;
	}

	public int getWho() {
		return who;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayNane(String displayNAme) {
		this.displayName = displayNAme;
	}

	@Override
	public String toString() {

		return "id:" + id + ", messageId :" + messageId + ", messageType: "
				+ messageType + ",userID:" + userID + ",friendId:" + friendId
				+ ",messageStatus : " + messageStatus + ",who:" + who
				+ ", message:" + message + ",time :" + time;

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
