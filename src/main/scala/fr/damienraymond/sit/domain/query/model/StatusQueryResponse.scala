package fr.damienraymond.sit.domain.query.model

import fr.damienraymond.sit.domain.model.file.{FilePath, FileStatus}

case class StatusQueryResponse(fileUpdated: Map[FilePath, FileStatus])
