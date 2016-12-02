package go.videobox;

import go.videobox.Mathem;
import go.videobox.core.AsyncTaskManager;
import go.videobox.core.OpenKinogoPageTask;
import go.videobox.dbClass.FilmData;
import go.videobox.dbClass.FilmHeader;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.mikepenz.fastadapter.utils.RecyclerViewCacheUtil;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private final static String searchpagelink="http://kinogo.club/index.php?do=search&subaction=search&search_start=0&full_search=0&result_from=1&titleonly=3&story=";
   // private  String back_pagelink="";
  //  private  String forward_pagelink="";

    String poster_url="";
    String poster_header="";
    String poster_sub_header="";
    String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private AccountHeader headerResult = null;
    private Drawer drawerresult = null;
    ExpandableDrawerItem category = new  ExpandableDrawerItem();
    ExpandableDrawerItem year = new  ExpandableDrawerItem();
    ExpandableDrawerItem serials = new  ExpandableDrawerItem();
    private IProfile profile_kinogo;
    private IProfile profile_kinokrad;
    private static final int PROFILE_SETTING = 1;

    public static  ArrayList<Item> arrayList = new ArrayList<>();
    public static  ArrayList<PlaylistItem> playList = new ArrayList<>();
    ListView listView;
    GridView gv;
    Context context;
    private AsyncTaskManager mAsyncTaskManager;


    ///Log.d("ololo",  Integer.toString(i));
    //  Toast.makeText(MainActivity.this, Integer.toString(i), Toast.LENGTH_SHORT).show();
    //System.out.println(st);

    //==============работа с МХ плеером

    public static final String RESULT_VIEW				= "com.mxtech.intent.result.VIEW";
    public static final String EXTRA_POSITION			= "position";
    public static final String EXTRA_RETURN_RESULT		= "return_result";
    public static final String EXTRA_DURATION			= "duration";
    public static final String EXTRA_END_BY				= "end_by";
    public static final int RESULT_ERROR				= Activity.RESULT_FIRST_USER + 0;
    private static final String 	PACKAGE_NAME_AD 		= "com.mxtech.videoplayer.ad";
    private static final String 	PLAYBACK_ACTIVITY_AD	= "com.mxtech.videoplayer.ad.ActivityScreen";
    public static final int REQUEST_CODE = 0x8001;

    private static void dumpParams( Intent intent )
    {
        StringBuilder sb = new StringBuilder();
        Bundle extras = intent.getExtras();

        sb.setLength(0);
        sb.append("* dat=").append(intent.getData());
      //  Log.v("ololo", sb.toString());

        sb.setLength(0);
        sb.append("* typ=").append(intent.getType());
      //  Log.v("ololo", sb.toString());

        if( extras != null && extras.size() > 0 )
        {
            sb.setLength(0);

            Integer p=1;
            Integer d=1;
            int i = 0;
            for( String key : extras.keySet() )
            {
              if (key.equals("duration")){

                  sb.setLength(0);
                  appendDetails( sb, extras.get( key ) );
                  String duration =sb.toString();
                   d= Integer.parseInt(duration);
                // / Log.v("ololo1", d.toString());

              }
                if (key.equals("position")){
                    sb.setLength(0);
                    appendDetails( sb, extras.get( key ) );
                    String position =sb.toString();
                     p= Integer.parseInt(position);
                  //  Log.v("ololo1", p.toString());
                }

            }
            positionfilm(p,d);
           // Log.v("ololo1",positionfilm(p,d).toString());

        }
    }

    public static Integer positionfilm (Integer pos, Integer dur){
                return  (int)(((double)pos/(double)dur) * 100);
    }

    private static void appendDetails( StringBuilder sb, Object object )
    {
        if( object != null && object.getClass().isArray() )
        {
            sb.append('[');

            int length = Array.getLength(object);
            for( int i = 0; i < length; ++i )
            {
                if( i > 0 )
                    sb.append(", ");

                appendDetails(sb, Array.get(object, i));
            }

            sb.append(']');
        }
        else if( object instanceof Collection)
        {
            sb.append('[');

            boolean first = true;
            for( Object element : (Collection)object )
            {
                if( first )
                    first = false;
                else
                    sb.append(", ");

                appendDetails(sb, element);
            }

            sb.append(']');
        }
        else
            sb.append(object);
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( requestCode == REQUEST_CODE )
        {
            switch( resultCode )
            {
                case RESULT_OK:
                //    Log.i( "ololo", "Ok: " + data );
                    break;

                case RESULT_CANCELED:
               //     Log.i( "ololo", "Canceled: " + data );
                    break;

                case RESULT_ERROR:
                //    Log.e( "ololo", "Error occurred: " + data );
                    break;

                default:
                 //   Log.w( "ololo", "Undefined result code (" + resultCode  + "): " + data );
                    break;
            }

            if( data != null )
                dumpParams(data);

			/*
			 * (YOUR CODE HERE) Handle result.
			 */

            finish();
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }


    private void startplayer(String flvurl){
      Intent playerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flvurl));
       playerIntent.putExtra(EXTRA_RETURN_RESULT, true);
       startActivityForResult(playerIntent,REQUEST_CODE);


           }




    //==============================================================






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                               opensearchpage(query);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
/// create resume============================================================


    private void buildHeader(boolean compact, Bundle savedInstanceState) {
            headerResult = new AccountHeaderBuilder()
                .withActivity(this)
               // .withHeaderBackground(R.drawable.header)
                .withHeaderBackground(new ColorDrawable(Color.parseColor("#c1000000")))
               // .withCompactStyle(true)
                .withProfileImagesVisible(false)
                .addProfiles(
                        profile_kinogo,
                        profile_kinokrad
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {

                        //if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {

                            if (headerResult.getProfiles() != null) {
                               if (profile.getName().toString()==("Kinogo.co"))headerResult.setHeaderBackground(new ImageHolder(R.drawable.header));
                                if (profile.getName().toString()==("Kinokrad.co"))headerResult.setHeaderBackground(new ImageHolder(R.drawable.header2));
                        }

                                               return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

      //  mAsyncTaskManager = new AsyncTaskManager(this, this);
     //   mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());

        gv=(GridView) findViewById(R.id.gridView1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_kinogo = new ProfileDrawerItem().withName("Kinogo.co").withIdentifier(1000);
        profile_kinokrad = new ProfileDrawerItem().withName("Kinokrad.co").withIdentifier(1001);

        buildHeader(false, savedInstanceState);


        category = new ExpandableDrawerItem().withName(R.string.drawer_item_category).withIdentifier(2).withSelectable(false).withArrowColor(getResources().getColor(R.color.arrows_barcolor));
        year = new ExpandableDrawerItem().withName(R.string.drawer_item_year).withIdentifier(3).withSelectable(false).withArrowColor(getResources().getColor(R.color.arrows_barcolor));
        serials = new ExpandableDrawerItem().withName(R.string.drawer_item_serials).withIdentifier(4).withSelectable(false).withArrowColor(getResources().getColor(R.color.arrows_barcolor));

        //выбор сохраненного профиля
        headerResult.setActiveProfile(profile_kinogo);headerResult.setHeaderBackground(new ImageHolder(R.drawable.header));



        drawerresult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult)
                .withSelectedItem(-1)
                .withInnerShadow(true)

                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowStatusBar(false)
                .addDrawerItems(
                        new  PrimaryDrawerItem().withName(R.string.drawer_item_startpage).withIdentifier(7).withSelectable(false),
                              new DividerDrawerItem(),
                        new  PrimaryDrawerItem().withName(R.string.drawer_item_history).withIdentifier(6).withSelectable(false),
                        new DividerDrawerItem(),
                        category.withSubItems(
                                new PrimaryDrawerItem().withName(R.string.drawer_item_fighting).withLevel(2).withIdentifier(201).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_detective).withLevel(2).withIdentifier(202).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_documental).withLevel(2).withIdentifier(203).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_drama).withLevel(2).withIdentifier(204).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_historic).withLevel(2).withIdentifier(205).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_comedy).withLevel(2).withIdentifier(206).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_criminal).withLevel(2).withIdentifier(207).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_melodrama).withLevel(2).withIdentifier(208).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_mult).withLevel(2).withIdentifier(209).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_musicle).withLevel(2).withIdentifier(210).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_ussr).withLevel(2).withIdentifier(211).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_adventures).withLevel(2).withIdentifier(212).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_family).withLevel(2).withIdentifier(213).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_sport).withLevel(2).withIdentifier(214).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_triller).withLevel(2).withIdentifier(215).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_horror).withLevel(2).withIdentifier(216).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_fantasy).withLevel(2).withIdentifier(217).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_fantastika).withLevel(2).withIdentifier(218).withSelectable(false)),
                        year.withSubItems(
                                new PrimaryDrawerItem().withName(R.string.drawer_item_year2016).withLevel(2).withIdentifier(301).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_year2015).withLevel(2).withIdentifier(302).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_year2014).withLevel(2).withIdentifier(303).withSelectable(false)),
                        serials.withSubItems(
                                new PrimaryDrawerItem().withName(R.string.drawer_item_serial_russian).withLevel(2).withIdentifier(401).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_serial_notrussian).withLevel(2).withIdentifier(402).withSelectable(false)),

                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_exit).withIdentifier(5).withSelectable(false)



                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem.getIdentifier() == 7) {
                          openpage(getResources().getString(R.string.link_item_startpage));
                        } else if (drawerItem.getIdentifier() == 1) {
                          //search


                        } else if (drawerItem.getIdentifier() == 5) {
                            MainActivity.this.finish();//exit
                        } else if (drawerItem.getIdentifier() == 6) {
                            showRecent(); //recent
                        } else if (drawerItem.getIdentifier() == 201) {
                            openpage(getResources().getString(R.string.link_item_fighting));
                        } else if (drawerItem.getIdentifier() == 202) {
                            openpage(getResources().getString(R.string.link_item_detective));
                        } else if (drawerItem.getIdentifier() == 203) {
                            openpage(getResources().getString(R.string.link_item_documental));
                        } else if (drawerItem.getIdentifier() == 204) {
                            openpage(getResources().getString(R.string.link_item_drama));
                        } else if (drawerItem.getIdentifier() == 205) {
                            openpage(getResources().getString(R.string.link_item_historic));
                        } else if (drawerItem.getIdentifier() == 206) {
                            openpage(getResources().getString(R.string.link_item_comedy));
                        } else if (drawerItem.getIdentifier() == 207) {
                            openpage(getResources().getString(R.string.link_item_criminal));
                        } else if (drawerItem.getIdentifier() == 208) {
                            openpage(getResources().getString(R.string.link_item_melodrama));
                        } else if (drawerItem.getIdentifier() == 209) {
                            openpage(getResources().getString(R.string.link_item_mult));
                        } else if (drawerItem.getIdentifier() == 210) {
                            openpage(getResources().getString(R.string.link_item_musicle));
                        } else if (drawerItem.getIdentifier() == 211) {
                            openpage(getResources().getString(R.string.link_item_ussr));
                        } else if (drawerItem.getIdentifier() == 212) {
                            openpage(getResources().getString(R.string.link_item_adventures));
                        } else if (drawerItem.getIdentifier() == 213) {
                            openpage(getResources().getString(R.string.link_item_family));
                        } else if (drawerItem.getIdentifier() == 214) {
                            openpage(getResources().getString(R.string.link_item_sport));
                        } else if (drawerItem.getIdentifier() == 215) {
                            openpage(getResources().getString(R.string.link_item_triller));
                        } else if (drawerItem.getIdentifier() == 216) {
                            openpage(getResources().getString(R.string.link_item_horror));
                        } else if (drawerItem.getIdentifier() == 217) {
                            openpage(getResources().getString(R.string.link_item_fantasy));
                        } else if (drawerItem.getIdentifier() == 218) {
                            openpage(getResources().getString(R.string.link_item_fantastika));
                        } else if (drawerItem.getIdentifier() == 301) {
                            openpage(getResources().getString(R.string.link_item_year2016));
                        } else if (drawerItem.getIdentifier() == 302) {
                            openpage(getResources().getString(R.string.link_item_year2015));
                        } else if (drawerItem.getIdentifier() == 303) {
                            openpage(getResources().getString(R.string.link_item_year2014));
                        } else if (drawerItem.getIdentifier() == 401) {
                            openpage(getResources().getString(R.string.link_item_serial_russian));
                        } else if (drawerItem.getIdentifier() == 402) {
                            openpage(getResources().getString(R.string.link_item_serial_notrussian));
                        }
                        return false;
                    }
                })
                .build();


        new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(drawerresult.getRecyclerView(), drawerresult.getDrawerItems());

      openpage(getResources().getString(R.string.link_item_startpage));
      // mAsyncTaskManager.setupTask(new OpenKinogoPageTask(getResources()));


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) clickgrid(position);
            }

        });




    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    //=======================================================================================


    public void clickgrid(Integer index){
        Item item;
        item=arrayList.get(index);
        getflv(item.pagelink);
        poster_url=item.pictureurl;
        poster_header=item.header;
        poster_sub_header=item.subheader;




      //  Log.d("ololo", );
    }

    private void opensearchpage(String link){

        new SearchKinogo().execute(link);

    }
    private void openpage(String link){
       //   new OpenKinogoPage().execute(link);
        new OpenKinogoPageTask(this).execute(link);

    }

    private void getflv(String httpurl){
        new GetPlayerTask().execute(httpurl);
    }


    private void showRecent(){
        Intent recent = new Intent(MainActivity.this, RecentActivity.class );
        startActivity(recent);

       // Log.d("ololo",  Integer.toString(new Select().from(DbItem.class).count()));

      //  recent.putExtra("headshot",mDbItem.mHeader);





    //   new Delete().from(FilmHeader.class).where("_id = ?", 1).execute();
  //   new Delete().from(FilmData.class).where("_id = ?", 1).execute();
      //  new Delete().from(FilmHeader.class).where("_id = ?", 2).execute();
     //   new Delete().from(FilmData.class).where("_id = ?", 2).execute();

        }





    private void viewSeriesList(){
        Intent playerIntent = new Intent(MainActivity.this, playlist.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("series", playList);
        playerIntent.putExtras(bundle);
        playerIntent.putExtra("poster_url",poster_url);
        playerIntent.putExtra("poster_header",poster_header);
        playerIntent.putExtra("poster_subheader",poster_sub_header);

        startActivity(playerIntent);

    }
//=================парсинг страницы перед запуском фильма. получение base64 плеера, ссылок.
    public class GetPlayerTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... parameter) {
            String flvurl="";
            for (String p : parameter)flvurl=p;

            try {

                Document document = Jsoup.connect(flvurl).userAgent(userAgent).get();

                    Elements encodeplayer = document.select(".box.visible");
                    Elements scriptElements = encodeplayer.tagName("script");
                    String elementString=scriptElements.html();
                   if (elementString.indexOf("<script")==0) {

                       String encodedstring = "";
                       String decodedstring = "";
                       for (Element encodedplayer : scriptElements)
                           encodedstring = encodedplayer.data();

                       encodedstring = encodedstring.substring(encodedstring.indexOf("'") + 1, encodedstring.length());
                       encodedstring = encodedstring.substring(0, encodedstring.indexOf("'"));


                       encodedstring = Mathem.base64_decode(encodedstring);
                       Document decodedplayerDoc = Jsoup.parse(encodedstring);
                       Elements decodedplayerElements = decodedplayerDoc.select(".uppod_style_video");
                       for (Element titleFromSite : decodedplayerElements) {
                           Element el = titleFromSite.select("param").last();
                           decodedstring = el.val();

                       }


                       if (decodedstring.indexOf("file=") > 0) { // если есть закодированный файл, раскодируем
                           String file = decodedstring.substring(decodedstring.indexOf("file=") + 5, decodedstring.indexOf("&poster"));
                           flvurl = Mathem.deup(file, Mathem.hash1);

                       } else if (decodedstring.indexOf("pl=") > 0) { // если есть плейлист, копируем название
                           flvurl = decodedstring.substring(decodedstring.indexOf("pl=") + 3, decodedstring.indexOf("&poster"));

                       }
                   }
                else if (elementString.indexOf("<iframe")==0){ //если фрейм с чужого сайта, ничего не делаем
                         Elements iframess = document.select(".box.visible");
                         Element framesElement = iframess.select("iframe").first();
                         flvurl = framesElement.attr("src");
                         flvurl=flvurl.substring(flvurl.indexOf("www."),flvurl.length());

                   }


                   }catch (IOException ex){
                ex.printStackTrace();
            }

            return flvurl;


        }




        protected void onPostExecute(String flvurl) {
            super.onPostExecute(flvurl);
          if (flvurl.indexOf(".flv")>0){

              checkWatchSingleFilm(poster_header,poster_url,flvurl);



              startplayer(flvurl); //если прямой линн, запускаем плеер
          }
                  else if (flvurl.indexOf(".txt")>0){

              new getPlaylist().execute(flvurl); // если  плейлист, получаем лист
          }

             }
    }


    public void checkWatchSingleFilm (String myHeader, String myPosterUrl, String myUrl){

        FilmHeader sHeader = new FilmHeader();
        FilmData sData = new FilmData();

        if (!sHeader.checkExistsDbItem(myHeader)) {
            sHeader.mHeader = myHeader;
            sHeader.mPosterUrl = myPosterUrl;
            sHeader.mSerialFlag = false;
            sHeader.mUrl = myUrl;
            sHeader.mDateWatch = new Date();
            sHeader.save();

            sData.mSubHeader="";
            sData.mUrl=myUrl;
            sData.mDuration=0;
            sData.mPosition=0;
            sData.myFilmHeader=sHeader;
            sData.save();
        }

        else {
            sHeader.updateDate(myHeader);
            Log.d("ololo", "обновили");
        }
    }

    public static void checkWatchSerialFilm (String myHeader, String myPosterUrl, String myUrl,String mySubHeader){

        FilmHeader sHeader = new FilmHeader();
        FilmData sData = new FilmData();

        if (!sHeader.checkExistsDbItem(myHeader)) {
            sHeader.mHeader = myHeader;
            sHeader.mPosterUrl = myPosterUrl;
            sHeader.mSerialFlag = true;
            sHeader.mUrl = myUrl;
            sHeader.mDateWatch = new Date();
            sHeader.save();

            sData.mSubHeader=mySubHeader;
            sData.mUrl=myUrl;
            sData.mDuration=0;
            sData.mPosition=0;
            sData.myFilmHeader=sHeader;
            sData.save();
        }

        else {
            sHeader.updateDate(myHeader);
            Log.d("ololo", "обновили");
        }
    }

  //===========парсинг строк из плейлиста тхт

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
//====================получение текста плейлиста тхт из тырнета
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

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }




  @Override
        protected void onPostExecute(Void aVoid)
        {
                  viewSeriesList(); // передаем в интент данные и открываем активити плейлиста

        }
    }

