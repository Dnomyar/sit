package fr.damienraymond.sit.domain.repository

import fr.damienraymond.sit.domain.model.file.{FilePath, FileUpdatedDate}

trait FileLastUpdateRepository extends Repository[FilePath, FileUpdatedDate]
