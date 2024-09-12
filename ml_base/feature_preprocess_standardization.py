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
