# 导入 pandas
import pandas as pd
# 导入字典特征抽取器
from sklearn.feature_extraction import DictVectorizer

# 实例化 字典特征抽取对象, sparse=True 表示返回的是稀疏矩阵
transfer = DictVectorizer(sparse=False)

# 字典数据
# 其中 city 的值不是数值类型的， 因此需要使用特征转换
data = [
    {'city': '上海', 'temperature': 100},
    {'city': '北京', 'temperature': 80},
    {'city': '深圳', 'temperature': 30},
]

print("原始特征:\n", data)
# 特征抽取, 返回的是二维矩阵
data_new = transfer.fit_transform(data)

print("进行特征转换之后:\n", data_new)

print("特征的名称:\n", transfer.get_feature_names())

df = pd.DataFrame(data_new, columns=transfer.get_feature_names())
print("构建的 DataFrame:\n", df)

print(transfer.inverse_transform(df.values))

# 初始化字典特征转换器示例，返回的是稀疏矩阵
transfer = DictVectorizer()
# 进行数据转换
data_new = transfer.fit_transform(data)

print("稀疏矩阵:\n", data_new)

# 还原转换之后的数据
print("转换之后的数据:\n", transfer.inverse_transform(data_new))
