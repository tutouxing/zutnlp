package zut.cs.sys.domain;

/**
 * 进一个实体对应文档的关键字和词频
 */
public class Key_words {
    public String name;
    public double value;
    public Key_words(String name,double value){
        this.name=name;
        this.value=value;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }
    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Key_words{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

}
