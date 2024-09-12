# 特征处理标准化

在某些算法中，特征缩放非常重要，比如 KNN, K-Means 算法需要计算点与点之间的距离

* 标准化是将数据按照比例进行缩放，使之落入一个较小的特定区间，把数据转为统一的标准。

* 通常使用的标准化方式为: z-score 标准化，零均值标准化

* $$z=\frac{x-\mu}{\sigma}$$

* 标准化之后，每个特征的平均值为 0，方差为 1

``` python
import pandas as pd
# 导入数据
from sklearn.preprocessing import StandardScaler


def preprocess_standard():
    # 获取数据
    data = pd.read_csv('train.csv')
    data = data[['Fare']]

    print("数据转换之前:\n", data.head(n=4))
    # 构造标准化数据转换器
    transfer = StandardScaler()
    # 对数据进行标准化
    data = transfer.fit_transform(data)
    # 打印数据
    print("转换之后的数据:\n", data)


if __name__ == '__main__':
    preprocess_standard()

```

对于标准化, 少量的异常数据在较大规模的数据中对均值的影响并不大，从而对方差的改变较小。
对于归一化，异常点会影响最大值和最小值，那么，异常点会对结果造成直接的影响