//===================парсинг страницы поискового результата

    public class SearchKinogo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... parameter) {
            String urlpage="";
            for (String p : parameter)urlpage=p;
            try {

                arrayList.clear();

               String normalUrl= URLEncoder.encode(urlpage, "Windows-1251");
               Document document = Jsoup.connect(searchpagelink+normalUrl).userAgent(userAgent).get();
               Elements shortstory = document.select(".shortstory");
               Elements headers = document.select(".zagolovki");
               Elements imgs = shortstory.select(".shortimg");

                for(Element titleFromSite:headers){// парсинг заголовков и ссылок на страницу с фильмом
                    String filmName = titleFromSite.text();
                    Element link= titleFromSite.select("a").first();
                    String linkHref = link.attr("href");

                    Item tempitem = new Item("","","","","","","","");//создание массива плиток фильмов
                    tempitem.header=filmName;
                    tempitem.pagelink=linkHref;
                    arrayList.add(tempitem);
                }

                Integer i=0;
                for(Element img:imgs){   //парсинг постеров и описания
                    if (imgs.size()==headers.size()){
                        String subtext=img.text();
                        Element link= img.select("img").first();
                        String linkHref = link.absUrl("src");
                        System.out.println(linkHref);
                        arrayList.get(i).subheader=subtext;
                        arrayList.get(i).pictureurl=linkHref;
                        i++;
                    }
                }

            }catch (IOException ex){
                ex.printStackTrace();
            }
            return urlpage;
        }
        @Override
        protected void onPostExecute(String urlpage) {
            CustomAdapter adapter = new CustomAdapter (MainActivity.this, arrayList);
            gv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

//==========парсит страницу с 10 фильмами
    public class OpenKinogoPage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...parameter) {
            String urlpage="";
            for (String p : parameter)urlpage=p;

            try {
                arrayList.clear();
                Document document = Jsoup.connect(urlpage).userAgent(userAgent).get();
                Elements shortstory = document.select(".shortstory");
                Elements headers = shortstory.select(".zagolovki");
                Elements imgs = shortstory.select(".shortimg");

                    for(Element titleFromSite:headers){ //получение названия фильма
                    if(titleFromSite.text().equals(""))   continue;
                        String filmName = titleFromSite.text();
                        Element link= titleFromSite.select("a").first();
                        String linkHref = link.attr("href");


                    Item tempitem = new Item("","","","","","","","");
                    tempitem.header=filmName;
                    tempitem.subheader=" ";
                    tempitem.pagelink=linkHref;
                     arrayList.add(tempitem);
                }
                Integer i=0;
                for(Element img:imgs){   // получение постера и описания
                    if (imgs.size()==headers.size()){
                        String subtext=img.text();
                        Element link= img.select("a").first();
                        String linkHref = link.attr("href");
                        arrayList.get(i).subheader=subtext;
                        arrayList.get(i).pictureurl=linkHref;
                    i++;
                    }
                }
                Elements buttons = document.select(".bot-navigation"); // получение ссылок на кнопки назад-вперед
                for(Element button:buttons){
                    Element link1= button.select("a").first();
                    GlobalData.Gd_back_pagelink = link1.attr("href");
                    Element link2= button.select("a").last();
                    GlobalData.Gd_forward_pagelink = link2.attr("href");
                }

                Element span= buttons.select("span").first();
                String temp = span.text();
                if (temp.equalsIgnoreCase("Раньше")) switchBackButton(false);
                   else switchBackButton(true);

                Element span2= buttons.select("span").last();
                String temp2 = span2.text();
                if (temp2.equalsIgnoreCase("Позже")) switchForwardButton(false);
                else switchForwardButton(true);

                }catch (IOException ex){
                ex.printStackTrace();
            }
            return urlpage;
        }
        @Override
        protected void onPostExecute(String urlpage) {
            CustomAdapter adapter = new CustomAdapter (MainActivity.this, arrayList);
            gv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
          if (drawerresult != null && drawerresult.isDrawerOpen()) {
              drawerresult.closeDrawer();
        } else {
            super.onBackPressed();
        }

    }
    public void back_click (View v) {

        openpage(GlobalData.Gd_back_pagelink);
    }
    public void forward_click (View v) {

       openpage(GlobalData.Gd_forward_pagelink);
    }

    private void switchBackButton(Boolean flag){

       if (flag) {
           Button backbutton=(Button) findViewById(R.id.backward);
           backbutton.setClickable(true);
//           backbutton.setTextColor(getResources().getColor(R.color.disabledbutton_fontcolor));
       } else {
           Button backbutton=(Button) findViewById(R.id.backward);
           backbutton.setClickable(false);
        //   backbutton.setTextColor(getResources().getColor(R.color.button_back_fontcolor));
       }


    }
    private void switchForwardButton(Boolean flag){
        if (flag) {
            Button forwardbutton = (Button) findViewById(R.id.forward);
            forwardbutton.setClickable(true);
       //   forwardbutton.setTextColor(getResources().getColor(R.color.disabledbutton_fontcolor));
        }
        else {
            Button forwardbutton = (Button) findViewById(R.id.forward);
            forwardbutton.setClickable(true);
        //   forwardbutton.setTextColor("#686666");
        }
    }

    //===========db work





    //==================

}
