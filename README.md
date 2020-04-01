# Sit - Git implementation in Scala

This project is a playground to experiment with domain modeling, algorithms, data structures and functional effects.

Features:
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


## Data-structures

### `IndexedList[T]`

This abstraction purpose is to deal with insertion and deletion of lines at some index. For the moment, the implementation is using a (Linked)List which is convenient because it is ordered and the insertion shifts all the indexes (line number in our case). However, in terms of performance, it is probably not the best.

Main operations :
- `get(idx: Int): Option[T]`
- `delete(idx: Int): IndexedList[T]`
- `insertAt(idx: Int, t: T): IndexedList[T]`
- `update(idx: Int, f: T => T): IndexedList[T]` 