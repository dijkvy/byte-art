import pandas as pd
# PCA 主成分分析类
from sklearn.decomposition import PCA


def pca_():
    # 导入数据
    data = pd.read_csv('train.csv')
    # print(data.head(n=4))
    print(data.columns)
    # 提取数据集中的三个特征
    data = data[['Fare', 'Age', 'Pclass']]
    # 去掉空集合
    data = data.dropna()
    print(data.shape)

    # n_components=None
    # n_components 参数为小数时， 表示保留百分之多少的信息量
    # n_components 参数为整数时， 表示保留多少个特征
    transfer = PCA(n_components=2)
    # 进行特征转换
    data_new = transfer.fit_transform(data)
    print(data_new)


if __name__ == '__main__':
    pca_()
