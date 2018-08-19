package com.wangyi.Dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {
    private static int DEFAULT_PAGE_SIZE = 20 ;

    private int pageSize = DEFAULT_PAGE_SIZE ;  //每页的记录数

    private long start ;  //当前页第一条数据在list中的位置,从0开始

    private List data ; //当前页中存放的记录,类型一般为list

    private long totalCount ; //总记录数

    public Page(){
        this(0,0,DEFAULT_PAGE_SIZE,new ArrayList());
    }

    public Page(long start,long totalCount,int pageSize,  List data) {
        this.pageSize = pageSize;
        this.start = start;
        this.data = data;
        this.totalCount = totalCount;
    }

    public long getTotalCount(){
        return this.totalCount ;
    }

    public long getTotalPageCount(){
        if(totalCount % pageSize == 0){
            return totalCount/pageSize ;
        }
        return totalCount / pageSize +1 ;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List getResult() {
        return data;
    }
    public long getCurrentPageNo(){return start/pageSize +1 ;}

    public boolean isHasNextPage(){return this.getCurrentPageNo()<this.getTotalPageCount();}

    public boolean isHasPreviousPage(){
        return this.getCurrentPageNo()>1 ;
    }

    public static int getStartOfPage(int pageNo){
        return getStartOfPage(pageNo,DEFAULT_PAGE_SIZE);
    }

    public static int getStartOfPage(int pageNo,int pageSize){
        return (pageNo -1)*pageSize ;

    }

}
