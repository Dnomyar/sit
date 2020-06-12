package fr.damienraymond.ddd.command
import fr.damienraymond.ddd.Event
import zio.ZIO
import zio.console._

object LoggerCommandBusMiddleware extends CommandMiddleware {
  override def dispatch(next: () => ZIO[Console, Any, Set[Event]])(command: Command): ZIO[Console, Any, Set[Event]] = {

    for {
      _ <- putStrLn(s"[LoggerCommandBusMiddleware] Command received $command")
      events <- next()
      _ <- putStrLn(s"[LoggerCommandBusMiddleware] Event returned ${events}")
    } yield events

  }
}
