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
主體是一個儲存多個節點的原生陣列`array`，同時擁有`head`、`tail`等作為雙向連結串列的屬性

**PUSH & UNSHIFT：**  

建立節點包含資料`data`、一個`index`  
以及`next`、`pre`指標指向相鄰的節點（如同雙向連結串鍊一樣）  
最後將結點PUSH至陣列`array`最尾端，而當操作是UNSHIFT時，內部`index`遞減。  

範例：(`{index, data}`)  
1. PUSH 5  
    `array = [{0, 5}]`
2. PUSH 3  
    `array = [{0, 5}, {1, 3}]`
3. UNSHIFT 1  
    `array = [{0, 5}, {1, 3}, {-1, 1}]`
4. UNSHIFT 7  
    `array = [{0, 5}, {1, 3}, {-1, 1}, {-2, 7}]`
5. PUSH 6  
    `array = [{0, 5}, {1, 3}, {-1, 1}, {-2, 7}, {2, 6}]`
    
**POP & SHIFT：**  

根據`head`、`tail`找到相對應的節點，將節點標示為空，並移動`head`、`tail`

**Refactor：**  

從`head`開始，將節點重新標序，並依序放入新陣列中，重構可以在任意時間點呼叫。  

重構前：  
`array = [{0, 5}, {1, 3}, {-1, 1}, {-2, 7}, {2, 6}]`  
重構後：  
`array = [{0, 7}, {1, 1}, {2, 5}, {3, 3}, {4, 6}]`  

**GET：**  

由於重構後且在下一次重構之前，可能進行插入、刪除的動作。  
因此於任意時間點，陣列處於部分重構、部分亂序的狀態。  
所以一個GET操作可能為：  
1. 請求的資料位於 **已重構** 範圍內
2. 請求的資料位於 **未重構** 範圍內，且紀錄的`index`小於0
3. 請求的資料位於 **未重構** 範圍內，且紀錄的`index`大於0

例如一個#0\~#4已重構，#5\~#9未重構的狀態：  
`array = [{0, 7}, {1, 1}, {2, 5}, {3, 3}, {4, 6}, {5, 9}, {-1, 6}, {6, 3}, {-2, 1}, {-3, 4}]`  

1. GET 4 (請求的資料位於 **已重構** 範圍內)  
    已知最小`index`為`-3`，因此請求的資料位於`array[4 + (-3)] = array[1] = {1, 1} = 1`
    
2. GET 1 (請求的資料位於 **未重構** 範圍內，且紀錄的`index`小於0)  
    請求的資料所記錄的`index`應為`1 + (-3) = -2`，而重構頂界位於`4`  
    因此從`array[4 + -(-2)] = array[6]`向後搜尋紀錄`index`為`-2`的資料  
    最後於`array[8] = {-2, 1} = 1`  
    *(因為必先有`-1`被插入，才有`-2`，因此從`array[6]`開始向後找，而非`array[5]`)*
    
3. GET 8 (請求的資料位於 **未重構** 範圍內，且紀錄的`index`大於0)  
    請求的資料所記錄的`index`應為`8 + (-3) = 5`  
    因此從`array[5]`向後搜尋紀錄`index`為`5`的資料  
    最後於`array[5] = {5, 9} = 9`  
    理由同2.  
    
### 結論：

PUSH、UNSHIF、SHIFT、POP時間複雜度均為O(1)  
GET操作時，若請求資料位於已重構範圍時，時間複雜度為O(1)，反之為O(n)  
Refactor操作時間複雜度為O(n)  

當重構範圍越大，GET操作效率越好，但也不可頻繁的Refactor。  
因此如何在適當的時間點進行重構，是LinkArray最大的課題。  
