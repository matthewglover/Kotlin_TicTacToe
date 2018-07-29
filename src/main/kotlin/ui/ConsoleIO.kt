package ui

object ConsoleIO : IO {
  private const val CLEAR_SCREEN = "\\033[H\\033[2J"

  override fun clearScreen() = write(CLEAR_SCREEN)

  override fun readLine(): String? = kotlin.io.readLine()

  override fun write(t: String) = print(t)
}