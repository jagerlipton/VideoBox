package go.videobox.base;

public interface ITaskLoaderListener {
    void onLoadFinished(Object data);
    void onCancelLoad();
}