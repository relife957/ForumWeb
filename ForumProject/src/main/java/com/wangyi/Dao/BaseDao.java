package com.wangyi.Dao;


import org.hibernate.Session;
import org.hibernate.Query ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DAO基类,
 * @param <T>
 */
public class BaseDao<T> {
    private Class<T> entityClass ;

    private HibernateTemplate hibernateTemplate ;

    /**
     * 通过反射确定子类的类型
     */
    public BaseDao(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        entityClass = (Class)params[0] ;
    }

    public T load(Serializable id){
        return (T) getHibernateTemplate().load(entityClass,id);
    }

    public T get(Serializable id){
        return (T) getHibernateTemplate().get(entityClass,id) ;
    }

    public List<T> loadAll(){
        return getHibernateTemplate().loadAll(entityClass);
    }

    public void save(T entity){
        getHibernateTemplate().save(entity);
    }

    public void remove(T entity){
        getHibernateTemplate().delete(entity);
    }
    public void removeAll(String tableName){
        getSession().createSQLQuery("truncate TABLE " + tableName+"").executeUpdate();
    }

    public void update(T entity){
        getHibernateTemplate().update(entity);
    }

    public List find(String hql,Object... params){
        return this.getHibernateTemplate().find(hql,params);
    }

    /**
     * 对延迟加载的实体进行初始化
     * @param entity
     */
    public void initialize(Object entity){
        this.getHibernateTemplate().initialize(entity);
    }

    public Page pagedQuery(String hql,int pageNo,int pageSize,Object... values){
        Assert.hasText(hql);
        Assert.isTrue(pageNo>=1,"pageNo should start from 1");

        String countQueryString = " select count(*) "+removeSelect(removeOrders(hql));
        List countList = getHibernateTemplate().find(countQueryString,values);
        long totalCount = (Long)countList.get(0) ;

        if(totalCount <1){
            return new Page();
        }

        int startIndex = Page.getStartOfPage(pageNo,pageSize);
        Query query = createQuery(hql,values);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

        return new Page(startIndex,totalCount,pageSize,list);
    }

    private Query createQuery(String hql, Object[] values) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql) ;
        for(int i = 0 ; i< values.length;i++){
            query.setParameter(i,values[i]);
        }
        return query ;
    }

    /**
     * 去除hql中from后的语句
     * @param hql
     * @return
     */
    private String removeSelect(String hql) {
        Assert.hasText(hql);
        int beginPos = hql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1,"hql : "+hql + " must have a keyword 'from' ");
        return hql.substring(beginPos) ;
    }

    /**
     * 去除
     * @param hql
     * @return
     */
    private String removeOrders(String hql) {
        Assert.hasText(hql);
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()){
            m.appendReplacement(sb,"");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public  HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate ;
    }

    @Autowired
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate){
        this.hibernateTemplate = hibernateTemplate ;
    }
    public Session getSession(){
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }
}
