package com.github.jimkinsey.builder

import scala.reflect.runtime.universe._
import scala.reflect.runtime._

class Builder[T: TypeTag]() {

	def build: T = {
		tConstructor().asInstanceOf[T]
	}
	
	private lazy val tType = universe.typeOf[T]
	
	private lazy val tClass = tType.typeSymbol.asClass
		
	private lazy val tConstructor = {
		val mirror = universe.runtimeMirror(getClass.getClassLoader)
		val reflectClass = mirror.reflectClass(tClass)
		reflectClass.reflectConstructor(tType.declaration(nme.CONSTRUCTOR).asMethod)
	}

}