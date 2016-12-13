package go.videobox;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Collection;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import go.videobox.adapters.PlaylistItem;
import go.videobox.dbClass.FilmHeader;

public class MXPlayerResults {
    public static final String TAG						= "MX.IntentTest";

    public static final String RESULT_VIEW				= "com.mxtech.intent.result.VIEW";
    public static final int RESULT_ERROR				= Activity.RESULT_FIRST_USER + 0;

    public static final String EXTRA_DECODER			= "decode_mode";	// (byte)
    public static final String EXTRA_VIDEO 				= "video";
    public static final String EXTRA_EXPLICIT_LIST		= "video_list_is_explicit";
    public static final String EXTRA_DURATION			= "duration";
    public static final String EXTRA_SUBTITLES			= "subs";
    public static final String EXTRA_SUBTITLE_NAMES 	= "subs.name";
    public static final String EXTRA_SUBTITLE_FILENAMES = "subs.filename";
    public static final String EXTRA_ENABLED_SUBTITLES	= "subs.enable";
    public static final String EXTRA_POSITION			= "position";
    public static final String EXTRA_RETURN_RESULT		= "return_result";
    public static final String EXTRA_HEADERS			= "headers";
    public static final String EXTRA_END_BY				= "end_by";
    public static final String EXTRA_VIDEO_ZOOM			= "video_zoom";
    public static final String EXTRA_DAR_HORZ			= "DAR_horz";
    public static final String EXTRA_DAR_VERT			= "DAR_vert";
    public static final String EXTRA_STICKY				= "sticky";
    public static final String EXTRA_ORIENTATION 		= "orientation";
    public static final String EXTRA_SUPPRESS_ERROR_MESSAGE = "suppress_error_message";
    public static final String EXTRA_SECURE_URI 		= "secure_uri";
    public static final String EXTRA_KEYS_DPAD_UPDOWN 	= "keys.dpad_up_down";

    public static final String EXTRA_LIST				= "video_list";

    public static final String EXTRA_TITLE				= "title";
    public static final String EXTRA_TITLES				= "video_list.name";

    public static final String EXTRA_SIZE               = "size";
    public static final String EXTRA_SIZES              = "video_list.size";

    public static final String EXTRA_FILENAME           = "filename";
    public static final String EXTRA_FILENAMES          = "video_list.filename";

    public static final String EXTRA_HASH_OPENSUBTITLES	= "hash.opensubtitles";
    public static final String EXTRA_HASHES_OPENSUBTITLES = "video_list.hash.opensubtitles";

