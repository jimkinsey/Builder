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
    case WithOrAndNo(_, field) if (args.isEmpty)=> copyWithField(field, default(fieldNamed(field).returnType).orNull)
    case WithOrAnd(_, field) => copyWithField(field, args(0))
  }
  
  private lazy val WithOrAnd = """(with|and)([A-Za-z]+)""".r
  private lazy val WithOrAndNo = """(with|and)No([A-Za-z]+)""".r
  
  private def copyWithField(fieldName: String, value: Any) = {
    new Builder[T](default, fields + (decapitalise(fieldName) -> value))
  }
  
  private def decapitalise(str: String) = str match {
    case str if !str.isEmpty => str.head.toString.toLowerCase + str.tail
    case _ => str
  }
    
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

  private def fieldNamed(name: String) = tFields.find( field => shortName(field) == decapitalise(name) ) match {
    case Some(field) => field.asInstanceOf[MethodSymbol]
    case _ => throw new IllegalArgumentException("No field with name [%s] found; available fields: (%s)".format(decapitalise(name), tFields.map(_.fullName).mkString(",")))
  }

  private def shortName(field: Symbol) = field.fullName.split('.').reverse.head
  
  private def defaultValue(field: Symbol, fieldIndex: Int) = field match {
    case field: MethodSymbol =>
      fieldDefaultMethodSymbol(fieldIndex) match {
        case NoSymbol => default(field.returnType).orNull
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
