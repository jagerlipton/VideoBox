package go.videobox;


import java.io.Serializable;

public class PlaylistItem  implements Serializable {

    public String mHeader;
    public String mPosterUrl;
    public Boolean mSerialFlag;
    public String mUrl;
    public String mSubHeader;
    public String mSubsubHeader;
    public Integer mDuration;
    public Integer mPosition;



    public PlaylistItem(String mHeader, String mPosterUrl, Boolean mSerialFlag, String mUrl, String mSubHeader, String mSubsubHeader, Integer mDuration,Integer mPosition){
        this.mHeader=mHeader;
        this.mPosterUrl=mPosterUrl;
        this.mSerialFlag=mSerialFlag;
        this.mUrl=mUrl;
        this.mSubHeader=mSubHeader;
        this.mSubsubHeader=mSubsubHeader;
        this.mDuration=mDuration;
        this.mPosition=mPosition;

    }

}