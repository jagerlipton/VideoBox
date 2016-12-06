package go.videobox.adapters;


import java.io.Serializable;

public class Item  implements Serializable {

    public  String header;
    public  String subheader;
    public  String pictureurl;
    public String quality;
    public String year;
    public  String audio;
    public  String duration;
    public String pagelink;


   public Item(String h, String s, String u, String q, String y, String a,String d,String p){
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