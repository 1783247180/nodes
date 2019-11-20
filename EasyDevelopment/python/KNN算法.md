KNN 寻找某个P点的最近的K个点根据K点颜色中的大多数判断P点的颜色

```python
from sklearn import neighbors
from sklearn import datasets
knn = neighbors.KNeighborsClassifier()
iris = datasets.load_iris()
#print(iris)
knn.fit(iris.data, iris.target)
predictedLabel = knn.predict([[7.2, 2.8, 5.5, 1.8]])
print(predictedLabel)
```

