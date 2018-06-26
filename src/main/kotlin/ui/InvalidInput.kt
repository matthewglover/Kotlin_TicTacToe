package ui

enum class InvalidInput(val message: String) {
  NON_INTEGER("Oops, that's not a valid move!"),
  MOVE_TAKEN("Oops, that move's already taken!")
}