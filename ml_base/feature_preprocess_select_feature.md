# 特征降维 -- 特征选择

数据中包含冗余或者相关变量(特征，属性或者指标等)， 旨在从特征中找出主要特征。

特征选择的方法

1. Filter(过滤式): 主要研究特征本身的特点，特征与特征
    * 方差选择法: 底方差特征过滤
    * 相关系数法:(通过相关系数可以衡量特征之间的相关性)
2. Embedded(嵌入式)
   * 决策树: 信息熵，信息增益
   * 正则化: L1, L2
   * 深度学习

## 低方差特征过滤

删除低方差的特征，

* 特征方差小: 某个特征大多样本的值比较接近
* 特征方差大: 某个特征大多样本的值有差别

``` python

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

```
