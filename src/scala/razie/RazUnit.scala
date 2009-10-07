package scala.razie

/** the scalatest stuff often lags behind bleading edge scala - use this simple idiotic implementation that actually does what i need :) */
class RazUnit extends RazUnitImpl /*extends JUnit3Suite*/ {
}

class RazUnitImpl {
	   var failed:Int=0
	   var succeeded:Int=0

	   def setUp = {}

	   def expect (x:Any) (f:Any) = {
	      val ff = f
	      if (x !=  ff) {
	         failed+=1
	         throw new RuntimeException ("expect failed: expected "+x+" and got instead "+ff)
	      }
	      else {
	         System.out.println ("EEEEEEEEEEEEEEEEEEEXPECTEDDDDDDDDDDDDDD was ok...")
	         succeeded+=1
	      }
	   }

	   def tearDown = {
	      Log.logThis ("TEST COMPLETED - succeeded="+succeeded + ", failed=" + failed)
	   }
	}
