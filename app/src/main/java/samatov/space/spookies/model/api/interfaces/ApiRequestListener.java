package samatov.space.spookies.model.api.interfaces;

public interface ApiRequestListener {
    void onRequestComplete(Object result, Throwable e);
}
