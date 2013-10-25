import com.github.jimkinsey.builder.Defaults
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import scala.reflect.runtime.universe._
import org.scalatest.Tag

class DefaultsTests extends FunSpec with ShouldMatchers with MockitoSugar {

  describe("The degenerate default") {

    it("should be an empty string for Strings") {
      Defaults.degenerate(typeOf[String]) should equal(Some(""))
    }

    it("should be 0 for integers") {
      Defaults.degenerate(typeOf[Int]) should equal(Some(0))
    }

    it("should be 0 for longs") {
      Defaults.degenerate(typeOf[Long]) should equal(Some(0L))
    }

    it("should be 0.0 for floats") {
      Defaults.degenerate(typeOf[Float]) should equal(Some(0.0f))
    }

    it("should be 0.0 for doubles") {
      Defaults.degenerate(typeOf[Double]) should equal(Some(0.0))
    }

    it("should be false for booleans") {
      Defaults.degenerate(typeOf[Boolean]) should equal(Some(false))
    }

    it("should be a space for chars") {
      Defaults.degenerate(typeOf[Char]) should equal(Some(' '))
    }

    it("should be None for Options") {
      Defaults.degenerate(typeOf[Option[_]]) should equal(Some(None))
    }

    it("should be an empty list for List") {
      Defaults.degenerate(typeOf[List[_]]) should equal(Some(List()))
    }

    it("should be an empty set for Set") {
      Defaults.degenerate(typeOf[Set[_]]) should equal(Some(Set()))
    }

    it("should be an empty map for Map") {
      Defaults.degenerate(typeOf[Map[_, _]]) should equal(Some(Map()))
    }

    it("should be an empty set for Seq") {
      Defaults.degenerate(typeOf[Seq[_]]) should equal(Some(Seq()))
    }

    it("should be a Tuple containing the appropriate degenerate defaults for a Tuple (2 values)") {
      Defaults.degenerate(typeOf[(String, Int)]) should equal(Some(("", 0)))
    }

  }

}