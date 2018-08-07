package gameOptions

enum class PlayerType {
  HUMAN,
  COMPUTER;

  fun toPrintableString() =
      toString()
          .toLowerCase()
          .capitalize()
}