package com.ogbongefriends.com.chat;

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

	private String userID, friendId; 
		
	private String message;

	public boolean isPersonalMessage = true;

	public MessageModel() {
	}

	public MessageModel(Parcel in) {
		userID = in.readString();
		friendId = in.readString();
		message = in.readString();
		
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(message);
		dest.writeInt(isPersonalMessage ? 1 : 0);
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

	

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

	@Override
	public String toString() {

		return "userID:" + userID + ",friendId:" + friendId
				+ ", message:" + message;

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
