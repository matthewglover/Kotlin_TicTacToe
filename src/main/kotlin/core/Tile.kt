package core

sealed class Tile
data class Move(val tileNumber: Int, val mark: Mark) : Tile()
data class FreeTile(val number: Int) : Tile()
