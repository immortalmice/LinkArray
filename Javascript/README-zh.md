Node.js v12.18.0  

JavaScript實作性能測試報告
---
*(本性能測試中所使用的原生陣列來自於JavaScript內建的陣列)*  
*(本性能測試中所使用的雙向連結串列來自於[此Repo](https://github.com/jasonsjones/doubly-linked-list))*  

- [AutoLinkArray](https://docs.google.com/spreadsheets/d/1BD9gHeoGC48EA0i1AbdahxI_zEBVmSjZAeOJUYpxKP4/edit?usp=sharing)
- [AdaptiveArray](https://docs.google.com/spreadsheets/d/1z5rMht8dqc3bMp_eS2LW20jr-XR3ADW8Iw7a85ZbWk0/edit?usp=sharing)

上述所連結的表格中，第一個頁籤
- Time (ms)

為原生陣列、雙向連結串列、Auto Link Array / Adaptive Array三者的比較，**單位是時間(毫秒)**  

而後兩個頁籤  
- Compare: Normal Array (Factor)
- Compare: Doubly Linked List (Factor)

是分別對於JavaScript原生陣列以及雙向連結串列進行效能測試，**單位是時間反比的倍率**  
Ex. Adaptive Array在 **[GET] With PreFilling** 測試項目的 **2000筆測資** 中表現比陣列快 **100.5128** 倍  
*若為負數，則意旨速度較慢，這是一個方便視覺化的轉換： 0.5倍 => -2倍*  
  
此兩個頁籤中的 19\~31 列為評分，旨在找尋在同一測試項目中 Auto Link Array / Adaptive Array 於各資料量下的表現
以 B18 為例 `B18 = (B18 - Min(B4-CO4)) / (Max(B4-CO4) - Min(B4-CO4))`  
最後再以行進行分析，旨在找尋此資料量下 Auto Link Array / Adaptive Array 於各測試項目的 **最佳、最差、平均** 表現
以 B 為例，`B32 = Min(B18-B30)`、`B33 = Max(B18-B30)`、`B34 = Averge(B18-B30)`

簡要分析
---

JavaScript中 Auto Link Array 的實作方法為：  
*`array`中未重構的元素個數超過5000個時進行重構*  

1. 以單一種操作來說：  
	- [SHIFT]、[UNSHIFT] AutoLinkArray 明顯勝過原生陣列
	- [GET] AutoLinkArray 明顯勝過雙向連結串列
	- 所有 AutoLinkArray 的劣勢在 AdaptiveArray 中明顯的被撫平

2. 資料量於 5000 以下時，由於未進行重構，可以看到以下項目的速度明顯的慢  
	- [GET] With PreFilling
	- [GET, POP, SHIFT] With PreFilling
	- [GET, PUSH, POP] With PreFilling
	- [GET, PUSH, POP]

3. 有 PreFilling 的項目，由於測試期間陣列處於內涵平均N個元素的狀態  
	此時進行操作會使需要O(n)時間複雜度的陣列花較多的時間  
	相反的，沒 PreFilling 的項目，AutoLinkArray 由於未重構，因此GET操作較慢  

	**所以 AutoLinkArray 在 PreFilling 的項目會勝出，沒 PreFilling 的項目會處於劣勢**  

	而這些劣勢在 AdaptiveArray 會被撫平  

4. 由 AutoLinkArray 的 Score 圖表中可以看出。以平均值做為參考，AutoLinkArray 於資料量越大時整體評分表現越高  
	而 AdaptiveArray 的評分表現對於資料量大小較上述所述相對無關  

5. 所有的劣勢相較於優勢相對較小