package go.videobox.assynctasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import go.videobox.adapters.PlaylistItem;
import go.videobox.base.AbstractTaskLoader;
import go.videobox.base.ITaskLoaderListener;
import go.videobox.base.TaskProgressDialogFragment;
import go.videobox.dbClass.FilmData;
import go.videobox.dbClass.FilmHeader;

public class GetPlaylistKinogo extends AbstractTaskLoader { //  получаем txt файл изх сети и обрабатываем
    private Bundle mBundle;

    private static ArrayList<PlaylistItem> playList = new ArrayList<>();

    public static void execute(FragmentActivity fa, ITaskLoaderListener taskLoaderListener, Bundle arguments) {
        GetPlaylistKinogo loader = new GetPlaylistKinogo(fa,arguments);

        new TaskProgressDialogFragment.Builder(fa, loader, "Wait...")
                .setCancelable(false)
                .setTaskLoaderListener(taskLoaderListener)
                .show();
    }

    protected GetPlaylistKinogo(Context context, Bundle arguments) {
        super(context);
        if (arguments != null)  this.mBundle=arguments;
    }



    @Override
    public Object loadInBackground() { // парсинг плейлиста тхт и добавление в список итемов
        String listurl = mBundle.getString("urlpage");
        String list_poster_header=mBundle.getString("poster_header");
        String list_poster_url=mBundle.getString("poster_url");
        String result = "";


        try {
            playList.clear();
            URL url = new URL(listurl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;

            PlaylistItem pItem = new PlaylistItem("","",false,"","","",0,0);
            while ((str = in.readLine()) != null) {
                result += str;
                if ((!str.isEmpty())&&(str.indexOf("comment")>0)){ playList.add(parsePlaylistLine(str,list_poster_header,list_poster_url));  }

            }
            in.close();
          //  db2Playlist();  //не работает
            if (isCanselled()) {
                //      break;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

        Bundle mbundle = new Bundle();
        mbundle.putSerializable("GetPlaylistKinogo",playList);
        mbundle.putString("header","GetPlaylistKinogo");
        return mbundle;
    }

    //парсинг строки тхт плейлиста

    private PlaylistItem parsePlaylistLine(String s,String sHeader, String sPosterUrl){
        PlaylistItem pItem= new PlaylistItem("","",true,"","","",0,0);
        pItem.mDuration=0;
        pItem.mPosition=0;
        pItem.mHeader=sHeader;
        pItem.mPosterUrl=sPosterUrl;

        if ((s.indexOf("comment")>0)&&(s.indexOf("<br>")<(s.indexOf("\",\"file\":\""))&&(s.indexOf("<br>")>0))) {
            pItem.mSubHeader=s.substring(s.indexOf(":")+2,s.indexOf("<br>"));
            pItem.mSubsubHeader = s.substring(s.indexOf("<br>") + 4, s.indexOf("\",\"file\":\""));
            pItem.mUrl=s.substring(s.indexOf("\",\"file\":\"")+10,s.indexOf("\"}"));

        }
        else if ((s.indexOf("comment")>0)&&(!s.contains("<br>"))) {
            pItem.mSubHeader=s.substring(s.indexOf("{\"comment\":\"")+12,s.indexOf("\",\"file\":\""));

            System.out.println(pItem.mSubHeader);
            pItem.mSubsubHeader = "";
            pItem.mUrl=s.substring(s.indexOf("\",\"file\":\"")+10,s.indexOf("\"}"));
        }

        return pItem;
    }


    public void db2Playlist(){   //заполнение итемов списка сериалов позициями и продолжительностью

        for (PlaylistItem pl:playList) {

            FilmHeader sHead = new FilmHeader().selectField("Header",pl.mHeader);
            List<FilmData> list = sHead.getFilmList();
            if (list.size()>0) {
                for (FilmData data: list) {
                    if (data.mSubHeader.contains(pl.mSubHeader)){
                        pl.mDuration=data.mDuration;
                        pl.mPosition=data.mPosition;

                    }
                }
            }
        }
    }



    @Override
    public Bundle getArguments() {
        return null;
    }

    @Override
    public void setArguments(Bundle args) {
    }




}


/*
  private class  getPlaylist extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... parameter) {
            String result = "";
            String listurl = "";
            for (String p : parameter) listurl = p;

            try {
                playList.clear();
                URL url = new URL(listurl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;

                PlaylistItem pItem = new PlaylistItem("","",false,"","","",0,0);
                while ((str = in.readLine()) != null) {
                    result += str;
                    if ((!str.isEmpty())&&(str.indexOf("comment")>0)){ playList.add(parsePlaylistLine(str,poster_header,poster_url));  }

                }
                in.close();
                db2Playlist();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }




  @Override
        protected void onPostExecute(Void aVoid)
        {


        }
    }

*
*
*
* */