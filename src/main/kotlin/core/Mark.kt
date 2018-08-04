package core

enum class Mark {
  ONE,
  TWO;

  override fun toString() = when (this) {
    ONE -> "X"
    TWO -> "O"
  }
}
