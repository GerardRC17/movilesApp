package mx.novaterra.mobile.com.model;

import java.net.URL;

/**
 * Created by Samuel on 12/05/2017.
 */

public class Channel
{
    private int _id;
    private String _name;
    private String _abv;
    private URL _imgPath;
    private URL _br;

    private static final String DEFAULT_CHANNEL_IMG = "http://18.221.160.127/guide-ws/img/web_hi_res_512.png";

    public Channel()
    {
        _imgPath = null;
        _br = null;
    }

    //region Propierties
    public int getId()
    {
        return this._id;
    }

    public void setId(int value)
    {
        this._id = value;
    }

    public String getName()
    {
        return this._name;
    }

    public void setName(String value)
    {
        this._name = value;
    }

    public String getAbbreviation()
    {
        return this._abv;
    }

    public void setAbbreviation(String value)
    {
        this._abv = value;
    }

    public URL getImageUrl()
    {
        return this._imgPath;
    }
    public void setImageUrl(URL imageUrl)
    {
        this._imgPath = imageUrl;
    }

    public URL getBroadcastUrl()
    {
        return this._br;
    }
    public void setBroadcastUrl(URL broadcast)
    {
        this._br = broadcast;
    }

    public void setDefaultImage()
    {
        try {
            String s = this.DEFAULT_CHANNEL_IMG;
            this._imgPath = new URL(s);
        } catch (Exception ex)
        {

        }
    }
    //endregion
}
