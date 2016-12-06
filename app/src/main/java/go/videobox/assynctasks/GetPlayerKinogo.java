package go.videobox.assynctasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import go.videobox.adapters.Item;
import go.videobox.Mathem;
import go.videobox.base.AbstractTaskLoader;
import go.videobox.base.ITaskLoaderListener;
import go.videobox.base.TaskProgressDialogFragment;


public class GetPlayerKinogo extends AbstractTaskLoader { // получение флеш плеера со страницы и ссылки из него
    private Bundle mBundle;

    private final static String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private ArrayList<Item> arrayList = new ArrayList<>();

    public static void execute(FragmentActivity fa, ITaskLoaderListener taskLoaderListener, Bundle arguments) {

        GetPlayerKinogo loader = new GetPlayerKinogo(fa,arguments);

        new TaskProgressDialogFragment.Builder(fa, loader,"Wait...")
                .setCancelable(true)
                .setTaskLoaderListener(taskLoaderListener)
                .show();
    }

    protected GetPlayerKinogo(Context context, Bundle arguments) {
        super(context);
        if (arguments != null)  this.mBundle=arguments;
    }

    @Override
    public Object loadInBackground() {
        String flvurl = mBundle.getString("urlpage");

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
                flvurl = flvurl.substring(flvurl.indexOf("www."),flvurl.length());

            }



            if (isCanselled()) {
                //      break;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

        Bundle mbundle = new Bundle();
        mbundle.putString("header","GetPlayerKinogo");
        mbundle.putString("GetPlayerKinogo",flvurl);
        return mbundle;
    }

    @Override
    public Bundle getArguments() {
        return null;
    }

    @Override
    public void setArguments(Bundle args) {

    }


}
