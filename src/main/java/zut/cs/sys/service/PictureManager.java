package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.domain.Picture;

public interface PictureManager extends GenericManager<Picture, Long> {
    /**
     * @Description: java类作用描述
     * @Author: yc
     * @CreateDate: 2019/5/14$ 15:29$
     */
    Picture findByUrl(String url);
}