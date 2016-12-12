package go.videobox.adapters;


import java.io.Serializable;

public class Item  implements Serializable {

    public  String mHeader;   //название фильма
    public  String mProfile; // название сайта профайла
    public  String mSubHeader; //  описание фильма
    public  String mPictureurl; //  линк постера
    public  Integer mDuration; //  длительность
    public  Integer mPosition; //  позиция
    public String mPagelink; //  линк страницы с плеером


   public Item(String mHeader, String mSubHeader, String mPictureurl, Integer mPosition, Integer mDuration,  String mPagelink,String mProfile){
        this.mHeader=mHeader;
        this.mSubHeader=mSubHeader;
        this.mPictureurl=mPictureurl;
        this.mDuration=mDuration;
        this.mPosition=mPosition;
        this.mPagelink=mPagelink;
        this.mProfile=mProfile;

    }

 }