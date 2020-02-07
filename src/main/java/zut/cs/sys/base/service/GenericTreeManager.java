package zut.cs.sys.base.service;


import zut.cs.sys.base.domain.BaseTreeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Generic Manager that talks to GenericDao to CRUD POJOs.
 * <p>
 * <p>
 * Extend this interface if you want typesafe (no casting necessary) managers
 * for your dao objects.
 *
 * @param <T>  a type variable
 * @param <PK> the primary key for that type
 */
@SuppressWarnings("rawtypes")
public interface GenericTreeManager<T extends BaseTreeEntity, PK extends Serializable> extends GenericManager<T, PK> {

    /**
     * 获取制定id的全部祖先实体集合；
     *
     * @param id
     * @return
     */
    public List<T> getAncestors(PK id);

    /**
     * 获取制定id的直接孩子实体集合；
     *
     * @param id
     * @return
     */
    public List<T> getChildren(PK id);

    /**
     * 获取制定id的全部子孙实体集合；
     *
     * @param id
     * @return
     */
    public List<T> getDescendants(PK id);

    /**
     * 获取根节点实体集合
     *
     * @return
     */
    public List<T> getRoot();

    public void addChild(PK id, T t);

    public void updateChild(PK id, T t);


}
