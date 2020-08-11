Javascript實作性能測試報告
---
*(本性能測試中所使用的雙向連結串列來自於[此Repo](https://github.com/jasonsjones/doubly-linked-list))*  

- [AutoLinkArray](https://docs.google.com/spreadsheets/d/1BD9gHeoGC48EA0i1AbdahxI_zEBVmSjZAeOJUYpxKP4/edit?usp=sharing)
- [AdaptiveArray](https://docs.google.com/spreadsheets/d/1z5rMht8dqc3bMp_eS2LW20jr-XR3ADW8Iw7a85ZbWk0/edit?usp=sharing)

上述所連結的表格中，前兩個頁籤
- Compare: Normal Array (Factor)
- Compare: Doubly Linked List (Factor)

是分別對於原生陣列以及雙向連結串列進行效能測試，**單位是時間反比的倍率**  
Ex. Adaptive Array在 **[GET] With PreFilling** 測試項目的 **2000筆測資** 中表現比陣列快 **100.5128** 倍  
*若為負數，則意旨速度較慢，這是一個方便視覺化的轉換： 0.5倍 => -2倍*  

而第三個頁籤
- Time (ms)

則為原生陣列、雙向連結串列、Auto Link Array / Adaptive Array三者的比較，**單位是時間(毫秒)**  

測試項目一共有13個  
方括弧中的是本項目中包含的指令種類，為隨機生成，因此各指令出線機率可視為相等  
測試結果測試對象跑完由本項目生成的指定長度指令列表所花的時間  
Ex. Auto Link Array跑完 4000筆 [GET] With PreFilling 所花的時間  

PreFilling指在運行指令列表前，先對測試對象塞入指令長度的資料  
每一個塞入動作為PUSH、UNSHIFT隨機擇一  
Ex. 在測試 [GET] With PreFilling 前，對Auto Link Array用PUSH、UNSHIFT隨機塞入4000筆資料  

- **[GET] With PreFilling**
    純GET能力
- **[PUSH]**
    純PUSH能力
- **[UNSHIFT]**
    純UNSHIFT能力
- **[POP] With PreFilling**
    純POP能力
- **[SHIFT] With PreFilling**
    純SHIFT能力
- **[GET, PUSH, UNSHIFT]**
    塞入能力
- **[GET, POP, SHIFT] With PreFilling**
    拔除能力
- **[GET, PUSH, POP] With PreFilling**
    尾端操作能力，包含預填入
- **[GET, PUSH, POP]**
    尾端操作能力
- **[GET, SHIFT, UNSHIFT] With PreFilling**
    首端操作能力，包含預填入
- **[GET, SHIFT, UNSHIFT]**
    首端操作能力
- **[GET, PUSH, UNSHIFT, POP, SHIFT] With PreFilling**
    綜合能力，包含預填入
- **[GET, PUSH, UNSHIFT, POP, SHIFT]**
    綜合能力