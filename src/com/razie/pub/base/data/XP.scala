/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.data

//import com.razie.pub.base.data.RazElement

object XP {
	def forScala (xpath:String) = 
		new XPi[scala.xml.Elem] (new XP[scala.xml.Elem] (xpath), new ScalaDomXqSolver) 
}

class XPi [T] (val xp : XP[T], val ctx:XqSolver[T,Any]){
    def xpl (root:T) : List[T] = xp.xpl (ctx, root)

    def xpe (root:T) : T = xp.xpe (ctx, root)
     
    // TODO i can pre-filter the non-attribute elements
     def xpa (root:T) : String = xp.xpa (ctx, root)
}

/** a simple resolver for x path like stuff 
* can resolve the following expressions
* 
* /a/b/c
* /a/b/@c
* /a/b[cond]/...
* /a/{assoc}b[cond]/...
* 
* It differs from a classic xpath only by having the {assoc} option. Useful when 
* navigating models that use assocations as well as composition. Using 
* "/a/{assoc}b" means that it will use association {assoc} to find the b starting 
* from a...
* 
* TODO the type system here is all gefuckt...need better understanding of variance in scala. 
* See this http://www.nabble.com/X-String--is-not-a-subtype-of-X-AnyRef--td23428970.html
* 
* Example usage: 
* <ul>
* <li> on Strings: new XP("/root").xpl(new StringXqSolver, "/root")
* <li> on scala xml: new XP[scala.xml.Elem] ("/root").xpl(new ScalaDomXqSolver, root) 
* </ul>
*/
class XP[T] (val expr:String){

   // list of parsed elements
   val elements =  
      for (val e <- (expr split "/").filter(_!="")) 
    	  yield new XqElement[T] (e)

      /** solve this path starting with the root and the given solving strategy */
      def xpl (ctx:XqSolver[T,Any], root:T) : List[T] = 
         for (e <- elements.foldLeft (List ((root,List(root).asInstanceOf[Any])) ) ( (x,y) => y.solve(ctx, x).asInstanceOf[List[(T,Any)]]) ) 
           yield e._1

      /** */
      def xpe (ctx:XqSolver[T,Any], root:T) : T = xpl (ctx, root).first
      
     // TODO i can pre-filter the non-attribute elements
      def xpa (ctx:XqSolver[T,Any], root:T) : String = 
         ctx.getAttr(
           (for (e <- elements.filter(_.attr!="@").foldLeft(List ((root,List(root).asInstanceOf[Any])) ) ( (x,y) => y.solve(ctx, x).asInstanceOf[List[(T,Any)]]) ) 
             yield e._1
          ).first, elements.last.name)
}

/** an element in the path */
protected class XqElement[T] (val expr:String){
   val parser = """(\{.*\})*([@])*(\w+)(\[.*\])*""".r
   val parser(assoc, attr, name, scond) = expr
   val cond = XqCondFactory.make (scond)

   // from res get the path and then reduce with condition
    // looking for elements
   def solve (ctx:XqSolver[T,Any], res:List[(T,Any)]):List[(T,Any)] = 
      for (e <- res; x <- ctx.reduce (ctx.getNext(e,name,assoc),cond)) 
         yield x
   
     // looking for an attribute
   def solvea (ctx:XqSolver[Any,Any], res:Any) : String = 
     ctx.getAttr(res,name)
}

/** overwrite this if you want other scriptables for conditions... */
object XqCondFactory {
   def make (s:String) = new XqCond(s)
}

/** the condition of anelement in the path */
class XqCond (val expr:String) {
   def passes (o:Any) : Boolean = {
     // TODO implement something
         true    
   }
}

/** the strategy to break down the input based on the current path element. The solving algorithm is: apply current sub-path to current sub-nodes, get the results and RESTs. Filter by conditions and recurse.  */
trait XqSolver[+A,+B] {
   /** get the next list of nodes at the current position in the path. For each, return a tuple with the respective value and the REST to continue solving */
   def getNext[T>:A,U>:B](o:(T,U),tag:String, assoc:String) : List[(T,U)]

