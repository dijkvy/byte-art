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
