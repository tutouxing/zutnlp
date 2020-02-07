package zut.cs.sys.base.rest;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.domain.BaseTreeEntity;
import zut.cs.sys.base.service.GenericTreeManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
@RestController
public abstract class GenericTreeController<T extends BaseTreeEntity<T>, PK extends Serializable, M extends GenericTreeManager<T, PK>>
        extends GenericController<T, PK, M> {

    protected M treeManager;

    /**
     * 得到树结构;
     *
     * @return
     * @paramrequest
     * @paramresponse
     */

    @ApiOperation(value = "通过ID得到本实体的所有孩子")
    @GetMapping("/getChildren/{id}")
    public List<T> getChildren(@PathVariable PK id) {
        List<T> result = this.treeManager.getChildren(id);
        return result;
    }

    /**
     * 得到树结构;
     *
     * @return
     * @paramrequest
     * @paramresponse
     */
    @ApiOperation(value = "通过ID得到本实体的所有孩子")
    @GetMapping("/getTree/{id}")
    public List<T> getTree(@PathVariable PK id) {
        List<T> result = null;
        if (id == null) {
            result = this.treeManager.getRoot();
        } else {
            T node = this.treeManager.findById(id);
            result = node.getChildren();
        }
        return result;
    }

    @ApiOperation(value = "删除孩子节点")
    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable PK id) throws IOException {
        T t = this.treeManager.findById(id);
        if (t.getChildren().size() > 0)
            return;
        this.treeManager.delete(id);
    }

    @ApiOperation(value = "添加孩子节点")
    @PostMapping("/addChild/{id}")
    public void addChild(@PathVariable PK id, @RequestBody T t) {
        this.treeManager.addChild(id, t);
    }

    @ApiOperation(value = "更改孩子节点")
    @PutMapping("updateChild/{id}")
    public void updateChild(@PathVariable PK id, @RequestBody T t) {
        this.treeManager.updateChild(id, t);
    }
}
