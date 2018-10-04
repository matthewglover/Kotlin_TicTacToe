package core

sealed class Tile {
  data class Taken(val tileNumber: Int, val mark: Mark) : Tile()
  data class Free(val number: Int) : Tile()
}
