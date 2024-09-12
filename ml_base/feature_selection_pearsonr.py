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
