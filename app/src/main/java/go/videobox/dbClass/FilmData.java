package go.videobox.dbClass;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by 111 on 30.11.2016.
 */


@Table(name = "Series",id = "_id")
public class FilmData extends Model {

    @Column(name = "Url")
    public String mUrl;
    @Column(name = "SubHeader")
    public String mSubHeader;
    @Column(name = "Duration")
    public Integer mDuration;
    @Column(name = "Position")
    public Integer mPosition;

    @Column(name = "FilmHeaderForeign", onDelete = Column.ForeignKeyAction.CASCADE, onUpdate =  Column.ForeignKeyAction.CASCADE)
    public FilmHeader myFilmHeader;




    public List<FilmData> getFifty() {
        return  new Select().from(FilmData.class).limit(50).execute();
    }

    public  FilmData () {
        super ();
    }

    public  void updatePosition(String myHeader) {
        FilmData model = selectField("SubHeader", myHeader);

        model.mPosition = 50;
        model.mDuration=100;
        model.save();
    }

    public static FilmData selectField(String fieldName, String fieldValue) {
        return new Select().from(FilmData.class)
                .where(fieldName + " = ?", fieldValue).executeSingle();
    }

}

