# tf-idf 词频逆文档词频频率

TF-IDF 如果一个词或者短语在一篇文章中出现的概率高，并且在其他文章中很少出现，则认为这个词或者短语具有很好的类别区分能力，适合用来分类。

TF-IDF 的作用， 用以评估一个词对于一个文件集或者一个语料库中的其中一个文件的重要程度

TF(term frequency) 指的是一个给定的词语在该文件中出现的频率

IDF(Inverse document frequency) 是一个词语普遍重要性的程度，某一个特定词语的 idf 可以由总文件数目初一包含该词语文件的数目， 再将得到的结果的商取以 10 为底的对数。

## TF 词条出现的频率

TF 表示词条在文本中出现的频率。 这个数字通常会被归一化， 以防止它偏向长的文件。

 $TF_{w} = \frac{词条 w 出现的次数}{所有词条的数量}$

![img](resource/1be9e26fe96f450e9cfda23f7d19807f.png)

## IDF 逆向文档的频率

![在这里插入图片描述](resource/cec13eb77d36439a8c3d2a61afed90be.png)

如果一个词越常见，那么分母就越大，逆文档频率就越小越接近0。分母之所以要加1，是为了避免分母为0（即所有文档都不包含该词）。log表示对得到的值取对数。

### TF-IDF

$$TF-IDF = TF * IDF$$

![img](resource/7c00bf60ac9545ab96acb95500ce75ec.png)

``` python
# 使用 tfidf 进行文本特征提取
import jieba
import pandas as pd
# 导入 tfidf 文本特征抽取器
from sklearn.feature_extraction.text import TfidfVectorizer


def word_cut(text: str) -> str:
    # [x for x in (jieba.cut(text))]
    # 使用 jieba 分词将 中文分割开来
    """太阳常常升起 => 太阳 常常 升起"""
    """中间没有空格的句子被分成一个一个单词， 然后使用空格再将单词连成句子"""

    return " ".join([x for x in (jieba.cut(text))])


def tf_idf_word():
    """使用 jieba 或者使用其他分词器"""
    raw_data = [
        '太阳常常升起',
        '俗话说“一图胜千言”。一张正确的图使思维导图更加直观',
        '新的头脑风暴模式允许用户在创意工厂里按组分类灵感。由此你可通过评估，组织和连接这些想法发现更多线索，隐藏的解决方案随即跃然纸上。头脑风暴的全屏模式有助于建立一个无压力的场景，让你全心全意关注脑海中闪烁的思维火花。',
    ]
    # data = []
    # for text in raw_data:
    #     data.append(word_cut(text))

    # 使用列表推导式生成 将 raw_data 经过 jieba 分词转为 data
    data = [word_cut(text) for text in raw_data]
    # 构建tf-idf特征抽取器并且传入停用词
    transfer = TfidfVectorizer(stop_words=['的'])
    data_new = transfer.fit_transform(data)
    # 输出转换之后的稀疏矩阵
    print("转换之后的词频:\n", data_new)
    # 获取转换之后的特征的名称
    print("转换之后的特征名称:\n", transfer.get_feature_names())
    # 将 转换之后的对象转为 pandas.DataFrame 对象
    df = pd.DataFrame(data_new.toarray(), columns=transfer.get_feature_names())
    print("pandas.DataFrame 对象:\n", df)
    # 将转换之后的数据转为二维矩阵
    print("将转换之后的特征转为二维矩阵", data_new.toarray())


if __name__ == '__main__':
    tf_idf_word()

```
