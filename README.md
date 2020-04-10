# Sit - Git implementation in Scala

This project is a playground to experiment with domain modeling, algorithms, data structures and functional effects.

## Details of features

### Model

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


### CQRS - Commands
- [ ] `CommitCommand` and `CommitCommandHandler`

## Katas / Dojo
As this project covers a lot of different skills (domain modeling, algorithms, data structures, CQRS/ES, functional effects, etc.), it is a great opportunity to share some knowledge. 

Here are the details of the 1h katas/dojos organized using this project : 

### #1 Implementing `ShowChangeService` (algorithm)
The idea of this kata is to implement the function [`ShowChangeService.show(init: String, change: Change): String`](https://github.com/Dnomyar/sit/blob/925bcf95b676149a130b8ee5451d547ece1df682/src/main/scala/fr/damienraymond/sit/change/ShowChange.scala#L9) . The goal of this function is to replicate the output of `git diff` (line added with `+` and line removed with a `-` at the beginning).

The challenge here is to make sure that you are not interleaving line added and line removed. The expected behevior is that every block of line added should be added after a block of line removed.

[Here is the test](https://github.com/Dnomyar/sit/blob/925bcf95b676149a130b8ee5451d547ece1df682/src/test/scala/fr/damienraymond/sit/change/ShowChangeSpec.scala#L64-L86)

### #2 Exploring Commit domain model (domain modeling)
The idea of this kata is to start to explore how commits can be modeled and start to think about commit event sourcing. How to represent a single commit? What are the main properties of a commit? Is there different types of commit? 

[Here is the code](https://github.com/Dnomyar/sit/blob/3c468bd248d4d6f8405bd465b369c8bed47aa928/src/main/scala/fr/damienraymond/sit/domain/model/commit/AbstractCommit.scala#L5-L14)
  
  
### #3 Commit Event Sourcing (event sourcing and domain modeling)
The idea of this kata would be to continue the work done in the previous kata (domain modeling of a commit) in order to handle a collection of commit.

It would be the opportunity to cover a bit of domain modeling (how to represent a collection of commit) and event sourcing (implementation of applying commits)

[Here is the code](https://github.com/Dnomyar/sit/blob/3c468bd248d4d6f8405bd465b369c8bed47aa928/src/main/scala/fr/damienraymond/sit/domain/model/commit/CommitHistory.scala#L7-L42)

[Here is the test](https://github.com/Dnomyar/sit/blob/3c468bd248d4d6f8405bd465b369c8bed47aa928/src/test/scala/fr/damienraymond/sit/domain/model/change/CommitHistorySpec.scala#L10)
  
 
## Data-structures

### `IndexedList[T]`

This abstraction purpose is to deal with insertion and deletion of lines at some index. For the moment, the implementation is using a (Linked)List which is convenient because it is ordered and the insertion shifts all the indexes (line number in our case). However, in terms of performance, it is probably not the best.

Main operations :
- `get(idx: Int): Option[T]`
- `delete(idx: Int): IndexedList[T]`
- `insertAt(idx: Int, t: T): IndexedList[T]`
- `update(idx: Int, f: T => T): IndexedList[T]`
