package fr.damienraymond.ddd.query

import fr.damienraymond.ddd.Event

trait Query extends Event {
  type RETURN_TYPE
}
