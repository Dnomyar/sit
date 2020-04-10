package fr.damienraymond.sit.domain.repository

import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, Filename}

trait FileLastUpdateRepository extends Repository[Filename, FileUpdatedDate]
