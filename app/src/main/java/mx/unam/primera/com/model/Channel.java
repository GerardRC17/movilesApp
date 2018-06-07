package mx.unam.primera.com.model;

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

    public Channel()
    {
        _imgPath = null;
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
    //endregion
}
