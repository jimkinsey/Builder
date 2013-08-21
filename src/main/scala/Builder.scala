package com.github.jimkinsey.builder

import scala.reflect.runtime.universe._
import scala.reflect.runtime._

class Builder[T: TypeTag](private val fields: Map[String,Any] = Map()) extends Dynamic {

	import scala.language.dynamics

	def build: T = {
		val values = tFields.map(v => fields(v.name.decoded))		
		tConstructor(values:_*).asInstanceOf[T]
	}
	
	def applyDynamic(name: String)(args: Any*) = name match {
		case WithOrAnd(_, field) => new Builder[T](fields + (decapitalise(field) -> args(0)))
	}
	
	private def decapitalise(str: String) = str match {
		case str if str.length > 1 => str(0).toString.toLowerCase + str.substring(1)
		case str if str.length == 1 => str.toLowerCase
		case _ => str
	}
	
	private lazy val WithOrAnd = """(with|and)([A-Za-z]+)""".r
		
	private lazy val tType = universe.typeOf[T]
	
	private lazy val tClass = tType.typeSymbol.asClass
	
	private lazy val tFields = tType.declarations.sorted.filter { symbol => symbol match {
			case symbol: MethodSymbol => symbol.isParamAccessor
			case _ => false
		}
	}
		
	private lazy val tConstructor = {
		val mirror = universe.runtimeMirror(getClass.getClassLoader)
		val reflectClass = mirror.reflectClass(tClass)
		reflectClass.reflectConstructor(tType.declaration(nme.CONSTRUCTOR).asMethod)
	}

}