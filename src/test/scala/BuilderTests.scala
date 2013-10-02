package com.github.jimkinsey.builder

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.reflect.runtime.universe._
import org.scalatest.Tag

class BuilderTests extends FunSpec with ShouldMatchers {

	describe("Invoking build") {
	
		it("should create an instance of the specified type") {
			val noFieldsBuilder = new Builder[NoFields]
			val builtNoFields = noFieldsBuilder.build
			builtNoFields should equal(NoFields())
		}	
		
		it("should set a field specified using the with prefix") {
			val builderWithField = new Builder[OneNameField].withName("Inego")
			val inego = builderWithField.build
			inego should equal(OneNameField("Inego"))
		}
		
		it("should set two fields specified using the with and and prefixes") {
			val builderWithTwoFields = new Builder[TwoNameFields].withFirstName("Inego").andLastName("Montoya")
			val inegoMontoya = builderWithTwoFields.build
			inegoMontoya should equal(TwoNameFields("Inego", "Montoya"))
		}
		
		it("should set two fields specified using the with and and prefixes regardless of order") {
			val builderWithTwoFields = new Builder[TwoNameFields].withLastName("Montoya").andFirstName("Inego")
			val inegoMontoya = builderWithTwoFields.build
			inegoMontoya should equal(TwoNameFields("Inego", "Montoya"))
		}
		
		it("should play nicely with fields with default values", Tag("defaults")) {
			val builder = new Builder[HasFieldsWithDefaults].withName("Wesley")
			val wesley = builder.build
			wesley should equal(HasFieldsWithDefaults("Wesley"))
		}

	    it("should use the default function when an unspecified field has no default value") {
	      def defaultFn(typeRef: Type): Any = "A suitable default"
	      val defaultedName = new Builder[HasFieldsWithDefaults](defaultFn).build
	      defaultedName should equal(HasFieldsWithDefaults(name = "A suitable default"))
	    }

	}
}

case class NoFields

case class OneNameField(name: String)

case class TwoNameFields(firstName: String, lastName: String)

case class HasFieldsWithDefaults(name: String, alive: Boolean = true)
