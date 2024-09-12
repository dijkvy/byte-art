# 皮尔逊相关性系数

$$r = \frac{n\sum{xy} -\sum{x}\sum{y}}{\sqrt{n\sum{x^{2}} -(\sum{x})^{2}} \sqrt{n\sum{y^2}-(\sum{y})^2}}$$

$$r$$ 是两个变量的相关系数

$$x, y$$ 表示需要计算相关性的两个变量

----




相关系数的值介于 -1 到 +1 之间，-1 <= r <=1

当  r >0 时，两个变量之间正相关， r< 0 时两个变量负相关

|r| <1 时， 表示两个变量完全相关， r = 0 时， 表示两个变量不相关

0 < |r| < 1 时， 表示两个变量存在一定程度的相关性， 且 |r| 越接近于 1 表示两个变量的相关性越接近， |r| 越接近于 0 表示两个变量的相关性越来越弱

----


相关性等级划分:

|r| < 0.4 表示两个变量之间存在低相关性

0.4 <= |r| < 0.7 表示两个变量有显著的相关性

0.7 <= |r | < 1 表示两个变量高度相关



使用 `scipy` 计算两个变量的相关性系数

``` python
# 绘图库
import matplotlib.pyplot as plt
import pandas as pd
# 从 scipy 中导入相关性系数的计算函数
from scipy.stats import pearsonr


def pearsonr_():
    data = pd.read_csv('train.csv')
    # print("data:", data)
    data.dropna(inplace=True)
    # 计算 Fare 与 Survived 之间的相关性系数
    r = pearsonr(data['Fare'], data['Survived'])
    #  输出相关性系数 (0.13424105283521096, 0.07002557714938451)
    print("相关性系数:\n", r)
    # 通过散点图来绘制两个变量之间的相关性系数
    plt.scatter(data['Fare'], data['Survived'])
    plt.show()


if __name__ == '__main__':
    pearsonr_()

```

