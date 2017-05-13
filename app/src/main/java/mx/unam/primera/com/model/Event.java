package mx.unam.primera.com.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Samuel on 12/05/2017.
 */

public class Event
{
    private String _id;
    private String _name;
    private Date _sch;
    private EventType _type;
    private List<Channel> _channelList;

    public Event()
    {
        _channelList = new ArrayList<>();
    }

    //region Propierties
    public String getId()
    {
        return this._id;
    }

    public void setId(String value)
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

    public Date getDate()
    {
        return this._sch;
    }

    public void setDate(Date value)
    {
        this._sch = value;
    }

    public EventType getType() { return this._type; }

    public void setType(EventType value)
    {
        this._type = value;
    }

    public List<Channel> getChannelList()
    {
        return this._channelList;
    }

    public void setChannelList(List<Channel> channelList)
    {
        this._channelList = channelList;
    }
    //endregion
}
