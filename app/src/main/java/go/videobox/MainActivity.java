package go.videobox;

import go.videobox.adapters.Item;
import go.videobox.adapters.MainPageGridViewAdapter;
import go.videobox.adapters.PlaylistItem;
import go.videobox.assynctasks.GetPlayerKinogo;
import go.videobox.assynctasks.GetPlaylistKinogo;
import go.videobox.assynctasks.SearchKinogoPageTask;
import go.videobox.base.ITaskLoaderListener;
import go.videobox.assynctasks.OpenKinogoPageTask;
import go.videobox.dbClass.FilmData;
import go.videobox.dbClass.FilmHeader;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.activeandroid.ActiveAndroid;
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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements ITaskLoaderListener {


    // private  String back_pagelink="";
  //  private  String forward_pagelink="";

    static String poster_url=""; //  ссылка на постер картинку
    static  String poster_header="";// название фильма
    static  String poster_sub_header="";// описание фильма


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
     GridView gv;


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



    @Override
    public void onCancelLoad() {
        Log.d("ololo", "task canceled");
    }

    private void setМainPageAdapter (){
        MainPageGridViewAdapter adapter = new MainPageGridViewAdapter (this, arrayList);
        gv=(GridView) findViewById(R.id.gridView1);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override  // сюда возвращает результат из таска любой.
    public void onLoadFinished(Object data) {
        if(data!=null && data instanceof Bundle){

              if (((Bundle) data).getString("header").equals("OpenKinogoPageTask")) {
                 arrayList = (ArrayList<Item>) ((Bundle) data).getSerializable("OpenKinogoPageTask");
                 setМainPageAdapter();
                 switchForwardButton(((Bundle) data).getBoolean("forward_state"));
                 switchBackButton(((Bundle) data).getBoolean("backward_state"));
              }
              if (((Bundle) data).getString("header").equals("SearchKinogoPageTask")) {
                  arrayList = (ArrayList<Item>) ((Bundle) data).getSerializable("SearchKinogoPageTask");
                  setМainPageAdapter();
              }
              if (((Bundle) data).getString("header").equals("GetPlaylistKinogo")) {
                  playList = (ArrayList<PlaylistItem>) ((Bundle) data).getSerializable("GetPlaylistKinogo");
                  viewSeriesList(); // передаем в интент данные и открываем активити плейлиста
              }
              if (((Bundle) data).getString("header").equals("GetPlayerKinogo")) {
                  String urlplayer = ((Bundle) data).getString("GetPlayerKinogo");

                  if (urlplayer.indexOf(".flv")>0){
                      checkWatchSingleFilm(poster_header,poster_url,urlplayer);
                      startplayer(urlplayer); //если прямой линн, запускаем плеер
                  }
                  else if (urlplayer.indexOf(".txt")>0){
                      Bundle myb = new Bundle();
                      myb.putString("urlpage",urlplayer);
                      myb.putString("poster_header",poster_header);
                      myb.putString("poster_url",poster_url);
                      GetPlaylistKinogo.execute(this, this,myb);
                  }
              }


//

        }
    }


    private static void dumpParams( Intent intent )
    {
        StringBuilder sb = new StringBuilder();
        Bundle extras = intent.getExtras();

        sb.setLength(0);
        sb.append("* dat=").append(intent.getData());
        sb.setLength(0);
        sb.append("* typ=").append(intent.getType());

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
              }
              if (key.equals("position")){
                    sb.setLength(0);
                    appendDetails( sb, extras.get( key ) );
                    String position =sb.toString();
                     p= Integer.parseInt(position);

              }

            }
            FilmHeader fs = new FilmHeader();
            fs.updatePositionFilm(poster_header,"",p,d); //применить позицию к фильму  (не сериалу)

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


      /*  FilmHeader sHeader = new FilmHeader();
        List<FilmHeader> setting = sHeader.getFifty();
        for (FilmHeader qitem: setting) {
            Log.d("ololo", qitem.mHeader + qitem.getId());
            List<FilmData> slist= qitem.getFilmList();
            for (FilmData qqq : slist) {
                Log.d("ololo", qqq.mSubHeader + qqq.getId());
                qqq.updatePosition(qqq.mSubHeader);

            }
        }
*/

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
    }

    private void opensearchpage(String link){

       // new SearchKinogo().execute(link);
        Bundle myb = new Bundle();
        myb.putString("urlpage",link);
        SearchKinogoPageTask.execute(this, this,myb);

    }
    private void openpage(String link){
       //   new OpenKinogoPage().execute(link);


     Bundle myb = new Bundle();
     myb.putString("urlpage",link);
     OpenKinogoPageTask.execute(this, this,myb);

    }

    private void getflv(String httpurl){
        Bundle myb = new Bundle();
        myb.putString("urlpage",httpurl);
        GetPlayerKinogo.execute(this, this,myb);
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
        Intent playerIntent = new Intent(MainActivity.this, PlaylistActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("series", playList);
        playerIntent.putExtras(bundle);
        playerIntent.putExtra("poster_url",poster_url);
        playerIntent.putExtra("poster_header",poster_header);
        playerIntent.putExtra("poster_subheader",poster_sub_header);

        startActivity(playerIntent);

    }
//=================парсинг страницы перед запуском фильма. получение base64 плеера, ссылок.



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
            Log.d("ololo", "записали новый фильм в хистори");

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
          System.out.println("назад тру");
//           backbutton.setTextColor(getResources().getColor(R.color.disabledbutton_fontcolor));
       } else {
           Button backbutton=(Button) findViewById(R.id.backward);
           backbutton.setClickable(false);
           System.out.println("назад нетру");
        //   backbutton.setTextColor(getResources().getColor(R.color.button_back_fontcolor));
       }


    }
    private void switchForwardButton(Boolean flag){
        if (flag) {
            Button forwardbutton = (Button) findViewById(R.id.forward);
            forwardbutton.setClickable(true);
            System.out.println("вперед тру");
       //   forwardbutton.setTextColor(getResources().getColor(R.color.disabledbutton_fontcolor));
        }
        else {
            Button forwardbutton = (Button) findViewById(R.id.forward);
            forwardbutton.setClickable(false);
            System.out.println("вперед нетру");
        //   forwardbutton.setTextColor("#686666");
        }
    }


}
