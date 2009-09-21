
import org.scalatest.junit._
import org.scalatest.SuperSuite

/** TODO testing the RazElement */
class SuitePubs2Scala extends SuperSuite (
  List (
    new com.razie.pub.base.data.test.TestRazElementJava,
    new com.razie.pub.test.TestAssetMgrTrait
  )
)

/** TODO this is sooooooooooooo messed up... */
class SuitePubs2 () extends junit.framework.TestSuite(classOf[XNada2]) {
  
  // this is where you list the tests...
   addTestSuite(classOf[com.razie.pub.base.data.test.TripleIdxTest])
   addTestSuite(classOf[com.razie.pub.test.TestAssetMgrTrait])
   addTestSuite(classOf[com.razie.pub.base.data.test.TestRazElementJava])
   addTestSuite(classOf[com.razie.pub.base.data.test.TestRazElementScala])
   addTestSuite(classOf[com.razie.pub.base.data.test.TestXpString])
   addTestSuite(classOf[com.razie.pub.base.data.test.TestXpScala])
   
   def test1() = 
     // don't touch this line
     addTest(new junit.framework.TestSuite(classOf[com.razie.pub.test.TestAssetMgrTrait]))
     
}

class XNada2 extends junit.framework.TestCase {
 def testNada : Unit =  {}
}