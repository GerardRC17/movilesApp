package mx.unam.primera.com.model;

/**
 * Created by Samuel on 12/05/2017.
 */

public class EventType
{
    private int  _id;
    private String _name;
    private int _imgr;
    private String _strColor;

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

    public int getImageResource()
    {
        return this._imgr;
    }
    public void setImageResource(int resource)
    {
        this._imgr = resource;
    }

    public String getColorHex()
    {
        return this._strColor;
    }
    public void setColorHex(String value)
    {
        this._strColor = value;
    }
}
