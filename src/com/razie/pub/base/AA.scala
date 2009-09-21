package com.razie.pub.base

import com.razie.pub.base.AttrAccess
import com.razie.pub.base.AttrAccess.Impl

/** simplify usage of AttrAccess */
object AA {
   def apply (s:AnyRef*):AA = {val x = new AA(); x.setAttr(s); x }
   def apply ():AA = new AA()
}

/** TODO will add some scala niceties for AttrAccess usage */
class AA extends AttrAccess.Impl {
	
}