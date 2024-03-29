package fr.wati.yacramanager.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DateIterator
   implements Iterator<Date>, Iterable<Date>
{

    private Calendar end = Calendar.getInstance();
    private Calendar current = Calendar.getInstance();

    public DateIterator(Date start, Date end)
    {
        this.end.setTime(end);
        this.end.add(Calendar.DATE, -1);
        this.current.setTime(start);
        this.current.add(Calendar.DATE, -1);
    }

    public boolean hasNext()
    {
        return !current.after(end);
    }

    public Date next()
    {
        current.add(Calendar.DATE, 1);
        return current.getTime();
    }

    public void remove()
    {
        throw new UnsupportedOperationException(
           "Cannot remove");
    }

    public Iterator<Date> iterator()
    {
        return this;
    }
} 