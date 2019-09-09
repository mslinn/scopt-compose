import scopt.{ OParser, OParserBuilder, Read }

object SubProject1  {
  trait ConfigLike1[R] {
    def withDebug(value: Boolean): R
  }

  def parser1[R <: ConfigLike1[R]]: OParser[_, R] = {
    val builder = OParser.builder[R]
    import builder._
    OParser.sequence(
      opt[Unit]("debug").action((_, c) => c.withDebug(true)),
      note("something")
    )
  }
}

object SubProject2 {
  trait ConfigLike2[R] {
    def withVerbose(value: Boolean): R
  }

  def parser2[R <: ConfigLike2[R]]: OParser[_, R] = {
    val builder = OParser.builder[R]
    import builder._
    OParser.sequence(
      opt[Unit]("verbose").action((_, c) => c.withVerbose(true)),
      note("something else")
    )
  }
}

object Main extends App {
  // compose config datatypes and parsers
  case class Config1(debug: Boolean = false, verbose: Boolean = false)
      extends SubProject1.ConfigLike1[Config1]
      with SubProject2.ConfigLike2[Config1] {
    override def withDebug(value: Boolean) = copy(debug = value)
    override def withVerbose(value: Boolean) = copy(verbose = value)
  }

  val parser3: OParser[_, Config1] = {
    val builder = OParser.builder[Config1]
    import builder._
    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      SubProject1.parser1,
      SubProject2.parser2
    )
  }
}
