package zut.cs.sys.dao;

import zut.cs.sys.base.dao.GenericDao;
import zut.cs.sys.domain.Picture;

public interface PictureDao extends GenericDao<Picture, Long> {
    Picture findByUrl(String url);

}