   /** get the value of an attribute from the given node */
   def getAttr[T>:A](o:T,attr:String) : String

   /** reduce the current set of possible nodes based on the given condition */
   def reduce[T>:A,U>:B](o:List[(T,U)],cond:XqCond) : List[(T,U)]
}

/** this resolved strings with the /x/y/z format */
class StringXqSolver extends XqSolver[String,List[String]] {
  override def getNext[T>:String,U>:List[String]](o:(T,U),tag:String, assoc:String) : List[(T,U)]={
    val pat = """/*(\w+)(/.*)*""".r
    val pat(result,next) = o._2.asInstanceOf[List[String]].first
    List((result,List(next)))
   } 
  
  override def getAttr[T>:String] (o:T,attr:String) : String = 
    throw new UnsupportedOperationException("can't get attrs from a str...")

  // TODO implement condition
  override def reduce[T>:String,U>:List[String]] (o:List[(T,U)],cond:XqCond) : List[(T,U)] = 
    o
}

/** this resolves dom trees*/
//class DomXqSolver extends XqSolver[RazElement,List[RazElement]] {
//   override def getNext[T>:RazElement,U>:List[RazElement]] (o:(T,U),tag:String, assoc:String) : List[(T,U)]={
//      val n = o._1.asInstanceOf[RazElement] xpl tag
//      for (e <- n) yield (e,e.asInstanceOf[U])
//   } 
//
//   override def getAttr[T>:RazElement] (o:T,attr:String) : String = o.asInstanceOf[RazElement] a attr
//   override def reduce[T>:RazElement,U>:List[RazElement]] (o:List[(T,U)],cond:XqCond) : List[(T,U)] = o.asInstanceOf[List[(T,U)]]
//}

/** this resolves dom trees*/
class ScalaDomXqSolver extends XqSolver[scala.xml.Elem,List[scala.xml.Elem]] {
   override def getNext[T>:scala.xml.Elem,U>:List[scala.xml.Elem]] (o:(T,U),tag:String, assoc:String) : List[(T,U)]={
      println ("--------------- getNext tag=" + tag + " o=" + o)
      val val1 = o._2.asInstanceOf[List[scala.xml.Elem]].filter(_.label==tag)
      println ("+++++++++++++++ val1 "+val1)
      val ret = o._2.asInstanceOf[List[scala.xml.Elem]].filter(_.label==tag).map(x => { val t = (x.asInstanceOf[T],children(x).toList.asInstanceOf[U]); println ("t=" + t); t}).toList
      println("================ " + ret)
      ret
   } 

   /** TODO can't i optimize this? how do i inline it at least? */
   private def children (e:scala.xml.Elem) = e.child.filter(_.isInstanceOf[scala.xml.Elem])
    
   override def getAttr[T>:scala.xml.Elem] (o:T,attr:String) : String = 
	   (o.asInstanceOf[scala.xml.Elem] \ ("@"+attr)) text

	   // TODO implement conditions
   override def reduce[T>:scala.xml.Elem,U>:List[scala.xml.Elem]] (o:List[(T,U)],cond:XqCond) : List[(T,U)] = 
	   o.asInstanceOf[List[(T,U)]]
}

/** reflection resolved for java/scala objects */
//class BeanXqSolver extends XqSolver[AnyRef,List[RazElement]] {
//   override def getNext[T>:AnyRef,U>:List[AnyRef]] (o:(T,U),tag:String, assoc:String) : List[(T,U)]={
//      val n = o._1
//      n.getClass
//      for (e <- n) yield (e,e.asInstanceOf[U])
//   }
//
//   override def getAttr[T>:AnyRef] (o:T,attr:String) : String = o.asInstanceOf[RazElement] a attr
//   override def reduce[T>:AnyRef,U>:List[AnyRef]] (o:List[(T,U)],cond:XqCond) : List[(T,U)] = o.asInstanceOf[List[(T,U)]]
//}

private object MyMain extends Application {
   List("root") == new XP[String]("/root").xpl(new StringXqSolver, "/root")
}
