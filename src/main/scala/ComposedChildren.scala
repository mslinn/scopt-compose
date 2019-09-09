import scopt.{OParser, OParserBuilder, Read}

// Sample code for "composing with cmd("...").children(...)"
// Another way of reusing an OParser is passing them into .children(...) method of a cmd("...") parser.
// The original code example is rather scrambled, and incomplete.
// I've tried to piece together what I think the original author meant, but it seems 2 case classes are required unless I misunderstand something.

/** Parses command lines like:
 * update from to
 * update -f from to
 * update --foo --debug from to
 * update --foo from to
 * status from to
 */
object ComposedChildren extends App {
  case class ConfigUpdate(foo: Int=0, debug: Boolean=false, update: Boolean=false, status: Boolean=false)
  case class ConfigStatus(source: String, dest: String, update: Boolean=false, status: Boolean=false)
  case class Config(selector: String, update: Option[ConfigUpdate]=None, status: Option[ConfigStatus]=None)

  val parserUpdate = {
    val builder: OParserBuilder[ConfigUpdate] = OParser.builder[ConfigUpdate]
    import builder._
    OParser.sequence(
      opt[Int]('f', "foo")
        .action((x, c) => c.copy(foo = x))
        .text("foo is an integer property"),
      opt[Unit]("debug")
        .action((_, c) => c.copy(debug = true))
        .text("debug is a flag")
    )
  }

  val parserStatus = {
    val builder: OParserBuilder[ConfigStatus] = OParser.builder[ConfigStatus]
    import builder._
    OParser.sequence(
      arg[String]("<source>")
        .action((x, c) => c.copy(source = x)),
      arg[String]("<dest>")
        .action((x, c) => c.copy(dest = x))
    )
  }

  val parser = {
    val builder = OParser.builder[Config]
    import builder._
    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      cmd("update")
        .action((x, c) => c.copy(update = true))
        .children(parserUpdate),
      cmd("status")
        .action((x, c) => c.copy(status = true))
        .children(parserStatus)
    )
  }

  // OParser.parse returns Option[Config]
  OParser.parse(parser, args, Config()) match {
    case Some(config) =>
      println("Parsed config")

    case _ =>
      // arguments are bad, error message will have been displayed
  }

}
