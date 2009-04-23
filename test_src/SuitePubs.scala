
import org.scalatest.junit._
import org.scalatest.SuperSuite

/** TODO testing the RazElement */
class SuitePubsScala extends SuperSuite (
  List (
    new com.razie.pub.data.test.TestRazElement,
    new com.razie.pub.test.TestAssetMgrTrait
  )
)

/** TODO this is sooooooooooooo messed up... */
class SuitePubs (s:String) extends junit.framework.TestSuite(classOf[Nada]) {
  
  // this is where you list the tests...
   addTestSuite(classOf[com.razie.pub.base.data.test.TripleIdxTest])
   addTestSuite(classOf[com.razie.pub.test.TestAssetMgrTrait])
   addTestSuite(classOf[com.razie.pub.data.test.TestRazElement])
   
   def test1() = 
     // don't touch this line
     addTest(new junit.framework.TestSuite(classOf[com.razie.pub.test.TestAssetMgrTrait]))
     
}

class Nada extends junit.framework.TestCase {
 def testNada : Unit =  {}
}