    private static final int ORIENTATION_LANDSCAPE					= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    private static final int ORIENTATION_REVERSE_LANDSCAPE			= ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
    private static final int ORIENTATION_AUTO_ROTATION_LANDSCAPE	= ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
    private static final int ORIENTATION_PORTRAIT					= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private static final int ORIENTATION_REVERSE_PORTRAIT			= ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
    private static final int ORIENTATION_AUTO_ROTATION_PORTRAIT		= ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private static final int ORIENTATION_AUTO_ROTATION				= ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
    private static final int ORIENTATION_SYSTEM_DEFAULT				= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    private static final int ORIENTATION_MATCH_VIDEO_ORIENTATION	= 99999;
    private static final String 	PACKAGE_NAME_PRO 		= "com.mxtech.videoplayer.pro";
    private static final String 	PACKAGE_NAME_AD 		= "com.mxtech.videoplayer.ad";
    private static final String 	PLAYBACK_ACTIVITY_PRO	= "com.mxtech.videoplayer.ActivityScreen";
    private static final String 	PLAYBACK_ACTIVITY_AD	= "com.mxtech.videoplayer.ad.ActivityScreen";

//-----------------------------------------------------------------------------------
    public static void dumpParams( Intent intent,String poster_header, String poster_sub_header) {
        StringBuilder sb = new StringBuilder();
        Bundle extras = intent.getExtras();
        sb.setLength(0);
        sb.append("* dat=").append(intent.getData());
        sb.setLength(0);
        sb.append("* typ=").append(intent.getType());
        if( extras != null && extras.size() > 0 ){
            sb.setLength(0);
            Integer p=1;
            Integer d=1;
            int i = 0;
            for( String key : extras.keySet() ){
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
            FilmHeader mfilm = new FilmHeader();
            mfilm.updatePositionFilm(poster_header,poster_sub_header,p,d);

            for (PlaylistItem plitem: PlaylistActivity.playList)
                if ((plitem.mSubHeader.equals(poster_sub_header))&&(plitem.mHeader.equals(poster_header)))   {
                    plitem.mPosition=p;
                    plitem.mDuration=d;
            }


            Log.d("ololo",poster_header+"/"+poster_sub_header+"position - "+p);//  описание.
              }
    }

//-----------------------------------------------------------------------------------
    private static void appendDetails( StringBuilder sb, Object object )
    {
        if( object != null && object.getClass().isArray() ) {
            sb.append('[');
            int length = Array.getLength(object);
            for( int i = 0; i < length; ++i ){
                if( i > 0 )  sb.append(", ");
                appendDetails(sb, Array.get(object, i));
            }
            sb.append(']');
        }
        else if( object instanceof Collection){
            sb.append('[');
            boolean first = true;
            for( Object element : (Collection)object ) {
                if( first )first = false;
                else  sb.append(", ");
                appendDetails(sb, element);
            }

            sb.append(']');
        }
        else
            sb.append(object);
    }
//-----------------------------------------------------------------------------------

    public static class MXOptions
    {
        static final byte NO_DECODER		= 0;
        static final byte DECODER_HW		= 1;
        static final byte DECODER_SW		= 2;

        static final int NO_ZOOM			= -1;
        static final int ZOOM_FIT_TO_SCREEN = 1;
        static final int ZOOM_STRETCH 		= 0;
        static final int ZOOM_CROP 			= 3;
        static final int ZOOM_ORIGINAL 		= 2;

        static final int kActionKeyUpdownVolume = 0;
        static final int kActionKeyUpdownNextPrev = 1;


        Integer 	resumeAt;
         byte		decoder;
        Boolean		video;
        boolean		explicitList;
        int 		videoZoom;
        float 		DAR_horz;
        float 		DAR_vert;
        Boolean 	sticky;
        Integer		orientation;
        boolean 	suppressErrorMessage;
        boolean 	secureUri;
        String[] 	headers;
        Integer		UpDownAction;

        MXOptions() {
            this.decoder = NO_DECODER;
            this.videoZoom = NO_ZOOM;
        }
//------------------------------------------------------------------------------
        void putToIntent( Intent intent ) {
            if( resumeAt != null )
                intent.putExtra(EXTRA_POSITION, resumeAt);

            if( decoder != NO_DECODER )
                intent.putExtra(EXTRA_DECODER, decoder);

            if( video != null )
                intent.putExtra(EXTRA_VIDEO, video);

            if( explicitList )
                intent.putExtra(EXTRA_EXPLICIT_LIST, true);

            if( videoZoom != NO_ZOOM )
                intent.putExtra(EXTRA_VIDEO_ZOOM, videoZoom);

            if( DAR_horz > 0 && DAR_vert > 0 )
            {
                intent.putExtra(EXTRA_DAR_HORZ, DAR_horz);
                intent.putExtra(EXTRA_DAR_VERT, DAR_vert);
            }

            if( sticky != null )
                intent.putExtra(EXTRA_STICKY, sticky);

            if( orientation != null )
                intent.putExtra(EXTRA_ORIENTATION, orientation);

            if( suppressErrorMessage )
                intent.putExtra(EXTRA_SUPPRESS_ERROR_MESSAGE, true);

            if( secureUri )
                intent.putExtra(EXTRA_SECURE_URI, true);

            if( headers != null )
                intent.putExtra(EXTRA_HEADERS, headers);

            if( UpDownAction != null )
                intent.putExtra( EXTRA_KEYS_DPAD_UPDOWN, UpDownAction );
        }
    }



//------------------------------------------------------------------------
public static class MXPackageInfo
{
    final String packageName;
    final String activityName;

    MXPackageInfo( String packageName, String activityName ) {
        this.packageName = packageName;
        this.activityName = activityName;
    }
}

    private static final MXPackageInfo[] PACKAGES = {
            new MXPackageInfo(PACKAGE_NAME_PRO, PLAYBACK_ACTIVITY_PRO),
            new MXPackageInfo(PACKAGE_NAME_AD, PLAYBACK_ACTIVITY_AD),
    };



    public static MXPackageInfo getMXPackageInfo(Context context)
    {
        for( MXPackageInfo pkg: PACKAGES )
        {
            try
            {
                ApplicationInfo info = context.getPackageManager().getApplicationInfo(pkg.packageName, 0);
                if( info.enabled )
                    return pkg;
                else
                    Log.v( TAG, "MX Player package `" + pkg.packageName + "` is disabled." );
            }
            catch(PackageManager.NameNotFoundException ex)
            {
                Log.v( TAG, "MX Player package `" + pkg.packageName + "` does not exist." );
            }
        }

        return null;
    }


}
