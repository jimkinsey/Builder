package com.github.jimkinsey.builder

import scala.reflect.runtime.universe._
import scala.reflect.runtime._

class Builder[T: TypeTag](
  val default: (String) => Any = Defaults.degenerate,
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
	
	private lazy val tFields = tDeclarations.filter { symbol => symbol match {
			case symbol: MethodSymbol => symbol.isParamAccessor
			case _ => false
		}
	}.asInstanceOf[List[MethodSymbol]]
	
	private lazy val tDeclarations = tType.declarations.sorted
		
	private lazy val tConstructor = {
		val mirror = universe.runtimeMirror(getClass.getClassLoader)
		val reflectClass = mirror.reflectClass(tClass)
		reflectClass.reflectConstructor(tType.declaration(nme.CONSTRUCTOR).asMethod)
	}

  private def defaultValue(field: MethodSymbol, fieldIndex: Int) = {
    val defaultMethodName = "apply$default$%s" format fieldIndex + 1
    val module = tClass.companionSymbol.asModule
    val instanceMirror = currentMirror reflect (currentMirror reflectModule module).instance
    val typeSignature = instanceMirror.symbol.typeSignature
    typeSignature member newTermName(defaultMethodName) match {
      case NoSymbol => default(field.returnType.typeSymbol.fullName)
      case defaultMethodSymbol => (instanceMirror reflectMethod defaultMethodSymbol.asMethod)()
    }
  }

}