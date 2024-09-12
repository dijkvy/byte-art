# 对文本数据进行特征值化
import jieba
import pandas as pd

# 统计样本中每个单词出现的次数
from sklearn.feature_extraction.text import CountVectorizer


def count_vector():
    # 样本数据
    data = ['life is short, i like like python', 'life is long, i like rust']
    # 初始化 CountVectorizer 对象
    transfer = CountVectorizer(stop_words=[])

    # 参数为 文本 或者包含文本的可迭代对象， 返回的是 词频的稀疏矩阵
    data_new = transfer.fit_transform(data)
    # 打印转换之后的数据
    print("返回转换之后的数据:\n", data_new)
    # 打印转换之后的数据类型
    print("返回转换之后的数据数据类型:\n", type(data_new))
    # 将转换之后的数据转为二维矩阵
    print("将转换之后的数据转为二维数组:\n", data_new.toarray())
    # 打印转换之后的特征名称
    print("获取特征名称:\n", transfer.get_feature_names())
    # 构建 pandas.DataFrame 对象
    print("将样本转为 DataFrame:\n", pd.DataFrame(data_new.toarray(), columns=transfer.get_feature_names()))
    # 获取转换之前的数据
    print("获取转换之前的是数据:\n", transfer.inverse_transform(data_new))


def word_cut(text: str) -> str:
    # [x for x in (jieba.cut(text))]
    # 使用 jieba 分词将 中文分割开来
    """太阳常常升起 => 太阳 常常 升起"""
    """中间没有空格的句子被分成一个一个单词， 然后使用空格再将单词连成句子"""

    return " ".join([x for x in (jieba.cut(text))])


def count_vector_chinese():
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
    # 构建词频统计器并且传入停用词
    transfer = CountVectorizer(stop_words=['的'])
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
    # count_vector()
    count_vector_chinese()
