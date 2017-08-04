package com.hzh.rpcframework.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class MessageReq {
    private long messageId;
    private String className;
    private String methodName;
    //参数类型
    private Class<?>[] typeParameters;
    //参数值
    private Object[] parameterVals;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(Class<?>[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public Object[] getParameterVals() {
        return parameterVals;
    }

    public void setParameterVals(Object[] parameterVals) {
        this.parameterVals = parameterVals;
    }

    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("messageId", messageId)
                .append("className", className)
                .append("methodName", methodName).toString();
    }
}
