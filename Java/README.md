Eclipse 2020-06 (4.16.0)  
Java jdk1.8.0 261, jre1.8.0 261 

# The Report of Performance Test in JAVA
*(The original array used in this test is from `java.util.ArrayList`)*  
*(The doubly-linked-list used in this test is from `java.util.LinkedList`)*  

* [AutoLinkArray](https://docs.google.com/spreadsheets/d/1HxICQQ9OwkDOyxkJOCSsRYNHTwmzQg6eJDhVC1DliAc/edit?usp=sharing)
* [AdaptiveArray](https://docs.google.com/spreadsheets/d/1DMEIXr-UyiBHu53ZgROcng_ugzBpTUWgUeEo5X8aHf0/edit?usp=sharing)

In the links mentioned above, the first page
* Time(ms)  

is the comparason of ArrayList, LinkedList and AutoLinkArray/AdaptiveArray. **The unit of time is ms.**

About the next two pages
* Compare: Array List (Factor)
* Compare: Linked List (Factor)  

are the performances comparison of ArrayList and LinkedList in Java. **The unit is the ratio to inverse of time.**  
Ex. AdaptiveArray performs faster than LinkedList by 23.9385 times in **\[GET\] With Prefilling** test with **2000 data**.  
*The ratio being a negative number means the performance is slower. It's a easier way to visualize: 0.5 times => -2 times.*

In these two pages, the 19 to 31 rows are the scores. The scores are used to compare the performances of AutoLinkArray/AdaptiveArray with different length of command list in the same test section. Say B18 as example. `B18 = (B18 - Min(B4-CO4)) / (Max(B4-CO4) - Min(B4-CO4))`.  
Finally, we analysis the performances by columns. The analysis is aim to find the **best, worst, average** performances with certain length of command list in each test section. Take column B as example. `B32 = Min(B18-B30)、B33 = Max(B18-B30)、B34 = Averge(B18-B30)`.  

# Brief Analysis
The way to implement AutoLinkArray in Java is:  
*When the internal array demands new space and tries to transfer to the new space, do refactor at the mean time.*
1. Only one type of command: 
    * \[SHIFT\], \[UNSHIFT\] AutoLinkArray performs better than original array apparently.
    * \[GET\] AutoLinkArray outperforms the doubly-linked-list apparently.
    * All disavantages in AutoLinkArray are improved in AdaptiveArray apparently.
2. Since the automatical refactor occurs when the internal space rearranges, the operations \[PUSH\], \[UNSHIFT\] are slower in AutoLinkArray to LinkedList.  
	The disadvantage in \[PUSH\] is removed in AdaptiveArray, because successive PUSH would not trigger the array to upgrade to AutoLinkArray.  
	*(See the source code for details. Only SHIFT, UNSHIFT would probably trigger the upgrade.)*
3. All disadvatages are rather minor compare to advantages.  

README Languages
---
* [繁體中文](https://github.com/immortalmice/LinkArray/blob/master/Java/README-zh.md)  