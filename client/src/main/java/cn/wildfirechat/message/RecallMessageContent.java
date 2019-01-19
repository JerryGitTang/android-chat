package cn.wildfirechat.message;

import android.os.Parcel;

import cn.wildfirechat.message.core.ContentTag;
import cn.wildfirechat.message.core.MessagePayload;
import cn.wildfirechat.message.core.PersistFlag;
import cn.wildfirechat.message.notification.NotificationMessageContent;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

import static cn.wildfirechat.message.core.MessageContentType.ContentType_Recall;

/**
 * Created by heavyrain lee on 2017/12/6.
 */

@ContentTag(type = ContentType_Recall, flag = PersistFlag.Persist)
public class RecallMessageContent extends NotificationMessageContent {
    private String operatorId;
    private long messageUid;

    public RecallMessageContent() {
    }

    public RecallMessageContent(String operatorId, long messageUid) {
        this.operatorId = operatorId;
        this.messageUid = messageUid;
    }

    @Override
    public MessagePayload encode() {
        MessagePayload payload = new MessagePayload();
        payload.content = operatorId;
        payload.binaryContent = new StringBuffer().append(messageUid).toString().getBytes();
        return payload;
    }


    @Override
    public void decode(MessagePayload payload) {
        operatorId = payload.content;
        messageUid = Long.parseLong(new String(payload.binaryContent));
    }

    @Override
    public String digest() {
        return formatNotification();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public long getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(long messageUid) {
        this.messageUid = messageUid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.operatorId);
        dest.writeLong(this.messageUid);
    }

    protected RecallMessageContent(Parcel in) {
        this.operatorId = in.readString();
        this.messageUid = in.readLong();
    }

    public static final Creator<RecallMessageContent> CREATOR = new Creator<RecallMessageContent>() {
        @Override
        public RecallMessageContent createFromParcel(Parcel source) {
            return new RecallMessageContent(source);
        }

        @Override
        public RecallMessageContent[] newArray(int size) {
            return new RecallMessageContent[size];
        }
    };

    @Override
    public String formatNotification() {
        UserInfo userInfo = ChatManager.Instance().getUserInfo(operatorId, false);
        String notification = "%s撤回了一条消息";
        if (fromSelf) {
            notification = String.format(notification, "您");
        } else {
            if (userInfo != null) {
                notification = String.format(notification, userInfo.displayName);
            } else {
                notification = String.format(notification, "<" + operatorId + "> ");
            }
        }
        return notification;
    }
}