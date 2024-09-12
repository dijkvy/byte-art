# 使用 knn 计算鸢尾花

``` python
# 使用 knn 算法分类鸢尾花

from sklearn.datasets import load_iris
# train_test_split 函数用于划分数据集合
from sklearn.model_selection import train_test_split
# StandardScaler 用于特征工程，数据标准化
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier

# 1. 获取数据
# 2. 数据集合划分
# 3. 特征工程
# -- 标准化
# 4. KNN 预估流程
# 5. 模型评估

# 1. 加载数据集合
data = load_iris()
# print(data.feature_names)
print(dict(data).keys())

# 划分数据集
features, targets = data.data, data.target
# x_train 训练特征
# y_train 训练标签
#
X_train, X_test, y_train, y_test = train_test_split(features, targets, random_state=0, test_size=0.3)

print(X_train.shape)

# 特征工程：对数据集做标准化
transfer = StandardScaler()

X_train = transfer.fit_transform(X_train)
print("标准差:", transfer.var_)
X_test = transfer.transform(X_test)

# KNN 算法预估器
estimator = KNeighborsClassifier(n_neighbors=3)
# 模型训练
estimator.fit(X_train, y_train)

# 模型评估
# 1) 直接对比真实值和预估值
# 2) 计算准确率
y_predict = estimator.predict(X_test)
print("y_predict:", y_predict)
print("y_predict:", y_predict == y_test)

# 计算准确率
score = estimator.score(X_test, y_test)
print("准确率为: ", score)

```

knn 算法的优点:
1. 简单，容易理解，容易是实现

缺点: 
1. 必须指定 K 值(K值选择不当，导致计算结果不准确)
2. 计算量大，内存开销大， 不适合数据量特别大的计算
