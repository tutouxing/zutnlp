package zut.cs.sys.dao;

import zut.cs.sys.domain.Key_words;

import java.util.List;


public interface Key_wordsDao {
    public List<Key_words> getkeywords(String id);
}
