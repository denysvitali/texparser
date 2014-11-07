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
package com.dickimawbooks.texparserlib.latex2latex;

import java.io.IOException;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.latex.*;

public class L2LMathCs extends MathCs
{
   public L2LMathCs()
   {
      super();
   }

   public L2LMathCs(String name)
   {
      super(name);
   }

   public Object clone()
   {
      return new L2LMathCs(getName());
   }

   public void process(TeXParser parser, TeXObjectList stack)
     throws IOException
   {
      char esc = parser.getEscChar();
      MathGroup list = new L2LMathGroup(true, esc+"(", esc+")");

      while (stack.size() > 0)
      {
         TeXObject object = stack.pop();

         if (object instanceof ControlSequence
          && ((ControlSequence)object).getName().equals(")"))
         {
            break;
         }

         list.add(object);
      }

      list.process(parser, stack);
   }

   public void process(TeXParser parser)
     throws IOException
   {
      char esc = parser.getEscChar();
      MathGroup list = new L2LMathGroup(true, esc+"(", esc+")");

      while (true)
      {
         TeXObject object = parser.pop();

         if (object instanceof ControlSequence
          && ((ControlSequence)object).getName().equals(")"))
         {
            break;
         }

         list.add(object);
      }

      list.process(parser);
   }

}
