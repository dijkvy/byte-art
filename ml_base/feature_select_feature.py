# 低方差过滤

import pandas as pd
# 低方差特征过滤器
from sklearn.feature_selection import VarianceThreshold


def variance_():
    """低方差特征过滤，"""
    # 1. 获取数据
    # 2. 实例化转换器类
    # 3. 调用 fit_transform

    # 构造数据
    data = pd.read_csv('train.csv')
    data = data[['Fare']]
    print("特征数量:\n", data.shape)
    # 构造低方差特征过滤器(这里是过滤掉方差小于 2000 特征的列)
    transfer = VarianceThreshold(threshold=2000)
    # 调用转换器
    data = transfer.fit_transform(data)
    print(data.shape)
    pass


if __name__ == '__main__':
    variance_()
