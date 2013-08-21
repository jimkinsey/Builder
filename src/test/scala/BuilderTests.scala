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
		
		it("should set a field specified using the with prefix") {
			val builderWithField = new Builder[OneNameField].withName("Inego")
			val inego = builderWithField.build
			inego should equal(new OneNameField("Inego"))
		}
		
		it("should set two fields specified using the with and and prefixes") {
			val builderWithTwoFields = new Builder[TwoNameFields].withFirstName("Inego").andLastName("Montoya")
			val inegoMontoya = builderWithTwoFields.build
			inegoMontoya should equal(new TwoNameFields("Inego", "Montoya"))
		}
		
		it("should set two fields specified using the with and and prefixes regardless of order") {
			val builderWithTwoFields = new Builder[TwoNameFields].withLastName("Montoya").andFirstName("Inego")
			val inegoMontoya = builderWithTwoFields.build
			inegoMontoya should equal(new TwoNameFields("Inego", "Montoya"))
		}
	}
}

case class NoFields

case class OneNameField(name: String)

case class TwoNameFields(firstName: String, lastName: String)