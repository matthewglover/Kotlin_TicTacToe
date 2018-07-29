package game

sealed class Tile
data class Move(val number: Int, val mark: Mark) : Tile()
data class FreeTile(val number: Int) : Tile()
