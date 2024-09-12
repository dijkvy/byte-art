# 主成分分析(PCA)

高维数据转化为低维数据的过程中， 有可能舍弃原有的数据，创造新的数据

作用: 是数据维数压缩，尽可能降低原数据的维度，损失少量的信息

应用于: 回归分析或者聚类分析当中

``` python
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


```
