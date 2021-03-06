/*
    Copyright (C) 2013 Nicola L.C. Talbot
    www.dickimaw-books.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package com.dickimawbooks.texparserlib.latex.jmlr;

import java.io.IOException;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.latex.*;

public class JmlrAddr extends ControlSequence
{
   public JmlrAddr()
   {
      this("addr");
   }

   public JmlrAddr(String name)
   {
      super(name);
   }

   public Object clone()
   {
      return new JmlrAddr(getName());
   }

   public void process(TeXParser parser, TeXObjectList stack)
      throws IOException
   {
      TeXObject obj = stack.popStack(parser);

      Group grp;

      if (obj instanceof Group)
      {
         grp = (Group)obj;
      }
      else
      {
         grp = parser.getListener().createGroup();
         grp.add(obj);
      }

      grp.push(new TeXCsRef("itshape"));
      grp.push(new TeXCsRef("small"));
      grp.push(new TeXCsRef("mdseries"));

      stack.push(grp);
   }

   public void process(TeXParser parser)
      throws IOException
   {
      TeXObject obj = parser.popStack();

      Group grp;

      if (obj instanceof Group)
      {
         grp = (Group)obj;
      }
      else
      {
         grp = parser.getListener().createGroup();
         grp.add(obj);
      }

      grp.push(new TeXCsRef("itshape"));
      grp.push(new TeXCsRef("small"));
      grp.push(new TeXCsRef("mdseries"));

      parser.push(grp);
   }
}
