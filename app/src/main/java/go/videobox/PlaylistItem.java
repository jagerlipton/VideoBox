package go.videobox;


import java.io.Serializable;

public class PlaylistItem  implements Serializable {

    String header;
    String subheader;
    String url;
    String poster;


    PlaylistItem(String h, String s, String u, String p){
        this.header=h;
        this.subheader=s;
        this.url=u;
        this.poster=p;
    }

}