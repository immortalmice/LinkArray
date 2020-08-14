Node.js v12.18.0

JavaScript實作性能測試報告
---
*(本性能測試中所使用的原生陣列來自於JavaScript內建的陣列)*  
*(本性能測試中所使用的雙向連結串列來自於[此Repo](https://github.com/jasonsjones/doubly-linked-list))*  

- [AutoLinkArray](https://docs.google.com/spreadsheets/d/1BD9gHeoGC48EA0i1AbdahxI_zEBVmSjZAeOJUYpxKP4/edit?usp=sharing)
- [AdaptiveArray](https://docs.google.com/spreadsheets/d/1z5rMht8dqc3bMp_eS2LW20jr-XR3ADW8Iw7a85ZbWk0/edit?usp=sharing)

上述所連結的表格中，前兩個頁籤
- Compare: Normal Array (Factor)
- Compare: Doubly Linked List (Factor)

是分別對於JavaScript原生陣列以及雙向連結串列進行效能測試，**單位是時間反比的倍率**  
Ex. Adaptive Array在 **[GET] With PreFilling** 測試項目的 **2000筆測資** 中表現比陣列快 **100.5128** 倍  
*若為負數，則意旨速度較慢，這是一個方便視覺化的轉換： 0.5倍 => -2倍*  

而第三個頁籤
- Time (ms)

則為原生陣列、雙向連結串列、Auto Link Array / Adaptive Array三者的比較，**單位是時間(毫秒)**  
