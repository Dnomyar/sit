package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.branch.Branch
import fr.damienraymond.sit.domain.repository.CurrentBranchRepository
import fr.damienraymond.sit.domain.service.change.CurrentBranchService.CurrentBranchNotFound
import zio.{IO, ZIO}

class CurrentBranchService(currentBranchRepository: CurrentBranchRepository) {

  val getCurrentBranch: IO[Exception, Branch] =
    currentBranchRepository
      .get
      .flatMap {
        case None => ZIO.fail(CurrentBranchNotFound())
        case Some(branchName) => ZIO.succeed(branchName)
      }


}

object CurrentBranchService {
  case class CurrentBranchNotFound() extends Exception
}