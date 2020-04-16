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
  
  
### #3 Commit Event Sourcing (folding, event sourcing and domain modeling)
The idea of this kata would be to continue the work done in the previous kata (domain modeling of a commit) in order to handle a collection of commit.

To simplify, we are going to work only with one file. The goal is to implement a function that take a list of changes and that returns the file as an output. 

It would be the opportunity to cover a bit of domain modeling (how to represent a collection of commit) and event sourcing (implementation of applying commits)

[Here is the code](https://github.com/Dnomyar/sit/blob/3c468bd248d4d6f8405bd465b369c8bed47aa928/src/main/scala/fr/damienraymond/sit/domain/model/change/Change.scala#L16-L29)

[Here is the test](https://github.com/Dnomyar/sit/blob/3c468bd248d4d6f8405bd465b369c8bed47aa928/src/test/scala/fr/damienraymond/sit/domain/model/change/ChangeSpec.scala#L13-L49)
  
### # Group by consecutive values (algorithm)
As far as I know, when git is displaying changes, it groups consecutive line removed together. After the block of line removed, line added are displayed. This is part of kata `#1` that we did not have time to do.

Line removed is represented as `SortedSet[Int]`. The goal is to write the function that is going to create `List[SortedSet[Int]]` where the list contains set composed only of consecutive numbers. 

The challenge here is to write an easily understandable function the has a correct time complexity (`O(n)`).

Example :
```
val input = SortedSet(1,2,3,10,11,12,60)
val output = List(SortedSet(1,2,3), SortedSet(10,11,12), SortedSet(60))
```

[Here is the code](https://github.com/Dnomyar/sit/blob/fb2bd7ab28b82cede5fa49540370ee718e65afd0/src/main/scala/fr/damienraymond/sit/domain/model/change/LinesRemoved.scala#L7-L32)

[Here is the test](https://github.com/Dnomyar/sit/blob/fb2bd7ab28b82cede5fa49540370ee718e65afd0/src/test/scala/fr/damienraymond/sit/domain/model/change/LinesRemovedSpec.scala#L13-L32)
 
 
## Data-structures

### `IndexedList[T]`

This abstraction purpose is to deal with insertion and deletion of lines at some index. For the moment, the implementation is using a (Linked)List which is convenient because it is ordered and the insertion shifts all the indexes (line number in our case). However, in terms of performance, it is probably not the best.

Main operations :
- `get(idx: Int): Option[T]`
- `delete(idx: Int): IndexedList[T]`
- `insertAt(idx: Int, t: T): IndexedList[T]`
- `update(idx: Int, f: T => T): IndexedList[T]`


## Sources 
- How different are different diff algorithms in Git? https://link.springer.com/article/10.1007/s10664-019-09772-z