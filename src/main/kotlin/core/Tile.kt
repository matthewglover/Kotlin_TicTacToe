package core

sealed class Tile
data class TakenTile(val tileNumber: Int, val mark: Mark) : Tile()
data class FreeTile(val number: Int) : Tile()
