package go.videobox.dbClass;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import java.util.Date;
import java.util.List;



    @Table(name = "Films",id = "_id")
    public class FilmHeader extends Model {
        @Column(name = "Profile")
        public String mProfile;

        @Column(name = "Header", unique = true)
        public String mHeader;

        @Column(name = "PosterUrl")
        public String mPosterUrl;

        @Column(name = "Url")
        public String mUrl;

        @Column(name = "SerialFlag")
        public Boolean mSerialFlag;

        @Column(name = "timestamp", index = true)
        private Date timestamp;

       @Column(name = "DateWatch")
        public Date mDateWatch;

        @Column(name = "Description")
        public String mDescription;

    public List<FilmData> filmList;



        public FilmHeader() {
            super();
        }

        public FilmHeader(String mProfile, String mHeader,String mDescription, String mPosterUrl, String mUrl, Boolean mSerialFlag) {
            super();
            this.mProfile = mProfile;//  имя профиля
            this.mHeader = mHeader;   // название фильма
            this.mDescription = mDescription;   // название фильма
            this.mPosterUrl = mPosterUrl;  // линк постера
            this.mUrl = mUrl;//  линк flv или txt
            this.mSerialFlag = mSerialFlag;//  cериал или нет

        }

//---------------------------------------------------------
    public Boolean checkExistsDbItem (String mHeader) {
        return  new Select().from(FilmHeader.class).where("Header = ?",mHeader).exists();
    }
//---------------------------------------------------------
    public List<FilmData> getFilmList() {
      return getMany(FilmData.class, "FilmHeaderForeign");
       }
//---------------------------------------------------------
    public  List<FilmHeader> getFifty() {
            return  new Select().from(FilmHeader.class).limit(50).orderBy("DateWatch DESC").execute();
    }
//---------------------------------------------------------
    public static FilmHeader selectField(String fieldName, String fieldValue) {
        return new Select().from(FilmHeader.class).where(fieldName + " = ?", fieldValue).executeSingle();
    }
//---------------------------------------------------------
    private static FilmData selectFieldData(String fieldName, String fieldValue) {
            return new Select().from(FilmData.class).where(fieldName + " = ?", fieldValue).executeSingle();
     }
//---------------------------------------------------------
    public  void updateDate(String myHeader) {
        FilmHeader model = selectField("Header", myHeader);
        model.mDateWatch = new Date ();
        model.save();
    }
//----------------------------------------------------------

//добавление позиции к фильму
    public  void updatePositionFilm(String myHeader,String mySubHeader,Integer myPos,Integer myDur ) {

        FilmHeader mHeader = selectField("Header", myHeader);

        if (!mHeader.mSerialFlag){ //Если не сериал
            FilmData mData = new FilmData();
            if (mData.checkExistsDbItem(myHeader)) mData =  selectFieldData("SubHeader",myHeader); //выделить ячейку с фильмом
            mData.mDuration = myDur;
            mData.mPosition = myPos;
            mData.save();
        }
        else {

            List<FilmData> listdata = mHeader.getFilmList();
            for (FilmData fordata: listdata)
            if (fordata.mSubHeader.contains(mySubHeader))
            {
                fordata.mDuration = myDur;
                fordata.mPosition = myPos;
                fordata.save();
                Log.d("ololo","нашли серию, пишем -  "+fordata.mSubHeader);
            }

           //по субхедеру нельзя выделять. есть совпадения
          /*/  if (mData.checkExistsDbItem(mySubHeader)) mData =  selectFieldData("SubHeader",mySubHeader); //выделить ячейку с серией
            Log.d("ololo","выделенная ячейка -  "+mData.mSubHeader);
            mData.mDuration = myDur;
            mData.mPosition = myPos;
            mData.save();*/

        }
    }
//---------------------------------------------------------
    public  void addSeries(String myHeader,String mySubHeader, String murl) {
            FilmHeader model = selectField("Header", myHeader);
            FilmData sData = new FilmData();
            sData.mSubHeader=mySubHeader;
            sData.mUrl=murl;
            sData.mDuration=0;
            sData.mPosition=0;
            sData.myFilmHeader=model;
            sData.save();
                 }
    }
//---------------------------------------------------------


