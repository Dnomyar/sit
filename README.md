# Sit - Git implementation in Scala

This project is a playground to experiment with domain modeling, algorithms, data structures and functional effects.

## Details of features
- [ ] Diff (or change): show changes made to parts of file (with `+` and `-` at the beginning of the lines). 
    - [ ] Data-structure `IndexedList[T]` (see below). 
    - [ ] Model `Change` ADT (`LinesAdded` and `LinesRemoved`)
    - [ ] Service `IdentifyChangesService` and `ShowChangeService` 
- [ ] Commits
    - [ ] Model `AbstractCommit` ADT (`OrphanCommit`, `Commit` and `MergeCommit`), `CommitHash` and `CommitHistory`
    - [ ] Repository `CommitRepository`
- [ ] Files
    - [ ] Model `File`, `FileChanged` and `Filename`
- [ ] Branch
    - [ ] Model `Branch`, `Branches` and `BranchName`
    - [ ] Repository `CurrentBranchRepository`


## Katas / Dojo
As this project covers a lot of different skills (domain modeling, algorithms, data structures, CQRS/ES, functional effects, etc.), it is a great opportunity to share some knowledge. 

Here are the details of the 1h katas/dojos organized using this project : 

### #1 Implementing `ShowChangeService` (algorithm)
The idea of this kata is to implement the function `ShowChangeService.show(init: String, change: Change): String`. The goal of this function is to replicate the output of `git diff` (line added with `+` and line removed with a `-` at the beginning).

The challenge here is to make sure that you are not interleaving line added and line removed. The expected behevior is that every block of line added should be added after a block of line removed.

This test exposes the issue: https://github.com/Dnomyar/sit/blob/925bcf95b676149a130b8ee5451d547ece1df682/src/test/scala/fr/damienraymond/sit/change/ShowChangeSpec.scala#L64-L86

### #2 Exploring Commit domain model (domain modeling, event sourcing)
The idea of this kata is to start to explore how commits can be modeled and start to think about commit event sourcing.

- 1 - How to represent a single commit? What are the main properties of a commit? Is there different types of commit? 
- 2 - (if enough time) A commit by itself is not enough. How to represent a collection of commit?   
  
  
 
## Data-structures

### `IndexedList[T]`

This abstraction purpose is to deal with insertion and deletion of lines at some index. For the moment, the implementation is using a (Linked)List which is convenient because it is ordered and the insertion shifts all the indexes (line number in our case). However, in terms of performance, it is probably not the best.

Main operations :
- `get(idx: Int): Option[T]`
- `delete(idx: Int): IndexedList[T]`
- `insertAt(idx: Int, t: T): IndexedList[T]`
- `update(idx: Int, f: T => T): IndexedList[T]`
