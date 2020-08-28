# This is a new array-like data structure.
* The original array can perform well on doing random `GET`, but perform bad on `SHIFT` and `UNSHIFT`.(Here the array represent the one which can adjust its length dynamically.)
* LinkedList perform well on `SHIFT` and `UNSHIFT`, but the performace on random `GET` is terrible.  
**Therefore, this study is aim to find an array-like data structure which can combine the advantage of the two mentioned above.**

# This repo contains the following new array-like data structure.
* LinkArray -- Containing the structure of both original array and doubly-linked-list, and replacing `UNSHIFT` by `PUSH` operation. One of the method in this datas structure is used to do "**refactor**". After calling refactor, the LinkArray can perform well on random `GET`.
* AutoLinkArray -- A class that inherit LinkArray. it will call refactor at suitable moment by itself.
* AdaptiveArray -- LinkArray performs bad when the size of data is small. AdaptiveArray remain the form of original array when the size of data is small and automatically upgrade itself to LinkArray when the size of data is large.

# About LinkArray
The main body is an original array containing multiple nodes. It contains `head` and `tail` which are the properties of doubly-linked-list.  

**PUSH & UNSHIFT**

Construct a node containing `data`, `index`. The node also containing `next` and `pre` pointers in order to point to adjacent nodes. (The same as doubly-linked-list).  
Finally, push the constructed node to the end of the `array`. When you do unshift, the internal `index` decrement.  

Example : (`{index, data}`)
1. PUSH 5  
`array = [{0, 5}]`
2. PUSH 3  
`array = [{0, 5}, {1, 3}]`
3. UNSHIFT 1  
`array = [{0, 5}, {1, 3}, {-1, 1}]`
4. UNSHIFT 7  
`array = [{0, 5}, {1, 3}, {-1, 1}, {-2, 7}]`
5. PUSH 6  
`array = [{0, 5}, {1, 3}, {-1, 1}, {-2, 7}, {2. 6}]`

**POP & SHIFT**

Find the nodes corresponding to `head` or `tail` and set the node to become empty, then move `head` or `tail`.

**Refactor**

Re-arrange the nodes by traveling from the `head` node, then put them into a new array in order. Refactor can be called at anytime.  
Before refactor:  
`array = [{0, 5}, {1, 3}, {-1, 1}, {-2, 7}, {2, 6}]`  
After refactor:  
`array = [{0, 7}, {1, 1}, {2, 5}, {3, 3}, {4, 6}]`  

**GET**

When you already do refactor and is at the time before the next call of refactor, you may do insertion or deletion to the array. As a result, at a arbitrary time, the array is in the status that is partially refactored and partially derangement. Therefore, one call of GET may be one of the following:  
1. The data being requested is in the scope that is **refactored**.
2. The data being requested is in the scope that is **NOT refactored** and the recording index is less than 0.
3. The data being requested is in the scope that is **NOT refactored** and the recording index is greater than 0.

