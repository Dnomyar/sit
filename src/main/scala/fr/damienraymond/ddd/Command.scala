package fr.damienraymond.ddd

trait Command extends Event {
  def handler: CommandHandler[this.type, Any]
}



