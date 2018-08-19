package com.wangyi.Dao;

import com.wangyi.domain.Board;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public class BoardDao extends BaseDao<Board> {
    private static final String GET_BOARD_NUM = "select count(f.boardId) from Board f " ;
    public long getBoardNum(){
        Iterator iter = getHibernateTemplate().iterate(GET_BOARD_NUM);
        return ((long)iter.next());
    }

}
