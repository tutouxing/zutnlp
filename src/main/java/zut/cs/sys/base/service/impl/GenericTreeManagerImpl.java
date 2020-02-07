package zut.cs.sys.base.service.impl;

import org.springframework.transaction.annotation.Transactional;
import zut.cs.sys.base.dao.GenericTreeDao;
import zut.cs.sys.base.domain.BaseTreeEntity;
import zut.cs.sys.base.service.GenericTreeManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as the Base class for all other Managers - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>
 * To register this class in your Spring context file, use the following XML.
 *
 * @param <T>  a type variable
 * @param <PK> the primary key for that type
 */
@Transactional
public class GenericTreeManagerImpl<T extends BaseTreeEntity<T>, PK extends Serializable>
        extends GenericManagerImpl<T, PK> implements GenericTreeManager<T, PK> {

    protected GenericTreeDao<T, PK> treeDao;

    @Override
    public List<T> getAncestors(PK id) {
        List<T> ancestors = new ArrayList<T>();
        T entity = this.treeDao.getOne(id);
        T parent = entity.getParent();
        while (parent != null) { // 追述祖先
            ancestors.add(parent);
            entity = parent;
            parent = entity.getParent();
        }
        return ancestors;
    }

    @Override
    public List<T> getChildren(PK id) {
        T entity = this.treeDao.getOne(id);
        return entity.getChildren();
    }

    @Override
    public List<T> getDescendants(PK id) {
        List<T> descendants = new ArrayList<T>();
        List<T> children = this.getChildren(id);
        if (children != null && children.size() > 0) { // 追加子孙
            descendants.addAll(children);
            for (T t : children) {
                @SuppressWarnings("unchecked")
                List<T> descendants2 = this.getDescendants((PK) t.getId());
                descendants.addAll(descendants2);
            }
        }
        // if (children != null && children.size() > 0) {
        // descendants.addAll(children);
        // for (T t : children) {
        // @SuppressWarnings("unchecked")
        // List<T> descendants2 = this.getDescendants((PK) t.getId());
        // descendants.addAll(descendants2);
        // }
        // }
        return descendants;
    }

    @Override
    public List<T> getRoot() {
        return this.treeDao.getRoot();
    }

    @Override
    public void addChild(PK id, T t) {
        T t1 = this.treeDao.getOne(id);
        t.setParent(t1);
        this.treeDao.save(t);
    }

    @Override
    public void updateChild(PK id, T t) {
        T t1 = this.treeDao.getOne(id);
        t.setParent(t1.getParent());
        t.setId((Long) id);
        this.treeDao.save(t);
    }
}
