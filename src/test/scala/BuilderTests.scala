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

    it("should use an empty string as the default value for unspecified string fields") {
      val builder = new Builder[HasFieldsWithDefaults]
      val unnamed = builder.build
      unnamed should equal(HasFieldsWithDefaults(name = ""))
    }

    it("should use 0 as the default value for unspecified integer fields") {
       new Builder[HasAnAge].build should equal(HasAnAge(0))
    }

    it("should use 0 as the default value for unspecified long fields") {
      new Builder[HasABigNumber].build should equal(HasABigNumber(0L))
    }

    it("should use 0.0 as the default value for unspecified float fields") {
      new Builder[PreciseThing].build should equal(PreciseThing(0.0f))
    }

    it("should use 0.0 as the default value for unspecified double fields") {
      new Builder[VeryPreciseThing].build should equal(VeryPreciseThing(0.0))
    }

    it("should use false as the default value for unspecified boolean fields") {
       new Builder[MaybeAwesome].build should equal(MaybeAwesome(false))
    }

    it("should use a space as the default value for unspecified char fields") {
      new Builder[BitOfACharacter].build should equal(BitOfACharacter(' '))
    }

    it("should use None as the default value for unspecified Option fields") {
      new Builder[PotentiallyPriceless].build should equal(PotentiallyPriceless(None))
    }
	}
}

case class NoFields

case class OneNameField(name: String)

case class TwoNameFields(firstName: String, lastName: String)

case class HasFieldsWithDefaults(name: String, alive: Boolean = true)

case class HasAnAge(age: Int)

case class MaybeAwesome(isAwesome: Boolean)

case class HasABigNumber(aBigNumber: Long)

case class PreciseThing(value: Float)

case class VeryPreciseThing(value: Double)

case class BitOfACharacter(character: Char)

case class PotentiallyPriceless(price: Option[Double])