package com.csupomona.polyfinder;
    /**
     * Created by marti on 11/9/13.
     */
    public class SingleComment {
        String author_name, message, timestamp;
        int event_id, comment_id;

        public SingleComment(){
            author_name = "";
            message = "";
            timestamp = "";
            event_id = -1;
            comment_id = -1;
        }

        public SingleComment(int e_id, int c_id, String a_name, String text, String time){
            author_name = a_name;
            message = text;
            timestamp = time;
            event_id = e_id;
            comment_id = c_id;
        }

        public void setAuthor_name(String name){author_name = name;}

        public void setMessage(String t){message = t;}

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public void setEvent_id(int event_id) {
            this.event_id = event_id;
        }

        public void setTimestamp(String t){timestamp = t;}

        public String getAuthorName(){return author_name;}
        public String getMessage() {return message;}
        public String getTimestamp() {return timestamp;}
        public int getEventId() {return event_id;}
        public int getCommentId() {return comment_id;}

        public String toString(){
            return author_name +": "+message;
        }



    }
