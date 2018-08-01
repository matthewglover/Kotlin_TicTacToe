package game

enum class InvalidInput(val message: String) {
  MOVE_TAKEN("Oops, that move's already taken!"),
  NON_INTEGER("Oops, that's not a valid move!"),
  OUT_OF_BOUNDS("Oops, that move's out of bounds!")
}