For example, an array that the nodes \#0 ~ \#4 are refactored and \#5 ~ \#9 are not refactored.  
(*\#5 is the adjacent call of push right after refactor, so it is regarded to the scope that is not refactored.*)  
`array = [{0, 7}, {1, 1}, {2, 5}, {3, 3}, {4, 6}, {5, 9}, {-1, 6}, {6, 3}, {-2, 1}, {-3, 4}]`  
1. GET 4 (The data being requested is in the scope that is **refactored**)  
Given the minimal `index` to be `-3`, the requesting data is at `array[4 + (-3)] = array[1] = {1, 1} = 1`  
2. GET 1 (The data being requested is in the scope that is **NOT refactored** and the recording index is less than 0.)  
The internal `index` corresponding to the requesting data should be `1 + (-3) = -2`, and the end of refactored array is at `4`.  
Therefore, we have to set the starting point of finding the data with `index` be `-2` to be `array[4 + -(-2)] = array[6]`.
(You must insert `-1` before `-2`, so the starting point is `array[6]`, not `array[5]`)
3. GET 8 (The data being requested is in the scope that is **NOT refactored** and the recording index is greater than 0.)  
The internal `index` corresponding to the requesting data should be `8 + (-3) = 5`.  
Therefore, we set the starting point of finding the data with `index` be `5` to be `array[5]`.  
Finally, we find `array[5] = {5, 9} = 9`. 
The reason we start finding at `array[5]` is the same as mentioned in 2.  

## Conclusion
The time complexity of PUSH, UNSHIFT, SHIFT and POP are O(1).  
If you do GET on a data that is refactored, the time complexity is O(1). Otherwise, O(n).  
The time complexity of rafactor is O(n).  

When the scope that is refactored becomes larger, the efficiency of GET becomes better. However, one should not call refactor frequently.  
As a result, the top goal of LinkArray is to call refactor at sutible time point.

# About AutoLinkArray
AutoLinkArray is a derived class of LinkArray.  
Refactor has a far influence on the efficiency of GET, but refactor is an action with time complexity O(n).  
Therefore, AutoLinkArray will call refactor at suitable time point.  

By now, we have three ways to implement AutoLinkArray.  
1. The number of nodes that are not refactored exceeds a certain amount and a GET command is received. Ex. The number of nodes that are not refactored in `array` exceeds 5000.  
2. The proportion of nodes that are not refactored exceeds a specified percent and a GET command is received. Ex. The proportion of nodes that are not refactored in `array` exceeds 20%.
3. When the internal array demands a new space and tries to transfer to the new space, the refactor can be done when doing transfer.  

One can choose his or her demanded way to implement from the three ways mentioned above.  
Apart from choosing one of the three ways, one can also mix two or more methods.  

# About AdaptiveArray
When the size of data contained in LinkArray is small, the advantage of LinkArray is not impressive.  
Although the time complexity on doing PUSH, UNSHIFT, POP, SHIFT are all O(1), in the situation that the amount of doing these actions is large, LinkArray still have to spend more time on doing these.  

As the result of the statement mentioned above, AdaptiveArray is invented.  
At the beginning, the AdaptiveArray is an original array. When the length of the array exceeds a certain number(Ex. 5000), the AdaptiveArray will automatically upgrade itself to a LinkArray(usually AutoLinkArray).  
Once the array is upgraded, the array will never downgrade to the original array even if the length becomes smaller than the certain value again.

# About Contribution
This repo contains the implementation by the languages listed below by now.
* [JavaScript](https://github.com/immortalmice/LinkArray/tree/master/Javascript)
* [Java](https://github.com/immortalmice/LinkArray/tree/master/Java)

The efficiency tests in the below languages are contained.  
* [JavaScript](https://github.com/immortalmice/LinkArray/blob/master/Javascript/README.md)
* [Java](https://github.com/immortalmice/LinkArray/blob/master/Java/README.md)  

If you are intend to provide some help on the languages not mentioned above, please make a pull request on your own implementation or efficiency test to me.

# About Tests
The test can be divided into 13 sections.  
The commands in the bracket are the types of commands that are contained in the certain section. All the commands are generated randomly, so the ratio of every command can be considered to be equal.  
The result of test is the time spent by running the specified number of commands in certain section.  
*Ex.The time spent by running 4000 \[GET\] With Prefilling with Auto Link Array.*  

Prefilling means inserting data which contains the number of commands in the test section before running the test commands.  
Every insertion is done by PUSH and UNSHIFT randomly.  
*Ex. Before testing \[GET\] With Prefilling, insert 4000 data into an Auto Link Array by PUSH and UNSHIFT randomly.*

* \[GET\] With Prefilling  
The ability of successive pure GET.
* \[PUSH\]  
The ability of successive pure PUSH.
* \[UNSHIFT\]  
The ability of successive pure UNSHIFT.
* \[POP\] With Prefilling  
The ability of successive pure POP.
* \[SHIFT\] With Prefilling  
The ability of successive pure SHIFT.
* \[GET, PUSH, UNSHIFT\]  
The ability of insertion.
* \[GET, POP, SHIFT\] With Prefilling  
The ability of deletion.
* \[GET, PUSH, POP\] With Prefilling  
The ability of operations on the tail. Containing prefilled data.
* \[GET, PUSH, POP\]  
The ability of operations on the tail.
* \[GET, SHIFT, UNSHIFT\] With Prefilling  
The ability of operations on the head. Containing prefilled data.
* \[GET, SHIFT, UNSHIFT\]  
The ability of operations on the head.
* \[GET, PUSH, UNSHIFT, POP, SHIFT\] With Prefilling  
The ability of mixed operations. Containing prefilled data.
* \[GET, PUSH, UNSHIFT, POP, SHIFT\]
The ability of mixed opeartions.