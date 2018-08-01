package ui

object ConsoleIO : IO {
  private const val CLEAR_SCREEN = "\u001b[H\u001b[2J"

  override fun clearScreen() = write(CLEAR_SCREEN)

  override fun readLine(): String? = kotlin.io.readLine()

  override fun write(text: String) = print(text)
}