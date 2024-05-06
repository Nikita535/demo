package com.example.demo.exception.base;


import com.example.demo.exception.Message;
import lombok.Getter;

import java.util.Arrays;

@Getter
public abstract class BaseException extends RuntimeException {
    private Message msg;
    private Object[] params;

    public BaseException() {
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Message msg, Object... params) {
        this.msg = msg;
        this.params = params;
    }

    public void setMsg(final Message msg) {
        this.msg = msg;
    }

    public void setParams(final Object[] params) {
        this.params = params;
    }

    public Object[] getParams() {
        return this.params;
    }

    public String toString() {
        Message var10000 = this.getMsg();
        return "BaseException(msg=" + var10000 + ", params=" + Arrays.deepToString(this.getParams()) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseException)) {
            return false;
        } else {
            BaseException other = (BaseException)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg == null) {
                        return Arrays.deepEquals(this.getParams(), other.getParams());
                    }
                } else if (this$msg.equals(other$msg)) {
                    return Arrays.deepEquals(this.getParams(), other.getParams());
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BaseException;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getParams());
        return result;
    }
}