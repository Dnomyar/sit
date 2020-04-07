package fr.damienraymond.sit.domain.model.branch

import fr.damienraymond.sit.domain.model.commit.CommitHash

case class Branch(name: BranchName, head: CommitHash)
