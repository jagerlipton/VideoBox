package go.videobox;


import java.io.Serializable;

public class Item  implements Serializable {

    String header;
    String subheader;
    String pictureurl;
    String quality;
    String year;
    String audio;
    String duration;
    String pagelink;


    Item(String h, String s, String u, String q, String y, String a,String d,String p){
        this.header=h;
        this.subheader=s;
        this.pictureurl=u;
        this.quality=q;
        this.year=y;
        this.audio=a;
        this.duration=d;
        this.pagelink=p;
    }

 }