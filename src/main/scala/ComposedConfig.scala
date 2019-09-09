import scopt.{ OParser, OParserBuilder, Read }

// Here's a demonstration of how we can split up the Config datatype.

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

/** Parser1 and parser2 are written against an abstract type R that meets type constraint of being a subtype of ConfigLike1[R] and ConfigLike2[R].
 * In parser3, R gets bound to a concrete datatype Config. */
object ComposedConfig extends App {
  // compose config datatypes and parsers
  case class Config(debug: Boolean = false, verbose: Boolean = false)
      extends SubProject1.ConfigLike1[Config]
      with SubProject2.ConfigLike2[Config]

  val parser: OParser[_, Config] = {
    val builder = OParser.builder[Config]
    import builder._
    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      SubProject1.parser1,
      SubProject2.parser2
    )
  }

  // OParser.parse returns Option[Config]
  OParser.parse(parser, args, Config()) match {
    case Some(config) =>
      // do something
    case _ =>
      // arguments are bad, error message will have been displayed
  }
}
