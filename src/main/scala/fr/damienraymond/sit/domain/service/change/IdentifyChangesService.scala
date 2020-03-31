package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.{File, FileChanged}
import zio.IO

object IdentifyChangesService {

  def identify(state: Set[File]): IO[Any, Set[FileChanged]] = {
    ???
  }

}
