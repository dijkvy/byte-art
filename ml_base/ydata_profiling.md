# 使用 ydata-profiling 可视化 pandas 数据

参考文档: https://github.com/ydataai/ydata-profiling  

``` python

import numpy as np
import pandas as pd
from ydata_profiling import ProfileReport

df = pd.read_csv('train.csv')
profile = ProfileReport(df, title="Profiling Report")
profile.to_file("your_report.html")

```
