package com.github.jimkinsey.builder

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.reflect.runtime.universe._

class BuilderTests extends FunSpec with ShouldMatchers {

	describe("Invoking build") {
	
		it("should create an instance of the specified type") {
			val noFieldsBuilder = new Builder[NoFields]
			val builtNoFields = noFieldsBuilder.build
			builtNoFields should equal(NoFields())
		}	
	}
}

case class NoFields