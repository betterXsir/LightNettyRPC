package com.hzh.rpcframework.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MessageResp implements Serializable {
    private long messageId;
    private Object result;
    private String status;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).
                append("messageId", messageId)
                .append("result", result)
                .append("status", status).toString();
    }
}
