package fr.damienraymond.sit.domain.model.change

import fr.damienraymond.sit.domain.service.change.ShowChangeService
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ShowChangeServiceSpec extends AnyFlatSpec with Matchers {

  behavior of "ShowChange"

  it should "show the initial file if the change is empty" in {

    val initFile =
      """hello
        |world""".stripMargin

    ShowChangeService.show(initFile, Change.empty) should be (initFile)

  }

  it should "show line added" in {

    val initFile =
      """hello
        |world""".stripMargin

    val change = Change(LinesRemoved.empty, LinesAdded(1 -> "bonjour"))
    ShowChangeService.show(initFile, change) should be (
      """hello
        |+bonjour
        |world""".stripMargin
    )

  }

  it should "show line deleted" in {

    val initFile =
      """hello
        |world""".stripMargin

    val change = Change(LinesRemoved(0), LinesAdded.empty)
    ShowChangeService.show(initFile, change) should be (
      """-hello
        |world""".stripMargin
    )

  }


  it should "show line added and line deleted 1" in {

    val initFile =
      """hello
        |world""".stripMargin

    val change = Change(LinesRemoved(1), LinesAdded(1 -> "Damien"))
    ShowChangeService.show(initFile, change) should be (
      """hello
        |-world
        |+Damien""".stripMargin
    )
  }

  it should "show line added and line deleted 2" in {

    val initFile =
      """hello
        |world
        |qwd;lj
        |qwdlkjqwd
        |lkqjwdlkjqwd
        |qwd
        |lqjqwd""".stripMargin

    val change = Change(LinesRemoved(1,2,3,4,5,6), LinesAdded(1 -> "Damien"))
    ShowChangeService.show(initFile, change) should be (
      """hello
        |-world
        |-qwd;lj
        |-qwdlkjqwd
        |-lkqjwdlkjqwd
        |-qwd
        |-lqjqwd
        |+Damien""".stripMargin
    )
  }




  it should "show line added and line deleted 3 - html" in {

    val initFile =
      """<html lang="en">
        |<head>
        |    <meta charset="UTF-8">
        |    <meta name="viewport" content="width=, initial-scale=1.0">
        |</head>
        |<body>
        |    <h1>Hello world</h1>
        |    <p>Lorem ipsum dolor sit amet consectetur, adipisicing elit. Dicta debitis enim tempora ab ipsum, mollitia voluptatum est. Quasi earum quis fuga ducimus! Eligendi corporis consectetur quasi similique facere! Aliquid, totam!</p>
        |</body>
        |</html>""".stripMargin

    val change = Change(LinesRemoved(3,6,7), LinesAdded(
      3 -> "    <title>Greetings</title>",
      6 -> "    <h1>Hello Damien</h1>",
      7 -> "    <div>",
      8 -> "        <p>Lorem ipsum dolor sit amet consectetur, adipisicing elit. Dicta debitis enim tempora ab ipsum, mollitia voluptatum est. Quasi earum quis fuga ducimus! Eligendi corporis consectetur quasi similique facere! Aliquid, totam!</p>",
      9 -> "    </div>",
    ))
    ShowChangeService.show(initFile, change) should be (
      """<html lang="en">
        |<head>
        |    <meta charset="UTF-8">
        |-    <meta name="viewport" content="width=, initial-scale=1.0">
        |+    <title>Greetings</title>
        |</head>
        |<body>
        |-    <h1>Hello world</h1>
        |-    <p>Lorem ipsum dolor sit amet consectetur, adipisicing elit. Dicta debitis enim tempora ab ipsum, mollitia voluptatum est. Quasi earum quis fuga ducimus! Eligendi corporis consectetur quasi similique facere! Aliquid, totam!</p>
        |+    <h1>Hello Damien</h1>
        |+    <div>
        |+        <p>Lorem ipsum dolor sit amet consectetur, adipisicing elit. Dicta debitis enim tempora ab ipsum, mollitia voluptatum est. Quasi earum quis fuga ducimus! Eligendi corporis consectetur quasi similique facere! Aliquid, totam!</p>
        |+    </div>
        |</body>
        |</html>""".stripMargin
    )
  }

}
