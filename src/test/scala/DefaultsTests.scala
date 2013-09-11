import com.github.jimkinsey.builder.{Builder, Defaults}
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class DefaultsTests  extends FunSpec with ShouldMatchers {

  describe("The degenerate default") {

    it("should be an empty string for Strings") {
      Defaults.degenerate("java.lang.String") should equal("")
    }

    it("should be 0 for integers") {
      Defaults.degenerate("scala.Int") should equal(0)
    }

    it("should be 0 for longs") {
      Defaults.degenerate("scala.Long") should equal(0L)
    }

    it("should be 0.0 for floats") {
      Defaults.degenerate("scala.Float") should equal(0.0f)
    }

    it("should be 0.0 for doubles") {
      Defaults.degenerate("scala.Double") should equal(0.0)
    }

    it("should be false for booleans") {
      Defaults.degenerate("scala.Boolean") should equal(false)
    }

    it("should be a space for chars") {
      Defaults.degenerate("scala.Char") should equal(' ')
    }

    it("should be None for Options") {
      Defaults.degenerate("scala.Option") should equal(None)
    }

    it("should be an empty list for List") {
      Defaults.degenerate("scala.collection.immutable.List") should equal(List())
    }

    it("should be an empty set for Set") {
      Defaults.degenerate("scala.collection.immutable.Set") should equal(Set())
    }

    it("should be an empty map for Map") {
      Defaults.degenerate("scala.collection.immutable.Map") should equal(Map())
    }

    it("should be an empty set for Seq") {
      Defaults.degenerate("scala.collection.immutable.Seq") should equal(Seq())
    }
  }

}