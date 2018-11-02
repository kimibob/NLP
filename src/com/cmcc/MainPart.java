package com.cmcc;

import edu.stanford.nlp.ling.IndexedWord;

public class MainPart
{
    /**
     * 主语
     */
    public IndexedWord subject;
    /**
     * 谓语
     */
    public IndexedWord predicate;
    /**
     * 宾语
     */
    public IndexedWord object;

    /**
     * 结果
     */
    public String result;

    public MainPart(IndexedWord subject, IndexedWord predicate, IndexedWord object)
    {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public MainPart(IndexedWord predicate)
    {
        this(null, predicate, null);
    }

    public MainPart()
    {
        result = "";
    }

    /**
     * 结果填充完成
     */
    public void done()
    {
        result = predicate.toString();
        if (subject != null)
        {
            result = subject.toString() + " " + result;
        }
        if (object != null)
        {
            result = result  +" "+ object.toString();
        }
    }

    public boolean isDone()
    {
        return result != null;
    }

    @Override
    public String toString()
    {
        //if (result != null) return result;
        return "MainPart{" +
                "主语='" + subject + '\'' +
                ", 谓语='" + predicate + '\'' +
                ", 宾语='" + object + '\'' +
                '}';
    }
}