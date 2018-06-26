package game

sealed class Tile
data class Move(val number: Int, val mark: Mark) : Tile()
class BlankTile(val number: Int) : Tile()