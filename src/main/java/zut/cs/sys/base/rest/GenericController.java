package zut.cs.sys.base.rest;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.domain.BaseEntity;
import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.util.UpdateUtil;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@RestController
public abstract class GenericController<T extends BaseEntity, PK extends Serializable, M extends GenericManager<T, PK>> {
    protected PK id;
    protected M manager;
    protected T model;
    protected Page<T> page;

    protected Pageable pageable;
    protected int pageNumber = 0;
    protected int pageSize = 20;

    /**
     * @param model
     * @return
     */
    @ApiOperation(value = "创建实体")
    @PostMapping("/")
    public T create(@RequestBody T model) {
        this.model = model;
        Date date = new Date();
        this.model.setDateCreated(date);
        this.model.setDateModified(date);
        this.model = this.manager.save(this.model);
        return this.model;
    }

    /**
     * @param id
     * @throws IOException
     */
    @ApiOperation(value = "通过ID删除实体")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable PK id) throws IOException {
        this.manager.delete(id);
    }

    /**
     * 根据输入，返回分页结果中的当前页，包括当前页信息和其中的实体对象集合
     *
     * @return
     * @paramrequest
     * @paramresponse
     */
    @ApiOperation(value = "得到分页列表")
    @GetMapping(value = "/")
    public Page<T> get(@RequestParam(name = "page", defaultValue = "0") String pageNumber,
                       @RequestParam(name = "limit", defaultValue = "20") String pageSize) {
        if (StringUtils.isNotBlank(pageNumber)) {
            this.pageNumber = Integer.valueOf(pageNumber) - 1;
        }
        if (StringUtils.isNotBlank(pageSize)) {
            this.pageSize = Integer.valueOf(pageSize);
        }
        this.pageable = new PageRequest(this.pageNumber, this.pageSize, Sort.Direction.ASC, "id");
        this.page = this.manager.findAll(this.pageable);
        return this.page;
    }


    /**
     * @param id
     * @return
     */
    @ApiOperation(value = "通过ID得到实体")
    @GetMapping("/{id}")
    public T getOne(@PathVariable PK id) {
        return this.manager.findById(id);
    }

    /**
     * @param id
     * @param model
     * @return
     */
    @ApiOperation(value = "通过ID修改实体")
    @PutMapping("/{id}")
    public T update(@PathVariable PK id, @RequestBody T model) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        model.setId(Long.valueOf(id.toString()));
        T t = this.manager.findById(id);
        model.setDateModified(new Date());// 更新修改时间
        T res = (T) UpdateUtil.get(model, t);
        res.setId((Long) id);
        this.model = this.manager.save(res);
        return res;
    }

}