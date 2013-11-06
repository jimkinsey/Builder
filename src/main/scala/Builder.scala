package com.github.jimkinsey.builder

import scala.reflect.runtime.universe._
import scala.reflect.runtime._

class Builder[T: TypeTag](
  val default: (Type) => Option[Any] = Defaults.degenerate,
  private val fields: Map[String,Any] = Map()
) extends Dynamic {

  import scala.language.dynamics

  def build: T = {
    val values = tFields.zipWithIndex map {
      case (field, index) => fields.get(field.name.decoded) match {
        case Some(value) => value
        case _ => defaultValue(field, index)
      }
    }
    tConstructor(values:_*).asInstanceOf[T]
  }
  
  def applyDynamic(name: String)(args: Any*) = name match {
    case WithOrAnd(_, field) => copyWithField(field, args(0))
  }

  private def copyWithField(fieldName: String, value: Any) = {
    new Builder[T](default, fields + (decapitalise(fieldName) -> value))
  }

  private def decapitalise(str: String) = str match {
    case str if str.length > 1 => str(0).toString.toLowerCase + str.substring(1)
    case str if str.length == 1 => str.toLowerCase
    case _ => str
  }
  
  private lazy val WithOrAnd = """(with|and)([A-Za-z]+)""".r
    
  private lazy val tType = universe.typeOf[T]
  
  private lazy val tClass = tType.typeSymbol.asClass
  
  private lazy val tFields = tConstructor.symbol.paramss.flatten.map(param => tType.declaration(param.name))

  private lazy val tConstructor = {
    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val reflectClass = mirror.reflectClass(tClass)
    reflectClass.reflectConstructor(tType.declaration(nme.CONSTRUCTOR).asMethod)
  }

  private lazy val tModule = tClass.companionSymbol.asModule

  private lazy val tInstanceMirror = currentMirror reflect (currentMirror reflectModule tModule).instance

  private def defaultValue(field: Symbol, fieldIndex: Int) = field match {
    case field: MethodSymbol =>
      fieldDefaultMethodSymbol(fieldIndex) match {
        case NoSymbol => default(field.returnType).getOrElse(null)
        case defaultMethodSymbol => (tInstanceMirror reflectMethod defaultMethodSymbol.asMethod)()
      }
    case _ => null
  }

  private def fieldDefaultMethodName(index: Int) =  "apply$default$%s" format index + 1

  private def fieldDefaultMethodSymbol(index: Int) = {
    val typeSignature = tInstanceMirror.symbol.typeSignature
    typeSignature member newTermName(fieldDefaultMethodName(index))
  }

}
