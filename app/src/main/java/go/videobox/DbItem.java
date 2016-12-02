package go.videobox;


import java.io.Serializable;
import java.util.Date;

public class DbItem  implements Serializable {

    public String mHeader;
    public String mPosterUrl;
    public Boolean mSerialFlag;
    public String mUrl;
    public String mSubHeader;
    public Integer mDuration;
    public Integer mPosition;
    public Date mDateWatch;



    public DbItem(String h, String s, Boolean u, String q, String y, Integer a,Integer d,Date ww){
        this.mHeader=h;
        this.mPosterUrl=s;
        this.mSerialFlag=u;
        this.mUrl=q;
        this.mSubHeader=y;
        this.mDuration=a;
        this.mPosition=d;
        this.mDateWatch=ww;

    }

}