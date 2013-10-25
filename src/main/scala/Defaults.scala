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
    defaults.get(typeRef.typeSymbol.fullName) match {
      case None => tupleDefault(typeRef)
      case default => default
    }
  }

  private lazy val Tuple2Types = """\((.*), (.*)\)""".r

  private def tupleDefault(typeRef: Type): Option[(_,_)] = typeRef.toString() match {
    case Tuple2Types(type_1, type_2) => Some(defaults(fullTypeNameFor(type_1)), defaults(fullTypeNameFor(type_2)))
    case _ => None
  }

  private def fullTypeNameFor(typeName: String) = typeName match {
    case "String" => "java.lang.String"
    case other => s"scala.$other"
  }

}
