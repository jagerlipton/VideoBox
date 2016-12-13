package go.videobox.dbClass;


import android.util.Log;

import java.util.Date;
import java.util.List;

import static go.videobox.dbClass.FilmHeader.selectField;

public  class WorkWithDB {

    public static void checkWatchSerialFilm (String myHeader, String myPosterUrl, String myUrlSerial,String myUrlSeries,String mySubHeader,String myDescription){

        FilmHeader sHeader = new FilmHeader();
        FilmData sData = new FilmData();

        if (!sHeader.checkExistsDbItem(myHeader)) { //если хедера не существует

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

            Log.d("ololo", "добавили новый сериал");
        }

        else { // если существует
            sHeader = selectField("Header", myHeader);
            List<FilmData> listdata1 = sHeader.getFilmList();
            if (listdata1.size() > 0) {
                Boolean ex = false;
                for (FilmData fordata : listdata1)
                    if (fordata.mSubHeader.contains(mySubHeader)) {  // если такая серия существует,
                        sHeader.updateDate(myHeader);  /// обновить дату
                        Log.d("ololo", "обновили дату");
                        ex = true;
                    }
                if (!ex) {
                    sHeader.addSeries(myHeader, mySubHeader, myUrlSeries);     //запись несуществующей серии сериала
                    Log.d("ololo", "добавили серию");
                }
            }
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
