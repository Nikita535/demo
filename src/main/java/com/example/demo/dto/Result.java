package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Schema(description = "Ответ на запрос")

public class Result<T> {

    @Schema(description = "Описание ошибки")

    private Error error;

    @Schema( description = "Данные")

    private T data;

    public Result(T data) {
        this.data = data;
    }

    public Result(Error error) {
        this.error = error;
    }

    public static Result success() {
        return success((Object)null);
    }

    public static <T> Result<T> success(final T data) {
        return new Result(data);
    }

    public static <T> Result<T> error(int code, String message) {
        return error(code, message, (Exception)null);
    }

    public static <T> Result<T> error(int code, String message, Exception ex) {
        Error error = new Error(code, LocalDateTime.now(), message, (String[])null);
        if (ex != null) {
            error.setTrace((String[])Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toArray((x$0) -> {
                return new String[x$0];
            }));
        }

        return new Result(error);
    }

    public Error getError() {
        return this.error;
    }

    public T getData() {
        return this.data;
    }

    public void setError(final Error error) {
        this.error = error;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Result)) {
            return false;
        } else {
            Result<?> other = (Result)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$error = this.getError();
                Object other$error = other.getError();
                if (this$error == null) {
                    if (other$error != null) {
                        return false;
                    }
                } else if (!this$error.equals(other$error)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Result;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        Error var10000 = this.getError();
        return "Result(error=" + var10000 + ", data=" + this.getData() + ")";
    }

    public Result() {
    }

    public Result(final Error error, final T data) {
        this.error = error;
        this.data = data;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)

    @Schema(description = "Описание ошибки")

    public static class Error {

        @Schema( description = "Код ошибки")

        private Integer code;
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy hh:mm:ss"
        )

        @Schema(description = "Время возбуждения ошибки")

        private LocalDateTime timestamp;

        @Schema(description = "Сообщение с описанием ошибки")

        private String message;

        @Schema(description = "Стектрейс системного исключения")

        private String[] trace;

        public Integer getCode() {
            return this.code;
        }

        public LocalDateTime getTimestamp() {
            return this.timestamp;
        }

        public String getMessage() {
            return this.message;
        }

        public String[] getTrace() {
            return this.trace;
        }

        public void setCode(final Integer code) {
            this.code = code;
        }

        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy hh:mm:ss"
        )
        public void setTimestamp(final LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public void setTrace(final String[] trace) {
            this.trace = trace;
        }

        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Error)) {
                return false;
            } else {
                Error other = (Error)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if
                        (other$code != null) {
                            return false;
                        }
                    } else if (!this$code.equals(other$code)) {
                        return false;
                    }

                    Object this$timestamp = this.getTimestamp();
                    Object other$timestamp = other.getTimestamp();
                    if (this$timestamp == null) {
                        if (other$timestamp != null) {
                            return false;
                        }
                    } else if (!this$timestamp.equals(other$timestamp)) {
                        return false;
                    }

                    Object this$message = this.getMessage();
                    Object other$message = other.getMessage();
                    if (this$message == null) {
                        if (other$message != null) {
                            return false;
                        }
                    } else if (!this$message.equals(other$message)) {
                        return false;
                    }

                    if (!Arrays.deepEquals(this.getTrace(), other.getTrace())) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Error;
        }

        public int hashCode() {
            boolean PRIME = true;
            int result = 1;
            Object $code = this.getCode();
            result = result * 59 + ($code == null ? 43 : $code.hashCode());
            Object $timestamp = this.getTimestamp();
            result = result * 59 + ($timestamp == null ? 43 : $timestamp.hashCode());
            Object $message = this.getMessage();
            result = result * 59 + ($message == null ? 43 : $message.hashCode());
            result = result * 59 + Arrays.deepHashCode(this.getTrace());
            return result;
        }

        public String toString() {
            Integer var10000 = this.getCode();
            return "Result.Error(code=" + var10000 + ", timestamp=" + this.getTimestamp() + ", message=" + this.getMessage() + ", trace=" + Arrays.deepToString(this.getTrace()) + ")";
        }

        public Error() {
        }

        public Error(final Integer code, final LocalDateTime timestamp, final String message, final String[] trace) {
            this.code = code;
            this.timestamp = timestamp;
            this.message = message;
            this.trace = trace;
        }
    }
}
