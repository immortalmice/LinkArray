這是一個新的類陣列(Array-Like)資料結構
---

- 原生陣列(Array)在GET表現優秀，但SHIFT & UNSHIFT表現差
- 連結串列(LinkedList)在SHIFT & UNSHIFT表現優異，但GET表現慘烈

**因此本研究在尋找一個可以融合兩者優點的類陣列資料結構**

本Repo包含以下幾個新的類陣列資料結構
---
- LinkArray  
    結構中同時包含了原生陣列和雙向連結串列的結構，並以PUSH取代UNSHIFT操作  
    其中有一個方法用於 **"重構"** ，呼叫重構後的LinkArray可以在GET操作上有很好的能力表現  

- AutoLinkArray  
    LinkArray的繼承類別，會在自己覺得合適的時間點自行呼叫重構  

- AdaptiveArray  
    LinkArray在資料量小時表現差  
    因此AdaptiveArray在資料量小時維持原生陣列的格式，資料量大時自行升級為LinkArray  
    
性能測試報告
---
- [AutoLinkArray](https://docs.google.com/spreadsheets/d/1BD9gHeoGC48EA0i1AbdahxI_zEBVmSjZAeOJUYpxKP4/edit?usp=sharing)
- [AdaptiveArray](https://docs.google.com/spreadsheets/d/1z5rMht8dqc3bMp_eS2LW20jr-XR3ADW8Iw7a85ZbWk0/edit?usp=sharing)

關於LinkArray
---
**PUSH & UNSHIFT：**  

建立節點，節點內包含資料、一個 "內部" `index`，以及`next`、`pre`指標指向相鄰的節點（如同雙向連結串鍊一樣）  
當操作是UNSHIFT時，內部`index`遞減。  

範例：(`{index, data}`)  
1. PUSH 5  
    `[{1, 5}]`
2. PUSH 3  
    `[{1, 5}, {2, 3}]`
3. UNSHIFT 1  
    `[{1, 5}, {2, 3}, {-1, 1}]`
4. UNSHIFT 7  
    `[{1, 5}, {2, 3}, {-1, 1}, {-2, 7}]`
5. PUSH 6  
    `[{1, 5}, {2, 3}, {-1, 1}, {-2, 7}, {3, 6}]`