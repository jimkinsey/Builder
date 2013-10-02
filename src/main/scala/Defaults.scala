package com.github.jimkinsey.builder

import scala.reflect.runtime.universe._

object Defaults {

  val defaults: Map[String, Any] = Map[String, Any](
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
    "scala.collection.Seq" -> Seq()
  )

  def degenerate(typeRef: Type): Option[Any] = {
    defaults.get(typeRef.typeSymbol.fullName)
  }

}
