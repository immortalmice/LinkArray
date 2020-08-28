Node.js v12.18.0  

# The Report of Performance Test in JavaScript
(The original array used in this test is from the built-in array in JavaScript)  
(The doubly-linked-list used in this test is from [this repo](https://github.com/jasonsjones/doubly-linked-list))  

* [AutoLinkArray](https://docs.google.com/spreadsheets/d/1BD9gHeoGC48EA0i1AbdahxI_zEBVmSjZAeOJUYpxKP4/edit?usp=sharing)
* [AdaptiveArray](https://docs.google.com/spreadsheets/d/1z5rMht8dqc3bMp_eS2LW20jr-XR3ADW8Iw7a85ZbWk0/edit?usp=sharing)

In the links mentioned above, the first page
* Time(ms)  

is the comparason of original array, doubly-linked-list and AutoLinkArray/AdaptiveArray. **The unit of time is ms.**

About the next two pages
* Compare: Normal Array (Factor)
* Compare: Doubly Linked List (Factor)  

are the performances comparison of original array and doubly-linked-list in JavaScript. **The unit is the ratio to inverse of time.**  
Ex. AdaptiveArray performs faster than doubly-linked-list by 100.5128 times in **\[GET\] With Prefilling** test with **2000 data**.  
*The ratio being a negative number means the performance is slower. It's a easier way to visualize: 0.5 times => -2 times.*

In these two pages, the 19 to 31 rows are the scores. The scores are used to compare the performances of AutoLinkArray/AdaptiveArray with different length of command list in the same test section. Say B18 as example. `B18 = (B18 - Min(B4-CO4)) / (Max(B4-CO4) - Min(B4-CO4))`.  
Finally, we analysis the performances by columns. The analysis is aim to find the **best, worst, average** performances with certain length of command list in each test section. Take column B as example. `B32 = Min(B18-B30)、B33 = Max(B18-B30)、B34 = Averge(B18-B30)`.  

# Brief Analysis
The way to implement AutoLinkArray in JavaScript is:  
*Do refactor when the number of not refactored nodes exceeds 5000 in `array`.*  
1. Only one type of command:
    * \[SHIFT\], \[UNSHIFT\] AutoLinkArray performs better than original array apparently.
    * \[GET\] AutoLinkArray outperforms the doubly-linked-list apparently.
    * All disavantages in AutoLinkArray are improved in AdaptiveArray apparently.
2. When the number of data is less than 5000, the array is not refactored. As a result, the speed of below commands are rather slow.
    * \[GET\] With Prefilling
    * \[GET, POP, SHIFT\] With Prefilling
    * \[GET, PUSH, POP\] With Prefilling
    * \[GET, PUSH, POP\]
3. In the sections with prefilling, since the array is in a state with N elements in average in testing time, applying operations with O(n) complexity will need more time.  
On the contrary, in the sections without prefilling, since the AutoLinkArray is not refactored, GET will performs slower.  
**As a result, AutoLinkArray will outperforms in sections with prefilling and performs worse in sections without prefilling.**  
All these disadvantages will be removed in AdaptiveArray.
4. As we can see from the charts of score of AutoLinkArray, AutoLinkArray performs better generally with respect to average when the length of command list is large. AdaptiveArray's scores seems to has no big difference with the length of command list.
5. All disadvatages are rather minor compare to advantages.  

README Languages
---
* [繁體中文](https://github.com/immortalmice/LinkArray/blob/master/Javascript/README-zh.md)  