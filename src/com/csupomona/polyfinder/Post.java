package com.csupomona.polyfinder;
/**
 * Created by marti on 11/9/13.
 */
public class Post {
    private String author_name, message, timestamp;

    public Post(){
        author_name = "";
        message = "";
        timestamp = "";
    }

    public Post(String a_name, String text, String time){
        author_name = a_name;
        message = text;
        timestamp = time;
    }

    public void setAuthor_name(String name){author_name = name;}

    public void setMessage(String t){message = t;}

    public void setTimestamp(String t){timestamp = t;}

    public String getAuthorName(){return author_name;}
    public String getMessage() {return message;}
    public String getTimestamp() {return timestamp;}

    public String toString(){
        return author_name +": "+message;
    }
}
