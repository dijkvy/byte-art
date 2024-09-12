# 将列表转字典
# ['hello', 'hello', 'g', 'u'] => {'hello': 2, 'g': 1, 'u': 1}
l = ['hello', 'hello', 'g', 'u']
w = {key: l.count(key) for key in l}
print(w)
