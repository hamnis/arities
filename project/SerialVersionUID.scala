import java.io.File

object SerialVersionUID {
  def sha(template: File): String = {
    sbt.Hash.toHex(sbt.Hash(template))
  }

  def shaAsLong(template: File): Long = {
    java.lang.Long.parseLong(sbt.Hash.trimHashString(sha(template), 8).toUpperCase(), 16)
  }
}
