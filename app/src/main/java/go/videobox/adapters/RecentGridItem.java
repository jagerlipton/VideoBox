package go.videobox.adapters;

import java.io.Serializable;
import java.util.Date;



public class RecentGridItem  implements Serializable {

    public String mHeader;  // название фильма
    public  String mProfile; // название сайта профайла
    public String mPosterUrl;   // линк постера
    public Boolean mSerialFlag;  // флаг сериала
    public String mUrl;//   урл на файл
    public String mSubHeader;//  хз зачем
    public String mDescription;//  описание фильма
    public Integer mDuration;//  длительность
    public Integer mPosition;//  позиция
    public Date mDateWatch;//  дата просмотра



    public RecentGridItem(String mHeader, String mPosterUrl, Boolean mSerialFlag, String mUrl, String mSubHeader, Integer mDuration,Integer mPosition,Date mDateWatch, String mProfile,String mDescription){
        this.mHeader=mHeader;
        this.mPosterUrl=mPosterUrl;
        this.mSerialFlag=mSerialFlag;
        this.mUrl=mUrl;
        this.mSubHeader=mSubHeader;
        this.mDuration=mDuration;
        this.mPosition=mPosition;
        this.mDateWatch=mDateWatch;
        this.mProfile=mProfile;
        this.mDescription=mDescription;
    }

}
