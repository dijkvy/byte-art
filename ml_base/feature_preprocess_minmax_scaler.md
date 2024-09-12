# 归一化处理数据

## 归一化的重要性

$$X^{'} = \frac{x - min}{max - min}$$

$$X^{"} =  X^{"} * (m_{x}-m_{i}) + m_{i}$$

其中: 

max 为每一列的最大值

min 为每一列的最小值

$m_{i},m_{x}$ 为指定的区间， 默认 $m_{i}$ 为 0， $m_{x}$ 为 1

``` python

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

```

总结: 归一化依赖于数据的最大值和最小值，对异常值比较敏感，(如果异常值偏差太大，对最终的结果比较敏感， 适合使用于精确数据的场景)
