Eclipse 2020-06 (4.16.0)  
Java jdk1.8.0 261, jre1.8.0 261  

Java實作性能測試報告
---
*(本性能測試中所使用的原生陣列來自於`java.util.ArrayList`)*  
*(本性能測試中所使用的雙向連結串列來自於`java.util.LinkedList`)*  

- [AutoLinkArray](https://docs.google.com/spreadsheets/d/1HxICQQ9OwkDOyxkJOCSsRYNHTwmzQg6eJDhVC1DliAc/edit?usp=sharing)
- [AdaptiveArray](https://docs.google.com/spreadsheets/d/1DMEIXr-UyiBHu53ZgROcng_ugzBpTUWgUeEo5X8aHf0/edit?usp=sharing)

上述所連結的表格中，第一個頁籤
- Time (ms)

為原生陣列、雙向連結串列、Auto Link Array / Adaptive Array三者的比較，**單位是時間(毫秒)**  

而後兩個頁籤  
- Compare: Normal Array (Factor)
- Compare: Doubly Linked List (Factor)

是分別對於原生陣列以及雙向連結串列進行效能測試，**單位是時間反比的倍率**  
Ex. Adaptive Array在 **[GET] With PreFilling** 測試項目的 **2000筆測資** 中表現比陣列快 **100.5128** 倍  
*若為負數，則意旨速度較慢，這是一個方便視覺化的轉換： 0.5倍 => -2倍*  
  
此兩個頁籤中的 19\~31 列為評分，旨在找尋在同一測試項目中 Auto Link Array / Adaptive Array 於各資料量下的表現
以 B18 為例 `B18 = (B18 - Min(B4-CO4)) / (Max(B4-CO4) - Min(B4-CO4))`  
最後再以行進行分析，旨在找尋此資料量下 Auto Link Array / Adaptive Array 於各測試項目的 **最佳、最差、平均** 表現
以 B 為例，`B32 = Min(B18-B30)`、`B33 = Max(B18-B30)`、`B34 = Averge(B18-B30)`

簡要分析
---

Java中 Auto Link Array 的實作方法為：  
*當內部陣列需要重新要求空間並轉移時，在轉移至新的陣列空間時順便進行重構*  

1. 以單一種操作來說：  
	- [SHIFT]、[UNSHIFT] AutoLinkArray 明顯勝過原生陣列
	- [GET] AutoLinkArray 明顯勝過雙向連結串列
	- 所有 AutoLinkArray 的劣勢在 AdaptiveArray 中明顯的被撫平

2. 由於自動重構發生於內部空間重新分配時，因此 AutoLinkArray [PUSH]、[UNSHIFT] 操作較為緩慢  
	而 [PUSH] 的劣勢在 AdaptiveArray 中會被去除，原因在於連續的PUSH並不會觸發升級為 AutoLinkArray  
	*(詳情請見程式碼，僅有SHIFT、UNSHIFT才有可能觸發升級)*

3. 所有的劣勢相較於優勢相對較小