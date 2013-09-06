import com.github.jimkinsey.builder.{Builder, Defaults}
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class DefaultsTests  extends FunSpec with ShouldMatchers {

  describe("The degenerate defaults") {

    it("should provide an empty string for Strings") {
      Defaults.degenerate("java.lang.String") should equal("")
    }

    it("should provide 0 for integers") {
      Defaults.degenerate("scala.Int") should equal(0)
    }

    it("should provide 0 for longs") {
      Defaults.degenerate("scala.Long") should equal(0L)
    }

    it("should provide 0.0 for floats") {
      Defaults.degenerate("scala.Float") should equal(0.0f)
    }

    it("should provide 0.0 for doubles") {
      Defaults.degenerate("scala.Double") should equal(0.0)
    }

    it("should provide false for booleans") {
      Defaults.degenerate("scala.Boolean") should equal(false)
    }

    it("should provide a space for chars") {
      Defaults.degenerate("scala.Char") should equal(' ')
    }

    it("should provide None for Options") {
      Defaults.degenerate("scala.Option") should equal(None)
    }

  }

}