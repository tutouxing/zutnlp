from sklearn.feature_extraction.text import TfidfVectorizer
import jieba
import codecs
import sys


def cut_word(corpus, path):
    after_cutword_corpus = []
    stopwords = {}.fromkeys(
        [line.strip() for line in codecs.open(path, encoding='UTF-8')])  # 停用词表
    for sentence in corpus:
        seglist = jieba.cut(sentence, cut_all=False)  # 精确模式
        output = ''
        for segs in seglist:
            seg = segs.lower()  # 英文字母小写
            if seg not in stopwords:  # 去停用词
                if len(seg) > 1:  # 去掉分词为1个字的结果
                    output += seg
                    output += ' '
        after_cutword_corpus.append(output)
    return after_cutword_corpus


def get_TF_IDF(corpus, path):
    corpus = cut_word(corpus, path)
    tfidf_vec = TfidfVectorizer()
    tfidf_matrix = tfidf_vec.fit_transform(corpus)
    # 得到语料库所有不重复的词
    unique_allwords = tfidf_vec.get_feature_names()
    # print(unique_allwords)
    # 得到每个单词对应的id值
    # print(tfidf_vec.vocabulary_)
    # 得到每个句子所对应的向量
    # 向量里数字的顺序是按照词语的id顺序来的
    # print(tfidf_matrix.toarray())
    for i in range(len(tfidf_matrix.toarray())):
        # print(u"-------这里输出第", i, u"类文本的词语tf-idf权重------")
        tfidf_dict = {}
        for j in range(len(unique_allwords)):
            if tfidf_matrix.toarray()[i][j]!=0:
                tfidf_dict.setdefault(unique_allwords[j], tfidf_matrix.toarray()[i][j])
        for key, value in tfidf_dict.items():
            print(key)
            print(value)


if __name__ == '__main__':
    corpus = [
        "我来到北京清华大学。",
        "他来到了网易杭研大厦。",
        "小明硕士毕业与中国的科学院。",
        "我爱北京天安门了。"
    ]
    get_TF_IDF(sys.argv[2:], sys.argv[1])





