這是一個新的類陣列(Array-Like)資料結構
---

- 原生陣列(Array)在GET表現優秀，但SHIFT & UNSHIFT表現差(這邊意旨被Modify過可動態調整長度的Array)
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
    

關於LinkArray
---
主體是一個儲存多個節點的原生陣列`array`，同時擁有`head`、`tail`等作為雙向連結串列的屬性

**PUSH & UNSHIFT：**  

建立節點包含資料`data`、一個`index`  
以及`next`、`pre`指標指向相鄰的節點（如同雙向連結串鍊一樣）  
最後將節點PUSH至陣列`array`最尾端，而當操作是UNSHIFT時，內部`index`遞減。  

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
*(#5為重構後的下一個指令剛好為PUSH，因此仍屬於未重構範圍)*
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

關於AutoLinkArray
---

AutoLinkArray是LinkArray的衍伸類別  
由於重構對於LinkArray的GET效率有著深厚的影響，但重構是一個O(n)的動作  
因此AutoLinkArray會自行在適當的時間點進行重構  

目前有三個實作方法  

1. 當未重構的範圍超過一定數量，並收到GET指令時，Ex.`array`中未重構的元素個數超過5000個時進行重構  
2. 當未重構的範圍超過一定比例，並收到GET指令時，Ex.`array`中未重構的元素個數佔比20%以上時進行重構  
3. 當內部陣列需要重新要求空間並轉移時，在轉移至新的陣列空間時順便進行重構  
  
三個實作方法可自行依照需求選擇實作方式  
除了擇一，也可選擇混和使用  

關於AdaptiveArray
---

LinkArray在內含資料量小的時候優勢並不明顯。  
儘管PUSH、UNSHIFT、POP、SHIFT的時間複雜度均為O(1)，但因為進行的操作的動作是比較多的情況下，LinkArray在做這些操作上依舊是會花較多的時間。  

因此AdapativeArray因應而生。  
AdaptiveArray剛開始的時候是一個原生的陣列，然而當陣列長度大於某個數值(EX.5000)，AdaptiveArray會自動升級成LinkArray(通常是AutoLinkArray)。  
一旦升級，便不會降級回原生陣列，即使陣列長度再度小於該數值。  

關於Contribution
---
目前本Repo包含了以下語言的實作
- JavaScript
- Java

包含以下語言的性能測試
- JavaScript
- Java

對於未在上表的語言，若你有意提供協助的話，歡迎把實作或性能測試發Pull Request給我

關於測試
---

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