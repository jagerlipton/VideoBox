package go.videobox.dbClass;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import java.util.Date;
import java.util.List;



    @Table(name = "Films",id = "_id")
    public class FilmHeader extends Model {
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


    public List<FilmData> filmList;



        public FilmHeader() {
            super();
        }

        public FilmHeader(String mHeader, String mPosterUrl, String mUrl, Boolean mSerialFlag) {
            super();
            this.mHeader = mHeader;
            this.mPosterUrl = mPosterUrl;
            this.mUrl = mUrl;
            this.mSerialFlag = mSerialFlag;

        }


    public Boolean checkExistsDbItem (String mHeader) {

        return  new Select().from(FilmHeader.class).where("Header = ?",mHeader).exists();
    }


    public List<FilmData> getFilmList() {
      return getMany(FilmData.class, "FilmHeaderForeign");
       }

    public  List<FilmHeader> getFifty() {
            return  new Select().from(FilmHeader.class).limit(50).orderBy("DateWatch DESC").execute();
    }
    public static FilmHeader selectField(String fieldName, String fieldValue) {
        return new Select().from(FilmHeader.class)
                .where(fieldName + " = ?", fieldValue).executeSingle();
    }


    public  void updateDate(String myHeader) {
        FilmHeader model = selectField("Header", myHeader);
        model.mDateWatch = new Date ();
        model.save();
    }

    public  void updatePositionFilm(String myHeader,String mySubHeader,Integer myPos,Integer myDur ) {
        FilmHeader model = selectField("Header", myHeader);
        if (model.mSerialFlag) {
            List<FilmData> dlist = model.getFilmList();
            if (dlist.size() > 0)
                for (FilmData data : dlist) {
                    data.mDuration = myPos;
                    data.mPosition = myDur;
                    data.save();
                }
        } else { //если сериал
            List<FilmData> dlist = model.getFilmList();
            if (dlist.size() > 0)
                for (FilmData data : dlist)     if (data.mSubHeader.equals(mySubHeader)) {
                    data.mDuration = myPos;
                    data.mPosition = myDur;
                    data.save();
                }
        }



        List<FilmData> dlist = model.getFilmList();
        if (dlist.size()>0) {
            for (FilmData data: dlist) {
                if (data.mSubHeader.equals(mySubHeader)) {
                    data.mDuration = myPos;
                    data.mPosition = myDur;
                    data.save();

                }
            }
        }


    }

    }



