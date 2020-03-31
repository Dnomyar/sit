# Sit - Git implementation in Scala

This project is a playground to experiment with domain modeling, algorithms, data structures and functional effects.

Features:
- :white_circle: `git diff`: show changes made to parts of file (with `+` and `-` at the beginning of the lines). Highlights:
    - Introduced `IndexedList[T]` data-structure. 
    - A change is described by a set (i.e. `Set(2,3,4,7,678)`) of line removed and a map for line added (i.e. `Map(34 -> "new line", 567 -> "other line")`)
- :white_circle: Commits


## Data-structures

### `IndexedList[T]`

This abstraction purpose is to deal with insertion and deletion of lines at some index. For the moment, the implementation is using a (Linked)List which is convenient because it is ordered and the insertion shifts all the indexes (line number in our case). However, in terms of performance, it is probably not the best.

Main operations :
- `get(idx: Int): Option[T]`
- `delete(idx: Int): IndexedList[T]`
- `insertAt(idx: Int, t: T): IndexedList[T]`
- `update(idx: Int, f: T => T): IndexedList[T]` 