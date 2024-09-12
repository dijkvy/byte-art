import pandas as pd
# 导入 minmax 归一化处理器
from sklearn.preprocessing import MinMaxScaler


def min_max():
    """归一化"""
    # 1. 获取数据
    # 2. 数据预处理
    # -- 实例化数据转换类
    # 3. 调用 fit_transform

    # 加载数据
    data = pd.read_csv('train.csv')
    data = data[['Fare']]
    # 初始化归一化转换器
    transfer = MinMaxScaler(feature_range=[0, 1])

    print("获取数据：\n", data.head())
    # 调用 fit_transform 处理数据
    data = transfer.fit_transform(data)
    print("转换之后的数据:\n", data)


if __name__ == '__main__':
    min_max()
