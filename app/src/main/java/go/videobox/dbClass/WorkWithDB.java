package go.videobox.dbClass;


import android.util.Log;

import java.util.Date;

public  class WorkWithDB {

    public static void checkWatchSerialFilm (String myHeader, String myPosterUrl, String myUrlSerial,String myUrlSeries,String mySubHeader,String myDescription){

        FilmHeader sHeader = new FilmHeader();
        FilmData sData = new FilmData();

        if (!sHeader.checkExistsDbItem(myHeader)) {

            sHeader.mHeader = myHeader;
            sHeader.mDescription=myDescription;
            sHeader.mProfile="";
            sHeader.mPosterUrl = myPosterUrl;
            sHeader.mSerialFlag = true;
            sHeader.mUrl = myUrlSerial;
            sHeader.mDateWatch = new Date();
            sHeader.save();

            sData.mSubHeader=mySubHeader;
            sData.mUrl=myUrlSeries;
            sData.mDuration=0;
            sData.mPosition=0;
            sData.myFilmHeader=sHeader;
            sData.save();
        }

        else {
            if (sData.checkExistsDbItem(mySubHeader)){
                sHeader.updateDate(myHeader);
            }
            else {
                //запись несуществующей серии сериала
                sHeader.addSeries(myHeader,mySubHeader,myUrlSeries);

            }




            Log.d("ololo", "обновили");
        }
    }
 //--------------------------------------------------------------

    public static  void checkWatchSingleFilm (String myHeader, String myPosterUrl, String myUrl,String myDescription){

        FilmHeader sHeader = new FilmHeader();
        FilmData sData = new FilmData();

        if (!sHeader.checkExistsDbItem(myHeader)) {
            sHeader.mProfile="";
            sHeader.mHeader = myHeader;
            sHeader.mDescription=myDescription;
            sHeader.mPosterUrl = myPosterUrl;
            sHeader.mSerialFlag = false;
            sHeader.mUrl = myUrl;
            sHeader.mDateWatch = new Date();
            sHeader.save();

            sData.mSubHeader=myHeader;
            sData.mUrl=myUrl;
            sData.mDuration=0;
            sData.mPosition=0;
            sData.myFilmHeader=sHeader;
            sData.save();
            Log.d("ololo", "записали новый фильм в хистори");

        }

        else {
            sHeader.updateDate(myHeader);
            Log.d("ololo", "обновили");
        }
    }


}
