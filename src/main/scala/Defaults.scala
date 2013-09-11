package com.github.jimkinsey.builder

object Defaults {

  val degenerate = Map[String, Any](
    "java.lang.String" -> "",
    "scala.Int" -> 0,
    "scala.Long" -> 0L,
    "scala.Float" -> 0.0f,
    "scala.Double" -> 0.0,
    "scala.Boolean" -> false,
    "scala.Char" -> ' ',
    "scala.Option" -> None,
    "scala.collection.immutable.List" -> List(),
    "scala.collection.immutable.Set" -> Set(),
    "scala.collection.immutable.Map" -> Map(),
    "scala.collection.immutable.Seq" -> Seq()
  )

}
