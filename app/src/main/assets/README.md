# Preparation (Java)
![alt tag](file:///android_asset/app_logo.png)

## Data Structures

### String
* A *String* is an *array* of characters ended with `'\0'`
* Concatenation '+' creates new String in String pool
* In the nth run, it copies nx characters, so runtime is O(x + 2x ... +nx) = `O(n^2x)`
* Stored in constant String pool, immutable, thread-safe, fast
  * **StringBuffer** : Stored in heap, mutable, thread-safe, slow
  * **StringBuilder** : Stored in heap, mutable, non-thread-safe, fast
  
### Hash Table
* A *hash table* is a data structure that maps keys to values for efficient lookup
* **Hash function**: a function maps a key to an integer that specifies the index of array
* When inserting an object with a key, hash function calculates the index and then stores the value in the index of array
* An *array* is a hash table with hash function of `f(x) = x`
* Time complexity (amortized) :
  * Search value: `O(1)`
  * Find min/max: `O(n)`
  * Insert (amortized): `O(1)` 
  * Delete (amortized): `O(1)`
* **Dynamic resizing** - To keep the load factor under a certain limit, the table is dynamically expanded after certain number of items are inserted.
  * Resizing by copying all entries - create a larger table and dump the old one
  * Incremental resizing - used when can't pay the price of enlarging hash table all at once
* Collision resolution : 
  * **Separate chaining** - store key and value in a separate *linked list*
  * **Open addressing** - all key-value pairs are stored in another slot of hash table 
  * *Linear probing* - distance between probes is constant
  * *Quadratic probing* - distance between probes increases by certain constant at each step
  * *Double hashing* - distance between probes is calculated using another hash function
  * Others: *cuckoo hashing*, *robin hood hashing*, *2-choice hashing*, and etc. 
* **Note** : In Java, *Hashmap* is non-synchronized and *HashTable* is synchronized.

### Array
* An *array* is a collection with specified size
  * Linear array: declared with fixed size
  * Dynamic array: automatically expands when adding elements (*Array List*)
* **Array List** : When resizing, array copy costs `O(n)`
* **Note** : In Java, *Vector* is basically an *ArrayList* but is synchronized (thread-safe)
* Time complexity:
  * Get index: `O(1)`
  * Search value: `O(n)`
  * Search value (sorted): `O(logn)` with binary search
  * Find min/max: `O(n)`
  * Find min/max (sorted): `O(1)`
  * Insert (amortized): `O(1)` (worst case `O(n)` to expand array)
  * Insert at index: `O(n)` (need to shift elements)
  * Delete: `O(n)` (need to shift elements)

### Linked List
* A *linked list* is a linear collection of nodes where each node has a value and a reference
  * **Singly-linked list**: each node has pointer to the next node
  * **Doubly-linked list**: each node has pointer to the next node and previous node (*LinkedList*)
* Fast at *insertion* and *deletion*, slow at *indexing* and *searching*
* Time complexity:
  * Get index: `O(n)`
  * Search value: `O(n)`
  * Find min/max: `O(n)`
  * Insert: `O(1)` 
  * Insert at index: `O(n)` (with Iterator: `O(1)`)
  * Delete: `O(n)` (with Iterator: `O(1)`)

### Stack
* A *stack* is a data structure with basic operations `push` , `pop` and `peek`
* A more restricted data structure that prevents peeking at other items below
* Principle: last in, first out (LIFO)
* Time complexity:  `O(1)` for `push`, `pop` and `peek`
* Can be implemented by using *array* , *linked list* or *queue*


### Queue
![alt tag](file:///android_asset/small/queues.png)

* A *queue* is a data structure which element is inserted in the tail and removed from head of queue
* **Enqueue** : add element to the end of queue (offer, add) 
* **Dequeue** : return and remove the element from the front of queue (poll, remove) 
* Principle: first in, first out (FIFO)
* *Dequeue* or *Deque*: items can be inserted and removed at either end (*stack* + *queue*)
* Can be implemented by using *array* , *linked list* or *stack*

### Trees
* A *tree* is an abstract data structure consisting of nodes organized in hierarchical structure
* A *tree* is a type of *graph*, it can't contain cycles 
* Terminology:
  * *Root node* - the top-most node in a tree
  * *Child node* 
  * *Parent node*
  * *Descendant* - a node reachable by repeated proceeding from parent to child
  * *Ancestor* - a node reachable by repeated proceeding from child to parent
  * *Internal node, inner node, inode, branch node* - node that has child nodes
  * *External node, outer node, leaf node, terminal node* - node doesn't have child nodes
  * Depth of node - length of its path to the root (root node has depth zero)
  * Height of node - length of longest path from that node to a leaf (height of *root* is the height of *tree*)
  * *Degree* - number of sub trees of a node
  * *Edge* - connection between one node to another
  * *Path* - A sequence of nodes and edges connecting a node with a descendant
  * *Forest* - A forest is a set of n >= 0 disjoint trees

#### Binary Tree
* A *binary tree* is a tree structure where each node has at most 2 children (left & right child)
* **Degenerate**: every parent node has only one associated child node, like *linked list* (unbalanced tree)
* **Full**: every node has either 0 or 2 children, that is, no node has only one child
* **Complete**: every level of the tree is fully filled, except perhaps the last level (filled from left to right)
* **Perfect**: hboth full and complete, all leaves will be at the same level
* **Height-Balanced**: the height difference of left and right sub-tree is within 0 and 1


#### Binary Tree Traversal
![alt tag](file:///android_asset/small/binary_tree.jpg)

* **Pre-order**: visit node, visit left subtree, visit right subtree
  * Sequence :  `A B D H I E C F G J`
* **In-order**: visit left subtree, visit node, visit right subtree (returns sorted list)
  * Sequence :  `H D I B E A F C G J`
* **Post-order**: visit left subtree, visit right subtree, visit node
  * Sequence :  `H I D E B F J G C A`
* **Level-order**: breadth-first traversal, level by level
  * Sequence :  `A B C D E F G H I J`

#### Binary Search Tree
* A *binary search tree* is an ordered binary tree structure which node's value is greater than all keys in left sub-tree but less than keys in the right
* Time complexity: 
  * **Search**: `O(logn)`
  * **Insert**: `O(logn)` (search and insert node when a leaf is reached)
  * **Delete**: `O(logn)` :
  * Case 1 : delete node with no children -  just remove node
  * Case 2 : delete node with one child -  replace node with its subtree
  * Case 3 : delete node two children - swap with minimum value in right subtree or maximum value in left subtree, then delete the node 

### Binary Heap
* *Heap* is a complete binary tree data structure that satisfies *heap property*
* **Heap property** : 
  * Every row is complete except for perhaps last row
  * Left child isn't necessary to be less than right child, but parent node must be smaller than both (*min-heap*)
  * Fast at insertion and deletion `O(logn)`, slow at traversal and searching
* **Min-heap** : value of each node is greater or equal to the value of parent (value ascending)
* **Max-heap** : value of each node is smaller or equal to the value of parent (value descending)
* Time complexity (min-heap):
  * Heapify : `O(logn)`
  * Find min : `O(1)`
  * Decrease value : `O(log n)` (if decreased value is larger than parent node, then do nothing)
  * Insert : `O(log n)` (append at the end of *heap*. precolation up to maintain heap property)
  * Delete min : `O(log n)` (swap with last element, precolation down to restore heap property)
  * Heapsort : `O(nlogn)` (n items * heapify)
* Can implement as a list where a node at index `i` has children at `2i+1` and `2i+2`
* Can be implemented by using *array*
* **Note** : *Heap* can be used to implement *priority queue*

#### Trie
* Also called a *prefix tree*, a variant of n-ary tree in which characters are stored at each node
* Each path down the tree represents a word
* *Null* node is often used to indicate complete words
* A *hashtable* can't tell if a string is a prefix of any valid words but a *trie* can do this quickly
* Problems involving lists of valid words leverage a *trie* as optimization
* Useful for autocomplete

#### Other trees
* *AVL Tree* :
  * Self-balancing binary search tree
  * Pairs of sub-trees differ in height at most 1
  * Lookup, insertion, and deletion all take `O(log n)`
* *Splay Tree* :
  *  Self-balancing binary search tree
  *  Take advantage of locality in keys used in incoming lookup request
  *  Lookup, insertion, and deletion all take `O(log n)`
* And etc.

### Graph
* A *graph* is a collection of nodes with edges between (some of) them
* *Nodes* are vertices that correspond to object, *edges* are connection between objects
* **Acylic graph** : a graph without cycles
* **Directed graph (digraph)** : edges are directed from one vertex to another
* **Undirected** : edges are directed from both end
* **Adjacency list** : every vertex stores a list of adjacent vertices
  * In undirected graph, an edge like (a,b) will be stored twice : one in a's adjacent vertices and one in b's adjacent vertices
  * Graph is used because unlike tree, you can't necessarily reach all the nodes from a single node
  * An array or hash table is good enough to store adjacent list
  * Easily iterate through neighbors of a node (best for *BFS, DFS*, and etc.)
* **Adjacency matrices** : a N*N boolean matrix where a `true` value at `matrix[i][j]` indicates an edge from node i to node j
  * Could also use an integer matrix with 0s and 1s
  * Graph algorithms (BFS, DFS and etc.) will be less efficient on matrices (need iterate through all the nodes to identify a node's neighbor)
* **Spanning tree**: a tree that includes all nodes in the graph
  * **Minimum spanning tree**: a spanning tree with minimum total edge weights


## Sorting
### Insertion

* Maintain a sorted sublist and insert new elements in it appropriately
* Best `O(n)`, average `O(n^2)`, worst `O(n^2)`
* Swaps and comparisons : `O(n^2)`
* Adaptive : efficient when data sets are nearly sorted
* Stable : yes, does not change the relative order of elements with equal key
* Space : `O(1)` (in-place) 
* Efficiency : efficient when data set is small, more efficient in practice than most quadratic algorithm such as *selection sort* and *bubble sort*
* Scenario : when you sort playing cards on your hand
  * Elements in reverse order - slowest
  * Elements in order - one pass `O(n)`

### Bubble

* In each pass, compare adjacent pairs of elements and swap if in wrong order
* Best `O(n)`, average `O(n^2)`, worst `O(n^2)`
* Swaps and comparisons : `O(n^2)`
* Adaptive : efficient when data sets are nearly sorted `O(n)`
* Stable : yes
* Space : `O(1)` (in-place) 
* Named for the way smaller elements *bubble* to the top of list
* Scenario : detect a very small error (like swap two elements)
* Compared to insertion sort : 
  * *bubble* needs at least 2 passes while *insertion* needs 1 pass through data
  * slightly higher overhead than *insertion* sort

### Selection

* Repeatedly find the smallest element to the right and swap with current element
* Best `O(n^2)`, average `O(n^2)`, worst `O(n^2)`
* Swaps : `O(n)`
* Comparisons : `O(n^2)`
* Adaptive : no
* Stable : no (swap can change the relative order of items with same value, see definition below)
* Space : `O(1)` (in-place) 
* Scenario : useful when the cost of swapping is expensive
* Definition of *stable* : is that the relative order of elements with same value is maintained
* **Note** : one should never use selection sort for the reason of not being adaptive

### Merge

* Recursively divide input list into two halves, then recursively merge the two sorted halves (*divide and conquer*)
* Best `O(n log n)`, average `O(n log n)`, worst `O(n log n)`
* Swaps : `log(n)` to `1.5log(n)`
* Comparisons : `0.5log(n)` to `log(n)`
* Adaptive : no
* Stable : yes
* Space : `O(n)` for array, `O(logn)` for linked list

### Quick

* Pick an element as *pivot* and partition the given array around picked pivot, then recursively sort leaf and right sub-arrays (*divide and conquer*)
  * always pick first element as pivot
  * always pick last element as pivot
  * pick random element as pivot
  * pick median as pivot
* Best `O(n log n)`, average `O(n log n)`, worst `O(n^2)`
* Adaptive : no
* Stable : no
* Space : `O(logn)` recursion stack , `O(n)` in the worst (unlikely)
* Scenario : when carefully implemented, it can be low overhead and about two or three times faster than *merge sort* and *heap sort*
  * Always pick smallest or largest element as pivot - worst case `O(n^2)`
  * Always pick the middle element as pivot - best case `O(n)`
* In real-world, quick sort is fast in practice as its inner loop can be efficently implemented
* Worse case rarely occurs for a given type of data since the choice of pivot can always be changed
* Quick sort is tail-recursive - a tail call might lead to same subroutine being called again later in the call chain
* When stability is not the issue, quick sort is the general purpose sorting algorithm of choice
* Quick sort is preferred over merge sort for sorting arrays:
  *  In general form, quick sort is an in-place sort (no extra strorage needed) whereas merge sort needs `O(n)` extra storage 
  *  Allocating extra space increases running time too
  *  Randomized pivot has expected `O(nlogn)` time
* Quick sort is preferred over merge sort for sorting linked list:
  *  In linked list, we can insert items in the middle in `O(1)` space and time, therefore mrge sort can be implemented without extra space
  *  No random access is allowed in linked list and quick sort requires a lot of that, therefore overhead increases
* Quick sort (3-way) : same as 2-way quick sort but highly adaptive in common case with few unique keys (also higher overhead)

### Counting
* *Counting sort* sorts a collection of objects according to keys that are small integers
* Count the number of objects having disinct key values, then determine the positions of each key value 
  * Create a count array to store the count of each object
  * Modify the count array so that each element at each index stores the sum of previous counts
  * (Modified count array indicates the position of each object in the output sequence)
  * Output each object from the input sequence followed by decreasing its count by 1 (no change in order - stable)
* Best `O(n+k)`, average `O(n+k)`, worst `O(n^2)` (n is the number of elements in input array, k is range of input)
  * In practice, we set k = `O(n)`, so running time is `O(n)` 
* Stable : yes
* A more detailed running time :
  * `O(n)` - go through input data
  * `O(k)` - create and initialize buckets
  * `O(n)` - count each object
  * `O(k)` - create cumulative array
  * `O(n)` - populate output array
  * Total time : `O(3n + 2k)` -> `O(n + k)`
* Space: `O(n+k)` (count array is of size `O(k)` and output array is of size `O(n)`)
* Not a comparison sort!
* Scenario : suitable when variation in keys is not larger than number of items


### Bucket (bin) 
* *Bucket sort* works by distributing elements of array into buckets, each bucket is then sorted individually
  * Create k empty buckets which has fixed range
  * Insert item into bucket based on the value 
  * Sort individual bucket using any sort (Insertion, Bubble and etc.)
  * Concatenate all sorted buckets 
* Best `O(n+k)`, average `O(n+k)`, worst `O(n^2)`(n is the number of elements in input array, k is number of bucket)
  * best when the elements are fairly even distributed
  * More buckets = more comparisons! -> *Selection sort* with extra space, yeww!
  * worst when all elements are allocated to the same bucket
* Stable : yes
* Space: `O(n+k)` , worst case `O(nk)` **(need clarification)**
* *Bucket sort* is a distribution sort and a generatlization of *pigeonhole sort*
* Impossible to create buckets without knowing upper and lower bounds
* If bucket size is 1, then it is a selection sort!
* Mainly useful when input is uniformly distributed over range
* A generalization of *counting sort* as bucket size of 1 is essentially *counting sort* but more complex

### Radix
* *Radix sort* sorts data with integer keys by grouping keys by individual digit which share the same significant position and value
* Apply a stable counting sort to every single digit in a number
* Sort places from least to most significant (LSD)
* Best `O(nk)`, average `O(nk)`, worst `O(nk)` (n is the number of elements in input array, k is the number of digits of longest input number)
  * If k is insignificantly small, then `O(kn)` = `O(n)` 
* Space: `O(n+k)`
* Also not a comparison sort!
* Most Significant Digit (MSD) Radix Sort : started with most significant digit (more complicated)
* Difference between *counting* , *bucket* and *radix* sort:
  * Counting - buckets hold only a single value
  * Bucket - buckets hold a range of values
  * Radix - buckets hold values on digits within vales


## Searching

### Binary Search
* Given a sorted array, search a particular item in logarithmic time 
* Best `O(1)`, average `O(log n)`, worst `O(logn)`

### Depth-first Search (DFS)
* Start at root and explore as far as possible along each branch before *backtracking*
  *  Traverse left down a tree until it can't get further down
  *  Traverse one level up and get to right node and repeat the same process 
  *  When finished examining a branch, it moves to the right node of root, and repeat the same process again
  *  The right-most node is visited last
* Time complexity : `O(|E| + |V|)` (E is number of edges, V is number of vertices)
* Space complexity : `O(V)`
* Implement with a stack (doesn't need to keep track of nodes, so less memory intensive)

### Breadth-first Search (BFS)
* Start at root and explore neighbor nodes first before moving to next level neighbors
  * Visit every node on the same level, from left to right
  * Track the children nodes of the nodes on the current level
  * When finished examining a level, it moves to next level starting from left-most node
  * The bottom right-most node is visited last
* Time complexity : `O(|E| + |V|)` (E is number of edges, V is number of vertices)
* Space complexity : `O(V)`
* Implement with a queue (more memory needed to store pointers)
* Best in finding shortest path in graph
* ***BFS is not recursive! It uses queue!***


## Concepts of Algorithms 

### Recursion
* An algorithm that calls itself recusively
  * Recursive case - a conditional statement that is used to trigger the recursion
  * Base case - a conditional statement that is used to break the recursion
* Each recursive call adds a new layer to the stack, very space inefficient

### Dynamic Programming
* A general method for solving a problem with optimal substructure by breaking it down into overlapping subproblems
* **Overlapping sub-problem** : 
  *  Can be broken down into subproblems whose results are stored and reused several times (like *Fibonacci number*)
  *  Two ways to store the results : **Top-down (memoization)**  and **Bottom-up (tabulation)**
  *  **Top-down** : cache the results to subproblems and solve problem recursively
  *  **Bottom-up** : build up subproblems from base case up and avoid recursive overhead
* **Optimal sub-structure** :
  * Can be constructed efficiently from optimal solutions of its subproblems (like *shortest path*)


### Greedy Algorithm
* A *greedy algorithm* follows the problem solving heuristic of making the locally optimal choice at each stage hoping to find a global optimum
* Once a decision is made, it never goes back (opposed to *backtracking*)
* A problem can be solved using *greedy* algorithm when it has the following:
  *  **Optimal sub-structure property** : an optimal global solution contains optimal solutions of all its sub-problems
  *  **Greedy choice property** : a global optimal solution can be obtained by greedily selecting a locally optimal choice

#### Dijkstra's Algorithm
* Find shortest path between two vertices in a graph
* Widely used in routing and as a subroutine in other graph algorithms
* Time complexity : `O(|E|+|V|log|V|)`

#### Bellman-Ford Algorithm
* Compute shortest paths from a single source to all other nodes in the graph
* Can handle negative edge weights & detect negative-weight cycles
* Time complexity : `O(|V||E|)`

#### Huffman Coding
* *Huffman coding* is a a particular type of optimal prefix code that is commonly used for lossless data compression
* Time complexity : `O(nlogn)`

#### Prim's Algorithm
* Find a minimum spanning tree for a weighted undirected graph
* Time complexity : `O(ElogV)`

### Backtracking
* A refinement of brute force approach which systematically searches for a solution to a problem among all available options
* Backtrack from a partial solution as soon as it realizes that is not feasible


### Object-oriented Programming
* *Class*: blueprint from which individual objects are created
* *Object*: instance of class
* *Encapsulation*: information hiding to ensure data integrity
  * Declare variables of class as private
  * Provide public getter and setter to view or modify values
* *Inheritance*: a subclass inherits data and methods from superclass
* *Polymorphism*: ability of an object to take on many forms depending on their data type or class
  * Most common use of polymorphism is when a parent class reference is used to refer to a child class object 
  * Ability to redefine methods for derived classes
  * Useful when an object's class can't be determined at compile time
* *Abstraction* : concept of describing something in simpler term, abstracting away the details in order to focus on what's important
  * Interface (100% abstract type) and abstract class

### Object-oriented Design
* Sketch out the classes and methods to implement technical problems or real-life objects
* Step 1 : Handle ambiguity
  * Should always inquire *6 Ws* - who, what ,when, how, why
* Step 2 : Define core objects
  * For example, designing a *restaurant* might need these objects : Table, Guest, Party, Meal, Employee, Host and etc. 
* Step 3 : Analyze Relationships
  * Which object is member of which object? Relationship many-to-many? One-to-many?
* Step 4 : Investigate actions
  * Consider the key options that objects will take and how they relate to each other
  * For example, a Party walks into Restaurant, or a Guest requests Table from Host
* Design Patterns:
  * *Model-view-controller*: model stores data, controller updates model, view generates user interface
  * *Singleton*: restrict instantiation of a class to a single object
  * *Factory method*: use a factory object to create other objects rather than using a constructor


## More on Java Library

### java.lang.String
##### extends Object implements Serializable, CharSequence, Comparable<String>

* *char* `charAt(int index)`
* *int* `compareTo(String str)` 
  * return 0 if two strings are the same, positive value if argument string is smaller and vice versa 
* *int* `compareToIgnoreCase(String str)` 
  * same but ignore case sensitives
* *String* `concat(String str)` 
  * similar to `"+"` operator but more strict (no null or other type)
* *boolean* `contains(CharSequence s)` 
* *boolean* `contentEquals(CharSequence s)`
  * similar to `equals(Object o)` but less strict (can be of other type)
* To be added...

### java.util.ArrayList 
##### extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable

* *boolean* `add(E e)` - return `true` if collection changed as a result of the call, return `false` if collection does not permit duplicates
* *void* `add(int index, E e)`
* *boolean* `addAll(Collections<? extends E> c)` - append all elements in collection to the end of list, return true if the list changed
* *boolean* `addAll(int index, Collection<? extends E> c)` - insert all of the elements in the specified index and shift the rest elements to the right
* To be added...




* *Prepared by Issac*, animation gifs retrieved from www.wikipedia.com
* Copyright 2016. All rights reserved.


