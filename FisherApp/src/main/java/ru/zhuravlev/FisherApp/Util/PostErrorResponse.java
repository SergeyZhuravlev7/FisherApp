package ru.zhuravlev.FisherApp.Util;

import org.springframework.http.HttpStatus;

public class PostErrorResponse {

        private String message;

        private long timestamp;

        public PostErrorResponse(String message, long timestamp, HttpStatus httpStatus) {
            this.message = message;
            this.timestamp = timestamp;
        }

        public PostErrorResponse(String message, long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

}

